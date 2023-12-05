package com.socatra.intellitrack.repositories;


import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.FAILURE_RESPONSE_MESSAGE;
import static com.socatra.intellitrack.constants.AppConstant.RAW_DATA_URL;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonElement;
import com.socatra.intellitrack.constants.AppConstant;
import com.socatra.intellitrack.database.entity.AddAllInvoiceDetailsSubmitTable;
import com.socatra.intellitrack.database.entity.AddBatchProcessingDtlDetailsSubmitTable;
import com.socatra.intellitrack.database.entity.AddBatchProcessingHdrDetailsSubmitTable;
import com.socatra.intellitrack.database.entity.AddGRNDetailsSubmitTable;
import com.socatra.intellitrack.database.entity.AddInvoiceDetailsSubmitTable;
import com.socatra.intellitrack.database.entity.GetFarmerListFromServerTable;
import com.socatra.intellitrack.database.entity.GetModeOfTransportDataFromServerTable;
import com.socatra.intellitrack.database.entity.UserLoginTable;
import com.socatra.intellitrack.helper.AppHelper;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.AppWebService;
import com.socatra.intellitrack.database.dao.AppDAO;
import com.socatra.intellitrack.models.LoginResponseDTO;
import com.socatra.intellitrack.models.SyncAllInvoiceDetailsSubmitRequestDTO;
import com.socatra.intellitrack.models.SyncBatchDtlDetailsSubmitRequestDTO;
import com.socatra.intellitrack.models.SyncBatchHdrDetailsSubmitRequestDTO;
import com.socatra.intellitrack.models.SyncGRNDetailsSubmitRequestDTO;
import com.socatra.intellitrack.models.SyncInvoiceDetailsSubmitRequestDTO;
import com.socatra.intellitrack.models.SyncLoginDetailsSubmitRequestDTO;
import com.socatra.intellitrack.models.SyncProcessorInvoiceDetailsSubmitRequestDTO;
import com.socatra.intellitrack.models.SyncQualityCheckDetailsSubmitRequestDTO;
import com.socatra.intellitrack.models.get.GetGrnByDealerId;
import com.socatra.intellitrack.models.post.PostDRCDtlDetailsDTO;
import com.socatra.intellitrack.models.post.PostDRCHdrDetailsDTO;
import com.socatra.intellitrack.models.post.PostQualityCheckDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@Singleton
public class AppRepository {

    private static final String TAG = AppRepository.class.getCanonicalName();

    private final AppDAO appDAO;
    private final Executor executor;
    private final AppHelper appHelper;
    Context context;
    //Get Grn by DealerId
    private MutableLiveData<List<GetGrnByDealerId>> grnDataLiveData = new MutableLiveData<>();

    @Inject
    public AppRepository(AppDAO appDAO, Executor executor, AppHelper appHelper, Context context) {
        this.appDAO = appDAO;
        this.executor = executor;
        this.appHelper = appHelper;
        this.context = context;
    }

    // TODO: 8/24/2023 adding server call getting of farmer details from sever
  /*  public LiveData<List<GetFarmerListFromServerTable>> getFarmerDetailsListFromServer(String dealerId) {
        final MutableLiveData<List<GetFarmerListFromServerTable>> data = new MutableLiveData<>();
        try {
            AppWebService.changeApiBaseUrl(RAW_DATA_URL);
            executor.execute(() -> {

                if (appHelper.isNetworkAvailable()) { // TODO: Checking internet connection

                    AppWebService.createService(AppAPI.class).getFarmerListFromserver(dealerId, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""))
                            .enqueue(new Callback<List<GetFarmerListFromServerTable>>() {
                                @Override
                                public void onResponse(Call<List<GetFarmerListFromServerTable>> call, Response<List<GetFarmerListFromServerTable>> response) {
                                    Log.e("TAG", "BreederSeedDetailListTable LIST REFRESHED FROM NETWORK");
                                    executor.execute(() -> {
                                        List<GetFarmerListFromServerTable> getFarmerListTableListFromServer = response.body();
                                        if (getFarmerListTableListFromServer != null && getFarmerListTableListFromServer.size() > 0) {
                                            // TODO: Delete & Insert Stage List
                                            //dynamicUIDao.deleteAndInsertStageList(stageDetailsTableList);
                                            for (GetFarmerListFromServerTable getFarmerListFromServerTable : getFarmerListTableListFromServer) {
                                                if (getFarmerListFromServerTable != null && !TextUtils.isEmpty(getFarmerListFromServerTable.getFarmerId())) {

                                                    GetFarmerListFromServerTable getFarmerListFromServerData = new GetFarmerListFromServerTable();
                                                    Log.d("onResponseData: ", getFarmerListFromServerTable.getFarmerId() + getFarmerListFromServerTable.getFarmerCode());
                                                    getFarmerListFromServerData.setFarmerId(getFarmerListFromServerTable.getFarmerId());
                                                    getFarmerListFromServerData.setFarmerCode(getFarmerListFromServerTable.getFarmerCode());
                                                    getFarmerListFromServerData.setFarmerName(getFarmerListFromServerTable.getFarmerName());
                                                    getFarmerListFromServerData.setDealerId(getFarmerListFromServerTable.getDealerId());
                                                    getFarmerListFromServerData.setDealerName(getFarmerListFromServerTable.getDealerName());
                                                    getFarmerListFromServerData.setIsActive(getFarmerListFromServerTable.getIsActive()); // TODO: STAFF ID
                                                    getFarmerListFromServerData.setCreatedByUserId(getFarmerListFromServerTable.getCreatedByUserId()); // TODO: STAFF ID
                                                    getFarmerListFromServerData.setCreatedDate(getFarmerListFromServerTable.getCreatedDate());// TODO: data needs to capture true
                                                    getFarmerListFromServerData.setUpdatedByUserId(getFarmerListFromServerTable.getUpdatedByUserId());
                                                    getFarmerListFromServerData.setUpdatedDate(getFarmerListFromServerTable.getUpdatedDate());

                                                }

                                            }

                                            // TODO: Sending Final Result
                                            data.postValue(getFarmerListTableListFromServer);
                                        } else {
                                            // TODO: Sending Final Result
                                            List<GetFarmerListFromServerTable> emptyStageList = new ArrayList<>();
                                            data.postValue(emptyStageList);
                                        }
                                    });
                                }

                                @Override
                                public void onFailure(Call<List<GetFarmerListFromServerTable>> call, Throwable t) {
                                    try {
                                        executor.execute(() -> {
                                            // TODO: Sending Final Result
                                            List<GetFarmerListFromServerTable> emptyStageList = new ArrayList<>();
                                            data.postValue(emptyStageList);

                                        });

                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            });

                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            executor.execute(() -> {
                // TODO: Sending Final Result
                List<GetFarmerListFromServerTable> emptyStageList = new ArrayList<>();
                data.postValue(emptyStageList);

            });
        }
        return data;
    }*/

