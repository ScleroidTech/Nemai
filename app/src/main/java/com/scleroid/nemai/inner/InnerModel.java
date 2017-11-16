package com.scleroid.nemai.inner;

/**
 * Created by Ganesh on 15-11-2017.
 */

public class InnerModel {
    private final String name;
    private final String address_line_1;
    private final String address_line_2;
    private final String state;
    private final String city;
    private final int pincode;
    private final String mobileNo;

    public InnerModel(String name, String address_line_1, String address_line_2, String state, String city, int pincode, String mobileNo) {

        this.name = name;
        this.address_line_1 = address_line_1;
        this.address_line_2 = address_line_2;
        this.state = state;

        this.city = city;
        this.pincode = pincode;
        this.mobileNo = mobileNo;
    }

    public String getName() {
        return name;
    }

    public String getAddress_line_1() {
        return address_line_1;
    }

    public String getAddress_line_2() {
        return address_line_2;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public int getPincode() {
        return pincode;
    }

    public String getMobileNo() {
        return mobileNo;
    }
}
