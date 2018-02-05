package com.zte.engineer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AlexWiFiTest extends Activity {


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                switch (intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN)) {
                    //Wifi被关闭。
                    case WifiManager.WIFI_STATE_DISABLED:
                        //禁用通过按钮。
                        passBtn.setEnabled(false);
                        break;
                    //Wifi被开启
                    case WifiManager.WIFI_STATE_ENABLED:
                        Toast.makeText(AlexWiFiTest.this, "WiFi已开启，正在扫描附近Wifi....", Toast.LENGTH_SHORT).show();
                        passBtn.setEnabled(true);
                        break;
                }

            }
        }
    };

    private Button passBtn, failBtn;


    private WifiManager mManager;
    private ListView wifi_list;

    private final Timer mTimer = new Timer();
    private TimerTask mTask;

    final Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                wifi_list.setAdapter(new myAdapter(mManager, mManager.getScanResults()));
                Log.i("Main", "handleMessage: ");

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alex_wi_fi_test);

        passBtn = (Button) findViewById(R.id.pass);
        failBtn =(Button) findViewById(R.id.fail);

        passBtn.setEnabled(false);

        passBtn.setOnClickListener(mOnClickListener);
        failBtn.setOnClickListener(mOnClickListener);

        wifi_list = (ListView) findViewById(R.id.list_wifi);
        mManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        if (mManager != null && !mManager.isWifiEnabled()) {
            mManager.setWifiEnabled(true);
            passBtn.setEnabled(true);
        }

        //set the intentFilter.
        IntentFilter filter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(mReceiver,filter);




        mTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                myHandler.sendMessage(message);
            }
        };
        mTimer.schedule(mTask, 0, 1000);

    }


    //extends the baseadapter to gain best control of the items.
    class myAdapter extends BaseAdapter {
        private WifiManager mWifiManager;
        List<ScanResult> mScanResults;


        myAdapter(WifiManager manager, List<ScanResult> results) {
            this.mScanResults = results;
            this.mWifiManager = manager;
        }

        @Override
        public int getCount() {
            return mScanResults.size();
        }

        @Override
        public Object getItem(int position) {
            return mScanResults.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ScanResult result = (ScanResult) getItem(position);
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.wifi_item, parent, false);
            TextView wifi_signal = (TextView) view.findViewById(R.id.wifi_signal);
            TextView wifi_name =(TextView) view.findViewById(R.id.wifi_name);
            int i = WifiManager.calculateSignalLevel(result.level, 100);
            wifi_signal.setText(String.valueOf(i));
            wifi_name.setText(result.SSID);
            return view;
        }
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        mTimer.cancel();
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        setResult(20);
        finish();
        super.onBackPressed();
    }
    /*点击事件监听*/
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pass:
                    setResult(10);
                    finish();
                    break;

                case R.id.fail:
                    setResult(20);
                    finish();
                    break;
            }
        }
    };
}
