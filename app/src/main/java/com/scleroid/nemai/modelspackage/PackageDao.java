package com.scleroid.nemai.modelspackage;

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
public interface PackageDao {
    @Query("SELECT * FROM Package")
    List<Package> getAll();

    @Query("SELECT * FROM Package where serialNo = :serialNo ")
    Package findById(int serialNo);

    @Query("SELECT COUNT(*) from Package")
    int countPackages();

    @Insert(onConflict = REPLACE)
    void insert(Package Package);

    @Insert
    void insertAll(Package... Packages);

    @Update(onConflict = REPLACE)
    void updatePackage(Package Package);

    @Delete
    void delete(Package Package);

    @Delete
    void deleteAll(Package... Packages);
}
