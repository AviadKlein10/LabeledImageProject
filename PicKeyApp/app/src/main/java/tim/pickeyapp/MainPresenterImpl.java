package tim.pickeyapp;

import android.content.Context;
import android.net.Uri;

import java.io.IOException;
import java.util.ArrayList;

import io.realm.RealmResults;
import tim.pickeyapp.model.ConvertImageUriToBitmap;
import tim.pickeyapp.model.GoogleLabelGenerator;
import tim.pickeyapp.model.GoogleLabelGeneratorImpl;
import tim.pickeyapp.custom_object.LabeledImage;
import tim.pickeyapp.custom_object.LabeledImageRealm;
import tim.pickeyapp.model.StoreAndRetrievingData;

/**
 * Created by Aviad on 28/05/2017.
 */

public class MainPresenterImpl implements MainPresenter, GoogleLabelGenerator.OnFinishListener {

    private MainView mainView;
    private GoogleLabelGenerator googleLabelGenerator;
    private ConvertImageUriToBitmap convertImageUriToBitmap;
    private StoreAndRetrievingData storeAndRetrievingData;


    public MainPresenterImpl(MainView mainView, GoogleLabelGeneratorImpl googleLabelGenerator, ConvertImageUriToBitmap convertImageUriToBitmap, StoreAndRetrievingData storeAndRetrievingData) {
        this.mainView = mainView;
        this.googleLabelGenerator = googleLabelGenerator;
        this.convertImageUriToBitmap = convertImageUriToBitmap;
        this.storeAndRetrievingData = storeAndRetrievingData;

    }

    @Override  //Use google api vision when image bitmap converted to uri
    public void onImageUriReady(Uri dataUri, Context context, int requestCode) throws IOException {
        googleLabelGenerator.callCloudVision(convertImageUriToBitmap.convertUriToBitmapWithDate(dataUri, context, requestCode), this);
    }


    @Override  //Add labeledItem to view when done receiving information
    public void onFinished(LabeledImage labeledImage) {
        mainView.addItem(labeledImage);
    }


    public ArrayList<LabeledImageRealm> onSaveList(ArrayList<LabeledImage> labeledImages) {
       return storeAndRetrievingData.convertListForSaving(labeledImages);
    }


    public ArrayList<LabeledImage> onLoadList(RealmResults<LabeledImageRealm> results) {
        return storeAndRetrievingData.convertLoadedResults(results);
    }

}
