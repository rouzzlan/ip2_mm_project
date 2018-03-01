package be.kdg.musicmaker.Instrument;

import be.kdg.musicmaker.model.DTO.InstrumentDTO;
import be.kdg.musicmaker.model.MusicInstrument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class InstrumentController {
    @Autowired
    InstrumentService instrumentService;

    @PostMapping(value = "/addInstrument")
    public ResponseEntity<String> postInstrument(@RequestBody InstrumentDTO instrumentDTO){
        System.out.println(instrumentDTO.toString());
        instrumentService.createInstrument(instrumentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/getinstrumentsJson")
    public HttpEntity<List<MusicInstrument>> getUsersJson(){
        return new ResponseEntity<>(instrumentService.getInstruments(), HttpStatus.OK);
    }
}
