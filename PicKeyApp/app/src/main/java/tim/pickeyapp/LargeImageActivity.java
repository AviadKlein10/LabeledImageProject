package tim.pickeyapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;

/**
 * Created by Aviad on 31/05/2017.
 */

public class LargeImageActivity extends AppCompatActivity {

    private TextView txtKeywords, txtDate;
    private  ImageView largeImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large_image);
        initViews(getIntent());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initViews(Intent intent) {

        String strKeywords = "Keywords: " + intent.getStringExtra("txt");
        String strDateOfCreation = "Date of creation: " + intent.getStringExtra("date");

        Bitmap bmp = null;
        String filename = intent.getStringExtra("img");
        try {
            FileInputStream is = this.openFileInput(filename);
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        txtKeywords = (TextView) findViewById(R.id.txtLabelsLarge);
        txtDate = (TextView) findViewById(R.id.txtDateOfCreation);
        largeImageView = (ImageView) findViewById(R.id.imgLarge);

        txtDate.setText(strDateOfCreation);
        txtKeywords.setMovementMethod(new ScrollingMovementMethod());
        txtKeywords.setText(strKeywords);
        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/DroidSans.ttf");
        txtKeywords.setTypeface(customFont);
        largeImageView.setImageBitmap(bmp);
        largeImageView.setDrawingCacheEnabled(false);
    }


    @Override // Add 'back' icon to LargeImageActivity's bar
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
