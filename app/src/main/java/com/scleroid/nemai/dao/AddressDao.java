package com.scleroid.nemai.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.scleroid.nemai.models.Address;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by Ganesh on 22-11-2017.
 */
@Dao
public interface AddressDao {
    @Query("SELECT * FROM Address")
    List<Address> getAll();

    @Query("SELECT * FROM Address")
    LiveData<List<Address>> getAllAddressLive();

    @Query("SELECT * FROM Address where serialNo = :serialNo ")
    Address findById(long serialNo);

    @Query("SELECT COUNT(*) from Address")
    int countAddresss();

    @Insert(onConflict = REPLACE)
    void insert(Address address);

    @Insert
    void insertAll(Address... addresss);

    @Update(onConflict = REPLACE)
    void update(Address address);

    @Delete
    void delete(Address address);

    @Query("DELETE FROM Address")
    void nukeTable();

}
