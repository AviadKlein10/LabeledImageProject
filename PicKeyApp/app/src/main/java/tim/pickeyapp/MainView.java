package tim.pickeyapp;

import android.view.View;

import tim.pickeyapp.custom_object.LabeledImage;

/**
 * Created by Aviad on 28/05/2017.
 */
public interface MainView {

    void addItem(LabeledImage newLabledImage);
    void onImageItemClick(int position, String s, View txtView,View imgView, LabeledImage labeledImage);
}
