package be.kdg.musicmaker.config;

import be.kdg.musicmaker.exceptions.UserNotFoundException;
import be.kdg.musicmaker.model.User;
import be.kdg.musicmaker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    public CustomAuthenticationProvider() {
        super();
    }

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        User user = null;
        try {
            user = userService.doesUserExist(username);
        } catch (UserNotFoundException e) {
        }

        if (user == null || !user.getEmail().equalsIgnoreCase(username)) {
            throw new BadCredentialsException("Email not found.");
        }

        if (!password.equals(user.getPassword())) {
            throw new BadCredentialsException("Wrong password.");
        }
        Collection<? extends GrantedAuthority> authorities
                = new ArrayList<GrantedAuthority>();
        ;
        return new
                UsernamePasswordAuthenticationToken(user, password,
                authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
                UsernamePasswordAuthenticationToken.class);
    }
}
