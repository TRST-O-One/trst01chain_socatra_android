package com.socatra.excutivechain.di.module;



import static com.socatra.excutivechain.utils.AppConstant.DB_NAME;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.socatra.excutivechain.di.app.App;
import com.socatra.excutivechain.utils.AppHelper;
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

        }
    };

    // --- DATABASE INJECTION ---

    @Provides
    @Singleton
    AppDatabase provideBootStrapDatabase(Application application) {
        String currentDBPath="";

        try {
           currentDBPath = App.createDBPath();
           if (TextUtils.isEmpty(currentDBPath)) {
               currentDBPath = App.context.getDatabasePath(DB_NAME).getAbsolutePath();
           }
       }catch (Exception ex){
           ex.printStackTrace();
       }
        return Room.databaseBuilder(application,
                AppDatabase.class,currentDBPath).addMigrations(MIGRATION_DB)
                .allowMainThreadQueries()
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
