package be.kdg.musicmaker.libraries.musiclib;

import be.kdg.musicmaker.libraries.musiclib.dto.MusicPiecePostDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/music_library")
@SessionAttributes("music_piece")
public class MusicLibraryController {
    @Autowired
    MusicLibraryService musicLibraryService;

    private final Logger logger = LoggerFactory.getLogger(MusicLibraryController.class);

    /**
     * dit is een test request om te evalueren of files opgehaald worden.
     * gebruikte request url: /music_library/get_sample_file
     * @param response
     * @return
     */
    @GetMapping(value = "/get_sample_file")
    public StreamingResponseBody getSteamingFile(HttpServletResponse response) {
        MusicPiece musicPiece = musicLibraryService.getMusicPieceById(Long.valueOf(1));
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=" + musicPiece.getFileName());
        InputStream inputStream = new ByteArrayInputStream(musicPiece.getMusicClip());
        return outputStream -> {
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                outputStream.write(data, 0, nRead);
            }
            logger.info("MusicLibrary controller sent file (http get)");
        };
    }
    @GetMapping(value = "/get_music_piece")
    public StreamingResponseBody getSteamingFile(HttpServletRequest request, HttpServletResponse response) {
        String musicPieceName = request.getParameter("title");
        logger.info("MusicLibraryController (get) /get_music_piece/with param: " + musicPieceName);
        MusicPiece musicPiece = musicLibraryService.getMusicPiecesByTitle(musicPieceName).get(0);
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=" + musicPiece.getFileName());
        InputStream inputStream = new ByteArrayInputStream(musicPiece.getMusicClip());
        return outputStream -> {
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                outputStream.write(data, 0, nRead);
            }
            logger.info("MusicLibrary controller sent file found by title (http get)");
        };
    }

    @PostMapping(value = "/upload/music_piece")
    public ResponseEntity<?> postMusicPiece(@ModelAttribute("music_piece") MusicPiecePostDTO musicPiecePostDTO){
        try{
            logger.info("MusicLibraryController: postMusicPiece method accessed");
            musicLibraryService.addMusicPiece(musicPiecePostDTO);
            return ResponseEntity.ok("Successfulle uploaded");
        }catch (IOException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
