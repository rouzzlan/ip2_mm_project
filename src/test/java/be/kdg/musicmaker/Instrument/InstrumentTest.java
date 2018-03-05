package be.kdg.musicmaker.Instrument;

import be.kdg.musicmaker.MMAplication;
import be.kdg.musicmaker.model.DTO.InstrumentDTO;
import be.kdg.musicmaker.model.MusicInstrument;
import be.kdg.musicmaker.security.CorsFilter;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.LinkedList;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = MMAplication.class)
public class InstrumentTest {
    private InstrumentDTO instrumentDTO= new InstrumentDTO("testgitaar","SNAAR","klassiek","5 snaren");
    private InstrumentDTO instrumentDTOChanged = new InstrumentDTO(1L, "testgitaar", "SNAAR", "klassiek", "4 snaren");

    private static String ACCESS_TOKEN_Admin = "";
    private static String ACCESS_TOKEN_Student = "";
    private static String ACCESS_TOKEN_Teacher = "";
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    InstrumentService instrumentService;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private CorsFilter corsFilter;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).apply(springSecurity())
                .addFilter(corsFilter).build();
        ACCESS_TOKEN_Admin = obtainAccessToken("user3@user.com", "user3");
        ACCESS_TOKEN_Student = obtainAccessToken("user@user.com", "user");
        ACCESS_TOKEN_Teacher = obtainAccessToken("user2@user.com", "user2");
    }

    @Test
    public void createInstrumentByAdmin() throws Exception {
        String jsonString = "";
        jsonString = objectMapper.writeValueAsString(instrumentDTO);
        this.mockMvc.perform(post("/addinstrument").header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .contentType(MediaType.APPLICATION_JSON).content(jsonString)).andDo(print())
                .andExpect(status().isCreated());
        MusicInstrument instrument = instrumentService.getInstrument("testgitaar");
        assertNotNull(instrument);
    }

    @Test
    public void createInstrumentByTeacher() throws Exception {
        String jsonString = "";
        jsonString = objectMapper.writeValueAsString(instrumentDTO);
        this.mockMvc.perform(post("/addinstrument").header("Authorization", "Bearer " + ACCESS_TOKEN_Teacher)
                .contentType(MediaType.APPLICATION_JSON).content(jsonString)).andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void createInstrumentByStudent() throws Exception {
        String jsonString = "";
        jsonString = objectMapper.writeValueAsString(instrumentDTO);
        this.mockMvc.perform(post("/addinstrument").header("Authorization", "Bearer " + ACCESS_TOKEN_Student)
                .contentType(MediaType.APPLICATION_JSON).content(jsonString)).andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void getInstrumentByAdmin() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/getinstrument/{id}", 1).header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
        InstrumentDTO instrDTO = objectMapper.readValue(result.getResponse().getContentAsString(), InstrumentDTO.class);
        assertTrue(instrDTO.getName().equalsIgnoreCase("basgitaar"));
    }
    @Test
    public void getInstrumentByTeacher() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/getinstrument/{id}", 1).header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
        InstrumentDTO instrDTO = objectMapper.readValue(result.getResponse().getContentAsString(), InstrumentDTO.class);
        assertTrue(instrDTO.getName().equalsIgnoreCase("basgitaar"));
    }

    @Test
     public void getInstrumentByStudent() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/getinstrument/{id}", 1).header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
        InstrumentDTO instrDTO = objectMapper.readValue(result.getResponse().getContentAsString(), InstrumentDTO.class);
        assertTrue(instrDTO.getName().equalsIgnoreCase("basgitaar"));
    }

    @Test
    public void editInstrumentByAdmin() throws Exception {
        InstrumentDTO editInstrumentDTO= new InstrumentDTO("editgitaar","SNAAR","klassiek","5 snaren");
        String jsonString = objectMapper.writeValueAsString(editInstrumentDTO);
        this.mockMvc.perform(post("/addinstrument").header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .contentType(MediaType.APPLICATION_JSON).content(jsonString)).andDo(print())
                .andExpect(status().isCreated());
        MusicInstrument instrument = instrumentService.getInstrument("editgitaar");
        Long id = instrument.getId();
        InstrumentDTO jsonEditDTO = new InstrumentDTO(id,"editgitaar","SNAAR","klassiek", "4 snaren");
        String jsonEditString = objectMapper.writeValueAsString(jsonEditDTO);
        this.mockMvc.perform(put("/editinstrument/{id}", id).header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .contentType(MediaType.APPLICATION_JSON).content(jsonEditString)).andDo(print())
                .andExpect(status().isOk());
        MusicInstrument musicInstrument = instrumentService.getInstrument(id);
        assertTrue(musicInstrument.getVersion().equals("4 snaren"));
    }

    @Test
    public void editInstrumentByTeacher() throws Exception{
        InstrumentDTO editInstrumentDTO= new InstrumentDTO("editgitaarByTeacher","SNAAR","klassiek","5 snaren");
        String jsonString = objectMapper.writeValueAsString(editInstrumentDTO);
        this.mockMvc.perform(post("/addinstrument").header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .contentType(MediaType.APPLICATION_JSON).content(jsonString)).andDo(print())
                .andExpect(status().isCreated());
        MusicInstrument instrument = instrumentService.getInstrument("editgitaarByTeacher");
        Long id = instrument.getId();
        InstrumentDTO jsonEditDTO = new InstrumentDTO(id,"editgitaarByTeacher","SNAAR","klassiek", "4 snaren");
        String jsonEditString = objectMapper.writeValueAsString(jsonEditDTO);
        this.mockMvc.perform(put("/editinstrument/{id}", id).header("Authorization", "Bearer " + ACCESS_TOKEN_Teacher)
                .contentType(MediaType.APPLICATION_JSON).content(jsonEditString)).andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void editInstrumentByStudent() throws Exception{
        InstrumentDTO editInstrumentDTO= new InstrumentDTO("editgitaarByStudent","SNAAR","klassiek","5 snaren");
        String jsonString = objectMapper.writeValueAsString(editInstrumentDTO);
        this.mockMvc.perform(post("/addinstrument").header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .contentType(MediaType.APPLICATION_JSON).content(jsonString)).andDo(print())
                .andExpect(status().isCreated());
        MusicInstrument instrument = instrumentService.getInstrument("editgitaarByStudent");
        Long id = instrument.getId();
        InstrumentDTO jsonEditDTO = new InstrumentDTO(id,"editgitaarByStudent","SNAAR","klassiek", "4 snaren");
        String jsonEditString = objectMapper.writeValueAsString(jsonEditDTO);
        this.mockMvc.perform(put("/editinstrument/{id}", id).header("Authorization", "Bearer " + ACCESS_TOKEN_Teacher)
                .contentType(MediaType.APPLICATION_JSON).content(jsonEditString)).andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteInstrumentByAdmin() throws Exception {
        InstrumentDTO deleteInstrumentDTO = new InstrumentDTO("deleteGitaar","SNAAR","","");
        String jsonString = objectMapper.writeValueAsString(deleteInstrumentDTO);
        this.mockMvc.perform(post("/addinstrument").header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .contentType(MediaType.APPLICATION_JSON).content(jsonString)).andDo(print())
                .andExpect(status().isCreated());
        MusicInstrument instrument = instrumentService.getInstrument("deleteGitaar");
        Long id = instrument.getId();
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/deleteinstrument/{id}", id).header("Authorization", "Bearer " + ACCESS_TOKEN_Admin))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteInstrumentByTeacher() throws Exception {
        InstrumentDTO deleteInstrumentDTO = new InstrumentDTO("deleteGitaarTeacher","SNAAR","","");
        String jsonString = objectMapper.writeValueAsString(deleteInstrumentDTO);
        this.mockMvc.perform(post("/addinstrument").header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .contentType(MediaType.APPLICATION_JSON).content(jsonString)).andDo(print())
                .andExpect(status().isCreated());
        MusicInstrument instrument = instrumentService.getInstrument("deleteGitaarTeacher");
        Long id = instrument.getId();
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/deleteinstrument/{id}", id).header("Authorization", "Bearer " + ACCESS_TOKEN_Teacher))
                .andExpect(status().isForbidden());
    }
    @Test
    public void deleteInstrumentByStudent() throws Exception {
        InstrumentDTO deleteInstrumentDTO = new InstrumentDTO("deleteGitaarStudent","SNAAR","","");
        String jsonString = objectMapper.writeValueAsString(deleteInstrumentDTO);
        this.mockMvc.perform(post("/addinstrument").header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .contentType(MediaType.APPLICATION_JSON).content(jsonString)).andDo(print())
                .andExpect(status().isCreated());
        MusicInstrument instrument = instrumentService.getInstrument("deleteGitaarStudent");
        Long id = instrument.getId();
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/deleteinstrument/{id}", id).header("Authorization", "Bearer " + ACCESS_TOKEN_Student))
                .andExpect(status().isForbidden());
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
