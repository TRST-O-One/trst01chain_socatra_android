package com.socatra.intellitrack.models.get;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetCustomersbyProcessorId {
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("EntityName")
    @Expose
    private String entityName;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Phone")
    @Expose
    private String phone;
    @SerializedName("VillageName")
    @Expose
    private String villageName;
    @SerializedName("SubDistrictname")
    @Expose
    private String subDistrictname;
    @SerializedName("DistrictorRegencyName")
    @Expose
    private String districtorRegencyName;
    @SerializedName("StateorProvinceName")
    @Expose
    private String stateorProvinceName;
    @SerializedName("CountryName")
    @Expose
    private String countryName;
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

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getSubDistrictname() {
        return subDistrictname;
    }

    public void setSubDistrictname(String subDistrictname) {
        this.subDistrictname = subDistrictname;
    }

    public String getDistrictorRegencyName() {
        return districtorRegencyName;
    }

    public void setDistrictorRegencyName(String districtorRegencyName) {
        this.districtorRegencyName = districtorRegencyName;
    }

    public String getStateorProvinceName() {
        return stateorProvinceName;
    }

    public void setStateorProvinceName(String stateorProvinceName) {
        this.stateorProvinceName = stateorProvinceName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
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
}
