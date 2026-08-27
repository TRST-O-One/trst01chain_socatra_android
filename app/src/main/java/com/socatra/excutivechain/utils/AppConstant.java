package com.socatra.excutivechain.utils;

import android.Manifest;

import java.io.File;

public class AppConstant {
    // TODO: Live URl
    public static String BASE_AUTH_URL = "https://socatra.trst01.com/api/V1/";//Socatra api

    public static String RAW_DATA_URL = BASE_AUTH_URL; // TODO: RAW DATA URL

    //DB
    public static final int DB_VERSION = 1; //Todo DB version
    public static String DB_NAME = "DemoExcutiveChain.db";
    public static String APP_ENVIRONMENT = "DemoExcutiveChain_Prod_DB";//DEV sub folder for db
    public static final String DB_SUB_FOLDER = APP_ENVIRONMENT;

    public static final String DeviceUserID = "DEVICEUSER ID";
    public static final String IsFirst = "IsFirst";
    public static final String DeviceUserName = "DEVICEUSER NAME";
    public static final String DeviceUserPwd = "DEVICEUSER PWD";
    public static final String AgentId = "AgentId";
    public static final String accessToken = "accessToken";
    public static final String FarmerCode = "FARMER CODE";

    public static String SUCCESS_RESPONSE_MESSAGE = "Data saved Successfully";
    public static String FAILURE_RESPONSE_MESSAGE = "failure";

    // TODO: Error messages for Add former
    public static String ERROR_MESSAGE_ENTER_FIRSTNAME = "Please enter farmer First Name";
    public static String ERROR_MESSAGE_ENTER_LASTNAME = "Please enter farmer Last Name";
    public static String ERROR_MESSAGE_ENTER_FATHERNAME = "Please enter farmer Father Name";
    public static String ERROR_MESSAGE_ENTER_ADDRESS = "Please enter farmer Address ";
    public static String ERROR_MESSAGE_ENTER_PINCODE = "Please enter farmer PinCode";
    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSS = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

}
