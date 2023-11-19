package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GroceryItemController {

    @Autowired
    ItemRepository groceryItemRepo;
    @PostMapping("/guardar")
    public String guardarGroceryItem(@RequestParam("id") String id,
                                     @RequestParam("name") String name,
                                     @RequestParam("quantity") int quantity,
                                     @RequestParam("category") String category,
                                     @RequestParam("img") String img) {

        groceryItemRepo.save(new GroceryItem(id,name,quantity,category,img));


        return "redirect:/view/findAll"; // Redirige a la página del formulario después de guardar los datos
    }
}
