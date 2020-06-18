package com.example.researchproject.Classes;

import java.util.List;

public class Cart {
    private String email;
    private List<ProductOrdered> productsCart;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ProductOrdered> getProductsCart() {
        return productsCart;
    }

    public void setProductsCart(List<ProductOrdered> productsCart) {
        this.productsCart = productsCart;
    }
}
