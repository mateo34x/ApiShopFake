package com.example.demo;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Query.query;

;

@RestController
@RequestMapping("/create")
public class Controller {

    @Autowired
    private GridFsTemplate gridFsTemplate;


    @Autowired
    ItemRepository groceryItemRepo;



    @GetMapping("/item")
    public String createGroceryItems() throws IOException {



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


    @PostMapping("/api/groceryitems/uploadImage")
    @Autowired
    public String handleImageUpload(@RequestParam("file") MultipartFile file) {
        // Aquí puedes guardar la imagen en el sistema de archivos o en una base de datos, según tus necesidades
        // También puedes asociar la imagen con un GroceryItem específico
        // Retorna la URL de la imagen o cualquier otra información relevante


        return "Image uploaded successfully!";
    }



}
