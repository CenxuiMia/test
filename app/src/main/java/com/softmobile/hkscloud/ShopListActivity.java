package com.softmobile.hkscloud;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.softmobile.mialibrary.Http.HttpDownload;
import com.softmobile.mialibrary.activity.StartIntent;
import com.softmobile.mialibrary.activity.SystemService;
import com.softmobile.mialibrary.utility.Utility;
import com.softmobile.mialibrary.view.ListViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import HksData.DataBase;

public class ShopListActivity extends AppCompatActivity implements LocationListener,
                                                                   AbsListView.OnScrollListener,
                                                                   AdapterView.OnItemClickListener {
    private static final String BRANCH     = "branch";
    private static final String APPID      = "119871";
    private static final String ANDROID    = "ANDROID";
    private static final int LIMIT         = 30;
    private static final int TWENTY        = 20;
    private static final long MINTIME      = 300;
    private static final float MINDISTANCE = 1;
    private static final double DEF_LON    = 121.564673;
    private static final double DEF_LAT    = 25.033938;
    private static final String KM         = "km";

    private ListViewAdapter m_ListViewAdapter = null;
    private int m_iDownload                   = 0; //滑動下載資料
    private boolean m_bIsDownload             = false;
    private ProgressBar m_pbLoading           = null;
    private String m_strGroupCode             = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        DataBase.getInstance().clear();//資料清空
        //抓上層資料
        m_strGroupCode = StartIntent.getActivityIntentBundle(this)
                         .getString(MainActivity.DATA_GROUP_CODE);
        setView();
        getLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        m_ListViewAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_good_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location.getLatitude() == 0 &&location.getLongitude() == 0) {
            return;
        }

        DataBase.getInstance().setLocation(location);
        downloadData();
        LocationManager locationManager = SystemService.getLocationManager(this);
        locationManager.removeUpdates(this);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                                           int visibleItemCount,
                                           int totalItemCount) {

        if (m_bIsDownload == true) {
            return;
        }

        //沒資料
        if (totalItemCount == 0) return;

        //滑動剩20筆則下載資料
        if ((totalItemCount-firstVisibleItem)<TWENTY) {
            downloadData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DataBase.getInstance().setCurrentShop(position);

        //whether to open EventListActivity
        if (DataBase.getInstance().getCurrentShop().getEventLength() == 1) {
            StartIntent.openActivity(this,GoodContextActivity.class);
        } else {
            StartIntent.openActivity(this,EventListActivity.class);
        }
    }

    private class SAdapter extends ListViewAdapter {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                                      .inflate(R.layout.adapter_layout, null);
            return new SViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            ((SViewHolder)viewHolder).tvName.setText(DataBase.getInstance()
                    .getShop(position).getName());
            ((SViewHolder)viewHolder).tvDistance.setText(getString(R.string.distance) +
                    DataBase.getInstance().getShop(position).getDistance() + KM);

            //set eventName in ListView
            if (DataBase.getInstance().getShop(position).getEventLength()==1){
                ((SViewHolder)viewHolder).tvSub.setText(DataBase.getInstance()
                        .getShop(position).getEvent(0).getName());
            } else if (DataBase.getInstance().getShop(position).getEventLength()==2){
                ((SViewHolder)viewHolder).tvSub.setText(DataBase.getInstance()
                        .getShop(position).getEvent(0).getName()+"|"+ DataBase.getInstance()
                        .getShop(position).getEvent(1).getName());
            } else {
                ((SViewHolder)viewHolder).tvSub.setText(DataBase.getInstance()
                        .getShop(position).getEvent(0).getName()+"|"+ DataBase.getInstance()
                        .getShop(position).getEvent(1).getName()+"|"+ DataBase.getInstance()
                        .getShop(position).getEvent(2).getName());
            }

            if (position%7==0) {
                ((SViewHolder)viewHolder).ivLogo.setImageDrawable(ContextCompat
                                                .getDrawable(ShopListActivity.this, R.drawable.shop1));
                return;
            }

            if (position%7==1) {
                ((SViewHolder)viewHolder).ivLogo.setImageDrawable(ContextCompat
                                                .getDrawable(ShopListActivity.this, R.drawable.shop2));
                return;
            }

            if (position%7==2) {
                ((SViewHolder)viewHolder).ivLogo.setImageDrawable(ContextCompat
                                                .getDrawable(ShopListActivity.this, R.drawable.shop3));
                return;
            }

            if (position%7==3) {
                ((SViewHolder)viewHolder).ivLogo.setImageDrawable(ContextCompat
                                                .getDrawable(ShopListActivity.this, R.drawable.shop4));
                return;
            }

            if (position%7==4) {
                ((SViewHolder)viewHolder).ivLogo.setImageDrawable(ContextCompat
                                                .getDrawable(ShopListActivity.this, R.drawable.shop5));
                return;
            }

            if (position%7==5) {
                ((SViewHolder)viewHolder).ivLogo.setImageDrawable(ContextCompat
                                                .getDrawable(ShopListActivity.this, R.drawable.shop6));
                return;
            }

            if (position%7==6) {
                ((SViewHolder)viewHolder).ivLogo.setImageDrawable(ContextCompat
                                                .getDrawable(ShopListActivity.this, R.drawable.shop7));
            }
        }

        @Override
        public int getItemCount() {
            return DataBase.getInstance().getShopLength();
        }

        class SViewHolder extends ViewHolder {
            TextView tvName     = null;
            TextView tvSub      = null;
            TextView tvDistance = null;
            ImageView ivLogo    = null;
            public SViewHolder(View v) {
                super(v);//設定子view
                tvName     = (TextView)v.findViewById(R.id.textView2);
                tvSub      = (TextView)v.findViewById(R.id.tvSub);
                tvDistance = (TextView)v.findViewById(R.id.tvDistance);
                ivLogo     = (ImageView)v.findViewById(R.id.imgLogo);
            }
        }
    }

    private class SHttpDownload extends HttpDownload {
        public SHttpDownload() {
            super();
        }

        @Override
        public void handleData(String strResponse) {
            try {
                JSONObject joShop = new JSONObject(strResponse);

                if (joShop.optString(BRANCH).equals("null")) {
                    handler.sendEmptyMessage(2);
                    return;
                }

                JSONArray jaShopArray = joShop.getJSONArray(BRANCH);
                DataBase.getInstance().setShop(jaShopArray);

                if (jaShopArray.length() < LIMIT) {
                    handler.sendEmptyMessage(1);
                    return;
                }

                handler.sendEmptyMessage(0);
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                Utility.log("response = " + strResponse);
            }
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            //normal download
            if (msg.what == 0) {
                m_ListViewAdapter.notifyDataSetChanged();
                m_bIsDownload = false;
                setProgressBar();
                return;
            }

            //no more download
            if (msg.what == 1) {
                m_ListViewAdapter.notifyDataSetChanged();
                m_bIsDownload = false;
                setProgressBar();
                Toast.makeText(getApplicationContext(),getString(R.string.last),
                               Toast.LENGTH_LONG).show();
                m_bIsDownload = true;
                return;
            }

            //empty
            if (msg.what == 2) {
                m_bIsDownload = false;
                setProgressBar();
                Toast.makeText(getApplicationContext(),getString(R.string.last),
                        Toast.LENGTH_LONG).show();
                m_bIsDownload = true;
            }
        }
    };

    private void downloadData() {
        m_bIsDownload = true;
        setProgressBar();
        SHttpDownload sHttpDownload = new SHttpDownload();
        //default location Taipei 101
        double dLon = DEF_LON;
        double dLat = DEF_LAT;

        //get my location here
        if (DataBase.getInstance().getLocation() != null) {
            dLon = DataBase.getInstance().getLocation().getLongitude();
            dLat = DataBase.getInstance().getLocation().getLatitude();
        }

        //httpGet的方法，解析UTF-8 string檔的結構
        sHttpDownload.append(AppData.SERVER_URL)
                     .append(AppData.APP_ID)
                     .append(APPID)
                     .append(AppData.DATA_GROUP_CODE)
                     .append(m_strGroupCode)
                     .append(AppData.INDEX)
                     .append(String.valueOf(m_iDownload))
                     .append(AppData.LIMIT)
                     .append(String.valueOf(LIMIT))
                     .append(AppData.OS).append(ANDROID)
                     .append(AppData.OS_VERSION)
                     .append(Build.VERSION.RELEASE)
                     .append(AppData.LAT)
                     .append(String.valueOf(dLat))
                     .append(AppData.LON)
                     .append(String.valueOf(dLon));

        new Thread(sHttpDownload).start();

        m_iDownload = m_iDownload+LIMIT;
        Utility.log("download" + m_iDownload);
    }

    private void getLocation() {
        LocationManager locationManager = SystemService.getLocationManager(this);

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                                   MINTIME,
                                                   MINDISTANCE,
                                                   this);

            return;
        }

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == true) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                                   MINTIME,
                                                   MINDISTANCE,
                                                   this);

            return;
        }

        downloadData();
    }

    private void setView() {
        m_pbLoading = (ProgressBar)findViewById(R.id.progressBar);
        m_pbLoading.setVisibility(View.INVISIBLE);
        ListView lvShop = (ListView)findViewById(R.id.listView);
        m_ListViewAdapter = new SAdapter();
        lvShop.setAdapter(m_ListViewAdapter);
        lvShop.setOnScrollListener(this);
        lvShop.setOnItemClickListener(this);
    }

    private void setProgressBar() {
        if (m_bIsDownload == true) {
            m_pbLoading.setVisibility(View.VISIBLE);
        } else {
            m_pbLoading.setVisibility(View.INVISIBLE);
        }
    }
}
