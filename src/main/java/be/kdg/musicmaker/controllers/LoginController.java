package be.kdg.musicmaker.controllers;

import be.kdg.musicmaker.config.CustomAuthenticationProvider;
import be.kdg.musicmaker.helpers.ExecutionStatus;
import be.kdg.musicmaker.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wouter on 31.01.17.
 */
@Controller
public class LoginController {

    @Autowired
    private CustomAuthenticationProvider authProvider;

    // Login form
    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    // Login form with error
    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

    @PostMapping(value="/login")
    public ExecutionStatus processLogin(@RequestBody User reqUser, HttpServletRequest request) {

        Authentication authentication = null;
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(reqUser.getEmail(), reqUser.getPassword());
        try {
            // Delegate authentication check to a custom
            // Authentication provider
            authentication = this.authProvider.authenticate(token);
            //
            // Store the authentication object in
            // SecurityContextHolder
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = (User) authentication.getPrincipal();
            user.setPassword(null);
            return new ExecutionStatus("USER_LOGIN_SUCCESSFUL", "Login Successful!", user);
        } catch (BadCredentialsException e) {
            return new ExecutionStatus("USER_LOGIN_UNSUCCESSFUL", "Username or password is incorrect. Please try again!");
        }
    }
}
