package tim.pickeyapp.custom_object;

import android.graphics.Bitmap;

/**
 * Created by Aviad on 28/05/2017.
 */
public class LabeledImage {
    private byte[] byteArrImage;
    private String labels;
    private String dateCreated;
    private Bitmap bitmapImage;


    public LabeledImage() {}

    // LabeledImage object for adapter
    public LabeledImage(byte[] byteArrImage, String labels, String dateCreated) {
        this.byteArrImage = byteArrImage;
        this.labels = labels;
        this.dateCreated = dateCreated;
    }

    public LabeledImage(Bitmap bitmapImage,String labels, String dateCreated) {
        this.bitmapImage = bitmapImage;
        this.labels = labels;
        this.dateCreated = dateCreated;
    }

    public LabeledImage(Bitmap bitmapImage, String dateCreated) {
        this.bitmapImage = bitmapImage;
        this.dateCreated = dateCreated;
    }

    public Bitmap getBitmapImage() {
        return bitmapImage;
    }

    public void setBitmapImage(Bitmap bitmapImage) {
        this.bitmapImage = bitmapImage;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public byte[] getByteArrImage() {
        return byteArrImage;
    }

    public void setByteArrImage(byte[] byteArrImage) {
        this.byteArrImage = byteArrImage;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }
}
