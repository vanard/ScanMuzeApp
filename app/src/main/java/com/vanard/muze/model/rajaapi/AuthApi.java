package com.vanard.muze.model.rajaapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthApi {

    @Expose
    @SerializedName("token")
    private String token;
    @Expose
    @SerializedName("success")
    private boolean success;
    @Expose
    @SerializedName("code")
    private int code;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
