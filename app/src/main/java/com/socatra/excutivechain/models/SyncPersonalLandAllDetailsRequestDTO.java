package com.socatra.excutivechain.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SyncPersonalLandAllDetailsRequestDTO {


    ArrayList<Farmer> Farmer = new ArrayList<Farmer>();

    ArrayList<Plantation> Plantation=new ArrayList<>();

    ArrayList<PlantationDocuments> PlantationDocuments=new ArrayList<>();

    ArrayList<PlantationGeoBoundaries> PlantationGeoBoundaries=new ArrayList<>();

    ArrayList<PlantationLabourSurvey> PlantationLabourSurvey=new ArrayList<>();

    ArrayList<FarmerHouseholdParentSurvey> FarmerHouseholdParentSurvey=new ArrayList<>();

    ArrayList<FarmerHouseholdChildrenSurvey> FarmerHouseholdChildrenSurvey=new ArrayList<>();

    ArrayList<RiskAssessment> RiskAssessment =new ArrayList<>();

    ArrayList<ProcessorFarmer> ProcessorFarmer =new ArrayList<>();//for gaja
    
//    ArrayList<ManfacturerFarmer> ManfacturerFarmer =new ArrayList<>();


    ArrayList<DealerFarmer> DealerFarmer =new ArrayList<>();


    //Getter and Setter
    public ArrayList<SyncPersonalLandAllDetailsRequestDTO.Farmer> getFarmer() {
        return Farmer;
    }

    public void setFarmer(ArrayList<SyncPersonalLandAllDetailsRequestDTO.Farmer> farmer) {
        Farmer = farmer;
    }

    public ArrayList<SyncPersonalLandAllDetailsRequestDTO.Plantation> getPlantation() {
        return Plantation;
    }

    public void setPlantation(ArrayList<SyncPersonalLandAllDetailsRequestDTO.Plantation> plantation) {
        Plantation = plantation;
    }

    public ArrayList<SyncPersonalLandAllDetailsRequestDTO.PlantationDocuments> getPlantationDocuments() {
        return PlantationDocuments;
    }

    public void setPlantationDocuments(ArrayList<SyncPersonalLandAllDetailsRequestDTO.PlantationDocuments> plantationDocuments) {
        PlantationDocuments = plantationDocuments;
    }

    public ArrayList<SyncPersonalLandAllDetailsRequestDTO.PlantationGeoBoundaries> getPlantationGeoBoundaries() {
        return PlantationGeoBoundaries;
    }

    public void setPlantationGeoBoundaries(ArrayList<SyncPersonalLandAllDetailsRequestDTO.PlantationGeoBoundaries> plantationGeoBoundaries) {
        PlantationGeoBoundaries = plantationGeoBoundaries;
    }

    public ArrayList<SyncPersonalLandAllDetailsRequestDTO.PlantationLabourSurvey> getPlantationLabourSurvey() {
        return PlantationLabourSurvey;
    }

    public void setPlantationLabourSurvey(ArrayList<SyncPersonalLandAllDetailsRequestDTO.PlantationLabourSurvey> plantationLabourSurvey) {
        PlantationLabourSurvey = plantationLabourSurvey;
    }

    public ArrayList<SyncPersonalLandAllDetailsRequestDTO.FarmerHouseholdParentSurvey> getFarmerHouseholdParentSurvey() {
        return FarmerHouseholdParentSurvey;
    }

    public void setFarmerHouseholdParentSurvey(ArrayList<SyncPersonalLandAllDetailsRequestDTO.FarmerHouseholdParentSurvey> farmerHouseholdParentSurvey) {
        FarmerHouseholdParentSurvey = farmerHouseholdParentSurvey;
    }

    public ArrayList<SyncPersonalLandAllDetailsRequestDTO.FarmerHouseholdChildrenSurvey> getFarmerHouseholdChildrenSurvey() {
        return FarmerHouseholdChildrenSurvey;
    }

    public void setFarmerHouseholdChildrenSurvey(ArrayList<SyncPersonalLandAllDetailsRequestDTO.FarmerHouseholdChildrenSurvey> farmerHouseholdChildrenSurvey) {
        FarmerHouseholdChildrenSurvey = farmerHouseholdChildrenSurvey;
    }

    public ArrayList<SyncPersonalLandAllDetailsRequestDTO.RiskAssessment> getRiskAssessment() {
        return RiskAssessment;
    }


    public void setRiskAssessment(ArrayList<SyncPersonalLandAllDetailsRequestDTO.RiskAssessment> riskAssessment) {
        RiskAssessment = riskAssessment;
    }

//    public ArrayList<SyncPersonalLandAllDetailsRequestDTO.ManfacturerFarmer> getManfacturerFarmer() {
//        return ManfacturerFarmer;
//    }
//
//    public void setManfacturerFarmer(ArrayList<SyncPersonalLandAllDetailsRequestDTO.ManfacturerFarmer> manfacturerFarmer) {
//        ManfacturerFarmer = manfacturerFarmer;
//    }
    
    //for gaja
    public ArrayList<ProcessorFarmer> getManfacturerFarmer() {
        return ProcessorFarmer;
    }

    public void setManfacturerFarmer(ArrayList<ProcessorFarmer> processorFarmer) {
        ProcessorFarmer = processorFarmer;
    }

    public ArrayList<SyncPersonalLandAllDetailsRequestDTO.DealerFarmer> getDealerFarmer() {
        return DealerFarmer;
    }

    public void setDealerFarmer(ArrayList<SyncPersonalLandAllDetailsRequestDTO.DealerFarmer> dealerFarmer) {
        DealerFarmer = dealerFarmer;
    }

    //Data class
    public static class Farmer {

        @SerializedName("FarmerCode")
        @Expose
        private String FarmerCode;
        @SerializedName("FirstName")
        @Expose
        private String FirstName;
        @SerializedName("LastName")
        @Expose
        private String LastName;
        @SerializedName("FatherName")
        @Expose
        private String FatherName;
        @SerializedName("Gender")
        @Expose
        private String Gender;
        @SerializedName("Age")
        @Expose
        private String Age;
        @SerializedName("PrimaryContactNo")
        @Expose
        private String PrimaryContactNo;
        @SerializedName("Address")
        @Expose
        private String Address;
        @SerializedName("VillageId")
        @Expose
        private String VillageId;
        @SerializedName("NationalIdentityCode")
        @Expose
        private String NationalIdentityCode;
        @SerializedName("NationalIdentityCodeDocument")
        @Expose
        private String NationalIdentityCodeDocument;
        @SerializedName("NoOfPlots")
        @Expose
        private String NoOfPlots;
        @SerializedName("Image")
        @Expose
        private String Image;
        @SerializedName("IsActive")
        @Expose
        private String IsActive;
        @SerializedName("CreatedDate")
        @Expose
        private String CreatedDate;
        @SerializedName("CreatedByUserId")
        @Expose
        private String CreatedByUserId;
        @SerializedName("UpdatedDate")
        @Expose
        private String UpdatedDate;
        @SerializedName("UpdatedByUserId")
        @Expose
        private String UpdatedByUserId;


        public String getFarmerCode() {
            return FarmerCode;
        }

        public void setFarmerCode(String FarmerCode) {
            this.FarmerCode = FarmerCode;
        }

        public String getFirstName() {
            return FirstName;
        }

        public void setFirstName(String FirstName) {
            this.FirstName = FirstName;
        }

        public String getLastName() {
            return LastName;
        }

        public void setLastName(String LastName) {
            this.LastName = LastName;
        }

        public String getFatherName() {
            return FatherName;
        }

        public void setFatherName(String FatherName) {
            this.FatherName = FatherName;
        }

        public String getGender() {
            return Gender;
        }

        public void setGender(String Gender) {
            this.Gender = Gender;
        }

        public String getAge() {
            return Age;
        }

        public void setAge(String Age) {
            this.Age = Age;
        }

        public String getPrimaryContactNo() {
            return PrimaryContactNo;
        }

        public void setPrimaryContactNo(String PrimaryContactNo) {
            this.PrimaryContactNo = PrimaryContactNo;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String Address) {
            this.Address = Address;
        }

        public String getVillageId() {
            return VillageId;
        }

        public void setVillageId(String VillageId) {
            this.VillageId = VillageId;
        }

        public String getNationalIdentityCode() {
            return NationalIdentityCode;
        }

        public void setNationalIdentityCode(String NationalIdentityCode) {
            this.NationalIdentityCode = NationalIdentityCode;
        }

        public String getNationalIdentityCodeDocument() {
            return NationalIdentityCodeDocument;
        }

        public void setNationalIdentityCodeDocument(String NationalIdentityCodeDocument) {
            this.NationalIdentityCodeDocument = NationalIdentityCodeDocument;
        }

        public String getNoOfPlots() {
            return NoOfPlots;
        }

        public void setNoOfPlots(String NoOfPlots) {
            this.NoOfPlots = NoOfPlots;
        }

        public String getImage() {
            return Image;
        }

        public void setImage(String Image) {
            this.Image = Image;
        }

        public String getIsActive() {
            return IsActive;
        }

        public void setIsActive(String IsActive) {
            this.IsActive = IsActive;
        }

        public String getCreatedDate() {
            return CreatedDate;
        }

        public void setCreatedDate(String CreatedDate) {
            this.CreatedDate = CreatedDate;
        }

        public String getCreatedByUserId() {
            return CreatedByUserId;
        }

        public void setCreatedByUserId(String CreatedByUserId) {
            this.CreatedByUserId = CreatedByUserId;
        }

        public String getUpdatedDate() {
            return UpdatedDate;
        }

        public void setUpdatedDate(String UpdatedDate) {
            this.UpdatedDate = UpdatedDate;
        }

        public String getUpdatedByUserId() {
            return UpdatedByUserId;
        }

        public void setUpdatedByUserId(String UpdatedByUserId) {
            this.UpdatedByUserId = UpdatedByUserId;
        }

//        @SerializedName("FarmerCode")
//        @Expose
//        private String FarmerCode = "";
//
//        @SerializedName("FarmerTitle")
//        @Expose
//        private String FarmerTittle = "";
//
//
//        @SerializedName("FirstName")
//        @Expose
//        private String FirstName = "";
//
//        @SerializedName("MiddleName")
//        @Expose
//        private String MiddleName = "";
//
//        @SerializedName("LastName")
//        @Expose
//        private String LastName = "";
//
//
//        @SerializedName("FatherName")
//        @Expose
//        private String FatherName = "";
//
//        @SerializedName("Gender")
//        @Expose
//        private String Gender = "";
//
//        @SerializedName("Age")
//        @Expose
//        private String Age = "";
//
//        @SerializedName("TotalPlotArea")
//        @Expose
//        private String TotalPlotArea;
//
//        @SerializedName("PlotsCount")
//        @Expose
//        private String PlotsCount;
//
//        @SerializedName("LocalImage")
//        @Expose
//        private String LocalImage;
//
//        public String getLocalImage() {
//            return LocalImage;
//        }
//
//        public void setLocalImage(String localImage) {
//            LocalImage = localImage;
//        }
//
//        public String getTotalPlotArea() {
//            return TotalPlotArea;
//        }
//
//        public void setTotalPlotArea(String totalPlotArea) {
//            TotalPlotArea = totalPlotArea;
//        }
//
//        public String getPlotsCount() {
//            return PlotsCount;
//        }
//
//        public void setPlotsCount(String plotsCount) {
//            PlotsCount = plotsCount;
//        }
//
////        @SerializedName("Aadhar")
////        @Expose
////        private String Aadhar = "";
//
//        @SerializedName("PrimaryContactNum")
//        @Expose
//        private String PrimaryContactNum = "";
//
//        @SerializedName("SecondaryContactNum")
//        @Expose
//        private String SecondaryContactNum = "";
//
//        @SerializedName("Address")
//        @Expose
//        private String Address = "";
//
//        @SerializedName("PinCode")
//        @Expose
//        private String PinCode = "";
//
//        @SerializedName("VillageId")
//        @Expose
//        private String VillageId = "";
//
//        @SerializedName("MandalId")
//        @Expose
//        private String MandalId = "";
//
//
//        @SerializedName("DistrictId")
//        @Expose
//        private String DistrictId = "";
//
//        @SerializedName("StateId")
//        @Expose
//        private String StateId = "";
//
//        @SerializedName("sync")
//        @Expose
//        private boolean sync;
//
//        @SerializedName("IsPlot")
//        @Expose
//        private String IsPlot = "";
//
//        @SerializedName("IsActive")
//        @Expose
//        private String IsActive = "1";
//
//        @SerializedName("CreatedDate")
//        @Expose
//        private String CreatedDate;
//
//        @SerializedName("CreatedByUserId")
//        @Expose
//        private String CreatedByUserId = "1";
//
//        @SerializedName("UpdatedDate")
//        @Expose
//        private String UpdatedDate;
//
//        @SerializedName("AgentId")
//        @Expose
//        private String AgentId;
//
//        @SerializedName("VoluntaryId")
//        @Expose
//        private String VoluntaryId;
//
//        @SerializedName("UpdatedByUserId")
//        @Expose
//        private String UpdatedByUserId = "1";
//
//        public String getAgentId() {
//            return AgentId;
//        }
//
//        public void setAgentId(String agentId) {
//            AgentId = agentId;
//        }
//
//        public String getVoluntaryId() {
//            return VoluntaryId;
//        }
//
//        public void setVoluntaryId(String voluntaryId) {
//            VoluntaryId = voluntaryId;
//        }
//
//        public String getFarmerCode() {
//            return FarmerCode;
//        }
//
//        public void setFarmerCode(String farmerCode) {
//            FarmerCode = farmerCode;
//        }
//
//        public String getFarmerTittle() {
//            return FarmerTittle;
//        }
//
//        public void setFarmerTittle(String farmerTittle) {
//            FarmerTittle = farmerTittle;
//        }
//
//        public String getFirstName() {
//            return FirstName;
//        }
//
//        public void setFirstName(String firstName) {
//            FirstName = firstName;
//        }
//
//        public String getMiddleName() {
//            return MiddleName;
//        }
//
//        public void setMiddleName(String middleName) {
//            MiddleName = middleName;
//        }
//
//        public String getLastName() {
//            return LastName;
//        }
//
//        public void setLastName(String lastName) {
//            LastName = lastName;
//        }
//
//        public String getFatherName() {
//            return FatherName;
//        }
//
//        public void setFatherName(String fatherName) {
//            FatherName = fatherName;
//        }
//
//        public String getGender() {
//            return Gender;
//        }
//
//        public void setGender(String gender) {
//            Gender = gender;
//        }
//
//        public String getAge() {
//            return Age;
//        }
//
//        public void setAge(String age) {
//            Age = age;
//        }
//
//        public String getPrimaryContactNum() {
//            return PrimaryContactNum;
//        }
//
//        public void setPrimaryContactNum(String primaryContactNum) {
//            PrimaryContactNum = primaryContactNum;
//        }
//
//        public String getSecondaryContactNum() {
//            return SecondaryContactNum;
//        }
//
//        public void setSecondaryContactNum(String secondaryContactNum) {
//            SecondaryContactNum = secondaryContactNum;
//        }
//
//
//        public String getAddress() {
//            return Address;
//        }
//
//        public void setAddress(String address) {
//            Address = address;
//        }
//
//        public String getPinCode() {
//            return PinCode;
//        }
//
//        public void setPinCode(String pinCode) {
//            PinCode = pinCode;
//        }
//
//        public String getVillageId() {
//            return VillageId;
//        }
//
//        public void setVillageId(String villageId) {
//            VillageId = villageId;
//        }
//
//        public String getMandalId() {
//            return MandalId;
//        }
//
//        public void setMandalId(String mandalId) {
//            MandalId = mandalId;
//        }
//
//        public String getDistrictId() {
//            return DistrictId;
//        }
//
//        public void setDistrictId(String districtId) {
//            DistrictId = districtId;
//        }
//
//        public String getStateId() {
//            return StateId;
//        }
//
//        public void setStateId(String stateId) {
//            StateId = stateId;
//        }
//
//        public boolean isSync() {
//            return sync;
//        }
//
//        public void setSync(boolean sync) {
//            this.sync = sync;
//        }
//
//        public String getIsPlot() {
//            return IsPlot;
//        }
//
//        public void setIsPlot(String isPlot) {
//            IsPlot = isPlot;
//        }
//
//        public String getIsActive() {
//            return IsActive;
//        }
//
//        public void setIsActive(String isActive) {
//            IsActive = isActive;
//        }
//
//        public String getCreatedDate() {
//            return CreatedDate;
//        }
//
//        public void setCreatedDate(String createdDate) {
//            CreatedDate = createdDate;
//        }
//
//        public String getCreatedByUserId() {
//            return CreatedByUserId;
//        }
//
//        public void setCreatedByUserId(String createdByUserId) {
//            CreatedByUserId = createdByUserId;
//        }
//
//        public String getUpdatedDate() {
//            return UpdatedDate;
//        }
//
//        public void setUpdatedDate(String updatedDate) {
//            UpdatedDate = updatedDate;
//        }
//
//        public String getUpdatedByUserId() {
//            return UpdatedByUserId;
//        }
//
//        public void setUpdatedByUserId(String updatedByUserId) {
//            UpdatedByUserId = updatedByUserId;
//        }
    }

    public static class Plantation{
        @SerializedName("PlotCode")
        @Expose
        private String PlotCode;
        @SerializedName("FarmerCode")
        @Expose
        private String FarmerCode;
        @SerializedName("TypeOfOwnership")
        @Expose
        private String TypeOfOwnership;
        @SerializedName("AreaInHectors")
        @Expose
        private Double AreaInHectors;

        @SerializedName("GeoboundariesArea")
        @Expose
        private String GeoboundariesArea;
        @SerializedName("Latitude")
        @Expose
        private Double Latitude;
        @SerializedName("Longitude")
        @Expose
        private Double Longitude;
        @SerializedName("Address")
        @Expose
        private String Address;
        @SerializedName("VillageId")
        @Expose
        private String VillageId;

        @SerializedName("LabourStatus")
        @Expose
        private int LabourStatus;

        @SerializedName("IsActive")
        @Expose
        private String IsActive;
        @SerializedName("CreatedDate")
        @Expose
        private String CreatedDate;
        @SerializedName("CreatedByUserId")
        @Expose
        private String CreatedByUserId;
        @SerializedName("UpdatedDate")
        @Expose
        private String UpdatedDate;
        @SerializedName("UpdatedByUserId")
        @Expose
        private String UpdatedByUserId;


        public String getPlotCode() {
            return PlotCode;
        }

        public void setPlotCode(String PlotCode) {
            this.PlotCode = PlotCode;
        }

        public String getFarmerCode() {
            return FarmerCode;
        }

        public void setFarmerCode(String FarmerCode) {
            this.FarmerCode = FarmerCode;
        }

        public String getTypeOfOwnership() {
            return TypeOfOwnership;
        }

        public void setTypeOfOwnership(String TypeOfOwnership) {
            this.TypeOfOwnership = TypeOfOwnership;
        }

        public int getLabourStatus() {
            return LabourStatus;
        }

        public void setLabourStatus(int LabourStatus) {
            this.LabourStatus = LabourStatus;
        }

        public Double getAreaInHectors() {
            return AreaInHectors;
        }

        public void setAreaInHectors(Double AreaInHectors) {
            this.AreaInHectors = AreaInHectors;
        }

        public Double getLatitude() {
            return Latitude;
        }

        public void setLatitude(Double Latitude) {
            this.Latitude = Latitude;
        }

        public Double getLongitude() {
            return Longitude;
        }

        public void setLongitude(Double Longitude) {
            this.Longitude = Longitude;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String Address) {
            this.Address = Address;
        }

        public String getVillageId() {
            return VillageId;
        }

        public void setVillageId(String VillageId) {
            this.VillageId = VillageId;
        }

        public String getIsActive() {
            return IsActive;
        }

        public void setIsActive(String IsActive) {
            this.IsActive = IsActive;
        }

        public String getCreatedDate() {
            return CreatedDate;
        }

        public void setCreatedDate(String CreatedDate) {
            this.CreatedDate = CreatedDate;
        }

        public String getCreatedByUserId() {
            return CreatedByUserId;
        }

        public void setCreatedByUserId(String CreatedByUserId) {
            this.CreatedByUserId = CreatedByUserId;
        }

        public String getUpdatedDate() {
            return UpdatedDate;
        }

        public void setUpdatedDate(String UpdatedDate) {
            this.UpdatedDate = UpdatedDate;
        }

        public String getUpdatedByUserId() {
            return UpdatedByUserId;
        }

        public void setUpdatedByUserId(String UpdatedByUserId) {
            this.UpdatedByUserId = UpdatedByUserId;
        }

        public String getGeoboundariesArea() {
            return GeoboundariesArea;
        }

        public void setGeoboundariesArea(String GeoboundariesArea) {
            this.GeoboundariesArea = GeoboundariesArea;
        }
    }

    public static class PlantationDocuments{

        @SerializedName("FarmerCode")
        @Expose
        private String FarmerCode;
        @SerializedName("PlotCode")
        @Expose
        private String PlotCode;
        @SerializedName("DocUrlValue")
        @Expose
        private String DocUrlValue;
        @SerializedName("DocURL")
        @Expose
        private String DocURL;
        @SerializedName("DocType")
        @Expose
        private String DocType;
        @SerializedName("IsActive")
        @Expose
        private String IsActive;
        @SerializedName("CreatedDate")
        @Expose
        private String CreatedDate;
        @SerializedName("CreatedByUserId")
        @Expose
        private String CreatedByUserId;
        @SerializedName("UpdatedDate")
        @Expose
        private String UpdatedDate;
        @SerializedName("UpdatedByUserId")
        @Expose
        private String UpdatedByUserId;

        public String getFarmerCode() {
            return FarmerCode;
        }

        public void setFarmerCode(String FarmerCode) {
            this.FarmerCode = FarmerCode;
        }

        public String getPlotCode() {
            return PlotCode;
        }

        public void setPlotCode(String PlotCode) {
            this.PlotCode = PlotCode;
        }

        public String getDocUrlValue() {
            return DocUrlValue;
        }

        public void setDocUrlValue(String DocUrlValue) {
            this.DocUrlValue = DocUrlValue;
        }

        public String getDocURL() {
            return DocURL;
        }

        public void setDocURL(String DocURL) {
            this.DocURL = DocURL;
        }

        public String getDocType() {
            return DocType;
        }

        public void setDocType(String DocType) {
            this.DocType = DocType;
        }

        public String getIsActive() {
            return IsActive;
        }

        public void setIsActive(String IsActive) {
            this.IsActive = IsActive;
        }

        public String getCreatedDate() {
            return CreatedDate;
        }

        public void setCreatedDate(String CreatedDate) {
            this.CreatedDate = CreatedDate;
        }

        public String getCreatedByUserId() {
            return CreatedByUserId;
        }

        public void setCreatedByUserId(String CreatedByUserId) {
            this.CreatedByUserId = CreatedByUserId;
        }

        public String getUpdatedDate() {
            return UpdatedDate;
        }

        public void setUpdatedDate(String UpdatedDate) {
            this.UpdatedDate = UpdatedDate;
        }

        public String getUpdatedByUserId() {
            return UpdatedByUserId;
        }

        public void setUpdatedByUserId(String UpdatedByUserId) {
            this.UpdatedByUserId = UpdatedByUserId;
        }
    }

    public static class PlantationGeoBoundaries{
        @SerializedName("PlotCode")
        @Expose
        private String PlotCode;
        @SerializedName("FarmerCode")
        @Expose
        private String FarmerCode;
        @SerializedName("Latitude")
        @Expose
        private Double Latitude;
        @SerializedName("Longitude")
        @Expose
        private Double Longitude;
        @SerializedName("SeqNo")
        @Expose
        private Integer SeqNo;
        @SerializedName("PlotCount")
        @Expose
        private Integer PlotCount;
        @SerializedName("IsActive")
        @Expose
        private String IsActive;
        @SerializedName("CreatedDate")
        @Expose
        private String CreatedDate;
        @SerializedName("CreatedByUserId")
        @Expose
        private String CreatedByUserId;
        @SerializedName("UpdatedDate")
        @Expose
        private String UpdatedDate;
        @SerializedName("UpdatedByUserId")
        @Expose
        private String UpdatedByUserId;

        public String getPlotCode() {
            return PlotCode;
        }

        public void setPlotCode(String PlotCode) {
            this.PlotCode = PlotCode;
        }

        public String getFarmerCode() {
            return FarmerCode;
        }

        public void setFarmerCode(String FarmerCode) {
            this.FarmerCode = FarmerCode;
        }

        public Double getLatitude() {
            return Latitude;
        }

        public void setLatitude(Double Latitude) {
            this.Latitude = Latitude;
        }

        public Double getLongitude() {
            return Longitude;
        }

        public void setLongitude(Double Longitude) {
            this.Longitude = Longitude;
        }

        public Integer getSeqNo() {
            return SeqNo;
        }

        public void setSeqNo(Integer SeqNo) {
            this.SeqNo = SeqNo;
        }

        public Integer getPlotCount() {
            return PlotCount;
        }

        public void setPlotCount(Integer PlotCount) {
            this.PlotCount = PlotCount;
        }

        public String getIsActive() {
            return IsActive;
        }

        public void setIsActive(String IsActive) {
            this.IsActive = IsActive;
        }

        public String getCreatedDate() {
            return CreatedDate;
        }

        public void setCreatedDate(String CreatedDate) {
            this.CreatedDate = CreatedDate;
        }

        public String getCreatedByUserId() {
            return CreatedByUserId;
        }

        public void setCreatedByUserId(String CreatedByUserId) {
            this.CreatedByUserId = CreatedByUserId;
        }

        public String getUpdatedDate() {
            return UpdatedDate;
        }

        public void setUpdatedDate(String UpdatedDate) {
            this.UpdatedDate = UpdatedDate;
        }

        public String getUpdatedByUserId() {
            return UpdatedByUserId;
        }

        public void setUpdatedByUserId(String UpdatedByUserId) {
            this.UpdatedByUserId = UpdatedByUserId;
        }
    }

    public static class PlantationLabourSurvey{

        @SerializedName("FarmerCode")
        @Expose
        private String FarmerCode;

        @SerializedName("PlantationCode")
        @Expose
        private String PlantationCode;

        @SerializedName("NoOfFieldWorkers")
        @Expose
        private Integer NoOfFieldWorkers;
        @SerializedName("NoOfMaleWorkers")
        @Expose
        private Integer NoOfMaleWorkers;
        @SerializedName("NoOfFemaleWorkers")
        @Expose
        private Integer NoOfFemaleWorkers;
        @SerializedName("NoOfResident")
        @Expose
        private Integer NoOfResident;
        @SerializedName("NoOfMigrant")
        @Expose
        private Integer NoOfMigrant;
        @SerializedName("OccupationOfChildren")
        @Expose
        private String OccupationOfChildren;
        @SerializedName("IsActive")
        @Expose
        private String IsActive;
        @SerializedName("CreatedDate")
        @Expose
        private String CreatedDate;
        @SerializedName("CreatedByUserId")
        @Expose
        private String CreatedByUserId;
        @SerializedName("UpdatedDate")
        @Expose
        private String UpdatedDate;
        @SerializedName("UpdatedByUserId")
        @Expose
        private String UpdatedByUserId;

        public String getFarmerCode() {
            return FarmerCode;
        }

        public void setFarmerCode(String FarmerCode) {
            this.FarmerCode = FarmerCode;
        }


        public Integer getNoOfFieldWorkers() {
            return NoOfFieldWorkers;
        }

        public String getPlantationCode() {
            return PlantationCode;
        }

        public void setPlantationCode(String plantationCode) {
            PlantationCode = plantationCode;
        }

        public void setNoOfFieldWorkers(Integer NoOfFieldWorkers) {
            this.NoOfFieldWorkers = NoOfFieldWorkers;
        }

        public Integer getNoOfMaleWorkers() {
            return NoOfMaleWorkers;
        }

        public void setNoOfMaleWorkers(Integer NoOfMaleWorkers) {
            this.NoOfMaleWorkers = NoOfMaleWorkers;
        }

        public Integer getNoOfFemaleWorkers() {
            return NoOfFemaleWorkers;
        }

        public void setNoOfFemaleWorkers(Integer NoOfFemaleWorkers) {
            this.NoOfFemaleWorkers = NoOfFemaleWorkers;
        }

        public Integer getNoOfResident() {
            return NoOfResident;
        }

        public void setNoOfResident(Integer NoOfResident) {
            this.NoOfResident = NoOfResident;
        }

        public Integer getNoOfMigrant() {
            return NoOfMigrant;
        }

        public void setNoOfMigrant(Integer NoOfMigrant) {
            this.NoOfMigrant = NoOfMigrant;
        }

        public String getOccupationOfChildren() {
            return OccupationOfChildren;
        }

        public void setOccupationOfChildren(String OccupationOfChildren) {
            this.OccupationOfChildren = OccupationOfChildren;
        }

        public String getIsActive() {
            return IsActive;
        }

        public void setIsActive(String IsActive) {
            this.IsActive = IsActive;
        }

        public String getCreatedDate() {
            return CreatedDate;
        }

        public void setCreatedDate(String CreatedDate) {
            this.CreatedDate = CreatedDate;
        }

        public String getCreatedByUserId() {
            return CreatedByUserId;
        }

        public void setCreatedByUserId(String CreatedByUserId) {
            this.CreatedByUserId = CreatedByUserId;
        }

        public String getUpdatedDate() {
            return UpdatedDate;
        }

        public void setUpdatedDate(String UpdatedDate) {
            this.UpdatedDate = UpdatedDate;
        }

        public String getUpdatedByUserId() {
            return UpdatedByUserId;
        }

        public void setUpdatedByUserId(String UpdatedByUserId) {
            this.UpdatedByUserId = UpdatedByUserId;
        }
    }
    public static class FarmerHouseholdParentSurvey{

        @SerializedName("FarmerCode")
        @Expose
        private String FarmerCode;
        @SerializedName("FarmerId")
        @Expose
        private String FarmerId;
        @SerializedName("FamilyCount")
        @Expose
        private Integer FamilyCount;
        @SerializedName("MaritalStatus")
        @Expose
        private String MaritalStatus;
        @SerializedName("SpouseName")
        @Expose
        private String SpouseName;
        @SerializedName("Age")
        @Expose
        private Integer Age;
        @SerializedName("Gender")
        @Expose
        private String Gender;
        @SerializedName("Occupation")
        @Expose
        private String Occupation;
        @SerializedName("NoofChildren")
        @Expose
        private Integer NoofChildren;
        @SerializedName("IsActive")
        @Expose
        private String IsActive;
        @SerializedName("CreatedDate")
        @Expose
        private String CreatedDate;
        @SerializedName("CreatedByUserId")
        @Expose
        private String CreatedByUserId;
        @SerializedName("UpdatedDate")
        @Expose
        private String UpdatedDate;
        @SerializedName("UpdatedByUserId")
        @Expose
        private String UpdatedByUserId;

        public String getFarmerCode() {
            return FarmerCode;
        }

        public void setFarmerCode(String FarmerCode) {
            this.FarmerCode = FarmerCode;
        }

        public String getFarmerId() {
            return FarmerId;
        }

        public void setFarmerId(String FarmerId) {
            this.FarmerId = FarmerId;
        }

        public Integer getFamilyCount() {
            return FamilyCount;
        }

        public void setFamilyCount(Integer FamilyCount) {
            this.FamilyCount = FamilyCount;
        }

        public String getMaritalStatus() {
            return MaritalStatus;
        }

        public void setMaritalStatus(String MaritalStatus) {
            this.MaritalStatus = MaritalStatus;
        }

        public String getSpouseName() {
            return SpouseName;
        }

        public void setSpouseName(String SpouseName) {
            this.SpouseName = SpouseName;
        }

        public Integer getAge() {
            return Age;
        }

        public void setAge(Integer Age) {
            this.Age = Age;
        }

        public String getGender() {
            return Gender;
        }

        public void setGender(String Gender) {
            this.Gender = Gender;
        }

        public String getOccupation() {
            return Occupation;
        }

        public void setOccupation(String Occupation) {
            this.Occupation = Occupation;
        }

        public Integer getNoofChildren() {
            return NoofChildren;
        }

        public void setNoofChildren(Integer NoofChildren) {
            this.NoofChildren = NoofChildren;
        }

        public String getIsActive() {
            return IsActive;
        }

        public void setIsActive(String IsActive) {
            this.IsActive = IsActive;
        }

        public String getCreatedDate() {
            return CreatedDate;
        }

        public void setCreatedDate(String CreatedDate) {
            this.CreatedDate = CreatedDate;
        }

        public String getCreatedByUserId() {
            return CreatedByUserId;
        }

        public void setCreatedByUserId(String CreatedByUserId) {
            this.CreatedByUserId = CreatedByUserId;
        }

        public String getUpdatedDate() {
            return UpdatedDate;
        }

        public void setUpdatedDate(String UpdatedDate) {
            this.UpdatedDate = UpdatedDate;
        }

        public String getUpdatedByUserId() {
            return UpdatedByUserId;
        }

        public void setUpdatedByUserId(String UpdatedByUserId) {
            this.UpdatedByUserId = UpdatedByUserId;
        }
    }
    public static class FarmerHouseholdChildrenSurvey{

        @SerializedName("FarmerCode")
        @Expose
        private String FarmerCode;

        @SerializedName("FarmerHouseholdSurveyId")
        @Expose
        private int FarmerHouseholdSurveyId;
        @SerializedName("FarmerId")
        @Expose
        private String FarmerId;
        @SerializedName("ChildrenName")
        @Expose
        private String ChildrenName;
        @SerializedName("ChildrenGender")
        @Expose
        private String ChildrenGender;
        @SerializedName("ChildrenAge")
        @Expose
        private int ChildrenAge;
        @SerializedName("ChildrenOccupation")
        @Expose
        private String ChildrenOccupation;

        @SerializedName("IsActive")
        @Expose
        private String IsActive;
        @SerializedName("CreatedDate")
        @Expose
        private String CreatedDate;
        @SerializedName("CreatedByUserId")
        @Expose
        private String CreatedByUserId;
        @SerializedName("UpdatedDate")
        @Expose
        private String UpdatedDate;
        @SerializedName("UpdatedByUserId")
        @Expose
        private String UpdatedByUserId;

        public String getFarmerCode() {
            return FarmerCode;
        }

        public void setFarmerCode(String FarmerCode) {
            this.FarmerCode = FarmerCode;
        }

        public Integer getFarmerHouseholdSurveyId() {
            return FarmerHouseholdSurveyId;
        }

        public void setFarmerHouseholdSurveyId(int FarmerHouseholdSurveyId) {
            this.FarmerHouseholdSurveyId = FarmerHouseholdSurveyId;
        }

        public String getFarmerId() {
            return FarmerId;
        }

        public void setFarmerId(String FarmerId) {
            this.FarmerId = FarmerId;
        }

        public String getChildrenName() {
            return ChildrenName;
        }

        public void setChildrenName(String ChildrenName) {
            this.ChildrenName = ChildrenName;
        }

        public String getChildrenGender() {
            return ChildrenGender;
        }

        public void setChildrenGender(String ChildrenGender) {
            this.ChildrenGender = ChildrenGender;
        }

        public Integer getChildrenAge() {
            return ChildrenAge;
        }

        public void setChildrenAge(int ChildrenAge) {
            this.ChildrenAge = ChildrenAge;
        }

        public String getChildrenOccupation() {
            return ChildrenOccupation;
        }

        public void setChildrenOccupation(String ChildrenOccupation) {
            this.ChildrenOccupation = ChildrenOccupation;
        }

        public String getIsActive() {
            return IsActive;
        }

        public void setIsActive(String IsActive) {
            this.IsActive = IsActive;
        }

        public String getCreatedDate() {
            return CreatedDate;
        }

        public void setCreatedDate(String CreatedDate) {
            this.CreatedDate = CreatedDate;
        }

        public String getCreatedByUserId() {
            return CreatedByUserId;
        }

        public void setCreatedByUserId(String CreatedByUserId) {
            this.CreatedByUserId = CreatedByUserId;
        }

        public String getUpdatedDate() {
            return UpdatedDate;
        }

        public void setUpdatedDate(String UpdatedDate) {
            this.UpdatedDate = UpdatedDate;
        }

        public String getUpdatedByUserId() {
            return UpdatedByUserId;
        }

        public void setUpdatedByUserId(String UpdatedByUserId) {
            this.UpdatedByUserId = UpdatedByUserId;
        }
    }

    public static class RiskAssessment {

        @SerializedName("RiskAssesmentQuestionHdrId")
        @Expose
        private int RiskAssesmentQuestionHdrId;

        @SerializedName("FarmerCode")
        @Expose
        private String FarmerCode;

        @SerializedName("Answers")
        @Expose
        private String Answers;

        @SerializedName("IsActive")
        @Expose
        private String IsActive;
        @SerializedName("CreatedDate")
        @Expose
        private String CreatedDate;
        @SerializedName("CreatedByUserId")
        @Expose
        private String CreatedByUserId;
        @SerializedName("UpdatedDate")
        @Expose
        private String UpdatedDate;
        @SerializedName("UpdatedByUserId")
        @Expose
        private String UpdatedByUserId;

        public int getRiskAssesmentQuestionHdrId() {
            return RiskAssesmentQuestionHdrId;
        }

        public void setRiskAssesmentQuestionHdrId(int RiskAssesmentQuestionHdrId) {
            this.RiskAssesmentQuestionHdrId = RiskAssesmentQuestionHdrId;
        }

        public String getFarmerCode() {
            return FarmerCode;
        }

        public void setFarmerCode(String FarmerCode) {
            this.FarmerCode = FarmerCode;
        }

        public String getAnswers() {
            return Answers;
        }

        public void setAnswers(String Answers) {
            this.Answers = Answers;
        }

        public String getIsActive() {
            return IsActive;
        }

        public void setIsActive(String IsActive) {
            this.IsActive = IsActive;
        }

        public String getCreatedDate() {
            return CreatedDate;
        }

        public void setCreatedDate(String CreatedDate) {
            this.CreatedDate = CreatedDate;
        }

        public String getCreatedByUserId() {
            return CreatedByUserId;
        }

        public void setCreatedByUserId(String CreatedByUserId) {
            this.CreatedByUserId = CreatedByUserId;
        }

        public String getUpdatedDate() {
            return UpdatedDate;
        }

        public void setUpdatedDate(String UpdatedDate) {
            this.UpdatedDate = UpdatedDate;
        }

        public String getUpdatedByUserId() {
            return UpdatedByUserId;
        }

        public void setUpdatedByUserId(String UpdatedByUserId) {
            this.UpdatedByUserId = UpdatedByUserId;
        }

    }

//    public static class ManfacturerFarmer {
//
//        @SerializedName("FarmerCode")
//        @Expose
//        private String FarmerCode;
//
//        @SerializedName("ManfacturerId")
//        @Expose
//        private int ManfacturerId;
//
//        @SerializedName("IsActive")
//        @Expose
//        private String IsActive;
//        @SerializedName("CreatedDate")
//        @Expose
//        private String CreatedDate;
//        @SerializedName("CreatedByUserId")
//        @Expose
//        private String CreatedByUserId;
//        @SerializedName("UpdatedDate")
//        @Expose
//        private String UpdatedDate;
//        @SerializedName("UpdatedByUserId")
//        @Expose
//        private String UpdatedByUserId;
//
//        public String getFarmerCode() {
//            return FarmerCode;
//        }
//
//        public void setFarmerCode(String FarmerCode) {
//            this.FarmerCode = FarmerCode;
//        }
//
//        public int getManfacturerId() {
//            return ManfacturerId;
//        }
//
//        public void setManfacturerId(int ManfacturerId) {
//            this.ManfacturerId = ManfacturerId;
//        }
//
//        public String getIsActive() {
//            return IsActive;
//        }
//
//        public void setIsActive(String IsActive) {
//            this.IsActive = IsActive;
//        }
//
//        public String getCreatedDate() {
//            return CreatedDate;
//        }
//
//        public void setCreatedDate(String CreatedDate) {
//            this.CreatedDate = CreatedDate;
//        }
//
//        public String getCreatedByUserId() {
//            return CreatedByUserId;
//        }
//
//        public void setCreatedByUserId(String CreatedByUserId) {
//            this.CreatedByUserId = CreatedByUserId;
//        }
//
//        public String getUpdatedDate() {
//            return UpdatedDate;
//        }
//
//        public void setUpdatedDate(String UpdatedDate) {
//            this.UpdatedDate = UpdatedDate;
//        }
//
//        public String getUpdatedByUserId() {
//            return UpdatedByUserId;
//        }
//
//        public void setUpdatedByUserId(String UpdatedByUserId) {
//            this.UpdatedByUserId = UpdatedByUserId;
//        }
//    }
    
    //For Gaja
    public static class ProcessorFarmer {

        @SerializedName("FarmerCode")
        @Expose
        private String FarmerCode;

        @SerializedName("ProcessorId")
        @Expose
        private int ProcessorId;

        @SerializedName("IsActive")
        @Expose
        private String IsActive;
        @SerializedName("CreatedDate")
        @Expose
        private String CreatedDate;
        @SerializedName("CreatedByUserId")
        @Expose
        private String CreatedByUserId;
        @SerializedName("UpdatedDate")
        @Expose
        private String UpdatedDate;
        @SerializedName("UpdatedByUserId")
        @Expose
        private String UpdatedByUserId;

        public String getFarmerCode() {
            return FarmerCode;
        }

        public void setFarmerCode(String FarmerCode) {
            this.FarmerCode = FarmerCode;
        }

        public int getProcessorId() {
            return ProcessorId;
        }

        public void setProcessorId(int ProcessorId) {
            this.ProcessorId = ProcessorId;
        }

        public String getIsActive() {
            return IsActive;
        }

        public void setIsActive(String IsActive) {
            this.IsActive = IsActive;
        }

        public String getCreatedDate() {
            return CreatedDate;
        }

        public void setCreatedDate(String CreatedDate) {
            this.CreatedDate = CreatedDate;
        }

        public String getCreatedByUserId() {
            return CreatedByUserId;
        }

        public void setCreatedByUserId(String CreatedByUserId) {
            this.CreatedByUserId = CreatedByUserId;
        }

        public String getUpdatedDate() {
            return UpdatedDate;
        }

        public void setUpdatedDate(String UpdatedDate) {
            this.UpdatedDate = UpdatedDate;
        }

        public String getUpdatedByUserId() {
            return UpdatedByUserId;
        }

        public void setUpdatedByUserId(String UpdatedByUserId) {
            this.UpdatedByUserId = UpdatedByUserId;
        }
    }

    public static class DealerFarmer {

        @SerializedName("FarmerCode")
        @Expose
        private String FarmerCode;

        @SerializedName("DealerId")
        @Expose
        private int DealerId;

        @SerializedName("IsActive")
        @Expose
        private String IsActive;
        @SerializedName("CreatedDate")
        @Expose
        private String CreatedDate;
        @SerializedName("CreatedByUserId")
        @Expose
        private String CreatedByUserId;
        @SerializedName("UpdatedDate")
        @Expose
        private String UpdatedDate;
        @SerializedName("UpdatedByUserId")
        @Expose
        private String UpdatedByUserId;

        public String getFarmerCode() {
            return FarmerCode;
        }

        public void setFarmerCode(String FarmerCode) {
            this.FarmerCode = FarmerCode;
        }

        public int getDealerId() {
            return DealerId;
        }

        public void setDealerId(int DealerId) {
            this.DealerId = DealerId;
        }

        public String getIsActive() {
            return IsActive;
        }

        public void setIsActive(String IsActive) {
            this.IsActive = IsActive;
        }

        public String getCreatedDate() {
            return CreatedDate;
        }

        public void setCreatedDate(String CreatedDate) {
            this.CreatedDate = CreatedDate;
        }

        public String getCreatedByUserId() {
            return CreatedByUserId;
        }

        public void setCreatedByUserId(String CreatedByUserId) {
            this.CreatedByUserId = CreatedByUserId;
        }

        public String getUpdatedDate() {
            return UpdatedDate;
        }

        public void setUpdatedDate(String UpdatedDate) {
            this.UpdatedDate = UpdatedDate;
        }

        public String getUpdatedByUserId() {
            return UpdatedByUserId;
        }

        public void setUpdatedByUserId(String UpdatedByUserId) {
            this.UpdatedByUserId = UpdatedByUserId;
        }
    }


}


