package com.scleroid.nemai.adapter;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.scleroid.nemai.models.Parcel;
import com.scleroid.nemai.models.ParcelDao;

/**
 * Created by scleroid on 27/10/17.
 */
@Database(entities = {Parcel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public static AppDatabase getAppDatabase(Context context) {
        if (instance == null) {
            instance =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "nemai-database")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            //.allowMainThreadQueries()
                            .build();
        }
        return instance;
    }

    public static void destroyInstance() {

        instance = null;
    }

    public abstract ParcelDao parcelDao();


}
