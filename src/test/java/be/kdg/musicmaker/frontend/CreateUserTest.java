package be.kdg.musicmaker.frontend;


import be.kdg.musicmaker.MMAplication;
import be.kdg.musicmaker.model.DTO.UserDTO;
import be.kdg.musicmaker.model.User;
import be.kdg.musicmaker.security.CorsFilter;
import be.kdg.musicmaker.user.UserService;
import be.kdg.musicmaker.util.UserNotFoundException;
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
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = MMAplication.class)
public class CreateUserTest {
    private UserDTO leerlingDTO = new UserDTO("testLeerling1", "kim", "vermuilen", "kim.vermuilen@mm.app", "kim", Arrays.asList("ROLE_LEERLING"));
    private static String ACCESS_TOKEN_Admin = "";
    private static String ACCESS_TOKEN_Student = "";
    private static String ACCESS_TOKEN_Teacher = "";
    private ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    UserService userService;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private CorsFilter corsFilter;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity())
                .addFilter(corsFilter).build();
        try {
            ACCESS_TOKEN_Admin = obtainAccessToken("user3@user.com", "user3");
            ACCESS_TOKEN_Student = obtainAccessToken("user@user.com", "user");
            ACCESS_TOKEN_Teacher = obtainAccessToken("user2@user.com", "user2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createStudentByAdmin() throws UserNotFoundException {
        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(leerlingDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            this.mockMvc.perform(post("/adduser").header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                    .contentType(MediaType.APPLICATION_JSON).content(jsonString)).andDo(print())
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            e.printStackTrace();
        }
        User leerling = userService.doesUserExist("kim.vermuilen@mm.app");
        assertNotNull(leerling);
    }
    @Test
    public void createStudentByTeacher() throws UserNotFoundException {
        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(leerlingDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            this.mockMvc.perform(post("/adduser").header("Authorization", "Bearer " + ACCESS_TOKEN_Teacher)
                    .contentType(MediaType.APPLICATION_JSON).content(jsonString)).andDo(print())
                    .andExpect(status().isForbidden());
        } catch (Exception e) {
            e.printStackTrace();
        }
        User leerling = userService.doesUserExist("kim.vermuilen@mm.app");
        assertNotNull(leerling);
    }

    @Test
    public void createStudentByStudent() throws Exception {
        UserDTO leerlingDTO = new UserDTO("testLeerling1", "kim", "vermuilen", "kim.vermuilen@mm.app", "kim", Arrays.asList("ROLE_LEERLING"));
        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(leerlingDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            this.mockMvc.perform(post("/adduser").header("Authorization", "Bearer " + ACCESS_TOKEN_Student)
                    .contentType(MediaType.APPLICATION_JSON).content(jsonString)).andDo(print())
                    .andExpect(status().isForbidden());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getRoles() throws Exception {
        ResultActions result = this.mockMvc.perform(get("/getRoles").header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        List roles = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), List.class);
        assertTrue(roles.size() == userService.getRoles().size());
    }



    private String obtainAccessToken(String username, String password) throws Exception {

        LinkedList<BasicNameValuePair> componentList = new LinkedList<>();
        componentList.add(new BasicNameValuePair("grant_type", "password"));
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
