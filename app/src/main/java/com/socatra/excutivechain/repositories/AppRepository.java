package com.socatra.excutivechain.repositories;

import static com.socatra.excutivechain.AppConstant.FAILURE_RESPONSE_MESSAGE;
import static com.socatra.excutivechain.AppConstant.RAW_DATA_URL;
import static com.socatra.excutivechain.AppConstant.SUCCESS_RESPONSE_MESSAGE;
import static com.socatra.excutivechain.AppConstant.accessToken;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.socatra.excutivechain.AppHelper;
import com.socatra.excutivechain.api.AppAPI;
import com.socatra.excutivechain.api.webservice.AppWebService;
import com.socatra.excutivechain.database.dao.AppDAO;
import com.socatra.excutivechain.database.entity.AppLanguageHDRTable;
import com.socatra.excutivechain.database.entity.AppLanguageTable;
import com.socatra.excutivechain.database.entity.Country;
import com.socatra.excutivechain.database.entity.DealerFarmer;
import com.socatra.excutivechain.database.entity.DealerMaster;
import com.socatra.excutivechain.database.entity.DistrictorRegency;
import com.socatra.excutivechain.database.entity.FarmerHouseholdChildrenSurvey;
import com.socatra.excutivechain.database.entity.FarmerHouseholdParentSurvey;
import com.socatra.excutivechain.database.entity.FarmersTable;
import com.socatra.excutivechain.database.entity.ManfacturerFarmer;
import com.socatra.excutivechain.database.entity.ManufacturerMaster;
import com.socatra.excutivechain.database.entity.Plantation;
import com.socatra.excutivechain.database.entity.PlantationDocuments;
import com.socatra.excutivechain.database.entity.PlantationGeoBoundaries;
import com.socatra.excutivechain.database.entity.PlantationLabourSurvey;
import com.socatra.excutivechain.database.entity.RefreshTableDateCheck;
import com.socatra.excutivechain.database.entity.RiskAssessment;
import com.socatra.excutivechain.database.entity.RiskAssessmentQuestion;
import com.socatra.excutivechain.database.entity.StateorProvince;
import com.socatra.excutivechain.database.entity.SubDistrict;
import com.socatra.excutivechain.database.entity.VillageTable;
import com.socatra.excutivechain.models.LoginResponseDTO;
import com.socatra.excutivechain.models.SyncPersonalLandAllDetailsRequestDTO;

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

//import okhttp3.ResponseBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;

@Singleton
public class AppRepository {

    private static final String TAG = com.socatra.excutivechain.repositories.AppRepository.class.getCanonicalName();

    private final AppDAO appDAO;
    private final Executor executor;
    private final AppHelper appHelper;
    Context context;

    @Inject
    public AppRepository(AppDAO appDAO, Executor executor, AppHelper appHelper, Context context) {
        this.appDAO = appDAO;
        this.executor = executor;
        this.appHelper = appHelper;
        this.context = context;
    }

