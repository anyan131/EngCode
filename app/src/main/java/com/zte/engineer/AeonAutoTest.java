package com.zte.engineer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.renderscript.Script;
import android.util.Log;
import android.widget.Toast;

import com.mediatek.fmradio.FmAlexTaoActivity;
import com.newmobi.iic.LEDHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AeonAutoTest extends Activity {

    private static final String TAG = "AutoLoopTest";
    /*add by zhoudawei for factory reset 20110729 start*/
    private int unusefulcode = 0;
    /*add by zhoudawei for factory reset 20110729 start*/
    ArrayList<Intent> list = new ArrayList<Intent>();

    //add hexs start
    private List<String> result = new ArrayList<String>();
    private List<Integer> reCode = new ArrayList<Integer>();
    private Resources res;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    //add hexs end

    private Map<String, Class> testItemMap = new HashMap<>();

    private static final int[] stringIds = {
            R.string.software_version,
            R.string.battery_info,
            R.string.gpio_test,
            R.string.lcd,
            R.string.backlight,
            R.string.touchpanel,
            R.string.camera_front,
            R.string.camera_back,
            R.string.key_test,
            R.string.vibrator,
            R.string.ringer,
            R.string.audio_loop,
            R.string.earphone_audio_loop,
            R.string.audio_receiver_new,
            R.string.SIM,
            R.string.imei,
            R.string.sd_info,
            R.string.bt_address,
            R.string.wifi_address,
            R.string.NM_fm_test,
            R.string.serial_port,
            R.string.NM_gps_test,
            R.string.NM_i2c_test,
            R.string.board_code,
            R.string.led_test,
    };

    /**
     * 对应每个测试项所进去的界面类。
     */
    public static final Class[] CLASSES = {
            ProduceInfoListView.class,
            BatteryLog.class,
            GPIO.class,
            LcdTestActivity.class,
            BacklightTest.class,
            TouchScreenTest.class,
            AlexFrontCamera.class,
            AlexBackCameraTest.class,
            KeyTest.class,
            VibratorTest.class,
            RingerTest.class,
            AudioLoopTest.class,
            EarPhoneAudioLoopTest.class,
            ReciverTest.class,
            SIMTest.class,
            ImeiTest.class,
            SDcardTest.class,
            BTAddressTest.class,
            AlexWiFiTest.class,
            FmAlexTaoActivity.class,
            SerialPort.class,
            AlexNewGPSTest.class,
            AlexIICTest.class,
            BoardCode.class,
            LedTest.class,
    };


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        //add hexs start
        res = getResources();
        //add hexs end

        //make sure our map is empty.
        testItemMap.clear();
        //fill up the HashMap with strings and classes.
        for (int i = 0; i < stringIds.length; i++) {
            testItemMap.put(res.getString(stringIds[i]), CLASSES[i]);
        }

        //then we need to initialize the prefs.
        prefs = getSharedPreferences("engineer", MODE_MULTI_PROCESS);
        editor = prefs.edit();

        initTestList();

        int index = 0;
        Intent intent = list.get(index);
        startActivityForResult(intent, index);
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    }

    private void initTestList() {
        list.clear();
        //
        result.clear();
//        list.add(newIntent(this, CalibrationTest.class));
//        result.add(res.getString(R.string.calibration_string));

        /*
        * Alextao modified here for sync with the single item test.
        * */

        list.add(newIntent(this, ProduceInfoListView.class));
        result.add(res.getString(R.string.produce_information));

        list.add(newIntent(this, BatteryLog.class));
        result.add(res.getString(R.string.battery_info));

        list.add(newIntent(this, GPIO.class));
        result.add(res.getString(R.string.gpio_test));

        list.add(newIntent(this, LcdTestActivity.class));
        result.add(res.getString(R.string.lcd));


        //list.add(newIntent(this, HallTest.class));
        list.add(newIntent(this, BacklightTest.class));
        result.add(res.getString(R.string.backlight));

        list.add(newIntent(this, TouchScreenTest.class));
        result.add(res.getString(R.string.touch_test));

        list.add(newIntent(this, AlexFrontCamera.class));
        result.add(res.getString(R.string.camera_front));

        list.add(newIntent(this, AlexBackCameraTest.class));
        result.add(res.getString(R.string.camera_back));

        list.add(newIntent(this, KeyTest.class));
        result.add(res.getString(R.string.key_test));

        list.add(newIntent(this, VibratorTest.class));
        result.add(res.getString(R.string.vibrator));

        list.add(newIntent(this, RingerTest.class));
        result.add(res.getString(R.string.ringer));

        list.add(newIntent(this, AudioLoopTest.class));
        result.add(res.getString(R.string.audio_loop));

        list.add(newIntent(this, EarPhoneAudioLoopTest.class));
        result.add(res.getString(R.string.earphone_audio_loop));

        list.add(newIntent(this, ReciverTest.class));
        result.add(res.getString(R.string.audio_receiver));

        list.add(newIntent(this, SIMTest.class));
        result.add(res.getString(R.string.SIM));


        list.add(newIntent(this, ImeiTest.class));
        result.add(res.getString(R.string.imei));

        list.add(newIntent(this, SDcardTest.class));
        result.add(res.getString(R.string.sd_test));

        list.add(newIntent(this, BTAddressTest.class));
        result.add(res.getString(R.string.bt_address));

        list.add(newIntent(this, AlexWiFiTest.class));
        result.add(res.getString(R.string.wifi_address));

        list.add(newIntent(this, FmAlexTaoActivity.class));
        result.add(res.getString(R.string.fm_test));

        list.add(newIntent(this, SerialPort.class));
        result.add(res.getString(R.string.serial_port));

        list.add(newIntent(this, AlexNewGPSTest.class));
        result.add(res.getString(R.string.gps));

        list.add(newIntent(this, AlexIICTest.class));
        result.add(res.getString(R.string.NM_i2c_test));

        list.add(newIntent(this, BoardCode.class));
        result.add(res.getString(R.string.board_code));
        list.add(newIntent(this,LedTest.class));
        result.add(res.getString(R.string.led_test));


        //  list.add(newIntent(this, ResultList.class));

//        Intent factoryIntent = new Intent();
//        factoryIntent.setClassName(Launcher.FACTORY_RESET_PACKAGE, Launcher.FACTORY_RESET_CLASS);
//        factoryIntent.putExtra("do_factory_reset", "FactoryMode");
//        list.add(factoryIntent);
        Util.log(TAG, "list count:" + list.size());

        Util.log(TAG, "hexs modify list count:" + list.size());
        if (result.size() > 0) {
            for (int i = 0; i < result.size(); i++) {
                reCode.add(10);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        /**
         * here we would like to write the test result into the file.
         * we need to tell the differences from the resultCode.
         * the resultCode is already defined in {@link EngineerCode}
         * */

        //if resultCode pass
        if (resultCode == 10){
            editor.putString(res.getString(stringIds[requestCode]),"PASS");
            editor.commit();
        }else if (resultCode == 20){
            editor.putString(res.getString(stringIds[requestCode]),"FAIL");
            editor.commit();
        }else{
            editor.putString(res.getString(stringIds[requestCode]),"PASS");
        }


        int index = requestCode + 1;
        //hexs start
        if (resultCode > 10 && requestCode < reCode.size()) {
            reCode.set(requestCode, 20);
        }
        //hexs end
        /*add by zhoudawei for factory reset 20110729 start*/
        unusefulcode++;
        if (index < unusefulcode) {
            return;
        }

		/*add by zhoudawei for factory reset 20110729 start*/

        if (index >= list.size()) {
//            finish();

            Intent i  = new Intent(this,TestReport.class);
            startActivity(i);
            finish();
            return;

        }
        if (index == list.size() - 2) {
            Intent intent = list.get(index);
            intent.putStringArrayListExtra("result", (ArrayList<String>) result);
            intent.putIntegerArrayListExtra("recode", (ArrayList<Integer>) reCode);
            startActivityForResult(intent, index);
        } else {
            Intent intent = list.get(index);
            startActivityForResult(intent, index);
        }
    }

    private Intent newIntent(Context packageContext, Class<?> cls) {
        return new Intent(packageContext, cls);
    }

    /**
     * FN0001234,Added by shihaijun
     *
     * @param packageName
     * @param className
     * @param cameraId
     * @return
     */
    private Intent newIntent(String packageName, String className, int cameraId) {
        Intent i = new Intent();
        i.setClassName(packageName, className);
        i.putExtra("android.intent.extras.CAMERA_FACING", cameraId);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        return i;
    }

    private Intent newIntent(String packageName, String className) {
        Intent i = new Intent();
        i.setClassName(packageName, className);
        return i;
    }

}
