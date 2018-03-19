package be.kdg.musicmaker.security;

import be.kdg.musicmaker.user.repo.UserRepository;
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
    public void authenticationManager(AuthenticationManagerBuilder builder, UserRepository repository) throws Exception {
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
