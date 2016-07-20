package com.mai.aso.masaya.teachu;

import android.content.Intent;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.firebase.client.Firebase;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.OnProgressListener;

import com.mai.aso.masaya.teachu.info.FirebaseInfo;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.util.Arrays;
import java.lang.Object;
import java.util.logging.LogManager;

/**
 * Created by MasayaAso on 7/3/16.
 */
public class ActivityLogin extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseStorage storage;
    private StorageReference mStorageReference;
    private StorageReference mStorageReferenceImages;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private JSONObject storeObject;
    private boolean permissionIntent = false;
    private boolean imageUploaded = false;
    public String email, firstname, lastname, gender, birthday;
    private static final String TAG = ActivityLogin.class.getSimpleName();


    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        AppEventsLogger.activateApp(this);

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        //final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        //get firebase auth instance
        //if (auth.getCurrentUser() != null) {
        //    startActivity(new Intent(ActivityLogin.this, ActivityMainTab.class));
        //    finish();
        //}

        //set the view
        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        //info = (TextView) findViewById(R.id.information);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityLogin.this, ActivitySignup.class));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityLogin.this, ActivityResetPassword.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password", Toast.LENGTH_LONG).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(ActivityLogin.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(ActivityLogin.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Intent intent = new Intent(ActivityLogin.this, ActivityMainTab.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });

        //info.setText("INIT:");

        //facebook log in
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                Toast.makeText(ActivityLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                //info.setText("User ID: " + loginResult.getAccessToken().getUserId());

                //facebook login and add to firebase auth
                handleFacebookAccessToken(loginResult.getAccessToken());

                progressBar.setVisibility(View.VISIBLE);
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject jsonObject, GraphResponse response) {
                                email = jsonObject.optString("email");
                                firstname = jsonObject.optString("first_name");
                                lastname = jsonObject.optString("last_name");
                                birthday = jsonObject.optString("birthday");
                                gender = jsonObject.optString("gender");
                                storeObject = response.getJSONObject();
                                Log.d(TAG, "FacebookProfile:storeObject :" + storeObject);

                                try {
                                    JSONObject data = response.getJSONObject();
                                    storeObject = response.getJSONObject();
                                    if (data.has("picture")) {
                                        String ResourceUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");
                                        URL ProfileUrl = new URL(ResourceUrl);
                                        Log.d(TAG, "FacebookProfile:ResourceUrl: " + ResourceUrl);
                                        Uri profileUri = Uri.parse(ResourceUrl);
                                        Log.d(TAG, "FacebookProfile:ProfileUri: " + profileUri);
                                        Bitmap profileBitmap = BitmapFactory.decodeStream(ProfileUrl.openConnection().getInputStream());
                                        Log.d(TAG, "FacebookProfile:ProfileBitmap: " + profileBitmap.toString());
                                        uploadFile(profileUri);
                                        //Bitmap profilePic = BitmapFactory.decodeStream(new URL(profilePicUrl).openConnection().getInputStream());
                                        //imagesRef.putStream(bufferedInputStream);
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                                mRootRef.child(FirebaseInfo.CHILD_USERS).child(user.getUid()).child(FirebaseInfo.USER_EMAIL).setValue(email);
                                mRootRef.child(FirebaseInfo.CHILD_USERS).child(user.getUid()).child(FirebaseInfo.USER_FIRST_NAME).setValue(firstname);
                                mRootRef.child(FirebaseInfo.CHILD_USERS).child(user.getUid()).child(FirebaseInfo.USER_LAST_NAME).setValue(lastname);
                                mRootRef.child(FirebaseInfo.CHILD_USERS).child(user.getUid()).child(FirebaseInfo.USER_BIRTHDAY).setValue(birthday);
                                mRootRef.child(FirebaseInfo.CHILD_USERS).child(user.getUid()).child(FirebaseInfo.USER_GENDER).setValue(gender);

                                //Toast.makeText(ActivityLogin.this, birthday, Toast.LENGTH_LONG).show();
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday,first_name,last_name,picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();
                permissionIntent = true;
                progressBar.setVisibility(View.GONE);

                String accessToken = loginResult.getAccessToken().getToken();

                Intent intent = new Intent(ActivityLogin.this, ActivityMainTab.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onCancel() {
                Toast.makeText(ActivityLogin.this, "cancel", Toast.LENGTH_SHORT).show();
                //info.setText("Cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(ActivityLogin.this, "error", Toast.LENGTH_SHORT).show();
                //info.setText("Error");
            }
        });

        //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(ActivityLogin.this, "unkounko", Toast.LENGTH_SHORT).show();
    }


    private void handleFacebookAccessToken(final AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        //Toast.makeText(ActivityLogin.this, "bakabakabakabaka", Toast.LENGTH_LONG).show();

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            //Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(ActivityLogin.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Log.d(TAG, "onStart");
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ActivityLogin Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.mai.aso.masaya.teachu/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Log.d(TAG, "onStop");
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ActivityLogin Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.mai.aso.masaya.teachu/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private void uploadFile(Uri pUri) {
        mStorageReference = storage.getReferenceFromUrl("gs://project-66993355093102411.appspot.com/");
        mStorageReferenceImages = mStorageReference.child("Images").child("profile.png");
        //showHorizontalProgressDialog("Uploading", "Please wait...");
        StorageReference uploadStorageReference = mStorageReference.child(pUri.getLastPathSegment());
        //final UploadTask uploadTask = uploadStorageReference.putFile(pUri);
        final UploadTask uploadTask = mStorageReferenceImages.putFile(pUri);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot pTaskSnapshot) {
                Log.d(TAG, "uploadFileonSuccess:" + pTaskSnapshot);
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

    /*
    public static Bitmap getFacebookProfilePicture(String userID){
        URL imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
        Bitmap bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());

        return bitmap;
    }

    Bitmap bitmap = getFacebookProfilePicture(userId);
    */


}
