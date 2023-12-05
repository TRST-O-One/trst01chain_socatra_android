package com.socatra.intellitrack.models.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetGrndetails {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("GRNnumber")
    @Expose
    private String gRNnumber;
    @SerializedName("FarmerCode")
    @Expose
    private String farmerCode;
    @SerializedName("FarmerName")
    @Expose
    private String farmerName;
    @SerializedName("farmeradress")
    @Expose
    private Object farmeradress;
    @SerializedName("ProcessorId")
    @Expose
    private Integer processorId;

    @SerializedName("Processor")
    @Expose
    private String Processor;
    @SerializedName("Dealer")
    @Expose
    private String dealer;
    @SerializedName("DealerId")
    @Expose
    private Integer dealerId;

    @SerializedName("PrimaryContactNo")
    @Expose
    private String PrimaryContactNo;


    @SerializedName("FarmerId")
    @Expose
    private Integer farmerId;
    @SerializedName("Quantity")
    @Expose
    private Integer quantity;
    @SerializedName("CurrentQty")
    @Expose
    private Integer currentQty;
    @SerializedName("GRNdate")
    @Expose
    private String gRNdate;



    @SerializedName("GRNDocument")
    @Expose
    private String GRNDocument;


    @SerializedName("ShippingToName")
    @Expose
    private String shippingToName;



    @SerializedName("ToDealerName")
    @Expose
    private String ToDealerName;

    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("CreatedByUserId")
    @Expose
    private Integer createdByUserId;
    @SerializedName("UpdatedDate")
    @Expose
    private String updatedDate;
    @SerializedName("UpdatedByUserId")
    @Expose
    private Integer updatedByUserId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGRNnumber() {
        return gRNnumber;
    }

    public void setGRNnumber(String gRNnumber) {
        this.gRNnumber = gRNnumber;
    }

    public String getFarmerCode() {
        return farmerCode;
    }

    public void setFarmerCode(String farmerCode) {
        this.farmerCode = farmerCode;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public Object getFarmeradress() {
        return farmeradress;
    }

    public void setFarmeradress(Object farmeradress) {
        this.farmeradress = farmeradress;
    }

    public Integer getProcessorId() {
        return processorId;
    }

    public void setProcessorId(Integer processorId) {
        this.processorId = processorId;
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    public String getGRNDocument() {
        return GRNDocument;
    }

    public void setGRNDocument(String GRNDocument) {
        this.GRNDocument = GRNDocument;
    }

    public String getProcessor() {
        return Processor;
    }

    public void setProcessor(String Processor) {
        this.Processor = Processor;
    }

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }

    public String getPrimaryContactNo() {
        return PrimaryContactNo;
    }

    public void setPrimaryContactNo(String PrimaryContactNo) {
        this.PrimaryContactNo = PrimaryContactNo;
    }

    public Integer getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(Integer farmerId) {
        this.farmerId = farmerId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getCurrentQty() {
        return currentQty;
    }

    public void setCurrentQty(Integer currentQty) {
        this.currentQty = currentQty;
    }

    public String getGRNdate() {
        return gRNdate;
    }

    public void setGRNdate(String gRNdate) {
        this.gRNdate = gRNdate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(Integer createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Integer getUpdatedByUserId() {
        return updatedByUserId;
    }

    public void setUpdatedByUserId(Integer updatedByUserId) {
        this.updatedByUserId = updatedByUserId;
    }

    public String getgRNnumber() {
        return gRNnumber;
    }

    public void setgRNnumber(String gRNnumber) {
        this.gRNnumber = gRNnumber;
    }

    public String getgRNdate() {
        return gRNdate;
    }

    public void setgRNdate(String gRNdate) {
        this.gRNdate = gRNdate;
    }

    public String getToDealerName() {
        return ToDealerName;
    }

    public void setToDealerName(String toDealerName) {
        ToDealerName = toDealerName;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }



}
