package com.miage.hospital.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity //Je veux activer la sécurité web
public class SecurityConfig  extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        PasswordEncoder passwordEncoder =passwordEncoder();
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username as principal,password as credentials,active from users where username = ?") //pour spring securité la colonne username s'appelle principale
                .authoritiesByUsernameQuery("select username as principal, role as role from users_roles where username=?")
                .rolePrefix("ROLE_")
                .passwordEncoder(passwordEncoder);

        /*String encodePwd = passwordEncoder.encode("1234");
        System.out.println(encodePwd);
        auth.inMemoryAuthentication()
                .withUser("user1").password(encodePwd).roles("USER")
                .and()
                .withUser("user2").password(passwordEncoder.encode("12345")).roles("USER")
                .and()
                .withUser("admin").password(passwordEncoder.encode("12345")).roles("USER","ADMIN");*/
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.formLogin().loginPage("/login"); //Je veux utiliser un formulaire d'authentication
        http.formLogin();
        http.authorizeRequests().antMatchers("/").permitAll();//Ca nécessite pas une authentification
        http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN");//Tous ces urls nécessitent d'avoir un role ADMIN
        http.authorizeRequests().antMatchers("/user/**").hasRole("USER");//Tous ces urls nécessitent d'avoir un role USER
        http.authorizeRequests().anyRequest().authenticated();//Pour dire toutes les requetes http nécessitent une authentification
        http.exceptionHandling().accessDeniedPage("/403");
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
