package com.example.demo;


import com.example.demo.controllers.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;


@RestController
@RequestMapping("/view")
public class ViewData {

   @Autowired
   ItemRepository groceryItemRepo;

   @Autowired
   JwtTokenUtil jwtTokenUtil;


    @RequestMapping("/findAll")
    @GetMapping(produces = "application/json")
    public Object getAllData(@RequestHeader("Authorization") String AuthorizationHeader) {

        String token = extractToken(AuthorizationHeader);

        if (JwtTokenUtil.validateToken(token)) {
            return groceryItemRepo.findAll();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
        }
    }


    private String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }








}
