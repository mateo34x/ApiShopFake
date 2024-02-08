
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
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizationSuccessHandler;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
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

    String valor;

    String methodAuth = "v1";


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(name = "AuthGoogle", required = false) String code) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");

        if (code != null && code.equals("EMAILR")) {
            modelAndView.addObject("emailError", true);
            modelAndView.addObject("title", "Error");
            modelAndView.addObject("message", "email already registered");
        } else if (code != null && code.equals("NOR")) {
            modelAndView.addObject("emailError", true);
            modelAndView.addObject("title", "Error");
            modelAndView.addObject("message", "Account not found, Register now");
        } else if (code != null && code.equals("Error")) {
            modelAndView.addObject("emailError", true);
            modelAndView.addObject("title", "Error");
            modelAndView.addObject("message", "Error in process code:390B");
        }
        return modelAndView;
    }



    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ModelAndView createNewUser(Users user, BindingResult bindingResult) throws ParseException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        Users userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {

            modelAndView.addObject("emailError", true);
            modelAndView.addObject("title", "Error");
            modelAndView.addObject("message", "email already registered");

        } else {
            String token = JwtTokenUtil.generateToken(user.getEmail(), "","full");

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


            long expirationTime = System.currentTimeMillis() + 24 * 60 * 60 * 1000;

            tokenFirts.setCreateL(System.currentTimeMillis() / 1000);
            tokenFirts.setExpirationL(expirationTime / 1000);
            tokenFirts.setId(IDSe.generateUuid());

            userService.saveUser(user, tokenFirts);
            modelAndView.addObject("success", true);
            modelAndView.addObject("title", "Welcome");
            modelAndView.addObject("message", " registered successfully");
            modelAndView.addObject("user", new Users());
            System.out.println(user.getEmail() + "\n" + user.getRoles().toString());
            modelAndView.addObject("success", true);

            Resend resend = new Resend("re_7GA5sESG_KTwKznX3qTTWtxPEuWZn8Xb2");

            SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                    .from("AdidasAPI <onboarding@resend.dev>")
                    .to(user.getEmail())
                    .subject("Bienvenido a AdidasAPI")
                    .html("<p>Este es tu token de acceso para poder acceder a nuestros recursos y usarlos en tus proyectos <strong>Token: </strong></p>" + token + "<p>Es token vence en dentro de 1 minuto una vez generado luego debes generar uno nuevo</p>")
                    .build();
            try {
                SendEmailResponse data = resend.emails().send(sendEmailRequest);
                System.out.println(data.getId());
            } catch (ResendException e) {
                e.printStackTrace();
                return login("Error");
            }


        }

        return modelAndView;

    }

    @RequestMapping(value = "/dashboard/{methodAuth}/{idUser}/{externo}", method = RequestMethod.GET)
    public ModelAndView dashboard(@PathVariable(required = false) String methodAuth,
                                  @PathVariable(required = false) String idUser,
                                  @PathVariable(required = false) String externo ) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();


        String correoFinal;

        Users user = userService.findUserByEmail(auth.getName());

        if (user == null) {
            correoFinal = (String) ((OAuth2User) auth.getPrincipal()).getAttributes().get("email");
            user = userService.findUserByEmail(correoFinal);
            String pictureUrl = (String) ((OAuth2User) auth.getPrincipal()).getAttributes().get("picture");
            modelAndView.addObject("fullImgUser", pictureUrl);

        }

        System.out.println(auth.getName());
        System.out.println(user.getFullname());
        System.out.println(user.getId());
        System.out.println(user.getId());
        String username = user.getFullname();
        modelAndView.addObject("currentUser", username);
        correoFinal = user.getEmail();
        Tokens ultimoToken = user.obtenerUltimoToken();

        if (ultimoToken != null) {

            modelAndView.addObject("token", ultimoToken.getToken());
            if (JwtTokenUtil.validateToken(ultimoToken.getToken())) {

                modelAndView.addObject("success", true);
            } else {
                modelAndView.addObject("error", true);
                modelAndView.addObject("info","Token sin permisos o vencido, verifique.");

            }
        } else {
            modelAndView.addObject("token", "genere un token");
            modelAndView.addObject("empty", true);


        }





        List<Tokens> tokens = userService.obtenerTodosLosTokens(correoFinal);
        List<Tokens> tokensOrdenados = tokens.stream()
                .sorted(Comparator.comparing(Tokens::getCreateL).reversed())
                .collect(Collectors.toList());
        modelAndView.addObject("tokens", tokensOrdenados);


        modelAndView.addObject("fullName", username);
        modelAndView.addObject("fullEmail", correoFinal);


        modelAndView.setViewName("dashboard");

        if (externo != null && externo.equals("a")) {
            modelAndView.addObject("tokenA", true);
        } else if (externo != null && externo.equals("b")) {
            modelAndView.addObject("tokenA", false);
        }


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
            @RequestParam("permisos") String per,
            @RequestParam("nameToken") String nametoken) throws ParseException {


        String token = null;
        String emailUserGoogle = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();


        if (principal instanceof OAuth2User ){
            OAuth2User oauth2User = (OAuth2User) auth.getPrincipal();
            emailUserGoogle = oauth2User.getAttribute("email");
            methodAuth = "v2";
        }

        Users user = userService.findUserByEmail(auth.getName());

        if (user==null){
            token = JwtTokenUtil.generateToken(emailUserGoogle, exp,per);
            user = userService.findUserByEmail(emailUserGoogle);

        }else if (user.getEmail()!=null){
            token = JwtTokenUtil.generateToken(user.getEmail(), exp,per);

        }


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

        long expirationL = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(exp + " " + Hms).getTime() / 1000;
        String expiration = exp + " " + Hms;


        tokenFirts.setExpiration(expiration);

        tokenFirts.setCreateL(System.currentTimeMillis() / 1000);
        tokenFirts.setExpirationL(expirationL);
        tokenFirts.setId(IDSe.generateUuid());


        userService.addToken(user, tokenFirts);
        return "redirect:/dashboard/"+methodAuth+"/"+user.getId()+"/a";


    }


    @PostMapping("/deleteToken")
    public Object eliminarTokenPorId(@RequestParam(name = "tokenid") String tokenId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();


        String emailUserGoogle = null;
        Object principal = auth.getPrincipal();

        if (principal instanceof OAuth2User ){
            OAuth2User oauth2User = (OAuth2User) auth.getPrincipal();
            emailUserGoogle = oauth2User.getAttribute("email");
            methodAuth = "v2";
        }

        Users user = userService.findUserByEmail(auth.getName());

        if (user==null){
            user = userService.findUserByEmail(emailUserGoogle);
        }


        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(user.getId()));

        Query innerQuery = new Query();
        innerQuery.addCriteria(Criteria.where("_id").is(tokenId));


        Update update = new Update();
        update.pull("tokens", innerQuery);

        mongoTemplate.findAndModify(query, update, Users.class, "user");




        return "redirect:/dashboard/"+methodAuth+"/"+user.getId()+"/a";
    }





    @RequestMapping(value = "/google", method = RequestMethod.POST)
    @ResponseBody
    public Object manejarSolicitud(@RequestBody String miCampo) {
        if ("login".equals(miCampo)) {
            System.out.println("Valor igual a: "+miCampo);
            valor = "login";
            return "";
        } else if ("registro".equals(miCampo)){

            System.out.println("Valor igual a: "+miCampo);
            valor = "registro";
            return "";
        }
        return "";
    }




    @RequestMapping(value = "/authV2", method = RequestMethod.GET)
    public Object ResgistreOrLogin () {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("prueba");
        modelAndView.addObject("boton", valor);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OAuth2User oauth2User = (OAuth2User) auth.getPrincipal();
        String emailUserGoogle = oauth2User.getAttribute("email");
        Users userExist = userService.findUserByEmail(emailUserGoogle);
        String NameUserGoogle = oauth2User.getAttribute("name");
        System.out.println("GoogleEmail"+emailUserGoogle);

        if (valor.equals("login")){
            if (userExist != null){
                return "redirect:/dashboard/v2/"+userExist.getId()+"/b";
            }else{
                return login("NOR");

            }

        } else if (valor.equals("registro")) {

            if (userExist != null){
                return login("EMAILR");
            }else{

                String token = null;
                try {
                    token = JwtTokenUtil.generateToken(emailUserGoogle,"","full");
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

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


                Users users = new Users();
                users.setEmail(emailUserGoogle);
                users.setFullname(NameUserGoogle);

                userService.saveUserGoogle(users,tokenFirts);


                Resend resend = new Resend("re_7GA5sESG_KTwKznX3qTTWtxPEuWZn8Xb2");

                SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                        .from("AdidasAPI <onboarding@resend.dev>")
                        .to(emailUserGoogle)
                        .subject("Bienvenido a AdidasAPI")
                        .html("<p>Este es tu token de acceso para poder acceder a nuestros recursos y usarlos en tus proyectos <strong>Token: </strong></p>"+token+"<p>Es token vence en dentro de 1 minuto una vez generado luego debes generar uno nuevo</p>")
                        .build();
                try {
                    SendEmailResponse data = resend.emails().send(sendEmailRequest);
                    System.out.println(data.getId());
                    return "redirect:/dashboard/v2/WelcomeFirstsTime/b";
                } catch (ResendException e) {
                    e.printStackTrace();
                    return login("Error");
                }

            }

        }

        return modelAndView;


    }

}
