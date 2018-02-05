package com.zte.engineer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zte.engineer.R;

public class UsbMainActivity extends ZteActivity {
    String BatteryStatus;
    String BatteryStatus2;
    String BatteryV;
    String BatteryT;
    String BatteryTemp;

    TextView tv_BatteryStatus;
    TextView tv_BatteryStatus2;
    TextView tv_BatteryV;
    TextView tv_BatteryT;
    TextView tv_BatteryTemp;

    // 当系统处于充电状态或者电量发生改变时会广播该action
    private static final String ACTION_NAME = Intent.ACTION_BATTERY_CHANGED;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usb_main);
        Toast.makeText(this, "点击按钮获取对应时刻的状态", 1).show();
        tv_BatteryStatus = (TextView) findViewById(R.id.BatteryStatus);
        tv_BatteryStatus2 = (TextView) findViewById(R.id.BatteryStatus2);
        tv_BatteryV = (TextView) findViewById(R.id.BatteryV);
        tv_BatteryT = (TextView) findViewById(R.id.BatteryT);
        tv_BatteryTemp = (TextView) findViewById(R.id.BatteryTemp);

        tv_BatteryT.setVisibility(View.GONE);
        tv_BatteryV.setVisibility(View.GONE);
        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(this);
        
        Button pass = (Button) findViewById(R.id.pass);
        pass.setOnClickListener(this);
        Button no = (Button) findViewById(R.id.no);
        no.setOnClickListener(this);
        
    }

    // 声明广播接收者
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        int intLevel = 0;
        int intScale = 0;

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            /*
             * 如果捕捉到的action是ACTION_BATTERY_CHANGED， 就运行onBatteryInfoReceiver()
             */
            if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
                intLevel = intent.getIntExtra("level", 0);
                intScale = intent.getIntExtra("scale", 100);

                // 电池伏数
                Log.d("Battery V", "" + intent.getIntExtra("voltage", 0));
                // 电池温度
                Log.d("Battery T", "" + intent.getIntExtra("temperature", 0));
                BatteryV = "当前电压为：" + intent.getIntExtra("voltage", 0);
                BatteryT = "当前温度为：" + intent.getIntExtra("temperature", 0);

                switch (intent.getIntExtra("status",
                        BatteryManager.BATTERY_STATUS_UNKNOWN)) {
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    BatteryStatus = "当前状态: 充电状态";
                    break;
                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                    BatteryStatus = "当前状态: 放电状态";
                    break;
                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                    BatteryStatus = "当前状态: 未充电";
                    break;
                case BatteryManager.BATTERY_STATUS_FULL:
                    BatteryStatus = "当前状态: 充满电";
                    break;
                case BatteryManager.BATTERY_STATUS_UNKNOWN:
                    BatteryStatus = "当前状态: 未知道状态";
                    break;
                }
                switch (intent.getIntExtra("plugged",
                        BatteryManager.BATTERY_PLUGGED_AC)) {
                case BatteryManager.BATTERY_PLUGGED_AC:
                    BatteryStatus2 = "当前充电方式: AC充电";
                    break;
                case BatteryManager.BATTERY_PLUGGED_USB:
                    BatteryStatus2 = "当前充电方式: USB充电";
                    break;
                default:
                    BatteryStatus2 = "当前充电方式: 未知道状态";
                    break;
                }
                
                switch (intent.getIntExtra("health",
                        BatteryManager.BATTERY_HEALTH_UNKNOWN)) {
                case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                    BatteryTemp = "电池状态: 未知错误";
                    break;
                case BatteryManager.BATTERY_HEALTH_GOOD:
                    BatteryTemp = "电池状态: 状态良好";
                    break;
                case BatteryManager.BATTERY_HEALTH_DEAD:
                    BatteryTemp = "电池状态: 电池没有电";
                    break;
                case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                    BatteryTemp = "电池状态: 电池电压过高";
                    break;
                case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                    BatteryTemp = "电池状态: 电池过热";
                    break;
                }

            }
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        case R.id.button1:
            IntentFilter filter = new IntentFilter(ACTION_NAME);
            registerReceiver(mBatInfoReceiver, filter);
            tv_BatteryStatus.setText(BatteryStatus);
            tv_BatteryStatus2.setText(BatteryStatus2);
            tv_BatteryV.setText(BatteryV);
            tv_BatteryT.setText(BatteryT);
            tv_BatteryTemp.setText(BatteryTemp);
            break;
        case R.id.pass:
        	finishSelf(RESULT_PASS);
			break;
        case R.id.no:
        	finishSelf(RESULT_FALSE);
			break;
        }

    }

}