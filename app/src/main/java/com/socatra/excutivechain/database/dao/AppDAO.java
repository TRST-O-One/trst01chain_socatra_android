package com.socatra.excutivechain.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

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

import java.util.List;

@Dao
public abstract class AppDAO {

    @Query("select *  from RefreshTableDateCheck where  DeviceID=:deviceID and Date=:date order by RefreshID asc")
    public abstract RefreshTableDateCheck getRefreshTableDateCheckDetailsFromLocalDB(String deviceID, String date);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertRefreshTableDateCheckTable(RefreshTableDateCheck refreshTableDateCheck);

    @Query("select *  from RefreshTableDateCheck where  DeviceID=:deviceID ORDER BY RefreshID asc LIMIT 1")
    public abstract RefreshTableDateCheck getTopRefreshTableDateCheckData(String deviceID);


    //Country
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertMasterSyncCountryListIntoLocalDB(Country country);

    //StateorProvince
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertMasterSyncStateListIntoLocalDB(StateorProvince stateorProvince);

    //DistrictorRegency
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertMasterSyncDistrictListIntoLocalDB(DistrictorRegency districtorRegency);

    //SubDistrict
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertMasterSyncSubDistrictListIntoLocalDB(SubDistrict subDistrict);


    //Village
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertMasterSyncvillageListIntoLocalDB(VillageTable villageTable);

    //Risk Question Master
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertMasterSyncRiskAssessmentQuestionListIntoLocalDB(RiskAssessmentQuestion riskAssessmentQuestion);

    //Manufacturer Master
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertMasterSyncManufacturerMasterListIntoLocalDB(ManufacturerMaster manufacturerMaster);

    //Risk Question Master
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertMasterSyncDealerMasterListIntoLocalDB(DealerMaster dealerMaster);

    //App Language
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertMasterSyncLanguageMasterListIntoLocalDB(AppLanguageTable appLanguageTable);

    //App Language
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertMasterSyncLanguageHDRMasterListIntoLocalDB(AppLanguageHDRTable appLanguageTable);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertGeoBoundariesTable(PlantationGeoBoundaries currentVisitFarmerTables);

    // remove master details
    @Transaction
    public void deleteAllTablesFromLocalMaster() {
        // Anything inside this method runs in a single transaction.

        deleteVillageTableSurveyTable();
        deleteCountryTableSurveyTable();
        deleteStateTableSurveyTable();
        deleteDistrictTableSurveyTable();
        deleteSubDistrictTableSurveyTable();
        deleteRiskAssessmentQuestionTableSurveyTable();
        deleteManufacturerMasterTableSurveyTable();
        deleteDealerMasterTableSurveyTable();
        deleteLanguageMasterTableSurveyTable();
        deleteLanguageHDRMasterTableSurveyTable();

        deleteRefreshTableDateCheck();
    }

    //Country
    @Query("DELETE FROM Country")
    public abstract void deleteCountryTableSurveyTable();

    //StateorProvince
    @Query("DELETE FROM StateorProvince")
    public abstract void deleteStateTableSurveyTable();

    //DistrictorRegency
    @Query("DELETE FROM DistrictorRegency")
    public abstract void deleteDistrictTableSurveyTable();


    //SubDistrict
    @Query("DELETE FROM SubDistrict")
    public abstract void deleteSubDistrictTableSurveyTable();

    //Village
    @Query("DELETE FROM VillageTable")
    public abstract void deleteVillageTableSurveyTable();

    //Risk Question Master
    @Query("DELETE FROM RiskAssessmentQuestion")
    public abstract void deleteRiskAssessmentQuestionTableSurveyTable();

    //Manufacturer Master
    @Query("DELETE FROM ManufacturerMaster")
    public abstract void deleteManufacturerMasterTableSurveyTable();

    //Dealer Master
    @Query("DELETE FROM DealerMaster")
    public abstract void deleteDealerMasterTableSurveyTable();

