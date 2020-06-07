package com.example.researchproject.Classes;

import java.io.Serializable;

public class Product implements Serializable {
    private Boolean availability;
    private String barcode;
    private String brand_id; //int
    private String categories;  //number??
    private double cost_price; //int
    private String description; //String
    private int id; //int
    private int inventory_level; //int
    private int inventory_warning_level; //int
    private String name; //String
    private double price; //double
    private int reviews_count; //int
    private String review_message; //String
    private int reviews_rating_sum; //int
    private String url_image; //String

    public Product(String barcode, String brand_id, String categories, double cost_price, String description, int id, int inventory_level, int inventory_warning_level, String name, double price, String url_image) {
        this.availability = true;
        this.barcode = barcode;
        this.brand_id = brand_id;
        this.categories = categories;
        this.cost_price = cost_price;
        this.description = description;
        this.id = id;
        this.inventory_level = inventory_level;
        this.inventory_warning_level = inventory_warning_level;
        this.name = name;
        this.price = price;
        this.reviews_count = 0;
        this.review_message = "";
        this.reviews_rating_sum = 0;
        this.url_image = url_image;
    }

    public Product(Boolean availability, String barcode, Double cost_price, String description, int id, int inventory_level, int inventory_warning_level, Double price) {
        this.availability = availability;
        this.barcode = barcode;
        this.brand_id = "";
        this.categories = "";
        this.cost_price = cost_price;
        this.description = description;
        this.id = id;
        this.inventory_level = inventory_level;
        this.inventory_warning_level = inventory_warning_level;
        this.name = "";
        this.price = price;
        this.reviews_count = 0;
        this.review_message = "";
        this.reviews_rating_sum = 0;
        this.url_image = "";
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
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

    public String getReview_message() {
        return review_message;
    }

    public void setReview_message(String review_message) {
        this.review_message = review_message;
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