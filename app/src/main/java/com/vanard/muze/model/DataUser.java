package com.vanard.muze.model;

import com.google.gson.annotations.Expose;

public class DataUser {
    private String email;
    private String name;
    private String numberKtp;
    @Expose
    private String numberNim;

    public DataUser() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumberKtp() {
        return numberKtp;
    }

    public void setNumberKtp(String numberKtp) {
        this.numberKtp = numberKtp;
    }

    public String getNumberNim() {
        return numberNim;
    }

    public void setNumberNim(String numberNim) {
        this.numberNim = numberNim;
    }
}
