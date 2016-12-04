package com.mai.aso.masaya.teachu;

import android.app.Activity;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.mai.aso.masaya.teachu.info.FbInfo;

import java.security.Timestamp;
import java.util.ArrayList;

/**
 * Created by MasayaAso on 6/19/16.
 */
public class ActivityMeetingCard extends AppCompatActivity {

    //private Toolbar toolbar;
    private static final String TAG = ActivityMeetingCard.class.getSimpleName();
    final CharSequence[] ItemLanguage = {"English", "Japanese", "Chinese"};
    private String selectedLanguage;
    private ImageView btn_language, btn_location, btn_calendar;
    private TextView txt_language, txt_location, txt_student_name, txt_teacher_name;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseStorage storage;
    private DatabaseReference mNotDone,mGeneral, mMyMeetingCard;
    private String mKey, last_name, first_name, name;
    private String locationName, locationAddress;
    private Double locationLat, locationLng;
    //private MenuItem menuItemSend;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_card);

        //Firebase初期設定
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        mMyMeetingCard = mRootRef.child(FbInfo.USERS).child(user.getUid()).child(FbInfo.USERS_MEETING_CARD);
        mGeneral = mRootRef.child(FbInfo.USERS).child(user.getUid()).child(FbInfo.USERS_GENERAL);
        mNotDone = mRootRef.child(FbInfo.MEETING_CARD).child(FbInfo.MEETING_CARD_NOT_DONE);


