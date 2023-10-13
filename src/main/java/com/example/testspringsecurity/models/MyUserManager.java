package com.example.testspringsecurity.models;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

public class MyUserManager implements UserDetailsManager {

    private AuthenticationDataContext authenticationdatacontext;


    private PasswordEncoder passwordEncoder;

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public MyUserManager(AuthenticationDataContext authenticationdatacontext, PasswordEncoder passwordEncoder) {
        this.authenticationdatacontext=authenticationdatacontext;
        this.passwordEncoder = passwordEncoder;
    }

    // La redéfinition des méthodes héritées

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // elle retourne un objet composé d'un usernane, password, list<role>
        try{
            // lire les information de l'usager
            UserDetails user= this.authenticationdatacontext.loadUserByUsername(username);
            return user;

        }catch(EmptyResultDataAccessException ex){
            return null;
        }
    }


    public void createUser(UserDetails user) {
        // vérifier si l'usager existe déjà
        if(this.authenticationdatacontext.userExists(user.getUsername())){
            throw new IncorrectResultSizeDataAccessException("Username existe déja",1);
        }
        // vérifier si l'usager a des rôles
        if(user.getAuthorities().isEmpty()){
            throw new IncorrectResultSizeDataAccessException("L'usager doit avoir au moins un role",1);
        }
        // Crypter le mot de passe
        String encodedpassword = this.passwordEncoder.encode(user.getPassword());
        // inserer un usager
        this.authenticationdatacontext.createUser(user,encodedpassword);


    }


    public void deleteUser(String username) {

        // supprimer l'usager
        this.authenticationdatacontext.deleteUser(username);
    }



    public void changePassword(String oldPassword, String newPassword) {
        // lire l'usager connnecté
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null) {
            throw new IncorrectResultSizeDataAccessException("Pour changer le mot de passe tu dois etre connecté",1);
        }
        String username = currentUser.getName();
        this.authenticationdatacontext.changePassword(username, newPassword, oldPassword);
    }


    public boolean userExists(String username) {
        return this.authenticationdatacontext.userExists(username);
    }

    public void updateUser(UserDetails user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

