package be.kdg.musicmaker.user;

import be.kdg.musicmaker.model.User;
import be.kdg.musicmaker.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/adduser", method = RequestMethod.POST)
    public ResponseEntity<String> postUser(@RequestBody UserDTO user){
        userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/getrolesJson")
    public HttpEntity<List<Role>> getRulesJson(){
        return new ResponseEntity<>(userService.getRoles(), HttpStatus.OK);
    }

    @GetMapping(value = "/getusersJson")
    public HttpEntity<List<User>> getUsersJson(){
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }
}