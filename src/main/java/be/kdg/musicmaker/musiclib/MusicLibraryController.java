package be.kdg.musicmaker.musiclib;

import be.kdg.musicmaker.model.Language;
import be.kdg.musicmaker.model.MusicPiece;
import be.kdg.musicmaker.musiclib.dto.MusicPieceDTO;
import be.kdg.musicmaker.musiclib.dto.RatingDTO;
import be.kdg.musicmaker.security.SeedData;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/music_library")
@SessionAttributes("music_piece")
public class MusicLibraryController {
    MusicLibraryService musicLibraryService;
    @Autowired
    public MusicLibraryController(MusicLibraryService musicLibraryService) {
        this.musicLibraryService = musicLibraryService;
    }

    private static final Logger LOG = LoggerFactory.getLogger(MusicLibraryController.class);


    //ADD
  /*  @PostMapping(value = "/musicpiece/submit")
    public ResponseEntity<?> postUser(@RequestBody MusicPieceDTO musicPieceDTO) {
        Long id = musicLibraryService.createMusicPiece(musicPieceDTO);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("musicPieceId", id.toString());
        responseHeaders.set("MusicPiece", musicPieceDTO.getTitle());
        return ResponseEntity.status(HttpStatus.CREATED).headers(responseHeaders).build();
    }
*/
    @PostMapping(value = "/musicpiece/submit")
    public ResponseEntity<?> postUser(@RequestBody MusicPieceDTO musicPieceDTO) {
        Long id = musicLibraryService.createMusicPiece(musicPieceDTO);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("musicPieceId", id.toString());
        responseHeaders.set("MusicPiece", musicPieceDTO.getTitle());
        return ResponseEntity.status(HttpStatus.CREATED).headers(responseHeaders).build();
    }

    @PostMapping(value = "/createmusicpiece")
    public ResponseEntity postRating(@RequestBody MusicPieceDTO dto){
        return new ResponseEntity<>(musicLibraryService.addMusicPiece(dto), HttpStatus.CREATED);
    }

    @PostMapping(value = "/setRating")
    public ResponseEntity postRating(@RequestBody RatingDTO dto){
        return new ResponseEntity<>(musicLibraryService.addRating(dto), HttpStatus.CREATED);
    }



    //VIEW

    @GetMapping(value="/ratings/{id}")
    public HttpEntity<List<RatingDTO>> getRatings(@PathVariable Long id){
        return new ResponseEntity<List<RatingDTO>>(musicLibraryService.getRatings(id), HttpStatus.OK);
    }

    @GetMapping(value = "/musicpiece/{id}")
    public  HttpEntity<MusicPieceDTO> getMusicPiece(@PathVariable Long id) {
        return new ResponseEntity<MusicPieceDTO>(musicLibraryService.getMusicPieceDTOById(id), HttpStatus.OK);
    }


    @GetMapping(value = "/musicpieces")
    public HttpEntity<List<MusicPieceDTO>> getMusicPieces() {
        return new ResponseEntity<List<MusicPieceDTO>>(musicLibraryService.getMusicPieces(), HttpStatus.OK);
    }


    @GetMapping(value = "/languages")
    public HttpEntity<List<Language>> getLanguages() {
        return new ResponseEntity<>(musicLibraryService.getLanguages(), HttpStatus.OK);
    }

    //EDIT
    @PatchMapping("/update/musicpiece/{id}")
    public ResponseEntity<?> partialUpdateName(@RequestBody MusicPieceDTO musicPieceDTO, @PathVariable("id") Long id) {
        musicLibraryService.update(musicPieceDTO, id);
        return ResponseEntity.ok("resource address updated");
    }

    //FILES
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

    @PostMapping(value="/editRating")
    public ResponseEntity<?> updaterating(@RequestBody RatingDTO rating){
        musicLibraryService.updateRating(rating);
        return ResponseEntity.ok("resource address updated");
    }


