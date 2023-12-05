package com.socatra.intellitrack.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("dealerid")
    @Expose
    private String dealerid;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("traderid")
    @Expose
    private String traderid;

    @SerializedName("processorid")
    @Expose
    private String ProcessorId;

    @SerializedName("customerid")
    @Expose
    private String customerid;





    @SerializedName("userName")
    @Expose
    private String userName;

    @SerializedName("role")
    @Expose
    private String role;

    @SerializedName("firstName")
    @Expose
    private String firstName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDealerid() {
        return dealerid;
    }

    public void setDealerid(String dealerid) {
        this.dealerid = dealerid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTraderid() {
        return traderid;
    }

    public void setTraderid(String traderid) {
        this.traderid = traderid;
    }

    public String getProcessorId() {
        return ProcessorId;
    }

    public void setProcessorId(String processorId) {
        this.ProcessorId = processorId;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String Customerid) {
        this.customerid = Customerid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
