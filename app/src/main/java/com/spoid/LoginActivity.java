package com.spoid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by DANBI on 2017-08-10.
 */

public class LoginActivity extends AppCompatActivity {

    private void DialogProgress() { // 로그인 다이얼로그
        ProgressDialog dialog = ProgressDialog.show(LoginActivity.this, "", "로그인 중입니다", true);
    }

    private DialogDefActivity def_dialog;

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

        View.OnClickListener listener2 = new View.OnClickListener() { //회원가입
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(LoginActivity.this, JoinActivity.class));
            }

        };


        resister.setOnClickListener(listener2);



}

    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.button:
                def_dialog = new DialogDefActivity(this,
                        "의자",
                        "나무",
                        "못",
                        "톱",
                        "의자(椅子)는 사람이 앉는데에 쓰는 가구이다.\n교상(交床)이라고도 한다.",
                        "2017.05.28 조합으로 획득");
                def_dialog.show();
                break;
        }
    }




}

