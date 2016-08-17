package com.mai.aso.masaya.teachu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by takamasa on 2016/08/07.
 */
public class ActivitySearchTeacher extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_teacher);
    }

    @Override
    public void onStart() {
        super.onStart();
        Button search_result_btn = (Button) findViewById(R.id.search_result);
        search_result_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivitySearchTeacher.this, ActivityMainTab.class);
                startActivity(i);
            }
        });
    }
}
