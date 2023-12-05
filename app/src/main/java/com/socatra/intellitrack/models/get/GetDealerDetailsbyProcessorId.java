package com.socatra.intellitrack.models.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetDealerDetailsbyProcessorId {

    @SerializedName("Id")
    @Expose
    private Integer Id;

    @SerializedName("DealerId")
    @Expose
    private String DealerId;

    @SerializedName("ProcessorId")
    @Expose
    private Integer ProcessorId;

    @SerializedName("DealerName")
    @Expose
    private String DealerName;


    @SerializedName("Address")
    @Expose
    private String Address;


    @SerializedName("PrimaryContactNo")
    @Expose
    private Integer PrimaryContactNo;



    @SerializedName("Processor")
    @Expose
    private Integer Processor;


    @SerializedName("IsActive")
    @Expose
    private Boolean IsActive;


    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        this.Id = id;
    }


    public Integer getProcessorId() {
        return ProcessorId;
    }

    public void setProcessorId(Integer processorId) {
        this.ProcessorId = processorId;
    }



    public String getDealerId() {
        return DealerId;
    }

    public void setDealerId(String dealerId) {
        this.DealerId = dealerId;
    }


    public String getDealerName() {
        return DealerName;
    }

    public void setDealerName(String dealerName) {
        this.DealerName = dealerName;
    }

    public String getaddress() {
        return Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public Integer getPrimaryContactNo() {
        return PrimaryContactNo;
    }

    public void setPrimaryContactNo(Integer primaryContactNo) {
        this.PrimaryContactNo = primaryContactNo;
    }


    public Boolean getIsActive() {
        return IsActive;
    }

    public void setIsActive(Boolean isActive) {
        this.IsActive = isActive;
    }





}
