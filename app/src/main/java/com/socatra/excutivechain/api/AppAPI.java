package com.socatra.excutivechain.api;


import com.google.gson.JsonElement;
import com.socatra.excutivechain.models.LoginResponseDTO;
import com.socatra.excutivechain.models.SyncPersonalLandAllDetailsRequestDTO;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface AppAPI {

    //Login
    @GET("User/GetUserByIMEINo/{userId}")
    Call<LoginResponseDTO> getlogInService(@Path("userId") String userId);

    @GET("Master/GetMasterDetails")
    Call<JsonElement> getMasterSyncDetailsFromServer(@Header("Authorization") String authHeader);//https://trst01chainrubber.trst01.com/api/v1/Master/GetMasterDetails
    @GET("Transaction/GetTransactionDetailsByIMEINo/{userId}")
    Call<JsonElement> getFarmerAllSyncDataDetailsFromServer(@Path("userId") String userId,@Header("Authorization") String authHeader);
    //Transaction/GetTransactionDetailsByIMEINo/4c3b2e586efcade7

    @POST("Transaction/AddTransactionDetails")//http://localhost:4000/V1/Transaction/AddTransactionDetails
    Call<ResponseBody> syncFarmerDetailsDataToServer(@Body SyncPersonalLandAllDetailsRequestDTO syncPersonalLandAllDetailsRequestDTO, @Header("Authorization") String authHeader);

    @Multipart
    @POST("Image/UploadImages")//https://trst01chainrubber.trst01.com/api/v1/Image/UploadImages
    Call<ResponseBody> uploadFileDataToServer(@Part("UserId") RequestBody api_key, @Part MultipartBody.Part file);

    @Multipart
    @POST("PDFUpload/UploadPDF")//http://localhost:4000/V1/PDFUpload/UploadPDF
    Call<ResponseBody> uploadPdfDataToServer(@Part("UserId") RequestBody api_key, @Part MultipartBody.Part file);


  /*  @Multipart
    @POST("Upload/UploadFiles")
    Call<ResponseBody> uploadSHPFileDataToServer(@Part("UserId") RequestBody api_key, @Part MultipartBody.Part file);*/


    @Multipart
    @POST("Upload/UploadFiles")
    Call<ResponseBody> uploadShapeFileToserver(@Part("plotcode") RequestBody plot_code, @Part MultipartBody.Part file);



    //Old
    @Multipart
    @POST("TabDatabase/UploadTabDatabaseFileByVoluntaryId")
    Call<ResponseBody> uploadDatabasefileDataToServer(@Part("AgentId") RequestBody api_key,@Part("VoluntaryId") RequestBody api_key2, @Part MultipartBody.Part file);


    @Multipart
    @POST("TabDatabase/UploadDatabase")
    Call<ResponseBody> uploadDatabasefileDataToServer(@Part("UserId") RequestBody api_key, @Part MultipartBody.Part file);

//    @POST("Sync/AddVoluntaryTransactionDetails")
//    Call<ResponseBody> syncFarmerDetailsDataToServer(@Body SyncPersonalLandAllDetailsRequestDTO syncPersonalLandAllDetailsRequestDTO, @Header("Authorization") String authHeader);

}