package be.kdg.musicmaker.Instrument;

import be.kdg.musicmaker.Instrument.InstrumentRepository;
import be.kdg.musicmaker.Instrument.InstrumentService;
import be.kdg.musicmaker.model.InstrumentSort;
import be.kdg.musicmaker.model.MusicInstrument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SeedInstrument {

    @Autowired
    InstrumentRepository instrumentRepository;
    @Autowired
    InstrumentService instrumentService;

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seed();
    }

    private void seed() {
        if (instrumentService.isInstrumentsEmpty()) {
            MusicInstrument instrument = new MusicInstrument("basgitaar", "elektrisch", InstrumentSort.SNAAR, "5 snaren");
            instrumentService.createInstrument(instrument);
        }
    }
}
