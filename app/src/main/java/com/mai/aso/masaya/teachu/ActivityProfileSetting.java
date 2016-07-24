package com.mai.aso.masaya.teachu;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.mai.aso.masaya.teachu.ImageEdit.RoundImage;

import java.io.IOException;

/**
 * Created by MasayaAso on 7/24/16.
 */
public class ActivityProfileSetting extends AppCompatActivity {

    private static final int RESULT_PICK_IMAGEFILE = 1001;
    private ImageView imageViewProfile;
    private TextView btn_select_image;
    private Uri m_uri;
    private static final int REQUEST_CHOOSER = 1000;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseStorage storage;
    private StorageReference mStorageReference;
    private StorageReference mStorageReferenceImages;
    private FloatingActionButton floatingActionButton;
    private static final String TAG = ActivityProfileSetting.class.getSimpleName();
    private boolean imageUploaded = false;
    private RoundImage roundImage;
    private Bitmap resultBitmap;

    private ProgressBar progressBar;
    private Button btnLanguageSetting;
    private LoginButton loginButton;
    private Spinner spinner_country, spinner_native_language, spinner_learn_language, spinner_learn_language2;
    private String setCountry, setNativeLanguage, setLearnLanguage, setLearnLanguage2;
    //private static final String TAG = ActivityLogin.class.getSimpleName();

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the view
        setContentView(R.layout.activity_profile_setting);

        imageViewProfile = (ImageView)findViewById(R.id.profile_image);
        floatingActionButton = (FloatingActionButton)findViewById(R.id.floating_next_btn);
        //btn_select_image = (TextView)findViewById(R.id.btn_photo_from_gallery);
        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGallery();
            }
        });

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        mStorageReference = storage.getReference();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityProfileSetting.this, ActivityMainTab.class);
                startActivity(intent);
            }
        });
    }

    private void showGallery(){
        //カメラIntentの起動準備
        String photoName = System.currentTimeMillis() + ".jpg";
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, photoName);
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        m_uri = getContentResolver()
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, m_uri);

        //ギャラリーIntentの起動準備
        Intent intentGallery;
        if (Build.VERSION.SDK_INT < 19) {
            intentGallery = new Intent(Intent.ACTION_GET_CONTENT);
            intentGallery.setType("image/*");
        } else {
            intentGallery = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intentGallery.addCategory(Intent.CATEGORY_OPENABLE);
            intentGallery.setType("image/jpeg");
        }
        Intent intent = Intent.createChooser(intentCamera, "Select Image");
        intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {intentGallery});
        startActivityForResult(intent, REQUEST_CHOOSER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CHOOSER) {
            if(resultCode != RESULT_OK) {
                // キャンセル時
                Toast.makeText(ActivityProfileSetting.this, getString(R.string.profile_failed), Toast.LENGTH_LONG).show();
                return ;
            }
            Uri resultUri = (data != null ? data.getData() : m_uri);
            if(resultUri == null) {
                // 取得失敗
                Toast.makeText(ActivityProfileSetting.this, getString(R.string.profile_failed), Toast.LENGTH_LONG).show();
                return;
            }

            // ギャラリーへスキャンを促す
            MediaScannerConnection.scanFile(
                    this,
                    new String[]{resultUri.getPath()},
                    new String[]{"image/jpeg"},
                    null
            );

            // 画像を設定
            imageViewProfile.setImageURI(resultUri);
            /*
            try {
                resultBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                roundImage = new RoundImage(resultBitmap);
                imageViewProfile.setImageDrawable(roundImage);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            uploadFile(resultUri);
        }
    }

    private void uploadFile(Uri pUri) {
        storage = FirebaseStorage.getInstance();
        mStorageReference = storage.getReference();
        mStorageReferenceImages = mStorageReference.child("profiles").child(user.getUid()).child(getString(R.string.profile_photo));
        //showHorizontalProgressDialog("Uploading", "Please wait...");
        //StorageReference uploadStorageReference = mStorageReference.child(pUri.getLastPathSegment());
        //final UploadTask uploadTask = uploadStorageReference.putFile(pUri);
        final UploadTask uploadTask = mStorageReferenceImages.putFile(pUri);
        Log.d(TAG, "Profile:pUri: " + pUri);
        Log.d(TAG, "Profile:Reference: " + mStorageReferenceImages);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot pTaskSnapshot) {
                Log.d(TAG, "uploadFileonSuccess:" + pTaskSnapshot);
                Toast.makeText(ActivityProfileSetting.this, getString(R.string.profile_upload_success), Toast.LENGTH_LONG).show();
                Uri downloadUrl = pTaskSnapshot.getDownloadUrl();
                //hideProgressDialog();
                if (downloadUrl != null) {
                    imageUploaded = true;
                    //btnDownload.setEnabled(true);
                    //Glide.with(context).load(downloadUrl).into(imageView);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception pE) {
                Log.d(TAG, "uploadFileonFailure:" + pE);
                Toast.makeText(ActivityProfileSetting.this, getString(R.string.profile_upload_fail), Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot pTaskSnapshot) {
                Log.d(TAG, "uploadFileonProgress:" + pTaskSnapshot);

                //progress中のアニメーションかな？たぶん
                //int progress = (int) (100 * (float) pTaskSnapshot.getBytesTransferred() / pTaskSnapshot.getTotalByteCount());
                //updateProgress(progress);
            }
        });

    }

}

