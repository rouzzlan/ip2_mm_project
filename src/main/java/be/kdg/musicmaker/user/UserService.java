package be.kdg.musicmaker.user;

import be.kdg.musicmaker.util.UserNotFoundException;
import be.kdg.musicmaker.model.Role;
import be.kdg.musicmaker.model.User;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private MapperFacade orikaMapperFacade;

    public User doesUserExist(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return user;
        } else throw new UserNotFoundException();
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public void createRole(Role role) {
        roleRepository.save(role);
    }

    public Role getRole(String name) {
        Role role = roleRepository.findByName(name);
        return role;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }
}
