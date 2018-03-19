package be.kdg.musicmaker.backend;

import be.kdg.musicmaker.MMAplication;
import be.kdg.musicmaker.musiclib.MusicLibraryService;
import be.kdg.musicmaker.musiclib.dto.MusicPieceDTO;
import be.kdg.musicmaker.musiclib.MusicPiece;
import be.kdg.musicmaker.security.CorsFilter;
import be.kdg.musicmaker.user.UserService;
import be.kdg.musicmaker.util.TokenGetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = MMAplication.class)
public class FileTransferTest {
    private static String ACCESS_TOKEN_Admin = "";
    private ObjectMapper objectMapper = new ObjectMapper();
    private ClassLoader classLoader;
    private File motzartMusicFile, shuberMusicFile, compactMUsicFile, partituurFile;
    private MockMultipartFile shuberMusicFileMultipartMock, miniMusicMultiPartMock;
    private MusicPieceDTO shubertMusicPiece, miniMusicPiece;
    private final String existingMusicPieceName = "Requiem piano Mozart. Lacrymosa, requiem in D minor, K 626 III sequence";
    private File tempFile;
    private TokenGetter tokenGetter;
    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
    @Autowired
    MusicLibraryService service;
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

        tokenGetter = new TokenGetter(mockMvc);
        ACCESS_TOKEN_Admin = tokenGetter.obtainAccessToken("user3@user.com", "user3");

        classLoader = getClass().getClassLoader();

        byte[] data;
        Path path = Paths.get(Objects.requireNonNull(classLoader.getResource("audio_files/audio_check.wav")).toURI());
        data = Files.readAllBytes(path);
        compactMUsicFile = testFolder.newFile("audio_check.wav");
        FileUtils.writeByteArrayToFile(compactMUsicFile, data);

        path = Paths.get(Objects.requireNonNull(classLoader.getResource("audio_files/musicTestFile.MP3")).toURI());
        data = Files.readAllBytes(path);
        shuberMusicFile = testFolder.newFile("musicTestFileOriginal.MP3");
        FileUtils.writeByteArrayToFile(shuberMusicFile, data);

        path = Paths.get(Objects.requireNonNull(classLoader.getResource("audio_files/Requiem-piano-mozart-lacrymosa.mp3")).toURI());
        data = Files.readAllBytes(path);
        motzartMusicFile = testFolder.newFile("Requiem-piano-mozart-lacrymosa.mp3");
        FileUtils.writeByteArrayToFile(motzartMusicFile, data);


        path = Paths.get(Objects.requireNonNull(classLoader.getResource("other_file_structures/How_To_Save_A_Life_-_The_Fray.mxl")).toURI());
        data = Files.readAllBytes(path);
        partituurFile = testFolder.newFile("How_To_Save_A_Life_-_The_Fray.mxl");
        FileUtils.writeByteArrayToFile(partituurFile, data);

