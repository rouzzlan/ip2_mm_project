package be.kdg.musicmaker.band;

import be.kdg.musicmaker.MMAplication;
import be.kdg.musicmaker.model.DTO.BandDTO;
import be.kdg.musicmaker.model.User;
import be.kdg.musicmaker.security.CorsFilter;
import be.kdg.musicmaker.util.BandNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.LinkedList;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = MMAplication.class)
public class CreateBandTest {
    private User student1 = new User("user1", "user1", "user", "1", "user1@kdg.be");
    private User student2 = new User("user2", "user2", "user", "2", "user2@kdg.be");
    private BandDTO bandDTO = new BandDTO("The X-Nuts", new User("user3", "user3", "user", "3", "user3@kdg.be"), Arrays.asList(student1, student2));
    private static String ACCES_TOKEN_Admin = "";
    private static String ACCESS_TOKEN_Student = "";
    private static String ACCESS_TOKEN_Teacher = "";
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    BandService bandService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CorsFilter corsFilter;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).apply(springSecurity())
                .addFilter(corsFilter).build();
        try {
            ACCES_TOKEN_Admin = obtainAccesToken("user3@user.com", "user3");
            ACCESS_TOKEN_Student = obtainAccesToken("user@user.com", "user");
            ACCESS_TOKEN_Teacher = obtainAccesToken("user2@student.com", "user2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createBandByAdmin() throws BandNotFoundException {
        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(bandDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        try {
            this.mockMvc.perform(post("/addBand").header("Authorization", "Bearer " + ACCES_TOKEN_Admin)
                    .contentType(MediaType.APPLICATION_JSON).content(jsonString))
                    .andDo(print())
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String obtainAccesToken(String username, String password) throws Exception {
        LinkedList<BasicNameValuePair> componentList = new LinkedList<>();
        componentList.add(new BasicNameValuePair("grant type", "password"));
        componentList.add(new BasicNameValuePair("username", username));
        componentList.add(new BasicNameValuePair("password", password));

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

