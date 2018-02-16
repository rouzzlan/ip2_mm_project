package be.kdg.musicmaker.user;

import be.kdg.musicmaker.model.User;
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
    public ResponseEntity<String> postUser(@RequestBody User user){
        System.out.println(user.toString());
        userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(value = "/getusersJson")
    public HttpEntity<List<User>> getUsersJson(){
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }
}