package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Base64;

@Controller
public class NewController {
    @Autowired
    ItemRepository groceryItemRepo;


    @GetMapping("/formulario")
    public String mostrarFormulario() {
        return "formulario";
    }


    public GroceryItem getImage(String id) {
        return groceryItemRepo.findItemByName(id);
    }

    @GetMapping("/photos/{id}")
    public String getPhoto(@PathVariable String id, Model model) {
        model.addAttribute("foto", getImage(id));
        return "photos";
    }
}
