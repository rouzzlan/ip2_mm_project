package be.kdg.musicmaker.musiclib;

import be.kdg.musicmaker.musiclib.dto.MusicPieceDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
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
    http://javasampleapproach.com/java-integration/upload-multipartfile-spring-boot
    http://javasampleapproach.com/spring-framework/spring-boot/angular-4-uploadget-multipartfile-tofrom-spring-boot-server
     */
    //TODO niet met files werken maar met bytestreams
    @RequestMapping(value = "/get_music_piece", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody
    Resource getSteamingFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String musicPieceName = request.getParameter("title");
        MusicPiece musicPiece = musicLibraryService.getMusicPiecesByTitle(musicPieceName).get(0);

        File file = new File(musicPiece.getFileName());
        FileUtils.writeByteArrayToFile(file, musicPiece.getMusicClip());
        file.deleteOnExit();


        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Disposition", "inline; filename=" + file.getName());
        response.setHeader("Content-Length", String.valueOf(file.length()));
        return new FileSystemResource(file);
    }

    @RequestMapping(value = "/get_music_piece/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody
    Resource getSteamingFile(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        MusicPiece musicPiece = musicLibraryService.getMusicPiecesById(id);

        File file = new File(musicPiece.getFileName());
        FileUtils.writeByteArrayToFile(file, musicPiece.getMusicClip());
        file.deleteOnExit();


        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Disposition", "inline; filename=" + file.getName());
        response.setHeader("Content-Length", String.valueOf(file.length()));
        return new FileSystemResource(file);
    }

    @RequestMapping(value = "/get_partituur_file/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody
    Resource getPartituurFile(HttpServletResponse response, @PathVariable("id") Long id) throws IOException {
        File file = musicLibraryService.getPartituur(id);
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Disposition", "inline; filename=" + file.getName());
        response.setHeader("Content-Length", String.valueOf(file.length()));
        return new FileSystemResource(file);
    }

    @PostMapping(value = "/upload/music_piece")
    @ResponseStatus(HttpStatus.OK)
    public HttpStatus postMusicPiece(@RequestParam(value = "musicpiece_info") String info, @RequestParam("file") MultipartFile file) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            MusicPieceDTO musicPiecePostDTO = mapper.readValue(info, MusicPieceDTO.class);
            musicLibraryService.addMusicPiece(musicPiecePostDTO, file);
            return HttpStatus.OK;
        } catch (Exception e) {
            return HttpStatus.BAD_REQUEST;
        }
    }

    @GetMapping(value = "/musicpieces")
    public HttpEntity<Collection<MusicPieceDTO>> getMusicPieces() {
        Collection<MusicPieceDTO> musicpieces = musicLibraryService.getMusicPieces();
        return new ResponseEntity<>(musicpieces, HttpStatus.OK);
    }

    @GetMapping(value = "/musicpiece/{id}")
    public HttpEntity<MusicPieceDTO> getMusicPiece(@PathVariable("id") Long id) {
        MusicPieceDTO musicPiece = musicLibraryService.getMusicPieceDTOById(id);
        return new ResponseEntity<MusicPieceDTO>(musicPiece, HttpStatus.OK);
    }

    @PostMapping(value = "/musicpiece/submit")
    public ResponseEntity<?> postUser(@RequestBody MusicPieceDTO musicPieceDTO) {
        Long id = musicLibraryService.createMusicPiece(musicPieceDTO);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("musicPieceId", id.toString());
        responseHeaders.set("MusicPiece", musicPieceDTO.getTitle());
        return ResponseEntity.status(HttpStatus.CREATED).headers(responseHeaders).build();
    }

    @PostMapping(value = "/musicpiece/submit/file/{id}")
    public ResponseEntity<?> addFileToMusicPiece(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file) {
        try {
            musicLibraryService.addMusicPieceFile(id, file);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/musicpiece/submit/file/{id}")
    public ResponseEntity<?> deleteMusicPiece(@PathVariable("id") Long id) {
        try {
            musicLibraryService.deleteMusicPiece(id);
        } catch (ResouceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/update/musicpiece/{id}")
    public ResponseEntity<?> partialUpdateName(@RequestBody MusicPieceDTO musicPieceDTO, @PathVariable("id") Long id) {
        musicLibraryService.update(musicPieceDTO, id);
        return ResponseEntity.ok("resource address updated");
    }
}
