package com.example.testspringsecurity.controllers;

import com.example.testspringsecurity.models.MyAuthenticationProvider;
import com.example.testspringsecurity.models.MyUserManager;
import com.example.testspringsecurity.models.Usager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginController {
    @Autowired
    MyAuthenticationProvider myprovider;
    @Autowired
    MyUserManager usermanager;
    @Autowired
    SecurityContextRepository securityContextRepository;

    @RequestMapping(value="/login/login", method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(value="loginError",required=false, defaultValue="false") String loginError) {
        ModelAndView view=new ModelAndView("login");
        if (loginError.equals("true")){
            // informer la vue login qu'il y a une erreur pour afficher le message
            view.addObject("loginError","true" );
        }else{
            view.addObject("loginError","false" );
        }
        return view;
    }

    @RequestMapping(value="/login/loginpost", method = RequestMethod.POST)
    public String login(HttpServletRequest req, HttpServletResponse response) {
        SecurityContext securitycontext = SecurityContextHolder.createEmptyContext();
        try{
            // créer un usager à base d'un username et un password
            String username=req.getParameter("username");
            String password =req.getParameter("password");
            Usager user= new Usager(username, password,new ArrayList());
            Authentication token =  new UsernamePasswordAuthenticationToken(user,password, new ArrayList());

            //valider le jeton d'authentification si possible
            Authentication aut=myprovider.authenticate(token );
            // sauvegarder le token dans la session
            securitycontext.setAuthentication(aut);
            securityContextRepository.saveContext(securitycontext, req, response);

            //Lire l'url d'origine pour une redirection après une authentification réussie
            SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(req, response);

            if(savedRequest==null)
                return "redirect:/home/accueil";
            else
                return "redirect:"+savedRequest.getRedirectUrl();


        }catch( AuthenticationException ex){
            HttpSession session=req.getSession(true);
            session.setAttribute("LoginErrorMessage", ex.getMessage());
            return "redirect:/login/login?loginError=true";
        }catch( Exception  ex){
            HttpSession session=req.getSession(true);
            session.setAttribute("LoginErrorMessage", ex.getMessage());
            return "redirect:/login/login?loginError=true";
        }
    }

    @RequestMapping(value="/login/logout", method = RequestMethod.POST)
    public String logout (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            //supprimer le jeton d'authentification
            new SecurityContextLogoutHandler().logout(request, response, auth);

        }
        return "redirect:/home/accueil";
    }

    @RequestMapping(value="/login/register", method = RequestMethod.GET)
    public ModelAndView register( @RequestParam(value="registerError",required=false, defaultValue="false") String registerError) {
        ModelAndView view= new ModelAndView("register");
        if (registerError.equals("true"))
            view.addObject("registerError","true" );
        return view;
    }

    @RequestMapping(value="/login/register", method = RequestMethod.POST)
    public String register(HttpServletRequest req, HttpSession session) {

        try{
            String username=req.getParameter("username");
            String password=req.getParameter("password");
            String courriel=req.getParameter("courriel");
            // attribuer le role ROLE_USER par défaut au nouveau usager
            List<GrantedAuthority> authorities= new ArrayList();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            Usager myuser=new Usager(username, password,true,courriel,authorities);
            this.usermanager.createUser(myuser);
            return "redirect:/home/accueil";
        }catch(IncorrectResultSizeDataAccessException ex){

            session.setAttribute("registerErrorMessage", ex.getMessage());
            return "redirect:/login/register?registerError=true";
        }
    }

}

