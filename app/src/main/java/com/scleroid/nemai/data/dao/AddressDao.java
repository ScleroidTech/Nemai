package com.scleroid.nemai.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.scleroid.nemai.data.models.Address;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Data Access Object required for
 * @see android.arch.persistence.room.Room
 * For Model
 * @see Address
 * @author Ganesh Kaple
 * @since 22-11-2017
 */
@Dao
public interface AddressDao {
    /**
     * Select Query
     *
     * @return List of all addresses in database
     */
    @Query("SELECT * FROM Address")
    List<Address> getAll();

    /**
     * Returns  list of all addresses
     * @return LiveData object List of all addresses in database
     */
    @Query("SELECT * FROM Address")
    LiveData<List<Address>> getAllAddressLive();

    /**
     * Returns a specific value compared to serialNo passed
     * @param serialNo the serialNo of object to be found
     * @return address object with same serialNo
     */
    @Query("SELECT * FROM Address where serialNo = :serialNo ")
    Address findById(long serialNo);

    /**
     * select query to count Number of address
     * @return number of total entries in the table
     */
    @Query("SELECT COUNT(*) from Address")
    int countAddress();

    /**
     * Performs insertion operation
     * @param address inserts this object in the database
     */
    @Insert(onConflict = REPLACE)
    void insert(Address address);

    /**
     * Performs insertion operation for multiple values
     * @param address inserts list of address object
     */
    @Insert
    void insertAll(Address... address);

    /**
     * Updates a specified dataset
     * @param address the address which needs to be updated
     */
    @Update(onConflict = REPLACE)
    void update(Address address);

    /**
     * Removes a particular dataset from the database
     * @param address the object which needs to be deleted
     */
    @Delete
    void delete(Address address);

    /**
     * Let the database be a part of history
     * I meant, it deletes the whole table
     */
    @Query("DELETE FROM Address")
    void nukeTable();

}
