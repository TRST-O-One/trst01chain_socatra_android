package com.socatra.intellitrack.api;


import com.google.gson.JsonElement;
import com.socatra.intellitrack.database.entity.GetModeOfTransportDataFromServerTable;
import com.socatra.intellitrack.models.LoginResponseDTO;
import com.socatra.intellitrack.models.SyncAllInvoiceDetailsSubmitRequestDTO;
import com.socatra.intellitrack.models.SyncBatchDtlDetailsSubmitRequestDTO;
import com.socatra.intellitrack.models.SyncBatchHdrDetailsSubmitRequestDTO;
import com.socatra.intellitrack.models.SyncGRNDetailsSubmitRequestDTO;
import com.socatra.intellitrack.models.SyncInvoiceDetailsSubmitRequestDTO;
import com.socatra.intellitrack.models.SyncLoginDetailsSubmitRequestDTO;
import com.socatra.intellitrack.models.SyncPersonalLandAllDetailsRequestDTO;
import com.socatra.intellitrack.models.SyncProcessorInvoiceDetailsSubmitRequestDTO;
import com.socatra.intellitrack.models.SyncQualityCheckDetailsSubmitRequestDTO;
import com.socatra.intellitrack.models.get.GetGrnData;
import com.socatra.intellitrack.models.get.GetGrnDrcData;
import com.socatra.intellitrack.models.get.GetGrnProcessorData;
import com.socatra.intellitrack.models.post.PostDRCDtlDetailsDTO;
import com.socatra.intellitrack.models.post.PostDRCHdrDetailsDTO;

import java.util.List;

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
    @GET("User/ValidateUser/{username},{password}")
    Call<LoginResponseDTO> getlogInService(@Path("username") String userId, @Path("password") String userPwd);


    @POST("User/ValidateUser")
    Call<LoginResponseDTO> postlogInService(@Body SyncLoginDetailsSubmitRequestDTO syncLoginDetailsSubmitRequestDTO);

    @POST("User/ValidateUser")
    Call<JsonElement> postlogInServiceNew(@Body SyncLoginDetailsSubmitRequestDTO syncLoginDetailsSubmitRequestDTO);

    @GET("Master/GetMasterDetails")
