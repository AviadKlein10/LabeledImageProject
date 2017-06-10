package tim.pickeyapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialcamera.MaterialCamera;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import tim.pickeyapp.custom_object.LabeledImage;
import tim.pickeyapp.custom_object.LabeledImageRealm;
import tim.pickeyapp.model.AdapterImagesList;
import tim.pickeyapp.model.ConvertImageUriToBitmapImpl;
import tim.pickeyapp.model.GoogleLabelGeneratorImpl;
import tim.pickeyapp.model.StoreAndRetrievingDataImpl;

public class MainActivity extends AppCompatActivity implements MainView, View.OnClickListener {

    private final int CAMERA_RC = 6969;;
    private final int GALLERY_RC = 1;
    private final int CAMERA_PERMISSION_CODE = 0;
    private MainPresenter presenter;
    private RecyclerView listImages;
    private AdapterImagesList adapterImagesList;
    private ArrayList<LabeledImage> arrLabeledImages;
    private FloatingActionMenu menuBlue;
    private FloatingActionButton fab_camera, fab_gallery;

    private EditText editSearch;
    private ImageView iconSearch;
    private TextView txtTip;
    private int counterNewItemLine = 0;
    private Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(this);
        presenter = new MainPresenterImpl(this, new GoogleLabelGeneratorImpl(), new ConvertImageUriToBitmapImpl(), new StoreAndRetrievingDataImpl());
        realm = Realm.getDefaultInstance();
        initViews();
    }

    private void initViews() {
        listImages = (RecyclerView) findViewById(R.id.listLabeledImages);
        menuBlue = (FloatingActionMenu) findViewById(R.id.menu_blue);
        fab_camera = (FloatingActionButton) findViewById(R.id.fab_camera);
        fab_gallery = (FloatingActionButton) findViewById(R.id.fab_gallery);
        editSearch = (EditText) findViewById(R.id.editSearch);
        iconSearch = (ImageView) findViewById(R.id.iconSearch);
        txtTip = (TextView) findViewById(R.id.txtEmptyList);

        fab_camera.setOnClickListener(this);
        fab_gallery.setOnClickListener(this);
        iconSearch.setOnClickListener(this);
        listImages.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listImages.setLayoutManager(linearLayoutManager);
        arrLabeledImages = new ArrayList<>();
        arrLabeledImages = loadList();

        //Show textTip if the list is empty
        if (arrLabeledImages.size() == 0) {
            txtTip.setVisibility(View.VISIBLE);
        }else{
            txtTip.setVisibility(View.GONE);
        }
        adapterImagesList = new AdapterImagesList(this, arrLabeledImages, this);

        listImages.setAdapter(adapterImagesList);
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override //Perform search on adapter when user type in editSearch
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() == 0) {
                    adapterImagesList.initList();
                    iconSearch.setImageResource(R.drawable.ic_search);
                } else {
                    adapterImagesList.performSearch(charSequence.toString().trim().toLowerCase());
                    iconSearch.setImageResource(R.drawable.ic_cancel);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    // Load last list
    private ArrayList<LabeledImage> loadList() {
        RealmQuery<LabeledImageRealm> query = realm.where(LabeledImageRealm.class);
        RealmResults<LabeledImageRealm> results = query.findAll();
        return presenter.onLoadList(results);
    }

    // Intent for library
    private void selectFromLibrary() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_RC);
    }

    // Intent for camera
    private void takePhoto() {
             new MaterialCamera(this)
                .stillShot() // launches the Camera in stillshot mode
                .start(CAMERA_RC);
    }

    @Override // Permission from user to use Camera and Gallery
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            }
        }
        if (requestCode == GALLERY_RC) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectFromLibrary();
            }
        }
    }

    @Override // Add item to list when item is ready
    public void addItem(LabeledImage newLabeledImage) {
        adapterImagesList.addNewItem(newLabeledImage, counterNewItemLine);
        counterNewItemLine--;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_gallery:
                if (isTakePictureAllow())
                    selectFromLibrary();
                menuBlue.close(true);
                break;
            case R.id.fab_camera:
                if (isTakePictureAllow())
                    takePhoto();
                menuBlue.close(true);
                break;
            case R.id.iconSearch:
                editSearch.setText("");
                break;
            default:
                editSearch.clearFocus();
                break;
        }
    }

    // Check if permissions accepted
    private boolean isTakePictureAllow() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
            }
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_RC);
            }
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }


    @Override // Handle the result after capture image using camera or pick image from gallery
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_RC || requestCode == GALLERY_RC) {
                try {
                    presenter.onImageUriReady(data.getData(), this, requestCode);
                    listImages.smoothScrollToPosition(0);
                    // Creating a loading item
                    adapterImagesList.addLoadingItem();
                    // Count the loading items to keep the order of new ready items
                    counterNewItemLine++;
                    txtTip.setVisibility(View.GONE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override  // Send intent with item info and transition info
    public void onImageItemClick(int i, String str, View txtView, View imgView, LabeledImage labeledImage) {
        menuBlue.close(true);
        Intent intent = new Intent(MainActivity.this, LargeImageActivity.class);
        intent.putExtra("txt", str);
        intent.putExtra("date", labeledImage.getDateCreated());
        String filename = "bitmap.png";
        FileOutputStream stream = null;
        try {

            stream = openFileOutput(filename, Context.MODE_PRIVATE);
            Bitmap bitmap = labeledImage.getBitmapImage();
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            }
            //Cleanup
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        intent.putExtra("img", filename);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Pair<View, String> pair1 = Pair.create(txtView, ViewCompat.getTransitionName(txtView));
            Pair<View, String> pair2 = Pair.create(imgView, ViewCompat.getTransitionName(imgView));
            ActivityOptionsCompat options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,
                            pair1, pair2
                    );
            ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    private void saveList() {
        // Delete previous saved list
        if (arrLabeledImages.size() != 0) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<LabeledImageRealm> result = realm.where(LabeledImageRealm.class).findAll();
                    result.deleteAllFromRealm();
                }
            });
            final ArrayList<LabeledImageRealm> arrtem = presenter.onSaveList(arrLabeledImages);
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(arrtem);
                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        editSearch.clearFocus();
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        saveList();
        super.onPause();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}