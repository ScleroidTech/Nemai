package info.androidhive.navigationdrawer.models;

/**
 * Created by OM on 22/07/2017.
 */
public class Parcel {
    private String weight;
    private String invoice;
    private String length;
    private String width;
    private String height;
    private String pkgDesc;

    public Parcel(String weight,String invoice,String length,String width,String height,String pkgDesc){
        this.weight = weight;
        this.invoice = invoice;
        this.length = length;
        this.width = width;
        this.height = height;
        this.pkgDesc = pkgDesc;
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

    public String getPkgDesc() {
        return pkgDesc;
    }

    public void setPkgDesc(String pkgDesc) {
        this.pkgDesc = pkgDesc;
    }
}
