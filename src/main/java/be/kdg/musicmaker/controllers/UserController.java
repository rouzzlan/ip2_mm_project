package be.kdg.musicmaker.controllers;

import be.kdg.musicmaker.DTO.UserDTO;
import be.kdg.musicmaker.service.UserManager;
import be.kdg.musicmaker.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    UserManager manager;

    @RequestMapping(value = "/adduser", method = RequestMethod.POST)
    public ResponseEntity <String> postUser(@RequestBody UserDTO userDTO){
        System.out.println(userDTO.toString());
        manager.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //http://127.0.0.1:8080/getusersJson
    @RequestMapping(value = "/getusersJson")
    public HttpEntity<List<User>> getUsersJson(){
        return new ResponseEntity<>(manager.getUsers(), HttpStatus.OK);
    }
}
