package be.kdg.musicmaker.libraries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/music_library")
public class MusicLibraryController {
    @Autowired
    MusicLibraryService musicLibraryService;

//    @GetMapping(value = "/get_sample_file")
//    public ResponseEntity<InputStreamResource> downloadSample()
//            throws IOException {
//
//        MusicPiece musicPiece = musicLibraryService.getMusicPieceById(Long.valueOf(1));
//        File tempFile = new File(musicPiece.getFileName());
//        FileUtils.writeByteArrayToFile(tempFile, musicPiece.getMusicClip());
//
//        return ResponseEntity
//                .ok()
//                .contentLength(tempFile.length())
//                .contentType(
//                        MediaType.parseMediaType("application/octet-stream"))
//                .body(new InputStreamResource(tempFile.getInputStream()));
//    }

    /**
     * dit is een test request om te evalueren of files opgehaald worden.
     * gebruikte request url: /music_library/get_sample_file
     * @param response
     * @return
     */
    @GetMapping(value = "/get_sample_file")
    public StreamingResponseBody getSteamingFile(HttpServletResponse response) {
        MusicPiece musicPiece = musicLibraryService.getMusicPieceById(Long.valueOf(1));
        response.setContentType("audio/mpeg");
        response.setHeader("Content-Disposition", "attachment; filename=" + musicPiece.getFileName());
        InputStream inputStream = new ByteArrayInputStream(musicPiece.getMusicClip());
        return outputStream -> {
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                outputStream.write(data, 0, nRead);
            }
        };
    }
}
