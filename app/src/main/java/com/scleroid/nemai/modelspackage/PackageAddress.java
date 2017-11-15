package com.scleroid.nemai.modelspackage;

/**
 * Created by scleroid on 15/9/17.
 */

public class PackageAddress {

    private String location, pincode, state, area;

    public PackageAddress(String location, String pincode, String state, String area) {
        this.location = location;
        this.pincode = pincode;
        this.state = state;
        this.area = area;

    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }


}
