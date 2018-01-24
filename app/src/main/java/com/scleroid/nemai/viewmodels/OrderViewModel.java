package com.scleroid.nemai.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.scleroid.nemai.AppDatabase;
import com.scleroid.nemai.models.OrderedCourier;

import java.util.List;

/**
 * Created by Ganesh on 21-12-2017.
 */

public class OrderViewModel extends AndroidViewModel {
    private final AppDatabase appDatabase;
    private LiveData<List<OrderedCourier>> orderList;

    public OrderViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getAppDatabase(this.getApplication());
        orderList = updateOrderLiveData();
    }

    private LiveData<List<OrderedCourier>> updateOrderLiveData() {
        return appDatabase.orderDao().getAllOrdersLive();
    }

    public LiveData<List<OrderedCourier>> getOrderList() {
        if (orderList == null) orderList = updateOrderLiveData();
        return orderList;
    }
}