    //App Language
    @Query("DELETE FROM AppLanguageTable")
    public abstract void deleteLanguageMasterTableSurveyTable();

    //App Language
    @Query("DELETE FROM AppLanguageHDRTable")
    public abstract void deleteLanguageHDRMasterTableSurveyTable();


    @Query("DELETE FROM RefreshTableDateCheck")
    public abstract void deleteRefreshTableDateCheck();

    //Todo : Remove synced transaction data
    @Transaction
    public void deleteTablesFromLocalTransactionData(String syncValue) {
        // Anything inside this method runs in a single transaction.
        //New Main
        deleteSyncFarmerDetailListTable(syncValue);
        deleteSyncPlantationDetailListTable(syncValue);
        deleteSyncPlantationDocDetailListTable(syncValue);
        deleteSyncPlantationGeoDetailListTable(syncValue);
        deleteSyncPlantationLabourSurveyListTable(syncValue);
        deleteSyncFarmerHouseholdParentSurveyListTable(syncValue);
        deleteSyncFarmerHouseholdChildrenSurveyListTable(syncValue);
        deleteSyncRiskAssessmentListTable(syncValue);
        deleteSyncManfacturerFarmerListTable(syncValue);
        deleteSyncDealerFarmerListTable(syncValue);

    }

    //farmer main
    @Query("delete from FarmersTable where ServerSync=:syncStatus")
    public abstract void deleteSyncFarmerDetailListTable(String syncStatus);

    //Plantation delete
    @Query("delete from Plantation where ServerSync=:syncStatus")
    public abstract void deleteSyncPlantationDetailListTable(String syncStatus);

    //Plantation Doc delete
    @Query("delete from PlantationDocuments where ServerSync=:syncStatus")
    public abstract void deleteSyncPlantationDocDetailListTable(String syncStatus);

    //Plantation Geo delete
    @Query("delete from PLANTATIONGEOBOUNDARIES where ServerSync=:syncStatus")
    public abstract void deleteSyncPlantationGeoDetailListTable(String syncStatus);

    //PlantationLabourSurvey
    @Query("delete from PlantationLabourSurvey where ServerSync=:syncStatus")
    public abstract void deleteSyncPlantationLabourSurveyListTable(String syncStatus);

    //FARMERHOUSEHOLDPARENTSURVEY
    @Query("delete from FarmerHouseholdParentSurvey where ServerSync=:syncStatus")
    public abstract void deleteSyncFarmerHouseholdParentSurveyListTable(String syncStatus);

    //FarmerHouseholdChildrenSurvey
    @Query("delete from FarmerHouseholdChildrenSurvey where ServerSync=:syncStatus")
    public abstract void deleteSyncFarmerHouseholdChildrenSurveyListTable(String syncStatus);

    //RiskAssessment
    @Query("delete from RiskAssessment where ServerSync=:syncStatus")
    public abstract void deleteSyncRiskAssessmentListTable(String syncStatus);

    //ManfacturerFarmer
    @Query("delete from ManfacturerFarmer where ServerSync=:syncStatus")
    public abstract void deleteSyncManfacturerFarmerListTable(String syncStatus);

    //ManfacturerFarmer
    @Query("delete from DealerFarmer where ServerSync=:syncStatus")
    public abstract void deleteSyncDealerFarmerListTable(String syncStatus);


    //Farmer Main
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertFarmerDetailListTableLocalDB(FarmersTable farmerTable);

    @Query("update FarmersTable set Address= :strAdd,VillageId= :strVillageId  where FarmerCode= :strFarmerCode")
    public abstract void  updateVillageIdFarmerTable(String strAdd,String strVillageId,String strFarmerCode);
    //Farmer Main
    @Query("select *  from FarmersTable where FarmerCode=:farmerCode ORDER BY FarmerId DESC LIMIT 1")
    public abstract FarmersTable geInsertFarmerDetailListTableData(String farmerCode);

