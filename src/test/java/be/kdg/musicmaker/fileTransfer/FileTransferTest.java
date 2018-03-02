package be.kdg.musicmaker.fileTransfer;

import be.kdg.musicmaker.MMAplication;
import be.kdg.musicmaker.libraries.musiclib.dto.MusicPieceGetDTO;
import be.kdg.musicmaker.libraries.musiclib.dto.MusicPiecePostDTO;
import be.kdg.musicmaker.security.CorsFilter;
import be.kdg.musicmaker.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    private final String existingMusicPieceName = "Requiem piano Mozart. Lacrymosa, requiem in D minor, K 626 III sequence";
    private final Logger logger = LoggerFactory.getLogger(FileTransferTest.class);
    private File tempFile;

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
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity())
                .addFilter(corsFilter).build();

        classLoader = getClass().getClassLoader();

        byte[] data;

        Path path = Paths.get(classLoader.getResource("audio_files/audio_check.wav").toURI());
        data = Files.readAllBytes(path);
        compactMUsicFile = testFolder.newFile("audio_check.wav");
        FileUtils.writeByteArrayToFile(compactMUsicFile, data);

        path = Paths.get(classLoader.getResource("audio_files/musicTestFile.MP3").toURI());
        data = Files.readAllBytes(path);
        shuberMusicFile = testFolder.newFile("musicTestFileOriginal.MP3");
        FileUtils.writeByteArrayToFile(shuberMusicFile, data);

        path = Paths.get(classLoader.getResource("audio_files/Requiem-piano-mozart-lacrymosa.mp3").toURI());
        data = Files.readAllBytes(path);
        motzartMusicFile = testFolder.newFile("Requiem-piano-mozart-lacrymosa.mp3");
        FileUtils.writeByteArrayToFile(motzartMusicFile, data);

        ACCESS_TOKEN_Admin = obtainAccessToken("user3@user.com", "user3");


        //schubert musicpiece
        shuberMusicFileMultipartMock = fileToMultipartFile(shuberMusicFile);
        shubertMusicPiece = new MusicPiecePostDTO();
        shubertMusicPiece.setArtist("Schubert");
        shubertMusicPiece.setTitle("Death_and_the_Maiden");
        shubertMusicPiece.setMusicClip(shuberMusicFileMultipartMock);
        shubertMusicPiece.setFileName("musicTestFile.MP3");
        //mini musicfile
        miniMusicMultiPartMock = fileToMultipartFile(compactMUsicFile);
        miniMusicPiece = new MusicPiecePostDTO();
        miniMusicPiece.setArtist("unknown");
        miniMusicPiece.setTitle("unknown");
        miniMusicPiece.setMusicClip(shuberMusicFileMultipartMock);
        miniMusicPiece.setFileName("audio_check.wav");

    }

    @After
    public void cleanup(){
        File f = new File("Requiem-piano-mozart-lacrymosa.mp3");
        File f2 = new File("musicTestFile.MP3");

        if(f.exists() && !f.isDirectory()) {
            f.deleteOnExit();
        }
        if(f.exists() && !f.isDirectory()) {
            f2.deleteOnExit();
        }
    }


    @Test
    public void testFileDownload() throws Exception {

        this.mockMvc.perform(get("/music_library/get_music_piece").param("title", existingMusicPieceName)
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE));
    }

    @Test
    public void testDownloadFileContent() throws Exception {

        MvcResult result = mockMvc.perform(get("/music_library/get_music_piece").param("title", existingMusicPieceName)
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE)).andReturn();
        byte[] byteArray = result.getResponse().getContentAsByteArray();
        tempFile = testFolder.newFile("Requiem-piano-mozart-lacrymosa2.mp3");
        FileUtils.writeByteArrayToFile(tempFile, byteArray);
        assertEquals(FileUtils.checksumCRC32(tempFile), FileUtils.checksumCRC32(motzartMusicFile));
    }

    @Test
    public void UploadMusicPieceTest() throws Exception {
        this.mockMvc.perform(post("/music_library/upload/music_piece").header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .sessionAttr("music_piece", shubertMusicPiece)).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void UploadMusicPieceAndVerifyTest() throws Exception {
        this.mockMvc.perform(post("/music_library/upload/music_piece").header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .sessionAttr("music_piece", shubertMusicPiece)).andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/music_library/get_music_piece").param("title", shubertMusicPiece.getTitle())
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition",
                        "inline; filename=" + shubertMusicPiece.getFileName()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE)).andReturn();

        byte[] byteArray = result.getResponse().getContentAsByteArray();
        tempFile = testFolder.newFile("musicTestFile.MP3");
        FileUtils.writeByteArrayToFile(tempFile, byteArray);

        assertEquals(FileUtils.checksumCRC32(tempFile), FileUtils.checksumCRC32(shuberMusicFile));
    }


    @Test
    public void getListOfMusicPiecesTest() throws Exception {
        MvcResult result = mockMvc.perform(get("/music_library/musicpieces")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andReturn();
    }

    @Test
    public void getListOfMusicPiecesContentTest() throws Exception {
        MvcResult result = mockMvc.perform(get("/music_library/musicpieces")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andReturn();
        MusicPieceGetDTO[] musicPieceGetDTOS = objectMapper.readValue(result.getResponse().getContentAsString(), MusicPieceGetDTO[].class);
        MusicPieceGetDTO musicPieceGetDTO = musicPieceGetDTOS[0];
        assertTrue(musicPieceGetDTO.getTitle().equalsIgnoreCase(existingMusicPieceName));
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

    private MultipartFile fileToMultipartFile(File file) throws IOException {
        logger.debug("File to multipart file, file converted: " + file.getName());
        System.out.println("File to multipart file, file converted: " + file.getName());
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
