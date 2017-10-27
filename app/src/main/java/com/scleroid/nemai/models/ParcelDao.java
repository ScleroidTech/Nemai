package com.scleroid.nemai.models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by scleroid on 27/10/17.
 */
@Dao
public interface ParcelDao {
    @Query("SELECT * FROM Parcel")
    List<Parcel> getAll();

    @Query("SELECT * FROM Parcel where serialNo LIKE :serialNo ")
    Parcel findById(String serialNo);

    @Query("SELECT COUNT(*) from Parcel")
    int countUsers();

    @Insert
    void insert(Parcel parcel);

    @Insert
    void insertAll(Parcel... parcels);

    @Delete
    void delete(Parcel parcel);

    @Delete
    void delete(Parcel... parcels);
}