        //schubert musicpiece
        shuberMusicFileMultipartMock = fileToMultipartFile(shuberMusicFile);
        shubertMusicPiece = new MusicPieceDTO();
        shubertMusicPiece.setArtist("Schubert");
        shubertMusicPiece.setTitle("Death_and_the_Maiden");
        //mini musicfile
        miniMusicMultiPartMock = fileToMultipartFile(compactMUsicFile);
        miniMusicPiece = new MusicPieceDTO();
        miniMusicPiece.setArtist("unknown");
        miniMusicPiece.setTitle("unknown");
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
        tempFile = testFolder.newFile("Requiem-piano-mozart-lacrymosa3.mp3");
        FileUtils.writeByteArrayToFile(tempFile, byteArray);
        assertEquals(FileUtils.checksumCRC32(motzartMusicFile), FileUtils.checksumCRC32(tempFile));
    }

    @Test
    public void testDownloadFileContentByMusicPieceId() throws Exception {
        MvcResult result = mockMvc.perform(get("/music_library/get_music_piece/1")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE)).andReturn();
        byte[] byteArray = result.getResponse().getContentAsByteArray();
        tempFile = testFolder.newFile("Requiem-piano-mozart-lacrymosa2.mp3");
        FileUtils.writeByteArrayToFile(tempFile, byteArray);
        assertEquals(FileUtils.checksumCRC32(motzartMusicFile), FileUtils.checksumCRC32(tempFile));
    }

    @Test
    public void UploadMusicPieceTest() throws Exception {
        MusicPieceDTO musicPieceDTO = new MusicPieceDTO();
        musicPieceDTO.setArtist("Test2");
        musicPieceDTO.setTitle("Test Music piece");
        musicPieceDTO.setLanguage("English");

        MockMultipartFile file = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/music_library/upload/music_piece")
                .file(file)
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .param("musicpiece_info", objectMapper.writeValueAsString(musicPieceDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void UploadMusicPieceAndVerifyTest() throws Exception {
        MusicPieceDTO musicPieceDTO = new MusicPieceDTO();
        musicPieceDTO.setArtist("Test3");
        musicPieceDTO.setTitle("Test 2 Music piece");
        musicPieceDTO.setLanguage("English");

        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/music_library/upload/music_piece")
                .file(miniMusicMultiPartMock)
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .param("musicpiece_info", objectMapper.writeValueAsString(musicPieceDTO)))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/music_library/get_music_piece").param("title", musicPieceDTO.getTitle())
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition",
                        "inline; filename=" + miniMusicMultiPartMock.getOriginalFilename()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE)).andReturn();

        byte[] byteArray = result.getResponse().getContentAsByteArray();
        Assert.assertArrayEquals(miniMusicMultiPartMock.getBytes(), byteArray);
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
        MusicPieceDTO[] musicPieceGetDTOS = objectMapper.readValue(result.getResponse().getContentAsString(), MusicPieceDTO[].class);
        MusicPieceDTO musicPieceGetDTO = musicPieceGetDTOS[0];
        assertTrue(musicPieceGetDTO.getTitle().equalsIgnoreCase(existingMusicPieceName));
    }


    @Test
    public void muisPiecePostInfo() throws Exception {
        MusicPieceDTO musicPieceDTO = new MusicPieceDTO();
        musicPieceDTO.setArtist("Test");
        musicPieceDTO.setTitle("Test Music piece");
        musicPieceDTO.setLanguage("English");

        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(musicPieceDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        this.mockMvc.perform(post("/music_library/musicpiece/submit").header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .contentType(MediaType.APPLICATION_JSON).content(jsonString))
                .andExpect(status().isCreated())
                .andExpect(header().string("MusicPiece", musicPieceDTO.getTitle()));
    }

    @Test
    public void muiscFileUploadInSteps() throws Exception {
        MusicPieceDTO musicPieceDTO = new MusicPieceDTO();
        musicPieceDTO.setArtist("Test2");
        musicPieceDTO.setTitle("Test Music piece");
        musicPieceDTO.setLanguage("English");

        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(musicPieceDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        MvcResult result = this.mockMvc.perform(post("/music_library/musicpiece/submit").header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .contentType(MediaType.APPLICATION_JSON).content(jsonString))
                .andExpect(status().isCreated())
                .andExpect(header().string("MusicPiece", musicPieceDTO.getTitle())).andReturn();

        Long id = Long.parseLong(result.getResponse().getHeader("musicPieceId"));

        MockMultipartFile file = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/music_library/musicpiece/submit/file/" + id)
                .file(file)
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin))
                .andExpect(status().isOk());
    }

    @Test
    public void updateMusicPieceTest() throws Exception {
        final long musicPieceId = 1;
        MvcResult result = this.mockMvc.perform(get("/music_library/musicpiece/" + musicPieceId)
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andReturn();
        MusicPieceDTO musicPieceDTO = objectMapper.readValue(result.getResponse().getContentAsString(), MusicPieceDTO.class);
        musicPieceDTO.setArtist("Michael Jackson");

        mapperFactory.classMap(MusicPieceDTO.class, MusicPieceDTO.class);
        MapperFacade mapperFacade = mapperFactory.getMapperFacade();
        MusicPieceDTO mp = mapperFacade.map(musicPieceDTO, MusicPieceDTO.class);
        //Object terug posten
        this.mockMvc.perform(patch("/music_library/update/musicpiece/" + musicPieceId)
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(mp)))
                .andExpect(status().isOk());
        MusicPiece musicPiece = service.getMusicPiecesById(musicPieceId);
        String artist = musicPiece.getArtist();
        Assert.assertTrue("Michael Jackson".equalsIgnoreCase(artist));
    }

    @Test
    public void getPartituurTest() throws Exception {
        MvcResult result = mockMvc.perform(get("/music_library/get_partituur_file/2")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition",
                        "inline; filename=" + "How_To_Save_A_Life_-_The_Fray.mxl"))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE)).andReturn();

        byte[] byteArray = result.getResponse().getContentAsByteArray();

        File receivedFile = testFolder.newFile("How_To_Save_A_Life_-_The_Fray2.mxl");
        FileUtils.writeByteArrayToFile(receivedFile, byteArray);
        assertEquals(FileUtils.checksumCRC32(partituurFile), FileUtils.checksumCRC32(receivedFile));
    }

    private MockMultipartFile fileToMultipartFile(File file) throws IOException {
        String contentType = "application/octet-stream";
        String name, originalFileName;
        name = "file";
        originalFileName = file.getName();
        byte[] content = null;
        try {
            content = Files.readAllBytes(file.toPath());
        } catch (final IOException e) {
        }
        return new MockMultipartFile(name, originalFileName, contentType, content);
    }
}
