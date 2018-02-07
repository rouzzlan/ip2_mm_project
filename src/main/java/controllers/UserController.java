package controllers;

import DTO.UserDTO;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import service.UserManager;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    UserManager manager;

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
}
