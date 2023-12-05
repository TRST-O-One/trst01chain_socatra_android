package com.socatra.excutivechain.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginResponseDTO {
        @SerializedName("data")
        @Expose
        private List<Datum> data;
        @SerializedName("messgae")
        @Expose
        private String messgae;
        @SerializedName("status")
        @Expose
        private Integer status;

        public List<Datum> getData() {
            return data;
        }

        public void setData(List<Datum> data) {
            this.data = data;
        }

        public String getMessgae() {
            return messgae;
        }

        public void setMessgae(String messgae) {
            this.messgae = messgae;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

    }

//    @SerializedName("Id")
//    @Expose
//    private String Id;
//
//    @SerializedName("UserName")
//    @Expose
//    public String UserName ;
//
//    @SerializedName("AgentId")
//    @Expose
//    public String AgentId ;
//
//    @SerializedName("Password")
//    @Expose
//    public String Password ;
//
//    @SerializedName("accessToken")
//    @Expose
//    public String accessToken ;
//
//
//    public String getId() {
//        return Id;
//    }
//
//    public void setId(String id) {
//        Id = id;
//    }
//
//    public String getUserName() {
//        return UserName;
//    }
//
//    public void setUserName(String userName) {
//        UserName = userName;
//    }
//
//    public String getPassword() {
//        return Password;
//    }
//
//    public void setPassword(String password) {
//        Password = password;
//    }
//
//    public String getAccessToken() {
//        return accessToken;
//    }
//
//    public void setAccessToken(String accessToken) {
//        this.accessToken = accessToken;
//    }
//
//    public String getAgentId() {
//        return AgentId;
//    }
//
//    public void setAgentId(String agentId) {
//        AgentId = agentId;
//    }

