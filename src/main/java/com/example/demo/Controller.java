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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


    public Binary subirImagenLocal() throws IOException {

        /*
        File imageFile = new File("static/image1.jpeg");
        byte[] imageData = new byte[(int) imageFile.length()];
        try (FileInputStream fileInputStream = new FileInputStream(imageFile)) {
            fileInputStream.read(imageData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream inputStream = new ClassPathResource("static/image1.jpeg").getInputStream();
        ObjectId fileId = gridFsTemplate.store(inputStream, "image1.jpeg", "image/jpeg");
        return fileId.toHexString();

         */

        File imageFile = new File("static/image1.jpeg");
        byte[] imageBytes = FileUtils.readFileToByteArray(imageFile);
        return new Binary(BsonBinarySubType.BINARY, imageBytes);


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
                    .contentType(MediaType.valueOf("image/jpeg"))
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/item")
    public String createGroceryItems() throws IOException {

        String imagePath = "static/image1.jpeg";

        byte[] imageBytes = cargarContenidoImagen(imagePath);

        Binary imgBinary = new Binary(imageBytes);

        groceryItemRepo.save(new GroceryItem("AdidasTwo", "AdidasTwo", 1, "shoes", imgBinary));
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


    private byte[] cargarContenidoImagen(String imagePath) throws IOException {
        Path path = Paths.get(imagePath);

        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new IOException("Error al cargar el contenido de la imagen", e);
        }

    }


}
