package com.mai.aso.masaya.teachu;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentFriend extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public FragmentFriend() {
        // Required empty public constructor
    }

    public static FragmentFriend newInstance(int sectionNumber){
        FragmentFriend fragmentFriend = new FragmentFriend();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragmentFriend.setArguments(args);
        return fragmentFriend;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //ActionBarTitle
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.titleChat);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_friend, container, false);
    }

}