        //Toolbar初期コネクト
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_meeting_card);
        //toolbar.getBackground().setAlpha(100);
        //toolbar.setLogo(R.drawable.ic_back_btn);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Layoutとの初期コネクト
        btn_language = (ImageView)findViewById(R.id.language_select);
        btn_location = (ImageView)findViewById(R.id.location_select);
        btn_calendar = (ImageView)findViewById(R.id.calendar_select);
        txt_language = (TextView)findViewById(R.id.act_mc_txt_language);
        txt_location = (TextView)findViewById(R.id.act_mc_txt_location);
        txt_student_name = (TextView)findViewById(R.id.act_mc_txt_student);
        txt_teacher_name = (TextView)findViewById(R.id.act_mc_txt_teacher);

        //FirebaseからGeneral個人情報取得
        mGeneral.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                first_name = dataSnapshot.child(FbInfo.GEN_FIRST_NAME).getValue().toString();
                last_name = dataSnapshot.child(FbInfo.GEN_LAST_NAME).getValue().toString();
                name = first_name + " " + last_name;
                Log.d(TAG, "Name:" + last_name);
                txt_student_name.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        btn_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ポップアップの表示メソッド
                AlertDialog.Builder languagePopupBuilder = new AlertDialog.Builder(ActivityMeetingCard.this);
                languagePopupBuilder.setTitle("Select Language");
                languagePopupBuilder.setSingleChoiceItems(ItemLanguage, -1, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int selected) {
                        //OKボタンが押された時の処理
                        selectedLanguage = ItemLanguage[selected].toString();
                        //Toast.makeText(ActivityMeetingCard.this, String.format("%s Selected", ItemLanguage[selected]), Toast.LENGTH_SHORT).show();
                    }});
                languagePopupBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        //送信ボタンが押された時の処理
                        txt_language.setText(selectedLanguage);
                        Toast.makeText(ActivityMeetingCard.this, String.format("OK %s Selected",selectedLanguage), Toast.LENGTH_SHORT).show();
                    }});
                languagePopupBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int selected) {
                        //キャンセルが押された時の処理
                        Toast.makeText(ActivityMeetingCard.this, "キャンセル", Toast.LENGTH_SHORT).show();
                    }});
                languagePopupBuilder.setCancelable(false);
                languagePopupBuilder.show();

            }
        });

        //Activity Mapへ
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ポップアップの表示メソッド
                Intent intent_location = new Intent(getApplication(), ActivityMap.class);
                startActivityForResult(intent_location, 1);
            }
        });

        //Activity calendarへ
        btn_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ポップアップの表示メソッド
                Intent intent_calendar = new Intent(ActivityMeetingCard.this, ActivitySelectCalendar.class);
                startActivity(intent_calendar);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //ミーティングカード用のメニュータブをセレクト
        getMenuInflater().inflate(R.menu.menu_meeting_card, menu);

        return true;
    }


    //阿蘓
    //ツールバーの右側のアイコンをタップした時の機能
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.check_meeting_card:
                Toast toast = Toast.makeText(getApplicationContext(), "Send Icon is selected", Toast.LENGTH_SHORT);
                toast.show();

                //ポップアップの表示メソッド
                AlertDialog.Builder sendPopupBuilder = new AlertDialog.Builder(ActivityMeetingCard.this);
                sendPopupBuilder.setTitle("Send your meeting card ?");
                sendPopupBuilder.setPositiveButton("Send", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        //送信ボタンが押された時の処理
                        //Log.d(TAG, "Data:" + mGeneral.getDatabase());

                        //Firebaseに入力する際はできるだけカード上の文章から入れる
                        //途中受け渡しをしてるデータ等はできるだけ使わない
                        mKey = mNotDone.push().getKey();
                        mNotDone.child(mKey).child(FbInfo.NOT_DONE_STUDENT).child(FbInfo.USERS).setValue(user.getUid());
                        mNotDone.child(mKey).child(FbInfo.NOT_DONE_STUDENT).child(FbInfo.GEN_LAST_NAME).setValue(last_name);
                        mNotDone.child(mKey).child(FbInfo.NOT_DONE_STUDENT).child(FbInfo.GEN_FIRST_NAME).setValue(first_name);
                        mNotDone.child(mKey).child(FbInfo.NOT_DONE_TEACHER);
                        mNotDone.child(mKey).child(FbInfo.NOT_DONE_WHAT).setValue(txt_language);
                        if (txt_location.getText().toString().equals("Location")) {
                            mNotDone.child(mKey).child(FbInfo.NOT_DONE_WHERE_NAME).setValue(FbInfo.NULL_NULL);
                        }else {
                            mNotDone.child(mKey).child(FbInfo.NOT_DONE_WHERE_NAME).setValue(txt_location.getText());
                            Log.d(TAG, "locationName:" + txt_location.getText());
                        }
                        mNotDone.child(mKey).child(FbInfo.NOT_DONE_WHERE_ADDRESS).setValue(locationAddress);
                        mNotDone.child(mKey).child(FbInfo.NOT_DONE_WHERE_LAT).setValue(locationLat);
                        mNotDone.child(mKey).child(FbInfo.NOT_DONE_WHERE_LNG).setValue(locationLng);
                        mNotDone.child(mKey).child(FbInfo.NOT_DONE_FEE).child(FbInfo.FEE_FEE).setValue("Example");
                        mNotDone.child(mKey).child(FbInfo.NOT_DONE_FEE).child(FbInfo.FEE_CURRENCY).setValue("Example");
                        mNotDone.child(mKey).child(FbInfo.NOT_DONE_ACCEPT_STUDENT).setValue(true);
                        mNotDone.child(mKey).child(FbInfo.NOT_DONE_ACCEPT_TEACHER).setValue(false);
                        mNotDone.child(mKey).child(FbInfo.NOT_DONE_STATUS_DONE).setValue(false);
                        mNotDone.child(mKey).child(FbInfo.NOT_DONE_STATUS_PAY).setValue(false);

                        Long tsLong = System.currentTimeMillis()/1000;
                        String ts = tsLong.toString();
                        mMyMeetingCard.child(mKey).child(FbInfo.TIME_STAMP).setValue(ts);


                        Toast.makeText(ActivityMeetingCard.this, "送信", Toast.LENGTH_SHORT).show();
                    }});
                sendPopupBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int selected) {
                        //キャンセルが押された時の処理
                        Toast.makeText(ActivityMeetingCard.this, "キャンセル", Toast.LENGTH_SHORT).show();
                    }});
                sendPopupBuilder.setCancelable(false);
                sendPopupBuilder.show();

                return true;
            //戻るボタンで一つまえのアクティビティに戻る
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //阿蘓　Activity Mapからの返り値をここでゲットだぜ
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        Log.i(TAG, "Check: ");

        if(resultCode == 1){
            locationName = intent.getStringExtra("locationName");
            locationAddress = intent.getStringExtra("locationAddress");
            locationLat = intent.getDoubleExtra("locationLat",0);
            locationLng = intent.getDoubleExtra("locationLng",0);
            txt_location.setText(locationName);

        }
    }


    //阿蘓　不必要！！！
    //ツールバーの中にある戻るボタンをボタン化する為のメソッド
    //setOnClickListenerでボタンを押した時の操作を実装できる
    public static View getToolbarLogoIcon(Toolbar toolbar){
        //check if contentDescription previously was set
        boolean hadContentDescription = android.text.TextUtils.isEmpty(toolbar.getLogoDescription());
        String contentDescription = String.valueOf(!hadContentDescription ? toolbar.getLogoDescription() : "logoContentDescription");
        toolbar.setLogoDescription(contentDescription);
        ArrayList<View> potentialViews = new ArrayList<View>();
        //find the view based on it's content description, set programatically or with android:contentDescription
        toolbar.findViewsWithText(potentialViews,contentDescription, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        //Nav icon is always instantiated at this point because calling setLogoDescription ensures its existence
        View logoIcon = null;
        if(potentialViews.size() > 0){
            logoIcon = potentialViews.get(0);
        }
        //Clear content description if not previously present
        if(hadContentDescription)
            toolbar.setLogoDescription(null);
        return logoIcon;
    }
}