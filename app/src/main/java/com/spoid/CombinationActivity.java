package com.spoid;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

/**
 * Created by user on 2017-08-07.
 */

public class CombinationActivity extends Activity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_combination);

    }
}
