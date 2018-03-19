package be.kdg.musicmaker.backend;

import be.kdg.musicmaker.MMAplication;
import be.kdg.musicmaker.security.CorsFilter;
import be.kdg.musicmaker.util.TokenGetter;
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

import java.util.LinkedList;

import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = MMAplication.class)
public class TokenTest {
    private TokenGetter tokenGetter;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private CorsFilter corsFilter;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                                        .apply(springSecurity())
                                        .addFilter(corsFilter).build();
        tokenGetter = TokenGetter.getInstance(this.mockMvc);
    }
    @Test
    public void getAdminTokenTest() {
        String accessToken = "";
        try {
            accessToken = tokenGetter.obtainAccessToken("user3@user.com", "user3");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("token:" + accessToken);
        assertTrue(accessToken.length() > 0);
    }
    @Test
    public void getTeacherTokenTest() {
        String accessToken = "";
        try {
            accessToken = tokenGetter.obtainAccessToken("user2@user.com", "user2");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("token:" + accessToken);
        assertTrue(accessToken.length() > 0);
    }
    @Test
    public void getStudentTokenTest() {
        String accessToken = "";
        try {
            accessToken = tokenGetter.obtainAccessToken("user@user.com", "user");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("token:" + accessToken);
        assertTrue(accessToken.length() > 0);
    }
}