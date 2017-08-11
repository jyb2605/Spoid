package com.spoid;

import android.app.Application;

/**
 * Created by user on 2017-08-11.
 */

public class Gloval extends Application {
    private static  boolean element1_clicked = false;
    private static  boolean element2_clicked = false;

    public static void element1On(){
        element1_clicked = true;
    }

    public static  void element1Off(){
        element1_clicked = false;
    }

    public static  void element2On(){
        element2_clicked = true;
    }

    public static  void element2Off(){
        element2_clicked = false;
    }

    public static  boolean getElement1State(){
        return element1_clicked;
    }
    public static  boolean getElement2State(){
        return element2_clicked;
    }



}
