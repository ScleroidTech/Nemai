package com.scleroid.nemai.models;

/**
 * Created by Ganesh on 22/07/2017.
 */

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Parcel {
    @PrimaryKey(autoGenerate = true)
    private int serialNo;

    private String sourcePin;
    private String destinationPin;
    private String deliveryType;
    private String packageType;
    private String weight;
    private String invoice;
    private String length;
    private String width;
    private String height;
    private String description;

    public Parcel(String sourcePin, String destinationPin, String deliveryType, String packageType, String weight, String invoice, String length, String width, String height, String description) {
        this.sourcePin = sourcePin;
        this.destinationPin = destinationPin;
        this.deliveryType = deliveryType;
        this.packageType = packageType;
        this.weight = weight;
        this.invoice = invoice;
        this.length = length;
        this.width = width;
        this.height = height;
        this.description = description;
    }

    public Parcel() {

    }

    int getSerialNo() {
        return serialNo;
    }

    void setSerialNo(int pSerialNo) {
        serialNo = pSerialNo;
    }

    public String getSourcePin() {
        return sourcePin;
    }

    void setSourcePin(String pSourcePin) {
        sourcePin = pSourcePin;
    }

    public String getDestinationPin() {
        return destinationPin;
    }

    void setDestinationPin(String pDestinationPin) {
        destinationPin = pDestinationPin;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    void setDeliveryType(String pDeliveryType) {
        deliveryType = pDeliveryType;
    }

    public String getPackageType() {
        return packageType;
    }

    void setPackageType(String pPackageType) {
        packageType = pPackageType;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
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

    public void setDescription(String pkgDesc) {
        this.description = pkgDesc;
    }
}