    @Query("select *  from FarmersTable where  FarmerCode=:farmerCode ORDER BY FarmerId asc LIMIT 1")
    public abstract FarmersTable getFarmerDetailsByFarmerCode(String farmerCode);

    @Query("select *  from VillageTable where  Id=:villageId ORDER BY villageId asc LIMIT 1")
    public abstract VillageTable getSubDisIdFromVillageTableById(String villageId);


    // TODO: 9/14/2023 new added

    @Query("select *  from SubDistrict where  Id=:subDistricId ORDER BY SubDistrictId asc LIMIT 1")
    public abstract SubDistrict getDisIdFromSubDistricTableById(String subDistricId);

    //App Language
    @Query("select ConvertedWord  from AppLanguageTable where SelectedLang =:stLang and SelectedWord =:stWord")
    public abstract String getLanguageData(String stLang,String stWord);

    @Query("select *  from DistrictorRegency where  Id=:disId ORDER BY DistrictId asc LIMIT 1")
    public abstract DistrictorRegency getDistricDetailsFromLocalDb(Integer disId);


    @Query("select *  from StateorProvince where  Id=:stateId ORDER BY StateId asc LIMIT 1")
    public abstract StateorProvince getStateorProcinceDetailsFromLocalDb(Integer stateId);

    @Query("select *  from Country where  Id=:countryId ORDER BY CountryId asc LIMIT 1")
    public abstract Country getCountryDetailsFromLocalDb(Integer countryId);

    //Plantation
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertPlantationDetailListTableLocalDB(Plantation plantation);
    //Plantation
    @Query("select *  from Plantation where PlotCode=:plotCode ORDER BY PlotId DESC LIMIT 1")
    public abstract Plantation getInsertPlotDetailListTableData(String plotCode);

    //Plantation Doc
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertPlantationDocDetailListTableLocalDB(PlantationDocuments plantationDocuments);
    //Plantation Doc
    @Query("select *  from PlantationDocuments where PlotCode=:plotCode ORDER BY ID DESC LIMIT 1")
    public abstract PlantationDocuments getInsertPlantDocDetailListTableData(String plotCode);

    //Plantation Geo
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertPlantationGeoDetailListTableLocalDB(PlantationGeoBoundaries plantationGeoBoundaries);
    //Plantation Geo
    @Query("select *  from PlantationGeoBoundaries where FarmerCode=:farmerCode ORDER BY ID DESC LIMIT 1")
    public abstract PlantationGeoBoundaries getInsertPlantGeoDetailListTableData(String farmerCode);

    //Labour Survey
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertPlantationLabourSurveyListTableLocalDB(PlantationLabourSurvey plantationLabourSurvey);

    //Farmer Household Parent Survey
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertFarmerHouseholdParentSurveyListTableLocalDB(FarmerHouseholdParentSurvey farmerHouseholdParentSurvey);

    //Child Survey
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertFarmerHouseholdChildrenSurveyListTableLocalDB(FarmerHouseholdChildrenSurvey farmerHouseholdChildrenSurvey);


    //Plantation added
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertGeoData(PlantationGeoBoundaries geoBoundariesTable);
    //Plantation added
    @Query("select *  from PlantationGeoBoundaries where  PlotCode=:plotNum ORDER BY ID DESC LIMIT 1")
    public abstract PlantationGeoBoundaries getTopGeoBoundariesTableFromLocalDb(String plotNum);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertDocDetailListTable(PlantationDocuments docIdentiFicationDeatilsTable);

    @Query("select *  from PlantationDocuments where FarmerCode=:farmerCode   ORDER BY ID DESC LIMIT 1")
    public abstract PlantationDocuments getTopDocListTableDataFromLocal(String farmerCode);

    //PlantationLabourSurvey
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertPlantationLabourSurveyDetailListTable(PlantationLabourSurvey plantationLabourSurvey);

    //Risk Assessment
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertRiskAssessmentDetailListTable(RiskAssessment riskAssessment);

    //Manfacturer Farmer
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertManfacturerFarmerDetailListTable(ManfacturerFarmer manfacturerFarmer);