    @RequestMapping(value = "/get_partituur_file/{id}", method = GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody
    Resource getPartituurFile(HttpServletResponse response, @PathVariable("id") Long id) throws IOException {
        File file = musicLibraryService.getPartituur(id);
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Disposition", "inline; filename=" + file.getName());
        response.setHeader("Content-Length", String.valueOf(file.length()));
        return new FileSystemResource(file);
    }

    @PostMapping(value = "/upload/music_piece")
    @ResponseStatus(HttpStatus.CREATED)
    public HttpStatus postMusicPiece(@RequestParam(value = "musicpiece_info") String info, @RequestParam("file") MultipartFile file) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            MusicPieceDTO musicPiecePostDTO = mapper.readValue(info, MusicPieceDTO.class);
            musicLibraryService.addMusicPieceEnMusicFile(musicPiecePostDTO, file);
            return HttpStatus.CREATED;
        } catch (Exception e) {
            return HttpStatus.BAD_REQUEST;
        }
    }

    @PostMapping(value = "/upload/music_piece_full")
    @ResponseStatus(HttpStatus.OK)
    public HttpStatus postMusicPiece(@RequestParam(value = "musicpiece_info") String info, @RequestParam("music_file") MultipartFile musicFile, @RequestParam("partituur_file") MultipartFile partituur) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            MusicPieceDTO musicPiecePostDTO = mapper.readValue(info, MusicPieceDTO.class);
            musicLibraryService.addMusicPiece(musicPiecePostDTO, musicFile, partituur);
            return HttpStatus.OK;
        } catch (Exception e) {
            return HttpStatus.BAD_REQUEST;
        }
    }

    @PostMapping(value = "/upload/music_piece_2")
    @ResponseStatus(HttpStatus.OK)
    public HttpStatus postMusicPiece2(@RequestParam(value = "musicpiece_info") String info, @RequestParam(value = "music_file", required = false) MultipartFile musicFile,
                                      @RequestParam(value = "partituur_file", required = false) MultipartFile partituur) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            MusicPieceDTO musicPieceDTO = mapper.readValue(info, MusicPieceDTO.class);
            if (musicFile == null && partituur == null){
                musicLibraryService.addMusicPiece(musicPieceDTO);
                LOG.info("Muziekstuk zonder file geupload");
                return HttpStatus.OK;
            }else if (musicFile == null){
                musicLibraryService.addMusicPieceEnPartituur(musicPieceDTO, partituur);
                LOG.info("Muziekstuk met partituur geupload");
                return HttpStatus.OK;
            }else if (partituur == null){
                musicLibraryService.addMusicPieceEnMusicFile(musicPieceDTO, musicFile);
                LOG.info("Muziekstuk met muziek file geupload");
                return HttpStatus.OK;
            }else {
                musicLibraryService.addMusicPieceFull(musicPieceDTO, musicFile, partituur);
                LOG.info("Muziekstuk compleet geupload");
                return HttpStatus.OK;
            }
        }catch (IOException e){
            return HttpStatus.BAD_REQUEST;
        }
    }

    /*
      goede informatie bron. toepassing: Download File via Resource
      https://memorynotfound.com/spring-mvc-download-file-examples/
      http://javasampleapproach.com/java-integration/upload-multipartfile-spring-boot
      http://javasampleapproach.com/spring-framework/spring-boot/angular-4-uploadget-multipartfile-tofrom-spring-boot-server
       */
    //TODO niet met files werken maar met bytestreams
    @RequestMapping(value = "/get_music_piece", method = GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
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

    @RequestMapping(value = "/get_music_piece/title", method = GET)
    public HttpEntity<MusicPieceDTO> getMusicPieceByTitle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String musicPieceName = request.getParameter("title");
        MusicPieceDTO musicPiece = musicLibraryService.getMusicPieceByTitle(musicPieceName);

        return new ResponseEntity<MusicPieceDTO>(musicPiece, HttpStatus.OK);
    }

    @RequestMapping(value = "/get_music_piece/{id}", method = GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
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

    //OTHER

  /* //DELETE
  @DeleteMapping(value = "/musicpiece/submit/file/{id}")
    public ResponseEntity<?> deleteMusicPiece(@PathVariable("id") Long id) {
        try {
            musicLibraryService.deleteMusicPiece(id);
        } catch (ResouceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
*/
}