    // login api call
    public LiveData<LoginResponseDTO> getlogInServiceResponse(UserLoginTable userLoginTable) {
        final MutableLiveData<LoginResponseDTO> data = new MutableLiveData<>();
        try {
            AppWebService.changeApiBaseUrl(RAW_DATA_URL);
            executor.execute(() -> {
                final SyncLoginDetailsSubmitRequestDTO syncLoginReqDTO = new SyncLoginDetailsSubmitRequestDTO();

                if (!TextUtils.isEmpty(userLoginTable.getUsername())) {
                    syncLoginReqDTO.setUsername(userLoginTable.getUsername());
                } else {
                    syncLoginReqDTO.setUsername("");
                }

                if (!TextUtils.isEmpty(userLoginTable.getPassword())) {
                    syncLoginReqDTO.setPassword(userLoginTable.getPassword());
                } else {
                    syncLoginReqDTO.setPassword("");
                }

                AppWebService.createService(AppAPI.class).postlogInService(syncLoginReqDTO)
                        .enqueue(new Callback<LoginResponseDTO>() {
                            @Override
                            public void onResponse(Call<LoginResponseDTO> call, Response<LoginResponseDTO> response) {
                                Log.e("TAG", "Login LIST REFRESHED FROM NETWORK");
                                executor.execute(() -> {
                                    if (response.body() != null) {
//                                            datumList.add(loginResponseDTOList.getData().get(0));
                                        data.postValue(response.body());
                                    } else {
                                        //  data.postValue("something error");
                                    }


                                });
                            }

                            @Override
                            public void onFailure(Call<LoginResponseDTO> call, Throwable t) {
                                try {
                                    executor.execute(() -> {
                                        // TODO: Sending Final Result
                                        LoginResponseDTO emptyStageList = new LoginResponseDTO();
                                        data.postValue(emptyStageList);

                                    });

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });


            });
        } catch (Exception ex) {
            ex.printStackTrace();
            executor.execute(() -> {
                // TODO: Sending Final Result
                LoginResponseDTO emptyStageList = new LoginResponseDTO();
                data.postValue(emptyStageList);

            });
        }
        return data;
    }

    public LiveData<List<GetFarmerListFromServerTable>> getFarmerDetailsListFromServer(String dealerId) {
        final MutableLiveData<List<GetFarmerListFromServerTable>> data = new MutableLiveData<>();
        try {
            AppWebService.changeApiBaseUrl(RAW_DATA_URL);
            executor.execute(() -> {


                AppWebService.createService(AppAPI.class).getFarmerListbyDealerIdFromserver(dealerId, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""))
                        .enqueue(new Callback<JsonElement>() {
                            @Override
                            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                                Log.e("TAG", "Login LIST REFRESHED FROM NETWORK");
                                executor.execute(() -> {
                                    if (response.body() != null) {
                                        String strResponse = String.valueOf(response.body());
                                        Log.d(TAG, "onResponse: >>>" + strResponse);
                                        JSONObject json_object = null;
                                        List<GetFarmerListFromServerTable> getFarmerListFromServerTableList = new ArrayList<>();
                                        try {
                                            json_object = new JSONObject(strResponse);
                                            JSONArray jsonArray = json_object.getJSONArray("data");

                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject json_farmerdata = jsonArray.getJSONObject(i);
                                                GetFarmerListFromServerTable getFarmerListFromServerTable = new GetFarmerListFromServerTable();
                                                getFarmerListFromServerTable.setFarmerCode(json_farmerdata.getString("FarmerCode"));
                                                getFarmerListFromServerTable.setFarmerName(json_farmerdata.getString("FarmerName"));

                                                getFarmerListFromServerTableList.add(getFarmerListFromServerTable);
                                            }


                                            data.postValue(getFarmerListFromServerTableList);
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }


//                                            datumList.add(loginResponseDTOList.getData().get(0));


                                    }


                                });
                            }

                            @Override
                            public void onFailure(Call<JsonElement> call, Throwable t) {
                                try {
                                    executor.execute(() -> {
                                        // TODO: Sending Final Result
                                        List<GetFarmerListFromServerTable> emptyStageList = new ArrayList<>();
                                        data.postValue(emptyStageList);

                                    });

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });

            });
        } catch (Exception ex) {
            ex.printStackTrace();
            executor.execute(() -> {
                // TODO: Sending Final Result
                List<GetFarmerListFromServerTable> emptyStageList = new ArrayList<>();
                data.postValue(emptyStageList);

            });
        }
        return data;
    }

    // TODO: 8/24/2023 get mode of trans port detatils froms server
    public LiveData<List<GetModeOfTransportDataFromServerTable>> getModeOfTransportDetailsFromServer(String dealerId) {
        final MutableLiveData<List<GetModeOfTransportDataFromServerTable>> data = new MutableLiveData<>();
        try {
            AppWebService.changeApiBaseUrl(RAW_DATA_URL);
            executor.execute(() -> {

                if (appHelper.isNetworkAvailable()) { // TODO: Checking internet connection

                    AppWebService.createService(AppAPI.class).getModeOfTransportListFromserver(dealerId, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""))
                            .enqueue(new Callback<List<GetModeOfTransportDataFromServerTable>>() {
                                @Override
                                public void onResponse(Call<List<GetModeOfTransportDataFromServerTable>> call, Response<List<GetModeOfTransportDataFromServerTable>> response) {
                                    Log.e("TAG", "BreederSeedDetailListTable LIST REFRESHED FROM NETWORK");
                                    executor.execute(() -> {
                                        JSONArray jsonArray = new JSONArray();
                                        jsonArray = (JSONArray) response.body();

                                        List<GetModeOfTransportDataFromServerTable> getFarmerListTableListFromServer = response.body();
                                        if (getFarmerListTableListFromServer != null && getFarmerListTableListFromServer.size() > 0) {
                                            // TODO: Delete & Insert Stage List
                                            //dynamicUIDao.deleteAndInsertStageList(stageDetailsTableList);
                                            for (GetModeOfTransportDataFromServerTable getFarmerListFromServerTable : getFarmerListTableListFromServer) {
                                                if (getFarmerListFromServerTable != null && !TextUtils.isEmpty(getFarmerListFromServerTable.getId())) {

                                                    GetModeOfTransportDataFromServerTable getModeOfTransportDataFromServerTable = new GetModeOfTransportDataFromServerTable();
                                                    Log.d("onResponseData: ", getFarmerListFromServerTable.getId() + getFarmerListFromServerTable.getFueltype());
                                                    getModeOfTransportDataFromServerTable.setId(getFarmerListFromServerTable.getId());
                                                    getModeOfTransportDataFromServerTable.setFueltype(getFarmerListFromServerTable.getFueltype());
                                                    getModeOfTransportDataFromServerTable.setInsuranceExpiryDate(getFarmerListFromServerTable.getFueltype());
                                                    getModeOfTransportDataFromServerTable.setLastMaintenanceDate(getFarmerListFromServerTable.getFueltype());
                                                    getModeOfTransportDataFromServerTable.setManufacturingYear(getFarmerListFromServerTable.getFueltype());
                                                    getModeOfTransportDataFromServerTable.setVehicleRegistrationNumber(getFarmerListFromServerTable.getFueltype());
                                                    getModeOfTransportDataFromServerTable.setLastMaintenanceDate(getFarmerListFromServerTable.getFueltype()); // TODO: LOAN TYPE
                                                    getModeOfTransportDataFromServerTable.setInsuranceExpiryDate(getFarmerListFromServerTable.getFueltype());
                                                    getModeOfTransportDataFromServerTable.setLastMaintenanceDate(getFarmerListFromServerTable.getFueltype());
                                                    getModeOfTransportDataFromServerTable.setIsActive(getFarmerListFromServerTable.getIsActive()); // TODO: STAFF ID
                                                    getModeOfTransportDataFromServerTable.setCreatedByUserId(getFarmerListFromServerTable.getCreatedByUserId()); // TODO: STAFF ID
                                                    getModeOfTransportDataFromServerTable.setCreatedDate(getFarmerListFromServerTable.getCreatedDate());// TODO: data needs to capture true
                                                    getModeOfTransportDataFromServerTable.setUpdatedByUserId(getFarmerListFromServerTable.getUpdatedByUserId());
                                                    getModeOfTransportDataFromServerTable.setUpdatedDate(getFarmerListFromServerTable.getUpdatedDate());

                                                }

                                            }

                                            // TODO: Sending Final Result
                                            data.postValue(getFarmerListTableListFromServer);
                                        } else {
                                            // TODO: Sending Final Result
                                            List<GetModeOfTransportDataFromServerTable> emptyStageList = new ArrayList<>();
                                            data.postValue(emptyStageList);
                                        }
                                    });
                                }

                                @Override
                                public void onFailure(Call<List<GetModeOfTransportDataFromServerTable>> call, Throwable t) {
                                    try {
                                        executor.execute(() -> {
                                            // TODO: Sending Final Result
                                            List<GetModeOfTransportDataFromServerTable> emptyStageList = new ArrayList<>();
                                            data.postValue(emptyStageList);

                                        });

                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            });

                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            executor.execute(() -> {
                // TODO: Sending Final Result
                List<GetModeOfTransportDataFromServerTable> emptyStageList = new ArrayList<>();
                data.postValue(emptyStageList);

            });
        }
        return data;
    }

    // TODO: 8/24/2023 adding GRN data to server
    public LiveData<String> syncGRNDetailsDataToServer(AddGRNDetailsSubmitTable addGRNDetailsSubmitTable, String reqType) {
        final MutableLiveData<String> data = new MutableLiveData<>();
        AppWebService.changeApiBaseUrl(RAW_DATA_URL);
        executor.execute(() -> {

            final SyncGRNDetailsSubmitRequestDTO syncGRNDetailsSubmitRequestDTO = new SyncGRNDetailsSubmitRequestDTO();


            if (!TextUtils.isEmpty(addGRNDetailsSubmitTable.getToDealerId())) {
                syncGRNDetailsSubmitRequestDTO.setToDealerId(addGRNDetailsSubmitTable.getToDealerId());
            } else {
                syncGRNDetailsSubmitRequestDTO.setToDealerId("");
            }


            if (!TextUtils.isEmpty(addGRNDetailsSubmitTable.getFarmerCode())) {
                syncGRNDetailsSubmitRequestDTO.setFarmerCode(addGRNDetailsSubmitTable.getFarmerCode());
            } else {
                syncGRNDetailsSubmitRequestDTO.setFarmerCode("");
            }

            //Processor Id
            if (!TextUtils.isEmpty(addGRNDetailsSubmitTable.getFromProcessorId())) {
                syncGRNDetailsSubmitRequestDTO.setFromProcessorId(addGRNDetailsSubmitTable.getFromProcessorId());
            } else {
                syncGRNDetailsSubmitRequestDTO.setFromProcessorId("");
            }

            //SubDealer
            if (!TextUtils.isEmpty(addGRNDetailsSubmitTable.getFromDealerId())) {
                syncGRNDetailsSubmitRequestDTO.setFromDealerId(addGRNDetailsSubmitTable.getFromDealerId());
            } else {
                syncGRNDetailsSubmitRequestDTO.setFromDealerId("");
            }

            //Invoice
            if (!TextUtils.isEmpty(addGRNDetailsSubmitTable.getInvoiceId())) {
                syncGRNDetailsSubmitRequestDTO.setInvoiceId(addGRNDetailsSubmitTable.getInvoiceId());
            } else {
                syncGRNDetailsSubmitRequestDTO.setInvoiceId("");
            }

            //GRNDocument
            if (!TextUtils.isEmpty(addGRNDetailsSubmitTable.getGRNDocument())) {
                syncGRNDetailsSubmitRequestDTO.setGRNDocument(addGRNDetailsSubmitTable.getGRNDocument());
            } else {
                syncGRNDetailsSubmitRequestDTO.setGRNDocument("");
            }


            if (!TextUtils.isEmpty(addGRNDetailsSubmitTable.getGRNdate())) {
                syncGRNDetailsSubmitRequestDTO.setGRNdate(addGRNDetailsSubmitTable.getGRNdate());
            } else {
                syncGRNDetailsSubmitRequestDTO.setGRNdate("");
            }

            if (!TextUtils.isEmpty(addGRNDetailsSubmitTable.getQuantity())) {
                syncGRNDetailsSubmitRequestDTO.setQuantity(addGRNDetailsSubmitTable.getQuantity());
            } else {
                syncGRNDetailsSubmitRequestDTO.setQuantity("");
            }
            if (!TextUtils.isEmpty(addGRNDetailsSubmitTable.getModeofTransport())) {
                syncGRNDetailsSubmitRequestDTO.setModeofTransport(addGRNDetailsSubmitTable.getModeofTransport());
            } else {
                syncGRNDetailsSubmitRequestDTO.setModeofTransport("");
            }

            if (!TextUtils.isEmpty(addGRNDetailsSubmitTable.getIsActive())) {
                syncGRNDetailsSubmitRequestDTO.setIsActive(addGRNDetailsSubmitTable.getIsActive());
            } else {
                syncGRNDetailsSubmitRequestDTO.setIsActive("");
            }
            if (!TextUtils.isEmpty(addGRNDetailsSubmitTable.getCreatedByUserId())) {
                syncGRNDetailsSubmitRequestDTO.setCreatedByUserId(addGRNDetailsSubmitTable.getCreatedByUserId());
            } else {
                syncGRNDetailsSubmitRequestDTO.setCreatedByUserId("");
            }


            if (!TextUtils.isEmpty(addGRNDetailsSubmitTable.getUpdatedByUserId())) {
                syncGRNDetailsSubmitRequestDTO.setUpdatedByUserId(addGRNDetailsSubmitTable.getUpdatedByUserId());
            } else {
                syncGRNDetailsSubmitRequestDTO.setUpdatedByUserId("");
            }

//            String dateTime = appHelper.getCurrentDateTime(AppConstant.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
            syncGRNDetailsSubmitRequestDTO.setCreatedDate(addGRNDetailsSubmitTable.getCreatedDate());
            syncGRNDetailsSubmitRequestDTO.setUpdatedDate(addGRNDetailsSubmitTable.getUpdatedDate());

            AppWebService.createService(AppAPI.class).syncAddGrnDetailsDataToServer(syncGRNDetailsSubmitRequestDTO, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, "")).
                    enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            executor.execute(() -> {
                                try {
                                    String strResponse = response.body().string();
                                    Log.e("onResponse: ", strResponse);
//                                    JSONObject json = new JSONObject(strResponse);
//                                    String serverResponse = json.getString("Message");
//                                    Log.e("response farmer result", serverResponse);

                                    JSONObject lObj = new JSONObject(strResponse);
                                    //  String status = lObj.optString("status");
                                    String message = lObj.getString("message");
                                    Log.d(TAG, "onResponse: data" + message);
//                                        Log.e("kzjdcvb", message);
                                    //String strMonitoringId = lObj.getString("id");
//                                    GROW_MONITORING_ID = null;
//                                    GROW_MONITORING_ID = strMonitoringId;
                                    // Log.e("id_monitoring", strMonitoringId);
                                    data.postValue("GRN added successfully");

                                } catch (Exception ex) {
                                    ex.printStackTrace();

                                    data.postValue("GRN added successfully");
                                }

                            });
                        }


                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            t.printStackTrace();
                            // TODO: Sending result
                            executor.execute(() -> {
                                data.postValue(FAILURE_RESPONSE_MESSAGE);
                            });
                        }
                    });
        });
        return data;
    }

