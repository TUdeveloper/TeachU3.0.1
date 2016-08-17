package com.mai.aso.masaya.teachu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by MasayaAso on 6/19/16.
 */
public class StartUp extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        ImageView imagaAsButtom = (ImageView) findViewById(R.id.startup_rabbit);
        imagaAsButtom.setOnClickListener(handler);
    }

    //ここは、ラビットのアイコンを押したらメインのタブ画面に移動する
    //最終的には削除　＞＞　FireBaseからデータを読み込んで終わったら、メインタブ画面に移動するようにしたい
    //（もし、アカウントがなかったらアカウント作成画面へ）
    View.OnClickListener handler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(StartUp.this, ActivityMainTab.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    };


}

