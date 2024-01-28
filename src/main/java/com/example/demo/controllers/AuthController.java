
package com.example.demo.controllers;

import com.example.demo.GroceryItem;
import com.example.demo.ItemRepository;
import com.example.demo.SecurityID.IDSe;
import com.example.demo.UserRepository;
import com.example.demo.domain.Tokens;
import com.example.demo.domain.Users;
import com.example.demo.services.CustomUserDetailsService;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Controller
public class AuthController {

    @Autowired
    private CustomUserDetailsService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private MongoTemplate mongoTemplate;


    @Autowired
    private IDSe idSe;


    @Autowired
    UserRepository userRepository;

    
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
    public ModelAndView createNewUser(Users user, BindingResult bindingResult) throws ParseException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        Users userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {

            modelAndView.addObject("emailError",true);
            modelAndView.addObject("title", "Error");
            modelAndView.addObject("message", "email already registered");

        } else {
            String token = JwtTokenUtil.generateToken(user.getEmail(),"");

            Tokens tokenFirts = new Tokens();
            tokenFirts.setName("Token Registre");
            tokenFirts.setAccess("All");

            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String formattedDate = dateFormat.format(currentDate);

            tokenFirts.setCreate(formattedDate);


            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.HOUR_OF_DAY, 24);
            Date expirationDate = calendar.getTime();
            String formattedExpirationDate = dateFormat.format(expirationDate);

            tokenFirts.setExpiration(formattedExpirationDate);

            tokenFirts.setToken(token);


            long expirationTime = System.currentTimeMillis() + 24*60*60*1000;

            tokenFirts.setCreateL(System.currentTimeMillis()/1000);
            tokenFirts.setExpirationL(expirationTime/1000);
            tokenFirts.setId(IDSe.generateUuid());

            userService.saveUser(user,tokenFirts);
            modelAndView.addObject("success", true);
            modelAndView.addObject("title", "Welcome");
            modelAndView.addObject("message", " registered successfully");
            modelAndView.addObject("user", new Users());
            System.out.println(user.getEmail()+"\n"+user.getRoles().toString());
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
    public ModelAndView dashboard(String externo) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = userService.findUserByEmail(auth.getName());
        System.out.println(auth.getName());
        System.out.println(user.getFullname());
        System.out.println(user.getId());
        String username = user.getFullname();
        modelAndView.addObject("currentUser", username);
        modelAndView.addObject("fullName",username);
        modelAndView.addObject("adminMessage", "Content Available Only for Users with Admin Role");



        Tokens ultimoToken = user.obtenerUltimoToken();

        if (ultimoToken != null) {

            modelAndView.addObject("token", ultimoToken.getToken());
        } else {
            modelAndView.addObject("token", "genere un token");
        }


        if (ultimoToken != null){

            if (JwtTokenUtil.validateToken(ultimoToken.getToken())) {

                modelAndView.addObject("success",true);
            }else{
                modelAndView.addObject("error",true);
            }
        }else{
            modelAndView.addObject("empty", true);
        }



        List<Tokens> tokens = userService.obtenerTodosLosTokens();
        List<Tokens> tokensOrdenados = tokens.stream()
                .sorted(Comparator.comparing(Tokens::getCreateL).reversed())
                .collect(Collectors.toList());
        modelAndView.addObject("tokens", tokensOrdenados);


        if (externo != null && !externo.isEmpty()) {
            modelAndView.addObject("tokenA", true);
        } else {
            modelAndView.addObject("tokenA", false);
        }

        modelAndView.setViewName("dashboard");
        return modelAndView;
    }

    @RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");
        return modelAndView;
    }






    @PostMapping("/crearToken")
    public Object crearToken(
            @RequestParam("dateInput") String exp,
            @RequestParam("permisos")String per,
            @RequestParam("nameToken")String nametoken ) throws ParseException {




        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = userService.findUserByEmail(auth.getName());
        String token = JwtTokenUtil.generateToken(user.getEmail(),exp);

        Tokens tokenFirts = new Tokens();
        tokenFirts.setName(nametoken);
        tokenFirts.setAccess(per);

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String formattedDate = dateFormat.format(currentDate);

        tokenFirts.setCreate(formattedDate);



        tokenFirts.setToken(token);


        LocalDateTime horaActual = LocalDateTime.now();

        int hora = horaActual.getHour();
        int minutos = horaActual.getMinute();
        int segundos = horaActual.getSecond();

        String Hms = String.format("%02d:%02d:%02d", hora, minutos, segundos);

        long expirationL = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(exp+" "+Hms).getTime() / 1000;
        String expiration = exp+" "+Hms;


        tokenFirts.setExpiration(expiration);

        tokenFirts.setCreateL(System.currentTimeMillis()/1000);
        tokenFirts.setExpirationL(expirationL);
        tokenFirts.setId(IDSe.generateUuid());


        userService.addToken(user,tokenFirts);
        return dashboard(" active");


    }


    @PostMapping("/deleteToken")
    public Object eliminarTokenPorId(@RequestParam(name = "tokenid") String tokenId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = userService.findUserByEmail(auth.getName());





        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(user.getId()));

        Query innerQuery = new Query();
        innerQuery.addCriteria(Criteria.where("_id").is(tokenId));




        Update update = new Update();
        update.pull("tokens", innerQuery);

        mongoTemplate.findAndModify(query, update, Users.class, "user");


        return dashboard(" active");
    }





}
