package be.kdg.musicmaker.Instrument;

import be.kdg.musicmaker.model.DTO.InstrumentDTO;
import be.kdg.musicmaker.model.InstrumentSort;
import be.kdg.musicmaker.model.MusicInstrument;
import be.kdg.musicmaker.util.InstrumentNotFoundException;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstrumentService {
    @Autowired
    InstrumentRepository instrumentRepository;

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    public MusicInstrument getInstrument(String name) throws InstrumentNotFoundException {
        MusicInstrument instrument = instrumentRepository.findByName(name);
        if (instrument != null) {
            return instrument;
        } else throw new InstrumentNotFoundException();
    }

    public void createInstrument(InstrumentDTO instrumentDTO) {
        MusicInstrument instrument = dtoToInstrument(instrumentDTO);
        System.out.println(instrument.toString());
        instrument.setSort(getSort(instrumentDTO.getSort()));
        instrumentRepository.save(instrument);
        System.out.println(instrument.toString());

    }

    private MusicInstrument dtoToInstrument(InstrumentDTO instrumentDTO) {
        mapperFactory.classMap(InstrumentDTO.class, MusicInstrument.class).exclude("sort");
        MapperFacade mapperFacade = mapperFactory.getMapperFacade();
        return mapperFacade.map(instrumentDTO, MusicInstrument.class);
    }

    public void createInstrument(MusicInstrument instrument) {
        instrumentRepository.save(instrument);
    }

    private InstrumentSort getSort(String sort) {
        return InstrumentSort.valueOf(sort);
    }

    public List<MusicInstrument> getInstruments() {
        List<MusicInstrument> muziekInstrumenten = instrumentRepository.findAll();
        for (MusicInstrument mi : muziekInstrumenten) {
            System.out.println(mi.toString());
        }
        return muziekInstrumenten;

    }

    public boolean isInstrumentsEmpty() {
        return instrumentRepository.count() == 0;
    }
}
