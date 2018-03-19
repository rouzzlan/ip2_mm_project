package be.kdg.musicmaker.security;

import be.kdg.musicmaker.user.UserNotFoundException;
import be.kdg.musicmaker.model.User;
import be.kdg.musicmaker.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserService userService;

	public CustomAuthenticationProvider() {
        super();
    }
	
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		final String username = authentication.getName();
		final String password = authentication.getCredentials().toString();

		User user = null;
		try {
			user = userService.getUser(username);
		} catch (UserNotFoundException e) {
		}

		if (user == null || !user.getEmail().equalsIgnoreCase(username)) {
			throw new BadCredentialsException("Username not found.");
		}

		if (!password.equals(user.getPassword())) {
			throw new BadCredentialsException("Wrong password.");
		}

        final UserDetails principal = new CustomUserDetails(user);

		if (!principal.isEnabled()) {
			throw new DisabledException("Account is nog niet geactiveerd");
		}
		return new UsernamePasswordAuthenticationToken(principal, password, principal.getAuthorities());
	}
	
	@Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
          UsernamePasswordAuthenticationToken.class);
    }

}