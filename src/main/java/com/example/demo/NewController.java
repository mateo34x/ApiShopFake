package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class NewController {
    @Autowired
    ItemRepository groceryItemRepo;


    @GetMapping("/formulario")
    public String mostrarFormulario() {
        return "formtest";
    }

    @GetMapping("/dataDash")
    public String dataDash() {
        return "dashboarddata";
    }

    @GetMapping("/TokenExp")
    public String Exist() {
        return "Exist";
    }

    @GetMapping("/edit")
    public String mostrarEdit() {
        return "EditDocument";
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
