package com.socatra.excutivechain.di.module;



import static com.socatra.excutivechain.AppConstant.DB_NAME;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.socatra.excutivechain.App;
import com.socatra.excutivechain.AppHelper;
import com.socatra.excutivechain.database.AppDatabase;
import com.socatra.excutivechain.database.dao.AppDAO;
import com.socatra.excutivechain.repositories.AppRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module(includes = ViewModelModule.class)
public class AppModule {
    // TODO: Production v-8-9 , DEV v-1-2
    static final Migration MIGRATION_DB = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // TODO: Migration 8 to 9 Released APK
            // TODO: 3/3/2022 update new tables by removing of old tables
//            database.execSQL("DROP TABLE GeoBoundariesTable");
//            // Create the new table
//            database.execSQL("CREATE TABLE  BankDetailsSubmitTable (BankID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,FarmerCode TEXT," +
//                    "IdentityCode TEXT,AccountHolderName TEXT,AccountNumber TEXT,BranchName TEXT,IFSCCode TEXT,DocExtension TEXT,DocUrl TEXT,LocalDocUrl TEXT,sync TEXT,serverSubmit TEXT," +
//                    "IsActive TEXT,CreatedDate TEXT,CreatedByUserId TEXT,UpdatedDate TEXT,UpdatedByUserId TEXT)");
//           // Copy the data
//            database.execSQL(
//                    "INSERT INTO BankDetailsSubmitTable (userid, username, last_update) SELECT userid, username, last_update FROM users");
//           // Remove the old table
//            database.execSQL("DROP TABLE users");
//
//            // Change the table name to the correct one
//            database.execSQL("ALTER TABLE users_new RENAME TO users");

