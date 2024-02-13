
package com.example.demo.controllers;

import com.example.demo.SecurityID.IDSe;
import com.example.demo.UserRepository;
import com.example.demo.domain.Tokens;
import com.example.demo.domain.Users;
import com.example.demo.services.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
    public ModelAndView login(@RequestParam(name = "code", required = false) String code) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");

        if (code != null && code.equals("01")) {
            modelAndView.addObject("emailError", true);
            modelAndView.addObject("title", "Error");
            modelAndView.addObject("message", "Email registrado, intente con otro");
        } else if (code != null && code.equals("02")) {
            modelAndView.addObject("emailError", true);
            modelAndView.addObject("title", "Error");
            modelAndView.addObject("message", "Cuenta no encontrada");
        } else if (code != null && code.equals("03")) {
            modelAndView.addObject("emailError", true);
            modelAndView.addObject("title", "Error");
            modelAndView.addObject("message", "Error in process code:390B");
        } else if (code!=null && code.equals("200")) {

            modelAndView.addObject("success", true);
            modelAndView.addObject("title", "YUJU");
            modelAndView.addObject("message", "Registrado correctamente con google");

        } else if (code!=null && code.equals("201")) {

        modelAndView.addObject("success", true);
        modelAndView.addObject("title", "Welcome");
        modelAndView.addObject("message", "Registrado correctamente");

        } else if (code!=null && code.equals("500")) {
            modelAndView.addObject("emailError", true);
            modelAndView.addObject("title", "Error");
            modelAndView.addObject("message", "Tienes una sesión activa");
        } else if (code!=null && code.equals("logout")) {
            modelAndView.addObject("success", true);
            modelAndView.addObject("title", "Adios");
            modelAndView.addObject("message", "Has cerrado sesión correctamente");
        }
        return modelAndView;
    }



    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public Object createNewUser(Users user, BindingResult bindingResult) throws ParseException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        Users userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {

            return "redirect:/login?code=01";

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
            modelAndView.addObject("user", new Users());
            System.out.println(user.getEmail() + "\n" + user.getRoles().toString());
            modelAndView.addObject("success", true);


            Properties props = new Properties();

            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            String username = "adidasapifake@gmail.com";
            String password = "boix zfxl jwto ztam";

            // Crear una sesión con la autenticación

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            try {
                // Crear un objeto MimeMessage
                Message message = new MimeMessage(session);

                // Establecer remitente y destinatario
                message.setFrom(new InternetAddress(username));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
                message.setSubject("WELCOME TO ADIDAS API");
                String htmlBody = "<img src=\"https://res.cloudinary.com/dtljcfmr3/image/upload/v1707427892/T%C3%ADtulo_secundario_pxopk9.png\n\" alt=\"Imagen de ejemplo\">"
                        +"<h1>¡BIENVENIDO!</h1>"
                        + "<h2>"+"Hola "+user.getFullname()+"</h2>"
                        + "<p>Nos alegra tu registro, esperamos ser de gran ayuda en tus proyectos.</p>"
                        + "<p>Para comenzar a usar nuestros servicios utiliza este token, estará disponible por un día, luego deberas generar uno nuevo:</p>"
                        + "<p style=\"font-weight: bold;\">"+token+"</p>"
                        + "<p>por ahora eso es todo estaremos más adelante en contacto contigo, saludos.</p>"
                        + "<p style=\"font-size: 16px; font-weight: bold; \">¡IMPOSSIBLE IS NOTHING!</p>";
                message.setContent(htmlBody, "text/html");
                Transport.send(message);

                System.out.println("Correo enviado exitosamente.");
                return "redirect:/login?code=201";

            } catch (MessagingException e) {
                e.printStackTrace();
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


        String username = user.getFullname();
        modelAndView.addObject("currentUser", username);
        correoFinal = user.getEmail();
        Tokens ultimoToken = user.obtenerUltimoToken();

        if (ultimoToken != null) {

            modelAndView.addObject("token", ultimoToken.getToken());
            if (JwtTokenUtil.validateToken(ultimoToken.getToken())) {

                if (JwtTokenUtil.validateAccess(ultimoToken.getToken())){
                    modelAndView.addObject("success", true);
                }else{
                    modelAndView.addObject("error", true);
                    modelAndView.addObject("info","Token sin permisos para crear productos");
                }

            } else {
                modelAndView.addObject("error", true);
                modelAndView.addObject("info","Token vencido.");

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
    public Object RegistreOrLogin () {
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
                if (userExist.isActivo()){
                    return "redirect:/login?code=500";
                }
                userExist.setActivo(true);
                userService.saveSesion(userExist);
                return "redirect:/dashboard/v2/"+userExist.getId()+"/b";
            }else{
                return "redirect:/login?code=02";

            }

        } else if (valor.equals("registro")) {

            if (userExist != null){
                return "redirect:/login?code=01";
            }else{

                String token = null;
                try {
                    token = JwtTokenUtil.generateToken(emailUserGoogle,"","full");
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                methodAuth = "v2";
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
                users.setEnabled(true);
                users.setActivo(true);

                userService.saveUserGoogle(users, tokenFirts);

                Properties props = new Properties();

                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");

                String username = "adidasapifake@gmail.com";
                String password = "boix zfxl jwto ztam";


                Session session = Session.getInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

                try {
                    Message message = new MimeMessage(session);

                    message.setFrom(new InternetAddress(username));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailUserGoogle));
                    message.setSubject("WELCOME TO ADIDAS API");
                    String htmlBody = "<img src=\"https://res.cloudinary.com/dtljcfmr3/image/upload/v1707427892/T%C3%ADtulo_secundario_pxopk9.png\n\" alt=\"Imagen de ejemplo\">"
                            +"<h1>¡BIENVENIDO!</h1>"
                            + "<h2>"+"Hola "+NameUserGoogle+"</h2>"
                            + "<p>Nos alegra tu registro, esperamos ser de gran ayuda en tus proyectos.</p>"
                            + "<p>Para comenzar a usar nuestros servicios utiliza este token, estará disponible por un día, luego deberas generar uno nuevo:</p>"
                            + "<p style=\"font-weight: bold;\">"+token+"</p>"
                            + "<p>por ahora eso es todo estaremos más adelante en contacto contigo, saludos.</p>"
                            + "<p style=\"font-size: 16px; font-weight: bold; \">¡IMPOSSIBLE IS NOTHING!</p>";
                    message.setContent(htmlBody, "text/html");
                    Transport.send(message);
                    System.out.println("Correo enviado exitosamente.");

                    return "redirect:/dashboard/"+methodAuth+"/"+users.getId()+"/b";



                } catch (MessagingException e) {
                    e.printStackTrace();
                    return "redirect:/login?code=Error";
                }

            }

        }

        return modelAndView;


    }



    @RequestMapping(value = "/cerrar", method = RequestMethod.POST)
    public Object logout() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String emailUserGoogle = null;
        Object principal = auth.getPrincipal();

        if (principal instanceof OAuth2User ){
            OAuth2User oauth2User = (OAuth2User) auth.getPrincipal();
            emailUserGoogle = oauth2User.getAttribute("email");
        }

        Users user = userService.findUserByEmail(auth.getName());

        if (user==null){
            user = userService.findUserByEmail(emailUserGoogle);
        }


        user.setActivo(false);
        userService.saveSesion(user);




        return "redirect:/login?code=logout";


    }


}
