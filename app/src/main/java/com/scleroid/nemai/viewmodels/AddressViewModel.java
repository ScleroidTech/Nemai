package com.scleroid.nemai.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.scleroid.nemai.AppDatabase;
import com.scleroid.nemai.controller.AddressLab;
import com.scleroid.nemai.models.Address;

import java.util.List;

public class AddressViewModel extends AndroidViewModel {

    private List<Address> addresses;

    //    private LiveData<List<Parcel>> parcelList;
    private LiveData<List<Address>> addressList;

    private AppDatabase appDatabase;

    public AddressViewModel(@NonNull Application application) {
        super(application);

        appDatabase = AppDatabase.getAppDatabase(this.getApplication());
        //      parcelList = updateParcelLiveData();
        addressList = updateAddressLiveData();
    }

/*Possible duplicate code    private LiveData<List<Parcel>> updateParcelLiveData() {

        LiveData<List<Parcel>> parcelList = appDatabase.parcelDao().getAllParcelsLive();
        return parcelList;
    }

    public LiveData<List<Parcel>> getParcelList() {
        if (parcelList == null) {
            parcelList = updateParcelLiveData();
        }

        return parcelList;
    }*/

    public LiveData<List<Address>> getAddressList() {
        if (addressList == null) {
            addressList = updateAddressLiveData();
        }

        return addressList;
    }

    private LiveData<List<Address>> updateAddressLiveData() {
        LiveData<List<Address>> addressList = appDatabase.addressDao().getAllAddressLive();
        return addressList;
    }

    public void editItem(Address address) {
        AddressLab.updateAddress(address, appDatabase);
    }
}