package com.example.testspringsecurity.models;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AuthenticationDataContext {
    private JdbcTemplate jdbcTemplate;

    // Définitiondes requetes SQL
    private final String insertUserQuery ="insert into Usagers(username,password,status, courriel) values(?,?,?,?)";
    private final String insertAuthQuery="insert into Roles values(?,?)";
    private final String selectPasswordQuery ="select password from Usagers where username=?";
    private final String selectAuthQuery="select role from Roles  where username =?";
    private final String deleteUserAuthoritiesQuery="delete from Roles where username =?";
    private final String deleteUserQuery="delete from Usagers where username =?";
    private final String changePasswordQuery="update Usagers set password=? where username=? and password=?";
    private final String userExistsQuery="select count(*) from Usagers  where username=?";


    public AuthenticationDataContext(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    // Les méthodes utilitaires privées
    private String getPassword(String username){
        return this.jdbcTemplate.queryForObject(selectPasswordQuery,String.class, username);
    }
    private List<GrantedAuthority> getAuthorities(String username){
        List<GrantedAuthority> grantedAuthorities= (List<GrantedAuthority>)jdbcTemplate.query(selectAuthQuery,new String[]{username} ,new RoleMapper());
        return grantedAuthorities;
    }



    private class RoleMapper implements RowMapper<GrantedAuthority> {
        @Override
        public GrantedAuthority mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new SimpleGrantedAuthority(rs.getString("role"));
        }
    }

    // La redéfinition des méthodes héritées

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // elle retourne un objet composé d'un usernane, password, list<role>
        try{
            // lire le mot de passe de l'usager
            String password=this.getPassword(username);
            if (password==null) throw new UsernameNotFoundException("Usager non trouvé");
            // lire les roles associés à l'usager
            List<GrantedAuthority> grantedAuthorities= this.getAuthorities(username);
            return new Usager(username,password,grantedAuthorities);

        }catch(EmptyResultDataAccessException ex){
            return null;
        }
    }


    public void createUser(UserDetails user, String encodedpassword) {
        // inserer un usager
        this.jdbcTemplate.update(insertUserQuery,user.getUsername(), encodedpassword, true,((Usager)user).getCourriel());

        // inserer les roles de l'usager
        List<GrantedAuthority> authorities=(List<GrantedAuthority>) user.getAuthorities();
        for (GrantedAuthority auth : authorities) {
            this.jdbcTemplate.update(insertAuthQuery, user.getUsername(), auth.getAuthority());
        }


    }

    public boolean userExists(String username) {
        int count = this.jdbcTemplate.queryForObject(userExistsQuery, Integer.class, username);
        return (count>0);

    }

    public void deleteUser(String username) {
        // supprimer les roles de l'usager
        this.jdbcTemplate.update(deleteUserAuthoritiesQuery, username);
        // supprimer l'usager
        this.jdbcTemplate.update(deleteUserQuery, username);
    }



    public void changePassword(String username,String oldPassword, String newPassword) {

        this.jdbcTemplate.update(changePasswordQuery, newPassword, username,oldPassword);
    }



}
