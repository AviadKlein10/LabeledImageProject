package tim.pickeyapp;

import android.content.Context;
import android.net.Uri;

import java.io.IOException;

/**
 * Created by Aviad on 28/05/2017.
 */

public interface MainPresenter {

    void onImageUriReady(Uri data, Context context, int requestCode) throws IOException;
}
