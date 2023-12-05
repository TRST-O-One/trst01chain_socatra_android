package com.socatra.excutivechain.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class AppLanguageHDRTable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @SerializedName("ServerId")
    @Expose
    private Integer ServerId;


    @SerializedName("LanguageId")
    @Expose(serialize = false, deserialize = false)
    private int LanguageId;

    @SerializedName("LanguageName")
    @Expose
    private String LanguageName;


    @SerializedName("IsActive")
    @Expose
    private Boolean IsActive;
    @SerializedName("CreatedDate")
    @Expose
    private String CreatedDate;
    @SerializedName("CreatedByUserId")
    @Expose
    private Integer CreatedByUserId;
    @SerializedName("UpdatedDate")
    @Expose
    private String UpdatedDate;
    @SerializedName("UpdatedByUserId")
    @Expose
    private Integer UpdatedByUserId;


    public int getLanguageId() {
        return LanguageId;
    }

    public void setLanguageId(int languageId) {
        LanguageId = languageId;
    }

    public Integer getServerId() {
        return ServerId;
    }

    public void setServerId(Integer serverId) {
        ServerId = serverId;
    }

    public String getLanguageName() {
        return LanguageName;
    }

    public void setLanguageName(String languageName) {
        LanguageName = languageName;
    }


    public Boolean getIsActive() {
        return IsActive;
    }

    public void setIsActive(Boolean IsActive) {
        this.IsActive = IsActive;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public Integer getCreatedByUserId() {
        return CreatedByUserId;
    }

    public void setCreatedByUserId(Integer createdByUserId) {
        CreatedByUserId = createdByUserId;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        UpdatedDate = updatedDate;
    }

    public Integer getUpdatedByUserId() {
        return UpdatedByUserId;
    }

    public void setUpdatedByUserId(Integer updatedByUserId) {
        UpdatedByUserId = updatedByUserId;
    }

    @Override
    public String toString() {
        return LanguageName;
    }
}
