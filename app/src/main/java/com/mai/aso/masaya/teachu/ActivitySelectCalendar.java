package com.mai.aso.masaya.teachu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by Ito on 2016/08/21.
 */
public class ActivitySelectCalendar extends AppCompatActivity {
    private Spinner nSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_calendar);

        final String[] spinnerItems = getResources().getStringArray(R.array.calendar_time_temp);

        nSpinner = (Spinner)findViewById(R.id.spinner);
        // ArrayAdapter
        ArrayAdapter<String> adapter
                = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // spinner に adapter をセット
        nSpinner.setAdapter(adapter);

    }

}