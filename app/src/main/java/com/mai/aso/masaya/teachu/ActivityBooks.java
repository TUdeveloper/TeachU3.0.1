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

import java.util.ArrayList;

/**
 * Created by Ito on 2016/08/03.
 */
public class ActivityBooks extends AppCompatActivity {

    View mFooter;// フッターのプログレスバー（クルクル） View（クラス）の特性を持ったmFooterという言葉を定義

    Handler handler = new Handler();// サブスレッドでの動作の受け渡し

    Integer per_page = 6;//一回当たりのリストビューの数

    public Integer listview_count_now_taka = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        final ListView listview = (ListView) findViewById(R.id.listView_books);
        ColorDrawable sage = new ColorDrawable(Color.argb(000, 255, 255, 255));
        listview.setDivider(sage);
        listview.setDividerHeight(50);

        // まずはデータの準備：ArrayList(大きさが可変の配列)をUserクラスを使って定義（Userのインスタンスが配列になってるイメージ）
        // !!!!!!!!!!!!!   iconをすべてmipmapで定義してるので，drawableに変える．結局はFireBaseからダウンロードしてくるんだけど．  !!!!!!!!!!!!!!!!!!
        final ArrayList<User> users = new ArrayList<>(); // UserクラスのArrayListのインスタンスusersを作成
        final int[] icons_book = {R.mipmap.book1, R.mipmap.book2, R.mipmap.book3};


        //for (int i=0; i<icons.length; i++) { //forでUserクラスのuserインスタンスを作って，ArayListのusersに追加
        for (int i = 0; i < per_page; i++) { //forでUserクラスのuserインスタンスを作って，ArrayListのusersに追加
            Integer j = listview_count_now_taka % icons_book.length;
            listview_count_now_taka++;
            User user = new User();
            user.setIcon_book(BitmapFactory.decodeResource(
                    getResources(), icons_book[j]
            ));
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
                                        Integer j = listview_count_now_taka % icons_book.length;
                                        listview_count_now_taka++;
                                        User user = new User();
                                        user.setIcon_book(BitmapFactory.decodeResource(
                                                getResources(), icons_book[j]
                                        ));
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
        private Bitmap icon_book;

        public Bitmap getIcon_book() {
            return icon_book;
        }

        public void setIcon_book(Bitmap icon_book) {
            this.icon_book = icon_book;
        }

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
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(
                        R.layout.listview_books, parent, false
                );
            }
            User user = (User) getItem(position);
            ((ImageView) convertView.findViewById(R.id.book1)).setImageBitmap(user.getIcon_book());

            return convertView;
        }
    }


    //  フッターの定義
    private View getFooter() {
        if (mFooter == null) {
            // mFooterに値を代入．mFooterはすでに定義されてるので，newは必要ない
            mFooter = getLayoutInflater().inflate(R.layout.footer_books, null);
        }
        return mFooter;
    }
}