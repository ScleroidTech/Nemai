package com.scleroid.nemai.models;

import java.util.Random;

/**
 * Created by Ganesh on 18-12-2017.
 */

public class Order {
    Parcel parcel;
    Address address;
    private Long serialNo;

    public Order(Long serialNo, Parcel parcel, Address address) {
        this.serialNo = serialNo;
        this.parcel = parcel;
        this.address = address;
    }


    public Order(Parcel parcel, Address address) {
        this(new Random().nextLong(), parcel, address);
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