    //Dealer Farmer
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertDealerFarmerDetailListTable(DealerFarmer dealerFarmer);

    //Todo:New Farmer count
    @Query("SELECT COUNT(*) FROM FarmersTable where ServerSync=:notSyncValue")
    public abstract LiveData<Integer> getFarmerNotSyncCountFromLocalDB(String notSyncValue);

    //Survey
    @Query("SELECT COUNT(*) FROM PLANTATIONLABOURSURVEY where ServerSync=:notSyncValue")
    public abstract LiveData<Integer> getSurveyNotSyncCountFromLocalDB(String notSyncValue);


    //Plantation count
    @Query("SELECT COUNT(*) FROM Plantation where ServerSync=:notSyncValue")
    public abstract LiveData<Integer> getPlantationNotSyncCountFromLocalDB(String notSyncValue);

    @Query("SELECT COUNT(*) FROM PlantationDocuments where ServerSync=:notSyncValue")
    public abstract LiveData<Integer> getDocNotSyncCountFromLocalDB(String notSyncValue);

    @Query("SELECT COUNT(*) FROM PlantationGeoBoundaries where ServerSync=:notSyncValue")
    public abstract LiveData<Integer> getGeoNotSyncCountFromLocalDB(String notSyncValue);

    //Village by pin-code
    @Query("SELECT * FROM VillageTable where PinCode=:pincode order by villageId asc")
    public abstract List<VillageTable> getVillageDetailsFromLocalDbByPincode(String pincode);

    //Village by sub dist
    @Query("SELECT * FROM VillageTable where SubDistrictId=:pincode order by villageId asc")
    public abstract List<VillageTable> getVillageDetailsFromLocalDbBySubDist(String pincode);

    @Query("SELECT * FROM VillageTable order by villageId asc")
    public abstract List<VillageTable> getAllVillageDetailsFromLocalDbBySubDist();

    //Village by Id
    @Query("SELECT * FROM VillageTable where Id=:id order by villageId asc")
    public abstract List<VillageTable> getVillageDetailsFromLocalDbById(String id);

    //SubDistrict by id
    @Query("SELECT * FROM SubDistrict where Id=:id order by SubDistrictId asc")
    public abstract List<SubDistrict> getSubDistrictDetailsFromLocalDbById(String id);

    //SubDistrict by District id
    @Query("SELECT * FROM SubDistrict where DistrictId=:id order by SubDistrictId asc")
    public abstract List<SubDistrict> getSubDistrictDetailsFromLocalDbByDistrictId(String id);

    //DistrictorRegency by id
    @Query("SELECT * FROM DistrictorRegency where Id=:id order by DistrictId asc")
    public abstract List<DistrictorRegency> getDistrictDetailsFromLocalDbById(int id);

    //DistrictorRegency by stateid
    @Query("SELECT * FROM DistrictorRegency where StateId=:id order by DistrictId asc")
    public abstract List<DistrictorRegency> getDistrictDetailsFromLocalDbByStateId(int id);

    //StateorProvince by id
    @Query("SELECT * FROM StateorProvince where Id=:id order by StateId asc")
    public abstract List<StateorProvince> getStateDetailsFromLocalDbById(int id);

    //StateorProvince by id
    @Query("SELECT * FROM StateorProvince where CountryId=:id order by StateId asc")
    public abstract List<StateorProvince> getStateDetailsFromLocalDbByCountryId(int id);

    //Country by id
    @Query("SELECT * FROM Country where Id=:id order by CountryId asc")
    public abstract List<Country> getCountryDetailsFromLocalDbById(int id);

    @Query("SELECT * FROM Country order by CountryId asc")
    public abstract List<Country> getAllCountryDetailsFromLocalDbById();

    //App HDR Table GET
    @Query("SELECT * FROM AppLanguageHDRTable where IsActive=1 order by LanguageId asc")
    public abstract List<AppLanguageHDRTable> getAllLanguagesFromHDR();


