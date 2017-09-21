package info.androidhive.navigationdrawer.models;

/**
 * Created by scleroid on 15/9/17.
 */

public class PinCode {

    private String location, pincode;

    public PinCode(String location, String pincode) {
        this.location = location;
        this.pincode = pincode;

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
