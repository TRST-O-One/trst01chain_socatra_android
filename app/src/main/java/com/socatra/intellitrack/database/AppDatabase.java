package com.socatra.intellitrack.database;


import static com.socatra.intellitrack.constants.AppConstant.DB_VERSION;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.socatra.intellitrack.database.converter.ArrayListConverter;
import com.socatra.intellitrack.database.converter.DateConverter;
import com.socatra.intellitrack.database.dao.AppDAO;
import com.socatra.intellitrack.database.entity.FarmersTable;


@Database(entities = {FarmersTable.class},
        version = DB_VERSION, exportSchema = false )

@TypeConverters({DateConverter.class, ArrayListConverter.class})
public abstract class  AppDatabase extends RoomDatabase {
    // --- SINGLETON ---

    private static volatile AppDatabase INSTANCE;

    // --- DAO ---
    public abstract AppDAO bootStrapDAO();

}