package com.spoid;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class UndrBarActivity extends AppCompatActivity {

    Context mContext;
    ArrayList<Item> hlist;
    RecyclerView recyclerView;
    RecyclerView.Adapter Adapter;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_undrbar);

        hlist = new ArrayList<Item>();
        mContext = getApplicationContext();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        hlist.add(new Item(R.drawable.group_7, "나무"));
        hlist.add(new Item(R.drawable.group_8, "꽃"));
        hlist.add(new Item(R.drawable.group_9, "볼펜"));
        hlist.add(new Item(R.drawable.group_13, "안경"));
        hlist.add(new Item(R.drawable.group_14, "핸드폰"));
        hlist.add(new Item(R.drawable.group_15, "종이"));
        hlist.add(new Item(R.drawable.group_7, "나무"));
        hlist.add(new Item(R.drawable.group_8, "꽃"));
        hlist.add(new Item(R.drawable.group_9, "볼펜"));
        hlist.add(new Item(R.drawable.group_13, "안경"));
        hlist.add(new Item(R.drawable.group_14, "핸드폰"));
        hlist.add(new Item(R.drawable.group_15, "종이"));
        hlist.add(new Item(R.drawable.group_7, "나무"));
        hlist.add(new Item(R.drawable.group_8, "꽃"));
        hlist.add(new Item(R.drawable.group_9, "볼펜"));
        hlist.add(new Item(R.drawable.group_13, "안경"));
        hlist.add(new Item(R.drawable.group_14, "핸드폰"));
        hlist.add(new Item(R.drawable.group_15, "종이"));

        SectionListDataAdapter MyAdapter = new SectionListDataAdapter(mContext, hlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(MyAdapter);


    }


    public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {

        private ArrayList<Item> itemsList;
        private Context mContext;

        public SectionListDataAdapter(Context context, ArrayList<Item> itemsList) {
            this.itemsList = itemsList;
            this.mContext = context;
        }

        @Override
        public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, null);
            SingleItemRowHolder mh = new SingleItemRowHolder(v);
            return mh;
        }

        @Override
        public void onBindViewHolder(SingleItemRowHolder holder, int i) {

            Item singleItem = itemsList.get(i);
            holder.title.setText(singleItem.getName());
            holder.image.setImageResource(singleItem.getImg());

        }

        @Override
        public int getItemCount() {
            return (null != itemsList ? itemsList.size() : 0);
        }

        public class SingleItemRowHolder extends RecyclerView.ViewHolder {

            protected TextView title;
            protected ImageView image;


            public SingleItemRowHolder(View view) {
                super(view);

                this.title = (TextView) view.findViewById(R.id.textView);
                this.image = (ImageView) view.findViewById(R.id.imageView2);

                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(Gloval.getElement1State()){

                        }
                        else if(Gloval.getElement2State()){

                        }
                        else{
                            // 다이어로그 출력
                        }
                    }
                });

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(v.getContext(), title.getText(), Toast.LENGTH_SHORT).show();

                    }
                });


            }

        }


    }
}
