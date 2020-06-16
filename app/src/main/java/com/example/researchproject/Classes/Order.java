package com.example.researchproject.Classes;

import java.util.List;

public class Order {
    private Customer customer;
    private String customer_message;
    private String date_created;
    private String date_shipped;
    private int id;
//    private int items_total;
    private String payment_method;
//    private String payment_status;
    private List<ProductOrdered> productsOrdered;
    private Double shipping_cost;
    private String status;
    private Double total;
    private Double total_tax;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getCustomer_message() {
        return customer_message;
    }

    public void setCustomer_message(String customer_message) {
        this.customer_message = customer_message;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getDate_shipped() {
        return date_shipped;
    }

    public void setDate_shipped(String date_shipped) {
        this.date_shipped = date_shipped;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public List<ProductOrdered> getProductsOrdered() {
        return productsOrdered;
    }

    public void setProductsOrdered(List<ProductOrdered> productsOrdered) {
        this.productsOrdered = productsOrdered;
    }

    public Double getShipping_cost() {
        return shipping_cost;
    }

    public void setShipping_cost(Double shipping_cost) {
        this.shipping_cost = shipping_cost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getTotal_tax() {
        return total_tax;
    }

    public void setTotal_tax(Double total_tax) {
        this.total_tax = total_tax;
    }
}
