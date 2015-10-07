package com.softmobile.hkscloud;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.softmobile.mialibrary.activity.StartIntent;
import com.softmobile.mialibrary.view.ListViewAdapter;

import HksData.SingletonData;

public class EventListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private SAdapter mAdapter;
    private TextView m_tvShopName;
    private TextView m_tvShopAddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        setView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
        refreshView();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SingletonData.getInstance().setCurrentEvent(position);
        StartIntent.openActivity(this,GoodContextActivity.class);
    }

    private class SAdapter extends ListViewAdapter {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view =
                    LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_layout2,null);
            return new SViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            ((SViewHolder)viewHolder).textView.setText(SingletonData.getInstance()
                    .getCurrentShop().getEvent(position)
                    .getName());
        }

        @Override
        public int getItemCount() {
            return SingletonData.getInstance()
                    .getCurrentShop()
                    .getEventLength();
        }

        //is also compatible with recyclerView
        class SViewHolder extends ViewHolder {
            TextView textView;
            public SViewHolder(View v) {
                super(v);//設定子view
                textView = (TextView)v.findViewById(R.id.textView2);
            }
        }
    }

    private void setView() {
        ListView listView = (ListView) findViewById(R.id.listView);
        mAdapter = new SAdapter();
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
        m_tvShopName = (TextView) findViewById(R.id.tvShpName);

        m_tvShopAddr = (TextView) findViewById(R.id.tvShopAddr);

    }

    private void refreshView() {
        m_tvShopName.setText(SingletonData.getInstance()
                .getCurrentShop()
                .getName());
        m_tvShopAddr.setText(SingletonData.getInstance()
                .getCurrentShop()
                .getAddress());
    }
}
