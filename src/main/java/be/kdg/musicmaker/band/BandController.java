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

    @GetMapping(value = "/getband/{bandName}")
    public HttpEntity<BandDTO> getBand(@PathVariable String bandName) throws BandNotFoundException {
        return new ResponseEntity<BandDTO>(bandService.getBand(bandName), HttpStatus.OK);
    }

    @GetMapping(value = "/getbands/{email}")
    public HttpEntity<List<BandDTO>> getBandByEmail(@PathVariable String email) throws BandNotFoundException, UserNotFoundException {
        return new ResponseEntity<List<BandDTO>>(bandService.getBandsByUserMail(email), HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteband/{id}")
    public ResponseEntity<String> deleteBand(@PathVariable Long id) throws BandNotFoundException {
        bandService.deleteBand(bandService.getBand(id));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping(value = "/editband/{id}")
    public HttpEntity<String> editBand(@RequestBody BandDTO band, @PathVariable Long id) throws BandNotFoundException {
        bandService.updateBand(band, id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
