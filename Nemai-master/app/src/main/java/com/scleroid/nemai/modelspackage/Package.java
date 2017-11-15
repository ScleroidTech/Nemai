package com.scleroid.nemai.modelspackage;

/**
 * Created by Ganesh on 22/07/2017.
 */

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Package {
    @PrimaryKey(autoGenerate = true)
    private int serialNo;

    private String name;
    private String surname;
    private String email;
    private String address;
    private String district;
    private String state;
    private String pincode;
    private String width;
    private String height;
    private String description;

    public Package(String Name, String Surname, String Email, String Address, String District, String State, String Pincode, String width, String height, String description) {
        this.name = Name;
        this.surname = Surname;
        this.email = Email;
        this.address = Address;
        this.district = District;
        this.state = State;
        this.pincode = Pincode;
        this.width = width;
        this.height = height;
        this.description = description;
    }

    public int getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(int serialNo) {
        this.serialNo = serialNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}