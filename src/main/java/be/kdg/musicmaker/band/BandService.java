package be.kdg.musicmaker.band;

import be.kdg.musicmaker.model.Band;
import be.kdg.musicmaker.model.DTO.BandDTO;
import be.kdg.musicmaker.model.User;
import be.kdg.musicmaker.user.UserRepository;
import be.kdg.musicmaker.util.BandNotFoundException;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BandService {
    @Autowired
    BandRepository bandRepository;

    @Autowired
    UserRepository userRepository;

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    public Band getBand(String name) throws BandNotFoundException {
        Band band = bandRepository.findByName(name);
        if( band != null){
            return band;
        } else {
            throw new BandNotFoundException();
        }
    }

    public void createBand(BandDTO bandDTO) {
        Band band = dtoToBand(bandDTO);
        band.setStudents(getStudents(bandDTO.getStudents()));
        bandRepository.save(band);
    }

    private Band dtoToBand(BandDTO bandDTO) {
        mapperFactory.classMap(BandDTO.class, Band.class).exclude("user");
        MapperFacade mapperFacade = mapperFactory.getMapperFacade();
        return mapperFacade.map(bandDTO, Band.class);
    }

    public void createBand(Band band){
        bandRepository.save(band);
    }

    public List<Band> getBands(){
        List<Band> bands = bandRepository.findAll();
        for (Band ba : bands) {
            System.out.println(ba.toString());
        }
        return bands;
    }

    public List<User> getStudents(List<User> students) {
        List<User> studentsToGet = new ArrayList<>();
        for (User student : students) {
            User u = userRepository.findByEmail(student.getEmail());
            studentsToGet.add(u);
        }
        return studentsToGet;
    }

    public boolean isBandEmpty(){
        return bandRepository.count() == 0;
    }
}
