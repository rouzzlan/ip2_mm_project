package be.kdg.musicmaker.fileTransfer;

import be.kdg.musicmaker.MMAplication;
import be.kdg.musicmaker.security.CorsFilter;
import be.kdg.musicmaker.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.junit.Assert;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.nio.file.Files;
import java.util.LinkedList;

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
public class FileDownloadTest {
    private static String ACCESS_TOKEN_Admin = "";
    private ObjectMapper objectMapper = new ObjectMapper();
    private ClassLoader classLoader;
    private File musicFile;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Autowired
    UserService userService;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private CorsFilter corsFilter;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception{
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity())
                .addFilter(corsFilter).build();
        classLoader = getClass().getClassLoader();
        musicFile = new File(classLoader.getResource("audio_files/Requiem-piano-mozart-lacrymosa.mp3").toURI());
//        byte[] fileArray = Files.readAllBytes(musicFile.toPath());
        ACCESS_TOKEN_Admin = obtainAccessToken("user3@user.com", "user3");

    }


    @Test
    public void testFileDownload() throws Exception {

        this.mockMvc.perform(get("/music_library/get_sample_file").header("Authorization", "Bearer " + ACCESS_TOKEN_Admin))
                .andExpect(status().isOk())
                .andExpect(content().contentType("APPLICATION/OCTET-STREAM"));

    }

    @Test
    public void testDownloadFileContent() throws Exception {

        MvcResult result = mockMvc.perform(get("/music_library/get_sample_file").header("Authorization", "Bearer " + ACCESS_TOKEN_Admin))
                .andExpect(status().isOk())
                .andExpect(content().contentType("APPLICATION/OCTET-STREAM")).andReturn();
        byte[] byteArray = result.getResponse().getContentAsByteArray();
        File tempFile = testFolder.newFile("Requiem-piano-mozart-lacrymosa.mp3");
        FileUtils.writeByteArrayToFile(tempFile, byteArray);

        Assert.assertEquals(FileUtils.readLines(tempFile).size(), FileUtils.readLines(musicFile).size());

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
