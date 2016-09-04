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
    private TextView txt_language;
    //private MenuItem menuItemSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_card);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_meeting_card);
        //toolbar.getBackground().setAlpha(100);
        //toolbar.setLogo(R.drawable.ic_back_btn);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        btn_language = (ImageView)findViewById(R.id.language_select);
        btn_location = (ImageView)findViewById(R.id.location_select);
        btn_calendar = (ImageView)findViewById(R.id.calendar_select);
        txt_language = (TextView)findViewById(R.id.txt_language);

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

        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ポップアップの表示メソッド
                Intent intent_location = new Intent(ActivityMeetingCard.this, ActivityMap.class);
                startActivity(intent_location);
            }
        });

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

    //阿蘓
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