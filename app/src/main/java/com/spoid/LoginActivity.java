package com.spoid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by DANBI on 2017-08-10.
 */

public class LoginActivity extends AppCompatActivity {


    //다이얼로그------------------------------
    private DialogCmpActivity cmp_dialog;
    private DialogDeCmpActivity decfm_dialog;
    private DialogCmpCfmActivity cmpCfm_dialog;
    private DialogDeCmpCfmActivity decmpCfm_dialog;
    //----------------------------------------------

    private void DialogProgress() { // 로그인 다이얼로그
        ProgressDialog dialog = ProgressDialog.show(LoginActivity.this, "", "로그인 중입니다", true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText content = new EditText(this);
        content.setGravity(Gravity.RIGHT);

        final Button button = (Button) findViewById(R.id.button); //로그인 성공 후 카메라 메인화면
        final TextView resister = (TextView) findViewById(R.id.textView4);//회원가입


        View.OnClickListener listener1 = new View.OnClickListener() { //로그인 성공 후 카메라 메인화면
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(LoginActivity.this, CameraActivity.class));
            }

        };


//        //  들어가야하는 위치에 다이얼로그 걸어주세용~~ ----------------------
//        View.OnClickListener listener1 = new View.OnClickListener() { //cfm dialog
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                decmpCfm_dialog = new DialogDeCmpCfmActivity(LoginActivity.this);
//                decmpCfm_dialog.show();
//            }
//
//        };
//        // 들어가야하는 위치에 다이얼로그 걸어주세용~~ -----------------------



        View.OnClickListener listener2 = new View.OnClickListener() { //회원가입
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(LoginActivity.this, JoinActivity.class));
            }

        };

        button.setOnClickListener(listener1);
        resister.setOnClickListener(listener2);
        button.setOnClickListener(listener1);

    }


}

