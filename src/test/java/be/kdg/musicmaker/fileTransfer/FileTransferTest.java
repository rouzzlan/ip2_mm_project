package be.kdg.musicmaker.fileTransfer;

import be.kdg.musicmaker.MMAplication;
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
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = MMAplication.class)
public class FileTransferTest {
    private static String ACCESS_TOKEN_Admin = "";
    private ObjectMapper objectMapper = new ObjectMapper();
    private ClassLoader classLoader;
    private File motzartMusicFile, shuberMusicFile, compactMUsicFile;
    private MultipartFile shuberMusicFileMultipartMock, miniMusicMultiPartMock;
    private MusicPiecePostDTO shubertMusicPiece, miniMusicPiece;
    private String existingMusicPieceName = "Requiem piano Mozart. Lacrymosa, requiem in D minor, K 626 III sequence";

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
        compactMUsicFile = new File(classLoader.getResource("audio_files/audio_check.wav").toURI());
        shuberMusicFile = new File(classLoader.getResource("audio_files/musicTestFile.MP3").toURI());

        ACCESS_TOKEN_Admin = obtainAccessToken("user3@user.com", "user3");


        //schubert musicpiece
        shuberMusicFileMultipartMock = fileToMultipartFile(shuberMusicFile);
        shubertMusicPiece = new MusicPiecePostDTO();
        shubertMusicPiece.setArtist("Schubert");
        shubertMusicPiece.setTitle("Death_and_the_Maiden");
        shubertMusicPiece.setMusicClip(shuberMusicFileMultipartMock);
        shubertMusicPiece.setFileName("musicTestFile.MP3");
        //mini musicfile
        miniMusicMultiPartMock = fileToMultipartFile(shuberMusicFile);
        miniMusicPiece = new MusicPiecePostDTO();
        miniMusicPiece.setArtist("Schubert");
        miniMusicPiece.setTitle("Death_and_the_Maiden");
        miniMusicPiece.setMusicClip(shuberMusicFileMultipartMock);
        miniMusicPiece.setFileName("musicTestFile.MP3");

    }


    @Test
    public void testFileDownload() throws Exception {

        this.mockMvc.perform(get("/music_library/get_music_piece").param("title",existingMusicPieceName)
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE));
    }

    @Test
    public void testDownloadFileContent() throws Exception {

        MvcResult result = mockMvc.perform(get("/music_library/get_music_piece").param("title",existingMusicPieceName)
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE)).andReturn();
        byte[] byteArray = result.getResponse().getContentAsByteArray();
        File tempFile = testFolder.newFile("Requiem-piano-mozart-lacrymosa.mp3");
        FileUtils.writeByteArrayToFile(tempFile, byteArray);
        Assert.assertArrayEquals(Files.readAllBytes(motzartMusicFile.toPath()), byteArray);
    }


    //TODO moeten alemaal uitgetest worden
    @Test
    public void UploadMusicPieceTest() throws Exception {
        this.mockMvc.perform(post("/music_library/upload/music_piece").header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .sessionAttr("music_piece",shubertMusicPiece)).andExpect(status().isOk());
    }

    @Test
    public void UploadMusicPieceAndVerifyTest() throws Exception {
        this.mockMvc.perform(post("/music_library/upload/music_piece").header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .sessionAttr("music_piece",shubertMusicPiece)).andExpect(status().isOk());


//todo aanpassen voor dit usecase
        MvcResult result = mockMvc.perform(get("/music_library/get_music_piece/Death_and_the_Maiden")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition",
                        "attachment; filename=" + shuberMusicFile.getName()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE)).andReturn();
        byte[] byteArray = result.getResponse().getContentAsByteArray();
        File tempFile = testFolder.newFile(shuberMusicFile.getName());
        FileUtils.writeByteArrayToFile(tempFile, byteArray);

//        Assert.assertEquals(tempFile.getTotalSpace(), shuberMusicFile.getTotalSpace());
//        FileAssert.assertEquals(tempFile, shuberMusicFile);
//        assertThat(tempFile).hasSameContentAs(shuberMusicFile);

        assertEquals(FileUtils.checksumCRC32(tempFile), FileUtils.checksumCRC32(shuberMusicFile));
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

    private MultipartFile fileToMultipartFile(File file){
        String contentType = "application/octet-stream";
        String name, originalFileName;
        name = file.getName();
        originalFileName = file.getName();
        byte[] content = null;
        try {
            content = Files.readAllBytes(file.toPath());
        } catch (final IOException e) {
        }
        MultipartFile result = new MockMultipartFile(name, originalFileName, contentType, content);
        return result;
    }
}
