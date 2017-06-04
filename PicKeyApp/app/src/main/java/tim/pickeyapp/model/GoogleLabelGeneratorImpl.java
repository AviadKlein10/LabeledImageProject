package tim.pickeyapp.model;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tim.pickeyapp.custom_object.LabeledImage;

/**
 * Created by Aviad on 28/05/2017.
 */

public class GoogleLabelGeneratorImpl implements GoogleLabelGenerator    {

    private String CLOUD_VISION_API_KEY;
    private Bitmap scaledBitmap;
    private String dateCreated;

    @Override
    public void callCloudVision(LabeledImage bitmapWithDateObj, final OnFinishListener onFinishListener) throws IOException {

        CLOUD_VISION_API_KEY ="AIzaSyDkPD_rauwdC_m5ySY8KbSSMkQM192HRDI";
       final Bitmap bitmap = bitmapWithDateObj.getBitmapImage();
        dateCreated = bitmapWithDateObj.getDateCreated();
        new AsyncTask<Object, Void, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Object... params) {

                try {
                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                    Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                    builder.setVisionRequestInitializer(new
                            VisionRequestInitializer(CLOUD_VISION_API_KEY));
                    Vision vision = builder.build();

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                            new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
                        AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

                        // Add the image for google vision api
                        com.google.api.services.vision.v1.model.Image base64EncodedImage = new com.google.api.services.vision.v1.model.Image();
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
                        scaledBitmap = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
                        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        byte[] imageBytes = byteArrayOutputStream.toByteArray();

                        // Base64 encode the JPEG
                        base64EncodedImage.encodeContent(imageBytes);
                        annotateImageRequest.setImage(base64EncodedImage);

                        // add the features we want
                        annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                            Feature labelDetection = new Feature();
                            labelDetection.setType("LABEL_DETECTION");//"LABEL_DETECTION"
                            labelDetection.setMaxResults(10);
                            add(labelDetection);
                        }});

                        // Add the list of one item to the request
                        add(annotateImageRequest);
                    }});

                    Vision.Images.Annotate annotateRequest =
                            vision.images().annotate(batchAnnotateImagesRequest);
                    annotateRequest.setDisableGZipContent(true);

                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    return parseResultToString(response);

                } catch (GoogleJsonResponseException e) {
                    return "Cloud Vision API request failed. Check logs for details.";
                }catch (IOException e) {

                    return "Cloud Vision API request failed. Check logs for details.";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return "Cloud Vision API request failed. Check logs for details.";

            }

            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                switch (result) {
                    case "Cloud Vision API request failed. Check logs for details.":
                        //error
                        onCloudVisionError();  //displays error message to user
                        onFinishListener.onFinished(new LabeledImage(scaledBitmap,"error", dateCreated));
                        break;
                    default:
                        onFinishListener.onFinished(new LabeledImage(scaledBitmap,result,dateCreated));
                        break;
                }
            }
        }.execute();
    }


    private String parseResultToString(BatchAnnotateImagesResponse response)  throws JSONException {
            String newResultStr = "";
            JSONObject jsonObject;
            List<EntityAnnotation> labellabels = response.getResponses().get(0).getLabelAnnotations();
            for (int i = 0; i < labellabels.size(); i++) {
                jsonObject = new JSONObject(labellabels.get(i));
                Log.d("resopeso", jsonObject.getString("description"));
                if(i!=labellabels.size()-1){
                    newResultStr+= jsonObject.getString("description") + ", ";
                }else{
                    newResultStr+= jsonObject.getString("description");
                }
            }
            return newResultStr;

    }

    private void onCloudVisionError() {
        Log.e("error", "CloudVisionError");
    }



}
