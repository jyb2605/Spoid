package com.spoid;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by DANBI on 2017-07-31.
 */

public class JoinActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_join);

        EditText content = new EditText(this);
        content.setGravity(Gravity.RIGHT);
        int flag = 0; //0이면 보이게, 1이면 안보이게, 3이면 사라지게

        final Button button = (Button) findViewById(R.id.button);
        final Button btn_man = (Button) findViewById(R.id.man); // 남성 클릭시 진하게
        final Button btn_man_b  = (Button) findViewById(R.id.man_bold);
        final Button btn_woman = (Button) findViewById(R.id.woman); // 여성 클릭시 진하게
        View.OnClickListener listener1 = new View.OnClickListener() { // 회원가입 완료후 로그인페이지로!
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(JoinActivity.this, LoginActivity.class));
            }
        };
        View.OnClickListener listener2 = new View.OnClickListener() { // 회원가입 완료후 로그인페이지로!
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                btn_man.setVisibility(View.GONE);
                btn_man_b.setVisibility(View.VISIBLE);
            }
        };

        button.setOnClickListener(listener1);
        btn_man.setOnClickListener(listener2);




    }
}
