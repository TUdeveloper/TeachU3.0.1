package com.mai.aso.masaya.teachu;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mai.aso.masaya.teachu.info.PublicAdapter;
import com.mai.aso.masaya.teachu.info.User;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPublic extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */


    // ============== For Taka edit from Here ==============
    View mFooter;// フッターのプログレスバー（クルクル） View（クラス）の特性を持ったmFooterという言葉を定義
    Handler handler = new Handler();// サブスレッドでの動作の受け渡し
    Integer per_page = 6;//一回当たりのリストビューの数
    public Integer listview_count_now_taka = 0;
    //  ==============  For Taka edit to Here ==============

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
        actionBar.show();

        View view = inflater.inflate(R.layout.fragment_public, container, false);

        textUid = (TextView)getActivity().findViewById(R.id.text_uid);
        textName = (TextView)getActivity().findViewById(R.id.text_user_name);
        textEmail = (TextView)getActivity().findViewById(R.id.text_email);
        textGender = (TextView)getActivity().findViewById(R.id.text_gender);
        textBirthday = (TextView)getActivity().findViewById(R.id.text_birthday);

        /*auth = FirebaseAuth.getInstance();
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
        });*/
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

        //return inflater.inflate(R.layout.fragment_public, container, false);






        // ============== For Taka edit from Here ==============

        ListView listview = (ListView)view.findViewById(R.id.publicListView_id);
        ColorDrawable sage = new ColorDrawable(Color.argb(000, 255, 255, 255));
        listview.setDivider(sage);
        listview.setDividerHeight(50);

        // まずはデータの準備：ArrayList(大きさが可変の配列)をUserクラスを使って定義（Userのインスタンスが配列になってるイメージ）
        // !!!!!!!!!!!!!   iconをすべてmipmapで定義してるので，drawableに変える．結局はFireBaseからダウンロードしてくるんだけど．  !!!!!!!!!!!!!!!!!!
        final ArrayList<User> users = new ArrayList<>(); // UserクラスのArrayListのインスタンスusersを作成
        final int[] icons = {R.mipmap.masaya_pic, R.mipmap.horibe_pic, R.mipmap.tommy_pic, R.mipmap.taichi_pic,
                R.mipmap.ic_picachu,R.mipmap.ic_asotaro,R.mipmap.ic_spaceman,R.mipmap.ic_icon_rabbit};
        final String[] names = getResources().getStringArray(R.array.publick_name_temp);
        final String[] languages = getResources().getStringArray(R.array.publick_language_temp);
        final String[] comments = getResources().getStringArray(R.array.publick_comment_temp);

        //for (int i=0; i<icons.length; i++) { //forでUserクラスのuserインスタンスを作って，ArayListのusersに追加
        for (int i=0; i<per_page; i++) { //forでUserクラスのuserインスタンスを作って，ArrayListのusersに追加
            Integer j = listview_count_now_taka % icons.length;
            listview_count_now_taka++;
            User user = new User();
            user.setIcon(BitmapFactory.decodeResource(
                    getResources(),icons[j]
            ));
            user.setName(names[j]);
            user.setLanguage(languages[j]);
            user.setComment(comments[j]);
            users.add(user);
        }


        // 上で定義したArrayListのusers引数として,UserAdapterクラス（下で定義）で，adapterを定義
        //final UserAdapter adapter = new UserAdapter(this, 0, users); // これはAtivityで動かすときのみ使用可能．今はFragmentなので，その上位のActivityのcontext情報が必要
        final PublicAdapter adapter = new PublicAdapter(this.getContext(),0,users);


        // listview(インスタンス)にアダプターを設定します
        listview.setAdapter(adapter);

        // リストビューにフッターを追加 getFooterっていう関数を使ってる．この関数は下で定義されてる．
        listview.addFooterView(getFooter());





        // これ以降，overload時のサブスレッド処理
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            // スクロール中の処理
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, final int totalItemCount) {
                // 最初とスクロール完了したとき
                if ((totalItemCount - visibleItemCount) == firstVisibleItem) {

                    // スレッドを変更して処理
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // ここで何かの処理．ネットからデータを取ってくるとか．
                            try {
                                Thread.sleep(1500);
                            } catch (InterruptedException e) {
                            }


                            handler.post(new Runnable() { // ここはメインスレッドへの指令．画面の変更はメインスレッドでのみ可能
                                @Override
                                public void run() {
                                    // UI部品への操作;
                                    for (int i = 0; i < per_page; i++) { //forでUserクラスのuserインスタンスを作って，ArrayListのusersに追加
                                        Integer j = listview_count_now_taka % icons.length;
                                        listview_count_now_taka++;
                                        User user = new User();
                                        user.setIcon(BitmapFactory.decodeResource(
                                                getResources(), icons[j]
                                        ));
                                        user.setName(names[j]);
                                        user.setLanguage(languages[j]);
                                        user.setComment(comments[j]);
                                        //users.add(user);
                                        adapter.add(user);
                                    }

                                }
                            });
                        }
                    }).start();

                }
            }

            // ListViewがスクロール中かどうか状態を返すメソッドです
            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
            }
        });
        // ============== For Taka edit to Here ==============




        //return inflater.inflate(R.layout.fragment_public, container, false);
        return view;



    }

    public void changeText(){
        textName.setText("test");
    }


    @Override
    public void onStart(){
        super.onStart();
        ImageButton search_teacher_btn = (ImageButton)getView().findViewById(R.id.search_teacher_botton);
        search_teacher_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ActivitySearchTeacher.class);
                startActivity(i);
            }
        });
    }

    //refUser.CompletionLi
    //addValueEventListener(new ValueEventListener()
    //@Override
    //public void onResume(){
    //    super.onResume();
    //    getActivity().getActionBar().setDisplayShowTitleEnabled(true);
    //    getActivity().getActionBar().setTitle("Public");
    //}



    // ============== For Taka edit from Here ==============
    // Adapterの情報をセットするためのクラス
    /*public class User {
        private Bitmap icon;
        private String name;
        private String language;
        private String comment;

        public String getName(){ return name;}
        public void setName(String name){this.name = name; }
        public String getLanguage(){return language;}
        public void setLanguage(String language){this.language = language;}
        public String getComment(){ return comment;}
        public void setComment(String comment){this.comment = comment; }
        public Bitmap getIcon(){ return icon; }
        public void setIcon(Bitmap icon){ this.icon = icon;}
    }


    // Userクラスを使ってAdapterをセットするためのクラス（よくわからん．こういうもんらしい）
    public class UserAdapter extends ArrayAdapter<User> {
        private LayoutInflater layoutInflater;
        public UserAdapter(Context c, int id, ArrayList<User> users) {
            super(c, id, users);
            this.layoutInflater = (LayoutInflater) c.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE
            );
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            if (convertView == null){
                convertView = layoutInflater.inflate(
                        R.layout.listview_main, parent, false
                );
            }
            User user = (User) getItem(position);
            ((ImageView) convertView.findViewById(R.id.user_icon)).setImageBitmap(user.getIcon());
            ((TextView) convertView.findViewById(R.id.user_name)).setText(user.getName());
            ((TextView) convertView.findViewById(R.id.user_language)).setText(user.getLanguage());
            ((TextView) convertView.findViewById(R.id.user_comment)).setText(user.getComment());

            return convertView;
        }
    }*/

    //  フッターの定義
    private View getFooter() {
        if (mFooter == null) {
            // mFooterに値を代入．mFooterはすでに定義されてるので，newは必要ない
            mFooter = getActivity().getLayoutInflater().inflate(R.layout.listview_footer,null);
        }
        return mFooter;
    }
    // ============== For Taka edit to Here ==============

}

