package tim.pickeyapp.model;

import android.content.Context;
import android.net.Uri;

import tim.pickeyapp.custom_object.LabeledImage;

/**
 * Created by Aviad on 28/05/2017.
 */
public interface ConvertImageUriToBitmap {

    LabeledImage convertUriToBitmapWithDate(Uri imageUri, Context context, int requestCode);
}
