package tim.pickeyapp.model;

import java.io.IOException;

import tim.pickeyapp.custom_object.LabeledImage;

/**
 * Created by Aviad on 28/05/2017.
 */
public interface GoogleLabelGenerator {

    void callCloudVision(LabeledImage bitmapWithDateObj, OnFinishListener onFinishListener) throws IOException;

    interface OnFinishListener {
        void onFinished(LabeledImage labeledImage);
    }
}
