package be.kdg.musicmaker.band;

import be.kdg.musicmaker.user.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BandController {
    @Autowired
    BandService bandService;

    @PostMapping(value = "/addband")
    public ResponseEntity<String> postBand(@RequestBody BandDTO band) {
        bandService.createBand(band);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/getbands")
    public HttpEntity<List<BandDTO>> getBands() {
        return new ResponseEntity<>(bandService.getBands(), HttpStatus.OK);
    }

    @GetMapping(value = "/getband/{id}")
    public HttpEntity<BandDTO> getBand(@PathVariable Long id) throws BandNotFoundException {
        return new ResponseEntity<>(bandService.getBand(id), HttpStatus.OK);
    }

    @GetMapping(value = "/getbands/{userId}")
    public HttpEntity<List<BandDTO>> getBandsByEmail(@PathVariable Long userId) throws UserNotFoundException {
        return new ResponseEntity<>(bandService.getBandsByUserMail(userId), HttpStatus.OK);
    }

    @PutMapping(value = "/editband/{id}")
    public HttpEntity<String> editBand(@RequestBody BandDTO band, @PathVariable Long id) throws BandNotFoundException {
        bandService.updateBand(band, id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping(value = "/deleteband/{id}")
    public ResponseEntity<String> deleteBand(@PathVariable Long id) throws BandNotFoundException {
        bandService.deleteBand(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
