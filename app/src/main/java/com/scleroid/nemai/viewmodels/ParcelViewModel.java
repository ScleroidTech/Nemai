package com.scleroid.nemai.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.scleroid.nemai.AppDatabase;
import com.scleroid.nemai.data.models.Parcel;

import java.util.List;

/**
 * Created by Ganesh on 13-12-2017.
 */

public class ParcelViewModel extends AndroidViewModel {
    private LiveData<List<Parcel>> parcelList;
    private AppDatabase appDatabase;

    public ParcelViewModel(@NonNull Application application) {
        super(application);

        appDatabase = AppDatabase.getAppDatabase(this.getApplication());
        parcelList = updateParcelLiveData();
    }

    private LiveData<List<Parcel>> updateParcelLiveData() {

        LiveData<List<Parcel>> parcelList = appDatabase.parcelDao().getAllParcelsLive();
        return parcelList;
    }

    public LiveData<List<Parcel>> getParcelList() {
        if (parcelList == null) {
            parcelList = updateParcelLiveData();
        }

        return parcelList;
    }

}
