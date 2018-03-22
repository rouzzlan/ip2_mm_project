package be.kdg.musicmaker.instrument;

import be.kdg.musicmaker.instrument.dto.InstrumentDTO;
import be.kdg.musicmaker.model.Instrument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/instrument")
public class InstrumentController {
    @Autowired
    InstrumentService instrumentService;

    @PostMapping(value = "/add")
    public ResponseEntity postInstrument(@RequestBody InstrumentDTO instrumentDTO) {
        return new ResponseEntity<>(instrumentService.createInstrument(instrumentDTO), HttpStatus.CREATED);
    }

    @GetMapping(value = "/get")
    public ResponseEntity<List<Instrument>> getInstruments() {
        return new ResponseEntity<>(instrumentService.getInstruments(), HttpStatus.OK);
    }

    @GetMapping(value = "/id/{id}")
    public ResponseEntity getInstrument(@PathVariable Long id) throws InstrumentNotFoundException {
        return new ResponseEntity<>(instrumentService.getInstrument(id), HttpStatus.OK);
    }

    @PutMapping(value = "/edit/{id}")
    public ResponseEntity<String> updateInstrument(@RequestBody InstrumentDTO instrumentDTO, @PathVariable Long id) {
        instrumentService.updateInstrument(instrumentDTO, id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping(value = "/delete/{id}")
    public HttpEntity<String> deleteInstrument(@PathVariable Long id) {
        instrumentService.deleteInstrument(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}