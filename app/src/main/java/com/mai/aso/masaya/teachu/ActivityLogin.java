package com.mai.aso.masaya.teachu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mai.aso.masaya.teachu.info.FirebaseInfo;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.lang.Object;
import java.util.logging.LogManager;

/**
 * Created by MasayaAso on 7/3/16.
 */
public class ActivityLogin extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private TextView info;
    private static final String TAG = ActivityLogin.class.getSimpleName();
<<<<<<< HEAD
    //asdfasdfasdfasdf
=======
>>>>>>> origin/master

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        AppEventsLogger.activateApp(this);

        auth = FirebaseAuth.getInstance();
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
        loginButton.setReadPermissions(Arrays.asList("public_profile","email","user_birthday","user_friends"));
        //info = (TextView) findViewById(R.id.information);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener(){
           @Override
            public void onClick(View v){
               startActivity(new Intent(ActivityLogin.this, ActivitySignup.class));
           }
        });

        btnReset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(ActivityLogin.this, ActivityResetPassword.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(), "Enter email address", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
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
                                if (!task.isSuccessful()){
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(ActivityLogin.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                }else {
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
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(ActivityLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                //info.setText("User ID: " + loginResult.getAccessToken().getUserId());

                //facebook login and add to firebase auth
                handleFacebookAccessToken(loginResult.getAccessToken());

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback(){
                            @Override
                            public void onCompleted(JSONObject jsonObject, GraphResponse response){
                                String email = jsonObject.optString("email");
                                String firstname = jsonObject.optString("first_name");
                                String lastname = jsonObject.optString("last_name");
                                String birthday = jsonObject.optString("birthday");
                                String gender = jsonObject.optString("gender");
                                mRootRef.child(FirebaseInfo.CHILD_USERS).child(auth.getCurrentUser().getUid()).child(FirebaseInfo.USER_EMAIL).setValue(email);
                                mRootRef.child(FirebaseInfo.CHILD_USERS).child(auth.getCurrentUser().getUid()).child(FirebaseInfo.USER_BIRTHDAY).setValue(birthday);
                                //mRootRef.child(FirebaseInfo.CHILD_USERS).child(auth.getCurrentUser().getUid()).child(FirebaseInfo.USER_EDUCATION).setValue(school);
                                mRootRef.child(FirebaseInfo.CHILD_USERS).child(auth.getCurrentUser().getUid()).child(FirebaseInfo.USER_GENDER).setValue(gender);
                                mRootRef.child(FirebaseInfo.CHILD_USERS).child(auth.getCurrentUser().getUid()).child(FirebaseInfo.USER_FIRST_NAME).setValue(firstname);
                                mRootRef.child(FirebaseInfo.CHILD_USERS).child(auth.getCurrentUser().getUid()).child(FirebaseInfo.USER_LAST_NAME).setValue(lastname);
                                //Toast.makeText(ActivityLogin.this, birthday, Toast.LENGTH_LONG).show();
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields","id,name,email,gender,birthday,first_name,last_name");
                request.setParameters(parameters);
                request.executeAsync();


                String accessToken = loginResult.getAccessToken().getToken();

                //mRootRef.child(FirebaseInfo.CHILD_USERS).child(auth.getCurrentUser().getUid()).child(FirebaseInfo.USER_EMAIL).setValue();
                Intent intent = new Intent(ActivityLogin.this, ActivityMainTab.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void handleFacebookAccessToken(AccessToken token) {
        //Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            //Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(ActivityLogin.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

}