//https://trst01chainrubber.trst01.com/api/v1/Master/GetMasterDetails
    Call<JsonElement> getMasterSyncDetailsFromServer(@Header("Authorization") String authHeader);


    //Todo: Navigation GRN Flow Api's

    //Farmer dropdown in dealer login
    @GET("DealerFarmer/GetDealerFarmerMappingByDealerId/{DealerId}")
    //http://4.240.93.105/gajahrukuapi/M1/DealerFarmer/GetDealerFarmerMappingByDealerId/10
    Call<JsonElement> getFarmerAllSyncDataDetailsFromServer(@Path("DealerId") String DealerId, @Header("Authorization") String authHeader);

    //Farmer dropdown in processor login
    @GET("ProcessorFarmer/GetProcessorFarmerMappingByProcessorId/{ProcessorId}")
    //http://4.240.93.105/gajahrukuapi/M1/ProcessorFarmer/GetProcessorFarmerMappingByProcessorId/1
    Call<JsonElement> getFarmerListByManufactureFromServer(@Path("ProcessorId") String ProcessorId, @Header("Authorization") String authHeader);

    //Dealer dropdown in processor login
    @GET("Dealer/GetDealerByProcessorId/{ProcessorId}")
    Call<JsonElement> getDealerDataByManufactureFromServer(@Path("ProcessorId") String ProcessorId, @Header("Authorization") String authHeader);

    //GRN List in Dealer login
    @GET("GRN/getGRNDetailsByDealerId/{DealerId}")
    Call<JsonElement> getGrnDetailsByDealerIdFromServer(@Path("DealerId") String DealerId, @Header("Authorization") String authHeader);

    //GRN List in Processor login
    @GET("GRN/GetGRNdetailsByProcessorId/{ProcessorId}")
    Call<JsonElement> getGrnDetailsByProcessorIdFromServer(@Path("ProcessorId") String ProcessorId, @Header("Authorization") String authHeader);


    //Todo: Navigation DRC Flow Api's

    //GRN details dropdown under the DRC  in processor login
    @GET("DRCDtl/GetDRCdetailsById/{ProcessorId}")
    Call<GetGrnDrcData> getDrcGRNDataByProcessorIdFromServer(@Path("ProcessorId") String ProcessorId, @Header("Authorization") String authHeader);

    @GET("DRCDtl/GetGRNDetailsForDRCByProcessorId/{ProcessorId}")
    Call<GetGrnDrcData> getDrcGRNDataForDrcByProcessorIdFromServer(@Path("ProcessorId") String ProcessorId, @Header("Authorization") String authHeader);

    //DRC List
    @GET("DRCHdr/GetDRCDetailByProcessorId/{ProcessorId}")
    Call<JsonElement> getDrcDetailsByProcessorIdFromServer(@Path("ProcessorId") String ProcessorId, @Header("Authorization") String authHeader);


    //Todo: Navigation Batch Processing Flow Api's


    //GRN details dropdown under the batch  in Dealer login
    @GET("GRN/getGRNDetailsByDealerId/{DealerId}")
    Call<GetGrnData> getGRNDataByDealerIdFromServer(@Path("DealerId") String DealerId, @Header("Authorization") String authHeader);


    //GRN details dropdown under the batch in Processor  login
    @GET("GRN/GetGRNdetailsByProcessorId/{ProcessorId}")
    Call<GetGrnDrcData> getGRNDataByProcessorIdFromServer(@Path("ProcessorId") String ProcessorId, @Header("Authorization") String authHeader);


    @GET("GRN/GetGRNDetailsForBatchCreationByProcessorId/{ProcessorId}")
    Call<GetGrnProcessorData> getGRNDataForBacthByProcessorIdFromServer(@Path("ProcessorId") String ProcessorId, @Header("Authorization") String authHeader);


    @GET("GRN/GetGRNDetailsForBatchCreationByDealerId/{ProcessorId}")
    Call<GetGrnData> getGRNDataForBacthByDealerIdFromServer(@Path("ProcessorId") String ProcessorId, @Header("Authorization") String authHeader);

    //Todo: Navigation Quality Check Flow Api's


    //Quality Rate dropdown
    @GET("Lookup/GetLookupDtlDetails")
    Call<JsonElement> getQualityRateDetailsFromServer(@Header("Authorization") String authHeader);

    //Quality Check List
    @GET("QualityCheck/GetQualityCheckDeatilsByProcessorId/{ProcessorId}")
    Call<JsonElement> getQualityCheckDeatilsByProcessorIdFromServer(@Path("ProcessorId") String ProcessorId, @Header("Authorization") String authHeader);


    //Todo: Navigation Invoice Flow Api's

    //Batch dropdown in DealerLogin
    @GET("BatchHdr/GetBatchdetailsByDealerId/{DealerId}")
    Call<JsonElement> getBatchDetailsDataFromServerByDealerID(@Path("DealerId") String DealerId, @Header("Authorization") String authHeader);

    //Processor dropdown in Dealerlogin
    @GET("Dealer/GetProcessorByDealerId/{userId}")
    Call<JsonElement> getProcessorListByDealerId(@Path("userId") String userId, @Header("Authorization") String authHeader);


    @GET("GRN/GetGRNdetailsByFarmerCode/{Farmercode}")
    Call<JsonElement> getGrnDataByFarmerCodeFromServer(@Path("Farmercode") String userId, @Header("Authorization") String authHeader);


    //processorAddGRN


    @GET("PlantationBoundries/GetPlantationGeoBoundariesByPlotCodePolygon/{PlotCode}")
    Call<JsonElement> getLatLongDetailsByPlotCodeFromServer(@Path("PlotCode") String userId, @Header("Authorization") String authHeader);

    @GET("  PlantationBoundries/GetPlantationDetailsByFarmerCode/{Farmercode}")
    Call<JsonElement> getplotDetailsByFarmerCodeFromServer(@Path("Farmercode") String userId, @Header("Authorization") String authHeader);


