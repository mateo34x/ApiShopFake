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

@org.springframework.stereotype.Controller
@RequestMapping(path = "/")
public class Controller {





    @GetMapping("/showForm")
    public String showImageUploadForm() {
        return "groceryItemFrom";
    }





   @Autowired
    ItemRepository groceryItemRepo;



    @GetMapping("/item")
    public String createGroceryItems() throws IOException {




        groceryItemRepo.save(new GroceryItem("Kodo Millet", "XYZ Kodo Millet healthy", 2, "millets","null"));



        return "Dato guardado";
    }

    @RequestMapping("/findAll")
    @GetMapping(produces = "application/json")
    public List<GroceryItem> getAllData() {

        return groceryItemRepo.findAll();
    }






}
