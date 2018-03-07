package be.kdg.musicmaker.lesson;

import be.kdg.musicmaker.MMAplication;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
public class TestLessonTypes {
    private static final Logger LOG = LoggerFactory.getLogger(SeedData.class);
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
        LOG.info("the student's access token is: " + ACCESS_TOKEN_Student);
        LOG.info("the teacher's access token is: " + ACCESS_TOKEN_Teacher);
        LOG.info("the admin's access token is: " + ACCESS_TOKEN_Admin);
    }

    @Test
    public void tessCreateLessonType() {
        List<LessonType> lessonTypes = lessonService.getLessonTypes();
        assertEquals("er moeten 3 lestypen in de databank zitten", 3, lessonTypes.size());
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

//    @Test
//    public void createLessonTypeAsAdmin() throws Exception {
//        mockMvc.perform(post("/lesson/type/add").header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(
//                        new LessonTypeDTO(15.50, "gitaar", "samenspel gitaar vor gevorderden", "gitaar 4")
//                )))
//                .andExpect(status().isCreated());
//    }

//    @Test
//    public void createLessonTypesAsTeacher() throws Exception {
//        mockMvc.perform(post("/lesson/type/add").header("Authorization", "Bearer " + ACCESS_TOKEN_Teacher)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(
//                        new LessonTypeDTO(15.50, "gitaar", "samenspel gitaar vor gevorderden", "gitaar 4")
//                )))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    public void createLessonTypeAsStudent() throws Exception {
//        mockMvc.perform(post("/lesson/type/add")
//                .header("Authorization", "Bearer " + ACCESS_TOKEN_Student)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(
//                        new LessonTypeDTO(15.50, "gitaar", "samenspel gitaar voor gevorderden", "gitaar 4")
//                )))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    public void testUpdateLessonType() {
//    }
}
