package be.kdg.musicmaker.band;

import be.kdg.musicmaker.model.Band;
import be.kdg.musicmaker.model.DTO.BandDTO;
import be.kdg.musicmaker.util.BandNotFoundException;
import be.kdg.musicmaker.util.UserNotFoundException;
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

    @PostMapping(value = "/addBand")
    public ResponseEntity<String> postBand(@RequestBody BandDTO band) {
        bandService.createBand(band);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/getBands")
    public HttpEntity<List<BandDTO>> getBands() {
        return new ResponseEntity<>(bandService.getBands(), HttpStatus.OK);
    }

    @GetMapping(value = "/getBands/{id}")
    public HttpEntity<Band> getBand(@PathVariable String bandName) throws BandNotFoundException {
        return new ResponseEntity<Band>(bandService.getBand(bandName), HttpStatus.OK);
    }

    @GetMapping(value = "/getBands/{email}")
    public HttpEntity<List<Band>> getBandByEmail(@PathVariable String email) throws BandNotFoundException, UserNotFoundException {
        return new ResponseEntity<List<Band>>(bandService.getBandsByUserMail(email), HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteBand/{band}")
    public ResponseEntity<Band> deleteBand(@PathVariable Band band) throws BandNotFoundException {
        bandService.deleteBand(bandService.getBand(band.getName()));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

//    @PutMapping(value = "/editBand/{band}")
//    public HttpE


}
