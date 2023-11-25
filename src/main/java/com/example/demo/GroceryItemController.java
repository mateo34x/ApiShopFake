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
                                     @RequestParam("descripcion") String descripcion,
                                     @RequestParam("price") String price,
                                     @RequestParam("quantity") int quantity,
                                     @RequestParam("category") String category,
                                     @RequestParam("img1") MultipartFile img1,
                                     @RequestParam("img2") MultipartFile img2) throws IOException {


        byte[] bytes = img1.getBytes();
        String encodedString = Base64.getEncoder().encodeToString(bytes);

        byte[] bytes2 = img2.getBytes();
        String encodedString2 = Base64.getEncoder().encodeToString(bytes2);

        groceryItemRepo.save(new GroceryItem(id,name,descripcion,"$"+price,quantity,category, img1.getName(),img1.getContentType(),encodedString,encodedString2 ));


        return "redirect:/view/findAll"; // Redirige a la página del formulario después de guardar los datos
    }
}
