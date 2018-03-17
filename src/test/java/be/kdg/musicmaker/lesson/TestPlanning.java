package be.kdg.musicmaker.lesson;

import be.kdg.musicmaker.MMAplication;
import be.kdg.musicmaker.security.CorsFilter;
import be.kdg.musicmaker.util.TokenGetter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = MMAplication.class)
public class TestPlanning {
    private static String ACCESS_TOKEN_Admin = "";
    private static String ACCESS_TOKEN_Teacher = "";
    private ObjectMapper objectMapper = new ObjectMapper();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyy HH:mm");

    @Autowired
    LessonService lessonService;
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

        ACCESS_TOKEN_Admin = tokenGetter.obtainAccessToken("user3@user.com", "user3");
        ACCESS_TOKEN_Teacher = tokenGetter.obtainAccessToken("user2@user.com", "user2");
    }

    @Test
    public void accessFreeMomentsAsAdmin() throws Exception {
        mockMvc.perform(get("/lesson/planning/free")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin))
                .andExpect(status().isOk());
    }

    @Test
    public void accessFreeMomentsAsTeacher() throws Exception {
        mockMvc.perform(get("/lesson/planning/free")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Teacher))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getFreeMomentsAsAdmin() throws Exception {
        mockMvc.perform(get("/lesson/planning/free")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .param("userid", "2")
                .param("date", LocalDateTime.now().toString()))
                .andExpect(status().isOk());
        System.out.println(LocalDateTime.parse("10/08/1990 00:00", formatter).toString());
    }
}
