package be.kdg.musicmaker.user;

import be.kdg.musicmaker.model.Role;
import be.kdg.musicmaker.model.User;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;


    @Autowired
    private MapperFacade orikaMapperFacade;
    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    public User doesUserExist(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return user;
        } else throw new UserNotFoundException();
    }

    public User findByEmail(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return user;
        } else throw new UserNotFoundException();
    }

    public void createUser(UserDTO userDTO) {
        User user = dtoToUser(userDTO);
        user.setRoles(getRoles(userDTO.getRoles()));
        userRepository.save(user);
    }

    private User dtoToUser(UserDTO userDTO) {
        mapperFactory.classMap(UserDTO.class, User.class).exclude("roles");
        MapperFacade mapperFacade = mapperFactory.getMapperFacade();
        return mapperFacade.map(userDTO, User.class);
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public void deleteUserByEmail(String email) { userRepository.deleteUserByEmail(email); }

    public void createRole(Role role) {
        roleRepository.save(role);
    }

    public Role getRole(String name) {
        Role role = roleRepository.findByName(name);
        return role;
    }

    public List<Role> getRoles(List<String> rolesString){
        List<Role> foundRoles = new ArrayList<>();
        for (String s : rolesString) {
            foundRoles.add(roleRepository.findByName(s));
        }

        return foundRoles;
    }

    public List<Role> getRoles(){
        return roleRepository.findAll();
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public Boolean isRolesEmpty() {
        return roleRepository.count() == 0;
    }

    public Boolean isUsersEmpty() {
        return userRepository.count() == 0;
    }

    public User getUser(Long id) throws UserNotFoundException {
        User user = userRepository.findOne(id);
        if (user == null) {
            throw new UserNotFoundException();
        } else {
            return user;
        }
    }

    public User findByConfirmationToken(String confirmationToken) throws UserNotFoundException {
        User user = userRepository.findByConfirmationToken(confirmationToken);
        if (user != null) {
            return user;
        } else throw new UserNotFoundException();
    }

    public User getUserByEmail(String email) throws UserNotFoundException {
        System.out.println(email);
        email = email.concat(".com");
        System.out.println(email);
        User user = userRepository.findByEmail(email);
        if (user == null){
            throw new UserNotFoundException();
        }else {
            return user;
        }
    }
}
