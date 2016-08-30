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
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mai.aso.masaya.teachu.R;

import java.util.ArrayList;

/**
 * Created by Ito on 2016/08/09.
 */
public class ActivityCalendar extends AppCompatActivity {

    CalendarView calendar;

    View mFooter;// フッターのプログレスバー（クルクル） View（クラス）の特性を持ったmFooterという言葉を定義

    Handler handler = new Handler();// サブスレッドでの動作の受け渡し

    Integer per_page = 6;//一回当たりのリストビューの数

    public Integer listview_count_now_taka = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendar = (CalendarView) findViewById(R.id.calender);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {
                Toast.makeText(getApplicationContext(),i + "/" + i1 + "/" + i2, Toast.LENGTH_LONG).show();
            }
        });

        final ListView listview = (ListView)findViewById(R.id.listView_books);
        ColorDrawable sage = new ColorDrawable(Color.argb(000, 255, 255, 255));
        listview.setDivider(sage);
        listview.setDividerHeight(5);

        // まずはデータの準備：ArrayList(大きさが可変の配列)をUserクラスを使って定義（Userのインスタンスが配列になってるイメージ）
        // !!!!!!!!!!!!!   iconをすべてmipmapで定義してるので，drawableに変える．結局はFireBaseからダウンロードしてくるんだけど．  !!!!!!!!!!!!!!!!!!
        final ArrayList<User> users = new ArrayList<>(); // UserクラスのArrayListのインスタンスusersを作成
        final int[] icons = {R.mipmap.masaya_pic, R.mipmap.horibe_pic, R.mipmap.tommy_pic, R.mipmap.taichi_pic,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
        final String[] names = getResources().getStringArray(R.array.calendar_name_temp);
        final String[] languages = getResources().getStringArray(R.array.calendar_language_temp);

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
                                        Integer j = listview_count_now_taka % icons.length;
                                        listview_count_now_taka++;
                                        User user = new User();
                                        user.setIcon(BitmapFactory.decodeResource(
                                                getResources(), icons[j]
                                        ));
                                        user.setName(names[j]);
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
        private Bitmap icon;
        private String name;
        private String language;

        public String getName(){ return name;}
        public void setName(String name){this.name = name; }
        public String getLanguage(){return language;}
        public void setLanguage(String language){this.language = language;}
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
                        R.layout.listview_calendar, parent, false
                );
            }
            User user = (User) getItem(position);
            ((ImageView) convertView.findViewById(R.id.user_icon)).setImageBitmap(user.getIcon());
            ((TextView) convertView.findViewById(R.id.user_name)).setText(user.getName());
            ((TextView) convertView.findViewById(R.id.user_language)).setText(user.getLanguage());

            return convertView;
        }
    }

    //  フッターの定義
    private View getFooter() {
        if (mFooter == null) {
            // mFooterに値を代入．mFooterはすでに定義されてるので，newは必要ない
            mFooter = getLayoutInflater().inflate(R.layout.listview_calendar,null);
        }
        return mFooter;
    }
}
