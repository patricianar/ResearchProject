package com.example.researchproject.Classes;

import java.io.Serializable;

public class Address implements Serializable {
    private String city;
    private String country;
    private String postal_code;
    private String province;
    private String street;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String state) {
        this.province = state;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
