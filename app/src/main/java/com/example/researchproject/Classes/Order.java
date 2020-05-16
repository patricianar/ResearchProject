package com.example.researchproject.Classes;

import java.util.List;

public class Order {
        private String cart_id;
        private Customer customer;
        private String customer_message;
        private String date_created;
        private String date_shipped;
        private int id;
        private int items_total;
        private String payment_method;
        private String payment_status;
        private List<ProductOrdered> productsOrdered;
        private Double shipping_cost;
        private String status;
        private Double total;
        private Double total_tax;
}
