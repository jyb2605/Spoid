package com.spoid;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
    Handler handler;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler = new Handler();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(2500);
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            Animation fade_out = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
//                        }
//                    });
//
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                intent = new Intent(SplashActivity.this, CameraActivity.class);
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        startActivity(intent);
//                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                        finish();
//                    }
//                });
//
//            }
//        }).start();

        final Button button = (Button) findViewById(R.id.button);
        final TextView resister = (TextView) findViewById(R.id.textView4);


        View.OnClickListener listener1 = new View.OnClickListener() { //로그인페이지
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }

        };
        View.OnClickListener listener2 = new View.OnClickListener() { //회원가입
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(SplashActivity.this, JoinActivity.class));
            }

        };

        resister.setOnClickListener(listener2);
        button.setOnClickListener(listener1);
    }


}
