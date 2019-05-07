package com.zte.engineer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.renderscript.Script;
import android.util.Log;
import android.widget.Toast;

import com.mediatek.fmradio.FmAlexTaoActivity;
import com.newmobi.iic.LEDHelper;
import com.newmobi.iic.TestSign;
import com.zte.engineer.CommitReportUtils.CommitUtils;
import com.zte.engineer.CommitReportUtils.StringUtils;

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
    //add by lzg
    private WifiManager mManager;
    private LocationManager mLocation;
    private Context mContext;
    final int testCount = stringIds.length;
    //end lzg
    //add hexs end

    private Map<String, Class> testItemMap = new HashMap<>();

    private static final int[] stringIds = {
            R.string.software_version,
            R.string.backlight,
            R.string.camera_front,
            R.string.camera_back,
            R.string.key_test,
            R.string.vibrator,
            R.string.ringer,
            R.string.audio_loop,
            R.string.audio_receiver_new,
            R.string.SIM,
            R.string.imei,
            R.string.sd_info,
            R.string.bt_address,
            R.string.wifi_address,
            //R.string.g_sensor,
            R.string.gyroscope_sensor,
            //R.string.m_sensor,
            //R.string.p_sensor,
            //R.string.ethernet,
            //R.string.serial_port,
            R.string.NM_gps_test,
            //R.string.NM_i2c_test,
            R.string.earphone_audio_loop,
            //R.string.led_test,
            R.string.board_code,
            R.string.gpio_test,
    };

    /**
     * 对应每个测试项所进去的界面类。
     */
    public static final Class[] CLASSES = {
            ProduceInfoListView.class,
            GPIO.class,
            BacklightTest.class,
            AlexFrontCamera.class,
            AlexBackCameraTest.class,
            KeyTest.class,
            VibratorTest.class,
            RingerTest.class,
            AudioLoopTest.class,
            ReciverTest.class,
            SIMTest.class,
            ImeiTest.class,
            SDcardTest.class,
            BTAddressTest.class,
            AlexWiFiTest.class,
            //GSensorTest.class,
            GyroscopeSensorTest.class,
            //MSensorTest.class,
            //PSensorTest.class,
            //EthernetTestActivity.class,
            //SerialPort.class,
            GPSTestActivity.class,
            AlexIICTest.class,
            EarPhoneAudioLoopTest.class,
            LedTest.class,
            BoardCode.class,
    };


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        //add hexs start
        res = getResources();
        //add hexs end
        //add by lzg
        mContext = this;
        mManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        mLocation = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //end lzg
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
        //add by lzg
        isFirst();
        enableWifiAndGps();
        //end lzg
        int index = 0;
        Intent intent = list.get(index);
        startActivityForResult(intent, index);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //add by lzg
        disableWifiAndGps();
        //end lzg
    }

    private void isFirst() {
        SharedPreferences share = getSharedPreferences("first", MODE_PRIVATE);
        SharedPreferences.Editor edt = share.edit();
        boolean isFirst = share.getBoolean("isFirst", true);
        if (isFirst) {
            Resources re = getResources();
            Toast.makeText(AeonAutoTest.this, "First", Toast.LENGTH_SHORT).show();
            //modify by lzg
            CommitUtils cu = new CommitUtils(mContext);
            boolean isReadSuccess = cu.readStorageToReport();
            if(!isReadSuccess){
                for (int i = 0; i < testCount; i++) {
                    System.out.println("----lzg isFirst");
                    editor.putString(re.getString(stringIds[i]), "NOT_TEST");
                    //add by lzg
                    editor.putString(StringUtils.getTestItemName(i),"*");
                    TestSign.JNISignflagAllClear();
                    //end lzg
                    editor.commit();
                }
            }
            //end lzg
            edt.putBoolean("isFirst", false);
            edt.commit();//TODO: maybe we should use apply()?? for the thread safe?
        }
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
        result.add(res.getString(R.string.software_version));

        //list.add(newIntent(this, BatteryLog.class));
        //result.add(res.getString(R.string.battery_info));

        //list.add(newIntent(this, LcdTestActivity.class));
        //result.add(res.getString(R.string.lcd));


        //list.add(newIntent(this, HallTest.class));
        list.add(newIntent(this, BacklightTest.class));
        result.add(res.getString(R.string.backlight));

        //list.add(newIntent(this, TouchScreenTest.class));
        //result.add(res.getString(R.string.touch_test));

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

        list.add(newIntent(this, ReciverTest.class));
        result.add(res.getString(R.string.audio_receiver_new));

        list.add(newIntent(this, SIMTest.class));
        result.add(res.getString(R.string.SIM));


        list.add(newIntent(this, ImeiTest.class));
        result.add(res.getString(R.string.imei));

        list.add(newIntent(this, SDcardTest.class));
        result.add(res.getString(R.string.sd_info));

        list.add(newIntent(this, BTAddressTest.class));
        result.add(res.getString(R.string.bt_address));

        list.add(newIntent(this, AlexWiFiTest.class));
        result.add(res.getString(R.string.wifi_address));

        //list.add(newIntent(this, FmAlexTaoActivity.class));
        //result.add(res.getString(R.string.fm_test));

        //list.add(newIntent(this, GSensorTest.class));
        //result.add(res.getString(R.string.g_sensor));

        list.add(newIntent(this, GyroscopeSensorTest.class));
        result.add(res.getString(R.string.gyroscope_sensor));

        //list.add(newIntent(this, MSensorTest.class));
        //result.add(res.getString(R.string.m_sensor));

        //list.add(newIntent(this, PSensorTest.class));
        //result.add(res.getString(R.string.p_sensor));

        //list.add(newIntent(this, EthernetTestActivity.class));
        //result.add(res.getString(R.string.ethernet));

        //list.add(newIntent(this, SerialPort.class));
        //result.add(res.getString(R.string.serial_port));

        list.add(newIntent(this, GPSTestActivity.class));
        result.add(res.getString(R.string.NM_gps_test));

        //list.add(newIntent(this, AlexIICTest.class));
        //result.add(res.getString(R.string.NM_i2c_test));

        list.add(newIntent(this, EarPhoneAudioLoopTest.class));
        result.add(res.getString(R.string.earphone_audio_loop));

        //list.add(newIntent(this,LedTest.class));
        //result.add(res.getString(R.string.led_test));

        list.add(newIntent(this, BoardCode.class));
        result.add(res.getString(R.string.board_code));

        list.add(newIntent(this, GPIO.class));
        result.add(res.getString(R.string.gpio_test));

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
            //add by lzg
            editor.putString(StringUtils.getTestItemName(requestCode),"1");
            //end lzg
            editor.commit();
        }else if (resultCode == 20){
            editor.putString(res.getString(stringIds[requestCode]),"FAIL");
            //add by lzg
            editor.putString(StringUtils.getTestItemName(requestCode),"0");
            //end lzg
            editor.commit();
        }else{
            editor.putString(res.getString(stringIds[requestCode]),"FAIL");
            //add by lzg
            editor.putString(StringUtils.getTestItemName(requestCode),"0");
            editor.commit();
            //end lzg
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

            Intent i  = new Intent(this,EngineerCode.class);
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
    //add by lzg
    private void enableWifiAndGps(){
        if (mManager != null && !mManager.isWifiEnabled()) {
            mManager.setWifiEnabled(true);
        }
        boolean flag = mLocation.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!flag){
            Settings.Secure.setLocationProviderEnabled(getContentResolver(),
                    LocationManager.GPS_PROVIDER, true);
        }
    }

    private void disableWifiAndGps(){
        if (mManager != null && mManager.isWifiEnabled()) {
            mManager.setWifiEnabled(false);
        }
        boolean flag = mLocation.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(flag){
            Settings.Secure.putInt(getContentResolver(), Settings.Secure.LOCATION_MODE,
                    android.provider.Settings.Secure.LOCATION_MODE_OFF);
        }
    }
    //end lzg
}
