package com.scleroid.nemai.utils;

import com.scleroid.nemai.data.models.Address;
import com.scleroid.nemai.data.models.Parcel;

import java.util.List;

/**
 * Created by Ganesh on 22-11-2017.
 */

public class ListPair {
    Parcel parcel;
    List<Address> addresses;

    public ListPair(Parcel parcel, List<Address> addresses) {
        this.parcel = parcel;
        this.addresses = addresses;
    }
}
