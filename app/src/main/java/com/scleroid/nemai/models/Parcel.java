package com.scleroid.nemai.models;

/**
 * Created by Ganesh on 22/07/2017.
 */

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Parcel {
    @PrimaryKey(autoGenerate = true)
    private int serialNo;

    private String sourcePin;
    private String destinationPin;
    private String deliveryType;
    private String packageType;
    private int weight;
    private int invoice;
    private int length;
    private int width;
    private int height;
    private String description;

    public Parcel(String sourcePin, String destinationPin, String deliveryType, String packageType, int weight, int invoice, int length, int width, int height, String description) {
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

    @Ignore
    public Parcel() {
        this.sourcePin = "null";
        this.destinationPin = "null";
        this.deliveryType = "null";
        this.packageType = "null";
        this.description = "null";

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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getInvoice() {
        return invoice;
    }

    public void setInvoice(int invoice) {
        this.invoice = invoice;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String pkgDesc) {
        this.description = pkgDesc;
    }
}