            // Create the new table
//            database.execSQL("CREATE TABLE IF NOT EXISTS  FarmerTable (FarmerId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
//                    "FarmerCode TEXT,FarmerTitle TEXT,FirstName TEXT,MiddleName TEXT,LastName TEXT,Cluster TEXT,FatherName TEXT,Gender TEXT,Age TEXT,PrimaryContactNum TEXT,SecondaryContactNum TEXT,Address TEXT,PinCode TEXT,VillageId TEXT,DistrictId TEXT,MandalId TEXT,StateId TEXT,DocExtension TEXT,DocUrl TEXT,sync INTEGER NOT NULL,pushToServer TEXT,IsPlot TEXT,LocalImage TEXT," +
//                    "IsActive TEXT,CreatedDate TEXT,CreatedByUserId TEXT,UpdatedDate TEXT,UpdatedByUserId TEXT)");
//
////            // Copy the data
//            database.execSQL("INSERT INTO FarmerTable (FarmerId, FarmerCode, FarmerTitle, FirstName, MiddleName, LastName, Cluster, FatherName, Gender, Age, PrimaryContactNum, SecondaryContactNum, Address, PinCode, VillageId, DistrictId, MandalId, StateId, " +
//                            "DocExtension,DocUrl,sync,pushToServer,IsPlot,LocalImage,IsActive,CreatedDate,CreatedByUserId,UpdatedDate,UpdatedByUserId) SELECT FarmerId, FarmerCode, FarmerTittle, FirstName, MiddleName, LastName, Cluster, FatherName, Gender, Age, PrimaryContactNum, SecondaryContactNum, Address, PinCode, VillageId, DistrictId, MandalId,StateId, \" +\n" +
//                            " \"DocExtension, DocUrl, sync, pushToServer, IsPlot, LocalImage, IsActive, CreatedDate, CreatedByUserId, UpdatedDate, UpdatedByUserId FROM FarmerDetailListTable");
//
//            // Copy the data
//            //database.execSQL("INSERT INTO FarmerTable  SELECT FarmerId, 'FarmerCode', 'FarmerTittle', 'FirstName', 'MiddleName', 'LastName', 'Cluster', 'FatherName', 'Gender', 'Age', 'PrimaryContactNum', 'SecondaryContactNum', 'Address', 'PinCode', 'VillageId', 'DistrictId', 'MandalId','StateId','DocExtension', 'DocUrl', sync, 'pushToServer', 'IsPlot', 'LocalImage', 'IsActive', 'CreatedDate', 'CreatedByUserId', 'UpdatedDate', 'UpdatedByUserId' FROM FarmerDetailListTable");
//         //   database.execSQL("INSERT INTO FarmerTable  SELECT FarmerId, 'FarmerCode', 'FarmerTitle', 'FirstName', 'MiddleName', 'LastName', 'Cluster', 'FatherName', 'Gender', 'Age', 'PrimaryContactNum', 'SecondaryContactNum', 'Address', 'PinCode', 'VillageId', 'DistrictId', 'MandalId','StateId','DocExtension', 'DocUrl', sync, 'pushToServer', 'IsPlot', 'LocalImage', 'IsActive', 'CreatedDate', 'CreatedByUserId', 'UpdatedDate', 'UpdatedByUserId' FROM FarmerDetailListTable");
//
//            // Remove the old table
//            database.execSQL("DROP TABLE FarmerDetailListTable");
//
////            // Change the table name to the correct one
////            database.execSQL("ALTER TABLE FarmerTable RENAME TO FarmerDetailListTable");
//
//
//            // TODO: 3/3/2022 for plot table migration
//            // Create the new table
//            database.execSQL("CREATE TABLE IF NOT EXISTS  PlotDetailsListTableNew (PlotId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
//                    "PlotNo TEXT,Size TEXT,CropId TEXT,Cluster TEXT,FarmerCode TEXT,Plotownership TEXT,LandMark TEXT,GPSPlotArea TEXT,Latitude TEXT,Longitude TEXT,Address TEXT,VillageId TEXT,DistrictId TEXT,MandalId TEXT,StateId TEXT,SurveyNo TEXT,PattadarBookNo TEXT,GPS TEXT,LandImage TEXT,LandLocalImage TEXT,sync INTEGER NOT NULL,serverStatus TEXT,PinCode TEXT,DocExtension TEXT,DocUrl TEXT," +
//                    "IsActive TEXT,CreatedDate TEXT,CreatedByUserId TEXT,UpdatedDate TEXT,UpdatedByUserId TEXT)");
//
////            // Copy the data
//            database.execSQL("INSERT INTO PlotDetailsListTableNew (PlotId, PlotNo, Size, CropId, Cluster, FarmerCode, Plotownership, LandMark, GPSPlotArea, Latitude, Longitude, Address, VillageId, DistrictId, MandalId, StateId, SurveyNo, PattadarBookNo,GPS, LandImage,LandLocalImage, " +
//                    "sync,serverStatus,PinCode,DocExtension,DocUrl,IsActive,CreatedDate,CreatedByUserId,UpdatedDate,UpdatedByUserId) SELECT PlotId, PlotNo, Size, CropId, Cluster, FarmerCode, Plotownership, LandMark, GPSPlotArea, Latitude, Longitude, Address, VillageId, DistrictId, MandalId, StateId, SurveyNo, PattadarBookNo,GPS, LandImage,LandLocalImage, \" +\n" +
//                    "                    \"sync,serverStatus,PinCode,DocExtension,DocUrl,IsActive,CreatedDate,CreatedByUserId,UpdatedDate,UpdatedByUserId FROM PlotDetailsListTable");
//
//            // Copy the data
//            //database.execSQL("INSERT INTO FarmerTable  SELECT FarmerId, 'FarmerCode', 'FarmerTittle', 'FirstName', 'MiddleName', 'LastName', 'Cluster', 'FatherName', 'Gender', 'Age', 'PrimaryContactNum', 'SecondaryContactNum', 'Address', 'PinCode', 'VillageId', 'DistrictId', 'MandalId','StateId','DocExtension', 'DocUrl', sync, 'pushToServer', 'IsPlot', 'LocalImage', 'IsActive', 'CreatedDate', 'CreatedByUserId', 'UpdatedDate', 'UpdatedByUserId' FROM FarmerDetailListTable");
//            //   database.execSQL("INSERT INTO FarmerTable  SELECT FarmerId, 'FarmerCode', 'FarmerTitle', 'FirstName', 'MiddleName', 'LastName', 'Cluster', 'FatherName', 'Gender', 'Age', 'PrimaryContactNum', 'SecondaryContactNum', 'Address', 'PinCode', 'VillageId', 'DistrictId', 'MandalId','StateId','DocExtension', 'DocUrl', sync, 'pushToServer', 'IsPlot', 'LocalImage', 'IsActive', 'CreatedDate', 'CreatedByUserId', 'UpdatedDate', 'UpdatedByUserId' FROM FarmerDetailListTable");
//
//            // Remove the old table
//            database.execSQL("DROP TABLE PlotDetailsListTable");
//
////            // Change the table name to the correct one
//            database.execSQL("ALTER TABLE PlotDetailsListTableNew RENAME TO PlotDetailsListTable");
//
//
//
//
//
//            // TODO: 2/16/2022 season table ,lookupdata table, logbookDropDownHdr table
//            //  Add LogBook table,Add Module status table ,Add Fertlizer table ,Add Organic Table,
//            //  Add Water Regime SeasonTable,Add Water Pre Season Table,Add BoreWel Pump Table,Add Water Regime Table,Add Harvest Table
//
//            // TODO: Season Table
//            database.execSQL("CREATE TABLE IF NOT EXISTS SeasonTable (seasonId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,Id TEXT," +
//                    "Code TEXT,Name TEXT,PlantFrom TEXT,PlantTo TEXT," +
//                    "IsActive TEXT,CreatedDate TEXT,CreatedByUserId TEXT,UpdatedDate TEXT,UpdatedByUserId TEXT)");
//
//            // TODO: LogBookDropDownHDRTable
//            database.execSQL("CREATE TABLE IF NOT EXISTS LogBookDropDownHDRTable (selectDropDownHDRID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,Id TEXT," +
//                    "Code TEXT,Name TEXT," +
//                    "IsActive TEXT,CreatedDate TEXT,CreatedByUserId TEXT,UpdatedDate TEXT,UpdatedByUserId TEXT)");
//
//            // TODO: LookUpDropDownDataTable
//            database.execSQL("CREATE TABLE IF NOT EXISTS LookUpDropDownDataTable (lookUpId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,Id TEXT," +
//                    "LookupHdrId TEXT,Code TEXT,Name TEXT,Remarks TEXT,Ord TEXT," +
//                    "IsActive TEXT,CreatedDate TEXT,CreatedByUserId TEXT,UpdatedDate TEXT,UpdatedByUserId TEXT)");
//
//            // TODO: AddLogBookDetailsTable
//            database.execSQL("CREATE TABLE IF NOT EXISTS AddLogBookDetailsTable (LogBookId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,PlotNo TEXT," +
//                    "LogBookNo TEXT,Season INTEGER,DateOfSowing TEXT,DateCheck TEXT,Crop TEXT,Harvest TEXT,CultivationPractice INTEGER," +
//                    "sync INTEGER NOT NULL,serverStatus TEXT," +
//                    "IsActive TEXT,CreatedDate TEXT,CreatedByUserId TEXT,UpdatedDate TEXT,UpdatedByUserId TEXT)");
//
//            // TODO: LogBookModulesStatusDetailsTable
//            database.execSQL("CREATE TABLE IF NOT EXISTS LogBookModulesStatusDetailsTable (LBModuleId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,LogBookNo TEXT," +
//                    "IsFertilizer TEXT,IsOrganic TEXT,IsWaterRegimeSeason TEXT,IsWaterReasonPre TEXT,IsBoreWell TEXT," +
//                    "IsWaterFeild TEXT,IsHarvest TEXT,sync INTEGER NOT NULL,serverStatus TEXT," +
//                    "IsActive TEXT,CreatedDate TEXT," +
//                    "CreatedByUserId TEXT,UpdatedDate TEXT,UpdatedByUserId TEXT)");
//
//
//            // TODO: AddFertilizerDetailsTable
//            database.execSQL("CREATE TABLE IF NOT EXISTS AddFertilizerDetailsTable (FertilizerId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,LogBookNo TEXT," +
//                    "Fertilizer INTEGER,FertilizerType INTEGER,Date TEXT,Qty TEXT,UOM INTEGER,CropProtection TEXT,DateCheck TEXT," +
//                    "sync INTEGER NOT NULL,serverStatus TEXT," +
//                    "IsActive TEXT,CreatedDate TEXT,CreatedByUserId TEXT,UpdatedDate TEXT,UpdatedByUserId TEXT)");
//
//
//            // TODO: AddOrganicDetailsTable
//            database.execSQL("CREATE TABLE IF NOT EXISTS AddOrganicDetailsTable (OrganicId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,LogBookNo TEXT," +
//                    "Value INTEGER,Date TEXT,sync INTEGER NOT NULL,serverStatus TEXT," +
//                    "IsActive TEXT,CreatedDate TEXT,CreatedByUserId TEXT,UpdatedDate TEXT,UpdatedByUserId TEXT)");
//
//            // TODO: AddWaterRegimeSeasonDetailsTable
//            database.execSQL("CREATE TABLE IF NOT EXISTS AddWaterRegimeSeasonDetailsTable (WaterRegSeasonId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,LogBookNo TEXT," +
//                    "Field INTEGER,sync INTEGER NOT NULL,serverStatus TEXT," +
//                    "IsActive TEXT,CreatedDate TEXT,CreatedByUserId TEXT,UpdatedDate TEXT,UpdatedByUserId TEXT)");
//
//            // TODO: AddWaterReasonPreSeasonTable
//            database.execSQL("CREATE TABLE IF NOT EXISTS AddWaterReasonPreSeasonTable (WaterPreSeasonId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,LogBookNo TEXT," +
//                    "Field INTEGER, sync INTEGER NOT NULL,serverStatus TEXT," +
//                    "IsActive TEXT,CreatedDate TEXT,CreatedByUserId TEXT,UpdatedDate TEXT,UpdatedByUserId TEXT)");
//
//            // TODO: AddBoreWellPumpSeasonTable
//            database.execSQL("CREATE TABLE IF NOT EXISTS AddBoreWellPumpSeasonTable (BorewellID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,LogBookNo TEXT," +
//                    "Date TEXT,HoursDay INTEGER,sync INTEGER NOT NULL,serverStatus TEXT," +
//                    "IsActive TEXT,CreatedDate TEXT,CreatedByUserId TEXT,UpdatedDate TEXT,UpdatedByUserId TEXT)");
//
//            // TODO: AddWaterSeasonFeildTable
//            database.execSQL("CREATE TABLE IF NOT EXISTS AddWaterSeasonFeildTable (WaterSeasonFieldId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,LogBookNo TEXT," +
//                    "Field INTEGER,sync INTEGER NOT NULL,serverStatus TEXT," +
//                    "IsActive TEXT,CreatedDate TEXT,CreatedByUserId TEXT,UpdatedDate TEXT,UpdatedByUserId TEXT)");
//
//            // TODO: AddHarvestDetailsTable
//            database.execSQL("CREATE TABLE IF NOT EXISTS AddHarvestDetailsTable (HarvestID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,LogBookNo TEXT," +
//                    "Date TEXT,YeildQty TEXT,sync INTEGER NOT NULL,serverStatus TEXT," +
//                    "IsActive TEXT,CreatedDate TEXT,CreatedByUserId TEXT,UpdatedDate TEXT,UpdatedByUserId TEXT)");
//
//            // TODO: RefreshTableDateCheck
//            database.execSQL("CREATE TABLE IF NOT EXISTS RefreshTableDateCheck (RefreshID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,Date TEXT," +
//                    "DeviceID TEXT,IsActive TEXT,CreatedDate TEXT,CreatedByUserId TEXT,UpdatedDate TEXT,UpdatedByUserId TEXT)");
//


