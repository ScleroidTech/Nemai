package com.scleroid.nemai;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.scleroid.nemai.dao.AddressDao;
import com.scleroid.nemai.dao.OrderDao;
import com.scleroid.nemai.dao.ParcelDao;
import com.scleroid.nemai.models.Address;
import com.scleroid.nemai.models.OrderedCourier;
import com.scleroid.nemai.models.Parcel;

/**
 * Created by scleroid on 27/10/17.
 */
@Database(entities = {Parcel.class, Address.class, OrderedCourier.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public static AppDatabase getAppDatabase(Context context) {
        if (instance == null) {
            instance =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "nemai-database")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            //.allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return instance;
    }

    public static void destroyInstance() {

        instance = null;
    }

    public abstract ParcelDao parcelDao();

    public abstract AddressDao addressDao();

    public abstract OrderDao orderDao();


}
