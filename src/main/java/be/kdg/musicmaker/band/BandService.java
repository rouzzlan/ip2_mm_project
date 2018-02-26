package be.kdg.musicmaker.band;

import be.kdg.musicmaker.model.Band;
import be.kdg.musicmaker.model.User;
import be.kdg.musicmaker.user.UserRepository;
import be.kdg.musicmaker.util.BandNotFoundException;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BandService {
    @Autowired
    BandRepository bandRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private MapperFacade orikaMapperFacade;

    public Band doesBandExist(String name) throws BandNotFoundException {
        Band band = bandRepository.findByName(name);
        if (band != null) {
            return band;
        } else {
            throw new BandNotFoundException();
        }
    }

    public void createBand(Band band) {
        bandRepository.save(band);
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public User getUser(String email) {
        User user = userRepository.findByEmail(email);
        return user;
    }

    public List<Band> getBands() {
        return bandRepository.findAll();
    }

    public Boolean isUserEmpty() {
        if (userRepository.count() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean isBandEmpty() {
        if (bandRepository.count() == 0) {
            return true;
        } else {
            return false;
        }
    }
}
