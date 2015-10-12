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

    static String DATA_GROUP_CODE = "dataGroupCode";//辨別intent需要的bundle的id
    private static int WAIT_TIME  = 2000;
    private static String ONE     = "01";
    private static String SEVEN   = "07";

    private long m_lExitTime      = 0;//關於按兩下離開

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
                bundle.putString(DATA_GROUP_CODE,ONE);
                break;
            case R.id.btnCredit:
                bundle.putString(DATA_GROUP_CODE,SEVEN);
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
            if ((System.currentTimeMillis()-m_lExitTime)>WAIT_TIME) {
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
