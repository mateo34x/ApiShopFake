package com.example.demo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/create")
public class Controller {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Value("${images.local.path}")
    private String localImagesPath;

    @Autowired
    ItemRepository groceryItemRepo;



    public String subirImagenLocal() throws IOException {

        InputStream inputStream = new ClassPathResource("static/image1.jpeg").getInputStream();
        ObjectId fileId = gridFsTemplate.store(inputStream, "image1.jpeg", "image/jpeg");
        return fileId.toHexString();
    }

    @GetMapping("/item")
    public String createGroceryItems() throws IOException {

        groceryItemRepo.save(new GroceryItem("Whole Wheat Biscuit", "Whole Wheat Biscuit", 5, "snacks",subirImagenLocal()));
        /*
        groceryItemRepo.save(new GroceryItem("Kodo Millet", "XYZ Kodo Millet healthy", 2, "millets"));
        groceryItemRepo.save(new GroceryItem("Dried Red Chilli", "Dried Whole Red Chilli", 2, "spices"));
        groceryItemRepo.save(new GroceryItem("Pearl Millet", "Healthy Pearl Millet", 1, "millets"));
        groceryItemRepo.save(new GroceryItem("Cheese Crackers", "Bonny Cheese Crackers Plain", 6, "snacks"));
        */

       return "Dato guardado";
    }


    @RequestMapping("/findAll")
    @GetMapping(produces = "application/json")
    public List<GroceryItem> getAllData() {

        return groceryItemRepo.findAll();
    }


}
