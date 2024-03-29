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
    private String price;
    private int quantity;
    private String category;
    private String nombre;
    private String mimeType;
    private String fotoBase1;
    private String fotoBase2;


    public GroceryItem() {
    }

    public GroceryItem(String id, String name,String description,String price, int quantity, String category, String nombre, String mimeType, String fotoBase1, String fotoBase2) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.name = name;
        this.quantity = quantity;
        this.category = category;
        this.mimeType = mimeType;
        this.nombre = nombre;
        this.fotoBase1 = fotoBase1;
        this.fotoBase2 = fotoBase2;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getFotoBase1() {
        return fotoBase1;
    }

    public void setFotoBase1(String fotoBase1) {
        this.fotoBase1 = fotoBase1;
    }

    public String getFotoBase2() {
        return fotoBase2;
    }

    public void setFotoBase2(String fotoBase2) {
        this.fotoBase2 = fotoBase2;
    }

    @Override
    public String toString() {
        return "GroceryItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", quantity=" + quantity +
                ", category='" + category + '\'' +
                ", nombre='" + nombre + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", fotoBase1='" + fotoBase1 + '\'' +
                ", fotoBase2'" + fotoBase2 + '\''+
                '}';
    }
}
