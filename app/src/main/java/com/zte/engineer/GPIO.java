package com.zte.engineer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by alextao on 2018/1/8.
 *
 * @author alextao
 * 2018/01/08
 */

public class GPIO extends Activity implements View.OnClickListener {

    private Button pass, fail;
    private static final String TAG = "NewMobi";


    //	 private static final int GPIO_INDEX[] = new int[]{10,96,57,3,120,19,97,58,4,122,80,86,0,5,44,
//	 79,59,1,6,43,78,60,2,7,42};
//	private static final int GPIO_INDEX[] = new int[]{1,2,3,28,27,25,26,17,19,18,20,21,22,23,24/*,85,86,87,88*/};
    private static final int GPIO_INDEX[] = new int[]{1, 2, 3, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 155, 156, 157, 158};
    private Button light;
    private Button ser;
    private Button locationGpio;
    private Button walkGpio;
    private Button nearGpio;


    private boolean light_status = false;
    private boolean ser_status = false;
    private boolean location_status = false;
    private Button near1Gpio;
    private Button near2Gpio;
    private Button near3Gpio;
    private Button near4Gpio;
    private Button near5Gpio;
    private Button near6Gpio;

    private TextView logCat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alex_tao_gpio);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initWidgets();


    }

    /**
     * this is for init the widgets.
     */
    private void initWidgets() {
        logCat = findViewById(R.id.logCat);
        light = findViewById(R.id.light);
        light.setOnClickListener(this);
        ser = findViewById(R.id.ser);
        ser.setOnClickListener(this);
        locationGpio = findViewById(R.id.location_gpio);
        locationGpio.setOnClickListener(this);
        walkGpio = findViewById(R.id.walk_gpio);
        walkGpio.setOnClickListener(this);

        near1Gpio = findViewById(R.id.near1_gpio);
        near1Gpio.setOnClickListener(this);
        near2Gpio = findViewById(R.id.near2_gpio);
        near2Gpio.setOnClickListener(this);
        near3Gpio = findViewById(R.id.near3_gpio);
        near3Gpio.setOnClickListener(this);
        near4Gpio = findViewById(R.id.near4_gpio);
        near4Gpio.setOnClickListener(this);
        near5Gpio = findViewById(R.id.near5_gpio);
        near5Gpio.setOnClickListener(this);
        near6Gpio = findViewById(R.id.near6_gpio);
        near6Gpio.setOnClickListener(this);

        pass = findViewById(R.id.gpio_pass);
        pass.setOnClickListener(this);
        fail = findViewById(R.id.gpio_fail);
        fail.setOnClickListener(this);
        Log.i(TAG, GPIO_INDEX.length + "");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gpio_pass:
                setResult(10);
                finish();
                break;
            case R.id.gpio_fail:
                setResult(20);
                finish();
                break;
            case R.id.light:
                light_status = !light_status;

                if (light_status) {
                    FileUtils.writePath("1", "/sys/devices/platform/flashlights_mt6360/flashlight_port");
                } else {
                    FileUtils.writePath("0", "/sys/devices/platform/flashlights_mt6360/flashlight_port");
                }
                break;
            case R.id.ser:
                ser_status = !ser_status;

                if (ser_status) {
                    FileUtils.writePath("out 174 1", "sys/devices/platform/pinctrl/mt_gpio");
                } else {
                    FileUtils.writePath("out 174 0", "sys/devices/platform/pinctrl/mt_gpio");
                }

                break;
            case R.id.location_gpio:
                location_status = !location_status;

                if (location_status) {
                    FileUtils.writePath("out 183 1", "sys/devices/platform/pinctrl/mt_gpio");
                } else {
                    FileUtils.writePath("out 183 0", "sys/devices/platform/pinctrl/mt_gpio");
                }


                break;
            case R.id.near1_gpio:
                FileUtils.writePath("mode 187 0", "sys/devices/platform/pinctrl/mt_gpio");
                FileUtils.writePath("mode 177 0", "sys/devices/platform/pinctrl/mt_gpio");
                FileUtils.writePath("mode 180 0", "sys/devices/platform/pinctrl/mt_gpio");
                break;
            case R.id.near2_gpio:
                FileUtils.writePath("mode 187 0", "sys/devices/platform/pinctrl/mt_gpio");
                FileUtils.writePath("mode 177 1", "sys/devices/platform/pinctrl/mt_gpio");
                FileUtils.writePath("mode 180 0", "sys/devices/platform/pinctrl/mt_gpio");
                break;
            case R.id.near3_gpio:
                FileUtils.writePath("mode 187 1", "sys/devices/platform/pinctrl/mt_gpio");
                FileUtils.writePath("mode 177 1", "sys/devices/platform/pinctrl/mt_gpio");
                FileUtils.writePath("mode 180 0", "sys/devices/platform/pinctrl/mt_gpio");
                break;
            case R.id.near4_gpio:
                FileUtils.writePath("mode 187 0", "sys/devices/platform/pinctrl/mt_gpio");
                FileUtils.writePath("mode 177 0", "sys/devices/platform/pinctrl/mt_gpio");
                FileUtils.writePath("mode 180 1", "sys/devices/platform/pinctrl/mt_gpio");
                break;
            case R.id.near5_gpio:
                FileUtils.writePath("mode 187 1", "sys/devices/platform/pinctrl/mt_gpio");
                FileUtils.writePath("mode 177 0", "sys/devices/platform/pinctrl/mt_gpio");
                FileUtils.writePath("mode 180 1", "sys/devices/platform/pinctrl/mt_gpio");
                break;
            case R.id.near6_gpio:
                FileUtils.writePath("mode 187 1", "sys/devices/platform/pinctrl/mt_gpio");
                FileUtils.writePath("mode 177 1", "sys/devices/platform/pinctrl/mt_gpio");
                FileUtils.writePath("mode 180 1", "sys/devices/platform/pinctrl/mt_gpio");
                break;
        }
    }


    StringBuilder sb = new StringBuilder();

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_M) {
            Log.e(TAG, "onKeyUp: M");
            sb.append("M");

            logCat.setText(sb);
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        setResult(20);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
