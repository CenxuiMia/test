package com.softmobile.hkscloud;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.softmobile.mialibrary.activity.StartIntent;

import HksData.SingletonData;

public class GoodContextActivity extends AppCompatActivity implements View.OnClickListener {

    private SGoodContextActivityUI m_GoodContextActivityUI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_context);
        setView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_good_context, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                //進入第一個event的狀況
                SingletonData.getInstance().lastEvent();
                refreshEvent();
                break;
            case R.id.btnNext:
                //第一個event的狀況
                SingletonData.getInstance().nextEvent();
                refreshEvent();
                break;
            case R.id.tvEventWeb:
                StartIntent.openWebURI(this,SingletonData.getInstance().getCurrentEvent().getWeb());
                break;
            case R.id.tvShopName:
                StartIntent.openActivity(this, MapActivity.class);
        }
    }

    private void setView() {

        m_GoodContextActivityUI = new SGoodContextActivityUI();

        m_GoodContextActivityUI.linearLayout = (LinearLayout)findViewById(R.id.linearLayout);

        m_GoodContextActivityUI.btnBack = (ImageButton)findViewById(R.id.btnBack);
        m_GoodContextActivityUI.btnBack.setOnClickListener(this);
        m_GoodContextActivityUI.btnNext = (ImageButton)findViewById(R.id.btnNext);
        m_GoodContextActivityUI.btnNext.setOnClickListener(this);

        m_GoodContextActivityUI.tvShopName = (TextView)findViewById(R.id.tvShopName);
        m_GoodContextActivityUI.tvShopName.setOnClickListener(this);
        m_GoodContextActivityUI.tvName = (TextView)findViewById(R.id.tvName);
        m_GoodContextActivityUI.tvDate = (TextView)findViewById(R.id.tvDate);
        m_GoodContextActivityUI.tvDetail = (TextView)findViewById(R.id.tvDetail);
        m_GoodContextActivityUI.tvNote = (TextView)findViewById(R.id.tvNote);
        m_GoodContextActivityUI.tvEventWeb = (TextView)findViewById(R.id.tvEventWeb);

        refreshEvent();
    }

    private class SGoodContextActivityUI {
        LinearLayout linearLayout = null;
        TextView tvShopName       = null;
        TextView tvName           = null;
        TextView tvDate           = null;
        ImageButton btnBack       = null;
        ImageButton btnNext       = null;
        TextView tvDetail         = null;
        TextView tvNote           = null;
        TextView tvEventWeb       = null;
    }

    private void refreshEvent() {
        m_GoodContextActivityUI.tvShopName.setText(new StringBuilder()
                    .append(SingletonData.getInstance().getCurrentShop().getName())
                    .append("\n")
                    .append(SingletonData.getInstance().getCurrentShop().getDistance())
                    .append("km"));
        m_GoodContextActivityUI.tvName.setText(SingletonData.getInstance().getCurrentEvent().getName());

        if (SingletonData.getInstance().getCurrentEvent().getStartDate().equals("0000-00-00 00:00:00")) {
            m_GoodContextActivityUI.tvDate.setText(new StringBuilder()
                    .append(getString(R.string.from_now))
                    .append("\n")
                    .append("↓")
                    .append("\n")
                    .append(SingletonData.getInstance().getCurrentEvent().getEndDate()));
        }else {
            m_GoodContextActivityUI.tvDate.setText(new StringBuilder()
                    .append(SingletonData.getInstance().getCurrentEvent().getStartDate())
                    .append("\n")
                    .append("↓")
                    .append("\n")
                    .append(SingletonData.getInstance().getCurrentEvent().getEndDate()));
        }
        m_GoodContextActivityUI.tvDetail.setText(SingletonData.getInstance().getCurrentEvent().getDetail());
        m_GoodContextActivityUI.tvNote.setText(SingletonData.getInstance().getCurrentEvent().getNote());

        m_GoodContextActivityUI.btnBack.setEnabled(true);
        m_GoodContextActivityUI.btnNext.setEnabled(true);

        m_GoodContextActivityUI.linearLayout.removeAllViews();

        //活動網址
        if (TextUtils.isEmpty(SingletonData.getInstance().getCurrentEvent().getWeb())) {
            m_GoodContextActivityUI.tvEventWeb.setVisibility(View.INVISIBLE);
        }else {
            m_GoodContextActivityUI.tvEventWeb.setVisibility(View.VISIBLE);
            m_GoodContextActivityUI.tvEventWeb.setOnClickListener(this);
        }

        View viewCouponList;

        for (int iPosition = 0;
             iPosition<SingletonData.getInstance().getCurrentEvent().getCouponLength();
             iPosition++) {
            viewCouponList = getLayoutInflater().inflate(R.layout.adapter_layout2,null);
            viewCouponList.setOnClickListener(couponListListener);
            viewCouponList.setTag(iPosition);

            String strCoupon = SingletonData.getInstance().getCurrentEvent().getCoupon(iPosition).getName();
            String strUrl = SingletonData.getInstance().getCurrentEvent().getCoupon(iPosition).getURL();

            if (TextUtils.isEmpty(strCoupon)||TextUtils.isEmpty(strUrl)) {
                m_GoodContextActivityUI.linearLayout.setVisibility(View.INVISIBLE);
            }

            ((TextView) viewCouponList.findViewById(R.id.textView2)).setText(strCoupon);
            m_GoodContextActivityUI.linearLayout.addView(viewCouponList);
        }
    }

    private View.OnClickListener couponListListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //傳資料過去
            int iPosition = (int)v.getTag();
            SingletonData.getInstance().setCurrentCoupon(iPosition);
            StartIntent.openActivity(GoodContextActivity.this,CouponActivity.class);
        }
    };
}
