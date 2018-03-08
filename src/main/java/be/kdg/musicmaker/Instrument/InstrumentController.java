package be.kdg.musicmaker.Instrument;

import be.kdg.musicmaker.model.MusicInstrument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InstrumentController {
    @Autowired
    InstrumentService instrumentService;

    @PostMapping(value = "/addinstrument")
    public ResponseEntity<String> postInstrument(@RequestBody InstrumentDTO instrumentDTO){
        instrumentService.createInstrument(instrumentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(value = "/editinstrument/{id}")
    public ResponseEntity<String> updateInstrument(@RequestBody InstrumentDTO instrumentDTO, @PathVariable Long id){
        instrumentService.updateInstrument(instrumentDTO, id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(value = "/getinstruments")
    public HttpEntity<List<MusicInstrument>> getInstruments(){
        return new ResponseEntity<>(instrumentService.getInstruments(), HttpStatus.OK);
    }

    @GetMapping(value = "/getinstrument/{id}")
    public HttpEntity<MusicInstrument> getInstrument(@PathVariable Long id) throws InstrumentNotFoundException{
        return new ResponseEntity(instrumentService.getInstrument(id), HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteinstrument/{id}")
    public HttpEntity<String> deleteInstrument(@PathVariable Long id) throws InstrumentNotFoundException{
        instrumentService.deleteInstrument(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}