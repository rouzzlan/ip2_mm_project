package be.kdg.musicmaker.event;

import be.kdg.musicmaker.MMAplication;
import be.kdg.musicmaker.model.Event;
import be.kdg.musicmaker.security.CorsFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.LinkedList;

import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = MMAplication.class)
public class CreateEventTest {
    private LocalDateTime localDateTime;
    private EventDTO eventDTO;
    private static String ACCESS_TOKEN_Admin = "";
    private static String ACCESS_TOKEN_Student = "";
    private static String ACCESS_TOKEN_Teacher = "";
    private ObjectMapper objectMapper;

    @Autowired
    EventService eventService;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private CorsFilter corsFilter;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity())
                .addFilter(corsFilter)
                .build();

        try {

            /**
             * Specifiek om Time Objecten te parsen volgends ISO formaat
             */
            objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            localDateTime = LocalDateTime.now();

            //TODO re-add dateTime

            eventDTO = new EventDTO("testEvent", localDateTime.toString(), "KdG", "The X-Nuts");
            ACCESS_TOKEN_Admin = obtainAccesToken("user3@user.com", "user3");
            ACCESS_TOKEN_Student = obtainAccesToken("user@user.com", "user");
            ACCESS_TOKEN_Teacher = obtainAccesToken("user2@user.com", "user2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // todo: test faalt
    @Test
    public void createEventByAdmin() throws EventNotFoundException {
//        String jsonString = "";
//        try {
//            jsonString = objectMapper.writeValueAsString(eventDTO);
//            System.out.println(jsonString);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            this.mockMvc.perform(post("/addevent")
//                    .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
//                    .contentType(MediaType.APPLICATION_JSON).content(jsonString)).andDo(print())
//                    .andExpect(status().isCreated());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        Event event = eventService.doesEventExist("testEvent");
//        assertNotNull(event);
    }

    private String obtainAccesToken(String username, String password) throws Exception {
        LinkedList<BasicNameValuePair> componentList = new LinkedList<>();
        componentList.add(new BasicNameValuePair("username", username));
        componentList.add(new BasicNameValuePair("password", password));
        componentList.add(new BasicNameValuePair("grant_type", "password"));

        ResultActions result
                = mockMvc.perform(post("/oauth/token")
                .content(EntityUtils.toString(new UrlEncodedFormEntity(componentList)))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .with(httpBasic("mmapp", "mmapp")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        String resultString = result.andReturn().getResponse().getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }
}
