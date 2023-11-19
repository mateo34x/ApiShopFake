package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.List;






@RestController
@RequestMapping("/view")
public class ViewData {


   @Autowired
   ItemRepository groceryItemRepo;



    @RequestMapping("/findAll")
    @GetMapping(produces = "application/json")
    public List<GroceryItem> getAllData() {

        return groceryItemRepo.findAll();
    }









}
