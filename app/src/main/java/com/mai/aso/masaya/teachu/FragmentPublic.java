package com.mai.aso.masaya.teachu;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mai.aso.masaya.teachu.info.FirebaseInfo;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPublic extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference refUser;
    private String Name;
    private TextView textUid, textName, textEmail, textGender, textBirthday;
    private static final String TAG = FragmentPublic.class.getSimpleName();

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
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

        View view = inflater.inflate(R.layout.fragment_public, container, false);

        textUid = (TextView)getActivity().findViewById(R.id.text_uid);
        textName = (TextView)getActivity().findViewById(R.id.text_user_name);
        textEmail = (TextView)getActivity().findViewById(R.id.text_email);
        textGender = (TextView)getActivity().findViewById(R.id.text_gender);
        textBirthday = (TextView)getActivity().findViewById(R.id.text_birthday);

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        refUser = mRootRef.child(FirebaseInfo.CHILD_USERS).child(user.getUid());
        refUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String Uid = (String)dataSnapshot.child(user.getUid()).getValue();
                String Name = (String)dataSnapshot.child(FirebaseInfo.USER_FIRST_NAME).getValue();
                String Email = (String)dataSnapshot.child(FirebaseInfo.USER_EMAIL).getValue();
                String Gender = (String)dataSnapshot.child(FirebaseInfo.USER_GENDER).getValue();
                String Birthday = (String)dataSnapshot.child(FirebaseInfo.USER_BIRTHDAY).getValue();
                Log.d(TAG, "UserName: " + Name);

                //textUid.setText(Uid);
                //textName.setText(Name);
                //textEmail.setText(Email);
                //textGender.setText(Gender);
                //textBirthday.setText(Birthday);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //String Uid = user.getUid();
        //Name = mRootRef.child(FirebaseInfo.CHILD_USERS).child(user.getUid()).child(FirebaseInfo.USER_FIRST_NAME).toString();
        //Log.d(TAG, "UserID: " + Uid);
        //Log.d(TAG, "UserName: " + Name);

        //((TextView)textUid).setText(Uid);
        //((TextView)textName).setText(Name);
        //View rootView = inflater.inflate(R.layout.fragment_fragment_public, container, false);
        //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        //return rootView;
        return inflater.inflate(R.layout.fragment_public, container, false);
    }

    public void changeText(){
        textName.setText("test");
    }


    //refUser.CompletionLi
    //addValueEventListener(new ValueEventListener()
    //@Override
    //public void onResume(){
    //    super.onResume();
    //    getActivity().getActionBar().setDisplayShowTitleEnabled(true);
    //    getActivity().getActionBar().setTitle("Public");
    //}
}

