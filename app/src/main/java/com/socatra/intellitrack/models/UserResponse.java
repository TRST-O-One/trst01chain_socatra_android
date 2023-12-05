package com.socatra.intellitrack.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserResponse {
    @SerializedName("data")
    private List<UserData> data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private int status;

    public List<UserData> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }


    public class UserData {
        private int id;
        private int dealerid;
        private int traderid;
        private int processorid;
        private String userName;
        private String role;
        private int roleid;
        private String token;
        private String firstName;
        private String lastName;

        public int getId() {
            return id;
        }

        public int getDealerid() {
            return dealerid;
        }

        public int getTraderid() {
            return traderid;
        }

        public int getProcessorid() {
            return processorid;
        }

        public String getUserName() {
            return userName;
        }

        public String getRole() {
            return role;
        }

        public int getRoleid() {
            return roleid;
        }

        public String getToken() {
            return token;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }
    }
}
