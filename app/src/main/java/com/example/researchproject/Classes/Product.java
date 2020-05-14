package com.example.researchproject.Classes;

public class Product {
    private String availability; //String
    private String barcode;
    private int base_variant_id; //array( undefined )
    private int brand_id; //int
    private int categories;  //array( number) ???
    private double cost_price; //int
    private String description; //String
    private int id; //int
    private int inventory_level; //int
    private int inventory_warning_level; //int
    private String name; //String
    private String preorder_message; //String
    private double price; //double
    private int reviews_count; //int
    private int reviews_rating_sum; //int
    private String url_image; //String

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getBase_variant_id() {
        return base_variant_id;
    }

    public void setBase_variant_id(int base_variant_id) {
        this.base_variant_id = base_variant_id;
    }

    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
    }

    public int getCategories() {
        return categories;
    }

    public void setCategories(int categories) {
        this.categories = categories;
    }

    public double getCost_price() {
        return cost_price;
    }

    public void setCost_price(double cost_price) {
        this.cost_price = cost_price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInventory_level() {
        return inventory_level;
    }

    public void setInventory_level(int inventory_level) {
        this.inventory_level = inventory_level;
    }

    public int getInventory_warning_level() {
        return inventory_warning_level;
    }

    public void setInventory_warning_level(int inventory_warning_level) {
        this.inventory_warning_level = inventory_warning_level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPreorder_message() {
        return preorder_message;
    }

    public void setPreorder_message(String preorder_message) {
        this.preorder_message = preorder_message;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getReviews_count() {
        return reviews_count;
    }

    public void setReviews_count(int reviews_count) {
        this.reviews_count = reviews_count;
    }

    public int getReviews_rating_sum() {
        return reviews_rating_sum;
    }

    public void setReviews_rating_sum(int reviews_rating_sum) {
        this.reviews_rating_sum = reviews_rating_sum;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }
}