    //Manufacturer Master list
    @Query("SELECT * FROM ManufacturerMaster order by Id asc")
    public abstract List<ManufacturerMaster> getAllManufacturerMasterDetailsFromLocalDbById();

    //Dealer Master list
    @Query("SELECT * FROM DealerMaster order by Id asc")
    public abstract List<DealerMaster> getAllDealerMasterDetailsFromLocalDbById();

    //Plantation list by farmer code
    @Query("SELECT * FROM Plantation where FarmerCode=:strFarmercode  ORDER BY PlotId desc")
    public abstract List<Plantation> getPlantationDetailsFromLocalDbById(String strFarmercode);

    //for lab adpt
    @Query("SELECT * FROM Plantation where PlotCode=:fcode order by PlotId desc")
    public abstract List<Plantation> getPlantationDetailsFromLocalDbBymId(String fcode);

    @Query("SELECT * FROM Plantation where FarmerCode=:fcode and LabourStatus=:lStat order by PlotId desc")
    public abstract List<Plantation> getPlantationDetailsFromLocalDbByIdAndStatus(String fcode,String lStat);

    //Plantation list by farmer code
    @Query("SELECT * FROM PlantationLabourSurvey where FarmerCode=:fcode order by Id desc")
    public abstract List<PlantationLabourSurvey> getPlantationLabourSurveyDetailsFromLocalDbById(String fcode);

    //Farmer Main
    @Query("SELECT * FROM FarmersTable where ServerSync =:value order by FarmerId desc")
    public abstract List<FarmersTable> getFarmerDetailsListFromLocalDBNotSync(String value);

    //Labour survey
    @Query("SELECT * FROM PlantationLabourSurvey where ServerSync =:value order by Id desc")
    public abstract List<PlantationLabourSurvey> getPlantationLabourSurveyDetailsListFromLocalDBNotSync(String value);

    //Plantation List not sync
    @Query("SELECT * FROM Plantation where ServerSync =:value order by PlotId desc")
    public abstract List<Plantation> getPlantationDetailsListFromLocalDBNotSync(String value);


    //Farmer Main
    @Query("select *  from FarmersTable where FarmerCode=:farmerCode ORDER BY FarmerId DESC LIMIT 1")
    public abstract FarmersTable getTopFarmerDetailListTableTableData(String farmerCode);

    //Survey
    @Query("select *  from PlantationLabourSurvey where FarmerCode=:farmerCode ORDER BY Id DESC LIMIT 1")
    public abstract PlantationLabourSurvey getTopLabouSurveyDetailListTableTableData(String farmerCode);

    //Plantation top
    @Query("select *  from Plantation where PlotCode=:plotCode ORDER BY PlotId DESC LIMIT 1")
    public abstract Plantation getTopPlantationListTableTableData(String plotCode);

    @Query("SELECT * FROM PlantationDocuments where ServerSync =:value order by ID desc")
    public abstract List<PlantationDocuments> getDocIdentiFicationDeatilsTableFromLocalDb(String value);


    @Query("SELECT * FROM PlantationDocuments where FarmerCode =:value and DocType=:dty order by ID desc")
    public abstract List<PlantationDocuments> getDocIdentiFicationDeatilsTableFromLocalDbByFidWDoc(String value,String dty);

    @Query("SELECT * FROM PlantationGeoBoundaries where PlotCode =:value order by ID DESC LIMIT 1")
    public abstract List<PlantationGeoBoundaries> getGeoBoundariesTableTablePlot(String value);


    //PlantationGeoBoundaries
    @Query("SELECT * FROM PlantationGeoBoundaries where ServerSync =:value order by ID asc")
    public abstract List<PlantationGeoBoundaries> getGeoBoundariesTableTableFromLocalDbList(String value);

    @Query("select *  from PlantationGeoBoundaries where  PlotCode=:plotNum ORDER BY ID DESC LIMIT 1")
    public abstract PlantationGeoBoundaries getGeoBoundariesTableData(String plotNum);

