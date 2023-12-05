package com.socatra.excutivechain.database;


import static com.socatra.excutivechain.AppConstant.DB_VERSION;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.socatra.excutivechain.database.converter.ArrayListConverter;
import com.socatra.excutivechain.database.converter.DateConverter;
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


@Database(entities = {FarmersTable.class, Plantation.class, PlantationDocuments.class, PlantationGeoBoundaries.class,
        RefreshTableDateCheck.class, FarmerHouseholdParentSurvey.class,
        FarmerHouseholdChildrenSurvey.class, PlantationLabourSurvey.class, RiskAssessment.class, DealerFarmer.class,
        ManfacturerFarmer.class,

        Country.class, VillageTable.class, StateorProvince.class, DistrictorRegency.class, SubDistrict.class,
        RiskAssessmentQuestion.class, DealerMaster.class, ManufacturerMaster.class,
        AppLanguageTable.class, AppLanguageHDRTable.class},
        version = DB_VERSION, exportSchema = false )

@TypeConverters({DateConverter.class, ArrayListConverter.class})
public abstract class  AppDatabase extends RoomDatabase {
    // --- SINGLETON ---

    private static volatile AppDatabase INSTANCE;

    // --- DAO ---
    public abstract AppDAO bootStrapDAO();

}