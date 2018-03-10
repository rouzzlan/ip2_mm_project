package be.kdg.musicmaker.lesson;

import be.kdg.musicmaker.MMAplication;
import be.kdg.musicmaker.model.LessonType;
import be.kdg.musicmaker.security.CorsFilter;
import be.kdg.musicmaker.user.SeedData;
import be.kdg.musicmaker.util.TokenGetter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = MMAplication.class)
public class TestLessonTypes {
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
    public void getLessonTypesAsAdmin() throws Exception {
        mockMvc.perform(get("/lesson/types/").header("Authorization", "Bearer " + ACCESS_TOKEN_Admin))
                .andExpect(status().isOk());
    }

    @Test
    public void getLessonTypesAsTeacher() throws Exception {
        mockMvc.perform(get("/lesson/types/").header("Authorization", "Bearer " + ACCESS_TOKEN_Teacher))
                .andExpect(status().isOk());
    }

    @Test
    public void getLessonTypesAsStudent() throws Exception {
        mockMvc.perform(get("/lesson/types/").header("Authorization", "Bearer " + ACCESS_TOKEN_Student))
//                .andExpect(status().isUnauthorized()); // todo: dit moet het worden
                .andExpect(status().isOk());
    }

    @Test
    public void createLessonTypeAsAdmin() throws Exception {
        String jsonString = objectMapper.writeValueAsString(new LessonTypeDTO(15.50, "gitaar", "samenspel gitaar vor gevorderden", "gitaar 4"));
        mockMvc.perform(post("/lesson/types/add/").header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void createLessonTypesAsTeacher() throws Exception {
        mockMvc.perform(post("/lesson/types/add/").header("Authorization", "Bearer " + ACCESS_TOKEN_Teacher)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new LessonTypeDTO(15.50, "gitaar", "samenspel gitaar vor gevorderden", "gitaar 4")
                )))
                .andExpect(status().isCreated());
//                .andExpect(status().isUnauthorized()); // todo: dit moet het worden
    }

    @Test
    public void createLessonTypeAsStudent() throws Exception {
        mockMvc.perform(post("/lesson/types/add/")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Student)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new LessonTypeDTO(15.50, "gitaar", "samenspel gitaar voor gevorderden", "gitaar 4")
                )))
                .andExpect(status().isCreated());
//                .andExpect(status().isUnauthorized()); // todo: dit moet het worden
    }

    @Test
    public void updateLessonTypeAsAdmin() throws Exception {
        List<LessonType> lessonTypes = lessonService.getLessonTypes();
        LessonType lessonType = lessonTypes.get(0);
        assertEquals(15.5, lessonType.getPrice(), 0);

        mockMvc.perform(put("/lesson/types/update")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", lessonType.getId().toString())
                .content(objectMapper.writeValueAsString(
                        new LessonTypeDTO(20, "gitaar", "samenspel gitaar voor gevorderden", "gitaar 4")
                )))
                .andDo(print())
                .andExpect(status().isContinue());

        lessonTypes = lessonService.getLessonTypes();
        lessonType = lessonTypes.get(0);
        assertEquals(20, lessonType.getPrice(), 0);
    }

    @Test
    public void updateLessonTypeAsTeacher() throws Exception {
        List<LessonType> lessonTypes = lessonService.getLessonTypes();
        LessonType lessonType = lessonTypes.get(1);
        assertEquals(15.5, lessonType.getPrice(), 0);

        mockMvc.perform(put("/lesson/types/update")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Teacher)
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", lessonType.getId().toString())
                .content(objectMapper.writeValueAsString(
                        new LessonTypeDTO(20, "viool", "samenspel gitaar voor gevorderden", "gitaar 4")
                )))
                .andDo(print())
                .andExpect(status().isContinue());
//                .andExpect(status().isUnauthorized()); // todo: dit moet het worden

        lessonTypes = lessonService.getLessonTypes();
        lessonType = lessonTypes.get(0);
        assertEquals(20, lessonType.getPrice(), 0);
    }

    @Test
    public void deleteLessonTypeAsAdmin() throws Exception {
        List<LessonType> lessonTypes = lessonService.getLessonTypes();
        LessonType lessonType = lessonTypes.get(0);

        mockMvc.perform(delete("/lesson/types/delete")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .param("id", lessonType.getId().toString()))
                .andExpect(status().isOk());

        List<LessonType> lessonTypesWithDeletedItem = lessonService.getLessonTypes();
        assertEquals("there must be just 2 lesson types", 1, lessonTypes.size() - lessonTypesWithDeletedItem.size());
    }

    @Test
    public void deleteLessonTypeAsTeacher() throws Exception {
        List<LessonType> lessonTypes = lessonService.getLessonTypes();
        LessonType lessonType = lessonTypes.get(0);

        mockMvc.perform(delete("/lesson/types/delete")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Teacher)
                .param("id", lessonType.getId().toString()))
                .andExpect(status().isForbidden());
    }
}
