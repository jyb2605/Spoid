package com.spoid;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by user on 2017-08-07.
 */

public class CombinationActivity extends Activity {


    ViewGroup element1;
    ViewGroup element2;
    ImageView e1_img;
    ImageView e2_img;
    TextView e1_name;
    TextView e2_name;
    static Item item_position01;
    static Item item_position02;

   public static Context con;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_combination);

        element1 = (ViewGroup)findViewById(R.id.element1);
        element2 = (ViewGroup)findViewById(R.id.element2);

        e1_img = (ImageView) findViewById(R.id.e1_img);
        e2_img = (ImageView) findViewById(R.id.e2_img);
        e1_name = (TextView) findViewById(R.id.e1_name);
        e2_name = (TextView) findViewById(R.id.e2_name);
        con = CombinationActivity.this;


        ListView listView;
        ad adapter = new ad();

        listView = (ListView)findViewById(R.id.listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DialogCmpActivity cmpdialog = new DialogCmpActivity(parent.getContext());
                cmpdialog.show();

            }
        });
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.btn_x),"없음");
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.icon_hammer),"망치");
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.icon_scissors),"가위");
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.icon_saw),"톱");


        // item add working
        element1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Gloval.getElement1State()){
                    Gloval.element1Off();
                    element1.setBackgroundResource(R.drawable.gray_border);
                }
                else {
                    element1.setBackgroundResource(R.drawable.blue_border);
                    Gloval.element1On();
                    Gloval.element2Off();
                    element2.setBackgroundResource(R.drawable.gray_border);
                }
            }
        });

        element2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Gloval.getElement2State()){
                    Gloval.element2Off();
                    element2.setBackgroundResource(R.drawable.gray_border);
                }
                else {
                    element2.setBackgroundResource(R.drawable.blue_border);
                    Gloval.element2On();
                    Gloval.element1Off();
                    element1.setBackgroundResource(R.drawable.gray_border);
                }
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(CameraConnectionFragment.itemAdapter);

        final GestureDetector gestureDetector = new GestureDetector(getApplicationContext(),new GestureDetector.SimpleOnGestureListener()
        {
            @Override
            public boolean onSingleTapUp(MotionEvent e)
            {
                return true;
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener()
        {
            String TAG = "LOG";
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e)
            {
                Log.d(TAG,"onInterceptTouchEvent");
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if(child!=null&&gestureDetector.onTouchEvent(e))
                {
                    Log.d(TAG, "getChildAdapterPosition=>" + rv.getChildAdapterPosition(child));
                    Log.d(TAG,"getChildLayoutPosition=>"+rv.getChildLayoutPosition(child));
                    Log.d(TAG,"getChildViewHolder=>" + rv.getChildViewHolder(child));



                    if(Gloval.getElement1State()){
                        //1이 선택되어 있다면 진입

                        item_position02 = CameraConnectionFragment.hlist.get(rv.getChildViewHolder(child).getPosition());

                        e2_img.setImageResource(CameraConnectionFragment.hlist.get(rv.getChildViewHolder(child).getPosition()).getImg());
                        e2_name.setText(CameraConnectionFragment.hlist.get(rv.getChildViewHolder(child).getPosition()).name);
                    }
                    else {
                        //1이 선택되어 있지 않다면 진입
                        item_position01 = CameraConnectionFragment.hlist.get(rv.getChildViewHolder(child).getPosition());

                        e1_img.setImageResource(CameraConnectionFragment.hlist.get(rv.getChildViewHolder(child).getPosition()).getImg());
                        e1_name.setText(CameraConnectionFragment.hlist.get(rv.getChildViewHolder(child).getPosition()).name);
                    }

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e)
            {
                Log.d(TAG,"onTouchEvent");

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept)
            {
                Log.d(TAG,"onRequestDisallowInterceptTouchEvent");
            }
        });



    }

    public class ad extends BaseAdapter{

        private ArrayList<Tool> listViewItemList = new ArrayList<Tool>();

        public ad(){}

        @Override
        public int getCount() {
            return listViewItemList.size();
        }

        @Override
        public Object getItem(int position) {
            return listViewItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           final int pos = position;
            final Context context = parent.getContext();
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.tool_item,parent,false);
            }
            ImageView iconImageView = (ImageView) convertView.findViewById(R.id.tool_img);
            TextView nameTextView = (TextView)convertView.findViewById(R.id.tool_name);
            Tool tool = listViewItemList.get(position);
            iconImageView.setImageDrawable(tool.getIcon());
            nameTextView.setText(tool.getName());
            return convertView;
        }

        public void addItem(Drawable icon, String name){
            Tool item = new Tool();
            item.setIcon(icon);
            item.setName(name);
            listViewItemList.add(item);
        }
    }


}
