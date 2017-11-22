package com.scleroid.nemai.models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by Ganesh on 22-11-2017.
 */
@Dao
public interface AddressDao {
    @Query("SELECT * FROM Address")
    List<Address> getAll();

    @Query("SELECT * FROM Address where serialNo = :serialNo ")
    Address findById(long serialNo);

    @Query("SELECT COUNT(*) from Address")
    int countAddresss();

    @Insert(onConflict = REPLACE)
    void insert(Address address);

    @Insert
    void insertAll(Address... addresss);

    @Update(onConflict = REPLACE)
    void updateAddress(Address address);

    @Delete
    void delete(Address address);

    @Query("DELETE FROM Address")
    void nukeTable();

}
