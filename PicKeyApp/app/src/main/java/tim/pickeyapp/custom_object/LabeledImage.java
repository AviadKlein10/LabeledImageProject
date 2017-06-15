package tim.pickeyapp.custom_object;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Aviad on 28/05/2017.
 */
public class LabeledImage extends RealmObject {
    private byte[] byteArrImage;
    private String labels;
    private String dateCreated;
    @PrimaryKey
    private String filePath;

    public LabeledImage(String labels, String dateCreated, String filePath) {
        this.labels = labels;
        this.dateCreated = dateCreated;
        this.filePath = filePath;
    }



    public LabeledImage(String dateCreated, String filePath) {
        this.dateCreated = dateCreated;
        this.filePath = filePath;

    }



    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }









    public LabeledImage() {}

    // LabeledImage object for adapter
    public LabeledImage(byte[] byteArrImage, String labels, String dateCreated) {
        this.byteArrImage = byteArrImage;
        this.labels = labels;
        this.dateCreated = dateCreated;
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