//    @GET("ProcessorFarmer/GetPlantationDetailsByFarmerCode/{Farmercode}")
//    Call<JsonElement> getplotDetailsByFarmerCodeFromServer(@Path("Farmercode") String userId, @Header("Authorization") String authHeader);

    @GET("DealerFarmer/GetPlotsDetailsDealerId/{userId}")
//http://4.240.93.105/gajahrukuapi/M1/DealerFarmer/GetPlotsDetailsDealerId/1
    Call<JsonElement> getplotDataByDealeridFromServer(@Path("userId") String userId, @Header("Authorization") String authHeader);

    @GET("ProcessorFarmer/GetPlotsDetailsProcessorId/{userId}")
//http://4.240.93.105/gajahrukuapi/M1/DealerFarmer/GetPlotsDetailsDealerId/1
    Call<JsonElement> getplotDataByProcessoridFromServer(@Path("userId") String userId, @Header("Authorization") String authHeader);

    @GET("Dealer/GetDashboardTotalProcurementDealerId/{DealerId}")
//http://4.240.93.105/gajahrukuapi/M1/Dealer/GetDashboardTotalProcurementDealerId/10
    Call<JsonElement> getDealerProcurementByDealerIdFromServer(@Path("DealerId") String userId, @Header("Authorization") String authHeader);


    @GET("BatchHdr/GetBatchdetailsByProcessorId/{ProcessorId}")
    Call<JsonElement> getBatchDetailsByProcessorIdFromServer(@Path("ProcessorId") String userId, @Header("Authorization") String authHeader);


    @GET("Dealer/GetDealerByProcessorId/{ProcessorId}")
//http://4.240.93.105/gajahrukuapi/M1/Dealer/GetDealerByProcessorId/1
    Call<JsonElement> getDealerDetailsByProcessorIdFromServer(@Path("ProcessorId") String userId, @Header("Authorization") String authHeader);

    @GET("Invoice/GetInvoiceDetailsByDealerId/{DealerId}")
//http://4.240.93.105/gajahrukuapi/M1/Dealer/GetDashboardTotalProcurementDealerId/10
    Call<JsonElement> getInvoiceDetailsByDealerIdFromServer(@Path("DealerId") String userId, @Header("Authorization") String authHeader);

//    @GET("Invoice/GetInvoiceDetailsForGrnByProcessorIdandDealerId/{DealerId}{SubDealerId}")//http://4.240.93.105/gajahrukuapi/M1/Dealer/GetDashboardTotalProcurementDealerId/10
//    Call<JsonElement> getInvoiceDetailsForGrnByProcessorIdandDealerIdFromServer(@Path("DealerId") String DealerId, @Path("SubDealerId") String SubDealerId,   @Header("Authorization") String authHeader);

    @GET("Invoice/GetInvoiceDetailsByProcessorId/{ProcessorId}")
//http://4.240.93.105/gajahrukuapi/M1/Dealer/GetDealerByProcessorId/1
    Call<JsonElement> getInvoiceDetailsByProcessorIdFromServer(@Path("ProcessorId") String userId, @Header("Authorization") String authHeader);


    //@GET("PlantationBoundries/GetPlantationGeoBoundariesByPlotCodePolygon/{PlotCode}")
    //http://4.240.93.105/gajahrukuapi/M1/ProcessorFarmer/GetPlantationDetailsByFarmerCode/F_1692273201000_27
    // Call<JsonElement> getLatLongDetailsByPlotCodeFromServer(@Path("PlotCode") String userId, @Header("Authorization") String authHeader);


    @GET("DRCDtl/GetDRCDetailByGRNNumber/{GRNNumber}")
