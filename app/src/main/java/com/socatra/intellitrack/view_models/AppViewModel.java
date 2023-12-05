package com.socatra.intellitrack.view_models;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.socatra.intellitrack.database.entity.AddAllInvoiceDetailsSubmitTable;
import com.socatra.intellitrack.database.entity.AddBatchProcessingDtlDetailsSubmitTable;
import com.socatra.intellitrack.database.entity.AddBatchProcessingHdrDetailsSubmitTable;
import com.socatra.intellitrack.database.entity.AddGRNDetailsSubmitTable;
import com.socatra.intellitrack.database.entity.AddInvoiceDetailsSubmitTable;
import com.socatra.intellitrack.database.entity.GetFarmerListFromServerTable;
import com.socatra.intellitrack.database.entity.GetModeOfTransportDataFromServerTable;

import com.socatra.intellitrack.database.entity.UserLoginTable;
import com.socatra.intellitrack.models.LoginResponseDTO;
import com.socatra.intellitrack.models.post.PostDRCDtlDetailsDTO;
import com.socatra.intellitrack.models.post.PostDRCHdrDetailsDTO;
import com.socatra.intellitrack.models.post.PostQualityCheckDetails;
import com.socatra.intellitrack.repositories.AppRepository;

import java.util.List;

import javax.inject.Inject;

public class AppViewModel extends ViewModel {

    private AppRepository appRepository;
    private LiveData<String> stringLiveData;
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
    public void logInServiceList(UserLoginTable userLoginTable) {
        try {
            loginResponseDTOFromServerLiveData = appRepository.getlogInServiceResponse(userLoginTable);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // for clearing local db for master details


    // TODO: 8/24/2023 Getting Farmer list from server by using dealer Id

    private LiveData<List<GetFarmerListFromServerTable>> getFarmerDetailListTableLivedata;

    public void getFarmerDetailsListDataFromserver(String dealerID) {
        try {
            getFarmerDetailListTableLivedata = appRepository.getFarmerDetailsListFromServer(dealerID);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<List<GetFarmerListFromServerTable>> getFarmerDetailListTableLivedata() {
        return getFarmerDetailListTableLivedata;
    }


    // TODO: 8/24/2023 get mode of transport details from server

    private LiveData<List<GetModeOfTransportDataFromServerTable>> getModeOfTransportDataLivedata;

    public void getModeOfTransportDataFromserver(String dealerID) {
        try {
            getModeOfTransportDataLivedata = appRepository.getModeOfTransportDetailsFromServer(dealerID);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public LiveData<List<GetModeOfTransportDataFromServerTable>> getModeOfTransportDataListLivedata() {
        return getModeOfTransportDataLivedata;
    }

    // TODO: 8/24/2023 adding GRN Details to server
    public void syncGRNlListDataToServer(AddGRNDetailsSubmitTable addGRNDetailsSubmitTable, String typeReq) {
        try {
            stringLiveData = appRepository.syncGRNDetailsDataToServer(addGRNDetailsSubmitTable, typeReq);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void syncAddInvoiceDetailsDataToServer(AddInvoiceDetailsSubmitTable addInvoiceetailsSubmitTable) {
        try {
            stringLiveData = appRepository.syncInvoiceDetailsDataToServer(addInvoiceetailsSubmitTable);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void syncQualityCheckDataToServer(PostQualityCheckDetails PostQualityCheckDetailsSubmitTable, String typeReq) {
        try {
            stringLiveData = appRepository.syncQualityCheckDetailsDataToServer(PostQualityCheckDetailsSubmitTable, typeReq);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void syncAllAddInvoiceDetailsDataToServer(AddAllInvoiceDetailsSubmitTable addInvoiceetailsSubmitTable, String deviceRoleName) {
        try {
            if (deviceRoleName.equals("Dealer"))
            {
                stringLiveData = appRepository.syncAllInvoiceDetailsDataToServer(addInvoiceetailsSubmitTable,deviceRoleName);
            }else {
                stringLiveData = appRepository.syncProcessorInvoiceDetailsDataToServer(addInvoiceetailsSubmitTable,deviceRoleName);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }




    public void syncBatchProcessinglHdrListDataToServer(AddBatchProcessingHdrDetailsSubmitTable addBatchProcessingHdrSubmitTable, String typeReq) {
        try {
            stringLiveData = appRepository.syncBatchProcessingHdrDetailsDataToServer(addBatchProcessingHdrSubmitTable, typeReq);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void syncBatchProcessingDtlListDataT0Server(AddBatchProcessingDtlDetailsSubmitTable batchProcessingDtlDetailsArrayList) {
        try {
            stringLiveData = appRepository.syncBatchProcessingDtlDetailsDataToServer(batchProcessingDtlDetailsArrayList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public LiveData<String> getStringLiveData() {
        return stringLiveData;
    }


    //DRC
    public void syncDRClHdrListDataToServer(PostDRCHdrDetailsDTO postDRCHdrDetailsDTO) {
        try {
            stringLiveData = appRepository.syncDRCHdrDetailsDataToServer(postDRCHdrDetailsDTO);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Drc dtl
    public void syncDRCDtlListDataT0Server(PostDRCDtlDetailsDTO postDRCDtlDetailsDTO) {
        try {
            stringLiveData = appRepository.syncDRCDtlDetailsDataToServer(postDRCDtlDetailsDTO);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}




