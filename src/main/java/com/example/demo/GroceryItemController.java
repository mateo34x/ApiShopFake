package com.example.demo;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Controller
public class GroceryItemController {

    @Autowired
    ItemRepository groceryItemRepo;
    @PostMapping("/guardar")
    public String guardarGroceryItem(@RequestParam("id") String id,
                                     @RequestParam("name") String name,
                                     @RequestParam("quantity") int quantity,
                                     @RequestParam("category") String category,
                                     @RequestParam("img") MultipartFile img) throws IOException {


        byte[] bytes = img.getBytes();
        String encodedString = Base64.getEncoder().encodeToString(bytes);

        groceryItemRepo.save(new GroceryItem(id,name,quantity,category, img.getName(),img.getContentType(),encodedString ));


        return "redirect:/view/findAll"; // Redirige a la página del formulario después de guardar los datos
    }
}