//http://4.240.93.105/gajahrukuapi/M1/Dealer/GetDealerByProcessorId/1
    Call<JsonElement> getDrcDetailsByGrnNumberFromServer(@Path("GRNNumber") String userId, @Header("Authorization") String authHeader);


    @GET("Dealer/GetSubDealerByDealerId/{userId}")
//Transaction/GetTransactionDetailsByIMEINo/4c3b2e586efcade7
    Call<JsonElement> getSubDealerDataFromServer(@Path("userId") String userId, @Header("Authorization") String authHeader);

    @GET("Logistics/GetLogisticsDetailsById/{userId}")
//Transaction/GetTransactionDetailsByIMEINo/4c3b2e586efcade7
    Call<JsonElement> getGetLogisticsDetailsDataByIDFromServer(@Path("userId") String userId, @Header("Authorization") String authHeader);

    @GET("Logistics/GetLogisticsDetails")
//Transaction/GetTransactionDetailsByIMEINo/4c3b2e586efcade7
    Call<JsonElement> getGetLogisticsDetailsDataFromServer(@Header("Authorization") String authHeader);

    @GET("PlantationBoundries/GetPlantationDetailsByFarmerCode/{farmercode}")
//Transaction/GetTransactionDetailsByIMEINo/4c3b2e586efcade7
    Call<JsonElement> getPlantationDetailsByFarmerCode(@Path("farmercode") String dealerID, @Header("Authorization") String authHeader);


    @POST("Transaction/AddTransactionDetails")
