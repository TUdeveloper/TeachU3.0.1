package com.mai.aso.masaya.teachu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import java.util.Collections;
import java.util.Locale;
import java.util.logging.LogManager;

/**
 * Created by MasayaAso on 7/3/16.
 */
public class ActivityLanguageSetting extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
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
        setContentView(R.layout.activity_language_setting);

        progressBar = (ProgressBar) findViewById(R.id.progress_language_setting);
        btnLanguageSetting = (Button) findViewById(R.id.btn_language_setting);
        spinner_country = (Spinner)findViewById(R.id.spinner_country);
        spinner_native_language = (Spinner)findViewById(R.id.spinner_native_language);
        spinner_learn_language = (Spinner)findViewById(R.id.spinner_learn_language);
        spinner_learn_language2 = (Spinner)findViewById(R.id.spinner_learn_language2);

        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        String country;
        for( Locale loc : locale ){
            country = loc.getDisplayCountry();
            if( country.length() > 0 && !countries.contains(country) ){
                countries.add( country );
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);

        //Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> languages = new ArrayList<String>();
        String language;
        for( Locale loc : locale ){
            language = loc.getDisplayLanguage();
            if( language.length() > 0 && !languages.contains(language) ){
                languages.add( language );
            }
        }
        Collections.sort(languages, String.CASE_INSENSITIVE_ORDER);

        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, countries);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_country.setAdapter(countryAdapter);

        ArrayAdapter<String> nativeLanguageAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, languages);
        nativeLanguageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_native_language.setAdapter(nativeLanguageAdapter);

        ArrayAdapter<String> learnLanguageAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, languages);
        learnLanguageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_learn_language.setAdapter(learnLanguageAdapter);

        ArrayAdapter<String> learnLanguage2Adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, languages);
        learnLanguage2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_learn_language2.setAdapter(learnLanguage2Adapter);

        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setCountry = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_native_language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setNativeLanguage = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_learn_language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setLearnLanguage = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_learn_language2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setLearnLanguage2 = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        btnLanguageSetting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                progressBar.setVisibility(View.VISIBLE);
                mRootRef.child(FirebaseInfo.CHILD_USERS).child(user.getUid()).child(FirebaseInfo.USER_COUNTRY).setValue(setCountry);
                mRootRef.child(FirebaseInfo.CHILD_USERS).child(user.getUid()).child(FirebaseInfo.USER_NATIVE_LANGUAGE).setValue(setNativeLanguage);
                mRootRef.child(FirebaseInfo.CHILD_USERS).child(user.getUid()).child(FirebaseInfo.USER_LEARN_LANGUAGE).setValue(setLearnLanguage);
                mRootRef.child(FirebaseInfo.CHILD_USERS).child(user.getUid()).child(FirebaseInfo.USER_LEARN_LANGUAGE2).setValue(setLearnLanguage2);
                progressBar.setVisibility(View.GONE);

                Intent intent = new Intent(ActivityLanguageSetting.this, ActivityMainTab.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

}
