package com.example.demo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.io.Serializable;

@Document("groceryitems")
public class GroceryItem implements Serializable {

    @Id
    private String id;
    private String name;
    private int quantity;
    private String category;

    private String img;

    public GroceryItem() {
    }

    public GroceryItem(String id, String name, int quantity, String category, String img) {
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
