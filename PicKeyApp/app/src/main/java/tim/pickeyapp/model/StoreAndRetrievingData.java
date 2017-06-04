package tim.pickeyapp.model;

import java.util.ArrayList;

import io.realm.RealmResults;
import tim.pickeyapp.custom_object.LabeledImage;
import tim.pickeyapp.custom_object.LabeledImageRealm;

/**
 * Created by Aviad on 04/06/2017.
 */
public interface StoreAndRetrievingData {
    ArrayList<LabeledImageRealm> convertListForSaving(ArrayList<LabeledImage> listForSaving);
    ArrayList<LabeledImage> convertLoadedResults(RealmResults<LabeledImageRealm> results);
}
