package tim.pickeyapp.custom_object;

import io.realm.RealmObject;

/**
 * Created by Aviad on 04/06/2017.
 */

public class LabeledImageRealm extends RealmObject {
    private byte[] byteArrImage;
    private String labels;
    private String dateCreated;

    public LabeledImageRealm(){}

    // LabeledImage object to handle saving list using realm
    public LabeledImageRealm(byte[] byteArrImage, String labels, String dateCreated) {
        this.byteArrImage = byteArrImage;
        this.labels = labels;
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

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
