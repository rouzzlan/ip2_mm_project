package be.kdg.musicmaker.band;

import be.kdg.musicmaker.event.EventService;
import be.kdg.musicmaker.model.Band;
import be.kdg.musicmaker.model.Event;
import be.kdg.musicmaker.model.User;
import be.kdg.musicmaker.user.UserNotFoundException;
import be.kdg.musicmaker.user.UserRepository;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BandService {
    @Autowired
    BandRepository bandRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EventService eventService;

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    public Band doesBandExist(String name) throws BandNotFoundException {
        Band band = bandRepository.findByName(name);
        if (band != null) {
            return band;
        } else {
            throw new BandNotFoundException();
        }
    }

    public BandDTO getBand(Long id) throws BandNotFoundException {
        List<Band> nodigOmTeWerken = bandRepository.findAll();
        Band band = bandRepository.findOne(id);
        if (band != null) {
            BandDTO bandDTO = bandToDto(band);
            bandDTO.setTeacher(band.getTeacher().getEmail());
            bandDTO.setStudents(new ArrayList<>());
            for (User student : band.getStudents()) {
                bandDTO.addStudent(student.getEmail());
            }
            return bandDTO;
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

    public void updateBand(BandDTO bandDTO, Long id) {
        Band band = bandRepository.findOne(id);
        if (band != null) {
            band.setName(bandDTO.getName());
            band.setTeacher(getTeacher(bandDTO.getTeacher()));
            band.setStudents(getStudents(bandDTO.getStudents()));
        }
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
        List<BandDTO> bandDTOs = new ArrayList<>();
        List<String> studentsEmail;
        for (Band band : bands) {
            studentsEmail = new ArrayList<>();
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

    private List<User> getStudents(List<String> students) {
        List<User> studentsToGet = new ArrayList<>();
        for (String student : students) {
            User u = userRepository.findByEmail(student);
            studentsToGet.add(u);
        }
        return studentsToGet;
    }

    private User getTeacher(String teacherMail) {
        return userRepository.findByEmail(teacherMail);

    }

    public boolean isBandEmpty() {
        return bandRepository.count() == 0;
    }

    public List<BandDTO> getBandsByUserMail(Long userId) throws UserNotFoundException {
        User user = userRepository.findOne(userId);
        if(user == null){
            throw new UserNotFoundException();
        } else {
            List<Band> foundBands = bandRepository.findByStudentsContainsOrTeacher(user,user);
            List<BandDTO> bandsToPass = new ArrayList<>();
            for (Band foundBand : foundBands) {
                bandsToPass.add(bandToDto(foundBand));
            }

            return bandsToPass;
        }
    }

    public void deleteBand(Long id) {
        List<Event> events = eventService.getEvents().stream().filter(e -> e.getBand().getName().equals(bandRepository.getOne(id).getName())).collect(Collectors.toList());
        for (Event event : events) {
            eventService.deleteEvent(event.getId());
        }
        bandRepository.delete(id);
    }
}
