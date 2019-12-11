package com.vanard.muze.model;

public class DataGuider {
    private String name;
    private String museum;
    private String phoneNumber;

    public DataGuider() {
    }

    public DataGuider(String name, String museum, String phoneNumber) {
        this.name = name;
        this.museum = museum;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMuseum() {
        return museum;
    }

    public void setMuseum(String museum) {
        this.museum = museum;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
