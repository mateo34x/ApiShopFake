package com.example.demo;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Controller
public class GroceryItemController {

    @Autowired
    ItemRepository groceryItemRepo;

    @Autowired
    private CloudinaryService cloudinaryService;
    @PostMapping("/guardar")
    public String guardarGroceryItem(@RequestParam("id") String id,
                                     @RequestParam("name") String name,
                                     @RequestParam("descripcion") String descripcion,
                                     @RequestParam("price") String price,
                                     @RequestParam("quantity") int quantity,
                                     @RequestParam("category") String category,
                                     @RequestParam("img1") MultipartFile img1,
                                     @RequestParam("img2") MultipartFile img2) throws IOException {


        /*byte[] bytes = img1.getBytes();
        String encodedString = Base64.getEncoder().encodeToString(bytes);

        byte[] bytes2 = img2.getBytes();
        String encodedString2 = Base64.getEncoder().encodeToString(bytes2);*/


        try {
            String imageUrl1 = cloudinaryService.uploadFile(img1);
            String imageUrl2 = cloudinaryService.uploadFile(img2);

            groceryItemRepo.save(new GroceryItem(id,name,descripcion,"$"+price,quantity,category, img1.getName(),img1.getContentType(),imageUrl1,imageUrl2 ));

            return "redirect:/view/findAll";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }



    }

    @PostMapping("/edit")
    public String editDocument(@RequestParam("id") String id,
                               @RequestParam("img1") MultipartFile img1,
                               @RequestParam("img2") MultipartFile img2) throws IOException {




        Optional<GroceryItem> optional = groceryItemRepo.findById(id);

        if (optional.isPresent()){

            byte[] bytes = img1.getBytes();
            String encodedString = Base64.getEncoder().encodeToString(bytes);

            byte[] bytes2 = img2.getBytes();
            String encodedString2 = Base64.getEncoder().encodeToString(bytes2);


            GroceryItem groceryItem = optional.get();
            groceryItem.setFotoBase1(encodedString);
            groceryItem.setFotoBase2(encodedString2);
            groceryItemRepo.save(groceryItem);

            return "redirect:/photos/"+optional.get().getName();
        }else{
            return "Document with "+id+ " not found, try again";
        }
    }




}
