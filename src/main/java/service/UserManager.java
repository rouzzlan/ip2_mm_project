package service;

import DTO.UserDTO;
import ma.glasnost.orika.MapperFacade;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.UserRepository;

import java.util.List;

@Service
public class UserManager {
    @Autowired
    UserRepository repository;
    @Autowired
    private MapperFacade orikaMapperFacade;

    public void createUser(UserDTO userDTO) {
        User user = orikaMapperFacade.map(userDTO, User.class);
        repository.save(user);
        System.out.println(user);
    }

    public List<User> getUsers() {
        return repository.findAll();
    }

}
