package be.kdg.musicmaker.frontend;

import be.kdg.musicmaker.MMAplication;
import be.kdg.musicmaker.model.DTO.UserDTO;
import be.kdg.musicmaker.model.User;
import be.kdg.musicmaker.security.CorsFilter;
import be.kdg.musicmaker.user.UserService;
import be.kdg.musicmaker.util.UserNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = MMAplication.class)
public class RegisterUser {
    private UserDTO userDTO = new UserDTO("", "", "", "olivier.b@telenet.be", "oli");
    private ObjectMapper objectMapper = new ObjectMapper();
    String jsonString;

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
    }

    @Test
    public void register() throws UserNotFoundException {
        try {
            jsonString = objectMapper.writeValueAsString(userDTO);
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
        User user = userService.doesUserExist("olivier.b@telenet.be");
        assertNotNull(user);
    }

    @Test
    public void confirm() throws UserNotFoundException {
        try {
            //create token
            String token = UUID.randomUUID().toString();
            //add user to DB
            userDTO.setConfirmationToken(token);
            userService.createUser(userDTO);
            //do confirm request with token
            this.mockMvc.perform(get("/confirm").param("token", token))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
        User user = userService.doesUserExist(userDTO.getEmail());
        assertTrue(user.isEnabled());
    }
}
