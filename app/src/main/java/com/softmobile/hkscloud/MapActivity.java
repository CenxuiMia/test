package com.softmobile.hkscloud;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.softmobile.mialibrary.activity.StartIntent;
import com.softmobile.mialibrary.utility.CameraZoom;
import com.softmobile.mialibrary.utility.Utility;

import HksData.SingletonData;

public class MapActivity extends AppCompatActivity implements View.OnClickListener {
    private GoogleMap m_Map          = null;
    private double mSize = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setView();
        setUpMapIfNeeded();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
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
            case R.id.btnDial:
                StartIntent.openTelephone(this,SingletonData.getInstance().getCurrentEvent().getPhone());
                break;
            case R.id.imgbtnBack:
                break;
            case R.id.imgbtnNext:
                break;
        }

    }

    private void setView() {
        ImageButton btnDial = (ImageButton) findViewById(R.id.btnDial);
        btnDial.setOnClickListener(this);
        btnDial.setVisibility(View.INVISIBLE);
        ImageButton imgBtnBack = (ImageButton) findViewById(R.id.imgbtnBack);
        imgBtnBack.setOnClickListener(this);
        ImageButton imgBtnNext = (ImageButton) findViewById(R.id.imgbtnNext);
        imgBtnNext.setOnClickListener(this);

        TextView tvMapShopName = (TextView) findViewById(R.id.tvMapShopName);
        tvMapShopName.setText(SingletonData.getInstance().getCurrentShop().getName());
        TextView tvMapAddress = (TextView) findViewById(R.id.tvMapAddress);
        tvMapAddress.setText(SingletonData.getInstance().getCurrentShop().getAddress());
        TextView tvMapPhone = (TextView) findViewById(R.id.tvMapPhone);

        if (SingletonData.getInstance().getCurrentEvent().getPhone().isEmpty() == true) {
            tvMapPhone.setText(R.string.no_phone);
            btnDial.setVisibility(View.INVISIBLE);
        }else {
            tvMapPhone.setText(SingletonData.getInstance().getCurrentEvent().getPhone());
            btnDial.setVisibility(View.VISIBLE);
        }
    }

    private void setUpMapIfNeeded() {

        if (m_Map == null) {

            m_Map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            if (m_Map != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        LatLng latLng = new LatLng(Double.valueOf(SingletonData.getInstance().getCurrentShop().getLat())
                , Double.valueOf(SingletonData.getInstance().getCurrentShop().getLon()));
        m_Map.setMyLocationEnabled(true);
        m_Map.addMarker(new MarkerOptions().title(SingletonData.getInstance().getCurrentShop().getName())
                        .snippet(SingletonData.getInstance().getCurrentShop().getAddress()).position(latLng))
                        .setVisible(true);
        double D = Double.valueOf(SingletonData.getInstance().getCurrentShop().getDistance());

        if(D>mSize){
            mSize = D;
        }

        if (SingletonData.getInstance().getShopLength() == 2) {
            if (SingletonData.getInstance().getCurrentShopPosition() == 0) {
                addMarker(1);
            }else {
                addMarker(0);
            }
        }else {
            if (SingletonData.getInstance().getShopLength() == SingletonData.getInstance().getShopLength()-1) {
                int position = SingletonData.getInstance().getShopLength()-2;
                addMarker(position);
                addMarker(position-1);
            }

            if (SingletonData.getInstance().getShopLength() == SingletonData.getInstance().getShopLength()-2) {
                addMarker(SingletonData.getInstance().getShopLength()-1);
                addMarker(SingletonData.getInstance().getShopLength()-3);
            }else {//後面兩個
                addMarker(SingletonData.getInstance().getCurrentShopPosition()+1);
                addMarker(SingletonData.getInstance().getCurrentShopPosition()+2);
            }
        }

        m_Map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        m_Map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(SingletonData.getInstance()
                        .getLocation()
                        .getLatitude(),
                        SingletonData.getInstance()
                                .getLocation()
                                .getLongitude()), CameraZoom.getZoom((int) (mSize * 1000))));
        Utility.log("camera"+mSize);

    }

    private void addMarker(int position) {
        LatLng latLng = new LatLng(Double.valueOf(SingletonData.getInstance().getShop(position).getLat()),
                Double.valueOf( SingletonData.getInstance().getShop(position).getLon()));
        m_Map.addMarker(new MarkerOptions()
                .title(SingletonData.getInstance().getShop(position).getName())
                .snippet(SingletonData.getInstance().getShop(position).getAddress()).position(latLng));
        double D = Double.valueOf(SingletonData.getInstance().getShop(position).getDistance());
        mSize = D>mSize? D:mSize;
        Utility.log("marker"+mSize);
    }
}
