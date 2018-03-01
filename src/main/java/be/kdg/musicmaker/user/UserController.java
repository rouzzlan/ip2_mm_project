package be.kdg.musicmaker.user;

import be.kdg.musicmaker.model.Role;
import be.kdg.musicmaker.model.User;
import be.kdg.musicmaker.model.DTO.UserDTO;
import be.kdg.musicmaker.util.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping(value = "/adduser")
    public ResponseEntity<String> postUser(@RequestBody UserDTO user){
        userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/user/{id}")
    public HttpEntity<User> getUser(@PathVariable Long id) throws UserNotFoundException {
        return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
    }

    @GetMapping(value = "/getusers")
    public HttpEntity<List<User>> getUsersJson(){
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @GetMapping(value = "/getroles")
    public HttpEntity<List<Role>> getRoles(){
        return new ResponseEntity<>(userService.getRoles(), HttpStatus.OK);
    }
}