//http://localhost:4000/V1/Transaction/AddTransactionDetails
    Call<ResponseBody> syncFarmerDetailsDataToServer(@Body SyncPersonalLandAllDetailsRequestDTO syncPersonalLandAllDetailsRequestDTO, @Header("Authorization") String authHeader);

    //image upload
    @Multipart
    @POST("Image/UploadImages")
    Call<ResponseBody> uploadFileDataToServer(@Part("UserId") RequestBody api_key, @Part MultipartBody.Part file);


    @GET("DealerFarmer/GetDealerFarmerMappingByDealerId/{dealerID}")
    Call<JsonElement> getFarmerListbyDealerIdFromserver(@Path("dealerID") String dealerID, @Header("Authorization") String authHeader);


    // TODO: 11/24/2023 for change language data ;

    // TODO: 11/24/2023 header 
    @GET("Language/GetAllLanguages")
    Call<JsonElement> getDataLanguages(@Header("Authorization") String authHeader);

    // TODO: 11/24/2023 english 
    @GET("Language/GetEnlishLanguageByLanguageId/{langId}")
    Call<JsonElement> getEnlishLanguageByLanguageId(@Path("langId") String dealerID, @Header("Authorization") String authHeader);


    // TODO: 11/24/2023 french
    @GET("Language/GetFrenchLanguageByLanguageId/{langId}")
    Call<JsonElement> getFrenchLanguageByLanguageId(@Path("langId") String dealerID, @Header("Authorization") String authHeader);

    // TODO: 11/24/2023 chines
    @GET("Language/GetChineseLanguageByLanguageId/{langId}")
    Call<JsonElement> getChineseLanguageByLanguageId(@Path("langId") String dealerID, @Header("Authorization") String authHeader);

    // TODO: 11/24/2023 thai
    @GET("Language/GetThaiLanguageByLanguageId/{langId}")
    Call<JsonElement> getThaiLanguageByLanguageId(@Path("langId") String dealerID, @Header("Authorization") String authHeader);

    // TODO: 11/24/2023 malaya

    @GET("Language/GetMalayLanguageByLanguageId/{langId}")
    Call<JsonElement> getMalayLanguageByLanguageId(@Path("langId") String dealerID, @Header("Authorization") String authHeader);


    @GET("ProcessorFarmer/GetProcessorFarmerMappingByProcessorId/{ProcessorId}")
    Call<JsonElement> getFarmerListbyProcessorIdFromserver(@Path("ProcessorId") String ProcessorId, @Header("Authorization") String authHeader);

    @GET("BatchHdr/GetFarmerDetailsByBatchId/{ProcessorId}")
    Call<JsonElement> getFarmerListbyProcessorIdFromserverFromBatch(@Path("ProcessorId") String ProcessorId, @Header("Authorization") String authHeader);

    @GET("DealerFarmer/GetFarmerAnalysisByDealerId/{dealerID}")
    Call<JsonElement> getFarmerAnalysisbyDealerIdFromserver(@Path("dealerID") String dealerID, @Header("Authorization") String authHeader);

    @GET("ProcessorFarmer/GetFarmerAnalysisByProcessorId/{ProcessorId}")
    Call<JsonElement> getFarmerAnalysisbyProcessorIdFromserver(@Path("ProcessorId") String dealerID, @Header("Authorization") String authHeader);

    @GET("Dealer/GetDashboardTotalProcurementDealerIdandyear/{dealerID}")
    Call<JsonElement> getDashboardTotalProcurementbyDealer(@Path("dealerID") String dealerID, @Header("Authorization") String authHeader);

    @GET("Processor/GetDashboardTotalProcurementProcessorIdandyear/{ProcessorId}")
    Call<JsonElement> getDashboardTotalProcurementbyProcessor(@Path("ProcessorId") String dealerID, @Header("Authorization") String authHeader);

    @GET("Dealer/GetDashboardTotalSupplyDealerIdandyear/{dealerID}")
    Call<JsonElement> getDashboardTotalSupplybyDealer(@Path("dealerID") String dealerID, @Header("Authorization") String authHeader);

    @GET("Processor/GetDashboardTotalSupplyProcessorIdandyear/{ProcessorId}")
    Call<JsonElement> getDashboardTotalSupplybyProcessor(@Path("ProcessorId") String dealerID, @Header("Authorization") String authHeader);

    @GET("Logistics/GetLogisticsDetailsById/{dealerID}")
    Call<List<GetModeOfTransportDataFromServerTable>> getModeOfTransportListFromserver(@Path("dealerID") String dealerID, @Header("Authorization") String authHeader);

    @POST("GRN/AddGRNDetails")
    Call<ResponseBody> syncAddGrnDetailsDataToServer(@Body SyncGRNDetailsSubmitRequestDTO syncGRNDetailsSubmitRequestDTO, @Header("Authorization") String authHeader);


    @POST("QualityCheck/AddQualityControlCheck")
    Call<ResponseBody> syncAddQualitycheckDetailsDataToServer(@Body SyncQualityCheckDetailsSubmitRequestDTO syncQualityCheckDetailsSubmitRequestDTO, @Header("Authorization") String authHeader);


    @Multipart
    @POST("TabDatabase/UploadTabDatabaseFileByVoluntaryId")
    Call<ResponseBody> uploadDatabasefileDataToServer(@Part("AgentId") RequestBody api_key, @Part("VoluntaryId") RequestBody api_key2, @Part MultipartBody.Part file);

//    @POST("Sync/AddVoluntaryTransactionDetails")
//    Call<ResponseBody> syncFarmerDetailsDataToServer(@Body SyncPersonalLandAllDetailsRequestDTO syncPersonalLandAllDetailsRequestDTO, @Header("Authorization") String authHeader);

    // TODO: 28-08-2023
    //Grn details
    @GET("GRN/getGRNDetailsByDealerId/{userId}")
