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
    public Object getAllData(@RequestParam("token") String token) {
        if (JwtTokenUtil.validateToken(token)) {
            return groceryItemRepo.findAll();
        } else {
            // Aquí puedes manejar la respuesta de error, por ejemplo, devolver un objeto ResponseEntity con un mensaje de error.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
        }
    }









}
