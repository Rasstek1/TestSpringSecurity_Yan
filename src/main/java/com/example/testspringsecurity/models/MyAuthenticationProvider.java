package com.example.testspringsecurity.models;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;

public class MyAuthenticationProvider implements AuthenticationProvider {

    private MyUserManager userDetailsManager;



    public MyAuthenticationProvider(MyUserManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;

    }
    public void setUserDetailsManager(MyUserManager uDetailsManager){

        this.userDetailsManager=uDetailsManager;
    }

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        // récupérer l'usager de la base de données
        Usager user =(Usager) this.userDetailsManager.loadUserByUsername(username);
        // vérifier l'existance de l'usager
        if (user == null) {
            throw new UsernameNotFoundException("L'usager est inexistant");
        }
        // en utilisant la méthode matches de la classe BCryptPasswordEncoder
        if (!this.userDetailsManager.getPasswordEncoder().matches(password,user.getPassword())) {
            throw new BadCredentialsException("Le mot de passe est incorect.");
        }
        // récupérer les rôles de l'usager
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) user.getAuthorities();
        if (authorities==null || authorities.isEmpty()){
            throw new BadCredentialsException("Erreur d'autorisation");
        }
        //la création du token (le jeton) d'authentification
        authentication= new UsernamePasswordAuthenticationToken(user, password, authorities);
        // mettre le jeton dans le conteneur du contexte de sécurité de la session
        //SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }


    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

