package com.scleroid.nemai.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.scleroid.nemai.data.models.Courier;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Data Access Object required for
 *
 * @author Ganesh Kaple
 * @see android.arch.persistence.room.Room
 * For Model
 * @see Courier
 * @since 10-01-2018
 */
@Dao
public interface CourierDao {

    /**
     * Select Query
     *
     * @return List of all courieres in database
     */
    @Query("SELECT * FROM Courier")
    List<Courier> getAll();

    /**
     * Returns  list of all courieres
     *
     * @return LiveData object List of all courieres in database
     */
    @Query("SELECT * FROM Courier")
    LiveData<List<Courier>> getAllCourierLive();

    /**
     * Returns a specific value compared to serialNo passed
     *
     * @param serialNo the serialNo of object to be found
     * @return courier object with same serialNo
     */
    @Query("SELECT * FROM Courier where serialNo = :serialNo ")
    Courier findById(long serialNo);

    /**
     * select query to count Number of courier
     *
     * @return number of total entries in the table
     */
    @Query("SELECT COUNT(*) from Courier")
    int countCourier();

    /**
     * Performs insertion operation
     *
     * @param courier inserts this object in the database
     */
    @Insert(onConflict = REPLACE)
    void insert(Courier courier);

    /**
     * Performs insertion operation for multiple values
     *
     * @param courier inserts list of courier object
     */
    @Insert
    void insertAll(Courier... courier);

    /**
     * Updates a specified dataset
     *
     * @param courier the courier which needs to be updated
     */
    @Update(onConflict = REPLACE)
    void update(Courier courier);

    /**
     * Removes a particular dataset from the database
     *
     * @param courier the object which needs to be deleted
     */
    @Delete
    void delete(Courier courier);

    /**
     * Let the database be a part of history
     * I meant, it deletes the whole table
     */
    @Query("DELETE FROM Courier")
    void nukeTable();

}
