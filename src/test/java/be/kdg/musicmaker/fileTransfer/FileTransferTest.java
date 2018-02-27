package be.kdg.musicmaker.fileTransfer;

import be.kdg.musicmaker.MMAplication;
import be.kdg.musicmaker.libraries.musiclib.MusicPiece;
import be.kdg.musicmaker.libraries.musiclib.dto.MusicPiecePostDTO;
import be.kdg.musicmaker.security.CorsFilter;
import be.kdg.musicmaker.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.util.LinkedList;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = MMAplication.class)
public class FileTransferTest {
    private static String ACCESS_TOKEN_Admin = "";
    private ObjectMapper objectMapper = new ObjectMapper();
    private ClassLoader classLoader;
    private File motzartMusicFile, shuberMusicFile;
    private MultipartFile shuberMusicFileMultipartMock;
    private MusicPiecePostDTO shubertMusicPiece;

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
        motzartMusicFile = new File(classLoader.getResource("audio_files/Requiem-piano-mozart-lacrymosa.mp3").toURI());
//        byte[] fileArray = Files.readAllBytes(motzartMusicFile.toPath());
        ACCESS_TOKEN_Admin = obtainAccessToken("user3@user.com", "user3");


        shuberMusicFile = new File(classLoader.getResource("audio_files/musicTestFile.MP3").toURI());
        byte[] fileArray = Files.readAllBytes(shuberMusicFile.toPath());
        shuberMusicFileMultipartMock = new MockMultipartFile("musicClip", "musicTestFile.MP3", "MP3", fileArray);
        shubertMusicPiece = new MusicPiecePostDTO();
        shubertMusicPiece.setArtist("Schubert");
        shubertMusicPiece.setTitle("String Quartet No 14 in D minor Death and the Maiden");
        shubertMusicPiece.setMusicClip(shuberMusicFileMultipartMock);
        shubertMusicPiece.setFileName("musicTestFile.MP3");

    }


    @Test
    public void testFileDownload() throws Exception {

        this.mockMvc.perform(get("/music_library/get_sample_file").header("Authorization", "Bearer " + ACCESS_TOKEN_Admin))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE));

    }

    @Test
    public void testDownloadFileContent() throws Exception {

        MvcResult result = mockMvc.perform(get("/music_library/get_sample_file").header("Authorization", "Bearer " + ACCESS_TOKEN_Admin))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE)).andReturn();
        byte[] byteArray = result.getResponse().getContentAsByteArray();
        File tempFile = testFolder.newFile("Requiem-piano-mozart-lacrymosa.mp3");
        FileUtils.writeByteArrayToFile(tempFile, byteArray);

        Assert.assertEquals(tempFile.getTotalSpace(), motzartMusicFile.getTotalSpace());
//        Assert.assertEquals(FileUtils.readLines(tempFile).size(), FileUtils.readLines(motzartMusicFile).size());

    }

    @Test
    public void UploadMusicPieceTest() throws Exception {
        this.mockMvc.perform(post("/music_library/upload/music_piece").header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .sessionAttr("music_piece",shubertMusicPiece)).andExpect(status().isOk());
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
