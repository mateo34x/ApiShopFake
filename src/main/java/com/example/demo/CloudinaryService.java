package com.example.demo;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    private final Cloudinary cloudinary;

    public CloudinaryService() {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dtljcfmr3",
                "api_key", "789363946539982",
                "api_secret", "Wre06T6ipsyPrYg_k_UmyDYiTDY",
                "secure", true));
    }


    public String uploadFile(MultipartFile file) throws IOException {
        // Subir la imagen a Cloudinary
        Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

        // Devolver la URL de la imagen en Cloudinary
        return (String) result.get("secure_url");
    }

    public void deleteFile(String publicId) throws IOException {
        // Eliminar la imagen de Cloudinary
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}
