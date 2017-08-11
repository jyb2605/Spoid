package com.spoid;

import android.graphics.drawable.Drawable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by user on 2017-07-31.
 */

public class Item {
    String name;
    String recipe;
    String tool;
    String definition;
    String created_time;
    Drawable img_src;
    Drawable icon_src;

    public Item() {
    }

    public Item(String name, String recipe, String tool, String definition,
                Drawable img_src, Drawable icon_src) {
        this.name = name;
        this.recipe = recipe;
        this.tool = tool;
        this.definition = definition;
        this.img_src = img_src;
        this.icon_src = icon_src;
        this.created_time = "";

        created_time += Calendar.getInstance().get(Calendar.YEAR);
        created_time+='.';
        created_time += Calendar.getInstance().get(Calendar.MONTH);
        created_time+='.';
        created_time += Calendar.getInstance().get(Calendar.DATE);
        
    }
}
