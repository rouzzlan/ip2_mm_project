package be.kdg.musicmaker.instrument;

import be.kdg.musicmaker.model.MusicInstrument;
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
    public MusicInstrument createInstrument(InstrumentDTO instrumentDTO) {
        MusicInstrument instrument = dtoToInstrument(instrumentDTO);
        instrumentRepository.save(instrument);
        return instrument;
    }

    public void createInstrument(MusicInstrument instrument) {
        instrumentRepository.save(instrument);
    }

    public List<MusicInstrument> getInstruments() {
        return instrumentRepository.findAll();
    }

    public MusicInstrument getInstrument(String name) throws InstrumentNotFoundException {
        MusicInstrument instrument = instrumentRepository.findByName(name);
        if (instrument != null) {
            return instrument;
        } else throw new InstrumentNotFoundException();
    }

    public MusicInstrument getInstrument(Long id) throws InstrumentNotFoundException {
        MusicInstrument instrument = instrumentRepository.findOne(id);
        if (instrument == null) {
            throw new InstrumentNotFoundException();
        } else {
            return instrument;
        }
    }

    public void updateInstrument(InstrumentDTO instrumentDTO, Long id) {
        instrumentDTO.setId(id);
        MusicInstrument musicInstrument = dtoToInstrument(instrumentDTO);
        instrumentRepository.save(musicInstrument);
    }

    public void deleteInstrument(Long id) {
        instrumentRepository.delete(id);
    }
    //endregion

    private MusicInstrument dtoToInstrument(InstrumentDTO instrumentDTO) {
        mapperFactory.classMap(InstrumentDTO.class, MusicInstrument.class);
        MapperFacade mapperFacade = mapperFactory.getMapperFacade();
        return mapperFacade.map(instrumentDTO, MusicInstrument.class);
    }

    public boolean isInstrumentsEmpty() {
        return instrumentRepository.count() == 0;
    }

}
