package com.example.demo;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Query.query;

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

    public InputStream Consultar(String imageId) throws IOException {

        GridFSFile gridFsFile = gridFsTemplate.findOne(query(Criteria.where("_id").is(new ObjectId(imageId))));

        if (gridFsFile == null) {
            throw new FileNotFoundException("No se encontró la imagen con ID: " + imageId);
        }

        // Obtener el recurso GridFS usando el archivo
        GridFsResource gridFsResource = gridFsTemplate.getResource(gridFsFile);

        return gridFsResource.getInputStream();
    }

    @GetMapping("/imagen/{imageId}")
    public ResponseEntity<InputStreamResource> obtenerImagen(@PathVariable String imageId) {
        try {
            InputStream imageStream = Consultar(imageId);

            InputStreamResource resource = new InputStreamResource(imageStream);

            return ResponseEntity.ok()
                    .contentLength(imageStream.available())
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
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
