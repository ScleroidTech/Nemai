package com.scleroid.nemai.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import static com.scleroid.nemai.utils.RandomSerialNumberGen.getRandomSerialNo;

/**
 * Created by Ganesh on 15-11-2017.
 */
@Entity
public class Address implements Parcelable {


    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
    @PrimaryKey
    private long serialNo;
    private String name;
    private String address_line_1;
    private String address_line_2;
    private String state;
    private String city;
    private String pincode;
    private String mobileNo;

    public Address(String name, String address_line_1, String address_line_2, String state, String city, String pincode, String mobileNo) {
        this(name, address_line_1, address_line_2, state, city, pincode, mobileNo, getRandomSerialNo());

    }
    @Ignore
    public Address(String name, String address_line_1, String address_line_2, String state, String city, String pincode, String mobileNo, long serialNo) {

        this.name = name;
        this.address_line_1 = address_line_1;
        this.address_line_2 = address_line_2;
        this.state = state;

        this.city = city;
        this.pincode = pincode;
        this.mobileNo = mobileNo;
        this.serialNo = serialNo;
    }

    @Ignore
    protected Address(Parcel in) {
        serialNo = in.readLong();
        name = in.readString();
        address_line_1 = in.readString();
        address_line_2 = in.readString();
        state = in.readString();
        city = in.readString();
        pincode = in.readString();
        mobileNo = in.readString();
    }

    public long getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(long serialNo) {
        this.serialNo = serialNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress_line_1() {
        return address_line_1;
    }

    public void setAddress_line_1(String address_line_1) {
        this.address_line_1 = address_line_1;
    }

    public String getAddress_line_2() {
        return address_line_2;
    }

    public void setAddress_line_2(String address_line_2) {
        this.address_line_2 = address_line_2;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeLong(serialNo);
        parcel.writeString(address_line_1);
        parcel.writeString(address_line_2);
        parcel.writeString(state);
        parcel.writeString(city);
        parcel.writeString(pincode);
        parcel.writeString(mobileNo);


    }
}
