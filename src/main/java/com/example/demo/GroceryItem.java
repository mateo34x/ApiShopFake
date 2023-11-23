package com.example.demo;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document("groceryitems")
public class GroceryItem implements Serializable {

    @Id
    private String id;
    private String name;
    private String description;
    private int quantity;
    private String category;
    private String nombre;
    private String mimeType;
    private String fotoBase64;

    public GroceryItem() {
    }

    public GroceryItem(String id, String name,String description, int quantity, String category, String nombre, String mimeType, String fotoBase64) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.quantity = quantity;
        this.category = category;
        this.mimeType = mimeType;
        this.nombre = nombre;
        this.fotoBase64 = fotoBase64;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFotoBase64() {
        return fotoBase64;
    }

    public void setFotoBase64(String fotoBase64) {
        this.fotoBase64 = fotoBase64;
    }

    @Override
    public String toString() {
        return "GroceryItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                ", category='" + category + '\'' +
                ", nombre='" + nombre + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", fotoBase64='" + fotoBase64 + '\'' +
                '}';
    }
}
