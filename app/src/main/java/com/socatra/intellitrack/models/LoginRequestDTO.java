package com.socatra.intellitrack.models;

public class LoginRequestDTO {
    String DeviceID;
//    String Password;
//    String Key="ValidateUserLogin";
//    String UserSystemName="Mobile";
//    String UserIPAddress="Mobile";


    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }
}
