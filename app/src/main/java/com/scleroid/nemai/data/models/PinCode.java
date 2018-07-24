package com.scleroid.nemai.data.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by scleroid on 15/9/17.
 */

public class PinCode implements Parcelable {

    public static final Creator<PinCode> CREATOR = new Creator<PinCode>() {
        @Override
        public PinCode createFromParcel(Parcel in) {
            return new PinCode(in);
        }

        @Override
        public PinCode[] newArray(int size) {
            return new PinCode[size];
        }
    };
    private String location, pincode, state, area;

    public PinCode(String location, String pincode, String state, String area) {
        this.location = location;
        this.pincode = pincode;
        this.state =state;
        this.area = area;

    }

    protected PinCode(Parcel in) {
        location = in.readString();
        pincode = in.readString();
        state = in.readString();
        area = in.readString();
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(location);
        parcel.writeString(pincode);
        parcel.writeString(state);
        parcel.writeString(area);
    }
}