//Transaction/GetTransactionDetailsByIMEINo/4c3b2e586efcade7
    Call<JsonElement> getGRNAllSyncDataDetailsFromServer(@Path("userId") String userId, @Header("Authorization") String authHeader);

    @GET("DRCDtl/GetDRCDetailByDealerId/{userId}")
        //Transaction/GetTransactionDetailsByIMEINo/4c3b2e586efcade7
    Call<JsonElement> getDRCAllByDealerIdSyncDataDetailsFromServer(@Path("userId") String userId, @Header("Authorization") String authHeader);

    @GET("DRCDtl/GetDRCdetailsById/{userId}")
        //Transaction/GetTransactionDetailsByIMEINo/4c3b2e586efcade7
    Call<JsonElement> getDRCSyncDataDetailsFromServer(@Path("userId") String userId, @Header("Authorization") String authHeader);

    @GET("BatchHdr/GetBatchDetails")
//Transaction/GetTransactionDetailsByIMEINo/4c3b2e586efcade7
    Call<JsonElement> getBatchDetailsDataFromServer(@Header("Authorization") String authHeader);


    @GET("BatchHdr/GetBatchdetailsByProcessorId/{userId}")
//Transaction/GetTransactionDetailsByIMEINo/4c3b2e586efcade7
    Call<JsonElement> getBatchDetailsDataFromServerByProcessorID(@Path("userId") String userId, @Header("Authorization") String authHeader);


    @GET("Customer/GetcustomerDetails")
