package com.socatra.intellitrack.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SyncLoginDetailsSubmitRequestDTO {




    @SerializedName("username")
    @Expose
    private String username = "";

    //Code for Only Bank
    @SerializedName("password")
    @Expose
    private String password = "";



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}