package com.example.testspringsecurity.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @RequestMapping(value="/home/accueil", method = RequestMethod.GET)
    public ModelAndView accueil( ) {
        ModelAndView mv=new ModelAndView("accueil");
        return mv;

    }

    @RequestMapping(value="/home/accueil", method = RequestMethod.POST)
    public ModelAndView accueilPost( ) {
        ModelAndView mv=new ModelAndView("accueil");
        return mv;

    }

    @RequestMapping(value="/home/usager", method = RequestMethod.GET)
    public ModelAndView usager( ) {
        ModelAndView mv=new ModelAndView("usager");
        return mv;

    }
    @RequestMapping(value="/home/admin", method = RequestMethod.GET)
    public ModelAndView admin( ) {
        ModelAndView mv=new ModelAndView("admin");
        return mv;

    }
    @RequestMapping(value="/home/accessdenied", method = RequestMethod.GET)
    public ModelAndView accessdenied( ) {
        ModelAndView mv=new ModelAndView("accessdenied");
        return mv;

    }

}
