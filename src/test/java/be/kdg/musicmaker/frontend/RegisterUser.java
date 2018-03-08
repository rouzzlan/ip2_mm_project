package be.kdg.musicmaker.frontend;

import be.kdg.musicmaker.MMAplication;
import be.kdg.musicmaker.user.UserDTO;
import be.kdg.musicmaker.model.User;
import be.kdg.musicmaker.security.CorsFilter;
import be.kdg.musicmaker.user.UserRepository;
import be.kdg.musicmaker.user.UserService;
import be.kdg.musicmaker.user.UserNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
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

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = MMAplication.class)
public class RegisterUser {

    UserDTO userDTO = new UserDTO("", "", "", "musicmakersapp@gmail.com", "mmapp");
    private ObjectMapper objectMapper = new ObjectMapper();
    String jsonString;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository repository;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private CorsFilter corsFilter;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity())
                .addFilter(corsFilter).build();
    }

    @Test
    public void register() throws UserNotFoundException {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("email", userDTO.getEmail());
            jsonObj.put("password", userDTO.getPassword());
            jsonString = objectMapper.writeValueAsString(jsonObj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            this.mockMvc.perform(post("/register")
                    .contentType(MediaType.APPLICATION_JSON).content(jsonString)).andDo(print())
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            e.printStackTrace();
        }
        User user = userService.doesUserExist("musicmakersapp@gmail.com");
        assertNotNull(user);
        assertFalse(user.isEnabled());
        assertNotNull(user.getConfirmationToken());
    }

    @Test
    public void confirm() throws Exception {
        String token;
        try {
            //get registrated user from DB
            User user = userService.doesUserExist(userDTO.getEmail());
            token = user.getConfirmationToken();

            //do confirm request with token
            this.mockMvc.perform(get("/confirm").param("token", token))
                    .andExpect(status().isOk());

            user = userService.findByConfirmationToken(token);
            assertTrue(user.isEnabled());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
