package be.kdg.musicmaker.CRUD.user;

import be.kdg.musicmaker.MMAplication;
import be.kdg.musicmaker.model.User;
import be.kdg.musicmaker.security.CorsFilter;
import be.kdg.musicmaker.user.UserNotFoundException;
import be.kdg.musicmaker.user.UserService;
import be.kdg.musicmaker.user.dto.UserDTO;
import be.kdg.musicmaker.util.TokenGetter;
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

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = MMAplication.class)
public class UserTest {
    private static String ACCESS_TOKEN_Admin = "";
    private static String ACCESS_TOKEN_Student = "";
    private static String ACCESS_TOKEN_Teacher = "";
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    UserService userService;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private CorsFilter corsFilter;

    private MockMvc mockMvc;
    private TokenGetter tokenGetter;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .apply(springSecurity())
                .addFilter(corsFilter).build();
        tokenGetter = TokenGetter.getInstance(mockMvc);

        ACCESS_TOKEN_Student = tokenGetter.obtainAccessToken("user@user.com", "user");
        ACCESS_TOKEN_Teacher = tokenGetter.obtainAccessToken("user2@user.com", "user2");
        ACCESS_TOKEN_Admin = tokenGetter.obtainAccessToken("user3@user.com", "user3");
    }

    //CREATE

    @Test
    public void CreateUserAsAdmin() throws Exception {
        UserDTO userDTO = new UserDTO("testLeerling", "kim", "vermuilen", "kim.vermuilen@mm.app", "kim", Arrays.asList("ROLE_LEERLING"));
        mockMvc.perform(post("/user/add")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userDTO)
                        )).andExpect(status().isCreated());
        User user = userService.getUser("kim.vermuilen@mm.app");
        userService.deleteUser(user);
    }

    @Test
    public void CreateUserAsTeacher() throws Exception {
        UserDTO userDTO = new UserDTO("testLeerling", "kim", "vermuilen", "kim.vermuilen@mm.app", "kim", Arrays.asList("ROLE_LEERLING"));
        mockMvc.perform(post("/user/add")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Teacher)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userDTO)
                )).andExpect(status().isCreated());
        User user = userService.getUser("kim.vermuilen@mm.app");
        userService.deleteUser(user);
    }

    @Test
    public void CreateUserAsStudent() throws Exception {
        UserDTO userDTO = new UserDTO("testLeerling", "kim", "vermuilen", "kim.vermuilen@mm.app", "kim", Arrays.asList("ROLE_LEERLING"));
        mockMvc.perform(post("/user/add")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Student)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userDTO)
                )).andExpect(status().isForbidden());
    }

    //READ

    @Test
    public void GetUserAsAdmin() throws Exception {
        mockMvc.perform(get("/user/get")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin))
                .andExpect(status().isOk());
        mockMvc.perform(get("/user/id/1")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin))
                .andExpect(status().isOk());
        mockMvc.perform(get("/user/email/user@user.com")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin))
                .andExpect(status().isOk());
    }

    @Test
    public void GetUserAsTeacher() throws Exception {
        mockMvc.perform(get("/user/get")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Teacher))
                .andExpect(status().isOk());
        mockMvc.perform(get("/user/id/1")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Teacher))
                .andExpect(status().isOk());
        mockMvc.perform(get("/user/email/user@user.com")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Teacher))
                .andExpect(status().isOk());
    }

    @Test
    public void GetUserAsStudent() throws Exception {
        mockMvc.perform(get("/user/get")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Student))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/user/id/1")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Student))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/user/email/user@user.com")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Student))
                .andExpect(status().isForbidden());
    }

    //UPDATE

    @Test
    public void UpdateUserAsAdmin() throws Exception {
        UserDTO original = new UserDTO("user", "user", "user", "user@user.com","user",  Arrays.asList("ROLE_LEERLING"));
        UserDTO userDTO = new UserDTO("update", "user", "user",  "user@user.com","user", Arrays.asList("ROLE_LEERLING"));

        User user = userService.getUser((long)1);
        assertEquals("user", user.getUsername());

        mockMvc.perform(put("/user/update")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andDo(print())
                .andExpect(status().isOk());

        user = userService.getUser((long)1);
        assertEquals("update", user.getUsername());
        userService.updateUser(original);
        user = userService.getUser((long)1);
        assertEquals("user", user.getUsername());
    }

    @Test
    public void UpdateUserAsTeacher() throws Exception {
        UserDTO original = new UserDTO("user", "user", "user", "user@user.com","user",  Arrays.asList("ROLE_LEERLING"));
        UserDTO userDTO = new UserDTO("update", "user", "user",  "user@user.com","user", Arrays.asList("ROLE_LEERLING"));

        User user = userService.getUser((long)1);
        assertEquals("user", user.getUsername());

        mockMvc.perform(put("/user/update")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Teacher)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andDo(print())
                .andExpect(status().isOk());

        user = userService.getUser((long)1);
        assertEquals("update", user.getUsername());
        userService.updateUser(original);
        user = userService.getUser((long)1);
        assertEquals("user", user.getUsername());
    }

    @Test
    public void UpdateUserAsStudent() throws Exception {
        UserDTO original = new UserDTO("user", "user", "user", "user@user.com","user",  Arrays.asList("ROLE_LEERLING"));
        UserDTO userDTO = new UserDTO("update", "user", "user",  "user@user.com","user", Arrays.asList("ROLE_LEERLING"));

        User user = userService.getUser((long)1);
        assertEquals("user", user.getUsername());

        mockMvc.perform(put("/user/update")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Student)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andDo(print())
                .andExpect(status().isOk());

        user = userService.getUser((long)1);
        assertEquals("update", user.getUsername());
        userService.updateUser(original);
        user = userService.getUser((long)1);
        assertEquals("user", user.getUsername());
    }

    //DELETE

    @Test
    public void DeleteUserAsAdmin() throws Exception {
        UserDTO userDTO = new UserDTO("user5", "user5", "user5", "user5@user.com","user5", Arrays.asList("ROLE_LEERLING"));
        User user = userService.getUser("user5@user.com");

        mockMvc.perform(delete("/user/delete")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .contentType(MediaType.APPLICATION_JSON)
                .param("email", user.getEmail()))
                .andDo(print())
                .andExpect(status().isOk());

        try {
            user = userService.getUser("user5@user.com");
        } catch (UserNotFoundException e) {
            user = null;
        }
        assertNull(user);
        userService.createUser(userDTO);
        assertNotNull(userService.getUser("user5@user.com"));
    }

    @Test
    public void DeleteUserAsTeacher() throws Exception {
        UserDTO userDTO = new UserDTO("user5", "user5", "user5", "user5@user.com","user5", Arrays.asList("ROLE_LEERLING"));
        User user = userService.getUser("user5@user.com");

        mockMvc.perform(delete("/user/delete")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Teacher)
                .contentType(MediaType.APPLICATION_JSON)
                .param("email", user.getEmail()))
                .andDo(print())
                .andExpect(status().isOk());

        try {
            user = userService.getUser("user5@user.com");
        } catch (UserNotFoundException e) {
            user = null;
        }
        assertNull(user);
        userService.createUser(userDTO);
        assertNotNull(userService.getUser("user5@user.com"));
    }

    @Test
    public void DeleteUserAsStudent() throws Exception {
        UserDTO userDTO = new UserDTO("user5", "user5", "user5", "user5@user.com","user5", Arrays.asList("ROLE_LEERLING"));
        User user = userService.getUser("user5@user.com");

        mockMvc.perform(delete("/user/delete")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Student)
                .contentType(MediaType.APPLICATION_JSON)
                .param("email", user.getEmail()))
                .andDo(print())
                .andExpect(status().isForbidden());

        user = userService.getUser("user5@user.com");
        assertNotNull(user);
    }
}

