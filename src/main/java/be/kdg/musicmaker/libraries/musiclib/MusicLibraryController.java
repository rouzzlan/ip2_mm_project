package be.kdg.musicmaker.libraries.musiclib;

import be.kdg.musicmaker.libraries.musiclib.dto.MusicPieceGetDTO;
import be.kdg.musicmaker.libraries.musiclib.dto.MusicPiecePostDTO;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

@RestController
@RequestMapping("/music_library")
@SessionAttributes("music_piece")
public class MusicLibraryController {
    @Autowired
    MusicLibraryService musicLibraryService;

    private final Logger logger = LoggerFactory.getLogger(MusicLibraryController.class);

    /*
    goede informatie bron. toepassing: Download File via Resource
    https://memorynotfound.com/spring-mvc-download-file-examples/

     */
    @RequestMapping(value = "/get_music_piece", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody Resource getSteamingFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String musicPieceName = request.getParameter("title");
        logger.info("MusicLibraryController (get) /get_music_piece/with param: " + musicPieceName);
        MusicPiece musicPiece = musicLibraryService.getMusicPiecesByTitle(musicPieceName).get(0);

        File file = new File(musicPiece.getFileName());
        FileUtils.writeByteArrayToFile(file, musicPiece.getMusicClip());

        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Disposition", "inline; filename=" + file.getName());
        response.setHeader("Content-Length", String.valueOf(file.length()));
        return new FileSystemResource(file);
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
    @GetMapping(value = "/musicpieces")
    public Collection<MusicPieceGetDTO> getMusicPieces(){
        return musicLibraryService.getMusicPieces();
    }

}
