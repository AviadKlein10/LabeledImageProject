package tim.pickeyapp.model;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import tim.pickeyapp.custom_object.LabeledImage;

/**
 * Created by Aviad on 28/05/2017.
 */

public class ConvertImageUriToBitmapImpl implements ConvertImageUriToBitmap {

    final private int CAMERA_RQ = 6969;


    @Override // Convert uri to bitmap and pull date info
    public LabeledImage convertUriToBitmapWithDate(Uri imageUri, Context context, int requestCode) {
        String dateCreated = "";
        String imagePath = "";

        try {
            if(requestCode== CAMERA_RQ){
                imagePath= getRealPathFromURI_CameraSource(context, imageUri);

                // Get path if uri received from Gallery | Handle two type of image URI
            }else if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT){
                if(imageUri.toString().contains("external")){
                    imagePath= getRealPathFromURI_API19_ExternalSource(context, imageUri);
                }else{
                    imagePath= getRealPathFromURI_API19(context, imageUri);
                }
            }else{
                imagePath= getRealPathFromURI_APIbelow19(context, imageUri);
            }

            dateCreated =pullDateFromUri(imagePath);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return new LabeledImage(dateCreated,imagePath);

    }



    private String pullDateFromUri(String imagePath) throws IOException, ParseException {
        ExifInterface ei = new ExifInterface(imagePath);
        String strDate = " ";
      try {
          strDate = ei.getAttribute(ExifInterface.TAG_DATETIME);
          SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:dd hh:mm:ss");
          Date newDate = format.parse(strDate);
          format = new SimpleDateFormat("dd/MM/yyyy");
          String date = format.format(newDate);
          return date + "";
      }catch(NullPointerException e){
          Log.e("nullPointerDate", e.toString());
      }
        return "unknown";
    }




    private String getRealPathFromURI_CameraSource(Context context,Uri contentURI) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String getRealPathFromURI_API19_ExternalSource(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {

            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = 0;
            if (cursor != null) {
                column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String getRealPathFromURI_API19(Context context, Uri uri){
       String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];
        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    private String getRealPathFromURI_APIbelow19(Context context, Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(
                context,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if(cursor != null){
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }
        return result;
    }

}
