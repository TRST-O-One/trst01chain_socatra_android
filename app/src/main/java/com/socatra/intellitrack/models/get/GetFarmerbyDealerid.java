package com.socatra.intellitrack.models.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetFarmerbyDealerid {

    @SerializedName("Id")
    @Expose
    private Integer Id;
    @SerializedName("FarmerId")
    @Expose
    private Integer FarmerId;
    @SerializedName("FarmerCode")
    @Expose
    private String FarmerCode;
    @SerializedName("FarmerName")
    @Expose
    private String FarmerName;
    @SerializedName("DealerId")
    @Expose
    private Integer DealerId;
    @SerializedName("DealerName")
    @Expose
    private String DealerName;
    @SerializedName("IsActive")
    @Expose
    private Boolean IsActive;
    @SerializedName("Age")
    @Expose
    private Integer Age;
    @SerializedName("PrimaryContactNo")
    @Expose
    private String PrimaryContactNo;
    @SerializedName("Address")
    @Expose
    private String Address;
    @SerializedName("VillageName")
    @Expose
    private String VillageName;
    @SerializedName("PinCode")
    @Expose
    private String PinCode;
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




    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        this.Id = id;
    }

    public Integer getFarmerId() {
        return FarmerId;
    }

    public void setFarmerId(Integer farmerId) {
        this.FarmerId = farmerId;
    }

    public String getFarmerCode() {
        return FarmerCode;
    }

    public void setFarmerCode(String farmerCode) {
        this.FarmerCode = farmerCode;
    }

    public String getFarmerName() {
        return FarmerName;
    }

    public void setFarmerName(String farmerName) {
        this.FarmerName = farmerName;
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

    public Boolean getIsActive() {
        return IsActive;
    }

    public void setIsActive(Boolean isActive) {
        this.IsActive = isActive;
    }

    public Integer getAge() {
        return Age;
    }

    public void setAge(Integer age) {
        this.Age = age;
    }

    public String getPrimaryContactNo() {
        return PrimaryContactNo;
    }

    public void setPrimaryContactNo(String primaryContactNo) {
        this.PrimaryContactNo = primaryContactNo;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public String getVillageName() {
        return VillageName;
    }

    public void setVillageName(String villageName) {
        this.VillageName = villageName;
    }

    public String getPinCode() {
        return PinCode;
    }

    public void setPinCode(String pinCode) {
        this.PinCode = pinCode;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        this.CreatedDate = createdDate;
    }

    public Integer getCreatedByUserId() {
        return CreatedByUserId;
    }

    public void setCreatedByUserId(Integer createdByUserId) {
        this.CreatedByUserId = createdByUserId;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.UpdatedDate = updatedDate;
    }

    public Integer getUpdatedByUserId() {
        return UpdatedByUserId;
    }

    public void setUpdatedByUserId(Integer updatedByUserId) {
        this.UpdatedByUserId = updatedByUserId;
    }

}
