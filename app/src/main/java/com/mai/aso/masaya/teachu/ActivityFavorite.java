package com.mai.aso.masaya.teachu;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.mai.aso.masaya.teachu.info.FavoriteAdapter;
import com.mai.aso.masaya.teachu.info.User;

import java.util.ArrayList;

/**
 * Created by takamasa on 2016/08/25.
 */
public class ActivityFavorite extends Activity {

    View mFooter;// フッターのプログレスバー（クルクル） View（クラス）の特性を持ったmFooterという言葉を定義
    Handler handler = new Handler();// サブスレッドでの動作の受け渡し
    Integer per_page = 6;//一回当たりのリストビューの数
    public Integer listview_count_now_taka = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        final ListView listview = (ListView)findViewById(R.id.favoriteListView_id);
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
        final FavoriteAdapter adapter = new FavoriteAdapter(this, 0, users);


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



    }






    //  フッターの定義
    private View getFooter() {
        if (mFooter == null) {
            // mFooterに値を代入．mFooterはすでに定義されてるので，newは必要ない
            mFooter = getLayoutInflater().inflate(R.layout.listview_footer,null);
        }
        return mFooter;
    }
}
