package be.kdg.musicmaker.fileTransfer;

import be.kdg.musicmaker.fileManagement.FileSystemStorageService;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FilePersistenceTest {
    @Autowired
    FileSystemStorageService storageService;
    private MultipartFile musicFile;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Before
    public void setup() throws IOException, URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("audio_files/musicTestFile.MP3").toURI());
        byte[] fileArray = Files.readAllBytes(file.toPath());
        musicFile = new MockMultipartFile("musicClip", "musicTestFile.MP3", "MP3", fileArray);
    }
    @Test
    public void testFileLoaded(){
        Assert.assertNotNull(musicFile);
    }
    @Test
    public void testFileNotCorrupted() throws IOException, URISyntaxException{
        ClassLoader classLoader = getClass().getClassLoader();
        File originalFile = new File(classLoader.getResource("audio_files/musicTestFile.MP3").toURI());
        File tempFile = testFolder.newFile(musicFile.getOriginalFilename());
        musicFile.transferTo(tempFile);
        Assert.assertEquals(FileUtils.readLines(tempFile), FileUtils.readLines(originalFile));
    }
}
