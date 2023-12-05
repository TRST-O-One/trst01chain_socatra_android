package com.socatra.intellitrack.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


@Entity
public class UserLoginTable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @SerializedName("Id")
    @Expose(serialize = false, deserialize = false)
    private int id_new;

    public int getId_new() {
        return id_new;
    }

    public void setId_new(int id_new) {
        this.id_new = id_new;
    }

    @SerializedName("Id")
    @Expose
    private int Id;

    @SerializedName("username")
    @Expose
    private String username = "";

    //Code for Only Bank
    @SerializedName("password")
    @Expose
    private String password = "";

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

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