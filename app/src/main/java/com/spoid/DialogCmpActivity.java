package com.spoid;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;


/**
 * Created by DANBI on 2017-08-12.
 */

public class DialogCmpActivity extends Dialog {
    Context con;
    DialogCmpCfmActivity cfmdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cfmdialog = new DialogCmpCfmActivity(con);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.cmp_dialog);

        findViewById(R.id.cmpbtn).setOnClickListener(
                new ImageButton.OnClickListener() {
                    public void onClick(View v) {
                        CameraConnectionFragment.hlist.remove(CombinationActivity.item_position01);
                        CameraConnectionFragment.hlist.remove(CombinationActivity.item_position02);


                        CameraConnectionFragment.hlist.add(new Item(R.drawable.icon_chair, "의자"));
                        CameraConnectionFragment.itemAdapter.notifyDataSetChanged();
                        dismiss();
                        cfmdialog.show();
                    }
                }
        );

    };

    public DialogCmpActivity(@NonNull Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        con= context;
    }

}
