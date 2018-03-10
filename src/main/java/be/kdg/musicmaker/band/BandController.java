package be.kdg.musicmaker.band;

import be.kdg.musicmaker.model.Band;
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
    public HttpEntity<Band> getBand(@PathVariable String bandName) throws BandNotFoundException {
        return new ResponseEntity<Band>(bandService.getBand(bandName), HttpStatus.OK);
    }

    @GetMapping(value = "/getbands/{email}")
    public HttpEntity<List<Band>> getBandByEmail(@PathVariable String email) throws BandNotFoundException, UserNotFoundException {
        return new ResponseEntity<List<Band>>(bandService.getBandsByUserMail(email), HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteband/{band}")
    public ResponseEntity<Band> deleteBand(@PathVariable Band band) throws BandNotFoundException {
        bandService.deleteBand(bandService.getBand(band.getName()));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

//    @PutMapping(value = "/editBand/{band}")
//    public HttpE


}