            // TODO: Migration 12 to 13 for testing
//            database.execSQL("CREATE TABLE IF NOT EXISTS LogBookModulesStatusDetailsTable (LBModuleId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,LogBookNo TEXT," +
//                    "IsFertilizer TEXT,IsOrganic TEXT,IsWaterRegimeSeason TEXT,IsWaterReasonPre TEXT,IsBoreWell TEXT," +
//                    "IsWaterFeild TEXT,IsHarvest TEXT,sync INTEGER NOT NULL,serverStatus TEXT,IsActive TEXT,CreatedDate TEXT," +
//                    "CreatedByUserId TEXT,UpdatedDate TEXT,UpdatedByUserId TEXT)");

//            database.execSQL(
//                    "CREATE TABLE users_new (userid TEXT, username TEXT, last_update INTEGER, PRIMARY KEY(userid))");
//            // Copy the data
//            database.execSQL(
//                    "INSERT INTO users_new (userid, username, last_update) SELECT userid, username, last_update FROM users");
//            // Remove the old table
//            database.execSQL("DROP TABLE users");




        }
    };

    // --- DATABASE INJECTION ---

    @Provides
    @Singleton
    AppDatabase provideBootStrapDatabase(Application application) {
        String currentDBPath="";
        currentDBPath="/sdcard/Android/data/com.socatra.excutivechain/db/"+DB_NAME;
       // SafeHelperFactory factory=SafeHelperFactory.fromUser(new SpannableStringBuilder("Rbl@123"));
       /* try {
           currentDBPath = App.createDBPath();
           if (TextUtils.isEmpty(currentDBPath)) {
               currentDBPath = App.context.getDatabasePath(DB_NAME).getAbsolutePath();
           }
       }catch (Exception ex){
           ex.printStackTrace();
       }*/
//        Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, SystemStrings.ROOM_DATABASE_NAME)
//                // allow queries on the main thread.
//                // Don't do this on a real app!
//                .allowMainThreadQueries()
//                .fallbackToDestructiveMigration()
//                .build();
        return Room.databaseBuilder(application,
                AppDatabase.class,currentDBPath).addMigrations(MIGRATION_DB)
                //AppDatabase.class,currentDBPath)
                .allowMainThreadQueries()
//                .fallbackToDestructiveMigration() // TODO: Database will be cleared

           //     .openHelperFactory(factory) // TODO: Set this encryption only for production release
                .build();

    }


    @Provides
    @Singleton
    AppDAO provideBootStrapDao(AppDatabase database) {
        return database.bootStrapDAO(); }

    // --- REPOSITORY INJECTION ---

    @Provides
    Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @Provides
    AppHelper provideAppHelper() {
        return new AppHelper(App.context);
    }

    @Provides
    Context provideAppContext() {
        return App.context;
    }

    @Provides
    @Singleton
    AppRepository provideBootStrapRepository(AppDAO appDAO, Executor executor, AppHelper appHelper, Context context) {
        return new AppRepository(appDAO, executor,appHelper,context);
    }


}
