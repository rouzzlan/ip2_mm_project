package be.kdg.musicmaker.band;

import be.kdg.musicmaker.model.Band;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BandController {
    @Autowired
    BandService bandService;

    @RequestMapping(value = "/addBand", method = RequestMethod.POST)
    public ResponseEntity<String> postEvent(@RequestBody Band band){
        bandService.createBand(band);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(value = "/getBands")
    public HttpEntity<List<Band>> getBands(){
        return new ResponseEntity<>(bandService.getBands(), HttpStatus.OK);
    }
}
