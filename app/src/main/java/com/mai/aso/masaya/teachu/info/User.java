package com.mai.aso.masaya.teachu.info;

import android.graphics.Bitmap;

/**
 * Created by takamasa on 2016/08/22.
 */


public class User {
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