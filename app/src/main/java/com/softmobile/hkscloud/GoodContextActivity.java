package com.softmobile.hkscloud;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.softmobile.mialibrary.activity.StartIntent;

import HksData.DataBase;

public class GoodContextActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String DEFAULT = "0000-00-00 00:00:00";
    private static final String KM      = "km";

    private LinearLayout m_linearLayout = null;
    private TextView m_tvShopName       = null;
    private TextView m_tvName           = null;
    private TextView m_tvDate           = null;
    private ImageButton m_btnBack       = null;
    private ImageButton m_btnNext       = null;
    private TextView m_tvDetail         = null;
    private TextView m_tvNote           = null;
    private TextView m_tvEventWeb       = null;

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
                DataBase.getInstance().goLast();
                refreshEvent();
                break;
            case R.id.btnNext:
                //第一個event的狀況
                DataBase.getInstance().goNext();
                refreshEvent();
                break;
            case R.id.tvEventWeb:
                StartIntent.openWebURI(this,DataBase.getInstance().getCurrentEvent().getWeb());
                break;
            case R.id.tvShopName:
                StartIntent.openActivity(this,MapActivity.class);
                break;
            case R.id.viewMap:
                StartIntent.openActivity(this,MapActivity.class);
                break;
        }
    }

    private void setView() {

        m_linearLayout = (LinearLayout)findViewById(R.id.linearLayout);

        m_btnBack = (ImageButton)findViewById(R.id.btnBack);
        m_btnBack.setOnClickListener(this);
        m_btnNext = (ImageButton)findViewById(R.id.btnNext);
        m_btnNext.setOnClickListener(this);

        m_tvShopName = (TextView)findViewById(R.id.tvShopName);
        m_tvShopName.setOnClickListener(this);
        m_tvName = (TextView)findViewById(R.id.tvName);
        m_tvDate = (TextView)findViewById(R.id.tvDate);
        m_tvDetail = (TextView)findViewById(R.id.tvDetail);
        m_tvNote = (TextView)findViewById(R.id.tvNote);
        m_tvEventWeb = (TextView)findViewById(R.id.tvEventWeb);
        ImageView ivMap = (ImageView)findViewById(R.id.viewMap);
        ivMap.setOnClickListener(this);

        refreshEvent();
    }

    private void refreshEvent() {
        m_tvShopName.setText(new StringBuilder()
                    .append(DataBase.getInstance().getCurrentShop().getName())
                    .append("\n")
                    .append(DataBase.getInstance().getCurrentShop().getDistance())
                    .append(KM));
        m_tvName.setText(DataBase.getInstance().getCurrentEvent().getName());

        if (DataBase.getInstance().getCurrentEvent().getStartDate().equals(DEFAULT)) {
            m_tvDate.setText(new StringBuilder()
                    .append(getString(R.string.from_now))
                    .append("\n")
                    .append("↓")
                    .append("\n")
                    .append(DataBase.getInstance().getCurrentEvent().getEndDate()));
        } else {
            m_tvDate.setText(new StringBuilder()
                    .append(DataBase.getInstance().getCurrentEvent().getStartDate())
                    .append("\n")
                    .append("↓")
                    .append("\n")
                    .append(DataBase.getInstance().getCurrentEvent().getEndDate()));
        }

        m_tvDetail.setText(DataBase.getInstance().getCurrentEvent().getDetail());
        m_tvNote.setText(DataBase.getInstance().getCurrentEvent().getNote());

        m_btnBack.setEnabled(true);
        m_btnNext.setEnabled(true);

        m_linearLayout.removeAllViews();

        //活動網址
        if (TextUtils.isEmpty(DataBase.getInstance().getCurrentEvent().getWeb())) {
            m_tvEventWeb.setVisibility(View.INVISIBLE);
        } else {
            m_tvEventWeb.setVisibility(View.VISIBLE);
            m_tvEventWeb.setOnClickListener(this);
        }

        View viewCouponList;
        int iCouponLength = DataBase.getInstance().getCurrentEvent().getCouponLength();
        String strCoupon;
        String strUrl;

        for (int iPosition=0;iPosition<iCouponLength;iPosition++) {
            viewCouponList = getLayoutInflater().inflate(R.layout.adapter_layout2,null);
            viewCouponList.setOnClickListener(couponListListener);
            viewCouponList.setTag(iPosition);

            strCoupon = DataBase.getInstance().getCurrentEvent().getCoupon(iPosition).getName();
            strUrl = DataBase.getInstance().getCurrentEvent().getCoupon(iPosition).getURL();

            if (TextUtils.isEmpty(strCoupon) == true || TextUtils.isEmpty(strUrl) == true) {
                m_linearLayout.setVisibility(View.INVISIBLE);
            }

            ((TextView) viewCouponList.findViewById(R.id.textView2)).setText(strCoupon);
            m_linearLayout.addView(viewCouponList);
        }
    }

    private View.OnClickListener couponListListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //傳資料過去
            int iPosition = (int)v.getTag();
            DataBase.getInstance().setCurrentCoupon(iPosition);
            StartIntent.openActivity(GoodContextActivity.this,CouponActivity.class);
        }
    };
}
