package com.vanard.muze.model;

import com.google.gson.annotations.Expose;

public class DataUser {
    private String email;
    private String name;
    private String numberKtp;
    @Expose
    private String numberNim;
    @Expose
    private String gender;
    @Expose
    private String dateBirth;
    @Expose
    private String subDistrict;
    @Expose
    private String district;
    @Expose
    private String province;

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(String dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getSubDistrict() {
        return subDistrict;
    }

    public void setSubDistrict(String subDistrict) {
        this.subDistrict = subDistrict;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
