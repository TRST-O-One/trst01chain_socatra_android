package com.socatra.intellitrack.database.dao;

import androidx.room.Dao;
import androidx.room.Transaction;

@Dao
public abstract class AppDAO {

    // remove master details
    @Transaction
    public void deleteAllTablesFromLocalMaster() {
        // Anything inside this method runs in a single transaction.

//        deleteVillageTableSurveyTable();
//        deleteCountryTableSurveyTable();
//        deleteStateTableSurveyTable();
//        deleteDistrictTableSurveyTable();
//        deleteSubDistrictTableSurveyTable();
//        deleteRiskAssessmentQuestionTableSurveyTable();
//
//        deleteRefreshTableDateCheck();
    }



    //Todo : Remove synced transaction data
    @Transaction
    public void deleteTablesFromLocalTransactionData(String syncValue) {
        // Anything inside this method runs in a single transaction.
        //New Main
//        deleteSyncFarmerDetailListTable(syncValue);
//        deleteSyncPlantationDetailListTable(syncValue);
//        deleteSyncPlantationDocDetailListTable(syncValue);
//        deleteSyncPlantationGeoDetailListTable(syncValue);
//        deleteSyncPlantationLabourSurveyListTable(syncValue);
//        deleteSyncFarmerHouseholdParentSurveyListTable(syncValue);
//        deleteSyncFarmerHouseholdChildrenSurveyListTable(syncValue);
//        deleteSyncRiskAssessmentListTable(syncValue);

    }






}
