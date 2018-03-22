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

                //ACCOUNT
                .antMatchers("/account/**").permitAll()

                //USER
                .antMatchers("/user/update",
                                        "/user/getroles").hasAnyRole("LEERLING", "LESGEVER", "BEHEERDER")
                .antMatchers("/user/**").hasAnyRole("LESGEVER","BEHEERDER")

                //LESSONS
                .antMatchers("/lesson/mine").hasAnyRole("LEERLING", "LESGEVER", "BEHEERDER")
                .antMatchers("/lesson/types/add",
                                        "/lesson/types/update",
                                        "/lesson/types/delete").hasRole("BEHEERDER")
                .antMatchers("/lesson/**").hasAnyRole("LESGEVER","BEHEERDER")

                //INSTRUMENT
                .antMatchers("/instrument/get",
                                        "/instrument/id/**",
                                        "/instrument/email/**").hasAnyRole("LEERLING", "LESGEVER", "BEHEERDER")
                .antMatchers("/instrument/**").hasAnyRole("LESGEVER","BEHEERDER")


                //=============================================================================================
                // ALLES ONDER DEZE LIJN MOET WSS NOG SPECIFIEKER MOMENTEEL GWN GEZET DAT JE INGELOGD MOET ZIJN
                //=============================================================================================

                //MUSIC-LIB
                .antMatchers("/music_library/**").authenticated()

                //EVENT
                .antMatchers("/event/**").authenticated()

                //BAND
                .antMatchers("/band/**").authenticated()

                //todo tijdig voor te testen toegevoegd
                //Chat
                .antMatchers("/chatjs").permitAll()
                .antMatchers("/app/chat/**").permitAll()
                .antMatchers("/topic/chat/**").permitAll()

                .and()
                .exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler())
                .and()
                .csrf().disable();
    }
}
