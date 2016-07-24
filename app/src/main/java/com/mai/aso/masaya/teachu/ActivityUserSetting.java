package com.mai.aso.masaya.teachu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mai.aso.masaya.teachu.info.FirebaseInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.lang.Object;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import java.util.logging.LogManager;

/**
 * Created by MasayaAso on 7/3/16.
 */
public class ActivityUserSetting extends AppCompatActivity {

    private EditText inputFirstName, inputLastName;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private ProgressBar progressBar;
    private Button btnUserSetting;
    private LoginButton loginButton;
    private Spinner spinner_gender, spinner_year, spinner_month, spinner_day;
    private String[] gender_list;
    private String setGender,setBirthday, setYear, setMonth, setDay;
    //private static final String TAG = ActivityLogin.class.getSimpleName();

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the view
        setContentView(R.layout.activity_user_setting);

        inputFirstName = (EditText) findViewById(R.id.first_name);
        inputLastName = (EditText) findViewById(R.id.last_name);
        progressBar = (ProgressBar) findViewById(R.id.progress_user_setting);
        btnUserSetting = (Button) findViewById(R.id.btn_user_setting);
        spinner_gender = (Spinner)findViewById(R.id.spinner_gender);
        spinner_year = (Spinner)findViewById(R.id.spinner_year);
        spinner_month = (Spinner)findViewById(R.id.spinner_month);
        spinner_day = (Spinner)findViewById(R.id.spinner_day);
        gender_list = getResources().getStringArray(R.array.spinner_gender_list);


        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, gender_list);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gender.setAdapter(genderAdapter);

        ArrayList<String> yearList = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 1900; i <= thisYear; i++){
            yearList.add(Integer.toString(i));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, yearList);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_year.setAdapter(yearAdapter);

        ArrayList<String> monthList = new ArrayList<String>();
        for (int i = 1; i <= 12; i++){
            monthList.add(Integer.toString(i));
        }
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, monthList);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_month.setAdapter(monthAdapter);

        ArrayList<String> dayList = new ArrayList<String>();
        for (int i = 1; i <= 31; i++){
            dayList.add(Integer.toString(i));
        }
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, dayList);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_day.setAdapter(dayAdapter);


        spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setGender = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setYear = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setMonth = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setDay = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

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

                setBirthday = (setMonth + "/" + setDay + "/" + setYear);

                progressBar.setVisibility(View.VISIBLE);
                mRootRef.child(FirebaseInfo.CHILD_USERS).child(user.getUid()).child(FirebaseInfo.USER_FIRST_NAME).setValue(first_name);
                mRootRef.child(FirebaseInfo.CHILD_USERS).child(user.getUid()).child(FirebaseInfo.USER_LAST_NAME).setValue(last_name);
                mRootRef.child(FirebaseInfo.CHILD_USERS).child(user.getUid()).child(FirebaseInfo.USER_GENDER).setValue(setGender);
                mRootRef.child(FirebaseInfo.CHILD_USERS).child(user.getUid()).child(FirebaseInfo.USER_BIRTHDAY).setValue(setBirthday);
                progressBar.setVisibility(View.GONE);

                Intent intent = new Intent(ActivityUserSetting.this, ActivityLanguageSetting.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}
