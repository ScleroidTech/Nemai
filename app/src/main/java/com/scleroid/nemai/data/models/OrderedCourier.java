package com.scleroid.nemai.data.models;

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
    @Embedded(prefix = "courier_")
    private Courier courier;

    public OrderedCourier(Long serialNo, Parcel parcel, Address address, Courier courier) {
        this.serialNo = serialNo;
        this.parcel = parcel;
        this.address = address;
        this.courier = courier;
    }

    @Ignore
    public OrderedCourier(Parcel parcel, Address address, Courier courier) {
        this(getRandomSerialNo(), parcel, address, courier);
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


    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }
}
