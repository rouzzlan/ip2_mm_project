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

    //http://127.0.0.1:8080/createuser
    @RequestMapping(value = "/createuser", method = RequestMethod.GET)
    public String addUser(Model model){
        model.addAttribute("userDTO", new UserDTO());
        return "createuser";
    }
    @RequestMapping(value = "/createuser", method = RequestMethod.POST)
    public String saveUser(@ModelAttribute UserDTO userDTO){
        manager.createUser(userDTO);
        return "home";
    }
    @RequestMapping(value = "/getusers", method = RequestMethod.GET)
    public String getUsers(){
        List<User> users = manager.getUsers();
        return "userlist";
    }

    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    public ResponseEntity <String> postUser(@RequestBody UserDTO userDTO){
        System.out.println(userDTO.toString());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //http://127.0.0.1:8080/getusersJson
    @RequestMapping(value = "/getusersJson")
    public HttpEntity<List<User>> getUsersJson(){
        return new ResponseEntity<>(manager.getUsers(), HttpStatus.OK);
    }
}
