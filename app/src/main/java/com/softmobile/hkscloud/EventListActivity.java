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

import HksData.DataBase;

public class EventListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private SAdapter m_adapter    = null;
    private TextView m_tvShopName = null;
    private TextView m_tvShopAddr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        setView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        m_adapter.notifyDataSetChanged();
        refreshView();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DataBase.getInstance().setCurrentEvent(position);
        StartIntent.openActivity(this,GoodContextActivity.class);
    }

    private class SAdapter extends ListViewAdapter {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                                      .inflate(R.layout.adapter_layout2,null);
            return new SViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            ((SViewHolder)viewHolder).textView.setText(DataBase.getInstance()
                                                       .getCurrentShop().getEvent(position)
                                                       .getName());
        }

        @Override
        public int getItemCount() {
            return DataBase.getInstance().getCurrentShop().getEventLength();
        }

        //compatible with recyclerView
        class SViewHolder extends ViewHolder {
            TextView textView = null;
            public SViewHolder(View v) {
                super(v);//設定子view
                textView = (TextView)v.findViewById(R.id.textView2);
            }
        }
    }

    private void setView() {
        ListView listView = (ListView)findViewById(R.id.listView);
        m_adapter = new SAdapter();
        listView.setAdapter(m_adapter);
        listView.setOnItemClickListener(this);
        m_tvShopName = (TextView)findViewById(R.id.tvShpName);
        m_tvShopAddr = (TextView)findViewById(R.id.tvShopAddr);

    }

    //reset
    private void refreshView() {
        m_tvShopName.setText(DataBase.getInstance().getCurrentShop().getName());
        m_tvShopAddr.setText(DataBase.getInstance().getCurrentShop().getAddress());
    }
}
