package com.example.researchproject.Classes;

import java.io.Serializable;

public class Customer implements Serializable
{
    private Address address;
    private Card card;
    private String email;
    private String first_name;
    private String id;
    private String last_name;
    private String password;
    private String phone;

    public Customer()
    {
        //default constructor
    }
    public Customer(String email, String id, String first_name, String last_name, String password) {
        this.email = email;
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.password = password;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddresses(Address address) {
        this.address = address;
    }
    
    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