    public LiveData<String> syncQualityCheckDetailsDataToServer(PostQualityCheckDetails PostQualityCheckDetailsSubmitTable, String reqType) {
        final MutableLiveData<String> data = new MutableLiveData<>();
        AppWebService.changeApiBaseUrl(RAW_DATA_URL);
        executor.execute(() -> {

            final SyncQualityCheckDetailsSubmitRequestDTO SyncQualityCheckDetailsSubmitRequestDTO = new SyncQualityCheckDetailsSubmitRequestDTO();


            if (!TextUtils.isEmpty(PostQualityCheckDetailsSubmitTable.getBatchCode())) {
                SyncQualityCheckDetailsSubmitRequestDTO.setBatchCode(PostQualityCheckDetailsSubmitTable.getBatchCode());
            } else {
                SyncQualityCheckDetailsSubmitRequestDTO.setBatchCode("");
            }


            if (!TextUtils.isEmpty(PostQualityCheckDetailsSubmitTable.getQuantityRate())) {
                SyncQualityCheckDetailsSubmitRequestDTO.setQuantityRate(PostQualityCheckDetailsSubmitTable.getQuantityRate());
            } else {
                SyncQualityCheckDetailsSubmitRequestDTO.setQuantityRate("");
            }

            //Processor Id
            if (!TextUtils.isEmpty(PostQualityCheckDetailsSubmitTable.getEvidenceDocument())) {
                SyncQualityCheckDetailsSubmitRequestDTO.setEvidenceDocument(PostQualityCheckDetailsSubmitTable.getEvidenceDocument());
            } else {
                SyncQualityCheckDetailsSubmitRequestDTO.setEvidenceDocument("");
            }

            //SubDealer
            if (!TextUtils.isEmpty(PostQualityCheckDetailsSubmitTable.getQualityCheckDate())) {
                SyncQualityCheckDetailsSubmitRequestDTO.setQualityCheckDate(PostQualityCheckDetailsSubmitTable.getQualityCheckDate());
            } else {
                SyncQualityCheckDetailsSubmitRequestDTO.setQualityCheckDate("");
            }

            //Invoice
            if (!TextUtils.isEmpty(PostQualityCheckDetailsSubmitTable.getProcessorId())) {
                SyncQualityCheckDetailsSubmitRequestDTO.setProcessorId(PostQualityCheckDetailsSubmitTable.getProcessorId());
            } else {
                SyncQualityCheckDetailsSubmitRequestDTO.setProcessorId("");
            }


            if (!TextUtils.isEmpty(PostQualityCheckDetailsSubmitTable.getIsActive())) {
                SyncQualityCheckDetailsSubmitRequestDTO.setIsActive(PostQualityCheckDetailsSubmitTable.getIsActive());
            } else {
                SyncQualityCheckDetailsSubmitRequestDTO.setIsActive("");
            }
            if (!TextUtils.isEmpty(PostQualityCheckDetailsSubmitTable.getCreatedByUserId())) {
                SyncQualityCheckDetailsSubmitRequestDTO.setCreatedByUserId(PostQualityCheckDetailsSubmitTable.getCreatedByUserId());
            } else {
                SyncQualityCheckDetailsSubmitRequestDTO.setCreatedByUserId("");
            }


            if (!TextUtils.isEmpty(PostQualityCheckDetailsSubmitTable.getUpdatedByUserId())) {
                SyncQualityCheckDetailsSubmitRequestDTO.setUpdatedByUserId(PostQualityCheckDetailsSubmitTable.getUpdatedByUserId());
            } else {
                SyncQualityCheckDetailsSubmitRequestDTO.setUpdatedByUserId("");
            }

//            String dateTime = appHelper.getCurrentDateTime(AppConstant.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
            SyncQualityCheckDetailsSubmitRequestDTO.setCreatedDate(PostQualityCheckDetailsSubmitTable.getCreatedDate());
            SyncQualityCheckDetailsSubmitRequestDTO.setUpdatedDate(PostQualityCheckDetailsSubmitTable.getUpdatedDate());

            AppWebService.createService(AppAPI.class).syncAddQualitycheckDetailsDataToServer(SyncQualityCheckDetailsSubmitRequestDTO, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, "")).
                    enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            executor.execute(() -> {
                                try {
                                    String strResponse = response.body().string();
                                    Log.e("onResponse: ", strResponse);
//                                    JSONObject json = new JSONObject(strResponse);
//                                    String serverResponse = json.getString("Message");
//                                    Log.e("response farmer result", serverResponse);

                                    JSONObject lObj = new JSONObject(strResponse);
                                    //  String status = lObj.optString("status");
                                    String message = lObj.getString("message");
                                    Log.d(TAG, "onResponse: data" + message);
//                                        Log.e("kzjdcvb", message);
                                    //String strMonitoringId = lObj.getString("id");
//                                    GROW_MONITORING_ID = null;
//                                    GROW_MONITORING_ID = strMonitoringId;
                                    // Log.e("id_monitoring", strMonitoringId);
                                    data.postValue("Success");

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    data.postValue("Success");
                                }

                            });
                        }


                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            t.printStackTrace();
                            // TODO: Sending result
                            executor.execute(() -> {
                                data.postValue(FAILURE_RESPONSE_MESSAGE);
                            });
                        }
                    });
        });
        return data;
    }

    // TODO: 8/24/2023 adding Invoice data to server
    public LiveData<String> syncInvoiceDetailsDataToServer(AddInvoiceDetailsSubmitTable addInvoiceetailsSubmitTable) {
        final MutableLiveData<String> data = new MutableLiveData<>();
        AppWebService.changeApiBaseUrl(RAW_DATA_URL);
        executor.execute(() -> {

            final SyncInvoiceDetailsSubmitRequestDTO syncInvoiceDetailsSubmitRequestDTO = new SyncInvoiceDetailsSubmitRequestDTO();


            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getDealerId())) {
                syncInvoiceDetailsSubmitRequestDTO.setDealerId(addInvoiceetailsSubmitTable.getDealerId());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setDealerId("");
            }


            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getProcessorId())) {
                syncInvoiceDetailsSubmitRequestDTO.setProcessorId(addInvoiceetailsSubmitTable.getProcessorId());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setProcessorId("");
            }


            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getBatchId())) {
                syncInvoiceDetailsSubmitRequestDTO.setBatchId(addInvoiceetailsSubmitTable.getBatchId());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setBatchId("");
            }


            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getProduct())) {
                syncInvoiceDetailsSubmitRequestDTO.setProduct(addInvoiceetailsSubmitTable.getProduct());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setProduct("");
            }

            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getQuantity())) {
                syncInvoiceDetailsSubmitRequestDTO.setQuantity(addInvoiceetailsSubmitTable.getQuantity());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setQuantity("");
            }
            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getShippingTo())) {
                syncInvoiceDetailsSubmitRequestDTO.setShippingTo(addInvoiceetailsSubmitTable.getShippingTo());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setShippingTo("");
            }

            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getShippingAddress())) {
                syncInvoiceDetailsSubmitRequestDTO.setShippingAddress(addInvoiceetailsSubmitTable.getShippingAddress());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setShippingAddress("");
            }

            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getIsActive())) {
                syncInvoiceDetailsSubmitRequestDTO.setIsActive(addInvoiceetailsSubmitTable.getIsActive());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setIsActive("");
            }

            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getpODTiming())) {
                syncInvoiceDetailsSubmitRequestDTO.setpODTiming(addInvoiceetailsSubmitTable.getpODTiming());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setpODTiming(null);
            }

            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getPOLTiming())) {
                syncInvoiceDetailsSubmitRequestDTO.setPOLTiming(addInvoiceetailsSubmitTable.getPOLTiming());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setPOLTiming(null);
            }

            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getPortofDischarge())) {
                syncInvoiceDetailsSubmitRequestDTO.setPortofDischarge(addInvoiceetailsSubmitTable.getPortofDischarge());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setPortofDischarge(null);
            }

            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getPortofLoading())) {
                syncInvoiceDetailsSubmitRequestDTO.setPortofLoading(addInvoiceetailsSubmitTable.getPortofLoading());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setPortofLoading(null);
            }

            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getVessel())) {
                syncInvoiceDetailsSubmitRequestDTO.setVessel(addInvoiceetailsSubmitTable.getVessel());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setVessel(null);
            }


            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getCreatedByUserId())) {
                syncInvoiceDetailsSubmitRequestDTO.setCreatedByUserId(addInvoiceetailsSubmitTable.getCreatedByUserId());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setCreatedByUserId("");
            }


            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getUpdatedByUserId())) {
                syncInvoiceDetailsSubmitRequestDTO.setUpdatedByUserId(addInvoiceetailsSubmitTable.getUpdatedByUserId());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setUpdatedByUserId("");
            }

            String dateTime = appHelper.getCurrentDateTime(AppConstant.DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS);
            syncInvoiceDetailsSubmitRequestDTO.setCreatedDate(dateTime);
            syncInvoiceDetailsSubmitRequestDTO.setUpdatedDate(dateTime);

            AppWebService.createService(AppAPI.class).syncAddInvoiceDetailsDataToServer(syncInvoiceDetailsSubmitRequestDTO, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, "")).
                    enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            executor.execute(() -> {
                                try {
                                    String strResponse = response.body().string();
                                    Log.e("onResponse: ", strResponse);
//                                    JSONObject json = new JSONObject(strResponse);
//                                    String serverResponse = json.getString("Message");
//                                    Log.e("response farmer result", serverResponse);

                                    JSONObject lObj = new JSONObject(strResponse);
                                    //  String status = lObj.optString("status");
                                    String message = lObj.getString("message");
                                    Log.d(TAG, "onResponse: data" + message);
//                                        Log.e("kzjdcvb", message);
                                    //String strMonitoringId = lObj.getString("id");
//                                    GROW_MONITORING_ID = null;
//                                    GROW_MONITORING_ID = strMonitoringId;
                                    // Log.e("id_monitoring", strMonitoringId);
                                    data.postValue("Invoice added successfully");

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    data.postValue("Invoice added successfully");
                                }

                            });
                        }


                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            t.printStackTrace();
                            // TODO: Sending result
                            executor.execute(() -> {
                                data.postValue(FAILURE_RESPONSE_MESSAGE);
                            });
                        }
                    });
        });
        return data;
    }

    public LiveData<String> syncAllInvoiceDetailsDataToServer(AddAllInvoiceDetailsSubmitTable addInvoiceetailsSubmitTable, String deviceRoleName) {
        final MutableLiveData<String> data = new MutableLiveData<>();
        AppWebService.changeApiBaseUrl(RAW_DATA_URL);
        executor.execute(() -> {

            final SyncAllInvoiceDetailsSubmitRequestDTO syncInvoiceDetailsSubmitRequestDTO = new SyncAllInvoiceDetailsSubmitRequestDTO();


            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getBatchId())) {
                syncInvoiceDetailsSubmitRequestDTO.setBatchId(addInvoiceetailsSubmitTable.getBatchId());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setBatchId("");
            }
            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getToProcessorId())) {
                syncInvoiceDetailsSubmitRequestDTO.setToProcessorId(addInvoiceetailsSubmitTable.getToProcessorId());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setToProcessorId("");
            }


            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getFromProcessorId())) {
                syncInvoiceDetailsSubmitRequestDTO.setFromProcessorId(addInvoiceetailsSubmitTable.getFromProcessorId());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setFromProcessorId("");
            }


            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getFromDealerId())) {
                syncInvoiceDetailsSubmitRequestDTO.setFromDealerId(addInvoiceetailsSubmitTable.getFromDealerId());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setFromDealerId("");
            }
            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getToDealerId())) {
                syncInvoiceDetailsSubmitRequestDTO.setToDealerId(addInvoiceetailsSubmitTable.getToDealerId());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setToDealerId("");
            }
            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getToCustomerId())) {
                syncInvoiceDetailsSubmitRequestDTO.setToCustomerId(addInvoiceetailsSubmitTable.getToCustomerId());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setToCustomerId("");
            }

            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getProduct())) {
                syncInvoiceDetailsSubmitRequestDTO.setProduct(addInvoiceetailsSubmitTable.getProduct());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setProduct("");
            }
            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getQuantity())) {
                syncInvoiceDetailsSubmitRequestDTO.setQuantity(addInvoiceetailsSubmitTable.getQuantity());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setQuantity("");
            }


            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getVessel())) {
                syncInvoiceDetailsSubmitRequestDTO.setVessel(addInvoiceetailsSubmitTable.getVessel());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setVessel("");
            }

            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getPortofLoading())) {
                syncInvoiceDetailsSubmitRequestDTO.setPortofLoading(addInvoiceetailsSubmitTable.getPortofLoading());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setPortofLoading("");
            }
            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getPortofDischarge())) {
                syncInvoiceDetailsSubmitRequestDTO.setPortofDischarge(addInvoiceetailsSubmitTable.getPortofDischarge());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setPortofDischarge("");
            }


            if (addInvoiceetailsSubmitTable.getPOLTiming() != null) {
                syncInvoiceDetailsSubmitRequestDTO.setPOLTiming(addInvoiceetailsSubmitTable.getPOLTiming());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setPOLTiming(null);
            }


            if (addInvoiceetailsSubmitTable.getPODTiming()  != null) {
                syncInvoiceDetailsSubmitRequestDTO.setPODTiming(addInvoiceetailsSubmitTable.getPODTiming());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setPODTiming(null);
            }




            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getShippingAddress())) {
                syncInvoiceDetailsSubmitRequestDTO.setShippingAddress(addInvoiceetailsSubmitTable.getShippingAddress());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setShippingAddress("");
            }

            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getIsActive())) {
                syncInvoiceDetailsSubmitRequestDTO.setIsActive(addInvoiceetailsSubmitTable.getIsActive());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setIsActive("");
            }


            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getCreatedByUserId())) {
                syncInvoiceDetailsSubmitRequestDTO.setCreatedByUserId(addInvoiceetailsSubmitTable.getCreatedByUserId());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setCreatedByUserId("");
            }


            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getUpdatedByUserId())) {
                syncInvoiceDetailsSubmitRequestDTO.setUpdatedByUserId(addInvoiceetailsSubmitTable.getUpdatedByUserId());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setUpdatedByUserId("");
            }

            String dateTime = appHelper.getCurrentDateTime(AppConstant.DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS);
            syncInvoiceDetailsSubmitRequestDTO.setCreatedDate(dateTime);
            syncInvoiceDetailsSubmitRequestDTO.setUpdatedDate(dateTime);

            AppWebService.createService(AppAPI.class).syncAddAllInvoiceDetailsDataToServer(syncInvoiceDetailsSubmitRequestDTO, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, "")).
                    enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            executor.execute(() -> {
                                try {
                                    String strResponse = response.body().string();
                                    Log.e("onResponse: ", strResponse);
//                                    JSONObject json = new JSONObject(strResponse);
//                                    String serverResponse = json.getString("Message");
//                                    Log.e("response farmer result", serverResponse);

                                    JSONObject lObj = new JSONObject(strResponse);
                                    //  String status = lObj.optString("status");
                                    String message = lObj.getString("messgae");
                                    Log.d(TAG, "onResponse: data" + message);
//                                        Log.e("kzjdcvb", message);
                                    //String strMonitoringId = lObj.getString("id");
//                                    GROW_MONITORING_ID = null;
//                                    GROW_MONITORING_ID = strMonitoringId;
                                    // Log.e("id_monitoring", strMonitoringId);
                                    data.postValue(message);

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    data.postValue("Invoice added successfully");
                                }

                            });
                        }


                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            t.printStackTrace();
                            // TODO: Sending result
                            executor.execute(() -> {
                                data.postValue(FAILURE_RESPONSE_MESSAGE);
                            });
                        }
                    });
        });
        return data;
    }


    public LiveData<String> syncProcessorInvoiceDetailsDataToServer(AddAllInvoiceDetailsSubmitTable addInvoiceetailsSubmitTable, String deviceRoleName) {
        final MutableLiveData<String> data = new MutableLiveData<>();
        AppWebService.changeApiBaseUrl(RAW_DATA_URL);
        executor.execute(() -> {

            final SyncProcessorInvoiceDetailsSubmitRequestDTO syncInvoiceDetailsSubmitRequestDTO = new SyncProcessorInvoiceDetailsSubmitRequestDTO();


            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getBatchId())) {
                syncInvoiceDetailsSubmitRequestDTO.setBatchId(addInvoiceetailsSubmitTable.getBatchId());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setBatchId("");
            }
            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getToProcessorId())) {
                syncInvoiceDetailsSubmitRequestDTO.setToProcessorId(addInvoiceetailsSubmitTable.getToProcessorId());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setToProcessorId("");
            }


            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getFromProcessorId())) {
                syncInvoiceDetailsSubmitRequestDTO.setFromProcessorId(addInvoiceetailsSubmitTable.getFromProcessorId());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setFromProcessorId("");
            }


            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getFromDealerId())) {
                syncInvoiceDetailsSubmitRequestDTO.setFromDealerId(addInvoiceetailsSubmitTable.getFromDealerId());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setFromDealerId("");
            }
            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getToDealerId())) {
                syncInvoiceDetailsSubmitRequestDTO.setToDealerId(addInvoiceetailsSubmitTable.getToDealerId());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setToDealerId("");
            }
            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getToCustomerId())) {
                syncInvoiceDetailsSubmitRequestDTO.setToCustomerId(addInvoiceetailsSubmitTable.getToCustomerId());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setToCustomerId("");
            }

            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getProduct())) {
                syncInvoiceDetailsSubmitRequestDTO.setProduct(addInvoiceetailsSubmitTable.getProduct());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setProduct("");
            }
            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getQuantity())) {
                syncInvoiceDetailsSubmitRequestDTO.setQuantity(addInvoiceetailsSubmitTable.getQuantity());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setQuantity("");
            }


            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getVessel())) {
                syncInvoiceDetailsSubmitRequestDTO.setVessel(addInvoiceetailsSubmitTable.getVessel());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setVessel("");
            }

            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getPortofLoading())) {
                syncInvoiceDetailsSubmitRequestDTO.setPortofLoading(addInvoiceetailsSubmitTable.getPortofLoading());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setPortofLoading("");
            }
            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getPortofDischarge())) {
                syncInvoiceDetailsSubmitRequestDTO.setPortofDischarge(addInvoiceetailsSubmitTable.getPortofDischarge());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setPortofDischarge("");
            }



                if (addInvoiceetailsSubmitTable.getStrPOLTiming() != null) {
                    syncInvoiceDetailsSubmitRequestDTO.setStrPOLTiming(addInvoiceetailsSubmitTable.getStrPOLTiming());
                } else {
                    syncInvoiceDetailsSubmitRequestDTO.setStrPOLTiming(null);
                }


                if (addInvoiceetailsSubmitTable.getStrPODTiming()  != null) {
                    syncInvoiceDetailsSubmitRequestDTO.setStrPODTiming(addInvoiceetailsSubmitTable.getStrPODTiming());
                } else {
                    syncInvoiceDetailsSubmitRequestDTO.setStrPODTiming(null);
                }





            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getShippingAddress())) {
                syncInvoiceDetailsSubmitRequestDTO.setShippingAddress(addInvoiceetailsSubmitTable.getShippingAddress());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setShippingAddress("");
            }

            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getIsActive())) {
                syncInvoiceDetailsSubmitRequestDTO.setIsActive(addInvoiceetailsSubmitTable.getIsActive());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setIsActive("");
            }


            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getCreatedByUserId())) {
                syncInvoiceDetailsSubmitRequestDTO.setCreatedByUserId(addInvoiceetailsSubmitTable.getCreatedByUserId());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setCreatedByUserId("");
            }


            if (!TextUtils.isEmpty(addInvoiceetailsSubmitTable.getUpdatedByUserId())) {
                syncInvoiceDetailsSubmitRequestDTO.setUpdatedByUserId(addInvoiceetailsSubmitTable.getUpdatedByUserId());
            } else {
                syncInvoiceDetailsSubmitRequestDTO.setUpdatedByUserId("");
            }

            String dateTime = appHelper.getCurrentDateTime(AppConstant.DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS);
            syncInvoiceDetailsSubmitRequestDTO.setCreatedDate(dateTime);
            syncInvoiceDetailsSubmitRequestDTO.setUpdatedDate(dateTime);

            AppWebService.createService(AppAPI.class).syncAddProcessorInvoiceDetailsDataToServer(syncInvoiceDetailsSubmitRequestDTO, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, "")).
                    enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            executor.execute(() -> {
                                try {
                                    String strResponse = response.body().string();
                                    Log.e("onResponse: ", strResponse);
//                                    JSONObject json = new JSONObject(strResponse);
//                                    String serverResponse = json.getString("Message");
//                                    Log.e("response farmer result", serverResponse);

                                    JSONObject lObj = new JSONObject(strResponse);
                                    //  String status = lObj.optString("status");
                                    String message = lObj.getString("messgae");
                                    Log.d(TAG, "onResponse: data" + message);
//                                        Log.e("kzjdcvb", message);
                                    //String strMonitoringId = lObj.getString("id");
//                                    GROW_MONITORING_ID = null;
//                                    GROW_MONITORING_ID = strMonitoringId;
                                    // Log.e("id_monitoring", strMonitoringId);
                                    data.postValue(message);

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    data.postValue("Invoice added successfully");
                                }

                            });
                        }


                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            t.printStackTrace();
                            // TODO: Sending result
                            executor.execute(() -> {
                                data.postValue(FAILURE_RESPONSE_MESSAGE);
                            });
                        }
                    });
        });
        return data;
    }

    public LiveData<String> syncBatchProcessingHdrDetailsDataToServer(AddBatchProcessingHdrDetailsSubmitTable addBatchProcessingHdrSubmitTable, String typeReq) {
        final MutableLiveData<String> data = new MutableLiveData<>();
        AppWebService.changeApiBaseUrl(RAW_DATA_URL);
        executor.execute(() -> {

            final SyncBatchHdrDetailsSubmitRequestDTO syncBatchHdrDetailsSubmitRequestDTO = new SyncBatchHdrDetailsSubmitRequestDTO();


            if (!TextUtils.isEmpty(addBatchProcessingHdrSubmitTable.getBatchNo())) {
                syncBatchHdrDetailsSubmitRequestDTO.setBatchNo(addBatchProcessingHdrSubmitTable.getBatchNo());
            } else {
                syncBatchHdrDetailsSubmitRequestDTO.setBatchNo("");
            }


            if (!TextUtils.isEmpty(addBatchProcessingHdrSubmitTable.getIsActive())) {
                syncBatchHdrDetailsSubmitRequestDTO.setIsActive(addBatchProcessingHdrSubmitTable.getIsActive());
            } else {
                syncBatchHdrDetailsSubmitRequestDTO.setIsActive("");
            }

            if (!TextUtils.isEmpty(addBatchProcessingHdrSubmitTable.getProcessorId())) {
                syncBatchHdrDetailsSubmitRequestDTO.setProcessorId(addBatchProcessingHdrSubmitTable.getProcessorId());
            } else {
                syncBatchHdrDetailsSubmitRequestDTO.setProcessorId(null);
            }

            if (!TextUtils.isEmpty(addBatchProcessingHdrSubmitTable.getDealerId())) {
                syncBatchHdrDetailsSubmitRequestDTO.setDealerId(addBatchProcessingHdrSubmitTable.getDealerId());
            } else {
                syncBatchHdrDetailsSubmitRequestDTO.setDealerId(null);
            }


            if (!TextUtils.isEmpty(addBatchProcessingHdrSubmitTable.getCreatedByUserId())) {
                syncBatchHdrDetailsSubmitRequestDTO.setCreatedByUserId(addBatchProcessingHdrSubmitTable.getCreatedByUserId());
            } else {
                syncBatchHdrDetailsSubmitRequestDTO.setCreatedByUserId("");
            }


            if (!TextUtils.isEmpty(addBatchProcessingHdrSubmitTable.getUpdatedByUserId())) {
                syncBatchHdrDetailsSubmitRequestDTO.setUpdatedByUserId(addBatchProcessingHdrSubmitTable.getUpdatedByUserId());
            } else {
                syncBatchHdrDetailsSubmitRequestDTO.setUpdatedByUserId("");
            }


            String dateTime = appHelper.getCurrentDateTime(AppConstant.DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS);
            syncBatchHdrDetailsSubmitRequestDTO.setCreatedDate(dateTime);
            syncBatchHdrDetailsSubmitRequestDTO.setUpdatedDate(dateTime);

            AppWebService.createService(AppAPI.class).syncAddBatchHdrDetailsDataToServer(syncBatchHdrDetailsSubmitRequestDTO, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, "")).
                    enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            executor.execute(() -> {
                                try {
                                    String strResponse = response.body().string();
                                    Log.e("onResponse: ", strResponse);

//                                    JSONObject json = new JSONObject(strResponse);
//                                    String serverResponse = json.getString("Message");
//                                    Log.e("response farmer result", serverResponse);

                                    JSONObject lObj = new JSONObject(strResponse);
                                    //  String status = lObj.optString("status");
                                    String message = lObj.getString("messgae");
                                    Log.d(TAG, "onResponse: data" + message);
                                    Log.d("mtags", "onResponse: data" + lObj.getJSONArray("data").get(0).toString());
//                                        Log.e("kzjdcvb", message);
                                    //String strMonitoringId = lObj.getString("id");
//                                    GROW_MONITORING_ID = null;
//                                    GROW_MONITORING_ID = strMonitoringId;
                                    // Log.e("id_monitoring", strMonitoringId);
                                    String idData = lObj.getJSONArray("data").get(0).toString();
                                    data.postValue(idData);


                                } catch (Exception ex) {
                                    ex.printStackTrace();
//                                    data.postValue("Batch added successfully");
                                }

                            });
                        }


                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            t.printStackTrace();
                            // TODO: Sending result
                            executor.execute(() -> {
                                data.postValue(FAILURE_RESPONSE_MESSAGE);
                            });
                        }
                    });
        });
        return data;
    }

    public LiveData<String> syncBatchProcessingDtlDetailsDataToServer(AddBatchProcessingDtlDetailsSubmitTable batchProcessingDtlDetailsArrayList) {
        final MutableLiveData<String> data = new MutableLiveData<>();
        AppWebService.changeApiBaseUrl(RAW_DATA_URL);
        executor.execute(() -> {

            final SyncBatchDtlDetailsSubmitRequestDTO syncBatchDtlDetailsSubmitRequestDTO = new SyncBatchDtlDetailsSubmitRequestDTO();


            if (!TextUtils.isEmpty(batchProcessingDtlDetailsArrayList.getBatchHdrId())) {
                syncBatchDtlDetailsSubmitRequestDTO.setBatchHdrId(batchProcessingDtlDetailsArrayList.getBatchHdrId());
            } else {
                syncBatchDtlDetailsSubmitRequestDTO.setBatchHdrId("");
            }

            if (!TextUtils.isEmpty(batchProcessingDtlDetailsArrayList.getGRNId())) {
                syncBatchDtlDetailsSubmitRequestDTO.setGRNId(batchProcessingDtlDetailsArrayList.getGRNId());
            } else {
                syncBatchDtlDetailsSubmitRequestDTO.setGRNId("");
            }

//            if (!TextUtils.isEmpty(batchProcessingDtlDetailsArrayList.getProcessorId())) {
//                syncBatchDtlDetailsSubmitRequestDTO.setProcessorId(batchProcessingDtlDetailsArrayList.getProcessorId());
//            } else {
//                syncBatchDtlDetailsSubmitRequestDTO.setProcessorId(null);
//            }
//
//            if (!TextUtils.isEmpty(batchProcessingDtlDetailsArrayList.getDealerId())) {
//                syncBatchDtlDetailsSubmitRequestDTO.setDealerId(batchProcessingDtlDetailsArrayList.getDealerId());
//            } else {
//                syncBatchDtlDetailsSubmitRequestDTO.setDealerId(null);
//            }
//
//            if (!TextUtils.isEmpty(batchProcessingDtlDetailsArrayList.getFarmerId())) {
//                syncBatchDtlDetailsSubmitRequestDTO.setFarmerId(batchProcessingDtlDetailsArrayList.getFarmerId());
//            } else {
//                syncBatchDtlDetailsSubmitRequestDTO.setFarmerId("");
//            }

            if (!TextUtils.isEmpty(batchProcessingDtlDetailsArrayList.getQuantity())) {
                syncBatchDtlDetailsSubmitRequestDTO.setQuantity(batchProcessingDtlDetailsArrayList.getQuantity());
            } else {
                syncBatchDtlDetailsSubmitRequestDTO.setQuantity("");
            }


            if (!TextUtils.isEmpty(batchProcessingDtlDetailsArrayList.getIsActive())) {
                syncBatchDtlDetailsSubmitRequestDTO.setIsActive(batchProcessingDtlDetailsArrayList.getIsActive());
            } else {
                syncBatchDtlDetailsSubmitRequestDTO.setIsActive("1");
            }

            if (!TextUtils.isEmpty(batchProcessingDtlDetailsArrayList.getCreatedByUserId())) {
                syncBatchDtlDetailsSubmitRequestDTO.setCreatedByUserId(batchProcessingDtlDetailsArrayList.getCreatedByUserId());
            } else {
                syncBatchDtlDetailsSubmitRequestDTO.setCreatedByUserId("");
            }


            if (!TextUtils.isEmpty(batchProcessingDtlDetailsArrayList.getUpdatedByUserId())) {
                syncBatchDtlDetailsSubmitRequestDTO.setUpdatedByUserId(batchProcessingDtlDetailsArrayList.getUpdatedByUserId());
            } else {
                syncBatchDtlDetailsSubmitRequestDTO.setUpdatedByUserId("");
            }


            syncBatchDtlDetailsSubmitRequestDTO.setCreatedDate(batchProcessingDtlDetailsArrayList.getCreatedDate());
            syncBatchDtlDetailsSubmitRequestDTO.setUpdatedDate(batchProcessingDtlDetailsArrayList.getUpdatedDate());

            AppWebService.createService(AppAPI.class).syncAddBatchDtlDetailsDataToServer(syncBatchDtlDetailsSubmitRequestDTO, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, "")).
                    enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            executor.execute(() -> {
                                try {
                                    String strResponse = response.body().string();
                                    Log.e("onResponse", "dealerDetails:" + strResponse);
//                                    JSONObject json = new JSONObject(strResponse);
//                                    String serverResponse = json.getString("Message");
//                                    Log.e("response farmer result", serverResponse);

                                    JSONObject lObj = new JSONObject(strResponse);
                                    //  String status = lObj.optString("status");
                                    String message = lObj.getString("messgae");
                                    Log.d(TAG, "onResponse: data" + message);
//                                        Log.e("kzjdcvb", message);
                                    //String strMonitoringId = lObj.getString("id");
//                                    GROW_MONITORING_ID = null;
//                                    GROW_MONITORING_ID = strMonitoringId;
                                    // Log.e("id_monitoring", strMonitoringId);
                                    data.postValue("GRN successfully");

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    data.postValue("GRN added successfully");
                                }

                            });
                        }


                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            t.printStackTrace();
                            // TODO: Sending result
                            executor.execute(() -> {
                                data.postValue(FAILURE_RESPONSE_MESSAGE);
                            });
                        }
                    });
        });
        return data;
    }

    //Post Header Drc
    public LiveData<String> syncDRCHdrDetailsDataToServer(PostDRCHdrDetailsDTO postDRCHdrDetailsDTO) {
        final MutableLiveData<String> data = new MutableLiveData<>();
        AppWebService.changeApiBaseUrl(RAW_DATA_URL);
        executor.execute(() -> {

            final PostDRCHdrDetailsDTO postData = new PostDRCHdrDetailsDTO();


            if (!TextUtils.isEmpty(postDRCHdrDetailsDTO.getDRCNo())) {
                postData.setDRCNo(postDRCHdrDetailsDTO.getDRCNo());
            } else {
                postData.setDRCNo("");
            }

            if (!TextUtils.isEmpty(postDRCHdrDetailsDTO.getDRCDocument())) {
                postData.setDRCDocument(postDRCHdrDetailsDTO.getDRCDocument());
            } else {
                postData.setDRCDocument("");
            }


            if (!TextUtils.isEmpty(postDRCHdrDetailsDTO.getIsActive())) {
                postData.setIsActive(postDRCHdrDetailsDTO.getIsActive());
            } else {
                postData.setIsActive("");
            }


            if (!TextUtils.isEmpty(postDRCHdrDetailsDTO.getCreatedByUserId())) {
                postData.setCreatedByUserId(postDRCHdrDetailsDTO.getCreatedByUserId());
            } else {
                postData.setCreatedByUserId("");
            }


            if (!TextUtils.isEmpty(postDRCHdrDetailsDTO.getUpdatedByUserId())) {
                postData.setUpdatedByUserId(postDRCHdrDetailsDTO.getUpdatedByUserId());
            } else {
                postData.setUpdatedByUserId("");
            }

            if (!TextUtils.isEmpty(postDRCHdrDetailsDTO.getProcessorId())) {
                postData.setProcessorId(postDRCHdrDetailsDTO.getProcessorId());
            } else {
                postData.setProcessorId("");
            }

            postData.setCreatedDate(postDRCHdrDetailsDTO.getCreatedDate());
            postData.setUpdatedDate(postDRCHdrDetailsDTO.getUpdatedDate());

            AppWebService.createService(AppAPI.class).syncAddDRCHdrDetailsDataToServer(postData, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, "")).
                    enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            executor.execute(() -> {
                                try {
                                    String strResponse = response.body().string();
                                    Log.e("DRC Hdr onResponse: ", strResponse);

//                                    JSONObject json = new JSONObject(strResponse);
//                                    String serverResponse = json.getString("Message");
//                                    Log.e("response farmer result", serverResponse);

                                    JSONObject lObj = new JSONObject(strResponse);
                                    //  String status = lObj.optString("status");
                                    String message = lObj.getString("messgae");
                                    Log.d(TAG, "onResponse: data" + message);
                                    Log.d("mtags", "onResponse: data" + lObj.getJSONArray("data").get(0).toString());
//                                        Log.e("kzjdcvb", message);
                                    //String strMonitoringId = lObj.getString("id");
//                                    GROW_MONITORING_ID = null;
//                                    GROW_MONITORING_ID = strMonitoringId;
                                    // Log.e("id_monitoring", strMonitoringId);
                                    String idData = lObj.getJSONArray("data").get(0).toString();
                                    data.postValue(idData);


                                } catch (Exception ex) {
                                    ex.printStackTrace();
//                                    data.postValue("Batch added successfully");
                                }

                            });
                        }


                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            t.printStackTrace();
                            // TODO: Sending result
                            executor.execute(() -> {
                                data.postValue(FAILURE_RESPONSE_MESSAGE);
                            });
                        }
                    });
        });
        return data;
    }

    //Post Drc dtl
    public LiveData<String> syncDRCDtlDetailsDataToServer(PostDRCDtlDetailsDTO postDRCDtlDetailsDTO) {
        final MutableLiveData<String> data = new MutableLiveData<>();
        AppWebService.changeApiBaseUrl(RAW_DATA_URL);
        executor.execute(() -> {

            final PostDRCDtlDetailsDTO postData = new PostDRCDtlDetailsDTO();


            if (!TextUtils.isEmpty(String.valueOf(postDRCDtlDetailsDTO.getDRCHdrId()))) {
                postData.setDRCHdrId(postDRCDtlDetailsDTO.getDRCHdrId());
            } else {
                postData.setDRCHdrId(0);
            }

            if (!TextUtils.isEmpty(String.valueOf(postDRCDtlDetailsDTO.getGRNId()))) {
                postData.setGRNId(postDRCDtlDetailsDTO.getGRNId());
            } else {
                postData.setGRNId(0);
            }

            if (!TextUtils.isEmpty(String.valueOf(postDRCDtlDetailsDTO.getDRCValue()))) {
                postData.setDRCValue(postDRCDtlDetailsDTO.getDRCValue());
            } else {
                postData.setDRCValue(0);
            }


            if (!TextUtils.isEmpty(postDRCDtlDetailsDTO.getIsActive())) {
                postData.setIsActive(postDRCDtlDetailsDTO.getIsActive());
            } else {
                postData.setIsActive("1");
            }

            if (!TextUtils.isEmpty(postDRCDtlDetailsDTO.getCreatedByUserId())) {
                postData.setCreatedByUserId(postDRCDtlDetailsDTO.getCreatedByUserId());
            } else {
                postData.setCreatedByUserId("");
            }


            if (!TextUtils.isEmpty(postDRCDtlDetailsDTO.getUpdatedByUserId())) {
                postData.setUpdatedByUserId(postDRCDtlDetailsDTO.getUpdatedByUserId());
            } else {
                postData.setUpdatedByUserId("");
            }


            postData.setCreatedDate(postDRCDtlDetailsDTO.getCreatedDate());
            postData.setUpdatedDate(postDRCDtlDetailsDTO.getUpdatedDate());

            AppWebService.createService(AppAPI.class).syncAddDRCDtlDetailsDataToServer(postData, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, "")).
                    enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            executor.execute(() -> {
                                try {
                                    String strResponse = response.body().string();
                                    Log.e("onResponse", "DrcDtlDetails:" + strResponse);
//                                    JSONObject json = new JSONObject(strResponse);
//                                    String serverResponse = json.getString("Message");
//                                    Log.e("response farmer result", serverResponse);

                                    JSONObject lObj = new JSONObject(strResponse);
                                    //  String status = lObj.optString("status");
                                    String message = lObj.getString("messgae");
                                    Log.d(TAG, "onResponse: data" + message);
//                                        Log.e("kzjdcvb", message);
                                    //String strMonitoringId = lObj.getString("id");
//                                    GROW_MONITORING_ID = null;
//                                    GROW_MONITORING_ID = strMonitoringId;
                                    // Log.e("id_monitoring", strMonitoringId);
                                    data.postValue("GRN successfully");

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    data.postValue("GRN added successfully");
                                }

                            });
                        }


                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            t.printStackTrace();
                            // TODO: Sending result
                            executor.execute(() -> {
                                data.postValue(FAILURE_RESPONSE_MESSAGE);
                            });
                        }
                    });
        });
        return data;
    }


}


