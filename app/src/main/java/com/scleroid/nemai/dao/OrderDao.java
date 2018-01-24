package com.scleroid.nemai.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.scleroid.nemai.models.OrderedCourier;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by Ganesh on 21-12-2017.
 */
@Dao
public interface OrderDao {

    @Query("SELECT * FROM OrderedCourier")
    List<OrderedCourier> getAll();

    @Query("select * from OrderedCourier")
    LiveData<List<OrderedCourier>> getAllOrdersLive();

    @Query("SELECT * FROM OrderedCourier where serialNo = :serialNo")
    OrderedCourier findById(long serialNo);


    @Query("SELECT COUNT(*) from OrderedCourier")
    int countOrders();

    @Insert(onConflict = REPLACE)
    void insert(OrderedCourier orderedCourier);

    @Insert
    void insertAll(OrderedCourier... orderedCouriers);

    @Update(onConflict = REPLACE)
    void updateOrder(OrderedCourier orderedCourier);

    @Delete
    void delete(OrderedCourier orderedCourier);

    @Query("DELETE FROM OrderedCourier")
    void nukeTable();

}