    @Query("SELECT * FROM FarmersTable order by FarmerId desc")
    public abstract List<FarmersTable> getFarmerDetailsListFromLocalDB();

    //Plantation Geo for cat
    @Query("SELECT * FROM PlantationGeoBoundaries where PlotCode=:plotCode order by ID desc")
    public abstract List<PlantationGeoBoundaries> getPlantGeoFromLocalDbBySubDist(String plotCode);

    //Plot 1 and sync
    @Query("update Plantation set sync= :mSync, ServerSync= :sSync , GeoboundariesArea= :area  where PlotCode= :pid")
    public abstract void updatePlotDetailListTableSyncAndPlotArea1(boolean mSync,String sSync,double area,String pid);

    //TODO : Plant update lab
    @Query("update Plantation set ServerSync= :serSync ,sync= :sync ,LabourStatus= :labStatus,UpdatedDate= :upDate ,UpdatedByUserId= :upId where PlotCode= :pid")
    public abstract void updatePlatDetailListTableForLabStatus(String serSync,boolean sync,String labStatus,String upDate,String upId,String pid);

    //FarmerHouseholdParentSurvey
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertFarmerHouseholdParentSurveyDetailListTable(FarmerHouseholdParentSurvey farmerHouseholdParentSurvey);

    @Query("SELECT COUNT(*) FROM FarmerHouseholdParentSurvey where ServerSync=:notSyncValue")
    public abstract LiveData<Integer> getFarmerHouseholdParentSurveyNotSyncCountFromLocalDB(String notSyncValue);

    //FarmerHouseholdParentSurvey list by farmer code
    @Query("SELECT * FROM FarmerHouseholdParentSurvey where FarmerCode=:fcode order by Id desc")
    public abstract List<FarmerHouseholdParentSurvey> getFarmerHouseholdParentSurveyDetailsFromLocalDbById(String fcode);

    //RiskAssessment list by farmer code
    @Query("SELECT * FROM RiskAssessment where FarmerCode=:fcode order by RiskId desc")
    public abstract List<RiskAssessment> getRiskAssessmentDetailsFromLocalDbById(String fcode);

    //ManfacturerFarmer list by farmer code
    @Query("SELECT * FROM ManfacturerFarmer where FarmerCode=:fcode order by ManfacturerId desc")
    public abstract List<ManfacturerFarmer> getManfacturerFarmerDetailsFromLocalDbById(String fcode);

    //DealerFarmer list by farmer code
    @Query("SELECT * FROM DealerFarmer where FarmerCode=:fcode order by DealerId desc")
    public abstract List<DealerFarmer> getDealerFarmerDetailsFromLocalDbById(String fcode);

    //Parent survey
    @Query("SELECT * FROM FarmerHouseholdParentSurvey where ServerSync =:value order by Id desc")
    public abstract List<FarmerHouseholdParentSurvey> getFarmerHouseholdParentSurveyDetailsListFromLocalDBNotSync(String value);

    //FarmerHouseholdParentSurvey
    @Query("select *  from FarmerHouseholdParentSurvey where FarmerCode=:farmerCode ORDER BY Id DESC LIMIT 1")
    public abstract FarmerHouseholdParentSurvey getTopFarmerHouseholdParentSurveyDetailListTableTableData(String farmerCode);


    //FarmerHouseholdChildrenSurvey
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertFarmerHouseholdChildrenSurveyDetailListTable(FarmerHouseholdChildrenSurvey farmerHouseholdChildrenSurvey);

    @Query("SELECT COUNT(*) FROM FarmerHouseholdChildrenSurvey where ServerSync=:notSyncValue")
    public abstract LiveData<Integer> getFarmerHouseholdChildrenSurveyNotSyncCountFromLocalDB(String notSyncValue);

    //Manfacturer Farmer
    @Query("SELECT COUNT(*) FROM ManfacturerFarmer where ServerSync=:notSyncValue")
    public abstract LiveData<Integer> getManufacturerNotSyncCountFromLocalDB(String notSyncValue);

