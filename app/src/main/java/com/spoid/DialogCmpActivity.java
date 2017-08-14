package com.spoid;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import static com.spoid.CombinationActivity.con;


/**
 * Created by DANBI on 2017-08-12.
 */

public class DialogCmpActivity extends Dialog {
    Button cmpbtn = (Button) findViewById(R.id.cmpbtn);

    DialogCfmActivity cfmdialog = new DialogCfmActivity(con);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.cmp_dialog);

        findViewById(R.id.cmpbtn).setOnClickListener(
                new ImageButton.OnClickListener() {
                    public void onClick(View v) {
                        CameraConnectionFragment.hlist.add(new Item(R.drawable.logo, "의자"));
                        CameraConnectionFragment.itemAdapter.notifyDataSetChanged();
                        cfmdialog.show();
                    }
                }
        );

    };





    public DialogCmpActivity(@NonNull Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
    }

}
