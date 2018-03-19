package be.kdg.musicmaker.CRUD.instrument;

import be.kdg.musicmaker.MMAplication;
import be.kdg.musicmaker.instrument.dto.InstrumentDTO;
import be.kdg.musicmaker.instrument.InstrumentService;
import be.kdg.musicmaker.model.Instrument;
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
        MvcResult result = this.mockMvc.perform(post("/instrument/add").header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(jsonString)).andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        InstrumentDTO instrumentDto = objectMapper.readValue(result.getResponse().getContentAsString(), InstrumentDTO.class);
        Instrument instrument = instrumentService.getInstrument(instrumentDto.getId());
        assertNotNull(instrument);
    }

    @Test
    public void createInstrumentByTeacher() throws Exception {
        String jsonString = "";
        jsonString = objectMapper.writeValueAsString(instrumentDTO);
        MvcResult result = this.mockMvc.perform(post("/instrument/add").header("Authorization", "Bearer " + ACCESS_TOKEN_Teacher)
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(jsonString)).andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        InstrumentDTO instrumentDto = objectMapper.readValue(result.getResponse().getContentAsString(), InstrumentDTO.class);
        Instrument instrument = instrumentService.getInstrument(instrumentDto.getId());
        assertNotNull(instrument);
    }

    @Test
    public void createInstrumentByStudent() throws Exception {
        String jsonString = "";
        jsonString = objectMapper.writeValueAsString(instrumentDTO);
        this.mockMvc.perform(post("/instrument/add").header("Authorization", "Bearer " + ACCESS_TOKEN_Student)
                .contentType(MediaType.APPLICATION_JSON).content(jsonString)).andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void getInstrumentByAdmin() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/instrument/id/{id}", 1).header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
        InstrumentDTO instrDTO = objectMapper.readValue(result.getResponse().getContentAsString(), InstrumentDTO.class);
        assertTrue(instrDTO.getName().equalsIgnoreCase("basgitaar"));
    }
    @Test
    public void getInstrumentByTeacher() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/instrument/id/{id}", 1).header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
        InstrumentDTO instrDTO = objectMapper.readValue(result.getResponse().getContentAsString(), InstrumentDTO.class);
        assertTrue(instrDTO.getName().equalsIgnoreCase("basgitaar"));
    }

    @Test
     public void getInstrumentByStudent() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/instrument/id/{id}", 1).header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
        InstrumentDTO instrDTO = objectMapper.readValue(result.getResponse().getContentAsString(), InstrumentDTO.class);
        assertTrue(instrDTO.getName().equalsIgnoreCase("basgitaar"));
    }

    @Test
    public void editInstrumentByAdmin() throws Exception {
        InstrumentDTO editInstrumentDTO= new InstrumentDTO("edit","SNAAR","klassiek","5 snaren");
        String jsonString = objectMapper.writeValueAsString(editInstrumentDTO);
        MvcResult result = this.mockMvc.perform(post("/instrument/add").header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .contentType(MediaType.APPLICATION_JSON).content(jsonString)).andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        InstrumentDTO instrumentDto = objectMapper.readValue(result.getResponse().getContentAsString(), InstrumentDTO.class);
        InstrumentDTO jsonEditDTO = new InstrumentDTO(instrumentDto.getId(),"editgitaar","SNAAR","klassiek", "4 snaren");
        String jsonEditString = objectMapper.writeValueAsString(jsonEditDTO);
        this.mockMvc.perform(put("/instrument/edit/{id}", instrumentDto.getId()).header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .contentType(MediaType.APPLICATION_JSON).content(jsonEditString)).andDo(print())
                .andExpect(status().isOk());
        Instrument Instrument = instrumentService.getInstrument(instrumentDto.getId());
        assertTrue(Instrument.getVersion().equals("4 snaren"));
    }

    @Test
    public void editInstrumentByTeacher() throws Exception{
        InstrumentDTO editInstrumentDTO= new InstrumentDTO("editgitaarByTeacher","SNAAR","klassiek","5 snaren");
        String jsonString = objectMapper.writeValueAsString(editInstrumentDTO);
        MvcResult result = this.mockMvc.perform(post("/instrument/add").header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .contentType(MediaType.APPLICATION_JSON).content(jsonString)).andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        InstrumentDTO instrumentDto = objectMapper.readValue(result.getResponse().getContentAsString(), InstrumentDTO.class);
        InstrumentDTO jsonEditDTO = new InstrumentDTO(instrumentDto.getId(),"editgitaarByTeacher","SNAAR","klassiek", "4 snaren");
        String jsonEditString = objectMapper.writeValueAsString(jsonEditDTO);
        this.mockMvc.perform(put("/instrument/edit/{id}", instrumentDto.getId()).header("Authorization", "Bearer " + ACCESS_TOKEN_Teacher)
                .contentType(MediaType.APPLICATION_JSON).content(jsonEditString)).andDo(print())
                .andExpect(status().isOk());
        Instrument Instrument = instrumentService.getInstrument(instrumentDto.getId());
        assertTrue(Instrument.getVersion().equals("4 snaren"));
    }

    @Test
    public void editInstrumentByStudent() throws Exception{
        InstrumentDTO editInstrumentDTO= new InstrumentDTO("editgitaarByStudent","SNAAR","klassiek","5 snaren");
        String jsonString = objectMapper.writeValueAsString(editInstrumentDTO);
        MvcResult result = this.mockMvc.perform(post("/instrument/add").header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .contentType(MediaType.APPLICATION_JSON).content(jsonString)).andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        InstrumentDTO instrumentDto = objectMapper.readValue(result.getResponse().getContentAsString(), InstrumentDTO.class);
        InstrumentDTO jsonEditDTO = new InstrumentDTO(instrumentDto.getId(),"editgitaarByStudent","SNAAR","klassiek", "4 snaren");
        String jsonEditString = objectMapper.writeValueAsString(jsonEditDTO);
        this.mockMvc.perform(put("/instrument/edit/{id}", instrumentDto.getId()).header("Authorization", "Bearer " + ACCESS_TOKEN_Student)
                .contentType(MediaType.APPLICATION_JSON).content(jsonEditString)).andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteInstrumentByAdmin() throws Exception {
        InstrumentDTO deleteInstrumentDTO = new InstrumentDTO("deleteGitaar","SNAAR","","");
        String jsonString = objectMapper.writeValueAsString(deleteInstrumentDTO);
        MvcResult result = this.mockMvc.perform(post("/instrument/add").header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .contentType(MediaType.APPLICATION_JSON).content(jsonString)).andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        InstrumentDTO instrumentDto = objectMapper.readValue(result.getResponse().getContentAsString(), InstrumentDTO.class);
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/instrument/delete/{id}", instrumentDto.getId()).header("Authorization", "Bearer " + ACCESS_TOKEN_Admin))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteInstrumentByTeacher() throws Exception {
        InstrumentDTO deleteInstrumentDTO = new InstrumentDTO("deleteGitaarTeacher","SNAAR","","");
        String jsonString = objectMapper.writeValueAsString(deleteInstrumentDTO);
        MvcResult result = this.mockMvc.perform(post("/instrument/add").header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .contentType(MediaType.APPLICATION_JSON).content(jsonString)).andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        InstrumentDTO instrumentDto = objectMapper.readValue(result.getResponse().getContentAsString(), InstrumentDTO.class);
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/instrument/delete/{id}", instrumentDto.getId()).header("Authorization", "Bearer " + ACCESS_TOKEN_Teacher))
                .andExpect(status().isOk());
    }
    @Test
    public void deleteInstrumentByStudent() throws Exception {
        InstrumentDTO deleteInstrumentDTO = new InstrumentDTO("deleteGitaarStudent","SNAAR","","");
        String jsonString = objectMapper.writeValueAsString(deleteInstrumentDTO);
        MvcResult result = this.mockMvc.perform(post("/instrument/add").header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .contentType(MediaType.APPLICATION_JSON).content(jsonString)).andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        InstrumentDTO instrumentDto = objectMapper.readValue(result.getResponse().getContentAsString(), InstrumentDTO.class);
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/instrument/delete/{id}", instrumentDto.getId()).header("Authorization", "Bearer " + ACCESS_TOKEN_Student))
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
