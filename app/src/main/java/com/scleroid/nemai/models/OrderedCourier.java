package com.scleroid.nemai.models;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import static com.scleroid.nemai.utils.RandomSerialNumberGen.getRandomSerialNo;

/**
 * Created by Ganesh on 18-12-2017.
 */
@Entity
public class OrderedCourier {
    @Embedded(prefix = "parcel_")
    private Parcel parcel;
    @Embedded(prefix = "address_")
    private Address address;
    @PrimaryKey
    private Long serialNo;

    public OrderedCourier(Long serialNo, Parcel parcel, Address address) {
        this.serialNo = serialNo;
        this.parcel = parcel;
        this.address = address;
    }

    @Ignore
    public OrderedCourier(Parcel parcel, Address address) {
        this(getRandomSerialNo(), parcel, address);
    }


    public Long getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Long serialNo) {
        this.serialNo = serialNo;
    }

    public Parcel getParcel() {
        return parcel;
    }

    public void setParcel(Parcel parcel) {
        this.parcel = parcel;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }


}
