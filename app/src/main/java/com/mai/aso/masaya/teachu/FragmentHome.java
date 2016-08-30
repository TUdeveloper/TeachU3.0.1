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
    private LinearLayout coming_card, history, profile, schedule, setting, favorite, book, for_meeting_card;
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
        actionBar.show();
        //this.getActivity().setTitle("Profile");
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        getUiInitialization();
        return mView;
    }

    public void getUiInitialization() {
        coming_card = (LinearLayout)mView.findViewById(R.id.frag_home_card_layout);
        profile = (LinearLayout)mView.findViewById(R.id.frag_home_profile_layout);
        history = (LinearLayout)mView.findViewById(R.id.frag_home_history_layout);
        schedule = (LinearLayout)mView.findViewById(R.id.frag_home_schedule_layout);
        setting = (LinearLayout)mView.findViewById(R.id.frag_home_setting_layout);
        favorite = (LinearLayout)mView.findViewById(R.id.frag_home_favorite_layout);
        book = (LinearLayout)mView.findViewById(R.id.frag_home_book_layout);
        for_meeting_card = (LinearLayout)mView.findViewById(R.id.for_meeting_card);

        coming_card.setOnClickListener(this);
        history.setOnClickListener(this);
        profile.setOnClickListener(this);
        schedule.setOnClickListener(this);
        setting.setOnClickListener(this);
        favorite.setOnClickListener(this);
        book.setOnClickListener(this);
        for_meeting_card.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frag_home_card_layout:
                Intent intent_card = new Intent(getActivity(), ActivityUndecided.class);
                startActivity(intent_card);
                break;
            case R.id.frag_home_history_layout:
                Intent intent_history = new Intent(getActivity(), ActivityHistory.class);
                startActivity(intent_history);
                break;
            case R.id.frag_home_profile_layout:
                Intent intent_profile = new Intent(getActivity(), ActivityProfile.class);
                startActivity(intent_profile);
                break;
            case R.id.frag_home_schedule_layout:
                Intent intent_schedule = new Intent(getActivity(), ActivityCalendar.class);
                startActivity(intent_schedule);
                break;
            case R.id.frag_home_setting_layout:
                //Intent intent_setting = new Intent(getActivity(), ActivityProfileSetting.class);
                //startActivity(intent_setting);
                break;
            case R.id.frag_home_favorite_layout:
                Intent intent_favorite = new Intent(getActivity(), ActivityFavorite.class);
                startActivity(intent_favorite);
                break;
            case R.id.frag_home_book_layout:
                Intent intent_book = new Intent(getActivity(), ActivityBooks.class);
                startActivity(intent_book);
                break;
            case R.id.for_meeting_card:
                Intent intent_meeting_card = new Intent(getActivity(), ActivityMeetingCard.class);
                startActivity(intent_meeting_card);
                break;
            default:
                break;
        }
    }

    @Override
    public void onStart(){
        super.onStart();

    }
}