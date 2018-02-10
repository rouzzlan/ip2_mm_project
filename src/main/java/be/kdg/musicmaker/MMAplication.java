package be.kdg.musicmaker;

import be.kdg.musicmaker.security.CustomUserDetails;
import be.kdg.musicmaker.model.Role;
import be.kdg.musicmaker.model.User;
import be.kdg.musicmaker.user.UserRepository;
import be.kdg.musicmaker.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Arrays;

@SpringBootApplication
public class MMAplication {
    public static void main(String[] args) {
        SpringApplication.run(MMAplication.class, args);
    }
}
