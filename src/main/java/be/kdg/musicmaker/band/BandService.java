package be.kdg.musicmaker.band;

import be.kdg.musicmaker.model.Band;
import be.kdg.musicmaker.model.User;
import be.kdg.musicmaker.user.UserNotFoundException;
import be.kdg.musicmaker.user.UserRepository;
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

    public Band doesBandExist(String name) throws BandNotFoundException {
        Band band = bandRepository.findByName(name);
        if (band != null) {
            return band;
        } else {
            throw new BandNotFoundException();
        }
    }

    public Band getBand(String name) throws BandNotFoundException {
        Band band = bandRepository.findByName(name);
        if (band != null) {
            return band;
        } else {
            throw new BandNotFoundException();
        }
    }

    public void createBand(BandDTO bandDTO) {
        Band band = dtoToBand(bandDTO);
        band.setTeacher(getTeacher(bandDTO.getTeacher()));
        band.setStudents(getStudents(bandDTO.getStudents()));
        bandRepository.save(band);
    }

    private Band dtoToBand(BandDTO bandDTO) {
        mapperFactory.classMap(BandDTO.class, Band.class).exclude("teacher").exclude("students");
        MapperFacade mapperFacade = mapperFactory.getMapperFacade();
        return mapperFacade.map(bandDTO, Band.class);
    }

    private BandDTO bandToDto(Band band) {
        mapperFactory.classMap(Band.class, BandDTO.class).exclude("teacher").exclude("students");
        MapperFacade mapperFacade = mapperFactory.getMapperFacade();
        return mapperFacade.map(band, BandDTO.class);
    }

    public void createBand(Band band) {
        bandRepository.save(band);
    }

    public List<BandDTO> getBands() {
        List<Band> bands = bandRepository.findAll();
//        for (Band ba : bands) {
//            System.out.println(ba.toString());
//        }
        List<BandDTO> bandDTOs = new ArrayList<>();
        List<String> studentsEmail = new ArrayList<>();
        for (Band band : bands) {
            BandDTO bandDto = bandToDto(band);
            bandDto.setTeacher(band.getTeacher().getEmail());
            for (User student : band.getStudents()) {
                studentsEmail.add(student.getEmail());
            }
            bandDto.setStudents(studentsEmail);
            bandDTOs.add(bandDto);
        }
        return bandDTOs;
    }

    public List<User> getStudents(List<String> students) {
        List<User> studentsToGet = new ArrayList<>();
        for (String student : students) {
            User u = userRepository.findByEmail(student);
            studentsToGet.add(u);
        }
        return studentsToGet;
    }

    public User getTeacher(String teacherMail) {
        return userRepository.findByEmail(teacherMail);

    }

    public boolean isBandEmpty() {
        return bandRepository.count() == 0;
    }

    public List<Band> getBandsByUserMail(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new UserNotFoundException();
        }else {
            List<Band> band = bandRepository.findByUser(user);
            return band;
        }
    }

    public void deleteBand(Band band) {
        bandRepository.delete(band);
    }
}
