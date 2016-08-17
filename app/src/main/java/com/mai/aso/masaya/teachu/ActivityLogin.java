package com.mai.aso.masaya.teachu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mai.aso.masaya.teachu.info.FirebaseInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

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
    private JSONArray educationArray;
    private JSONObject storeObject;
    private ProgressDialog pDialog;
    private Uri profileUri;
    private boolean permissionIntent = false;
    private boolean imageUploaded = false;
    public String email, firstname, lastname, gender, birthday, school, ResourceUrl;
    private static final String TAG = ActivityLogin.class.getSimpleName();
    private static final String SCHOOL = "school";

    //comment comment
    //ブランチをきる
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
        //ここから４つ下までは、もし権限があればすぐにメインのタブに飛ぶ
        //今は開発段階なので、コメントアウトしている
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

        //登録画面へ飛ぶ
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityLogin.this, ActivitySignup.class));
            }
        });

        //パスワード再設定画面へ飛ぶ
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityLogin.this, ActivityResetPassword.class));
            }
        });

        //メールとパスワードでログイン
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

        //facebookでログイン
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
                                try {
                                    educationArray = jsonObject.getJSONArray("education");
                                    for (int i = 0; i < educationArray.length(); i++){
                                        JSONObject educationObject = educationArray.getJSONObject(i);
                                        JSONObject school = educationObject.getJSONObject("school");
                                        String school_name = school.getString("name");
                                        Log.d(TAG, "SchoolName :" + school_name);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                storeObject = response.getJSONObject();
                                Log.d(TAG, "FacebookProfile:storeObject :" + storeObject);

                                //ProfilePictureView profilePictureView;
                                //profilePictureView;
                                /*
                                try {
                                    if (storeObject.has("picture")) {
                                        //Log.d(TAG, "FacebookProfile:storeObject2 :" + storeObject);
                                        //String ResourceUrl = storeObject.getJSONObject("picture").getJSONObject("data").getString("url");
                                        //Bitmap bitmap = DownloadImage(ResourceUrl);
                                        //Bitmap bitmap = BitmapFactory.decodeStream(new URL(ResourceUrl).openConnection().getInputStream());
                                        //ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                        //byte[] profileData = baos.toByteArray();
                                        //uploadBitmap(profileData);
                                        //InputStream stream = new FileInputStream(new File(ResourceUrl));
                                        //uploadStream(stream);
                                        //URL ProfileUrl = new URL(ResourceUrl.toString());
                                        //HttpURLConnection connection = (HttpURLConnection) ProfileUrl.openConnection();
                                        //connection.setDoInput(true);
                                        //connection.connect();
                                        //InputStream inputStream = connection.getInputStream();
                                        //uploadStream(inputStream);

                                        //Bitmap bitmap = getBitmapFromURL(ResourceUrl);
                                        //Log.d(TAG, "FacebookProfile:ResourceUrl: " + ResourceUrl);
                                        //profileUri = Uri.parse(ResourceUrl);
                                        //Log.d(TAG, "FacebookProfile:ProfileUri: " + profileUri);
                                        //Bitmap profileBitmap = BitmapFactory.decodeStream(ProfileUrl.openConnection().getInputStream());
                                        //Log.d(TAG, "FacebookProfile:ProfileBitmap: " + profileBitmap.toString());
                                        //uploadFile(profileUri);
                                        //Bitmap profilePic = BitmapFactory.decodeStream(new URL(profilePicUrl).openConnection().getInputStream());
                                        //imagesRef.putStream(bufferedInputStream);


                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }*/
                                //Facebookでログインした後にfirebaseに以下の情報を登録しているemail, first name last name birthday gender
                                //増やす可能性あり
                                mRootRef.child(FirebaseInfo.CHILD_USERS).child(user.getUid()).child(FirebaseInfo.USER_EMAIL).setValue(email);
                                mRootRef.child(FirebaseInfo.CHILD_USERS).child(user.getUid()).child(FirebaseInfo.USER_FIRST_NAME).setValue(firstname);
                                mRootRef.child(FirebaseInfo.CHILD_USERS).child(user.getUid()).child(FirebaseInfo.USER_LAST_NAME).setValue(lastname);
                                mRootRef.child(FirebaseInfo.CHILD_USERS).child(user.getUid()).child(FirebaseInfo.USER_BIRTHDAY).setValue(birthday);
                                mRootRef.child(FirebaseInfo.CHILD_USERS).child(user.getUid()).child(FirebaseInfo.USER_GENDER).setValue(gender);

                                //Toast.makeText(ActivityLogin.this, birthday, Toast.LENGTH_LONG).show();
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday,first_name,last_name");
                //parameters.putParcelableArray("fields", );
                request.setParameters(parameters);
                request.executeAsync();

                permissionIntent = true;
                progressBar.setVisibility(View.GONE);

                String accessToken = loginResult.getAccessToken().getToken();
                //ResourceUrl = storeObject.optJSONObject("picture").optJSONObject("data").optString("url");
                //new LoadImage().execute(ResourceUrl);

                //他のアクティビティへ飛ぶ
                Intent intent = new Intent(ActivityLogin.this, ActivityProfileSetting.class);
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
        storage = FirebaseStorage.getInstance();
        //mStorageReference = storage.getReferenceFromUrl("gs://project-66993355093102411.appspot.com/");
        mStorageReference = storage.getReference();
        mStorageReferenceImages = mStorageReference.child("Images/profile.jpg");
        //showHorizontalProgressDialog("Uploading", "Please wait...");
        //StorageReference uploadStorageReference = mStorageReference.child(pUri.getLastPathSegment());
        //final UploadTask uploadTask = uploadStorageReference.putFile(pUri);
        final UploadTask uploadTask = mStorageReferenceImages.putFile(pUri);
        Log.d(TAG, "FacebookProfile:pUri: " + pUri);
        Log.d(TAG, "FacebookProfile:Reference: " + mStorageReferenceImages);

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
        Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

        return bitmap;
    }*/

    //ここから先は、Facebookのプロフィール写真をfirebaseに読み込もうとしたときに使った残骸
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    //Bitmap bitmap = getFacebookProfilePicture(userId);

    private void uploadStream(InputStream stream){
        storage = FirebaseStorage.getInstance();
        //mStorageReference = storage.getReferenceFromUrl("gs://project-66993355093102411.appspot.com/");
        mStorageReference = storage.getReference();
        mStorageReferenceImages = mStorageReference.child("Images/profile.jpg");

        UploadTask uploadTask = mStorageReferenceImages.putStream(stream);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }

    private void uploadBitmap(byte[] data){
        storage = FirebaseStorage.getInstance();
        //mStorageReference = storage.getReferenceFromUrl("gs://project-66993355093102411.appspot.com/");
        mStorageReference = storage.getReference();
        mStorageReferenceImages = mStorageReference.child("Images/profile.jpg");

        UploadTask uploadTask = mStorageReferenceImages.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "uploadFialure:");
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "uploadSucess:");
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }

    private InputStream OpenHttpConnection(String urlString) throws IOException {
        InputStream in = null;
        int response = -1;

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");

        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        } catch (Exception ex) {
            throw new IOException("Error connecting");
        }
        return in;
    }

    private Bitmap DownloadImage(String URL) {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return bitmap;
    }

    private class LoadImage extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
                //String ResourceUrl2 = storeObject.getJSONObject("picture").getJSONObject("data").getString("url");
                String sample = params.toString();
                profileUri = Uri.parse(sample);
                Log.d(TAG, "AsyncTask:" + sample);
                uploadFile(profileUri);
            return null;
        }

    }
}
