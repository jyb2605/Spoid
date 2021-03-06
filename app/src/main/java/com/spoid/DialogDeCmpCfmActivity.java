package com.spoid;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.WindowManager;

/**
 * Created by DANBI on 2017-08-14.
 */

public class DialogDeCmpCfmActivity extends Dialog {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.decmp_cfm_dialog);

    }
    public DialogDeCmpCfmActivity(@NonNull Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
    }
}