    //Dealer Farmer
    @Query("SELECT COUNT(*) FROM DealerFarmer where ServerSync=:notSyncValue")
    public abstract LiveData<Integer> getDealerFarmerNotSyncCountFromLocalDB(String notSyncValue);

    //Risk count
    @Query("SELECT COUNT(*) FROM RiskAssessment where ServerSync=:notSyncValue")
    public abstract LiveData<Integer> getRiskNotSyncCountFromLocalDB(String notSyncValue);


    @Query("SELECT COUNT(*) FROM Plantation where FarmerCode=:strfarmerCode And ServerSync=:strNotSyncValue order by PlotId asc")
    public abstract LiveData<Integer> getPlantationCountForFarmerBasedOnFarmerCodeData(String strfarmerCode,String strNotSyncValue);


    @Query("SELECT COUNT(*) FROM Plantation where FarmerCode=:strfarmerCode order by PlotId asc")
    public abstract LiveData<Integer> getPlantationCountForFarmerBasedOnFarmerCode(String strfarmerCode);

//    @Query("SELECT COUNT(*) FROM FarmersTable where FarmerCode=:strfarmerCode order by FarmerCode asc")
//    public abstract LiveData<Integer> getNumberOFPlotsValues(String strfarmerCode);


    //    @Query("SELECT COUNT(*) FROM AddHarvestDetailsTable where LogBookNo =:logBookNumber  order by HarvestID desc")
//    public abstract LiveData<Integer> getHarvestDetailsDataCount(String logBookNumber)
    //Child list by farmer code
    @Query("SELECT * FROM FarmerHouseholdChildrenSurvey where FarmerCode=:fcode order by Id desc")
    public abstract List<FarmerHouseholdChildrenSurvey> getFarmerHouseholdChildrenSurveyDetailsFromLocalDbById(String fcode);

    //Child survey
    @Query("SELECT * FROM FarmerHouseholdChildrenSurvey where ServerSync =:value order by Id desc")
    public abstract List<FarmerHouseholdChildrenSurvey> getFarmerHouseholdChildrenSurveyDetailsListFromLocalDBNotSync(String value);

    //Child survey
    @Query("select *  from FarmerHouseholdChildrenSurvey where FarmerCode=:farmerCode ORDER BY Id DESC LIMIT 1")
    public abstract FarmerHouseholdChildrenSurvey getTopFarmerHouseholdChildrenSurveyDetailListTableTableData(String farmerCode);

    //RiskAssessment
    @Query("SELECT * FROM RiskAssessment where ServerSync =:value order by RiskId desc")
    public abstract List<RiskAssessment> getRiskAssessmentDetailsListFromLocalDBNotSync(String value);

    //Risk survey
    @Query("select *  from RiskAssessment where FarmerCode=:farmerCode ORDER BY RiskId DESC LIMIT 1")
    public abstract RiskAssessment getTopRiskAssessmentDetailListTableTableData(String farmerCode);

    //Manu
    @Query("SELECT * FROM ManfacturerFarmer where ServerSync =:value order by Id desc")
    public abstract List<ManfacturerFarmer> getManfacturerFarmerDetailsListFromLocalDBNotSync(String value);

    //Manu
    @Query("select *  from ManfacturerFarmer where FarmerCode=:farmerCode ORDER BY Id DESC LIMIT 1")
    public abstract ManfacturerFarmer getTopManfacturerFarmerDetailListTableTableData(String farmerCode);

    //DealerFarmer
    @Query("SELECT * FROM DealerFarmer where ServerSync =:value order by Id desc")
    public abstract List<DealerFarmer> getDetailsListFromLocalDBNotSync(String value);

    //Dealer
    @Query("select *  from DealerFarmer where FarmerCode=:farmerCode ORDER BY Id DESC LIMIT 1")
    public abstract DealerFarmer getTopDealerFarmerDetailListTableTableData(String farmerCode);




}
