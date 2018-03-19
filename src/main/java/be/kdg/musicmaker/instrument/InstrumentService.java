package be.kdg.musicmaker.instrument;

import be.kdg.musicmaker.instrument.dto.InstrumentDTO;
import be.kdg.musicmaker.instrument.repo.InstrumentRepository;
import be.kdg.musicmaker.model.Instrument;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class    InstrumentService {
    @Autowired
    InstrumentRepository instrumentRepository;

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    //region CRUD
    public Instrument createInstrument(InstrumentDTO instrumentDTO) {
        Instrument instrument = dtoToInstrument(instrumentDTO);
        instrumentRepository.save(instrument);
        return instrument;
    }

    public void createInstrument(Instrument instrument) {
        instrumentRepository.save(instrument);
    }

    public List<Instrument> getInstruments() {
        return instrumentRepository.findAll();
    }

    public Instrument getInstrument(String name) throws InstrumentNotFoundException {
        Instrument instrument = instrumentRepository.findByName(name);
        if (instrument != null) {
            return instrument;
        } else throw new InstrumentNotFoundException();
    }

    public Instrument getInstrument(Long id) throws InstrumentNotFoundException {
        Instrument instrument = instrumentRepository.findOne(id);
        if (instrument == null) {
            throw new InstrumentNotFoundException();
        } else {
            return instrument;
        }
    }

    public void updateInstrument(InstrumentDTO instrumentDTO, Long id) {
        instrumentDTO.setId(id);
        Instrument Instrument = dtoToInstrument(instrumentDTO);
        instrumentRepository.save(Instrument);
    }

    public void deleteInstrument(Long id) {
        instrumentRepository.delete(id);
    }
    //endregion

    private Instrument dtoToInstrument(InstrumentDTO instrumentDTO) {
        mapperFactory.classMap(InstrumentDTO.class, Instrument.class);
        MapperFacade mapperFacade = mapperFactory.getMapperFacade();
        return mapperFacade.map(instrumentDTO, Instrument.class);
    }

    public boolean isInstrumentsEmpty() {
        return instrumentRepository.count() == 0;
    }

}
