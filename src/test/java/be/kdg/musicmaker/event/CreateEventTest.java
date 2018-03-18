package be.kdg.musicmaker.event;

import be.kdg.musicmaker.MMAplication;
import be.kdg.musicmaker.model.Event;
import be.kdg.musicmaker.security.CorsFilter;
import be.kdg.musicmaker.util.TokenGetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    private TokenGetter tokenGetter;
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
            tokenGetter = TokenGetter.getInstance(this.mockMvc);
            objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            localDateTime = LocalDateTime.now();

            //TODO re-add dateTime

            eventDTO = new EventDTO("testEvent", LocalDateTime.now().toString(), "KdG", "The X-Nuts");
            ACCESS_TOKEN_Admin = tokenGetter.obtainAccessToken("user3@user.com", "user3");
            ACCESS_TOKEN_Student = tokenGetter.obtainAccessToken("user@user.com", "user");
            ACCESS_TOKEN_Teacher = tokenGetter.obtainAccessToken("user2@user.com", "user2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // todo: test faalt
    @Test
    public void createEventByAdmin() throws EventNotFoundException {
        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(eventDTO);
            System.out.println(jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        try {
            this.mockMvc.perform(post("/addevent")
                    .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                    .contentType(MediaType.APPLICATION_JSON).content(jsonString)).andDo(print())
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Event event = eventService.doesEventExist("testEvent");
        assertNotNull(event);
    }
}