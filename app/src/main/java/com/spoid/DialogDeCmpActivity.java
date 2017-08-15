package com.spoid;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import static com.spoid.R.id.decmpbtn;

/**
 * Created by DANBI on 2017-08-13.
 */

public class DialogDeCmpActivity extends Dialog {
    public DialogDeCmpCfmActivity decmpcfm = new DialogDeCmpCfmActivity(getContext());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.decmp_dialog);

        Button btn = (Button) findViewById(decmpbtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraConnectionFragment.hlist.remove(CameraConnectionFragment.item_position);
                CameraConnectionFragment.hlist.add(new Item(R.drawable.icon_board, "나무판자"));
                CameraConnectionFragment.hlist.add(new Item(R.drawable.icon_nail, "못"));
                CameraConnectionFragment.itemAdapter.notifyDataSetChanged();
                decmpcfm.show();
                dismiss();
            }
        });
    }



    public DialogDeCmpActivity(@NonNull Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
    }

}
