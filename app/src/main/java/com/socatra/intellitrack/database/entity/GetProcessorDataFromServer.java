package com.socatra.intellitrack.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class GetProcessorDataFromServer {
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

    @SerializedName("DealerId")
    @Expose
    private String DealerId;

    @SerializedName("DealerName")
    @Expose
    private String DealerName;

    @SerializedName("ProcessorId")
    @Expose
    private String ProcessorId;

    @SerializedName("Processor")
    @Expose
    private String Processor;

    @SerializedName("Address")
    @Expose
    private String Address;


    @SerializedName("PrimaryContactNo")
    @Expose
    private String PrimaryContactNo;

    @SerializedName("IsActive")
    @Expose
    private String IsActive = "true";


    public String getDealerId() {
        return DealerId;
    }

    public void setDealerId(String dealerId) {
        DealerId = dealerId;
    }

    public String getDealerName() {
        return DealerName;
    }

    public void setDealerName(String dealerName) {
        DealerName = dealerName;
    }

    public String getProcessorId() {
        return ProcessorId;
    }

    public void setProcessorId(String processorId) {
        ProcessorId = processorId;
    }

    public String getProcessor() {
        return Processor;
    }

    public void setProcessor(String processor) {
        Processor = processor;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPrimaryContactNo() {
        return PrimaryContactNo;
    }

    public void setPrimaryContactNo(String primaryContactNo) {
        PrimaryContactNo = primaryContactNo;
    }

    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String isActive) {
        IsActive = isActive;
    }
}