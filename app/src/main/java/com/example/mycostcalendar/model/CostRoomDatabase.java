package com.example.mycostcalendar.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Cost.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class CostRoomDatabase extends RoomDatabase {

    public abstract CostDao costDao();

    private static CostRoomDatabase dbInstance;

    static CostRoomDatabase getDatabase(final Context context) {
        if (dbInstance == null) {
            synchronized (CostRoomDatabase.class) {
                if (dbInstance == null) {
                    dbInstance = Room.databaseBuilder(context.getApplicationContext(),
                            CostRoomDatabase.class, "cost_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return dbInstance;
    }
}
