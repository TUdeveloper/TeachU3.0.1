package com.mai.aso.masaya.teachu.info;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mai.aso.masaya.teachu.R;

import java.util.ArrayList;

/**
 * Created by takamasa on 2016/08/22.
 */





// Userクラスを使ってAdapterをセットするためのクラス（よくわからん．こういうもんらしい）
public class PublicAdapter extends ArrayAdapter<User> {
    private LayoutInflater layoutInflater;
    public PublicAdapter(Context c, int id, ArrayList<User> users) {
        super(c, id, users);
        this.layoutInflater = (LayoutInflater) c.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
        );
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView == null){
            convertView = layoutInflater.inflate(
                    R.layout.listview_public, parent, false
            );
        }
        User user = (User) getItem(position);
        ((ImageView) convertView.findViewById(R.id.user_icon)).setImageBitmap(user.getIcon());
        ((TextView) convertView.findViewById(R.id.name_favorite)).setText(user.getName());
        ((TextView) convertView.findViewById(R.id.user_language)).setText(user.getLanguage());
        ((TextView) convertView.findViewById(R.id.user_comment)).setText(user.getComment());

        return convertView;
    }
}