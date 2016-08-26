package com.mai.aso.masaya.teachu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

/**
 * Created by MasayaAso on 8/2/16.
 */
public class FragmentHome extends Fragment implements View.OnClickListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private View mView;
    private LinearLayout coming_card;
    private static final String ARG_SECTION_NUMBER = "section_number";

    public FragmentHome() {
        // Required empty public constructor
    }

    public static FragmentHome newInstance(int sectionNumber) {
        FragmentHome fragmentHome = new FragmentHome();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragmentHome.setArguments(args);
        return fragmentHome;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //ActionBarTitle
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.titleHome);
        //this.getActivity().setTitle("Profile");
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        getUiInitialization();
        return mView;
    }

    public void getUiInitialization() {
        coming_card = (LinearLayout)mView.findViewById(R.id.coming_card);
        coming_card.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.coming_card:
                Intent intent = new Intent(getActivity(), SubMake.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        ImageButton home_favorite_btn = (ImageButton)getView().findViewById(R.id.homeFavoriteButton_id);
        home_favorite_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ActivityFavorite.class);
                startActivity(i);
            }
        });
    }
}