package com.scleroid.nemai.activity.selectcourieractivity;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.scleroid.nemai.data.AppDatabase;
import com.scleroid.nemai.data.models.Parcel;

import java.util.List;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 7/24/18
 */
public class SelectCourierViewModel extends AndroidViewModel {
	private LiveData<List<Parcel>> parcelList;
	private AppDatabase appDatabase;

	public SelectCourierViewModel(@NonNull Application application) {
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
