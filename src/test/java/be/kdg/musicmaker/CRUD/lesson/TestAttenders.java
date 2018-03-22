package be.kdg.musicmaker.CRUD.lesson;

import be.kdg.musicmaker.MMAplication;
import be.kdg.musicmaker.lesson.LessonService;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = MMAplication.class)
public class TestAttenders {
    private static String ACCESS_TOKEN_Admin = "";
    private static String ACCESS_TOKEN_Student = "";
    private static String ACCESS_TOKEN_Teacher = "";
    private ObjectMapper objectMapper = new ObjectMapper();

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
        ACCESS_TOKEN_Student = tokenGetter.obtainAccessToken("user@user.com", "user");
        ACCESS_TOKEN_Teacher = tokenGetter.obtainAccessToken("user2@user.com", "user2");
    }

    @Test
    public void addStudentToLessonAsAdmin() throws Exception {
        mockMvc.perform(put("/lesson/student/add")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .param("email", "user@user.com")
                .param("role", "leerling")
                .param("lessonid", "3"))
                .andExpect(status().isOk());
    }

    @Test
    public void addStudentToLessonAsTeacher() throws Exception {
        mockMvc.perform(put("/lesson/student/add")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Teacher)
                .param("email", "user5@user.com")
                .param("role", "leerling")
                .param("lessonid", "7"))
                .andExpect(status().isOk());
    }

    @Test
    public void addStudentToLessonAsStudent() throws Exception {
        mockMvc.perform(put("/lesson/student/add")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Student))
                .andExpect(status().isForbidden());
    }
}
