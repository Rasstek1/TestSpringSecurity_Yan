package com.example.testspringsecurity.config;

import com.example.testspringsecurity.models.AuthenticationDataContext;
import com.example.testspringsecurity.models.MyAuthenticationProvider;
import com.example.testspringsecurity.models.MyUserManager;
import com.example.testspringsecurity.models.Usager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    // utilisation du système de sécurité par défaut du Spring sécurité
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests((autorize)->
                autorize.requestMatchers(new AntPathRequestMatcher("/home/admin")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/home/usager")).hasRole("USER")
                        .anyRequest().permitAll());

        // la configuration de la page de login
        http.formLogin((form)->form.loginPage("/login/login"));

        // la configuration du logout
        http.logout(log->log.logoutSuccessUrl("/home/accueil")
                .invalidateHttpSession(false)
                .clearAuthentication(true));

        // la configuration de refus d'accès
        http.exceptionHandling((exceptionHandling) ->
                exceptionHandling.accessDeniedPage("/home/accessdenied"));
        // la configuration du context de securite
        http.securityContext((securityContext) -> securityContext.requireExplicitSave(true) );
        // la construction du "SecurityFilterChain"
        return http.build();

    }
    @Bean
    SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptEnc= new BCryptPasswordEncoder();
        return bCryptEnc;
    }
    /* @Bean
     public NoOpPasswordEncoder passwordEncoder() {
         return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
     }*/
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate;
    }
    @Bean
    public AuthenticationDataContext authenticationDataContext(JdbcTemplate jdbcTemplate){
        return new AuthenticationDataContext(jdbcTemplate);
    }
    @Bean(name="myUserManager")
    public MyUserManager userManager(AuthenticationDataContext authenticationDataContext, PasswordEncoder passwordEncoder) {
        MyUserManager usermanager = new MyUserManager(authenticationDataContext,passwordEncoder);
        //création del'usager admin s'il n'est pas deja crée
        if(!usermanager.userExists("admin")){
            List<GrantedAuthority> authorities= new ArrayList();
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            Usager usager= new Usager("admin","admin", true,"",authorities);
            usermanager.createUser(usager);
        }
        return usermanager;
    }

    @Bean
    MyAuthenticationProvider authenticationProvider(@Qualifier("myUserManager") MyUserManager uManager) {
        MyAuthenticationProvider authenticationProvider = new MyAuthenticationProvider(uManager);

        return authenticationProvider;
    }


}
