package com.scleroid.nemai.data.localdb;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 7/24/18
 */
@Dao
public interface PinDao {

	/**
	 * Select Query
	 *
	 * @return List of all indiaes in database
	 */
	@Query("SELECT * FROM India")
	List<PinCode> getAll();

	/**
	 * Returns  list of all indiaes
	 *
	 * @return LiveData object List of all indiaes in database
	 */
	@Query("SELECT * FROM India")
	LiveData<List<PinCode>> getAllIndiaLive();


	/**
	 * Returns  list of all indiaes
	 *
	 * @return LiveData object List of all indiaes in database
	 */
	@Query("SELECT * from india where pincode LIKE :pincode")

	Flowable<PinCode> getAllIndiaRxViaPin(String pincode);



	/**
	 * Returns  list of all indiaes
	 *
	 * @return LiveData object List of all indiaes in database
	 */
	@Query("SELECT * from india where location LIKE :pincode or area LIKE :pincode")
	Flowable<PinCode> getAllIndiaRxViaCity(String pincode);



	/**
	 * select query to count Number of india
	 *
	 * @return number of total entries in the table
	 */
	@Query("SELECT COUNT(*) from India")
	int countIndia();

	/**
	 * Performs insertion operation
	 *
	 * @param india inserts this object in the database
	 */
	@Insert(onConflict = REPLACE)
	void insert(PinCode india);


	/**
	 * Let the database be a part of history I meant, it deletes the whole table
	 */
	@Query("DELETE FROM India")
	void nukeTable();

}