    // login api call
    public LiveData<LoginResponseDTO> getlogInServiceResponse(String userId) {
        final MutableLiveData<LoginResponseDTO> data = new MutableLiveData<>();
        try {
            AppWebService.changeApiBaseUrl(RAW_DATA_URL);
            executor.execute(() -> {

                if (appHelper.isNetworkAvailable()) { // TODO: Checking internet connection
                    AppWebService.createService(AppAPI.class).getlogInService(userId)
                            .enqueue(new Callback<LoginResponseDTO>() {
                                @Override
                                public void onResponse(Call<LoginResponseDTO> call, Response<LoginResponseDTO> response) {
                                    Log.e("TAG", "Login LIST REFRESHED FROM NETWORK");
                                    executor.execute(() -> {
//                                        LoginResponseDTO loginResponseDTOList = ;
//                                        if (loginResponseDTOList != null && loginResponseDTOList.size() > 0) {
//                                            // TODO: Delete & Insert Stage List
//                                            for (LoginResponseDTO loginResponseDTO : loginResponseDTOList) {
//
//                                                // if (cropDetailsTable != null && !TextUtils.isEmpty(cropDetailsTable.getId())) {
//                                                LoginResponseDTO loginResponseDTO1 = new LoginResponseDTO();
//                                                Log.d("deviceId", loginResponseDTO.getData().get(0).getId().toString());
//                                                loginResponseDTO1.getData().get(0).setId(loginResponseDTO.getData().get(0).getId());
//                                                loginResponseDTO1.getData().get(0).setUserName(loginResponseDTO.getUserName());
//                                                loginResponseDTO1.getData().get(0).setPassword(loginResponseDTO.getPassword());
//                                                loginResponseDTO1.getData().get(0).setAccessToken(loginResponseDTO.getAccessToken());
////                                                loginResponseDTO1.setDeviceUserID(loginResponseDTO.getDeviceUserID());
////                                                loginResponseDTO1.setDeviceUserID(loginResponseDTO.getDeviceUserID());
////
//                                            }
//
//                                            // TODO: Sending Final Result
//                                            data.postValue(loginResponseDTOList);
//                                        } else {
//                                            // TODO: Sending Final Result
//                                            LoginResponseDTO emptyStageList = new LoginResponseDTO();
//                                            data.postValue(emptyStageList);
//                                        }
//                                        List<Datum> datumList=new ArrayList<>();
                                        if (response.body() != null){
//                                            datumList.add(loginResponseDTOList.getData().get(0));
                                            data.postValue(response.body());
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

                }
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

    // clearing local db for master details
    public void deleteAllTablesFromLocalMaster() {
        executor.execute(() -> {
            appDAO.deleteAllTablesFromLocalMaster();
        });
    }

    // clearing local db for transaction details
    public void deleteTablesFromLocalTransactionData() {
        executor.execute(() -> {
            appDAO.deleteTablesFromLocalTransactionData("1");
        });
    }

    // daily login insertion
    public LiveData<RefreshTableDateCheck> getAddRefreshTableDateCheckTableDate(String deviceID, String currentDate) {
        final MutableLiveData<RefreshTableDateCheck> data = new MutableLiveData<>();
        executor.execute(() -> {
            Log.d(TAG, "deviceID" + deviceID + "date" + currentDate);
            data.postValue(appDAO.getRefreshTableDateCheckDetailsFromLocalDB(deviceID, currentDate));

        });
        return data;
    }


    public LiveData<RefreshTableDateCheck> insertRefreshTableDateCheck(RefreshTableDateCheck refreshTableDateCheck) {
        final MutableLiveData<RefreshTableDateCheck> data = new MutableLiveData<>();
        executor.execute(() -> {
            appDAO.insertRefreshTableDateCheckTable(refreshTableDateCheck);
            // TODO: Sending result
            data.postValue(appDAO.getTopRefreshTableDateCheckData(refreshTableDateCheck.getDeviceID()));
        });
        return data;
    }


    //Country
    public LiveData<Country> insertCountryListDataIntoLocalDBRepository(Country country) {
        final MutableLiveData<Country> data = new MutableLiveData<>();
        executor.execute(() -> {
            appDAO.insertMasterSyncCountryListIntoLocalDB(country);
        });
        return data;
    }

    //StateorProvince
    public LiveData<StateorProvince> insertStateListDataIntoLocalDBRepository(StateorProvince stateorProvince) {
        final MutableLiveData<StateorProvince> data = new MutableLiveData<>();
        executor.execute(() -> {
            appDAO.insertMasterSyncStateListIntoLocalDB(stateorProvince);
        });
        return data;
    }

    //DistrictorRegency
    public LiveData<DistrictorRegency> insertDistrictListDataIntoLocalDBRepository(DistrictorRegency districtorRegency) {
        final MutableLiveData<DistrictorRegency> data = new MutableLiveData<>();
        executor.execute(() -> {
            appDAO.insertMasterSyncDistrictListIntoLocalDB(districtorRegency);
        });
        return data;
    }

    //SubDistrict
    public LiveData<SubDistrict> insertSubDistrictListDataIntoLocalDBRepository(SubDistrict subDistrict) {
        final MutableLiveData<SubDistrict> data = new MutableLiveData<>();
        executor.execute(() -> {
            appDAO.insertMasterSyncSubDistrictListIntoLocalDB(subDistrict);
        });
        return data;
    }


    //Village
    public LiveData<VillageTable> insertVillageListDataIntoLocalDBRepository(VillageTable villageTable) {
        final MutableLiveData<VillageTable> data = new MutableLiveData<>();
        executor.execute(() -> {
            // FarmerDetailListTable topFarmerDetailListTableTableData = appDAO.getTopFarmerDetailListTableTableData(farmerDetailListTable.getFirstName(), farmerDetailListTable.getFarmerCode());
            appDAO.insertMasterSyncvillageListIntoLocalDB(villageTable);
            //data.postValue(appDAO.getTopMasterSyncVillageTablDataLocalDBQuery(villageTable.getCode()));
        });
        return data;
    }

    //Risk Question Master
    public LiveData<RiskAssessmentQuestion> insertRiskAssessmentQuestionListDataIntoLocalDBRepository(RiskAssessmentQuestion riskAssessmentQuestion) {
        final MutableLiveData<RiskAssessmentQuestion> data = new MutableLiveData<>();
        executor.execute(() -> {
            // FarmerDetailListTable topFarmerDetailListTableTableData = appDAO.getTopFarmerDetailListTableTableData(farmerDetailListTable.getFirstName(), farmerDetailListTable.getFarmerCode());
            appDAO.insertMasterSyncRiskAssessmentQuestionListIntoLocalDB(riskAssessmentQuestion);
            //data.postValue(appDAO.getTopMasterSyncVillageTablDataLocalDBQuery(villageTable.getCode()));
        });
        return data;
    }

    //Manufacturer Master
    public LiveData<ManufacturerMaster> insertManufacturerMasterDataIntoLocalDBRepository(ManufacturerMaster manufacturerMaster) {
        final MutableLiveData<ManufacturerMaster> data = new MutableLiveData<>();
        executor.execute(() -> {
            // FarmerDetailListTable topFarmerDetailListTableTableData = appDAO.getTopFarmerDetailListTableTableData(farmerDetailListTable.getFirstName(), farmerDetailListTable.getFarmerCode());
            appDAO.insertMasterSyncManufacturerMasterListIntoLocalDB(manufacturerMaster);
            //data.postValue(appDAO.getTopMasterSyncVillageTablDataLocalDBQuery(villageTable.getCode()));
        });
        return data;
    }

    //Manufacturer Master
    public LiveData<DealerMaster> insertDealerMasterDataIntoLocalDBRepository(DealerMaster dealerMaster) {
        final MutableLiveData<DealerMaster> data = new MutableLiveData<>();
        executor.execute(() -> {
            // FarmerDetailListTable topFarmerDetailListTableTableData = appDAO.getTopFarmerDetailListTableTableData(farmerDetailListTable.getFirstName(), farmerDetailListTable.getFarmerCode());
            appDAO.insertMasterSyncDealerMasterListIntoLocalDB(dealerMaster);
            //data.postValue(appDAO.getTopMasterSyncVillageTablDataLocalDBQuery(villageTable.getCode()));
        });
        return data;
    }

    //App Language
    public LiveData<AppLanguageTable> insertLanguageMasterDataIntoLocalDBRepository(AppLanguageTable table) {
        final MutableLiveData<AppLanguageTable> data = new MutableLiveData<>();
        executor.execute(() -> {
            appDAO.insertMasterSyncLanguageMasterListIntoLocalDB(table);
        });
        return data;
    }

    //App Language
    public LiveData<AppLanguageHDRTable> insertLanguageHDRMasterDataIntoLocalDBRepository(AppLanguageHDRTable table) {
        final MutableLiveData<AppLanguageHDRTable> data = new MutableLiveData<>();
        executor.execute(() -> {
            appDAO.insertMasterSyncLanguageHDRMasterListIntoLocalDB(table);
        });
        return data;
    }



    public LiveData<FarmersTable> insertFarmerDetailListTableTable(FarmersTable farmerTable) {
        final MutableLiveData<FarmersTable> data = new MutableLiveData<>();
        executor.execute(() -> {
            appDAO.insertFarmerDetailListTableLocalDB(farmerTable);
            data.postValue(appDAO.geInsertFarmerDetailListTableData(farmerTable.getFarmerCode()));
        });
        return data;
    }


    public LiveData<FarmersTable> getFarmersDataFromLocalDb(String strFarmerCode) {
        final MutableLiveData<FarmersTable> data = new MutableLiveData<>();
        executor.execute(() -> {
            // TODO: Sending result
            data.postValue(appDAO.getFarmerDetailsByFarmerCode(strFarmerCode));
        });
        return data;
    }

    public LiveData<VillageTable> getDisIdFromVillageTableById(String strVillageId) {
        final MutableLiveData<VillageTable> data = new MutableLiveData<>();
        executor.execute(() -> {
            // TODO: Sending result
            data.postValue(appDAO.getSubDisIdFromVillageTableById(strVillageId));
        });
        return data;
    }

    //App Language
    public LiveData<String> getLanguageDataRepository(String stLang,String stWord) {
        final MutableLiveData<String > data = new MutableLiveData<>();
        executor.execute(() -> {
            // TODO: Sending result
            data.postValue(appDAO.getLanguageData(stLang,stWord));
        });
        return data;
    }

    public String getLanguageDataRepo(String stLang, String stWord) {
//        String data;
//        executor.execute(() -> {
//            // TODO: Sending result
//            data=appDAO.getLanguageData(stLang,stWord);
//        });
        return appDAO.getLanguageData(stLang, stWord);
    }


    // TODO: 9/14/2023 new added

    public LiveData<SubDistrict> getDisIdFromSubDistricTableById(String strSubDistricId) {
        final MutableLiveData<SubDistrict> data = new MutableLiveData<>();
        executor.execute(() -> {
            // TODO: Sending result
            data.postValue(appDAO.getDisIdFromSubDistricTableById(strSubDistricId));
        });
        return data;
    }

    public LiveData<DistrictorRegency> getDistricDetailsByIdFromLocalDb(Integer strDisId) {
        final MutableLiveData<DistrictorRegency> data = new MutableLiveData<>();
        executor.execute(() -> {
            // TODO: Sending result
            data.postValue(appDAO.getDistricDetailsFromLocalDb(strDisId));
        });
        return data;
    }


    public LiveData<StateorProvince> getSateorProvinceDetailsFromLocalDb(Integer stateId) {
        final MutableLiveData<StateorProvince> data = new MutableLiveData<>();
        executor.execute(() -> {
            // TODO: Sending result
            data.postValue(appDAO.getStateorProcinceDetailsFromLocalDb(stateId));
        });
        return data;
    }
    public LiveData<Country> getCountryDetailsByIdFromLocalDb(Integer countryId) {
        final MutableLiveData<Country> data = new MutableLiveData<>();
        executor.execute(() -> {
            // TODO: Sending result
            data.postValue(appDAO.getCountryDetailsFromLocalDb(countryId));
        });
        return data;
    }
    //Plantation
    public LiveData<Plantation> insertPlantationDetailListTableTable(Plantation plantation) {
        final MutableLiveData<Plantation> data = new MutableLiveData<>();
        executor.execute(() -> {
            appDAO.insertPlantationDetailListTableLocalDB(plantation);
            data.postValue(appDAO.getInsertPlotDetailListTableData(plantation.getFarmerCode()));
        });
        return data;
    }

    //Plantation Doc
    public LiveData<PlantationDocuments> insertPlantationDocDetailListTableTable(PlantationDocuments plantationDocuments) {
        final MutableLiveData<PlantationDocuments> data = new MutableLiveData<>();
        executor.execute(() -> {
            appDAO.insertPlantationDocDetailListTableLocalDB(plantationDocuments);
            data.postValue(appDAO.getInsertPlantDocDetailListTableData(plantationDocuments.getPlotCode()));
        });
        return data;
    }

    //Plantation Geo
    public LiveData<PlantationGeoBoundaries> insertPlantationGeoDetailListTableTable(PlantationGeoBoundaries plantationGeoBoundaries) {
        final MutableLiveData<PlantationGeoBoundaries> data = new MutableLiveData<>();
        executor.execute(() -> {
            appDAO.insertPlantationGeoDetailListTableLocalDB(plantationGeoBoundaries);
            data.postValue(appDAO.getInsertPlantGeoDetailListTableData(plantationGeoBoundaries.getFarmerCode()));
        });
        return data;
    }

    //Labour Survey
    public LiveData<PlantationLabourSurvey> insertPlantationLabourSurveyListTableTable(PlantationLabourSurvey plantationLabourSurvey) {
        final MutableLiveData<PlantationLabourSurvey> data = new MutableLiveData<>();
        executor.execute(() -> {
            appDAO.insertPlantationLabourSurveyListTableLocalDB(plantationLabourSurvey);
//            data.postValue(appDAO.getInsertPlantGeoDetailListTableData(plantationLabourSurvey.getFarmerCode()));
        });
        return data;
    }

    //FarmerHouseholdParentSurvey
    public LiveData<FarmerHouseholdParentSurvey> insertFarmerHouseholdParentSurveyListTableTable(FarmerHouseholdParentSurvey farmerHouseholdParentSurvey) {
        final MutableLiveData<FarmerHouseholdParentSurvey> data = new MutableLiveData<>();
        executor.execute(() -> {
            appDAO.insertFarmerHouseholdParentSurveyListTableLocalDB(farmerHouseholdParentSurvey);
//            data.postValue(appDAO.getInsertPlantGeoDetailListTableData(farmerHouseholdParentSurvey.getFarmerCode()));
        });
        return data;
    }

    //FarmerHouseholdChildrenSurvey
    public LiveData<FarmerHouseholdChildrenSurvey> insertFarmerHouseholdChildrenSurveyListTableTable(FarmerHouseholdChildrenSurvey farmerHouseholdChildrenSurvey) {
        final MutableLiveData<FarmerHouseholdChildrenSurvey> data = new MutableLiveData<>();
        executor.execute(() -> {
            appDAO.insertFarmerHouseholdChildrenSurveyListTableLocalDB(farmerHouseholdChildrenSurvey);
//            data.postValue(appDAO.getInsertPlantGeoDetailListTableData(farmerHouseholdParentSurvey.getFarmerCode()));
        });
        return data;
    }

    public LiveData<PlantationGeoBoundaries> insertGoeDataIntolocaDB(PlantationGeoBoundaries geoBoundariesTable) {
        final MutableLiveData<PlantationGeoBoundaries> data = new MutableLiveData<>();
        executor.execute(() -> {
            appDAO.insertGeoData(geoBoundariesTable);
            data.postValue(appDAO.getTopGeoBoundariesTableFromLocalDb(geoBoundariesTable.getPlotCode()));
        });
        return data;
    }

    public LiveData<PlantationDocuments> insertDocIntoLocalDB(PlantationDocuments docIdentiFicationDeatilsTable) {
        final MutableLiveData<PlantationDocuments> data = new MutableLiveData<>();
        executor.execute(() -> {
            appDAO.insertDocDetailListTable(docIdentiFicationDeatilsTable);

//            FarmerDetailListTable topFarmerDetailListTableTableData = appDAO.getTopFarmerDetailListTableTableData(farmerDetailListTable.getFirstName(), farmerDetailListTable.getFarmerCode());
//            if (topFarmerDetailListTableTableData != null) {
//                topFarmerDetailListTableTableData.setFarmerId(farmerDetailListTable.getFarmerId());
//                appDAO.insertFarmerDetailListTable(farmerDetailListTable);
//            } else {
//
//            }
//            CurrentVisitFarmerTables currentVisitFarmerTables = appDAO.getTopCurrentVisitFarmerTablesData( appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
//            if (currentVisitFarmerTables != null) {
//                currentVisitFarmerTables.setDocumentsVisit(false);
//                appDAO.insertCurrentVisitFarmerTable(currentVisitFarmerTables);
//            }
            // TODO: Sending result
            data.postValue(appDAO.getTopDocListTableDataFromLocal(docIdentiFicationDeatilsTable.getFarmerCode()));
        });
        return data;
    }


    //PlantationLabourSurvey
    public LiveData<PlantationLabourSurvey> insertPlantationLabourSurveyIntoLocalDB(PlantationLabourSurvey plantationLabourSurvey) {
        final MutableLiveData<PlantationLabourSurvey> data = new MutableLiveData<>();
        executor.execute(() -> {
            appDAO.insertPlantationLabourSurveyDetailListTable(plantationLabourSurvey);
//            data.postValue(appDAO.getTopDocListTableDataFromLocal(docIdentiFicationDeatilsTable.getFarmerCode()));
        });
        return data;
    }

    public LiveData<Integer> getNotSyncFarmerCountDataFromLocalDB() {
        return appDAO.getFarmerNotSyncCountFromLocalDB("0");
    }

    //Survey
    public LiveData<Integer> getNotSyncSurveyCountDataFromLocalDB() {
        return appDAO.getSurveyNotSyncCountFromLocalDB("0");
    }

    //Plantation Count
    public LiveData<Integer> getNotSyncPlantationCountDataFromLocalDB() {
        return appDAO.getPlantationNotSyncCountFromLocalDB("0");
    }


    public LiveData<Integer> getNotSyncDocCountDataFromLocalDB() {
        return appDAO.getDocNotSyncCountFromLocalDB("0");
    }

    public LiveData<Integer> getNotSyncGeoCountDataFromLocalDB() {
        return appDAO.getGeoNotSyncCountFromLocalDB("0");
    }


    //Village by pin-code
    public LiveData<List<VillageTable>> getVillageTableDetailsFromLocalDbByPincode(String pincode) {
        final MutableLiveData<List<VillageTable>> data = new MutableLiveData<>();
        executor.execute(() -> {
            Log.d(TAG, "pincode" + pincode);
            boolean dataExist = (appDAO.getVillageDetailsFromLocalDbBySubDist(pincode) != null);
            if (dataExist) {
                data.postValue(appDAO.getVillageDetailsFromLocalDbBySubDist(pincode));
            }
        });
        return data;
    }



    public LiveData<List<VillageTable>> getAllVillageTableDetailsFromLocalDbByPincode() {
        final MutableLiveData<List<VillageTable>> data = new MutableLiveData<>();
        executor.execute(() -> {
            boolean dataExist = (appDAO.getAllVillageDetailsFromLocalDbBySubDist() != null);
            if (dataExist) {
                data.postValue(appDAO.getAllVillageDetailsFromLocalDbBySubDist());
            }
        });
        return data;
    }

    //SubDistrict by Id
    public LiveData<List<SubDistrict>> getSubDistrictTableDetailsFromLocalDbById(String id) {
        final MutableLiveData<List<SubDistrict>> data = new MutableLiveData<>();
        executor.execute(() -> {
            Log.d(TAG, "SubDistrictId" + id);
            boolean dataExist = (appDAO.getSubDistrictDetailsFromLocalDbByDistrictId(id) != null);
            if (dataExist) {
                data.postValue(appDAO.getSubDistrictDetailsFromLocalDbByDistrictId(id));
            }
        });
        return data;
    }

    //District by Id
    public LiveData<List<DistrictorRegency>> getDistrictTableDetailsFromLocalDbById(int id) {
        final MutableLiveData<List<DistrictorRegency>> data = new MutableLiveData<>();
        executor.execute(() -> {
            Log.d(TAG, "DistrictId" + id);
            boolean dataExist = (appDAO.getDistrictDetailsFromLocalDbByStateId(id) != null);
            if (dataExist) {
                data.postValue(appDAO.getDistrictDetailsFromLocalDbByStateId(id));
            }
        });
        return data;
    }

    //StateorProvince by Id
    public LiveData<List<StateorProvince>> getStateTableDetailsFromLocalDbById(int id) {
        final MutableLiveData<List<StateorProvince>> data = new MutableLiveData<>();
        executor.execute(() -> {
            Log.d(TAG, "State" + id);
            boolean dataExist = (appDAO.getStateDetailsFromLocalDbByCountryId(id) != null);
            if (dataExist) {
                data.postValue(appDAO.getStateDetailsFromLocalDbByCountryId(id));
            }
        });
        return data;
    }

    //Country by Id
    public LiveData<List<Country>> getCountryTableDetailsFromLocalDbById() {
        final MutableLiveData<List<Country>> data = new MutableLiveData<>();
        executor.execute(() -> {
            boolean dataExist = (appDAO.getAllCountryDetailsFromLocalDbById() != null);
            if (dataExist) {
                data.postValue(appDAO.getAllCountryDetailsFromLocalDbById());
            }
        });
        return data;
    }

    //App Language HDR
    public LiveData<List<AppLanguageHDRTable>> getAllLanguagesFromHDR() {
        final MutableLiveData<List<AppLanguageHDRTable>> data = new MutableLiveData<>();
        executor.execute(() -> {
            boolean dataExist = (appDAO.getAllLanguagesFromHDR() != null);
            if (dataExist) {
                data.postValue(appDAO.getAllLanguagesFromHDR());
            }
        });
        return data;
    }


    //Manufacturer Master list
    public LiveData<List<ManufacturerMaster>> getManufacturerMasterTableDetailsFromLocalDb() {
        final MutableLiveData<List<ManufacturerMaster>> data = new MutableLiveData<>();
        executor.execute(() -> {
            boolean dataExist = (appDAO.getAllManufacturerMasterDetailsFromLocalDbById() != null);
            if (dataExist) {
                data.postValue(appDAO.getAllManufacturerMasterDetailsFromLocalDbById());
            }
        });
        return data;
    }

    //Dealer Master list
    public LiveData<List<DealerMaster>> getDealerMasterTableDetailsFromLocalDb() {
        final MutableLiveData<List<DealerMaster>> data = new MutableLiveData<>();
        executor.execute(() -> {
            boolean dataExist = (appDAO.getAllDealerMasterDetailsFromLocalDbById() != null);
            if (dataExist) {
                data.postValue(appDAO.getAllDealerMasterDetailsFromLocalDbById());
            }
        });
        return data;
    }


    //Village by Id
    public LiveData<List<VillageTable>> getVillageTableDetailsFromLocalDbById(String id) {
        final MutableLiveData<List<VillageTable>> data = new MutableLiveData<>();
        executor.execute(() -> {
            Log.d(TAG, "Id" + id);
            boolean dataExist = (appDAO.getVillageDetailsFromLocalDbById(id) != null);
            if (dataExist) {
                data.postValue(appDAO.getVillageDetailsFromLocalDbById(id));
            }
        });
        return data;
    }

    //Plantation by fid
    public LiveData<List<Plantation>> getPlantationDetailsFromLocalDbById(String strFarmercode) {
        final MutableLiveData<List<Plantation>> data = new MutableLiveData<>();
        executor.execute(() -> {
            Log.d(TAG, "FId" + strFarmercode);
            boolean dataExist = (appDAO.getPlantationDetailsFromLocalDbById(strFarmercode) != null);
            if (dataExist) {
                data.postValue(appDAO.getPlantationDetailsFromLocalDbById(strFarmercode));
            }
        });
        return data;
    }


    //
    //PlantationLabourSurvey by fid
    public LiveData<List<PlantationLabourSurvey>> getPlantationLabourSurveyDetailsFromLocalDbById(String fid) {
        final MutableLiveData<List<PlantationLabourSurvey>> data = new MutableLiveData<>();
        executor.execute(() -> {
            Log.d(TAG, "FId" + fid);
            boolean dataExist = (appDAO.getPlantationLabourSurveyDetailsFromLocalDbById(fid) != null);
            if (dataExist) {
                data.postValue(appDAO.getPlantationLabourSurveyDetailsFromLocalDbById(fid));
            }
        });
        return data;
    }

    //for lab adp getPlantationDetailsFromLocalDbBymId
    public LiveData<List<Plantation>> getPlantationDetailsFromLocalDbBymId(String fid) {
        final MutableLiveData<List<Plantation>> data = new MutableLiveData<>();
        executor.execute(() -> {
            Log.d(TAG, "FId" + fid);
            boolean dataExist = (appDAO.getPlantationDetailsFromLocalDbBymId(fid) != null);
            if (dataExist) {
                data.postValue(appDAO.getPlantationDetailsFromLocalDbBymId(fid));
            }
        });
        return data;
    }

    //
    public LiveData<List<Plantation>> getPlantationDetailsFromLocalDbByIdAndStatus(String fcode,String lStat) {
        final MutableLiveData<List<Plantation>> data = new MutableLiveData<>();
        executor.execute(() -> {
//            Log.d(TAG, "FId" + fid);
            boolean dataExist = (appDAO.getPlantationDetailsFromLocalDbByIdAndStatus(fcode,lStat) != null);
            if (dataExist) {
                data.postValue(appDAO.getPlantationDetailsFromLocalDbByIdAndStatus(fcode,lStat));
            }
        });
        return data;
    }

    //Farmer Main
    public LiveData<List<FarmersTable>> getFarmerDetailslistFromLocalDBNotSync() {
        final MutableLiveData<List<FarmersTable>> data = new MutableLiveData<>();
        executor.execute(() -> {
            boolean dataExist = (appDAO.getFarmerDetailsListFromLocalDBNotSync("0") != null);
            if (dataExist) {
                data.postValue(appDAO.getFarmerDetailsListFromLocalDBNotSync("0"));
            }
        });
        return data;
    }

    //Labour Survey Main
    public LiveData<List<PlantationLabourSurvey>> getPlantationLabourSurveyDetailslistFromLocalDBNotSync() {
        final MutableLiveData<List<PlantationLabourSurvey>> data = new MutableLiveData<>();
        executor.execute(() -> {
            boolean dataExist = (appDAO.getPlantationLabourSurveyDetailsListFromLocalDBNotSync("0") != null);
            if (dataExist) {
                data.postValue(appDAO.getPlantationLabourSurveyDetailsListFromLocalDBNotSync("0"));
            }
        });
        return data;
    }


    //Farmer Main
    public LiveData<List<Plantation>> getPlantaionDetailslistFromLocalDBNotSync() {
        final MutableLiveData<List<Plantation>> data = new MutableLiveData<>();
        executor.execute(() -> {
            boolean dataExist = (appDAO.getPlantationDetailsListFromLocalDBNotSync("0") != null);
            if (dataExist) {
                data.postValue(appDAO.getPlantationDetailsListFromLocalDBNotSync("0"));
            }
        });
        return data;
    }

    //Farmer table sync
    public LiveData<String> syncFarmerDetailsDataToServer(FarmersTable farmerTable) {
        final MutableLiveData<String> data = new MutableLiveData<>();
        AppWebService.changeApiBaseUrl(RAW_DATA_URL);
        executor.execute(() -> {
            final SyncPersonalLandAllDetailsRequestDTO syncPersonalLandAllDetailsRequestDTO = new SyncPersonalLandAllDetailsRequestDTO();
            SyncPersonalLandAllDetailsRequestDTO.Farmer spNameFarmerPersonalDetailsClass = new SyncPersonalLandAllDetailsRequestDTO.Farmer();


            if (!TextUtils.isEmpty(farmerTable.getFarmerCode())) {
                spNameFarmerPersonalDetailsClass.setFarmerCode(farmerTable.getFarmerCode());
            } else {
                spNameFarmerPersonalDetailsClass.setFarmerCode("");
            }

            if (!TextUtils.isEmpty(farmerTable.getFirstName())) {
                spNameFarmerPersonalDetailsClass.setFirstName(farmerTable.getFirstName());
            } else {
                spNameFarmerPersonalDetailsClass.setFirstName("");
            }

            if (!TextUtils.isEmpty(farmerTable.getLastName())) {
                spNameFarmerPersonalDetailsClass.setLastName(farmerTable.getLastName());
            } else {
                spNameFarmerPersonalDetailsClass.setLastName("");
            }

            if (!TextUtils.isEmpty(farmerTable.getFatherName())) {
                spNameFarmerPersonalDetailsClass.setFatherName(farmerTable.getFatherName());
            } else {
                spNameFarmerPersonalDetailsClass.setFatherName("");
            }

            if (!TextUtils.isEmpty(farmerTable.getGender())) {
                spNameFarmerPersonalDetailsClass.setGender(farmerTable.getGender());
            } else {
                spNameFarmerPersonalDetailsClass.setGender("");
            }

            if (!TextUtils.isEmpty(farmerTable.getAge())) {
                spNameFarmerPersonalDetailsClass.setAge(farmerTable.getAge());
            } else {
                spNameFarmerPersonalDetailsClass.setAge("");
            }

            if (!TextUtils.isEmpty(farmerTable.getPrimaryContactNo())) {
                spNameFarmerPersonalDetailsClass.setPrimaryContactNo(farmerTable.getPrimaryContactNo());
            } else {
                spNameFarmerPersonalDetailsClass.setPrimaryContactNo("");
            }

            if (!TextUtils.isEmpty(farmerTable.getAddress())) {
                spNameFarmerPersonalDetailsClass.setAddress(farmerTable.getAddress());
            } else {
                spNameFarmerPersonalDetailsClass.setAddress("");
            }

            if (!TextUtils.isEmpty(farmerTable.getVillageId())) {
                spNameFarmerPersonalDetailsClass.setVillageId(farmerTable.getVillageId());
            } else {
                spNameFarmerPersonalDetailsClass.setVillageId("");
            }

            if (!TextUtils.isEmpty(farmerTable.getNationalIdentityCode())) {
                spNameFarmerPersonalDetailsClass.setNationalIdentityCode(farmerTable.getNationalIdentityCode());
            } else {
                spNameFarmerPersonalDetailsClass.setNationalIdentityCode("");
            }

            if (!TextUtils.isEmpty(farmerTable.getNationalIdentityCodeDocument())) {
                spNameFarmerPersonalDetailsClass.setNationalIdentityCodeDocument(farmerTable.getNationalIdentityCodeDocument());
            } else {
                spNameFarmerPersonalDetailsClass.setNationalIdentityCodeDocument("");
            }

            if (!TextUtils.isEmpty(farmerTable.getNoOfPlots())) {
                spNameFarmerPersonalDetailsClass.setNoOfPlots(farmerTable.getNoOfPlots());
            } else {
                spNameFarmerPersonalDetailsClass.setNoOfPlots("");
            }

            if (!TextUtils.isEmpty(farmerTable.getImage())) {
                spNameFarmerPersonalDetailsClass.setImage(farmerTable.getImage());
            } else {
                spNameFarmerPersonalDetailsClass.setImage("");
            }

            if (!TextUtils.isEmpty(farmerTable.getIsActive())) {
                spNameFarmerPersonalDetailsClass.setIsActive(farmerTable.getIsActive());
            } else {
                spNameFarmerPersonalDetailsClass.setIsActive("");
            }


            if (!TextUtils.isEmpty(farmerTable.getCreatedByUserId())) {
                spNameFarmerPersonalDetailsClass.setCreatedByUserId(farmerTable.getCreatedByUserId());
            } else {
                spNameFarmerPersonalDetailsClass.setCreatedByUserId("");
            }

            if (!TextUtils.isEmpty(farmerTable.getUpdatedByUserId())) {
                spNameFarmerPersonalDetailsClass.setUpdatedByUserId(farmerTable.getUpdatedByUserId());
            } else {
                spNameFarmerPersonalDetailsClass.setUpdatedByUserId("");
            }

            if (!TextUtils.isEmpty(farmerTable.getUpdatedDate())) {
                spNameFarmerPersonalDetailsClass.setUpdatedDate(farmerTable.getUpdatedDate());
            } else {
                spNameFarmerPersonalDetailsClass.setUpdatedDate("");
            }

            if (!TextUtils.isEmpty(farmerTable.getCreatedDate())) {
                spNameFarmerPersonalDetailsClass.setCreatedDate(farmerTable.getCreatedDate());
            } else {
                spNameFarmerPersonalDetailsClass.setCreatedDate("");
            }



            ArrayList<SyncPersonalLandAllDetailsRequestDTO.Farmer> spNameFarmerPersonalDetailsClasses = new ArrayList<SyncPersonalLandAllDetailsRequestDTO.Farmer>();
            spNameFarmerPersonalDetailsClasses.add(spNameFarmerPersonalDetailsClass);
            syncPersonalLandAllDetailsRequestDTO.setFarmer(spNameFarmerPersonalDetailsClasses);


            AppWebService.createService(AppAPI.class).syncFarmerDetailsDataToServer(syncPersonalLandAllDetailsRequestDTO,

                            appHelper.getSharedPrefObj().getString(accessToken, "")).
                    enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            executor.execute(() -> {
                                try {
                                    String strResponse = response.body().string();
                                    Log.d(TAG, "onResponse: AppData " + response);
                                    JSONObject json_object = new JSONObject(strResponse);
                                    String message = "", status = "";
                                    Log.e(TAG, "onResponse: data json" + json_object);
                                    message = json_object.getString("messgae");
                                    status = String.valueOf(json_object.getInt("status"));
                                    Log.d(TAG, "status " + status);
                                    if (status.equals("201")) {
                                        FarmersTable businessReviewSurveyTableFromDB = appDAO.getTopFarmerDetailListTableTableData(farmerTable.getFarmerCode());
                                        if (businessReviewSurveyTableFromDB != null) {
                                            businessReviewSurveyTableFromDB.setFarmerId(businessReviewSurveyTableFromDB.getFarmerId());
                                            farmerTable.setSync(true);
                                            farmerTable.setServerSync("1");
                                            appDAO.insertFarmerDetailListTableLocalDB(farmerTable);
                                            data.postValue(SUCCESS_RESPONSE_MESSAGE);
                                        }

                                    } else if (status.equals("0")) {
                                        data.postValue(message);
                                    }

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    data.postValue(FAILURE_RESPONSE_MESSAGE);
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


    //Plantation sync
    public LiveData<String> syncPlantationDetailsDataToServer(Plantation plantation) {

        final MutableLiveData<String> data = new MutableLiveData<>();
        AppWebService.changeApiBaseUrl(RAW_DATA_URL);
        executor.execute(() -> {
            final SyncPersonalLandAllDetailsRequestDTO syncPersonalLandAllDetailsRequestDTO = new SyncPersonalLandAllDetailsRequestDTO();
            SyncPersonalLandAllDetailsRequestDTO.Plantation spNamePlantationDetailsClass = new SyncPersonalLandAllDetailsRequestDTO.Plantation();

            if (!TextUtils.isEmpty(plantation.getPlotCode())) {
                spNamePlantationDetailsClass.setPlotCode(plantation.getPlotCode());
            } else {
                spNamePlantationDetailsClass.setPlotCode("");
            }

            if (!TextUtils.isEmpty(plantation.getFarmerCode())) {
                spNamePlantationDetailsClass.setFarmerCode(plantation.getFarmerCode());
            } else {
                spNamePlantationDetailsClass.setFarmerCode("");
            }

            if (!TextUtils.isEmpty(plantation.getTypeOfOwnership())) {
                spNamePlantationDetailsClass.setTypeOfOwnership(plantation.getTypeOfOwnership());
            } else {
                spNamePlantationDetailsClass.setTypeOfOwnership("");
            }

            if (!TextUtils.isEmpty(String.valueOf(plantation.getAreaInHectors()))) {
                spNamePlantationDetailsClass.setAreaInHectors(plantation.getAreaInHectors());
            } else {
                spNamePlantationDetailsClass.setAreaInHectors(0.0);
            }

            if (!TextUtils.isEmpty(String.valueOf(plantation.getGeoboundariesArea()))) {
//                spNamePlantationDetailsClass.setGeoboundariesArea(String.valueOf(plantation.getGeoboundariesArea()));
                String decimalForm = String.format("%.5f", plantation.getGeoboundariesArea());
                decimalForm = decimalForm.replace(",", ".");
                spNamePlantationDetailsClass.setGeoboundariesArea(decimalForm);
            } else {
                spNamePlantationDetailsClass.setGeoboundariesArea("0.0");
            }

            if (!TextUtils.isEmpty(String.valueOf(plantation.getLatitude()))) {
                spNamePlantationDetailsClass.setLatitude(plantation.getLatitude());
            } else {
                spNamePlantationDetailsClass.setLatitude(0.0);
            }

            if (!TextUtils.isEmpty(String.valueOf(plantation.getLongitude()))) {
                spNamePlantationDetailsClass.setLongitude(plantation.getLongitude());
            } else {
                spNamePlantationDetailsClass.setLongitude(0.0);
            }

            if (!TextUtils.isEmpty(plantation.getAddress())) {
                spNamePlantationDetailsClass.setAddress(plantation.getAddress());
            } else {
                spNamePlantationDetailsClass.setAddress("");
            }

            if (!TextUtils.isEmpty(plantation.getVillageId())) {
                spNamePlantationDetailsClass.setVillageId(plantation.getVillageId());
            } else {
                spNamePlantationDetailsClass.setVillageId("");
            }

            if (!TextUtils.isEmpty(plantation.getLabourStatus())) {
                if (plantation.getLabourStatus().trim().equals("false")){
                    spNamePlantationDetailsClass.setLabourStatus(0);
                } else if (plantation.getLabourStatus().trim().equals("true")){
                    spNamePlantationDetailsClass.setLabourStatus(1);
                }
            } else {
                spNamePlantationDetailsClass.setLabourStatus(0);
            }

            if (!TextUtils.isEmpty(plantation.getIsActive())) {
                spNamePlantationDetailsClass.setIsActive(plantation.getIsActive());
            } else {
                spNamePlantationDetailsClass.setIsActive("");
            }


            if (!TextUtils.isEmpty(plantation.getCreatedByUserId())) {
                spNamePlantationDetailsClass.setCreatedByUserId(plantation.getCreatedByUserId());
            } else {
                spNamePlantationDetailsClass.setCreatedByUserId("");
            }

            if (!TextUtils.isEmpty(plantation.getUpdatedByUserId())) {
                spNamePlantationDetailsClass.setUpdatedByUserId(plantation.getUpdatedByUserId());
            } else {
                spNamePlantationDetailsClass.setUpdatedByUserId("");
            }

            if (!TextUtils.isEmpty(plantation.getUpdatedDate())) {
                spNamePlantationDetailsClass.setUpdatedDate(plantation.getUpdatedDate());
            } else {
                spNamePlantationDetailsClass.setUpdatedDate("");
            }

            if (!TextUtils.isEmpty(plantation.getCreatedDate())) {
                spNamePlantationDetailsClass.setCreatedDate(plantation.getCreatedDate());
            } else {
                spNamePlantationDetailsClass.setCreatedDate("");
            }



            ArrayList<SyncPersonalLandAllDetailsRequestDTO.Plantation> spNameFarmerPersonalDetailsClasses = new ArrayList<SyncPersonalLandAllDetailsRequestDTO.Plantation>();
            spNameFarmerPersonalDetailsClasses.add(spNamePlantationDetailsClass);
            syncPersonalLandAllDetailsRequestDTO.setPlantation(spNameFarmerPersonalDetailsClasses);


            AppWebService.createService(AppAPI.class).syncFarmerDetailsDataToServer(syncPersonalLandAllDetailsRequestDTO,

                            appHelper.getSharedPrefObj().getString(accessToken, "")).
                    enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            executor.execute(() -> {
                                try {
                                    String strResponse = response.body().string();
                                    Log.d(TAG, "onResponse: AppData " + response);
                                    JSONObject json_object = new JSONObject(strResponse);
                                    String message = "", status = "";
                                    Log.e(TAG, "onResponse: data json" + json_object);
                                    message = json_object.getString("messgae");
                                    status = String.valueOf(json_object.getInt("status"));
                                    Log.d(TAG, "status " + status);
                                    if (status.equals("201")) {
                                        Plantation businessReviewSurveyTableFromDB = appDAO.getTopPlantationListTableTableData(plantation.getPlotCode());
                                        if (businessReviewSurveyTableFromDB != null) {
                                            businessReviewSurveyTableFromDB.setPlotId(businessReviewSurveyTableFromDB.getPlotId());
                                            plantation.setSync(true);
                                            plantation.setServerSync("1");
                                            appDAO.insertPlantationDetailListTableLocalDB(plantation);
                                            data.postValue(SUCCESS_RESPONSE_MESSAGE);
                                        }

                                    } else if (status.equals("0")) {
                                        data.postValue(message);
                                    }

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    data.postValue(FAILURE_RESPONSE_MESSAGE);
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

    public LiveData<String> syncSurveyDetailsDataToServer(PlantationLabourSurvey plantationLabourSurvey) {
        final MutableLiveData<String> data = new MutableLiveData<>();
        AppWebService.changeApiBaseUrl(RAW_DATA_URL);
        executor.execute(() -> {
            final SyncPersonalLandAllDetailsRequestDTO syncPersonalLandAllDetailsRequestDTO = new SyncPersonalLandAllDetailsRequestDTO();
            SyncPersonalLandAllDetailsRequestDTO.PlantationLabourSurvey spNameFarmerPersonalDetailsClass = new SyncPersonalLandAllDetailsRequestDTO.PlantationLabourSurvey();


            if (!TextUtils.isEmpty(plantationLabourSurvey.getFarmerCode())) {
                spNameFarmerPersonalDetailsClass.setFarmerCode(plantationLabourSurvey.getFarmerCode());
            } else {
                spNameFarmerPersonalDetailsClass.setFarmerCode("");
            }

//            if (!TextUtils.isEmpty(String.valueOf(plantationLabourSurvey.getPlantationId()))) {
//                spNameFarmerPersonalDetailsClass.setPlantationId(plantationLabourSurvey.getPlantationId());
//            } else {
//                spNameFarmerPersonalDetailsClass.setPlantationId(0);
//            }

            if (!TextUtils.isEmpty(plantationLabourSurvey.getPlantationCode())) {
                spNameFarmerPersonalDetailsClass.setPlantationCode(plantationLabourSurvey.getPlantationCode());
            } else {
                spNameFarmerPersonalDetailsClass.setPlantationCode("");
            }

            if (!TextUtils.isEmpty(String.valueOf(plantationLabourSurvey.getNoOfFieldWorkers()))) {
                spNameFarmerPersonalDetailsClass.setNoOfFieldWorkers(plantationLabourSurvey.getNoOfFieldWorkers());
            } else {
                spNameFarmerPersonalDetailsClass.setNoOfFieldWorkers(0);
            }

            if (!TextUtils.isEmpty(String.valueOf(plantationLabourSurvey.getNoOfMaleWorkers()))) {
                spNameFarmerPersonalDetailsClass.setNoOfMaleWorkers(plantationLabourSurvey.getNoOfMaleWorkers());
            } else {
                spNameFarmerPersonalDetailsClass.setNoOfMaleWorkers(0);
            }

            if (!TextUtils.isEmpty(String.valueOf(plantationLabourSurvey.getNoOfFemaleWorkers()))) {
                spNameFarmerPersonalDetailsClass.setNoOfFemaleWorkers(plantationLabourSurvey.getNoOfFemaleWorkers());
            } else {
                spNameFarmerPersonalDetailsClass.setNoOfFemaleWorkers(0);
            }

            if (!TextUtils.isEmpty(String.valueOf(plantationLabourSurvey.getNoOfResident()))) {
                spNameFarmerPersonalDetailsClass.setNoOfResident(plantationLabourSurvey.getNoOfResident());
            } else {
                spNameFarmerPersonalDetailsClass.setNoOfResident(0);
            }

            if (!TextUtils.isEmpty(String.valueOf(plantationLabourSurvey.getNoOfMigrant()))) {
                spNameFarmerPersonalDetailsClass.setNoOfMigrant(plantationLabourSurvey.getNoOfMigrant());
            } else {
                spNameFarmerPersonalDetailsClass.setNoOfMigrant(0);
            }

            if (!TextUtils.isEmpty(String.valueOf(plantationLabourSurvey.getOccupationOfChildren()))) {
                spNameFarmerPersonalDetailsClass.setOccupationOfChildren(plantationLabourSurvey.getOccupationOfChildren());
            } else {
                spNameFarmerPersonalDetailsClass.setOccupationOfChildren("");
            }

            if (!TextUtils.isEmpty(String.valueOf(plantationLabourSurvey.getIsActive()))) {
                spNameFarmerPersonalDetailsClass.setIsActive(plantationLabourSurvey.getIsActive());
            } else {
                spNameFarmerPersonalDetailsClass.setIsActive("");
            }


            if (!TextUtils.isEmpty(plantationLabourSurvey.getCreatedByUserId())) {
                spNameFarmerPersonalDetailsClass.setCreatedByUserId(plantationLabourSurvey.getCreatedByUserId());
            } else {
                spNameFarmerPersonalDetailsClass.setCreatedByUserId("");
            }

            if (!TextUtils.isEmpty(plantationLabourSurvey.getUpdatedByUserId())) {
                spNameFarmerPersonalDetailsClass.setUpdatedByUserId(plantationLabourSurvey.getUpdatedByUserId());
            } else {
                spNameFarmerPersonalDetailsClass.setUpdatedByUserId("");
            }

            if (!TextUtils.isEmpty(plantationLabourSurvey.getUpdatedDate())) {
                spNameFarmerPersonalDetailsClass.setUpdatedDate(plantationLabourSurvey.getUpdatedDate());
            } else {
                spNameFarmerPersonalDetailsClass.setUpdatedDate("");
            }

            if (!TextUtils.isEmpty(plantationLabourSurvey.getCreatedDate())) {
                spNameFarmerPersonalDetailsClass.setCreatedDate(plantationLabourSurvey.getCreatedDate());
            } else {
                spNameFarmerPersonalDetailsClass.setCreatedDate("");
            }



            ArrayList<SyncPersonalLandAllDetailsRequestDTO.PlantationLabourSurvey> spNameFarmerPersonalDetailsClasses = new ArrayList<>();
            spNameFarmerPersonalDetailsClasses.add(spNameFarmerPersonalDetailsClass);
            syncPersonalLandAllDetailsRequestDTO.setPlantationLabourSurvey(spNameFarmerPersonalDetailsClasses);


            AppWebService.createService(AppAPI.class).syncFarmerDetailsDataToServer(syncPersonalLandAllDetailsRequestDTO,

                            appHelper.getSharedPrefObj().getString(accessToken, "")).
                    enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            executor.execute(() -> {
                                try {
                                    String strResponse = response.body().string();
                                    Log.d(TAG, "onResponse: AppData " + response);
                                    JSONObject json_object = new JSONObject(strResponse);
                                    String message = "", status = "";
                                    Log.e(TAG, "onResponse: data json" + json_object);
                                    message = json_object.getString("messgae");
                                    status = String.valueOf(json_object.getInt("status"));
                                    Log.d(TAG, "status " + status);
                                    if (status.equals("201")) {
                                        PlantationLabourSurvey businessReviewSurveyTableFromDB = appDAO.getTopLabouSurveyDetailListTableTableData(plantationLabourSurvey.getFarmerCode());
                                        if (businessReviewSurveyTableFromDB != null) {
                                            businessReviewSurveyTableFromDB.setId(businessReviewSurveyTableFromDB.getId());
                                            plantationLabourSurvey.setSync(true);
                                            plantationLabourSurvey.setServerSync("1");
                                            appDAO.insertPlantationLabourSurveyListTableLocalDB(plantationLabourSurvey);
                                            data.postValue(SUCCESS_RESPONSE_MESSAGE);
                                        }

                                    } else if (status.equals("0")) {
                                        data.postValue(message);
                                    }

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    data.postValue(FAILURE_RESPONSE_MESSAGE);
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


    public LiveData<List<PlantationDocuments>> getDocIdentiFicationDeatilsTableFromLocalDb() {
        final MutableLiveData<List<PlantationDocuments>> data = new MutableLiveData<>();
        executor.execute(() -> {
            boolean dataExist = (appDAO.getDocIdentiFicationDeatilsTableFromLocalDb("0") != null);
            if (dataExist) {
                data.postValue(appDAO.getDocIdentiFicationDeatilsTableFromLocalDb("0"));
            }
        });
        return data;
    }


    //Doc by fid
    public LiveData<List<PlantationDocuments>> getDocIdentiFicationDeatilsTableFromLocalDbByFidWDoc(String fid,String dty) {
        final MutableLiveData<List<PlantationDocuments>> data = new MutableLiveData<>();
        executor.execute(() -> {
            boolean dataExist = (appDAO.getDocIdentiFicationDeatilsTableFromLocalDbByFidWDoc(fid,dty) != null);
            if (dataExist) {
                data.postValue(appDAO.getDocIdentiFicationDeatilsTableFromLocalDbByFidWDoc(fid,dty));
            }
        });
        return data;
    }


//    Todo:Sync Plantation Doc
    public LiveData<String> syncDocIdentificationDetailsDataToServer(PlantationDocuments docIdentiFicationDeatilsTable) {
        final MutableLiveData<String> data = new MutableLiveData<>();
        AppWebService.changeApiBaseUrl(RAW_DATA_URL);
        executor.execute(() -> {
            final SyncPersonalLandAllDetailsRequestDTO syncPersonalLandAllDetailsRequestDTO = new SyncPersonalLandAllDetailsRequestDTO();
            SyncPersonalLandAllDetailsRequestDTO.PlantationDocuments spNameLandDetailsClass = new SyncPersonalLandAllDetailsRequestDTO.PlantationDocuments();

            if (!TextUtils.isEmpty(docIdentiFicationDeatilsTable.getDocUrlValue())) {
                spNameLandDetailsClass.setDocUrlValue(docIdentiFicationDeatilsTable.getDocUrlValue());
            } else {
                spNameLandDetailsClass.setDocUrlValue("");
            }

            if (!TextUtils.isEmpty(docIdentiFicationDeatilsTable.getPlotCode())) {
                spNameLandDetailsClass.setPlotCode(docIdentiFicationDeatilsTable.getPlotCode());
            } else {
                spNameLandDetailsClass.setPlotCode("");
            }

            if (!TextUtils.isEmpty(docIdentiFicationDeatilsTable.getDocType())) {
                spNameLandDetailsClass.setDocType(String.valueOf(docIdentiFicationDeatilsTable.getDocType()));
            } else {
                spNameLandDetailsClass.setDocType("");
            }

            if (!TextUtils.isEmpty(docIdentiFicationDeatilsTable.getDocURL())) {
                spNameLandDetailsClass.setDocURL(docIdentiFicationDeatilsTable.getDocURL());
            } else {
                spNameLandDetailsClass.setDocURL("");
            }

//            if (!TextUtils.isEmpty(docIdentiFicationDeatilsTable.getDocLocal())) {
//                spNameLandDetailsClass.setDocLocal(docIdentiFicationDeatilsTable.getDocLocal());
//            } else {
//                spNameLandDetailsClass.setDocLocal("");
//            }

//            if (!TextUtils.isEmpty(docIdentiFicationDeatilsTable.getDocExtension())) {
//                spNameLandDetailsClass.setDocExtension(docIdentiFicationDeatilsTable.getDocExtension());
//            } else {
//                spNameLandDetailsClass.setDocExtension("");
//            }


//            if (!TextUtils.isEmpty(docIdentiFicationDeatilsTable.getDocOldNum())) {
//                spNameLandDetailsClass.setDocOldNum(docIdentiFicationDeatilsTable.getDocOldNum());
//            } else {
//                spNameLandDetailsClass.setDocOldNum("");
//            }

//            String dateTime = appHelper.getCurrentDateTime(AppConstant.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
//            spNameLandDetailsClass.setCreatedDate(dateTime);
//            spNameLandDetailsClass.setUpdatedDate(dateTime);

            if (!TextUtils.isEmpty(docIdentiFicationDeatilsTable.getIsActive())) {
                spNameLandDetailsClass.setIsActive(docIdentiFicationDeatilsTable.getIsActive());
            } else {
                spNameLandDetailsClass.setIsActive("");
            }


            if (!TextUtils.isEmpty(docIdentiFicationDeatilsTable.getCreatedByUserId())) {
                spNameLandDetailsClass.setCreatedByUserId(docIdentiFicationDeatilsTable.getCreatedByUserId());
            } else {
                spNameLandDetailsClass.setCreatedByUserId("");
            }

            if (!TextUtils.isEmpty(docIdentiFicationDeatilsTable.getUpdatedByUserId())) {
                spNameLandDetailsClass.setUpdatedByUserId(docIdentiFicationDeatilsTable.getUpdatedByUserId());
            } else {
                spNameLandDetailsClass.setUpdatedByUserId("");
            }

            if (!TextUtils.isEmpty(docIdentiFicationDeatilsTable.getUpdatedDate())) {
                spNameLandDetailsClass.setUpdatedDate(docIdentiFicationDeatilsTable.getUpdatedDate());
            } else {
                spNameLandDetailsClass.setUpdatedDate("");
            }

            if (!TextUtils.isEmpty(docIdentiFicationDeatilsTable.getCreatedDate())) {
                spNameLandDetailsClass.setCreatedDate(docIdentiFicationDeatilsTable.getCreatedDate());
            } else {
                spNameLandDetailsClass.setCreatedDate("");
            }

            if (!TextUtils.isEmpty(docIdentiFicationDeatilsTable.getFarmerCode())){
                spNameLandDetailsClass.setFarmerCode(docIdentiFicationDeatilsTable.getFarmerCode());
            } else {
                spNameLandDetailsClass.setFarmerCode("");
            }

//            spNameLandDetailsClass.setVoluntaryId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
//            spNameLandDetailsClass.setAgentId(appHelper.getSharedPrefObj().getString(AgentId, ""));


            ArrayList<SyncPersonalLandAllDetailsRequestDTO.PlantationDocuments> spNameLandDetailsClasses = new ArrayList<SyncPersonalLandAllDetailsRequestDTO.PlantationDocuments>();
            spNameLandDetailsClasses.add(spNameLandDetailsClass);
            syncPersonalLandAllDetailsRequestDTO.setPlantationDocuments(spNameLandDetailsClasses);
            AppWebService.createService(AppAPI.class).syncFarmerDetailsDataToServer(syncPersonalLandAllDetailsRequestDTO, appHelper.getSharedPrefObj().getString(accessToken, "")).
                    enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            executor.execute(() -> {
//
                                try {

                                    String strResponse = response.body().string();
                                    Log.d(TAG, "onResponse: AppData " + response);
                                    JSONObject json_object = new JSONObject(strResponse);
                                    String message = "";
                                    int status = 0;
                                    Log.e(TAG, "onResponse: data json" + json_object);
                                    message = json_object.getString("messgae");
                                    status = json_object.getInt("status");
                                    Log.d(TAG, "status " + String.valueOf(status));
                                    if (status==201) {
                                        PlantationDocuments businessReviewSurveyTableFromDB = appDAO.getTopDocListTableDataFromLocal(docIdentiFicationDeatilsTable.getFarmerCode());
                                        if (businessReviewSurveyTableFromDB != null) {
                                            businessReviewSurveyTableFromDB.setID(businessReviewSurveyTableFromDB.getID());
                                            docIdentiFicationDeatilsTable.setSync(true);
                                            docIdentiFicationDeatilsTable.setServerSync("1");
                                            appDAO.insertDocDetailListTable(docIdentiFicationDeatilsTable);
                                        }
                                        data.postValue(SUCCESS_RESPONSE_MESSAGE);
                                    } else if (status==500) {
                                        data.postValue(message);
                                    }

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    data.postValue(FAILURE_RESPONSE_MESSAGE);
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


    //Not Sync Geo PlantationGeoBoundaries
    public LiveData<List<PlantationGeoBoundaries>> getGeoBoundariesTableListFromLocalDB() {
        final MutableLiveData<List<PlantationGeoBoundaries>> data = new MutableLiveData<>();
        executor.execute(() -> {
            boolean dataExist = (appDAO.getGeoBoundariesTableTableFromLocalDbList("0") != null);
            if (dataExist) {
                data.postValue(appDAO.getGeoBoundariesTableTableFromLocalDbList("0"));
            }
        });
        return data;
    }

    //PlantationGeoBoundaries
    public LiveData<List<PlantationGeoBoundaries>> getGeoBoundariesTableListPlot(String plot) {
        final MutableLiveData<List<PlantationGeoBoundaries>> data = new MutableLiveData<>();
        executor.execute(() -> {
            boolean dataExist = (appDAO.getGeoBoundariesTableTablePlot(plot) != null);
            if (dataExist) {
                data.postValue(appDAO.getGeoBoundariesTableTablePlot(plot));
            }
        });
        return data;
    }

    //Todo : Sync Plantation Geo Main
    public LiveData<String> syncGeoBoundariesDetailsDataToServer(PlantationGeoBoundaries geoBoundariesTable,int index) {
        final MutableLiveData<String> data = new MutableLiveData<>();
        AppWebService.changeApiBaseUrl(RAW_DATA_URL);
        executor.execute(() -> {
            final SyncPersonalLandAllDetailsRequestDTO syncPersonalLandAllDetailsRequestDTO = new SyncPersonalLandAllDetailsRequestDTO();
            SyncPersonalLandAllDetailsRequestDTO.PlantationGeoBoundaries spNameGPSDetailsClass = new SyncPersonalLandAllDetailsRequestDTO.PlantationGeoBoundaries();

            if (!TextUtils.isEmpty(geoBoundariesTable.getPlotCode())) {
                spNameGPSDetailsClass.setPlotCode(geoBoundariesTable.getPlotCode());
            } else {
                spNameGPSDetailsClass.setPlotCode("");
            }

            if (!TextUtils.isEmpty(geoBoundariesTable.getFarmerCode())) {
                spNameGPSDetailsClass.setFarmerCode(geoBoundariesTable.getFarmerCode());
            } else {
                spNameGPSDetailsClass.setFarmerCode("");
            }


            if (!TextUtils.isEmpty(String.valueOf(geoBoundariesTable.getLatitude())))
            {
                spNameGPSDetailsClass.setLatitude(geoBoundariesTable.getLatitude());
            } else {
                spNameGPSDetailsClass.setLatitude(0.0);
            }

            if (!TextUtils.isEmpty(String.valueOf(geoBoundariesTable.getLongitude()))) {
                spNameGPSDetailsClass.setLongitude(geoBoundariesTable.getLongitude());
            } else {
                spNameGPSDetailsClass.setLongitude(0.0);
            }


            if (!TextUtils.isEmpty(String.valueOf(geoBoundariesTable.getPlotCount()))) {
                spNameGPSDetailsClass.setPlotCount(geoBoundariesTable.getPlotCount());
            } else {
                spNameGPSDetailsClass.setPlotCount(1);
            }

            if (!TextUtils.isEmpty(String.valueOf(index))) {
                spNameGPSDetailsClass.setSeqNo(index);
            } else {
                spNameGPSDetailsClass.setSeqNo(0);
            }


            if (!TextUtils.isEmpty(geoBoundariesTable.getIsActive())) {
                spNameGPSDetailsClass.setIsActive(geoBoundariesTable.getIsActive());
            } else {
                spNameGPSDetailsClass.setIsActive("");
            }


            if (!TextUtils.isEmpty(geoBoundariesTable.getCreatedByUserId())) {
                spNameGPSDetailsClass.setCreatedByUserId(geoBoundariesTable.getCreatedByUserId());
            } else {
                spNameGPSDetailsClass.setCreatedByUserId("");
            }

            if (!TextUtils.isEmpty(geoBoundariesTable.getUpdatedByUserId())) {
                spNameGPSDetailsClass.setUpdatedByUserId(geoBoundariesTable.getUpdatedByUserId());
            } else {
                spNameGPSDetailsClass.setUpdatedByUserId("");
            }

            if (!TextUtils.isEmpty(geoBoundariesTable.getUpdatedDate())) {
                spNameGPSDetailsClass.setUpdatedDate(geoBoundariesTable.getUpdatedDate());
            } else {
                spNameGPSDetailsClass.setUpdatedDate("");
            }

            if (!TextUtils.isEmpty(geoBoundariesTable.getCreatedDate())) {
                spNameGPSDetailsClass.setCreatedDate(geoBoundariesTable.getCreatedDate());
            } else {
                spNameGPSDetailsClass.setCreatedDate("");
            }


            ArrayList<SyncPersonalLandAllDetailsRequestDTO.PlantationGeoBoundaries> spNameGPSDetailsClasses = new ArrayList<SyncPersonalLandAllDetailsRequestDTO.PlantationGeoBoundaries>();
            spNameGPSDetailsClasses.add(spNameGPSDetailsClass);
            syncPersonalLandAllDetailsRequestDTO.setPlantationGeoBoundaries(spNameGPSDetailsClasses);
            AppWebService.createService(AppAPI.class).syncFarmerDetailsDataToServer(syncPersonalLandAllDetailsRequestDTO, appHelper.getSharedPrefObj().getString(accessToken, "")).
                    enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            executor.execute(() -> {
                                try {

                                    String strResponse = response.body().string();
                                    Log.d(TAG, "onResponse: AppData " + response);
                                    JSONObject json_object = new JSONObject(strResponse);
                                    String message = "", status = "";
                                    Log.e(TAG, "onResponse: data json" + json_object);
                                    message = json_object.getString("messgae");
                                    status = json_object.getString("status");
                                    Log.d(TAG, "status " + status);
                                    if (status.equals("201")) {
                                        PlantationGeoBoundaries businessReviewSurveyTableFromDB = appDAO.getGeoBoundariesTableData(geoBoundariesTable.getPlotCode());
                                        if (businessReviewSurveyTableFromDB != null) {
                                            businessReviewSurveyTableFromDB.setID(businessReviewSurveyTableFromDB.getID());
                                            geoBoundariesTable.setSync(true);
                                            geoBoundariesTable.setServerSync("1");
                                            appDAO.insertGeoBoundariesTable(geoBoundariesTable);
                                        }
                                        data.postValue(SUCCESS_RESPONSE_MESSAGE);
                                    } else if (status.equals("0")) {
                                        data.postValue(FAILURE_RESPONSE_MESSAGE);
                                    }

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    data.postValue(FAILURE_RESPONSE_MESSAGE);
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

    public void updateFarmerImage(String docUrl,String code) {
        executor.execute(() -> {
//            appDAO.updateFarmerImage(docUrl,code);
        });
    }

    //Get Farmer
    public LiveData<List<FarmersTable>> getFarmerDetailslistFromLocalDB() {
        final MutableLiveData<List<FarmersTable>> data = new MutableLiveData<>();
        executor.execute(() -> {
            boolean dataExist = (appDAO.getFarmerDetailsListFromLocalDB() != null);
            if (dataExist) {
                data.postValue(appDAO.getFarmerDetailsListFromLocalDB());
            }
        });
        return data;
    }

    //Geo for cat desc
    public LiveData<List<PlantationGeoBoundaries>> getPlantationGeoFromLocalDb(String plotCode) {
        final MutableLiveData<List<PlantationGeoBoundaries>> data = new MutableLiveData<>();
        executor.execute(() -> {
            Log.d(TAG, "plotCode" + plotCode);
            boolean dataExist = (appDAO.getPlantGeoFromLocalDbBySubDist(plotCode) != null);
            if (dataExist) {
                data.postValue(appDAO.getPlantGeoFromLocalDbBySubDist(plotCode));
            }
        });
        return data;
    }

    //updatePlotDetailListTableSyncAndPlotArea1
    public LiveData<Plantation> updatePlotDetailListTableSyncAndPlotArea1(boolean mSync,String sSync,double area,String pid){
        final MutableLiveData<Plantation> data = new MutableLiveData<>();

        executor.execute(()->{
            appDAO.updatePlotDetailListTableSyncAndPlotArea1(mSync,sSync,area,pid);
//            data.postValue();
        });

        return data;
    }

    public LiveData<FarmersTable> updateVillageIDForFarmerTable(String strAdd,String strVillageId,String strFarmerCode){
        final MutableLiveData<FarmersTable> data = new MutableLiveData<>();

        executor.execute(()->{
            appDAO.updateVillageIdFarmerTable(strAdd,strVillageId,strFarmerCode);
//            data.postValue();
        });
        return data;
    }
    //updatePlotDetailListTableSyncAndPlotArea1
    public LiveData<Plantation> updatePlatDetailListTableForLabStatus(String serSync,boolean sync,String labStatus,String upDate,String upId,String pid){
        final MutableLiveData<Plantation> data = new MutableLiveData<>();

        executor.execute(()->{
            appDAO.updatePlatDetailListTableForLabStatus(serSync,sync,labStatus,upDate,upId,pid);
//            data.postValue();
        });

        return data;
    }

    //FarmerHouseholdParentSurvey
    public LiveData<FarmerHouseholdParentSurvey> insertFarmerHouseholdParentSurveyIntoLocalDB(FarmerHouseholdParentSurvey farmerHouseholdParentSurvey) {
        final MutableLiveData<FarmerHouseholdParentSurvey> data = new MutableLiveData<>();
        executor.execute(() -> {
            appDAO.insertFarmerHouseholdParentSurveyDetailListTable(farmerHouseholdParentSurvey);
//            data.postValue(appDAO.getTopDocListTableDataFromLocal(docIdentiFicationDeatilsTable.getFarmerCode()));
        });
        return data;
    }

    public LiveData<Integer> getNotFarmerHouseholdParentSurveyCountDataFromLocalDB() {
        return appDAO.getFarmerHouseholdParentSurveyNotSyncCountFromLocalDB("0");
    }

    //list
    public LiveData<List<FarmerHouseholdParentSurvey>> getFarmerHouseholdParentSurveyDetailsFromLocalDbById(String fid) {
        final MutableLiveData<List<FarmerHouseholdParentSurvey>> data = new MutableLiveData<>();
        executor.execute(() -> {
            Log.d(TAG, "FId" + fid);
            boolean dataExist = (appDAO.getFarmerHouseholdParentSurveyDetailsFromLocalDbById(fid) != null);
            if (dataExist) {
                data.postValue(appDAO.getFarmerHouseholdParentSurveyDetailsFromLocalDbById(fid));
            }
        });
        return data;
    }

    //Parent Survey Main
    public LiveData<List<FarmerHouseholdParentSurvey>> getFarmerHouseholdParentSurveyDetailslistFromLocalDBNotSync() {
        final MutableLiveData<List<FarmerHouseholdParentSurvey>> data = new MutableLiveData<>();
        executor.execute(() -> {
            boolean dataExist = (appDAO.getFarmerHouseholdParentSurveyDetailsListFromLocalDBNotSync("0") != null);
            if (dataExist) {
                data.postValue(appDAO.getFarmerHouseholdParentSurveyDetailsListFromLocalDBNotSync("0"));
            }
        });
        return data;
    }

    //Parent sync
    public LiveData<String> syncParentSurveyDetailsDataToServer(FarmerHouseholdParentSurvey farmerParentSurvey) {
        final MutableLiveData<String> data = new MutableLiveData<>();
        AppWebService.changeApiBaseUrl(RAW_DATA_URL);
        executor.execute(() -> {
            final SyncPersonalLandAllDetailsRequestDTO syncPersonalLandAllDetailsRequestDTO = new SyncPersonalLandAllDetailsRequestDTO();
            SyncPersonalLandAllDetailsRequestDTO.FarmerHouseholdParentSurvey spNameFarmerPersonalDetailsClass = new SyncPersonalLandAllDetailsRequestDTO.FarmerHouseholdParentSurvey();


            if (!TextUtils.isEmpty(farmerParentSurvey.getFarmerCode())) {
                spNameFarmerPersonalDetailsClass.setFarmerCode(farmerParentSurvey.getFarmerCode());
            } else {
                spNameFarmerPersonalDetailsClass.setFarmerCode("");
            }

            if (!TextUtils.isEmpty(farmerParentSurvey.getFarmerId())) {
                spNameFarmerPersonalDetailsClass.setFarmerId(farmerParentSurvey.getFarmerId());
            } else {
                spNameFarmerPersonalDetailsClass.setFarmerId("0");
            }

            if (!TextUtils.isEmpty(String.valueOf(farmerParentSurvey.getFamilyCount()))) {
                spNameFarmerPersonalDetailsClass.setFamilyCount(farmerParentSurvey.getFamilyCount());
            } else {
                spNameFarmerPersonalDetailsClass.setFamilyCount(0);
            }

            if (!TextUtils.isEmpty(farmerParentSurvey.getMaritalStatus())) {
                spNameFarmerPersonalDetailsClass.setMaritalStatus(farmerParentSurvey.getMaritalStatus());
            } else {
                spNameFarmerPersonalDetailsClass.setMaritalStatus("");
            }

            if (!TextUtils.isEmpty(farmerParentSurvey.getSpouseName())) {
                spNameFarmerPersonalDetailsClass.setSpouseName(farmerParentSurvey.getSpouseName());
            } else {
                spNameFarmerPersonalDetailsClass.setSpouseName("");
            }

            if (!TextUtils.isEmpty(String.valueOf(farmerParentSurvey.getAge()))) {
                spNameFarmerPersonalDetailsClass.setAge(farmerParentSurvey.getAge());
            } else {
                spNameFarmerPersonalDetailsClass.setAge(0);
            }

            if (!TextUtils.isEmpty(farmerParentSurvey.getGender())) {
                spNameFarmerPersonalDetailsClass.setGender(farmerParentSurvey.getGender());
            } else {
                spNameFarmerPersonalDetailsClass.setGender("");
            }

            if (!TextUtils.isEmpty(farmerParentSurvey.getOccupation())) {
                spNameFarmerPersonalDetailsClass.setOccupation(farmerParentSurvey.getOccupation());
            } else {
                spNameFarmerPersonalDetailsClass.setOccupation("");
            }

            if (!TextUtils.isEmpty(String.valueOf(farmerParentSurvey.getNoofChildren()))) {
                spNameFarmerPersonalDetailsClass.setNoofChildren(farmerParentSurvey.getNoofChildren());
            } else {
                spNameFarmerPersonalDetailsClass.setNoofChildren(0);
            }

            if (!TextUtils.isEmpty(String.valueOf(farmerParentSurvey.getIsActive()))) {
                spNameFarmerPersonalDetailsClass.setIsActive(farmerParentSurvey.getIsActive());
            } else {
                spNameFarmerPersonalDetailsClass.setIsActive("");
            }


            if (!TextUtils.isEmpty(farmerParentSurvey.getCreatedByUserId())) {
                spNameFarmerPersonalDetailsClass.setCreatedByUserId(farmerParentSurvey.getCreatedByUserId());
            } else {
                spNameFarmerPersonalDetailsClass.setCreatedByUserId("");
            }

            if (!TextUtils.isEmpty(farmerParentSurvey.getUpdatedByUserId())) {
                spNameFarmerPersonalDetailsClass.setUpdatedByUserId(farmerParentSurvey.getUpdatedByUserId());
            } else {
                spNameFarmerPersonalDetailsClass.setUpdatedByUserId("");
            }

            if (!TextUtils.isEmpty(farmerParentSurvey.getUpdatedDate())) {
                spNameFarmerPersonalDetailsClass.setUpdatedDate(farmerParentSurvey.getUpdatedDate());
            } else {
                spNameFarmerPersonalDetailsClass.setUpdatedDate("");
            }

            if (!TextUtils.isEmpty(farmerParentSurvey.getCreatedDate())) {
                spNameFarmerPersonalDetailsClass.setCreatedDate(farmerParentSurvey.getCreatedDate());
            } else {
                spNameFarmerPersonalDetailsClass.setCreatedDate("");
            }



            ArrayList<SyncPersonalLandAllDetailsRequestDTO.FarmerHouseholdParentSurvey> spNameFarmerPersonalDetailsClasses = new ArrayList<>();
            spNameFarmerPersonalDetailsClasses.add(spNameFarmerPersonalDetailsClass);
            syncPersonalLandAllDetailsRequestDTO.setFarmerHouseholdParentSurvey(spNameFarmerPersonalDetailsClasses);


            AppWebService.createService(AppAPI.class).syncFarmerDetailsDataToServer(syncPersonalLandAllDetailsRequestDTO,

                            appHelper.getSharedPrefObj().getString(accessToken, "")).
                    enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            executor.execute(() -> {
                                try {
                                    String strResponse = response.body().string();
                                    Log.d(TAG, "onResponse: AppData " + response);
                                    JSONObject json_object = new JSONObject(strResponse);
                                    String message = "", status = "";
                                    Log.e(TAG, "onResponse: data json" + json_object);
                                    message = json_object.getString("messgae");
                                    status = String.valueOf(json_object.getInt("status"));
                                    Log.d(TAG, "status " + status);
                                    if (status.equals("201")) {
                                        FarmerHouseholdParentSurvey businessReviewSurveyTableFromDB = appDAO.getTopFarmerHouseholdParentSurveyDetailListTableTableData(farmerParentSurvey.getFarmerCode());
                                        if (businessReviewSurveyTableFromDB != null) {
                                            businessReviewSurveyTableFromDB.setId(businessReviewSurveyTableFromDB.getId());
                                            farmerParentSurvey.setSync(true);
                                            farmerParentSurvey.setServerSync("1");
                                            appDAO.insertFarmerHouseholdParentSurveyListTableLocalDB(farmerParentSurvey);
                                            data.postValue(SUCCESS_RESPONSE_MESSAGE);
                                        }

                                    } else if (status.equals("0")) {
                                        data.postValue(message);
                                    }

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    data.postValue(FAILURE_RESPONSE_MESSAGE);
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






    //FarmerHouseholdChildrenSurvey
    public LiveData<FarmerHouseholdChildrenSurvey> insertFarmerHouseholdChildrenSurveyIntoLocalDB(FarmerHouseholdChildrenSurvey farmerHouseholdChildrenSurvey) {
        final MutableLiveData<FarmerHouseholdChildrenSurvey> data = new MutableLiveData<>();
        executor.execute(() -> {
            appDAO.insertFarmerHouseholdChildrenSurveyDetailListTable(farmerHouseholdChildrenSurvey);
//            data.postValue(appDAO.getTopDocListTableDataFromLocal(docIdentiFicationDeatilsTable.getFarmerCode()));
        });
        return data;
    }

    //child count
    public LiveData<Integer> getFarmerHouseholdChildrenSurveyNotSyncCountFromLocalDB() {
        return appDAO.getFarmerHouseholdChildrenSurveyNotSyncCountFromLocalDB("0");
    }


    //list
    public LiveData<List<FarmerHouseholdChildrenSurvey>> getFarmerHouseholdChildrenSurveyDetailsFromLocalDbById(String fid) {
        final MutableLiveData<List<FarmerHouseholdChildrenSurvey>> data = new MutableLiveData<>();
        executor.execute(() -> {
            Log.d(TAG, "FId" + fid);
            boolean dataExist = (appDAO.getFarmerHouseholdChildrenSurveyDetailsFromLocalDbById(fid) != null);
            if (dataExist) {
                data.postValue(appDAO.getFarmerHouseholdChildrenSurveyDetailsFromLocalDbById(fid));
            }
        });
        return data;
    }

    //Child Survey Main
    public LiveData<List<FarmerHouseholdChildrenSurvey>> getFarmerHouseholdChildrenSurveyDetailslistFromLocalDBNotSync() {
        final MutableLiveData<List<FarmerHouseholdChildrenSurvey>> data = new MutableLiveData<>();
        executor.execute(() -> {
            boolean dataExist = (appDAO.getFarmerHouseholdChildrenSurveyDetailsListFromLocalDBNotSync("0") != null);
            if (dataExist) {
                data.postValue(appDAO.getFarmerHouseholdChildrenSurveyDetailsListFromLocalDBNotSync("0"));
            }
        });
        return data;
    }

    //Child sync
    public LiveData<String> syncChildrenSurveyDetailsDataToServer(FarmerHouseholdChildrenSurvey farmerChildrenSurvey) {
        final MutableLiveData<String> data = new MutableLiveData<>();
        AppWebService.changeApiBaseUrl(RAW_DATA_URL);
        executor.execute(() -> {
            final SyncPersonalLandAllDetailsRequestDTO syncPersonalLandAllDetailsRequestDTO = new SyncPersonalLandAllDetailsRequestDTO();
            SyncPersonalLandAllDetailsRequestDTO.FarmerHouseholdChildrenSurvey spNameFarmerPersonalDetailsClass = new SyncPersonalLandAllDetailsRequestDTO.FarmerHouseholdChildrenSurvey();


            if (!TextUtils.isEmpty(farmerChildrenSurvey.getFarmerCode())) {
                spNameFarmerPersonalDetailsClass.setFarmerCode(farmerChildrenSurvey.getFarmerCode());
            } else {
                spNameFarmerPersonalDetailsClass.setFarmerCode("");
            }

            if (!TextUtils.isEmpty(farmerChildrenSurvey.getFarmerId())) {
                spNameFarmerPersonalDetailsClass.setFarmerId(farmerChildrenSurvey.getFarmerId());
            } else {
                spNameFarmerPersonalDetailsClass.setFarmerId("0");
            }

            if (!TextUtils.isEmpty(String.valueOf(farmerChildrenSurvey.getFarmerHouseholdSurveyId()))) {
                spNameFarmerPersonalDetailsClass.setFarmerHouseholdSurveyId(farmerChildrenSurvey.getFarmerHouseholdSurveyId());
            } else {
                spNameFarmerPersonalDetailsClass.setFarmerHouseholdSurveyId(0);
            }

            if (!TextUtils.isEmpty(farmerChildrenSurvey.getChildrenName())) {
                spNameFarmerPersonalDetailsClass.setChildrenName(farmerChildrenSurvey.getChildrenName());
            } else {
                spNameFarmerPersonalDetailsClass.setChildrenName("");
            }


            if (!TextUtils.isEmpty(String.valueOf(farmerChildrenSurvey.getChildrenAge()))) {
                spNameFarmerPersonalDetailsClass.setChildrenAge(farmerChildrenSurvey.getChildrenAge());
            } else {
                spNameFarmerPersonalDetailsClass.setChildrenAge(0);
            }

            if (!TextUtils.isEmpty(farmerChildrenSurvey.getChildrenGender())) {
                spNameFarmerPersonalDetailsClass.setChildrenGender(farmerChildrenSurvey.getChildrenGender());
            } else {
                spNameFarmerPersonalDetailsClass.setChildrenGender("");
            }

            if (!TextUtils.isEmpty(farmerChildrenSurvey.getChildrenOccupation())) {
                spNameFarmerPersonalDetailsClass.setChildrenOccupation(farmerChildrenSurvey.getChildrenOccupation());
            } else {
                spNameFarmerPersonalDetailsClass.setChildrenOccupation("");
            }


            if (!TextUtils.isEmpty(String.valueOf(farmerChildrenSurvey.getIsActive()))) {
                spNameFarmerPersonalDetailsClass.setIsActive(farmerChildrenSurvey.getIsActive());
            } else {
                spNameFarmerPersonalDetailsClass.setIsActive("");
            }


            if (!TextUtils.isEmpty(farmerChildrenSurvey.getCreatedByUserId())) {
                spNameFarmerPersonalDetailsClass.setCreatedByUserId(farmerChildrenSurvey.getCreatedByUserId());
            } else {
                spNameFarmerPersonalDetailsClass.setCreatedByUserId("");
            }

            if (!TextUtils.isEmpty(farmerChildrenSurvey.getUpdatedByUserId())) {
                spNameFarmerPersonalDetailsClass.setUpdatedByUserId(farmerChildrenSurvey.getUpdatedByUserId());
            } else {
                spNameFarmerPersonalDetailsClass.setUpdatedByUserId("");
            }

            if (!TextUtils.isEmpty(farmerChildrenSurvey.getUpdatedDate())) {
                spNameFarmerPersonalDetailsClass.setUpdatedDate(farmerChildrenSurvey.getUpdatedDate());
            } else {
                spNameFarmerPersonalDetailsClass.setUpdatedDate("");
            }

            if (!TextUtils.isEmpty(farmerChildrenSurvey.getCreatedDate())) {
                spNameFarmerPersonalDetailsClass.setCreatedDate(farmerChildrenSurvey.getCreatedDate());
            } else {
                spNameFarmerPersonalDetailsClass.setCreatedDate("");
            }



            ArrayList<SyncPersonalLandAllDetailsRequestDTO.FarmerHouseholdChildrenSurvey> spNameFarmerPersonalDetailsClasses = new ArrayList<>();
            spNameFarmerPersonalDetailsClasses.add(spNameFarmerPersonalDetailsClass);
            syncPersonalLandAllDetailsRequestDTO.setFarmerHouseholdChildrenSurvey(spNameFarmerPersonalDetailsClasses);


            AppWebService.createService(AppAPI.class).syncFarmerDetailsDataToServer(syncPersonalLandAllDetailsRequestDTO,

                            appHelper.getSharedPrefObj().getString(accessToken, "")).
                    enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            executor.execute(() -> {
                                try {
                                    String strResponse = response.body().string();
                                    Log.d(TAG, "onResponse: AppData " + response);
                                    JSONObject json_object = new JSONObject(strResponse);
                                    String message = "", status = "";
                                    Log.e(TAG, "onResponse: data json" + json_object);
                                    message = json_object.getString("messgae");
                                    status = String.valueOf(json_object.getInt("status"));
                                    Log.d(TAG, "status " + status);
                                    if (status.equals("201")) {
                                        FarmerHouseholdChildrenSurvey businessReviewSurveyTableFromDB = appDAO.getTopFarmerHouseholdChildrenSurveyDetailListTableTableData(farmerChildrenSurvey.getFarmerCode());
                                        if (businessReviewSurveyTableFromDB != null) {
                                            businessReviewSurveyTableFromDB.setId(businessReviewSurveyTableFromDB.getId());
                                            farmerChildrenSurvey.setSync(true);
                                            farmerChildrenSurvey.setServerSync("1");
                                            appDAO.insertFarmerHouseholdChildrenSurveyListTableLocalDB(farmerChildrenSurvey);
                                            data.postValue(SUCCESS_RESPONSE_MESSAGE);
                                        }

                                    } else if (status.equals("0")) {
                                        data.postValue(message);
                                    }

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    data.postValue(FAILURE_RESPONSE_MESSAGE);
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

    //Risk
    public LiveData<RiskAssessment> insertRiskAssessmentIntoLocalDB(RiskAssessment riskAssessment) {
        final MutableLiveData<RiskAssessment> data = new MutableLiveData<>();
        executor.execute(() -> {
            appDAO.insertRiskAssessmentDetailListTable(riskAssessment);
//            data.postValue(appDAO.getTopDocListTableDataFromLocal(docIdentiFicationDeatilsTable.getFarmerCode()));
        });
        return data;
    }

    //Risk count
    public LiveData<Integer> getRiskNotSyncCountFromLocalDB() {
        return appDAO.getRiskNotSyncCountFromLocalDB("0");
    }

    //Risk Survey Main
    public LiveData<List<RiskAssessment>> getRiskAssessmentDetailslistFromLocalDBNotSync() {
        final MutableLiveData<List<RiskAssessment>> data = new MutableLiveData<>();
        executor.execute(() -> {
            boolean dataExist = (appDAO.getRiskAssessmentDetailsListFromLocalDBNotSync("0") != null);
            if (dataExist) {
                data.postValue(appDAO.getRiskAssessmentDetailsListFromLocalDBNotSync("0"));
            }
        });
        return data;
    }

    //Manfacturer Farmer Survey Main
    public LiveData<List<ManfacturerFarmer>> getManfacturerFarmerDetailslistFromLocalDBNotSync() {
        final MutableLiveData<List<ManfacturerFarmer>> data = new MutableLiveData<>();
        executor.execute(() -> {
            boolean dataExist = (appDAO.getManfacturerFarmerDetailsListFromLocalDBNotSync("0") != null);
            if (dataExist) {
                data.postValue(appDAO.getManfacturerFarmerDetailsListFromLocalDBNotSync("0"));
            }
        });
        return data;
    }

    //DealerFarmer Survey Main
    public LiveData<List<DealerFarmer>> getDealerFarmerDetailslistFromLocalDBNotSync() {
        final MutableLiveData<List<DealerFarmer>> data = new MutableLiveData<>();
        executor.execute(() -> {
            boolean dataExist = (appDAO.getDetailsListFromLocalDBNotSync("0") != null);
            if (dataExist) {
                data.postValue(appDAO.getDetailsListFromLocalDBNotSync("0"));
            }
        });
        return data;
    }


    //Child sync
    public LiveData<String> syncRiskDetailsDataToServer(RiskAssessment riskAssessment) {
        final MutableLiveData<String> data = new MutableLiveData<>();
        AppWebService.changeApiBaseUrl(RAW_DATA_URL);
        executor.execute(() -> {
            final SyncPersonalLandAllDetailsRequestDTO syncPersonalLandAllDetailsRequestDTO = new SyncPersonalLandAllDetailsRequestDTO();
            SyncPersonalLandAllDetailsRequestDTO.RiskAssessment spNameFarmerPersonalDetailsClass = new SyncPersonalLandAllDetailsRequestDTO.RiskAssessment();

            if (!TextUtils.isEmpty(String.valueOf(riskAssessment.getRiskAssesmentQuestionHdrId()))) {
                spNameFarmerPersonalDetailsClass.setRiskAssesmentQuestionHdrId(riskAssessment.getRiskAssesmentQuestionHdrId());
            } else {
                spNameFarmerPersonalDetailsClass.setRiskAssesmentQuestionHdrId(0);
            }

            if (!TextUtils.isEmpty(riskAssessment.getFarmerCode())) {
                spNameFarmerPersonalDetailsClass.setFarmerCode(riskAssessment.getFarmerCode());
            } else {
                spNameFarmerPersonalDetailsClass.setFarmerCode("");
            }


            if (!TextUtils.isEmpty(riskAssessment.getAnswers())) {
                spNameFarmerPersonalDetailsClass.setAnswers(riskAssessment.getAnswers());
            } else {
                spNameFarmerPersonalDetailsClass.setAnswers("");
            }



            if (!TextUtils.isEmpty(String.valueOf(riskAssessment.getIsActive()))) {
                spNameFarmerPersonalDetailsClass.setIsActive(riskAssessment.getIsActive());
            } else {
                spNameFarmerPersonalDetailsClass.setIsActive("");
            }


            if (!TextUtils.isEmpty(riskAssessment.getCreatedByUserId())) {
                spNameFarmerPersonalDetailsClass.setCreatedByUserId(riskAssessment.getCreatedByUserId());
            } else {
                spNameFarmerPersonalDetailsClass.setCreatedByUserId("");
            }

            if (!TextUtils.isEmpty(riskAssessment.getUpdatedByUserId())) {
                spNameFarmerPersonalDetailsClass.setUpdatedByUserId(riskAssessment.getUpdatedByUserId());
            } else {
                spNameFarmerPersonalDetailsClass.setUpdatedByUserId("");
            }

            if (!TextUtils.isEmpty(riskAssessment.getUpdatedDate())) {
                spNameFarmerPersonalDetailsClass.setUpdatedDate(riskAssessment.getUpdatedDate());
            } else {
                spNameFarmerPersonalDetailsClass.setUpdatedDate("");
            }

            if (!TextUtils.isEmpty(riskAssessment.getCreatedDate())) {
                spNameFarmerPersonalDetailsClass.setCreatedDate(riskAssessment.getCreatedDate());
            } else {
                spNameFarmerPersonalDetailsClass.setCreatedDate("");
            }



            ArrayList<SyncPersonalLandAllDetailsRequestDTO.RiskAssessment> spNameFarmerPersonalDetailsClasses = new ArrayList<>();
            spNameFarmerPersonalDetailsClasses.add(spNameFarmerPersonalDetailsClass);
            syncPersonalLandAllDetailsRequestDTO.setRiskAssessment(spNameFarmerPersonalDetailsClasses);


            AppWebService.createService(AppAPI.class).syncFarmerDetailsDataToServer(syncPersonalLandAllDetailsRequestDTO,

                            appHelper.getSharedPrefObj().getString(accessToken, "")).
                    enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            executor.execute(() -> {
                                try {
                                    String strResponse = response.body().string();
                                    Log.d(TAG, "onResponse: AppData " + response);
                                    JSONObject json_object = new JSONObject(strResponse);
                                    String message = "", status = "";
                                    Log.e(TAG, "onResponse: data json" + json_object);
                                    message = json_object.getString("messgae");
                                    status = String.valueOf(json_object.getInt("status"));
                                    Log.d(TAG, "status " + status);
                                    if (status.equals("201")) {
                                        RiskAssessment businessReviewSurveyTableFromDB = appDAO.getTopRiskAssessmentDetailListTableTableData(riskAssessment.getFarmerCode());
                                        if (businessReviewSurveyTableFromDB != null) {
                                            businessReviewSurveyTableFromDB.setRiskId(businessReviewSurveyTableFromDB.getRiskId());
                                            riskAssessment.setSync(true);
                                            riskAssessment.setServerSync("1");
                                            appDAO.insertRiskAssessmentDetailListTable(riskAssessment);
                                            data.postValue(SUCCESS_RESPONSE_MESSAGE);
                                        }

                                    } else if (status.equals("0")) {
                                        data.postValue(message);
                                    }

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    data.postValue(FAILURE_RESPONSE_MESSAGE);
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

    //list
    public LiveData<List<RiskAssessment>> getRiskAssessmentDetailsFromLocalDbById(String fid) {
        final MutableLiveData<List<RiskAssessment>> data = new MutableLiveData<>();
        executor.execute(() -> {
            Log.d(TAG, "FId" + fid);
            boolean dataExist = (appDAO.getRiskAssessmentDetailsFromLocalDbById(fid) != null);
            if (dataExist) {
                data.postValue(appDAO.getRiskAssessmentDetailsFromLocalDbById(fid));
            }
        });
        return data;
    }


    //Manfacturer Farmer
    public LiveData<ManfacturerFarmer> insertManfacturerFarmerIntoLocalDB(ManfacturerFarmer manfacturerFarmer) {
        final MutableLiveData<ManfacturerFarmer> data = new MutableLiveData<>();
        executor.execute(() -> {
            appDAO.insertManfacturerFarmerDetailListTable(manfacturerFarmer);
//            data.postValue(appDAO.getTopDocListTableDataFromLocal(docIdentiFicationDeatilsTable.getFarmerCode()));
        });
        return data;
    }

    //manu count
    public LiveData<Integer> getManufacturerNotSyncCountFromLocalDB() {
        return appDAO.getManufacturerNotSyncCountFromLocalDB("0");
    }

    //ManfacturerFarmer list
    public LiveData<List<ManfacturerFarmer>> getManfacturerFarmerDetailsFromLocalDbById(String fid) {
        final MutableLiveData<List<ManfacturerFarmer>> data = new MutableLiveData<>();
        executor.execute(() -> {
            Log.d(TAG, "FId" + fid);
            boolean dataExist = (appDAO.getManfacturerFarmerDetailsFromLocalDbById(fid) != null);
            if (dataExist) {
                data.postValue(appDAO.getManfacturerFarmerDetailsFromLocalDbById(fid));
            }
        });
        return data;
    }


    //Dealer Farmer
    public LiveData<DealerFarmer> insertDealerFarmerIntoLocalDB(DealerFarmer dealerFarmer) {
        final MutableLiveData<DealerFarmer> data = new MutableLiveData<>();
        executor.execute(() -> {
            appDAO.insertDealerFarmerDetailListTable(dealerFarmer);
//            data.postValue(appDAO.getTopDocListTableDataFromLocal(docIdentiFicationDeatilsTable.getFarmerCode()));
        });
        return data;
    }

    //dealer count
    public LiveData<Integer> getDealerNotSyncCountFromLocalDB() {
        return appDAO.getDealerFarmerNotSyncCountFromLocalDB("0");
    }

    //Dealer list
    public LiveData<List<DealerFarmer>> getDealerFarmerDetailsFromLocalDbById(String fid) {
        final MutableLiveData<List<DealerFarmer>> data = new MutableLiveData<>();
        executor.execute(() -> {
            Log.d(TAG, "FId" + fid);
            boolean dataExist = (appDAO.getDealerFarmerDetailsFromLocalDbById(fid) != null);
            if (dataExist) {
                data.postValue(appDAO.getDealerFarmerDetailsFromLocalDbById(fid));
            }
        });
        return data;
    }

    //Manufacturer sync
    public LiveData<String> syncManufacturerDetailsDataToServer(ManfacturerFarmer manfacturerFarmer) {
        final MutableLiveData<String> data = new MutableLiveData<>();
        AppWebService.changeApiBaseUrl(RAW_DATA_URL);
        executor.execute(() -> {
            final SyncPersonalLandAllDetailsRequestDTO syncPersonalLandAllDetailsRequestDTO = new SyncPersonalLandAllDetailsRequestDTO();
            SyncPersonalLandAllDetailsRequestDTO.ProcessorFarmer spNameFarmerPersonalDetailsClass = new SyncPersonalLandAllDetailsRequestDTO.ProcessorFarmer();

            if (!TextUtils.isEmpty(String.valueOf(manfacturerFarmer.getManfacturerId()))) {
                spNameFarmerPersonalDetailsClass.setProcessorId(manfacturerFarmer.getManfacturerId());
            } else {
                spNameFarmerPersonalDetailsClass.setProcessorId(0);
            }

            if (!TextUtils.isEmpty(manfacturerFarmer.getFarmerCode())) {
                spNameFarmerPersonalDetailsClass.setFarmerCode(manfacturerFarmer.getFarmerCode());
            } else {
                spNameFarmerPersonalDetailsClass.setFarmerCode("");
            }


            if (!TextUtils.isEmpty(String.valueOf(manfacturerFarmer.getIsActive()))) {
                spNameFarmerPersonalDetailsClass.setIsActive(manfacturerFarmer.getIsActive());
            } else {
                spNameFarmerPersonalDetailsClass.setIsActive("");
            }


            if (!TextUtils.isEmpty(manfacturerFarmer.getCreatedByUserId())) {
                spNameFarmerPersonalDetailsClass.setCreatedByUserId(manfacturerFarmer.getCreatedByUserId());
            } else {
                spNameFarmerPersonalDetailsClass.setCreatedByUserId("");
            }

            if (!TextUtils.isEmpty(manfacturerFarmer.getUpdatedByUserId())) {
                spNameFarmerPersonalDetailsClass.setUpdatedByUserId(manfacturerFarmer.getUpdatedByUserId());
            } else {
                spNameFarmerPersonalDetailsClass.setUpdatedByUserId("");
            }

            if (!TextUtils.isEmpty(manfacturerFarmer.getUpdatedDate())) {
                spNameFarmerPersonalDetailsClass.setUpdatedDate(manfacturerFarmer.getUpdatedDate());
            } else {
                spNameFarmerPersonalDetailsClass.setUpdatedDate("");
            }

            if (!TextUtils.isEmpty(manfacturerFarmer.getCreatedDate())) {
                spNameFarmerPersonalDetailsClass.setCreatedDate(manfacturerFarmer.getCreatedDate());
            } else {
                spNameFarmerPersonalDetailsClass.setCreatedDate("");
            }



            ArrayList<SyncPersonalLandAllDetailsRequestDTO.ProcessorFarmer> spNameFarmerPersonalDetailsClasses = new ArrayList<>();
            spNameFarmerPersonalDetailsClasses.add(spNameFarmerPersonalDetailsClass);
            syncPersonalLandAllDetailsRequestDTO.setManfacturerFarmer(spNameFarmerPersonalDetailsClasses);


            AppWebService.createService(AppAPI.class).syncFarmerDetailsDataToServer(syncPersonalLandAllDetailsRequestDTO,

                            appHelper.getSharedPrefObj().getString(accessToken, "")).
                    enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            executor.execute(() -> {
                                try {
                                    String strResponse = response.body().string();
                                    Log.d(TAG, "onResponse: AppData " + response);
                                    JSONObject json_object = new JSONObject(strResponse);
                                    String message = "", status = "";
                                    Log.e(TAG, "onResponse: data json" + json_object);
                                    message = json_object.getString("messgae");
                                    status = String.valueOf(json_object.getInt("status"));
                                    Log.d(TAG, "status " + status);
                                    if (status.equals("201")) {
                                        ManfacturerFarmer businessReviewSurveyTableFromDB = appDAO.getTopManfacturerFarmerDetailListTableTableData(manfacturerFarmer.getFarmerCode());
                                        if (businessReviewSurveyTableFromDB != null) {
                                            businessReviewSurveyTableFromDB.setId(businessReviewSurveyTableFromDB.getId());
                                            manfacturerFarmer.setSync(true);
                                            manfacturerFarmer.setServerSync("1");
                                            appDAO.insertManfacturerFarmerDetailListTable(manfacturerFarmer);
                                            data.postValue(SUCCESS_RESPONSE_MESSAGE);
                                        }

                                    } else if (status.equals("0")) {
                                        data.postValue(message);
                                    }

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    data.postValue(FAILURE_RESPONSE_MESSAGE);
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

    //Dealer sync
    public LiveData<String> syncDealerDetailsDataToServer(DealerFarmer dealerFarmer) {
        final MutableLiveData<String> data = new MutableLiveData<>();
        AppWebService.changeApiBaseUrl(RAW_DATA_URL);
        executor.execute(() -> {
            final SyncPersonalLandAllDetailsRequestDTO syncPersonalLandAllDetailsRequestDTO = new SyncPersonalLandAllDetailsRequestDTO();
            SyncPersonalLandAllDetailsRequestDTO.DealerFarmer spNameFarmerPersonalDetailsClass = new SyncPersonalLandAllDetailsRequestDTO.DealerFarmer();

            if (!TextUtils.isEmpty(String.valueOf(dealerFarmer.getDealerId()))) {
                spNameFarmerPersonalDetailsClass.setDealerId(dealerFarmer.getDealerId());
            } else {
                spNameFarmerPersonalDetailsClass.setDealerId(0);
            }

            if (!TextUtils.isEmpty(dealerFarmer.getFarmerCode())) {
                spNameFarmerPersonalDetailsClass.setFarmerCode(dealerFarmer.getFarmerCode());
            } else {
                spNameFarmerPersonalDetailsClass.setFarmerCode("");
            }


            if (!TextUtils.isEmpty(String.valueOf(dealerFarmer.getIsActive()))) {
                spNameFarmerPersonalDetailsClass.setIsActive(dealerFarmer.getIsActive());
            } else {
                spNameFarmerPersonalDetailsClass.setIsActive("");
            }


            if (!TextUtils.isEmpty(dealerFarmer.getCreatedByUserId())) {
                spNameFarmerPersonalDetailsClass.setCreatedByUserId(dealerFarmer.getCreatedByUserId());
            } else {
                spNameFarmerPersonalDetailsClass.setCreatedByUserId("");
            }

            if (!TextUtils.isEmpty(dealerFarmer.getUpdatedByUserId())) {
                spNameFarmerPersonalDetailsClass.setUpdatedByUserId(dealerFarmer.getUpdatedByUserId());
            } else {
                spNameFarmerPersonalDetailsClass.setUpdatedByUserId("");
            }

            if (!TextUtils.isEmpty(dealerFarmer.getUpdatedDate())) {
                spNameFarmerPersonalDetailsClass.setUpdatedDate(dealerFarmer.getUpdatedDate());
            } else {
                spNameFarmerPersonalDetailsClass.setUpdatedDate("");
            }

            if (!TextUtils.isEmpty(dealerFarmer.getCreatedDate())) {
                spNameFarmerPersonalDetailsClass.setCreatedDate(dealerFarmer.getCreatedDate());
            } else {
                spNameFarmerPersonalDetailsClass.setCreatedDate("");
            }



            ArrayList<SyncPersonalLandAllDetailsRequestDTO.DealerFarmer> spNameFarmerPersonalDetailsClasses = new ArrayList<>();
            spNameFarmerPersonalDetailsClasses.add(spNameFarmerPersonalDetailsClass);
            syncPersonalLandAllDetailsRequestDTO.setDealerFarmer(spNameFarmerPersonalDetailsClasses);


            AppWebService.createService(AppAPI.class).syncFarmerDetailsDataToServer(syncPersonalLandAllDetailsRequestDTO,

                            appHelper.getSharedPrefObj().getString(accessToken, "")).
                    enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            executor.execute(() -> {
                                try {
                                    String strResponse = response.body().string();
                                    Log.d(TAG, "onResponse: AppData " + response);
                                    JSONObject json_object = new JSONObject(strResponse);
                                    String message = "", status = "";
                                    Log.e(TAG, "onResponse: data json" + json_object);
                                    message = json_object.getString("messgae");
                                    status = String.valueOf(json_object.getInt("status"));
                                    Log.d(TAG, "status " + status);
                                    if (status.equals("201")) {
                                        DealerFarmer businessReviewSurveyTableFromDB = appDAO.getTopDealerFarmerDetailListTableTableData(dealerFarmer.getFarmerCode());
                                        if (businessReviewSurveyTableFromDB != null) {
                                            businessReviewSurveyTableFromDB.setId(businessReviewSurveyTableFromDB.getId());
                                            dealerFarmer.setSync(true);
                                            dealerFarmer.setServerSync("1");
                                            appDAO.insertDealerFarmerDetailListTable(dealerFarmer);
                                            data.postValue(SUCCESS_RESPONSE_MESSAGE);
                                        }

                                    } else if (status.equals("0")) {
                                        data.postValue(message);
                                    }

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    data.postValue(FAILURE_RESPONSE_MESSAGE);
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


    public LiveData<Integer> getPlotsCountWhichAreNotSyncByFarmercode(String strFarmerCode) {
    //    Log.d(TAG, "getFerlizerDataCount: " + logBookNum);
        return appDAO.getPlantationCountForFarmerBasedOnFarmerCode(strFarmerCode);
    }
}


