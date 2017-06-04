package tim.pickeyapp.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import io.realm.RealmResults;
import tim.pickeyapp.custom_object.LabeledImage;
import tim.pickeyapp.custom_object.LabeledImageRealm;

/**
 * Created by Aviad on 03/06/2017.
 */


public class StoreAndRetrievingDataImpl implements StoreAndRetrievingData {


    // Convert list with bitmap to list with byte[] for saving
    private ArrayList<LabeledImageRealm> prepareListForSaving(ArrayList<LabeledImage> labeledImages) {
        ArrayList<LabeledImageRealm> readyToSaveList = new ArrayList<>();
        for (int i = 0; i < labeledImages.size(); i++) {
            if (labeledImages.get(i).getBitmapImage() != null) {
                readyToSaveList.add(new LabeledImageRealm(convertBitmapToByteArray(labeledImages.get(i).getBitmapImage())
                        , labeledImages.get(i).getLabels(), labeledImages.get(i).getDateCreated()));
            }
        }
        return readyToSaveList;
    }

    @Override
    public ArrayList<LabeledImageRealm> convertListForSaving(ArrayList<LabeledImage> listForSaving) {
        if (listForSaving.size() != 0) {
            return prepareListForSaving(listForSaving);
        }
        return null;
    }


    // Convert list with byte[] to list with bitmap for loading
    private ArrayList<LabeledImage> prepareListForLoading(RealmResults<LabeledImageRealm> labeledImages) {
        ArrayList<LabeledImage> readyToLoadList = new ArrayList<>();
        for (int i = 0; i < labeledImages.size(); i++) {
            readyToLoadList.add(new LabeledImage(convertByteArrayToBitmap(labeledImages.get(i).getByteArrImage())
                    , labeledImages.get(i).getLabels(), labeledImages.get(i).getDateCreated()));
        }
        return readyToLoadList;
    }

    @Override
    public ArrayList<LabeledImage> convertLoadedResults(RealmResults<LabeledImageRealm> results) {
        return prepareListForLoading(results);
    }


    private Bitmap convertByteArrayToBitmap(byte[] byteArrImage) {
        return BitmapFactory.decodeByteArray(byteArrImage, 0, byteArrImage.length);
    }

    private byte[] convertBitmapToByteArray(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }


}
