package com.scleroid.nemai.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.scleroid.nemai.data.dao.AddressDao;
import com.scleroid.nemai.data.dao.CourierDao;
import com.scleroid.nemai.data.dao.OrderDao;
import com.scleroid.nemai.data.dao.ParcelDao;
import com.scleroid.nemai.data.models.Address;
import com.scleroid.nemai.data.models.Courier;
import com.scleroid.nemai.data.models.OrderedCourier;
import com.scleroid.nemai.data.models.Parcel;

/**
 * @author Ganesh Kaple
 * @see Parcel
 * @see Address
 * @see OrderedCourier
 * @since 27/10/17 It is a singleton class,so it holds only one object for it's entire existance It
 * holds the current object of database It handles creating of the database if it doesn't exists &
 * providing the database object whenever required There are 3 tables in this database,
 */
@Database(entities = {Parcel.class, Address.class, OrderedCourier.class, Courier.class}, version
		= 5)
public abstract class AppDatabase extends RoomDatabase {
	/**
	 * Holds the instance of the database
	 */

	private static AppDatabase instance;

	/**
	 * Returns the instance of AppDatabase class, creates a new one if doesn't exists, & returns
	 * that
	 *
	 * @param context Context of Application or current activity needs to be passed
	 * @return AppDatabase returns the instance of Appdatabase
	 */
	public static AppDatabase getAppDatabase(Context context) {
        /*
           creates a new database if instance doesn't exists
         */

		if (instance == null) {
			instance =
					Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
							"nemai-database")
							//While Migration of database, it destroys previous versions, should
                            // be removed
							.fallbackToDestructiveMigration()
							.build();
		}
		return instance;
	}

	/**
	 * Destroys the instance of the database, doesn't actually destroy the database, just the
	 * pointer to it,
	 */
	public static void destroyInstance() {

		instance = null;
	}

	/**
	 * Parcel Model Data Access Object, For Room Library
	 *
	 * @return an object of
	 * @see ParcelDao
	 * @see ParcelDao
	 */
	public abstract ParcelDao parcelDao();

	/**
	 * Address Model Data Access Object, For Room Library
	 *
	 * @return an object of
	 * @see AddressDao
	 * @see AddressDao
	 */
	public abstract AddressDao addressDao();

	/**
	 * Courier Model Data Access Object, For Room Library
	 *
	 * @return an object of
	 * @see com.scleroid.nemai.data.dao.CourierDao
	 * @see com.scleroid.nemai.data.dao.CourierDao
	 */
	public abstract CourierDao courierDao();


	/**
	 * Order Model Data Access Object, For Room Library
	 *
	 * @return returns an object of
	 * @see OrderDao
	 * @see OrderDao
	 */
	public abstract OrderDao orderDao();


}
