package be.kdg.musicmaker.lesson;

import be.kdg.musicmaker.MMAplication;
import be.kdg.musicmaker.model.*;
import be.kdg.musicmaker.security.CorsFilter;
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

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = MMAplication.class)
public class TestLesson {
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
    public void getLessonAsAdmin() throws Exception {
        mockMvc.perform(get("/lesson")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin))
                .andExpect(status().isOk());
    }

    @Test
    public void getLessonAsTeacher() throws Exception {
        mockMvc.perform(get("/lesson")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Teacher))
                .andExpect(status().isOk());
    }

    @Test
    public void getLessonAsStudent() throws Exception {
        mockMvc.perform(get("/lesson")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Student))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getMyLessonAsAdmin() throws Exception {
        mockMvc.perform(get("/lesson/mine")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .param("userid", "3"))
                .andExpect(status().isOk());
    }

    @Test
    public void getMyLessonAsTeacher() throws Exception {
        mockMvc.perform(get("/lesson/mine")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Teacher)
                .param("userid", "2"))
                .andExpect(status().isOk());
    }

    @Test
    public void getMyLessonAsStudent() throws Exception {
        mockMvc.perform(get("/lesson/mine")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Student)
                .param("userid", "1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getMyLessonAsAnonymous() throws Exception {
        mockMvc.perform(get("/lesson/mine"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void createLessonAsAdmin() throws Exception {
        mockMvc.perform(post("/lesson/add")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(
                        new LessonDTO(50, 25.0, "open",
                                new Playlist(),
                                null,
                                new SeriesOfLessons()
                        )))).andExpect(status().isCreated());
    }

    @Test
    public void createLessonAsTeacher() throws Exception {
        mockMvc.perform(post("/lesson/add")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Teacher)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(
                        new LessonDTO(50, 25.0, "open",
                                new Playlist(),
                                null,
                                new SeriesOfLessons()
                        )))).andExpect(status().isCreated());
    }

    @Test
    public void createLessonAsStudent() throws Exception {
        mockMvc.perform(post("/lesson/add")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Student)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(
                        new LessonDTO(50, 25.0, "open",
                                new Playlist(),
                                null,
                                new SeriesOfLessons()
                        )))).andExpect(status().isForbidden());
    }

    @Test
    public void updateLessonAsAdmin() throws Exception {
        List<Lesson> lessons = lessonService.getLessons();
        Lesson lesson = lessons.get(0);
        assertEquals(90, lesson.getPrice(), 0);

        mockMvc.perform(put("/lesson/update")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", lesson.getId().toString())
                .content(objectMapper.writeValueAsString(new LessonDTO(50, 35.0, "open",
                        new Playlist(),
                        null,
                        new SeriesOfLessons()
                ))))
                .andDo(print())
                .andExpect(status().isContinue());

        lessons = lessonService.getLessons();
        lesson = lessons.get(0);
        assertEquals(35.0, lesson.getPrice(), 0);
    }

    @Test
    public void updateLessonAsTeacher() throws Exception {
        List<Lesson> lessons = lessonService.getLessons();
        Lesson lesson = lessons.get(1);
        assertEquals(90, lesson.getPrice(), 0);

        mockMvc.perform(put("/lesson/update")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Teacher)
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", lesson.getId().toString())
                .content(objectMapper.writeValueAsString(new LessonDTO(50, 35.0, "open",
                        new Playlist(),
                        null,
                        new SeriesOfLessons()
                ))))
                .andDo(print())
                .andExpect(status().isContinue());

        lessons = lessonService.getLessons();
        lesson = lessons.get(1);
        assertEquals(35.0, lesson.getPrice(), 0);
    }

    @Test
    public void updateLessonAsStudent() throws Exception {
        List<Lesson> lessons = lessonService.getLessons();
        Lesson lesson = lessons.get(2);
        assertEquals(90, lesson.getPrice(), 0);

        mockMvc.perform(put("/lesson/update")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Student)
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", lesson.getId().toString())
                .content(objectMapper.writeValueAsString(new LessonDTO(50, 35.0, "open",
                        new Playlist(),
                        null,
                        new SeriesOfLessons()
                ))))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteLessonAsAdmin() throws Exception {
        List<Lesson> lessons = lessonService.getLessons();
        Lesson lesson = lessons.get(2);

        mockMvc.perform(delete("/lesson/delete")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", lesson.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void deleteLessonAsTeacher() throws Exception {
        List<Lesson> lessons = lessonService.getLessons();
        Lesson lesson = lessons.get(2);

        mockMvc.perform(delete("/lesson/delete")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Teacher)
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", lesson.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void deleteLessonAsStudent() throws Exception {
        List<Lesson> lessons = lessonService.getLessons();
        Lesson lesson = lessons.get(2);

        mockMvc.perform(delete("/lesson/delete")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Student)
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", lesson.getId().toString()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
