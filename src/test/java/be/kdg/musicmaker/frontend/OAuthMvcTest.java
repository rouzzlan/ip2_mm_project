package be.kdg.musicmaker.frontend;

import be.kdg.musicmaker.MMAplication;
import be.kdg.musicmaker.security.CorsFilter;
import be.kdg.musicmaker.security.ResourceServerConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = MMAplication.class)
public class OAuthMvcTest {

    @Autowired
    private WebApplicationContext wac;

//    @Autowired
//    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private ResourceServerConfig resourceServerConfig;

    @Autowired
    private CorsFilter corsFilter;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilter(corsFilter).build();
    }
    private String obtainAccessToken(String username, String password) throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("username", username);
        params.add("password", password);

        ResultActions result
                = mockMvc.perform(post("/oauth/token").contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .params(params).header(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString("mmapp:mmapp".getBytes()))
                .with(httpBasic("mmapp", "mmapp"))).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        String resultString = result.andReturn().getResponse().getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }
    @Test
    public void givenInvalidRole_whenGetSecureRequest_thenForbidden() throws Exception {
        final String accessToken = obtainAccessToken("user3@user.com", "user3");
        System.out.println("token:" + accessToken);
        assertTrue(accessToken.length() > 0);
    }
}