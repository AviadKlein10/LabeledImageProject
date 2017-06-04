package tim.pickeyapp;

import android.content.Context;
import android.net.Uri;

import java.io.IOException;
import java.util.ArrayList;

import io.realm.RealmResults;
import tim.pickeyapp.custom_object.LabeledImage;
import tim.pickeyapp.custom_object.LabeledImageRealm;

/**
 * Created by Aviad on 28/05/2017.
 */

public interface MainPresenter {

    void onImageUriReady(Uri data, Context context, int requestCode) throws IOException;
   ArrayList<LabeledImageRealm> onSaveList(ArrayList<LabeledImage> labeledImages );
   ArrayList<LabeledImage> onLoadList(RealmResults<LabeledImageRealm> results);
}
