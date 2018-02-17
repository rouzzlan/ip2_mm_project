package be.kdg.musicmaker.unittests.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestUserController {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getUsersJsonAnonymous() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/getusersJson")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }


}

//public class UserController {
//
//    @Test
//    public void getUsersJsonLeerling() throws Exception {
//        String token = mockMvc.perform(MockMvcRequestBuilders.post("/oauth/token")
//        .accept(MediaType.APPLICATION_JSON)
//        .);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/getusersJson").accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isUnauthorized());
//    }
//}
