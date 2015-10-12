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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.softmobile.mialibrary.activity.StartIntent;
import com.softmobile.mialibrary.utility.CameraZoom;
import com.softmobile.mialibrary.utility.Utility;

import java.util.ArrayList;

import HksData.DataBase;

public class MapActivity extends AppCompatActivity implements View.OnClickListener {
    private GoogleMap m_Map               = null;
    private ArrayList<Marker> m_alMarkers = null;
    private double m_dbSize               = 0;
    private int m_iMarkerNumber           = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setView();
        setUpMap();
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
                StartIntent.openTelephone(this, DataBase.getInstance().getCurrentEvent().getPhone());
                break;
            case R.id.imgbtnBack:
                m_dbSize = 0;
                m_alMarkers.get(m_iMarkerNumber).hideInfoWindow();

                if (m_iMarkerNumber == 0) {
                    m_iMarkerNumber = m_alMarkers.size()-1;
                } else {
                    m_iMarkerNumber = m_iMarkerNumber-1;
                }

                Marker marker = m_alMarkers.get(m_iMarkerNumber);
                marker.showInfoWindow();
                m_Map.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                break;
            case R.id.imgbtnNext:
                m_dbSize = 0;
                m_alMarkers.get(m_iMarkerNumber).hideInfoWindow();

                if (m_iMarkerNumber < m_alMarkers.size()-1) {
                    m_iMarkerNumber = m_iMarkerNumber+1;
                } else {
                    m_iMarkerNumber = 0;
                }

                marker = m_alMarkers.get(m_iMarkerNumber);
                marker.showInfoWindow();
                m_Map.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                break;
        }

    }

    private void setView() {
        m_Map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();
        ImageButton btnDial = (ImageButton) findViewById(R.id.btnDial);
        btnDial.setOnClickListener(this);
        btnDial.setVisibility(View.INVISIBLE);
        ImageButton imgBtnBack = (ImageButton) findViewById(R.id.imgbtnBack);
        imgBtnBack.setOnClickListener(this);
        ImageButton imgBtnNext = (ImageButton) findViewById(R.id.imgbtnNext);
        imgBtnNext.setOnClickListener(this);

        TextView tvMapShopName = (TextView) findViewById(R.id.tvMapShopName);
        tvMapShopName.setText(DataBase.getInstance().getCurrentShop().getName());
        TextView tvMapAddress = (TextView) findViewById(R.id.tvMapAddress);
        tvMapAddress.setText(DataBase.getInstance().getCurrentShop().getAddress());
        TextView tvMapPhone = (TextView) findViewById(R.id.tvMapPhone);

        m_alMarkers = new ArrayList<>();

        if (DataBase.getInstance().getCurrentEvent().getPhone().isEmpty() == true) {
            tvMapPhone.setText(R.string.no_phone);
            btnDial.setVisibility(View.INVISIBLE);
        } else {
            tvMapPhone.setText(DataBase.getInstance().getCurrentEvent().getPhone());
            btnDial.setVisibility(View.VISIBLE);
        }
    }

    private void setUpMap() {
        LatLng latLng = new LatLng(Double.valueOf(DataBase.getInstance().getCurrentShop().getLat())
                ,Double.valueOf(DataBase.getInstance().getCurrentShop().getLon()));

        m_Map.setMyLocationEnabled(true);
        Marker markerTheShop = m_Map.addMarker(new MarkerOptions().title(DataBase.getInstance()
                                               .getCurrentShop().getName()).snippet(DataBase
                                               .getInstance().getCurrentShop().getAddress())
                                               .position(latLng));
        markerTheShop.showInfoWindow();
        m_alMarkers.add(markerTheShop);
        double dbDistance = Double.valueOf(DataBase.getInstance().getCurrentShop().getDistance());

        if(dbDistance > m_dbSize){
            m_dbSize = dbDistance;
        }

        if (DataBase.getInstance().getShopLength() <= 1) {
            return;
        }

        if (DataBase.getInstance().getShopLength() == 2) {

            if (DataBase.getInstance().getCurrentShopPosition() == 0) {
                addMarker(1);
            } else {
                addMarker(0);
            }
        } else {
            //the last shop
            if (DataBase.getInstance().getCurrentShopPosition()
                    == DataBase.getInstance().getShopLength()-1) {
                int position = DataBase.getInstance().getCurrentShopPosition();
                addMarker(position-1);
                addMarker(position-2);
            } else if (DataBase.getInstance().getCurrentShopPosition()
                    == DataBase.getInstance().getShopLength()-2) {
                addMarker(DataBase.getInstance().getShopLength()-1);
                addMarker(DataBase.getInstance().getShopLength()-3);
            } else {//後面兩個
                addMarker(DataBase.getInstance().getCurrentShopPosition()+1);
                addMarker(DataBase.getInstance().getCurrentShopPosition()+2);
            }
        }

        m_Map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        m_Map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(DataBase.getInstance().getLocation().getLatitude(),
                           DataBase.getInstance().getLocation().getLongitude()),
                CameraZoom.getZoom((int) (m_dbSize * 1000))));
        Utility.log("camera"+ m_dbSize);

    }

    private void addMarker(int position) {
        LatLng latLngShop = new LatLng(Double.valueOf(DataBase.getInstance()
                                       .getShop(position).getLat()),Double.valueOf(DataBase
                                       .getInstance().getShop(position).getLon()));
        Marker marker = m_Map.addMarker(new MarkerOptions()
                                        .title(DataBase.getInstance().getShop(position).getName())
                                        .snippet(DataBase.getInstance().getShop(position).getAddress())
                                        .position(latLngShop));
        m_alMarkers.add(marker);
        double dbDistance = Double.valueOf(DataBase.getInstance().getShop(position).getDistance());
        m_dbSize = dbDistance>m_dbSize ? dbDistance:m_dbSize;
        Utility.log("marker"+ m_dbSize);

    }
}
