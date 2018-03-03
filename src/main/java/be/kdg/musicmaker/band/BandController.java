package be.kdg.musicmaker.band;

import be.kdg.musicmaker.model.Band;
import be.kdg.musicmaker.model.DTO.BandDTO;
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
    public ResponseEntity<String> postEvent(@RequestBody BandDTO band){
        bandService.createBand(band);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value= "/getBands")
    public HttpEntity<List<Band>> getBands(){
        return new ResponseEntity<>(bandService.getBands(), HttpStatus.OK);
    }
}
