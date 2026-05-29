package com.halliasanthe.digital.models;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class Product {
    @DocumentId
    private String id;
    private String name;
    private double price;
    private String imageUrl;
    private String category;
    private String description;
    private String sellerName;
    private String sellerPhone;
    private String sellerUid;
    private boolean inStock;
    @ServerTimestamp
    private Date createdAt;

    public Product() {}

    public Product(String name, double price, String imageUrl, String category,
                   String description, String sellerName, String sellerPhone, String sellerUid) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.description = description;
        this.sellerName = sellerName;
        this.sellerPhone = sellerPhone;
        this.sellerUid = sellerUid;
        this.inStock = true;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getSellerName() { return sellerName; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }
    public String getSellerPhone() { return sellerPhone; }
    public void setSellerPhone(String sellerPhone) { this.sellerPhone = sellerPhone; }
    public String getSellerUid() { return sellerUid; }
    public void setSellerUid(String sellerUid) { this.sellerUid = sellerUid; }
    public boolean isInStock() { return inStock; }
    public void setInStock(boolean inStock) { this.inStock = inStock; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
