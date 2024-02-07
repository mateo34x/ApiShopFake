package com.example.demo;

import com.example.demo.controllers.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
public class GroceryItemController {

    @Autowired
    ItemRepository groceryItemRepo;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @PostMapping("/guardar")
    public Object guardarGroceryItem(@RequestParam("name") String name,
                                     @RequestParam("descripcion") String descripcion,
                                     @RequestParam("price") String price,
                                     @RequestParam("quantity") int quantity,
                                     @RequestParam("token")String token,
                                     @RequestParam("category") String category,
                                     @RequestParam("uploadFile") MultipartFile img1,
                                     @RequestParam("uploadFile2") MultipartFile img2) throws IOException {



        try {

            if (!token.equals("genere un token")){
                if (JwtTokenUtil.validateToken(token)) {
                    String imageUrl1 = cloudinaryService.uploadFile(img1);
                    String imageUrl2 = cloudinaryService.uploadFile(img2);

                    groceryItemRepo.save(new GroceryItem("OOO",name,descripcion,"$"+price,quantity,category, img1.getName(),img1.getContentType(),imageUrl1,imageUrl2 ));

                    return "redirect:/dashboard";

                } else {
                    //
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
                }
            }else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No posee un token para crear productos, genere uno");
            }



        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }



    }

    @PostMapping("/edit")
    public Object editDocument(@RequestParam("token") String token,
                               @RequestParam("id") String id,
                               @RequestParam("img1") MultipartFile img1,
                               @RequestParam("img2") MultipartFile img2) throws IOException {




        Optional<GroceryItem> optional = groceryItemRepo.findById(id);

        if (optional.isPresent()) {


            if(JwtTokenUtil.validateToken(token)){
                String imageUrl1 = cloudinaryService.uploadFile(img1);
                String imageUrl2 = cloudinaryService.uploadFile(img2);

                GroceryItem groceryItem = optional.get();
                groceryItem.setFotoBase1(imageUrl1);
                groceryItem.setFotoBase2(imageUrl2);
                groceryItemRepo.save(groceryItem);

                return "redirect:/photos/" + optional.get().getName();
            }else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");

            }



        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El recurso con id: "+id+" no fue encontrado");

        }


    }




}
