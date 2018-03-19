package be.kdg.musicmaker.user;

import be.kdg.musicmaker.model.Role;
import be.kdg.musicmaker.model.User;
import be.kdg.musicmaker.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping(value = "/add")
    public ResponseEntity<String> postUser(@RequestBody UserDTO user){
        userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/id/{id}")
    public HttpEntity<User> getUser(@PathVariable Long id) throws UserNotFoundException {
        return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
    }

    @GetMapping(value = "/email/{email:.+}")
    public HttpEntity<User> getUser(@PathVariable String email) throws UserNotFoundException {
        return new ResponseEntity<>(userService.getUser(email), HttpStatus.OK);
    }

    @GetMapping(value = "/get")
    public HttpEntity<List<User>> getUsers(){
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @GetMapping(value = "/getroles")
    public HttpEntity<List<Role>> getRoles(){
        return new ResponseEntity<>(userService.getRoles(), HttpStatus.OK);
    }

    @GetMapping(value = "/getstudents")
    public HttpEntity<List<String>> getStudents(){
        return new ResponseEntity<>(userService.getStudents(), HttpStatus.OK);
    }

    @GetMapping(value = "/getteachers")
    public HttpEntity<List<String>> getTeachers(){
        return new ResponseEntity<>(userService.getTeachers(), HttpStatus.OK);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<String> updateLesson(@RequestBody UserDTO userDTO) {
        userService.updateUser(userDTO);
        return ResponseEntity.status(HttpStatus.CONTINUE).build();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteLesson(@RequestParam String email) {
        userService.deleteUser(email);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}