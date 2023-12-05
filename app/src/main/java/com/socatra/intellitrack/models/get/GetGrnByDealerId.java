package com.socatra.intellitrack.models.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetGrnByDealerId {

    @SerializedName("Id")
    @Expose
    private Integer Id;

    @SerializedName("GRNnumber")
    @Expose
    private String GRNnumber;

    @SerializedName("FarmerCode")
    @Expose
    private String FarmerCode;

    @SerializedName("FarmerName")
    @Expose
    private String FarmerName;

    @SerializedName("Farmeradress")
    @Expose
    private Object Farmeradress;

    @SerializedName("ProcessorId")
    @Expose
    private Object ProcessorId;

    @SerializedName("Dealer")
    @Expose
    private String Dealer;

    @SerializedName("DealerId")
    @Expose
    private Integer DealerId;

    @SerializedName("FarmerId")
    @Expose
    private Integer FarmerId;

    @SerializedName("Quantity")
    @Expose
    private Integer Quantity;

    @SerializedName("CurrentQty")
    @Expose
    private Integer CurrentQty;

    @SerializedName("GRNdate")
    @Expose
    private String GRNdate;

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

    public Integer getId() {
        return Id;
    }

    public void setId(Integer Id) {
        this.Id = Id;
    }

    public String getGRNnumber() {
        return GRNnumber;
    }

    public void setGRNnumber(String GRNnumber) {
        this.GRNnumber = GRNnumber;
    }

    public String getFarmerCode() {
        return FarmerCode;
    }

    public void setFarmerCode(String FarmerCode) {
        this.FarmerCode = FarmerCode;
    }

    public String getFarmerName() {
        return FarmerName;
    }

    public void setFarmerName(String FarmerName) {
        this.FarmerName = FarmerName;
    }

    public Object getFarmeradress() {
        return Farmeradress;
    }

    public void setFarmeradress(Object Farmeradress) {
        this.Farmeradress = Farmeradress;
    }

    public Object getProcessorId() {
        return ProcessorId;
    }

    public void setProcessorId(Object ProcessorId) {
        this.ProcessorId = ProcessorId;
    }

    public String getDealer() {
        return Dealer;
    }

    public void setDealer(String Dealer) {
        this.Dealer = Dealer;
    }

    public Integer getDealerId() {
        return DealerId;
    }

    public void setDealerId(Integer DealerId) {
        this.DealerId = DealerId;
    }

    public Integer getFarmerId() {
        return FarmerId;
    }

    public void setFarmerId(Integer FarmerId) {
        this.FarmerId = FarmerId;
    }

    public Integer getQuantity() {
        return Quantity;
    }

    public void setQuantity(Integer Quantity) {
        this.Quantity = Quantity;
    }

    public Integer getCurrentQty() {
        return CurrentQty;
    }

    public void setCurrentQty(Integer CurrentQty) {
        this.CurrentQty = CurrentQty;
    }

    public String getGRNdate() {
        return GRNdate;
    }

    public void setGRNdate(String GRNdate) {
        this.GRNdate = GRNdate;
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

    public void setCreatedDate(String CreatedDate) {
        this.CreatedDate = CreatedDate;
    }

    public Integer getCreatedByUserId() {
        return CreatedByUserId;
    }

    public void setCreatedByUserId(Integer CreatedByUserId) {
        this.CreatedByUserId = CreatedByUserId;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String UpdatedDate) {
        this.UpdatedDate = UpdatedDate;
    }

    public Integer getUpdatedByUserId() {
        return UpdatedByUserId;
    }

    public void setUpdatedByUserId(Integer UpdatedByUserId) {
        this.UpdatedByUserId = UpdatedByUserId;
    }

    @Override
    public String toString() {
        return GRNnumber;
    }
}

