package com.socatra.excutivechain.view_models;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

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
import com.socatra.excutivechain.repositories.AppRepository;

import java.util.List;

import javax.inject.Inject;

public class AppViewModel extends ViewModel {

    private AppRepository appRepository;

    @Inject
    public AppViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    //Live data login
    private LiveData<LoginResponseDTO> loginResponseDTOFromServerLiveData;

    public LiveData<LoginResponseDTO> getloginResponseDTOFromServerLiveData() {
        return loginResponseDTOFromServerLiveData;
    }

    // for login
    public void logInServiceList(String userId) {
        try {
            loginResponseDTOFromServerLiveData = appRepository.getlogInServiceResponse(userId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // for clearing local db for master details


    public void deleteAllTablesFromLocalMaster() {
        try {
            appRepository.deleteAllTablesFromLocalMaster();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void deleteTablesFromLocalTransactionData() {
        try {
            appRepository.deleteTablesFromLocalTransactionData();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // refresh data, daily timings
    private LiveData<RefreshTableDateCheck> getAddRefreshTableDateCheck;


    public void getRefreshTableDateCheckFromLocalDBByDate(String DeviceID, String currentDate) {
        try {
            getAddRefreshTableDateCheck = appRepository.getAddRefreshTableDateCheckTableDate(DeviceID, currentDate);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<RefreshTableDateCheck> getRefreshTableDateCheckByDateFromLocalDBLiveData() {
        return getAddRefreshTableDateCheck;
    }

    private LiveData<RefreshTableDateCheck> addRefreshTableDateCheckLiveData;

    public void insertRefreshTableDateCheckTableLocalDb(RefreshTableDateCheck refreshTableDateCheck) {
        try {
            addRefreshTableDateCheckLiveData = appRepository.insertRefreshTableDateCheck(refreshTableDateCheck);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<RefreshTableDateCheck> getRefreshTableDateCheckLiveDataFromLocalDB() {
        return addRefreshTableDateCheckLiveData;
    }

    //Country
    private LiveData<Country> insertCountryListDataIntoLocalDBQueryLiveData;

    public void insertCountryListDetailIntoLocalDBQuery(Country country) {
        try {
            insertCountryListDataIntoLocalDBQueryLiveData = appRepository.insertCountryListDataIntoLocalDBRepository(country);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<Country> getinsertCountryTableListDataIntoLocalDBQueryLiveDataLocalDB() {
        return insertCountryListDataIntoLocalDBQueryLiveData;
    }

    //StateorProvince
    private LiveData<StateorProvince> insertStateListDataIntoLocalDBQueryLiveData;

    public void insertStateListDetailIntoLocalDBQuery(StateorProvince stateorProvince) {
        try {
            insertStateListDataIntoLocalDBQueryLiveData = appRepository.insertStateListDataIntoLocalDBRepository(stateorProvince);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<StateorProvince> getinsertStateTableListDataIntoLocalDBQueryLiveDataLocalDB() {
        return insertStateListDataIntoLocalDBQueryLiveData;
    }

    //DistrictorRegency
    private LiveData<DistrictorRegency> insertDistrictListDataIntoLocalDBQueryLiveData;

    public void insertDistrictListDetailIntoLocalDBQuery(DistrictorRegency districtorRegency) {
        try {
            insertDistrictListDataIntoLocalDBQueryLiveData = appRepository.insertDistrictListDataIntoLocalDBRepository(districtorRegency);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<DistrictorRegency> getinsertDistrictTableListDataIntoLocalDBQueryLiveDataLocalDB() {
        return insertDistrictListDataIntoLocalDBQueryLiveData;
    }


    //SubDistrict
    private LiveData<SubDistrict> insertSubDistrictListDataIntoLocalDBQueryLiveData;

    public void insertSubDistrictListDetailIntoLocalDBQuery(SubDistrict subDistrict) {
        try {
            insertSubDistrictListDataIntoLocalDBQueryLiveData =
                    appRepository.insertSubDistrictListDataIntoLocalDBRepository(subDistrict);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<SubDistrict> getinsertSubDistrictTableListDataIntoLocalDBQueryLiveDataLocalDB() {
        return insertSubDistrictListDataIntoLocalDBQueryLiveData;
    }


    //village
    private LiveData<VillageTable> insertVillageListDataIntoLocalDBQueryLiveData;

    public void insertVillageListDetailIntoLocalDBQuery(VillageTable villageTable) {
        try {
            insertVillageListDataIntoLocalDBQueryLiveData = appRepository.insertVillageListDataIntoLocalDBRepository(villageTable);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<VillageTable> getinsertVillageTableListDataIntoLocalDBQueryLiveDataLocalDB() {
        return insertVillageListDataIntoLocalDBQueryLiveData;
    }


    //Risk Question Master
    private LiveData<RiskAssessmentQuestion> insertRiskAssessmentQuestionIntoLocalDBQueryLiveData;

    public void insertRiskAssessmentQuestionListDetailIntoLocalDBQuery(RiskAssessmentQuestion riskAssessmentQuestion) {
        try {
            insertRiskAssessmentQuestionIntoLocalDBQueryLiveData = appRepository.insertRiskAssessmentQuestionListDataIntoLocalDBRepository(riskAssessmentQuestion);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<RiskAssessmentQuestion> getInsertRiskAssessmentQuestionTableListDataIntoLocalDBQueryLiveDataLocalDB() {
        return insertRiskAssessmentQuestionIntoLocalDBQueryLiveData;
    }


    //Manufacturer Master
    private LiveData<ManufacturerMaster> insertManufacturerMasterIntoLocalDBQueryLiveData;

    public void insertManufacturerMasterDetailIntoLocalDBQuery(ManufacturerMaster manufacturerMaster) {
        try {
            insertManufacturerMasterIntoLocalDBQueryLiveData = appRepository.insertManufacturerMasterDataIntoLocalDBRepository(manufacturerMaster);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<ManufacturerMaster> getInsertManufacturerMasterDataIntoLocalDBQueryLiveDataLocalDB() {
        return insertManufacturerMasterIntoLocalDBQueryLiveData;
    }

    //Dealer Master
    private LiveData<DealerMaster> insertDealerMasterIntoLocalDBQueryLiveData;

    public void insertDealerMasterDetailIntoLocalDBQuery(DealerMaster dealerMaster) {
        try {
            insertDealerMasterIntoLocalDBQueryLiveData = appRepository.insertDealerMasterDataIntoLocalDBRepository(dealerMaster);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<DealerMaster> getInsertDealerMasterDataIntoLocalDBQueryLiveDataLocalDB() {
        return insertDealerMasterIntoLocalDBQueryLiveData;
    }

    //App Language

    private LiveData<AppLanguageTable> insertAppLanguageMasterIntoLocalDBQueryLiveData;
    public void insertLanguageMasterDetailIntoLocalDBQuery(AppLanguageTable appLanguageTable) {
        try {
            insertAppLanguageMasterIntoLocalDBQueryLiveData = appRepository.insertLanguageMasterDataIntoLocalDBRepository(appLanguageTable);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public LiveData<AppLanguageTable> getInsertAppLanguageMasterDataIntoLocalDBQueryLiveDataLocalDB() {
        return insertAppLanguageMasterIntoLocalDBQueryLiveData;
    }

    //App Language HDR
    private LiveData<AppLanguageHDRTable> insertAppLanguageHDRMasterIntoLocalDBQueryLiveData;
    public void insertLanguageHDRMasterDetailIntoLocalDBQuery(AppLanguageHDRTable appLanguageHDRTable) {
        try {
            insertAppLanguageHDRMasterIntoLocalDBQueryLiveData = appRepository.insertLanguageHDRMasterDataIntoLocalDBRepository(appLanguageHDRTable);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public LiveData<AppLanguageHDRTable> getInsertAppLanguageHDRMasterDataIntoLocalDBQueryLiveDataLocalDB() {
        return insertAppLanguageHDRMasterIntoLocalDBQueryLiveData;
    }

    //Farmer Main
    private LiveData<FarmersTable> farmerDetailListTableLiveDataInsert;


    public void insertFarmerDetailListTableLocal(FarmersTable farmerTable) {
        try {
            farmerDetailListTableLiveDataInsert = appRepository.insertFarmerDetailListTableTable(farmerTable);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<FarmersTable> getfarmerDetailListTableLiveDataInsertLiveDataFromLocalDB() {
        return farmerDetailListTableLiveDataInsert;
    }


    // TODO: 9/14/2023 get Farmer details by farmer code
    private LiveData<FarmersTable> farmersTableLiveData;
    public void getFarmerDetailsByFarmerCode(String strFarmercode) {
        try {
            farmersTableLiveData = appRepository.getFarmersDataFromLocalDb(strFarmercode);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<FarmersTable> getFarmersTableByFarmerCodeLiveData() {
        return farmersTableLiveData;
    }

    //App Language
    private LiveData<String > stLanguage;
    public void getLanguageDataViewModel(String stLang,String stWord) {
        try {
            stLanguage = appRepository.getLanguageDataRepository(stLang,stWord);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getLanguageDataVM(String stLang, String stWord) {
        return appRepository.getLanguageDataRepo(stLang, stWord);
    }

    public LiveData<String > getLanguageLiveData() {
        return stLanguage;
    }

    // TODO: 9/14/2023 get distric id by village id
    private LiveData<VillageTable> villageTableLiveData;
    public void getDistricIDFromVillageTableDetailsByVillageId(String strVillageId) {
        try {
            villageTableLiveData = appRepository.getDisIdFromVillageTableById(strVillageId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<VillageTable> getDissIdFromVillageTableLiveData() {
        return villageTableLiveData;
    }



    // TODO: 9/14/2023 get distric id by village id
    private LiveData<SubDistrict> subDistrictLiveData;
    public void getDistricIDFromSubDistricId(String strSubdistricId) {
        try {
            subDistrictLiveData = appRepository.getDisIdFromSubDistricTableById(strSubdistricId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<SubDistrict> getDissIdFromSubDistrictTableLiveData() {
        return subDistrictLiveData;
    }

    // TODO: 9/14/2023 distric details

    private LiveData<DistrictorRegency> districtorRegencyLiveData;
    public void getDistricDetailsFromLocalDBById(Integer strDisId) {
        try {
            districtorRegencyLiveData = appRepository.getDistricDetailsByIdFromLocalDb(strDisId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public LiveData<DistrictorRegency> getDistrictorRegencyLiveDataLiveData() {
        return districtorRegencyLiveData;
    }

    private LiveData<StateorProvince> stateOfProvinceLiveData;
    public void getStateorProvinceDetailsFromLocalDBById(Integer strSateId) {
        try {
            stateOfProvinceLiveData = appRepository.getSateorProvinceDetailsFromLocalDb(strSateId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public LiveData<StateorProvince> getStateorProvinceLiveDataLiveData() {
        return stateOfProvinceLiveData;
    }



    private LiveData<Country> CountryLiveData;
    public void getCountryDetailsFromLocalDBById(Integer strCountryId) {
        try {
            CountryLiveData = appRepository.getCountryDetailsByIdFromLocalDb(strCountryId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public LiveData<Country> getCountryRegencyLiveDataLiveData() {
        return CountryLiveData;
    }

    //Plantation insert and get
    private LiveData<Plantation> plantationLiveData;

    public void insertPlantationDetailListTableLocal(Plantation plantation) {
        try {
            plantationLiveData = appRepository.insertPlantationDetailListTableTable(plantation);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<Plantation> getPlantationDetailListTableLiveDataInsertLiveDataFromLocalDB() {
        return plantationLiveData;
    }


    //PlantationDoc insert and get
    private LiveData<PlantationDocuments> plantationDocumentsLiveData;

    public void insertPlantationDocDetailListTableLocal(PlantationDocuments plantationDocuments) {
        try {
            plantationDocumentsLiveData = appRepository.insertPlantationDocDetailListTableTable(plantationDocuments);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<PlantationDocuments> getPlantationDocDetailListTableLiveDataInsertLiveDataFromLocalDB() {
        return plantationDocumentsLiveData;
    }

    //Plantation Geo insert and get
    private LiveData<PlantationGeoBoundaries> plantationGeoBoundariesLiveData;

    public void insertPlantationGeoDetailListTableLocal(PlantationGeoBoundaries plantationGeoBoundaries) {
        try {
            plantationGeoBoundariesLiveData = appRepository.insertPlantationGeoDetailListTableTable(plantationGeoBoundaries);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<PlantationGeoBoundaries> getPlantationGeoDetailListTableLiveDataInsertLiveDataFromLocalDB() {
        return plantationGeoBoundariesLiveData;
    }

    //Labour Survey
    private LiveData<PlantationLabourSurvey> plantationPlantationLabourSurveyLiveData;

    public void insertPlantationLabourSurveyListTableLocal(PlantationLabourSurvey plantationLabourSurvey) {
        try {
            plantationPlantationLabourSurveyLiveData = appRepository.insertPlantationLabourSurveyListTableTable(plantationLabourSurvey);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<PlantationLabourSurvey> getPlantationLabourSurveyListTableLiveDataInsertLiveDataFromLocalDB() {
        return plantationPlantationLabourSurveyLiveData;
    }

    //sync
    private LiveData<List<PlantationLabourSurvey>> labourListFromLocalDBLiveDataNotSync;

    public LiveData<List<PlantationLabourSurvey>> getLabourDetailsListNotSyncLiveData() {
        return labourListFromLocalDBLiveDataNotSync;
    }

    public void getLabourListFromLocalDBNotSync() {
        try {
            labourListFromLocalDBLiveDataNotSync = appRepository.getPlantationLabourSurveyDetailslistFromLocalDBNotSync();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }




    //FarmerHouseholdParentSurvey
    private LiveData<FarmerHouseholdParentSurvey> farmerHouseholdParentSurveyLiveData;

    public void insertFarmerHouseholdParentSurveyListTableLocal(FarmerHouseholdParentSurvey farmerHouseholdParentSurvey) {
        try {
            farmerHouseholdParentSurveyLiveData = appRepository.insertFarmerHouseholdParentSurveyListTableTable(farmerHouseholdParentSurvey);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<FarmerHouseholdParentSurvey> getFarmerHouseholdParentSurveyListTableLiveDataInsertLiveDataFromLocalDB() {
        return farmerHouseholdParentSurveyLiveData;
    }

    //FarmerHouseholdChildrenSurvey
    private LiveData<FarmerHouseholdChildrenSurvey> farmerHouseholdChildrenSurveyLiveData;

    public void insertFarmerHouseholdChildrenSurveyListTableLocal(FarmerHouseholdChildrenSurvey farmerHouseholdChildrenSurvey) {
        try {
            farmerHouseholdChildrenSurveyLiveData = appRepository.insertFarmerHouseholdChildrenSurveyListTableTable(farmerHouseholdChildrenSurvey);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<FarmerHouseholdChildrenSurvey> getFarmerHouseholdChildrenSurveyListTableLiveDataInsertLiveDataFromLocalDB() {
        return farmerHouseholdChildrenSurveyLiveData;
    }



    //Plantation added
    private LiveData<PlantationGeoBoundaries> geoBoundariesTableLiveDataInsert;
    public void insertGeoBoundariesvaluesIntolocalDB(PlantationGeoBoundaries geoBoundariesTable) {
        try {
            geoBoundariesTableLiveDataInsert = appRepository.insertGoeDataIntolocaDB(geoBoundariesTable);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //Plantation added
    public LiveData<PlantationGeoBoundaries> getGeoBoundariesTableLocalDB() {
        return geoBoundariesTableLiveDataInsert;
    }

    private LiveData<PlantationDocuments> documentSavingDataLocalDB;
    public void insertDoctable(PlantationDocuments docIdentiFicationDeatilsTable) {
        try {
            documentSavingDataLocalDB = appRepository.insertDocIntoLocalDB(docIdentiFicationDeatilsTable);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<PlantationDocuments> getDocDetailLsTableLiveDataFromLocalDB() {
        return documentSavingDataLocalDB;
    }

    //PlantationLabourSurvey
    private LiveData<PlantationLabourSurvey> plantationLabourSurveyDataLocalDB;
    public void insertPlantationLabourSurveyDataLocalDB(PlantationLabourSurvey plantationLabourSurvey) {
        try {
            plantationLabourSurveyDataLocalDB = appRepository.insertPlantationLabourSurveyIntoLocalDB(plantationLabourSurvey);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<PlantationLabourSurvey> getPlantationLabourSurveyDataLocalDBTableLiveDataFromLocalDB() {
        return plantationLabourSurveyDataLocalDB;
    }



    public LiveData<Integer> getNotSyncFarmerCountDataFromLocalDB() {
        return appRepository.getNotSyncFarmerCountDataFromLocalDB();

    }

    //Survey
    public LiveData<Integer> getNotSyncSurveyCountDataFromLocalDB() {
        return appRepository.getNotSyncSurveyCountDataFromLocalDB();

    }

    //Plantation Count
    public LiveData<Integer> getNotSyncPlantationCountDataFromLocalDB() {
        return appRepository.getNotSyncPlantationCountDataFromLocalDB();

    }

    public LiveData<Integer> getNotSyncDocCountDataFromLocalDB() {
        return appRepository.getNotSyncDocCountDataFromLocalDB();

    }


    public LiveData<Integer> getNotSyncGeoCountDataFromLocalDB() {
        return appRepository.getNotSyncGeoCountDataFromLocalDB();

    }


    //Village by pin-code
    private LiveData<List<VillageTable>> villageDetailsByPincode;
    public void getVillageDetailsListFromLocalDB(String pincode) {
        try {
            villageDetailsByPincode = appRepository.getVillageTableDetailsFromLocalDbByPincode(pincode);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<List<VillageTable>> getvillageDetailsByPincodeLiveData() {
        return villageDetailsByPincode;
    }

    public void getAllVillageDetailsListFromLocalDB() {
        try {
            villageDetailsByPincode = appRepository.getAllVillageTableDetailsFromLocalDbByPincode();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Village by Id
    public void getVillageDetailsListFromLocalDBbyId(String id) {
        try {
            villageDetailsByPincode = appRepository.getVillageTableDetailsFromLocalDbById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    //Sub District
    private LiveData<List<SubDistrict>> subDistrictDetailsById;
    public void getSubDistrictDetailsListFromLocalDB(String id) {
        try {
            subDistrictDetailsById = appRepository.getSubDistrictTableDetailsFromLocalDbById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public LiveData<List<SubDistrict>> getSubDistrictDetailsByIdLiveData() {
        return subDistrictDetailsById;
    }

    //District
    private LiveData<List<DistrictorRegency>> districtDetailsById;
    public void getDistrictDetailsListFromLocalDB(int id) {
        try {
            districtDetailsById = appRepository.getDistrictTableDetailsFromLocalDbById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public LiveData<List<DistrictorRegency>> getDistrictDetailsByIdLiveData() {
        return districtDetailsById;
    }

    //StateOrProvince
     private LiveData<List<StateorProvince>> stateOrProvinceDetailsById;
    public void getStateorProvinceDetailsListFromLocalDB(int id) {
        try {
            stateOrProvinceDetailsById = appRepository.getStateTableDetailsFromLocalDbById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public LiveData<List<StateorProvince>> getStateOrProvinceDetailsByIdLiveData() {
        return stateOrProvinceDetailsById;
    }

    //Country
    private LiveData<List<Country>> countryDetailsById;
    public void getCountryDetailsListFromLocalDB() {
        try {
            countryDetailsById = appRepository.getCountryTableDetailsFromLocalDbById();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<List<Country>> getCountryDetailsByIdLiveData() {
        return countryDetailsById;
    }

    //App Language HDR
    private LiveData<List<AppLanguageHDRTable>> appLanguageLiveData;
    public void getAllLanguagesFromHDR() {
        try {
            appLanguageLiveData = appRepository.getAllLanguagesFromHDR();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<List<AppLanguageHDRTable>> getAllLanguagesFromHDRLiveData() {
        return appLanguageLiveData;
    }

    //Manufacturer Master list
    private LiveData<List<ManufacturerMaster>> manufacturerMasterDetails;
    public void getManufacturerMasterDetailsListFromLocalDB() {
        try {
            manufacturerMasterDetails = appRepository.getManufacturerMasterTableDetailsFromLocalDb();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public LiveData<List<ManufacturerMaster>> getManufacturerMasterDetailsLiveData() {
        return manufacturerMasterDetails;
    }

    //Dealer Master list
    private LiveData<List<DealerMaster>> dealerMasterDetails;
    public void getDealerMasterDetailsListFromLocalDB() {
        try {
            dealerMasterDetails = appRepository.getDealerMasterTableDetailsFromLocalDb();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public LiveData<List<DealerMaster>> getDealerMasterDetailsLiveData() {
        return dealerMasterDetails;
    }

    //Plantation by fid
    private LiveData<List<Plantation>> plantationDetailsById;
    public void getPlantationDetailsFromLocalDbById(String strFarmerCode) {
        try {
            plantationDetailsById = appRepository.getPlantationDetailsFromLocalDbById(strFarmerCode);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<List<Plantation>> getPlantationDetailsByIdLiveData() {
        return plantationDetailsById;
    }
    //For Lab stat
    public void getPlantationDetailsFromLocalDbByIdAndStatus(String id) {
        try {
            plantationDetailsById = appRepository.getPlantationDetailsFromLocalDbByIdAndStatus(id,"false");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //for lab adpt
    public void getPlantationDetailsFromLocalDbBymId(String id) {
        try {
            plantationDetailsById = appRepository.getPlantationDetailsFromLocalDbBymId(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Labour by fid
    private LiveData<List<PlantationLabourSurvey>> labourSurveyDetailsByFId;
    public void getPlantationLabourSurveyDetailsFromLocalDbById(String id) {
        try {
            labourSurveyDetailsByFId = appRepository.getPlantationLabourSurveyDetailsFromLocalDbById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public LiveData<List<PlantationLabourSurvey>> getPlantationLabourSurveyDetailsByIdLiveData() {
        return labourSurveyDetailsByFId;
    }




    //Farmer Main
    private LiveData<List<FarmersTable>> farmerListFromLocalDBLiveDataNotSync;

    public LiveData<List<FarmersTable>> getFarmerDetailsListNotSyncLiveData() {
        return farmerListFromLocalDBLiveDataNotSync;
    }

    public void getFarmerListFromLocalDBNotSync() {
        try {
            farmerListFromLocalDBLiveDataNotSync = appRepository.getFarmerDetailslistFromLocalDBNotSync();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Plantation not sync list
    private LiveData<List<Plantation>> plantationListFromLocalDBLiveDataNotSync;

    public LiveData<List<Plantation>> getPlantationDetailsListNotSyncLiveData() {
        return plantationListFromLocalDBLiveDataNotSync;
    }

    //Plantation not sync list
    public void getPlantationListFromLocalDBNotSync() {
        try {
            plantationListFromLocalDBLiveDataNotSync = appRepository.getPlantaionDetailslistFromLocalDBNotSync();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    //Farmer Main
    private LiveData<String> farmerLiveData;
    public LiveData<String> getFarmerLiveData() {
        return farmerLiveData;
    }

    public void syncFarmerDetailsDataToServer(FarmersTable farmerTable) {
        try {
                farmerLiveData = appRepository.syncFarmerDetailsDataToServer(farmerTable);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    //Plantation to server Main
    private LiveData<String> plantationsLiveData;
    public LiveData<String> getPlantationLiveData() {
        return plantationsLiveData;
    }

    public void syncPlantationDataToServer(Plantation plantation) {
        try {
            plantationsLiveData = appRepository.syncPlantationDetailsDataToServer(plantation);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private LiveData<String> stringLiveData;
    public LiveData<String> getStringLiveData() {
        return stringLiveData;
    }

    public void syncGeoBoundariesDetailsSubmitTableDataToServer(PlantationGeoBoundaries geoBoundariesTable,int index) {
        try {
            stringLiveData = appRepository.syncGeoBoundariesDetailsDataToServer(geoBoundariesTable,index);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Survey
    public void syncSurveyDetailsDataToServer(PlantationLabourSurvey plantationLabourSurvey) {
        try {
            farmerLiveData = appRepository.syncSurveyDetailsDataToServer(plantationLabourSurvey);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private LiveData<List<PlantationDocuments>> docIdentificationDetailsSubmitTableLiveData;

    public LiveData<List<PlantationDocuments>> getDocIdentiFicationDeatilsTableFromLocalLiveData() {
        return docIdentificationDetailsSubmitTableLiveData;
    }

    public void getLocalDocIdentificationFromLocalDB() {
        try {
            docIdentificationDetailsSubmitTableLiveData = appRepository.getDocIdentiFicationDeatilsTableFromLocalDb();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Doc by fid
    public void getLocalDocIdentificationFromLocalDBByFidandDtype(String fid,String dty) {
        try {
            docIdentificationDetailsSubmitTableLiveData = appRepository.getDocIdentiFicationDeatilsTableFromLocalDbByFidWDoc(fid,dty);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    //PlantationGeoBoundaries
    private LiveData<List<PlantationGeoBoundaries>> geoBoundariesTableFromLocalDbLiveData;

    public LiveData<List<PlantationGeoBoundaries>> getGeoBoundariesFromLocalLiveData() {
        return geoBoundariesTableFromLocalDbLiveData;
    }

    //Geo by plot desc for categ
    public void getGeoBoudariesFromLocalDB(String plot) {
        try {
            geoBoundariesTableFromLocalDbLiveData = appRepository.getPlantationGeoFromLocalDb(plot);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //PlantationGeoBoundaries
    public void getGeoBoudariesFromLocalDB() {
        try {
            geoBoundariesTableFromLocalDbLiveData = appRepository.getGeoBoundariesTableListFromLocalDB();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    //Todo : Sync Geo Main
//    public void syncGeoBoundariesDetailsSubmitTableDataToServer(GeoBoundariesTable geoBoundariesTable,int index) {
//        try {
//            farmerLiveData = appRepository.syncGeoBoundariesDetailsDataToServer(geoBoundariesTable,index);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }


    //Get Farmer
    private LiveData<List<FarmersTable>> farmerListFromLocalDBLiveData;
    public LiveData<List<FarmersTable>> getFarmerDetailsListLiveData() {
        return farmerListFromLocalDBLiveData;
    }

    public void getFarmerListFromLocalDBStatus() {
        try {
            farmerListFromLocalDBLiveData = appRepository.getFarmerDetailslistFromLocalDB();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //updatePlotDetailListTableSyncAndPlotArea1
    public void updatePlotDetailListTableSyncAndPlotArea1(boolean mSync,String sSync,double area,String id){
        try {
            plantationLiveData=appRepository.updatePlotDetailListTableSyncAndPlotArea1(mSync,sSync,area,id);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }



    private LiveData<FarmersTable> farmersTableLiveDataUpdate;

    public void updateVillageIdInFarmerTable(String strAddress,String strVillageId,String strFarmerCode){
        try {
            farmersTableLiveDataUpdate=appRepository.updateVillageIDForFarmerTable(strAddress,strVillageId,strFarmerCode);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public LiveData<FarmersTable> getFarmersTableLiveDataForUpdateVillageId() {
        return farmersTableLiveDataUpdate;
    }
    //updatePlatDetailListTableForLabStatus
    public void updatePlatDetailListTableForLabStatus(String serSync,boolean sync,String labStatus,String upDate,String upId,String pid){
        try {
            plantationLiveData=appRepository.updatePlatDetailListTableForLabStatus(serSync,sync,labStatus,upDate,upId,pid);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //Plant Doc
    private LiveData<String> docLiveData;
    public LiveData<String> getDocLiveData() {
        return docLiveData;
    }

    public void syncDocIdentifcationDetailsDataToServer(PlantationDocuments docIdentiFicationDeatilsTable) {
        try {
            docLiveData = appRepository.syncDocIdentificationDetailsDataToServer(docIdentiFicationDeatilsTable);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    //Parent survey
    private LiveData<FarmerHouseholdParentSurvey> farmerHouseholdParentSurveyDataLocalDB;
    public void insertFarmerHouseholdParentSurveyDataLocalDB(FarmerHouseholdParentSurvey farmerHouseholdParentSurvey) {
        try {
            farmerHouseholdParentSurveyDataLocalDB = appRepository.insertFarmerHouseholdParentSurveyIntoLocalDB(farmerHouseholdParentSurvey);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<FarmerHouseholdParentSurvey> getFarmerHouseholdParentSurveyDataLocalDBTableLiveDataFromLocalDB() {
        return farmerHouseholdParentSurveyDataLocalDB;
    }

    //parent count
    public LiveData<Integer> getNotSyncFarmerHouseholdParentSurveyCountDataFromLocalDB() {
        return appRepository.getNotFarmerHouseholdParentSurveyCountDataFromLocalDB();

    }


    //getting list
    private LiveData<List<FarmerHouseholdParentSurvey>> farmerHouseholdParentSurveyDetailsById;

    public void getFarmerHouseholdParentSurveyDetailsFromLocalDbById(String id) {
        try {
            farmerHouseholdParentSurveyDetailsById = appRepository.getFarmerHouseholdParentSurveyDetailsFromLocalDbById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<List<FarmerHouseholdParentSurvey>> getFarmerHouseholdParentSurveyDetailsByIdLiveData() {
        return farmerHouseholdParentSurveyDetailsById;
    }

    //Not sync list
    public void getParentListFromLocalDBNotSync() {
        try {
            farmerHouseholdParentSurveyDetailsById = appRepository.getFarmerHouseholdParentSurveyDetailslistFromLocalDBNotSync();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Parent Survey
    public void syncParentSurveyDetailsDataToServer(FarmerHouseholdParentSurvey farmerHouseholdParentSurvey) {
        try {
            farmerLiveData = appRepository.syncParentSurveyDetailsDataToServer(farmerHouseholdParentSurvey);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }











    //Child survey
    private LiveData<FarmerHouseholdChildrenSurvey> farmerHouseholdChildrenSurveyDataLocalDB;
    public void insertFarmerHouseholdChildrenSurveyDataLocalDB(FarmerHouseholdChildrenSurvey farmerHouseholdChildrenSurvey) {
        try {
            farmerHouseholdChildrenSurveyDataLocalDB = appRepository.insertFarmerHouseholdChildrenSurveyIntoLocalDB(farmerHouseholdChildrenSurvey);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<FarmerHouseholdChildrenSurvey> getFarmerHouseholdChildrenSurveyDataLocalDBTableLiveDataFromLocalDB() {
        return farmerHouseholdChildrenSurveyDataLocalDB;
    }


    //child count
    public LiveData<Integer> getNotSyncFarmerHouseholdChildrenSurveyCountDataFromLocalDB() {
        return appRepository.getFarmerHouseholdChildrenSurveyNotSyncCountFromLocalDB();

    }


    //getting list
    private LiveData<List<FarmerHouseholdChildrenSurvey>> farmerHouseholdChildrenSurveyDetailsById;

    public void getFarmerHouseholdChildrenSurveyDetailsFromLocalDbById(String id) {
        try {
            farmerHouseholdChildrenSurveyDetailsById = appRepository.getFarmerHouseholdChildrenSurveyDetailsFromLocalDbById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<List<FarmerHouseholdChildrenSurvey>> getFarmerHouseholdChildrenSurveyDetailsByIdLiveData() {
        return farmerHouseholdChildrenSurveyDetailsById;
    }

    //Not sync list
    public void getChildrenListFromLocalDBNotSync() {
        try {
            farmerHouseholdChildrenSurveyDetailsById = appRepository.getFarmerHouseholdChildrenSurveyDetailslistFromLocalDBNotSync();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //child Survey
    public void syncChildrenSurveyDetailsDataToServer(FarmerHouseholdChildrenSurvey farmerHouseholdChildrenSurvey) {
        try {
            farmerLiveData = appRepository.syncChildrenSurveyDetailsDataToServer(farmerHouseholdChildrenSurvey);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    //Risk
    private LiveData<RiskAssessment> riskAssessmentDataLocalDB;
    public void insertRiskAssessmentDataLocalDB(RiskAssessment riskAssessment) {
        try {
            riskAssessmentDataLocalDB = appRepository.insertRiskAssessmentIntoLocalDB(riskAssessment);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<RiskAssessment> getRiskAssessmentDataLocalDBTableLiveDataFromLocalDB() {
        return riskAssessmentDataLocalDB;
    }

    //Risk count
    public LiveData<Integer> getNotSyncRiskCountDataFromLocalDB() {
        return appRepository.getRiskNotSyncCountFromLocalDB();

    }

    //Risk list
    private LiveData<List<RiskAssessment>> riskAssessmentDetailsById;
    //Not sync list
    public void getRiskListFromLocalDBNotSync() {
        try {
            riskAssessmentDetailsById = appRepository.getRiskAssessmentDetailslistFromLocalDBNotSync();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<List<RiskAssessment>> getRiskAssessmentDetailsByIdLiveData() {
        return riskAssessmentDetailsById;
    }

    public void getRiskDetailsFromLocalDbById(String id) {
        try {
            riskAssessmentDetailsById = appRepository.getRiskAssessmentDetailsFromLocalDbById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    //String live data sync
    private LiveData<String> riskLiveData;
    public LiveData<String> getRiskLiveDataLiveData() {
        return riskLiveData;
    }
    public void syncRiskDetailsDataToServer(RiskAssessment riskAssessment) {
        try {
            riskLiveData = appRepository.syncRiskDetailsDataToServer(riskAssessment);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    //Manfacturer Farmer
    private LiveData<ManfacturerFarmer> manfacturerFarmerDataLocalDB;
    public void insertManfacturerFarmerDataLocalDB(ManfacturerFarmer manfacturerFarmer) {
        try {
            manfacturerFarmerDataLocalDB = appRepository.insertManfacturerFarmerIntoLocalDB(manfacturerFarmer);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<ManfacturerFarmer> getManfacturerFarmerDataLocalDBTableLiveDataFromLocalDB() {
        return manfacturerFarmerDataLocalDB;
    }

    //manu count
    public LiveData<Integer> getNotSyncManufacturerCountDataFromLocalDB() {
        return appRepository.getManufacturerNotSyncCountFromLocalDB();

    }


    //Manu list
    private LiveData<List<ManfacturerFarmer>> manfacturerFarmerDetailsById;

    public LiveData<List<ManfacturerFarmer>> getManfacturerFarmerDetailsByIdLiveData() {
        return manfacturerFarmerDetailsById;
    }

    public void getManfacturerFarmerDetailsFromLocalDbByFId(String fid) {
        try {
            manfacturerFarmerDetailsById = appRepository.getManfacturerFarmerDetailsFromLocalDbById(fid);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Not sync list
    public void getManufacturerListFromLocalDBNotSync() {
        try {
            manfacturerFarmerDetailsById = appRepository.getManfacturerFarmerDetailslistFromLocalDBNotSync();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //String live data sync
    private LiveData<String> manuLiveData;
    public LiveData<String> getManufacturerLiveDataLiveData() {
        return manuLiveData;
    }
    public void syncManufacturerDetailsDataToServer(ManfacturerFarmer manfacturerFarmer) {
        try {
            manuLiveData = appRepository.syncManufacturerDetailsDataToServer(manfacturerFarmer);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    //Dealer Farmer
    private LiveData<DealerFarmer> dealerFarmerDataLocalDB;
    public void insertDealerFarmerDataLocalDB(DealerFarmer dealerFarmer) {
        try {
            dealerFarmerDataLocalDB = appRepository.insertDealerFarmerIntoLocalDB(dealerFarmer);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<DealerFarmer> getDealerFarmerDataLocalDBTableLiveDataFromLocalDB() {
        return dealerFarmerDataLocalDB;
    }

    //dealer count
    public LiveData<Integer> getNotSyncDealerCountDataFromLocalDB() {
        return appRepository.getDealerNotSyncCountFromLocalDB();

    }

    //Dealer Farmer list
    private LiveData<List<DealerFarmer>> dealerFarmerDetailsById;

    public LiveData<List<DealerFarmer>> getDealerFarmerDetailsByIdLiveData() {
        return dealerFarmerDetailsById;
    }

    public void getDealerFarmerDetailsFromLocalDbByFId(String fid) {
        try {
            dealerFarmerDetailsById = appRepository.getDealerFarmerDetailsFromLocalDbById(fid);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Not sync list
    public void getDealerListFromLocalDBNotSync() {
        try {
            dealerFarmerDetailsById = appRepository.getDealerFarmerDetailslistFromLocalDBNotSync();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //String live data sync
    private LiveData<String> dealerLiveData;
    public LiveData<String> getDealerLiveDataLiveData() {
        return dealerLiveData;
    }
    public void syncDealerDetailsDataToServer(DealerFarmer dealerFarmer) {
        try {
            dealerLiveData = appRepository.syncDealerDetailsDataToServer(dealerFarmer);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public LiveData<Integer> getPlotListCountWhichNotSyncByStrFarmerCode(String strFarmerCode) {
        return appRepository.getPlotsCountWhichAreNotSyncByFarmercode(strFarmerCode);
    }

}




