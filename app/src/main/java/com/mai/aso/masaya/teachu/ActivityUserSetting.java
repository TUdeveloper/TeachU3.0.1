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

import java.util.Arrays;
import java.lang.Object;
import java.util.logging.LogManager;

/**
 * Created by MasayaAso on 7/3/16.
 */
public class ActivityUserSetting extends AppCompatActivity {

    private EditText inputFirstName, inputLastName;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnUserSetting;
    private LoginButton loginButton;
    //private static final String TAG = ActivityLogin.class.getSimpleName();

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //auth = FirebaseAuth.getInstance();
        //get firebase auth instance
        //if (auth.getCurrentUser() != null) {
        //    startActivity(new Intent(ActivityLogin.this, ActivityMainTab.class));
        //    finish();
        //}

        //set the view
        setContentView(R.layout.activity_user_setting);

        inputFirstName = (EditText) findViewById(R.id.first_name);
        inputLastName = (EditText) findViewById(R.id.last_name);
        progressBar = (ProgressBar) findViewById(R.id.progress_user_setting);
        btnUserSetting = (Button) findViewById(R.id.btn_user_setting);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnUserSetting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final String first_name = inputFirstName.getText().toString();
                final String last_name = inputLastName.getText().toString();

                if (TextUtils.isEmpty(first_name)){
                    Toast.makeText(getApplicationContext(), "Enter your first name", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(last_name)){
                    Toast.makeText(getApplicationContext(), "Enter you last name", Toast.LENGTH_LONG).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                mRootRef.child(FirebaseInfo.CHILD_USERS).child(auth.getCurrentUser().getUid()).child(FirebaseInfo.USER_FIRST_NAME).setValue(first_name);
                mRootRef.child(FirebaseInfo.CHILD_USERS).child(auth.getCurrentUser().getUid()).child(FirebaseInfo.USER_LAST_NAME).setValue(last_name);
                progressBar.setVisibility(View.GONE);

                Intent intent = new Intent(ActivityUserSetting.this, ActivityMainTab.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

}
