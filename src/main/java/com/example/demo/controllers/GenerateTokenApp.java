package com.example.demo.controllers;


import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Date;

@RestController
@RequestMapping("/v2App")
public class GenerateTokenApp {

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    private static final String SECRET_KEY = "c307c011160d0c0c4826f6a5eb41bbebc3c132f2528b2de9708886cb7ddd879d";


    //v2App/GeToken/mateo15rg@gmail.com

    @RequestMapping("/GeToken/{email}/{access}")
    public String GenerateToken(@PathVariable() String email,
                           @PathVariable() String access) throws ParseException {

        return JwtTokenUtil.generateToken(email,"",access);
    }

    @RequestMapping("/validateToken/{token}")
    public Boolean ValidateToken(@PathVariable() String token) throws ParseException {

            try {
                Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
                Date expirationDate = claims.getBody().getExpiration();

                long currentTime = System.currentTimeMillis();
                long expirationTime = expirationDate.getTime();

                return  currentTime <= expirationTime;
            } catch (SignatureException | ExpiredJwtException e) {

                return false;
            }


    }

    @RequestMapping("/renewToken/{token}")
    public String RenewToken(@PathVariable() String token) throws ParseException {

        String userRenew = "default";
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            userRenew = (String) claims.getBody().get("sub");
            return JwtTokenUtil.generateToken(userRenew, "", "full");

        }catch (SignatureException e){
            return "Error1";
        } catch (ExpiredJwtException ignored) {
            return "Error2: "+userRenew;
        }



    }


}
