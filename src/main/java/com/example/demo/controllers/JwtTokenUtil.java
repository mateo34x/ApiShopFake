package com.example.demo.controllers;

import io.jsonwebtoken.*;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {
    private static final String SECRET_KEY = "c307c011160d0c0c4826f6a5eb41bbebc3c132f2528b2de9708886cb7ddd879d";

    public static String generateToken(String username,String expDate,String access) throws ParseException {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", "ADMIN");
        claims.put("access",access);


        if (expDate.isEmpty()){
            long expirationTime = System.currentTimeMillis() + 24*60*60*1000;
            claims.put("exp", expirationTime / 1000);
        }else{


            LocalDateTime horaActual = LocalDateTime.now();

            int hora = horaActual.getHour();
            int minutos = horaActual.getMinute();
            int segundos = horaActual.getSecond();

            String Hms = String.format("%02d:%02d:%02d", hora, minutos, segundos);

            long epoch = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(expDate+" "+Hms).getTime() / 1000;

            claims.put("exp",epoch);

        }

        JwtBuilder builder = Jwts.builder().setClaims(claims);
        Map<String, Object> header = new HashMap<>();
        header.put("Authorization", "Bearer");

        builder.signWith(SignatureAlgorithm.HS256, SECRET_KEY).setHeader(header);

        return builder.compact();
    }





    public static boolean validateToken(String token) {
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

    public static boolean validateAccess(String token){

        Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
        String access = (String) claims.getBody().get("access");
        return access.equals("full");
    }
}
