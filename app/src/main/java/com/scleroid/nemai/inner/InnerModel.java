package com.scleroid.nemai.inner;

/**
 * Created by Ganesh on 15-11-2017.
 */

public class InnerModel {
    public final String name;
    public final String address;
    public final String city;
    public final int pincode;
    public final String mobileNo;


    public InnerModel(String name, String address, String city, int pincode, String mobileNo) {

        this.name = name;
        this.address = address;
        this.city = city;
        this.pincode = pincode;
        this.mobileNo = mobileNo;
    }
}
