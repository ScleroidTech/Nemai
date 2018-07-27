package com.scleroid.nemai.data.localdb;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.huma.room_for_asset.RoomAsset;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 7/24/18
 */
@Database(entities = PinCode.class, version = 4)
public abstract class PinDatabase extends RoomDatabase {

	/**
	 * Holds the instance of the database
	 */

	private static PinDatabase instance;

	/**
	 * Returns the instance of AppDatabase class, creates a new one if doesn't exists, & returns
	 * that
	 *
	 * @param context Context of Application or current activity needs to be passed
	 * @return AppDatabase returns the instance of Appdatabase
	 */
	public static PinDatabase getPinDatabase(Context context) {
        /*
           creates a new database if instance doesn't exists
         */

		if (instance == null) {
			instance =
					RoomAsset.databaseBuilder(context.getApplicationContext(), PinDatabase.class,
							"pincode-db")
							//While Migration of database, it destroys previous versions, should
							// be removed
							//	.fallbackToDestructiveMigration()
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
	 * @see PinDao
	 * @see PinDao
	 */
	public abstract PinDao pinDao();


}
