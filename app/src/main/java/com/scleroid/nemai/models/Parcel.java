package com.scleroid.nemai.models;

/**
 * Created by Ganesh on 22/07/2017.
 */

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcelable;

import com.scleroid.nemai.utils.DateConverter;

import java.util.Date;
import java.util.Random;

@Entity
public class Parcel implements Parcelable {

    public static final Creator<Parcel> CREATOR = new Creator<Parcel>() {
        @Override
        public Parcel createFromParcel(android.os.Parcel in) {
            return new Parcel(in);
        }

        @Override
        public Parcel[] newArray(int size) {
            return new Parcel[size];
        }
    };
    @PrimaryKey
    private long serialNo;
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
    @TypeConverters(DateConverter.class)
    private Date parcelDate;
    @Ignore
    private PinCode sourcePinCode, destinationPinCode;

    @Ignore
    protected Parcel(android.os.Parcel in) {
        serialNo = in.readLong();
        sourcePin = in.readString();
        destinationPin = in.readString();
        deliveryType = in.readString();
        packageType = in.readString();
        weight = in.readInt();
        invoice = in.readInt();
        length = in.readInt();
        width = in.readInt();
        height = in.readInt();
        description = in.readString();
    }

    @Ignore
    public Parcel(String sourcePin, String destinationPin, String deliveryType, String packageType, int weight, int invoice, int length, int width, int height, String description, Date parcelDate, long serialNo, PinCode sourcePinCode, PinCode destinationPinCode) {
        initializeObject(sourcePin, destinationPin, deliveryType, packageType, weight, invoice, length, width, height, description, parcelDate, serialNo, sourcePinCode, destinationPinCode);

    }

    public Parcel(String sourcePin, String destinationPin, String deliveryType, String packageType, int weight, int invoice, int length, int width, int height, String description, Date parcelDate, long serialNo) {
        initializeObject(sourcePin, destinationPin, deliveryType, packageType, weight, invoice, length, width, height, description, parcelDate, serialNo, null, null);

    }

    //TODO handle data through serial no, implement viewmodel if things doesn't work. & Make changes(whatever that means)
    @Ignore
    public Parcel() {
        /*this.sourcePin = "null";
        this.destinationPin = "null";
        this.deliveryType = "Domestic";
        this.packageType = "Document";
        this.description = "null";
        this.serialNo = UUID.randomUUID();*/
        this("null", "null", "Domestic", "Document", 0, 0, 0, 0, 0, "null", new Date(), new Random().nextLong(), null, null);

    }

    //TODO remove this constructor, only for dummy data
    @Ignore
    public Parcel(String city, String city1, String domestic, String parcel, int positive, int positive1, int positive2, int positive3, int positive4, String s, Date birthday) {
        this(city, city1, domestic, parcel, positive, positive1, positive2, positive3, positive4, s, birthday, new Random().nextLong(), null, null);
    }

    public PinCode getSourcePinCode() {
        return sourcePinCode;
    }

    public void setSourcePinCode(PinCode sourcePinCode) {
        this.sourcePinCode = sourcePinCode;
    }

    public PinCode getDestinationPinCode() {
        return destinationPinCode;
    }

    public void setDestinationPinCode(PinCode destinationPinCode) {
        this.destinationPinCode = destinationPinCode;
    }

    public Parcel updateInstance(String sourcePin, String destinationPin, String deliveryType, String packageType, int weight, int invoice, int length, int width, int height, String description, Date parcelDate, long serialNo, PinCode sourcePinCode, PinCode destinationPinCode) {
        initializeObject(sourcePin, destinationPin, deliveryType, packageType, weight, invoice, length, width, height, description, parcelDate, serialNo, sourcePinCode, destinationPinCode);
        return this;

    }

    private void initializeObject(String sourcePin, String destinationPin, String deliveryType, String packageType, int weight, int invoice, int length, int width, int height, String description, Date parcelDate, long serialNo, PinCode sourcePinCode, PinCode destinationPinCode) {
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
        this.parcelDate = parcelDate;
        this.serialNo = serialNo;
        this.sourcePinCode = sourcePinCode;
        this.destinationPinCode = destinationPinCode;
    }

    public long getSerialNo() {
        return serialNo;
    }

    void setSerialNo(long pSerialNo) {
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

    public Date getParcelDate() {
        return parcelDate;
    }

    public void setParcelDate(Date parcelDate) {
        this.parcelDate = parcelDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel, int i) {
        parcel.writeLong(serialNo);
        parcel.writeString(sourcePin);
        parcel.writeString(destinationPin);
        parcel.writeString(deliveryType);
        parcel.writeString(packageType);
        parcel.writeInt(weight);
        parcel.writeInt(invoice);
        parcel.writeInt(length);
        parcel.writeInt(width);
        parcel.writeInt(height);
        parcel.writeString(description);
    }
}
