package com.example.demo;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("groceryitems")
public class GroceryItem {

    @Id
    private String id;
    private String name;
    private int quantity;
    private String category;
    private Binary img;


    public GroceryItem(String id, String name, int quantity, String category, Binary img) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.category = category;
        this.img = img;
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

    public Binary getImg() {
        return img;
    }

    public void setImg(Binary img) {
        this.img = img;
    }
}
