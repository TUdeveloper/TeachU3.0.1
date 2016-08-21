package com.mai.aso.masaya.teachu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ito on 2016/08/03.
 */
public class ActivityUndecided extends AppCompatActivity {

    View mFooter;// フッターのプログレスバー（クルクル） View（クラス）の特性を持ったmFooterという言葉を定義

    Handler handler = new Handler();// サブスレッドでの動作の受け渡し

    Integer per_page = 6;//一回当たりのリストビューの数

    public Integer listview_count_now_taka = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_undecided);

        final ListView listview = (ListView) findViewById(R.id.listView_undecided);
        ColorDrawable sage = new ColorDrawable(Color.argb(000, 255, 255, 255));
        listview.setDivider(sage);
        listview.setDividerHeight(50);

        // まずはデータの準備：ArrayList(大きさが可変の配列)をUserクラスを使って定義（Userのインスタンスが配列になってるイメージ）
        // !!!!!!!!!!!!!   iconをすべてmipmapで定義してるので，drawableに変える．結局はFireBaseからダウンロードしてくるんだけど．  !!!!!!!!!!!!!!!!!!
        final ArrayList<User> users = new ArrayList<>(); // UserクラスのArrayListのインスタンスusersを作成
        final int[] icons_student = {R.mipmap.masaya_pic, R.mipmap.horibe_pic, R.mipmap.tommy_pic, R.mipmap.taichi_pic,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
        final int[] icons_teacher = {R.mipmap.horibe_pic, R.mipmap.tommy_pic, R.mipmap.taichi_pic,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.masaya_pic};
        final String[] names_student = getResources().getStringArray(R.array.student_name_temp);
        final String[] names_teacher = getResources().getStringArray(R.array.teacher_name_temp);
        final String[] moneys = getResources().getStringArray(R.array.money_temp);
        final String[] times = getResources().getStringArray(R.array.Time_temp);
        final String[] locations = getResources().getStringArray(R.array.location_temp);
        final String[] languages = getResources().getStringArray(R.array.language_temp);

        //for (int i=0; i<icons.length; i++) { //forでUserクラスのuserインスタンスを作って，ArayListのusersに追加
        for (int i = 0; i < per_page; i++) { //forでUserクラスのuserインスタンスを作って，ArrayListのusersに追加
            Integer j = listview_count_now_taka % icons_student.length;
            listview_count_now_taka++;
            User user = new User();
            user.setIcon_student(BitmapFactory.decodeResource(
                    getResources(), icons_student[j]
            ));
            user.setIcon_teacher(BitmapFactory.decodeResource(
                    getResources(), icons_teacher[j]
            ));
            user.setName_student(names_student[j]);
            user.setName_teacher(names_teacher[j]);
            user.setMoney(moneys[j]);
            user.setTime(times[j]);
            user.setLocation(locations[j]);
            user.setLanguage(languages[j]);
            users.add(user);
        }
        // 上で定義したArrayListのusers引数として,UserAdapterクラス（下で定義）で，adapterを定義
        final UserAdapter adapter = new UserAdapter(this, 0, users);


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
                                        Integer j = listview_count_now_taka % icons_student.length;
                                        listview_count_now_taka++;
                                        User user = new User();
                                        user.setIcon_student(BitmapFactory.decodeResource(
                                                getResources(), icons_student[j]
                                        ));
                                        user.setIcon_teacher(BitmapFactory.decodeResource(
                                                getResources(), icons_teacher[j]
                                        ));
                                        user.setName_student(names_student[j]);
                                        user.setName_teacher(names_teacher[j]);
                                        user.setMoney(moneys[j]);
                                        user.setTime(times[j]);
                                        user.setLocation(locations[j]);
                                        user.setLanguage(languages[j]);
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

    }

    // Adapterの情報をセットするためのクラス
    public class User {
        private Bitmap icon_student;
        private Bitmap icon_teacher;
        private String name_student;
        private String name_teacher;
        private String money;
        private String time;
        private String location;
        private String language;

        public Bitmap getIcon_student(){ return icon_student; }
        public void setIcon_student(Bitmap icon_student){ this.icon_student = icon_student;}
        public Bitmap getIcon_teacher(){ return icon_teacher; }
        public void setIcon_teacher(Bitmap icon_teacher){ this.icon_teacher = icon_teacher;}
        public String getName_student(){ return name_student;}
        public void setName_student(String name){this.name_student = name; }
        public String getName_teacher(){ return name_teacher;}
        public void setName_teacher(String name){this.name_teacher = name; }
        public String getMoney(){ return money;}
        public void setMoney(String money){this.money = money; }
        public String getTime(){ return time;}
        public void setTime(String time){this.time = time; }
        public String getLocation(){ return location;}
        public void setLocation(String location){this.location = location; }
        public String getLanguage(){return language;}
        public void setLanguage(String language){this.language = language;}
    }

    // Userクラスを使ってAdapterをセットするためのクラス（よくわからん．こういうもんらしいby堀部くん）
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
                        R.layout.listview_undecided, parent, false
                );
            }
            User user = (User) getItem(position);
            ((ImageView) convertView.findViewById(R.id.imageView2)).setImageBitmap(user.getIcon_student());
            ((ImageView) convertView.findViewById(R.id.imageView3)).setImageBitmap(user.getIcon_teacher());
            ((TextView) convertView.findViewById(R.id.textView2)).setText(user.getName_student());
            ((TextView) convertView.findViewById(R.id.textView3)).setText(user.getName_teacher());
            ((TextView) convertView.findViewById(R.id.textView16)).setText(user.getMoney());
            ((TextView) convertView.findViewById(R.id.textView17)).setText(user.getTime());
            ((TextView) convertView.findViewById(R.id.textView18)).setText(user.getLocation());
            ((TextView) convertView.findViewById(R.id.textView19)).setText(user.getLanguage());

            return convertView;
        }
    }


    //  フッターの定義
    private View getFooter() {
        if (mFooter == null) {
            // mFooterに値を代入．mFooterはすでに定義されてるので，newは必要ない
            mFooter = getLayoutInflater().inflate(R.layout.footer_undecided,null);
        }
        return mFooter;
    }
}
