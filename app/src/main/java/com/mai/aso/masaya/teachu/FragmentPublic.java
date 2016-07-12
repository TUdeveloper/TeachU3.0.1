package com.mai.aso.masaya.teachu;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPublic extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public FragmentPublic() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FragmentPublic newInstance(int sectionNumber) {
        FragmentPublic fragmentPublic = new FragmentPublic();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragmentPublic.setArguments(args);
        return fragmentPublic;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //ActionBarTitle
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.titlePublic);
        //View rootView = inflater.inflate(R.layout.fragment_fragment_public, container, false);
        //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        //return rootView;
        return inflater.inflate(R.layout.fragment_fragment_public, container, false);
    }

    //@Override
    //public void onResume(){
    //    super.onResume();
    //    getActivity().getActionBar().setDisplayShowTitleEnabled(true);
    //    getActivity().getActionBar().setTitle("Public");
    //}
}

