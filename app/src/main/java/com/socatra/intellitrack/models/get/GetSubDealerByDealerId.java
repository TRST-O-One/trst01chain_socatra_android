package com.socatra.intellitrack.models.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetSubDealerByDealerId {

    @SerializedName("Id")
    @Expose
    private Integer Id;

    @SerializedName("DealerId")
    @Expose
    private Integer DealerId;

    @SerializedName("SubDealerId")
    @Expose
    private Integer SubDealerId;

    @SerializedName("DealerName")
    @Expose
    private String DealerName;


    @SerializedName("SubDealerAddress")
    @Expose
    private String SubDealerAddress;


    @SerializedName("SubDealerContact")
    @Expose
    private Integer SubDealerContact;



    @SerializedName("SubDealerName")
    @Expose
    private String SubDealerName;


    @SerializedName("IsActive")
    @Expose
    private Boolean IsActive;


    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        this.Id = id;
    }


    public Integer getSubDealerId() {
        return SubDealerId;
    }

    public void setSubDealerId(Integer SubDealerId) {
        this.SubDealerId = SubDealerId;
    }



    public Integer getDealerId() {
        return DealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.DealerId = dealerId;
    }


    public String getDealerName() {
        return DealerName;
    }

    public void setDealerName(String dealerName) {
        this.DealerName = dealerName;
    }



    public String getSubDealerName() {
        return SubDealerName;
    }

    public void setSubDealerName(String SubDealerName) {
        this.SubDealerName = SubDealerName;
    }



    public String getSubDealerAddress() {
        return SubDealerAddress;
    }

    public void setSubDealerAddress(String SubDealerAddress) {
        this.SubDealerAddress = SubDealerAddress;
    }

    public Integer getSubDealerContact() {
        return SubDealerContact;
    }

    public void setSubDealerContact(Integer SubDealerContact) {
        this.SubDealerContact = SubDealerContact;
    }


    public Boolean getIsActive() {
        return IsActive;
    }

    public void setIsActive(Boolean isActive) {
        this.IsActive = isActive;
    }

}

