package be.kdg.musicmaker.fileTransfer;

import be.kdg.musicmaker.libraries.musiclib.MusicLibraryService;
import be.kdg.musicmaker.libraries.musiclib.MusicPiece;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.contentOf;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FilePersistenceTest {
    @Autowired
    MusicLibraryService musicLibraryService;

    private MultipartFile mpMusicFile1;
    private MusicPiece musicPiece1;
    private ClassLoader classLoader;
    private File musicFile;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Before
    public void setup() throws IOException, URISyntaxException {
        classLoader = getClass().getClassLoader();
        musicFile = new File(classLoader.getResource("audio_files/musicTestFile.MP3").toURI());
        byte[] fileArray = Files.readAllBytes(musicFile.toPath());
        mpMusicFile1 = new MockMultipartFile("musicClip", "musicTestFile.MP3", "MP3", fileArray);
        musicPiece1 = new MusicPiece();
        musicPiece1.setArtist("Schubert");
        musicPiece1.setTitle("String Quartet No 14 in D minor Death and the Maiden");
        musicPiece1.setMusicClip(mpMusicFile1.getBytes());
        musicPiece1.setFileName("musicTestFile.MP3");
    }

    @Test
    public void testFileLoaded() {
        Assert.assertNotNull(mpMusicFile1);
    }

    @Test
    public void testFileNotCorrupted() throws IOException {
        File tempFile = testFolder.newFile(mpMusicFile1.getOriginalFilename());
        mpMusicFile1.transferTo(tempFile);
        Assert.assertEquals(FileUtils.readLines(tempFile), FileUtils.readLines(musicFile));
    }

    @Test
    public void musicPiecePersistTest() {
        musicLibraryService.addMusicPiece(musicPiece1);
    }

    @Test
    public void musicPieceRetrieveTest() {
        musicLibraryService.addMusicPiece(musicPiece1);
        List<MusicPiece> musicPiece = musicLibraryService.getMusicPiecesByTitle(musicPiece1.getTitle());
        Assert.assertNotNull(musicPiece);
    }

    @Test
    public void correct_file_format_test() throws IOException {
        musicLibraryService.addMusicPiece(musicPiece1);
        List<MusicPiece> musicPiece = musicLibraryService.getMusicPiecesByTitle(musicPiece1.getTitle());
        byte[] byteArray = musicPiece.get(0).getMusicClip();

        File tempFile = testFolder.newFile(musicPiece.get(0).getFileName());
        FileUtils.writeByteArrayToFile(tempFile, byteArray);
        Assert.assertEquals(FileUtils.readLines(tempFile), FileUtils.readLines(musicFile));
//        assertThat(tempFile).hasSameContentAs(musicFile);

    }
    @Test
    public void basic_framework_test() throws IOException {
        File tempFile1 = testFolder.newFile("file1");
        FileUtils.writeByteArrayToFile(tempFile1, "hello world".getBytes());
        File tempFile2 = testFolder.newFile("file2");
        FileUtils.writeByteArrayToFile(tempFile2, "hello world".getBytes());
        assertThat(tempFile1).hasSameContentAs(tempFile2);

    }
    @Test(expected = AssertionError.class)
    public void basic_framework_fail_test() throws IOException {
        File tempFile1 = testFolder.newFile("file1");
        FileUtils.writeByteArrayToFile(tempFile1, "hello world?".getBytes());
        File tempFile2 = testFolder.newFile("file2");
        FileUtils.writeByteArrayToFile(tempFile2, "hello world!".getBytes());
        assertThat(tempFile1).hasSameContentAs(tempFile2);

    }
}
