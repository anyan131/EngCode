package com.zte.engineer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.newmobi.iic.LEDHelper;
import com.newmobi.iic.TestSign;
/**
 * Created by Alextao on 2018/5/22,星期二.
 * Email : tao_xue@new-mobi.com
 */

public class LedTest extends Activity {

    private Button red, blue, green;
    private Button pass, fail;
    //byte[] SignData;

    MyClickListener clickListener = new MyClickListener();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.led_test);

        red = (Button) findViewById(R.id.red_led);
        red.setOnClickListener(clickListener);
        blue = (Button) findViewById(R.id.blue_led);
        blue.setOnClickListener(clickListener);
        green = (Button) findViewById(R.id.green_led);
        green.setOnClickListener(clickListener);
        pass = (Button) findViewById(R.id.led_pass);
        pass.setOnClickListener(clickListener);
        fail = (Button) findViewById(R.id.led_fail);
        fail.setOnClickListener(clickListener);

    }

    class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            final int flag;
            switch (v.getId()) {
                case R.id.red_led:
                    flag = 0;
                    FileUtils.writePath("255", "sys/class/leds/mt6360_pmu_led1/brightness");
                    break;
                case R.id.blue_led:
                    flag = 1;
                    FileUtils.writePath("255", "sys/class/leds/mt6360_pmu_led3/brightness");
                    break;
                case R.id.green_led:
                    flag = 2;
                    FileUtils.writePath("255", "sys/class/leds/mt6360_pmu_led2/brightness");
                    break;

                case R.id.led_pass:
                    setResult(10);
                    finish();
                    break;

                case R.id.led_fail:
                    setResult(20);
                    finish();
                    break;
            }

        }
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
        FileUtils.writePath("0", "sys/class/leds/mt6360_pmu_led1/brightness");
        FileUtils.writePath("0", "sys/class/leds/mt6360_pmu_led2/brightness");
        FileUtils.writePath("0", "sys/class/leds/mt6360_pmu_led3/brightness");
    }
}
