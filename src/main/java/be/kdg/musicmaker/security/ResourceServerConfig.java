package be.kdg.musicmaker.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/confirm").permitAll()
                .antMatchers("/private/**", "/lesson/*", "/lesson").hasAnyRole("LESGEVER","BEHEERDER") //.hasAnyAuthority("ROLE_BEHEERDER","ROLE_LESGEVER")
                .antMatchers("/getRoles").hasRole("LEERLING")
                .antMatchers("/adduser", "/getusers", "/addinstrument","/editinstrument/{id}","/deleteinstrument/{id}").hasRole("BEHEERDER")       //.hasAuthority("ROLE_BEHEERDER") // single auth acces
                // .hasRole("BEHEERDER")         // single auth access
                .antMatchers("/lesson/mine").hasAnyRole("LEERLING", "LESGEVER", "BEHEERDER")
                .and()
                .exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler())
                .and()
                .csrf().disable();
    }
}
