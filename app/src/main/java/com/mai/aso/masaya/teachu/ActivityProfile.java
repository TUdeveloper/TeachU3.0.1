package com.mai.aso.masaya.teachu;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

/**
 * Created by MasayaAso on 8/29/16.
 */
public class ActivityProfile extends AppCompatActivity {

    private static final int RESULT_PICK_IMAGEFILE = 1001;
    private ImageView backButton, editButton;
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
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        //toolbar.getBackground().setAlpha(100);
        //toolbar.setLogo(R.drawable.ic_friend_1_white);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        backButton = (ImageView)findViewById(R.id.act_profile_back_btn);
        editButton = (ImageView)findViewById(R.id.act_profile_edit_btn);
        //btn_select_image = (TextView)findViewById(R.id.btn_photo_from_gallery);

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        mStorageReference = storage.getReference();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityProfile.this, ActivityProfileSetting.class);
                startActivity(intent);
            }
        });
    }

    //今はmeeting card menuを使ってるけど、変更させる必要あり！
    // TODO:阿蘓 プロフィール用のmenu_profile
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //ミーティングカード用のメニュータブをセレクト
        getMenuInflater().inflate(R.menu.menu_meeting_card, menu);

        return true;
    }

    //戻るボタンを押したときに前のアクティビティに戻る（注意：R.id.では無くandroid.R.idってなってる）
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}


