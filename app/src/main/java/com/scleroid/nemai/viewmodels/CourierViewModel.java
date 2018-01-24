package com.scleroid.nemai.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.scleroid.nemai.AppDatabase;
import com.scleroid.nemai.models.Courier;
import com.scleroid.nemai.models.Parcel;

import java.util.List;

/**
 * @author Ganesh Kaple
 * @since 09-01-2018
 */

public class CourierViewModel extends AndroidViewModel {
    private Parcel parcel;
    private List<Courier> courieres;

    private LiveData<List<Courier>> courierList;

    private AppDatabase appDatabase;

    public CourierViewModel(@NonNull Application application) {
        super(application);

        appDatabase = AppDatabase.getAppDatabase(this.getApplication());

        courierList = updateCourierLiveData();
    }


    public LiveData<List<Courier>> getCourierList() {
        if (courierList == null) {
            courierList = updateCourierLiveData();
        }

        return courierList;
    }

    private LiveData<List<Courier>> updateCourierLiveData() {
        //TODO use Network based data source here
        LiveData<List<Courier>> courierList = appDatabase.courierDao().getAllCourierLive();
        return courierList;
    }

}
