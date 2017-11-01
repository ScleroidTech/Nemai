package com.scleroid.nemai.models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by scleroid on 27/10/17.
 */
@Dao
public interface ParcelDao {
    @Query("SELECT * FROM parcel")
    List<Parcel> getAll();

    @Query("SELECT * FROM parcel where serialNo = :serialNo ")
    Parcel findById(int serialNo);

    @Query("SELECT COUNT(*) from parcel")
    int countParcels();

    @Insert(onConflict = REPLACE)
    void insert(Parcel parcel);

    @Insert
    void insertAll(Parcel... parcels);

    @Update(onConflict = REPLACE)
    void updateParcel(Parcel parcel);

    @Delete
    void delete(Parcel parcel);

    @Delete
    void deleteAll(Parcel... parcels);
}
