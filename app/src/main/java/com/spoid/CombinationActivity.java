package com.spoid;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by user on 2017-08-07.
 */

public class CombinationActivity extends Activity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_combination);

        ListView listView;
        ad adapter = new ad();

        listView = (ListView)findViewById(R.id.listview);
        listView.setAdapter(adapter);

        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.icon_hammer),"망치");
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.icon_scissors),"가위");
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.icon_saw),"톱");

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
