package com.softmobile.hkscloud;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.softmobile.mialibrary.activity.StartIntent;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private long m_lExitTime      = 0;//關於按兩下離開

    static String DATA_GROUP_CODE = "dataGroupCode";//辨別intent需要的bundle的id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        Bundle bundle = new Bundle();

        switch (v.getId()){
            case R.id.btnNear:
                bundle.putString(DATA_GROUP_CODE,"01");
                break;
            case R.id.btnCredit:
                bundle.putString(DATA_GROUP_CODE,"07");
                break;
        }
        StartIntent.openActivity(this,ShopListActivity.class,bundle);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        /*
        按兩下離開
         */
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //record to exit when next press
            if ((System.currentTimeMillis()-m_lExitTime)>2000) {
                Toast.makeText(MainActivity.this, R.string.ask, Toast.LENGTH_SHORT).show();
                m_lExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
        }
        return false;
    }

    private void setView() {
        Button btnNear = (Button) findViewById(R.id.btnNear);
        btnNear.setOnClickListener(this);
        Button btnCredit = (Button) findViewById(R.id.btnCredit);
        btnCredit.setOnClickListener(this);
    }
}
