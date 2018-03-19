package be.kdg.musicmaker.user;

import be.kdg.musicmaker.band.repo.BandRepository;
import be.kdg.musicmaker.lesson.repo.AttenderRepository;
import be.kdg.musicmaker.model.Role;
import be.kdg.musicmaker.model.User;
import be.kdg.musicmaker.user.dto.UserDTO;
import be.kdg.musicmaker.user.repo.RoleRepository;
import be.kdg.musicmaker.user.repo.UserRepository;
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
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AttenderRepository attenderRepository;

    @Autowired
    BandRepository bandRepository;

    @Autowired
    RoleRepository roleRepository;

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    //CREATE
    public void createUser(UserDTO userDTO) {
        User user = dtoToUser(userDTO);
        user.setRoles(getRoles(userDTO.getRoles()));
        userRepository.save(user);
    }
    public void createUser(User user) {
        userRepository.save(user);
    }

    public void createRole(Role role) {
        roleRepository.save(role);
    }

    //READ
    public User getUser(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return user;
        } else throw new UserNotFoundException();
    }
    public User getUser(Long id) throws UserNotFoundException {
        User user = userRepository.findOne(id);
        if (user == null) {
            throw new UserNotFoundException();
        } else {
            return user;
        }
    }
    public User getUserByToken(String confirmationToken) throws UserNotFoundException {
        User user = userRepository.findByConfirmationToken(confirmationToken);
        if (user != null) {
            return user;
        } else throw new UserNotFoundException();
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public Role getRole(String name) {
        Role role = roleRepository.findByName(name);
        return role;
    }
    public List<Role> getRoles(){
        return roleRepository.findAll();
    }

    public List<String> getStudents() {
        List<User> students = getUsers().stream().filter(user -> user.getRoles().contains(getRole("ROLE_LEERLING"))).collect(Collectors.toList());
        List<String> studentMails = new ArrayList<>();
        for (User student : students) {
            studentMails.add(student.getEmail());
        }
        return studentMails;
    }
    public List<String> getTeachers() {
        List<User> teachers = getUsers().stream().filter(user -> user.getRoles().contains(getRole("ROLE_LESGEVER"))).collect(Collectors.toList());
        List<String> teacherMails = new ArrayList<>();
        for (User teacher : teachers) {
            teacherMails.add(teacher.getEmail());
        }
        return teacherMails;
    }

    public Boolean isRolesEmpty() {
        return roleRepository.count() == 0;
    }
    public Boolean isUsersEmpty() {
        return userRepository.count() == 0;
    }

    //UPDATE
    public void updateUser(UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.getEmail());
        user.setFirstname(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setPassword(userDTO.getPassword());
        user.setUsername(userDTO.getUsername());
        user.setRoles(getRoles(userDTO.getRoles()));
        userRepository.save(user);
    }

    //DELETE
    public void deleteUser(User user) {
        userRepository.delete(user);
    }
    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email);
        attenderRepository.deleteAttender(user);
        userRepository.delete(email);
    }

    //HELPER
    private User dtoToUser(UserDTO userDTO) {
        mapperFactory.classMap(UserDTO.class, User.class).exclude("roles");
        MapperFacade mapperFacade = mapperFactory.getMapperFacade();
        return mapperFacade.map(userDTO, User.class);
    }
    private List<Role> getRoles(List<String> rolesString){
        List<Role> foundRoles = new ArrayList<>();
        for (String s : rolesString) {
            foundRoles.add(roleRepository.findByName(s));
        }
        return foundRoles;
    }

}