//Transaction/GetTransactionDetailsByIMEINo/4c3b2e586efcade7
    Call<JsonElement> getCustomerDetailsDataFromServer(@Header("Authorization") String authHeader);

    @GET("Customer/GetcustomerDetailsbyProcessorId/{userId}")
        //Transaction/GetTransactionDetailsByIMEINo/4c3b2e586efcade7
    Call<JsonElement> getCustomerDetailsbyIdFromServerByProcessorId(@Path("userId") String userId, @Header("Authorization") String authHeader);


    //Batch header
    @POST("BatchHdr/AddBatchHdrDetails")
    Call<ResponseBody> syncAddBatchHdrDetailsDataToServer(@Body SyncBatchHdrDetailsSubmitRequestDTO syncBatchHdrDetailsSubmitRequestDTO, @Header("Authorization") String authHeader);


    //Batch footer
    @POST("BatchHdr/addBatchDtlDetails")
    Call<ResponseBody> syncAddBatchDtlDetailsDataToServer(@Body SyncBatchDtlDetailsSubmitRequestDTO syncBatchDtlDetailsSubmitRequestDTO, @Header("Authorization") String authHeader);

    @POST("Invoice/AddInvoiceDetails")
    Call<ResponseBody> syncAddInvoiceDetailsDataToServer(@Body SyncInvoiceDetailsSubmitRequestDTO syncInvoiceDetailsSubmitRequestDTO, @Header("Authorization") String authHeader);

    @POST("Invoice/AddInvoiceDetails")
    Call<ResponseBody> syncAddAllInvoiceDetailsDataToServer(@Body SyncAllInvoiceDetailsSubmitRequestDTO syncInvoiceDetailsSubmitRequestDTO, @Header("Authorization") String authHeader);


    @POST("Invoice/AddInvoiceDetails")
    Call<ResponseBody> syncAddProcessorInvoiceDetailsDataToServer(@Body SyncProcessorInvoiceDetailsSubmitRequestDTO syncInvoiceDetailsSubmitRequestDTO, @Header("Authorization") String authHeader);


    //grn for drc


    //Drc Post
    //header
    @POST("DRCHdr/AddDRCHdrDetails")
    Call<ResponseBody> syncAddDRCHdrDetailsDataToServer(@Body PostDRCHdrDetailsDTO postDRCHdrDetailsDTO, @Header("Authorization") String authHeader);

    //footer
    @POST("DRCDtl/AddDRCDtlDetails")
    Call<ResponseBody> syncAddDRCDtlDetailsDataToServer(@Body PostDRCDtlDetailsDTO postDRCDtlDetailsDTO, @Header("Authorization") String authHeader);

    @GET("Invoice/GetInvoiceDetailsForGrnByProcessorIdandDealerId/{ProcessorId}/{DealerId}")
    Call<JsonElement> getInvoiceDetailsForGrnByProcessorIdandDealerIdFromServer(@Path("ProcessorId") String ProcessorId, @Path("DealerId") String DealerId, @Header("Authorization") String authHeader);

    @GET("Invoice/GetInvoiceDetailsForGrnBySubDealerIdandMainDealerId/{SubDealerId}/{MainDealerId}")
    Call<JsonElement> getInvoiceDetailsForGrnBySubDealerIdandMainDealerIdFromServer(@Path("SubDealerId") String SubDealerId, @Path("MainDealerId") String MainDealerId, @Header("Authorization") String authHeader);

    @GET("Invoice/GetInvoiceDetailsByCustomerId/{CustomerId}")
        //Transaction/GetTransactionDetailsByIMEINo/4c3b2e586efcade7
    Call<JsonElement> getInvoiceDetailsByCustomerIdFromServer(@Path("CustomerId") String CustomerId, @Header("Authorization") String authHeader);

    @GET("Invoice/GetInvoiceBatchDetailsByInvoiceId/{InvoiceId}")
    Call<JsonElement> getBatchDetailsByInvoiceIdFromServer(@Path("InvoiceId") String InvoiceId, @Header("Authorization") String authHeader);


    //Map
    @GET("PlantationBoundries/GetPlotPolygonsDetailsByBatchHdrId/{BatchId}")
    Call<JsonElement> getLatLongDetailsByBatchIdFromServer(@Path("BatchId") String userId, @Header("Authorization") String authHeader);


    @GET("RiskAssesment/GetRiskAssessmentForViewByFarmerCode/{FarmerCode}")
        ///F_49994952652_1
    Call<JsonElement> getRiskAssessmentForViewByFarmerCodeFromServer(@Path("FarmerCode") String FarmerCode, @Header("Authorization") String authHeader);

    @GET("Invoice/GetFarmerDetailsByInvoiceId/{InvoiceId}")
    Call<JsonElement> getFarmerDetailsByInvoiceIdFromServer(@Path("InvoiceId") String InvoiceId, @Header("Authorization") String authHeader);


    // Trader
    @GET("Trader/GetDashboardDataByTraderId/{TraderId}")
    Call<JsonElement> getDashboardDataByTraderIdFromServer(@Path("TraderId") String TraderId, @Header("Authorization") String authHeader);

    @GET("Trader/getProcessorDetailsByTraderId/{TraderId}")
    Call<JsonElement> getProcessorDetailsByTraderIdFromServer(@Path("TraderId") String TraderId, @Header("Authorization") String authHeader);

    @GET("Trader/getDealersDetailsByTraderId/{TraderId}")
    Call<JsonElement> getDealersDetailsByTraderIdFromServer(@Path("TraderId") String TraderId, @Header("Authorization") String authHeader);

    @GET("Trader/GetPlotDetailsBasedOnTraderId/{TraderId}")
    Call<JsonElement> getPlotDetailsBasedOnTraderIdFromServer(@Path("TraderId") String TraderId, @Header("Authorization") String authHeader);

    @GET("Trader/GetCustomerInvoicesByTraderId/{TraderId}/{Year}/{Month}")
    Call<JsonElement> getCustomerInvoicesByTraderIdFromServer(@Path("TraderId") String TraderId, @Path("Year") String Year,@Path("Month") String Month, @Header("Authorization") String authHeader);

    @GET("Language/GetTransalteLanguageWordsByLanguageId/{LanguageId}")
    Call<JsonElement> getTransalteLanguageWordsByLanguageIdFromServer(@Path("LanguageId") String LanguageId, @Header("Authorization") String authHeader);


}
