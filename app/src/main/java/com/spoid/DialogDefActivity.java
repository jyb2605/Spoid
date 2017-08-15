package com.spoid;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by DANBI on 2017-08-11.
 */

public class DialogDefActivity extends Dialog{

    private TextView mTitleView;
    Context mContext;
    private TextView mContentView1_1;
    private TextView mContentView1_2;
    private TextView mContentView1_3;

    private TextView mContentView2;
    private TextView mContentView3;

    private ImageView mImage;
    private String mTitle;
    private String mContent1_1;
    private String mContent1_2;
    private String mContent1_3;
    private String mContent2;
    private String mContent3;

   public DialogDeCmpActivity decmpdialog = new DialogDeCmpActivity(getContext());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.def_dialog);

        mImage = (ImageView) findViewById(R.id.def_img);
        mImage.setImageResource(R.drawable.def_1);

        mTitleView = (TextView) findViewById(R.id.txt_title);
        mContentView1_1 = (TextView) findViewById(R.id.txt_content1_1);
        mContentView1_2 = (TextView) findViewById(R.id.txt_content1_2);
        mContentView1_3 = (TextView) findViewById(R.id.txt_content1_3);
        mContentView2 = (TextView) findViewById(R.id.txt_content2);
        mContentView3 = (TextView) findViewById(R.id.txt_content3);

        Button defbtn = (Button) findViewById(R.id.defbtn);

        defbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                decmpdialog.show();

            }
        });


        // 제목,내용 세팅
        mTitleView.setText(mTitle);
        mContentView1_1.setText(mContent1_1);
        mContentView1_2.setText(mContent1_2);
        mContentView1_3.setText(mContent1_3);
        mContentView2.setText(mContent2);
        mContentView3.setText(mContent3);


    }

    public DialogDefActivity(Context context, String txt_title , String txt_content1_1, String txt_content1_2,
                             String txt_content1_3, String txt_content2, String txt_content3) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mTitle=txt_title;
        this.mContent1_1=txt_content1_1;
        this.mContent1_2=txt_content1_2;
        this.mContent1_3=txt_content1_3;
        this.mContent2=txt_content2;
        this.mContent3=txt_content3;

    }


}
