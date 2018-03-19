package be.kdg.musicmaker.CRUD.band;

import be.kdg.musicmaker.MMAplication;
import be.kdg.musicmaker.band.dto.BandDTO;
import be.kdg.musicmaker.band.BandNotFoundException;
import be.kdg.musicmaker.band.BandService;
import be.kdg.musicmaker.security.CorsFilter;
import be.kdg.musicmaker.user.UserNotFoundException;
import be.kdg.musicmaker.util.TokenGetter;
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
public class BandTest {
    private String member1;
    private String member2;
    private String owner;
    private BandDTO bandDTO;
    private static String ACCESS_TOKEN_Admin = "";
    private static String ACCESS_TOKEN_Student = "";
    private static String ACCESS_TOKEN_Teacher = "";
    private ObjectMapper objectMapper = new ObjectMapper();
    private TokenGetter tokenGetter;

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
            tokenGetter = TokenGetter.getInstance(this.mockMvc);
            ACCESS_TOKEN_Admin = tokenGetter.obtainAccessToken("user3@user.com", "user3");
            ACCESS_TOKEN_Student = tokenGetter.obtainAccessToken("user@user.com", "user");
            ACCESS_TOKEN_Teacher = tokenGetter.obtainAccessToken("user2@user.com", "user2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //CREATE

    @Test
    public void createBandByAdmin() throws BandNotFoundException, UserNotFoundException {
        String jsonString = "";
        try {
            member1 = "user@user.com";
            member2 = "user2@user.com";
            owner = "user3@user.com";
            bandDTO = new BandDTO("The Y-Nuts", owner, Arrays.asList(member1, member2));
            jsonString = objectMapper.writeValueAsString(bandDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        try {
            this.mockMvc.perform(post("/band/add").header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                    .contentType(MediaType.APPLICATION_JSON).content(jsonString))
                    .andDo(print())
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //READ
    //TODO implement

    //UPDATE
    //TODO implement

    //DELETE
    //TODO implement
}

