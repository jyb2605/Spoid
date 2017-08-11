package com.spoid;

import java.util.Calendar;

/**
 * Created by user on 2017-07-31.
 */

public class Item {
    String name;
    String recipe;
    String tool;
    String definition;
    String created_time;
    int img_src;
    int icon_src;

    public Item() {
    }
    public Item(int img_src, String name){
        this.name = name;
        this.img_src = img_src;
    }
    public Item(String name, String recipe, String tool, String definition,
                int img_src, int icon_src) {
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
    public String getName() {
      return name;
    }
    public int getImg()
    {
        return img_src;
    }
}
