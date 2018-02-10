package be.kdg.musicmaker.security;

import be.kdg.musicmaker.model.Role;
import be.kdg.musicmaker.model.User;
import be.kdg.musicmaker.user.UserRepository;
import be.kdg.musicmaker.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private CustomAuthenticationProvider authProvider;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Password grants are switched on by injecting an AuthenticationManager.
     * Here, we setup the builder so that the userDetailsService is the one we coded.
     * @param builder
     * @param repository
     * @throws Exception
     */
    @Autowired
    public void authenticationManager(AuthenticationManagerBuilder builder, UserRepository repository, UserService service) throws Exception {
        //Setup default users if db is empty
        if (repository.count()==0) {
            Role leerling = new Role("ROLE_LEERLING");
            Role lesgever = new Role("ROLE_LESGEVER");
            Role beheerder = new Role("ROLE_BEHEERDER");
            service.createRole(leerling);
            service.createRole(lesgever);
            service.createRole(beheerder);

            User user = new User("user","user","user","user","user@user.com");
            User user2 = new User("user2", "user2", "user2", "user2", "user2@user.com");
            User user3 = new User("user3", "user3", "user3", "user3", "user3@user.com");
            service.createUser(user);
            service.createUser(user2);
            service.createUser(user3);

            user.setRoles(Arrays.asList(leerling));
            user2.setRoles(Arrays.asList(leerling,lesgever));
            user3.setRoles(Arrays.asList(leerling,lesgever,beheerder));
            service.createUser(user);
            service.createUser(user2);
            service.createUser(user3);
        }
        builder.userDetailsService(userDetailsService(repository));
        builder.authenticationProvider(this.authProvider);
    }

    /**
     * We return an instance of our CustomUserDetails.
     */
    private UserDetailsService userDetailsService(final UserRepository repository) {
        return email -> new CustomUserDetails(repository.findByEmail(email));
    }
}
