
package com.example.demo.controllers;

import com.example.demo.domain.Users;
import com.example.demo.services.CustomUserDetailsService;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;



@Controller
public class AuthController {

    @Autowired
    private CustomUserDetailsService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public ModelAndView signup() {
        ModelAndView modelAndView = new ModelAndView();
        Users user = new Users();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ModelAndView createNewUser(Users user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        Users userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {

            modelAndView.addObject("emailError",true);
            modelAndView.addObject("title", "Error");
            modelAndView.addObject("message", "email already registered");

        } else {
            userService.saveUser(user);
            modelAndView.addObject("success", true);
            modelAndView.addObject("title", "Welcome");
            modelAndView.addObject("message", " registered successfully");
            modelAndView.addObject("user", new Users());
            System.out.println(user.getEmail()+"\n"+user.getRoles().toString());
            String token = JwtTokenUtil.generateToken(user.getEmail());
            modelAndView.addObject("success",true);

            Resend resend = new Resend("re_QuvsQCTS_7iZ6duRG8JVw37Loc5XsQPP6");

            SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                    .from("AdidasAPI <onboarding@resend.dev>")
                    .to(user.getEmail())
                    .subject("Bienvenido a AdidasAPI")
                    .html("<p>Este es tu token de acceso para poder acceder a nuestros recursos y usarlos en tus proyectos <strong>Token: </strong></p>"+token+"<p>Es token vence en dentro de 1 minuto una vez generado luego debes generar uno nuevo</p>")
                    .build();
            try {
                SendEmailResponse data = resend.emails().send(sendEmailRequest);
                System.out.println(data.getId());
            } catch (ResendException e) {
                e.printStackTrace();
            }



        }

        if (bindingResult.hasErrors()) {

            modelAndView.addObject("title", "Error");
            modelAndView.addObject("message", "We had an error, try again");

        }

        return modelAndView;

    }

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public ModelAndView dashboard() {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = userService.findUserByEmail(auth.getName());
        System.out.println(auth.getName());
        System.out.println(user.getFullname());
        String username = user.getFullname();
        modelAndView.addObject("currentUser", user);
        modelAndView.addObject("fullName", username);
        modelAndView.addObject("adminMessage", "Content Available Only for Users with Admin Role");
        modelAndView.setViewName("dashboard");
        return modelAndView;
    }

    @RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");
        return modelAndView;
    }

}
