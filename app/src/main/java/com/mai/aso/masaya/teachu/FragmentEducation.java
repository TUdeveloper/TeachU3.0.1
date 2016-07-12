package com.mai.aso.masaya.teachu;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.support.v7.widget.Toolbar;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentEducation extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    //private Toolbar mToolBarBottom;
    public FragmentEducation() {
        // Required empty public constructor
    }

    public static FragmentEducation newInstance(int sectionNumber){
        FragmentEducation fragmentEducation = new FragmentEducation();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragmentEducation.setArguments(args);
        return fragmentEducation;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //ActionBarTitle
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.titleEducation);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_education, container, false);
    }


    @Override
    public void onStart(){
        super.onStart();
        ImageButton make_btn = (ImageButton)getView().findViewById(R.id.education_make_btn);
        ImageButton calendar_btn = (ImageButton)getView().findViewById(R.id.education_calendar_btn);
        ImageButton book_btn = (ImageButton)getView().findViewById(R.id.education_book_btn);
        make_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SubMake.class);
                startActivity(i);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SubCalendar.class);
                startActivity(i);
            }
        });
        book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SubBook.class);
                startActivity(i);
            }
        });
    }

}
