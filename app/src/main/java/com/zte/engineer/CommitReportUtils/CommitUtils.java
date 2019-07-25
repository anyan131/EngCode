package com.zte.engineer.CommitReportUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.telecom.Response;
import android.util.Log;

import com.newmobi.iic.TestSign;
import com.zte.engineer.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class CommitUtils {
    /** 上传测试报告 */
    public static final String  SERVER_COMMIT_REPORT_URL = "http://new-mobi.com:1070/";
    private Context mContext;
    private SharedPreferences mSp;
    private SharedPreferences.Editor mEditor;
    private String TAG = "CommitUtils";
    public CommitUtils(Context context) {
        this.mContext = context;
        mSp = mContext.getSharedPreferences("engineer", Context.MODE_MULTI_PROCESS);
        mEditor = mSp.edit();
    }

    public CommitReportEntity getCommitReport( ) throws JSONException {
        final CommitReportEntity commitReportEntity = new CommitReportEntity();
        JSONObject commitReportJson = new JSONObject();
        commitReportJson.put(Constants.IMEI_STRING, StringUtils.getDeviceIMEI(mContext));
        commitReportJson.put(Constants.PROJECT_NAME, StringUtils.getProjectname());
        commitReportJson.put(Constants.SAVE_TIME, StringUtils.getSystemTime());
        commitReportJson.put(Constants.VERSION, mSp.getString(Constants.VERSION,"#"));
        commitReportJson.put(Constants.BATTERY, mSp.getString(Constants.BATTERY,"#"));
        commitReportJson.put(Constants.GPIO, mSp.getString(Constants.GPIO,"#"));
        commitReportJson.put(Constants.LCM, mSp.getString(Constants.LCM,"#"));
        commitReportJson.put(Constants.BACK_LIGHT, mSp.getString(Constants.BACK_LIGHT,"#"));
        commitReportJson.put(Constants.TOUCH_PANEL, mSp.getString(Constants.TOUCH_PANEL,"#"));
        commitReportJson.put(Constants.FRONT_CAMERA, mSp.getString(Constants.FRONT_CAMERA,"#"));
        commitReportJson.put(Constants.BACK_CAMERA, mSp.getString(Constants.BACK_CAMERA,"#"));
        commitReportJson.put(Constants.KEYS, mSp.getString(Constants.KEYS,"#"));
        commitReportJson.put(Constants.VIBRATOR, mSp.getString(Constants.VIBRATOR,"#"));
        commitReportJson.put(Constants.RING, mSp.getString(Constants.RING,"#"));
        commitReportJson.put(Constants.AUDIO_LOOP, mSp.getString(Constants.AUDIO_LOOP,"#"));
        commitReportJson.put(Constants.EARPHONE_AUDIO_LOOP, mSp.getString(Constants.EARPHONE_AUDIO_LOOP,"#"));
        commitReportJson.put(Constants.AUDIO_RECEIVER, mSp.getString(Constants.AUDIO_RECEIVER,"#"));
        commitReportJson.put(Constants.SIM, mSp.getString(Constants.SIM,"#"));
        commitReportJson.put(Constants.IMEI, mSp.getString(Constants.IMEI,"#"));
        commitReportJson.put(Constants.SDCARD, mSp.getString(Constants.SDCARD,"#"));
        commitReportJson.put(Constants.BLUTOOTH, mSp.getString(Constants.BLUTOOTH,"#"));
        commitReportJson.put(Constants.WIFI, mSp.getString(Constants.WIFI,"#"));
        commitReportJson.put(Constants.FM, mSp.getString(Constants.FM,"#"));
        commitReportJson.put(Constants.UART, mSp.getString(Constants.UART,"#"));
        commitReportJson.put(Constants.GPS, mSp.getString(Constants.GPS,"#"));
        commitReportJson.put(Constants.I2C, mSp.getString(Constants.I2C,"#"));
        commitReportJson.put(Constants.LED, mSp.getString(Constants.LED,"#"));
        commitReportJson.put(Constants.BOARD_CODE, mSp.getString(Constants.BOARD_CODE,"#"));
        commitReportJson.put(Constants.G_SENSOR, mSp.getString(Constants.G_SENSOR,"#"));
        commitReportJson.put(Constants.GYROSCOPE_SENSOR, mSp.getString(Constants.GYROSCOPE_SENSOR,"#"));
        commitReportJson.put(Constants.M_SENSOR, mSp.getString(Constants.M_SENSOR,"#"));
        commitReportJson.put(Constants.L_SENSOR, mSp.getString(Constants.L_SENSOR,"#"));
        commitReportJson.put(Constants.P_SENSOR, mSp.getString(Constants.P_SENSOR,"#"));
        commitReportJson.put(Constants.USB_CAMERA, mSp.getString(Constants.USB_CAMERA,"#"));
        commitReportJson.put(Constants.ETHERNET, mSp.getString(Constants.ETHERNET,"#"));
        commitReportJson.put(Constants.SN, mSp.getString(Constants.SN,"#"));
        HttpClients client = new HttpClients();
        Log.i(TAG,"param: "+commitReportJson.toString());
        HttpResponseResult result = client.sendRequestGetResponse(SERVER_COMMIT_REPORT_URL, commitReportJson.toString());
        String json = result.getResponseResult();
        if (json != null && json.equals(Constants.COMMIT_OK)){
            Log.i(TAG,"commit success: "+json);
            commitReportEntity.setSuccess(true);
            commitReportEntity.setData(json);
        }else{
            Log.i(TAG,"commit fail");
            commitReportEntity.setSuccess(false);
            commitReportEntity.setData("");
        }

//        OkHttpClient okHttpClient  = new OkHttpClient.Builder()
//                .connectTimeout(10, TimeUnit.SECONDS)
//                .writeTimeout(10,TimeUnit.SECONDS)
//                .readTimeout(20, TimeUnit.SECONDS)
//                .build();
//
//        final String json =commitReportJson.toString();
//        //MediaType  设置Content-Type 标头中包含的媒体类型值
//        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
//                , json);
//        Log.i(TAG,"param: "+json);
//        Request request = new Request.Builder()
//                .url(SERVER_COMMIT_REPORT_URL)//请求的url
//                .post(requestBody)
//                .build();
//
//        //创建/Call
//        Call call = okHttpClient.newCall(request);
//        //加入队列 异步操作
//        call.enqueue(new Callback() {
//
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.i(TAG,"提交失败");
//                commitReportEntity.setSuccess(false);
//                commitReportEntity.setData("");
//            }
//            @Override
//            public void onResponse(Call call, okhttp3.Response response) throws IOException {
//                int reponseCode=response.code();
//                Log.i(TAG,"提交成功: "+reponseCode);
//                commitReportEntity.setSuccess(true);
//                commitReportEntity.setData("OK");
//            }
//        });
        return commitReportEntity;
    }

    public void writeReportToDeviceStorege(){
        byte[] signData = null;
        String signString = null;
        signString = Constants.VERSION + mSp.getString(Constants.VERSION,"#") + ";" +
                Constants.BATTERY + mSp.getString(Constants.BATTERY,"#") + ";" +
                Constants.GPIO + mSp.getString(Constants.GPIO,"#") + ";" +
                Constants.LCM + mSp.getString(Constants.LCM,"#") + ";" +
                Constants.BACK_LIGHT + mSp.getString(Constants.BACK_LIGHT,"#") + ";" +
                Constants.TOUCH_PANEL + mSp.getString(Constants.TOUCH_PANEL,"#") + ";" +
                Constants.FRONT_CAMERA + mSp.getString(Constants.FRONT_CAMERA,"#") + ";" +
                Constants.BACK_CAMERA + mSp.getString(Constants.BACK_CAMERA,"#") + ";" +
                Constants.KEYS + mSp.getString(Constants.KEYS,"#") + ";" +
                Constants.VIBRATOR + mSp.getString(Constants.VIBRATOR,"#") + ";" +
                Constants.RING + mSp.getString(Constants.RING,"#") + ";" +
                Constants.AUDIO_LOOP + mSp.getString(Constants.AUDIO_LOOP,"#") + ";" +
                Constants.EARPHONE_AUDIO_LOOP + mSp.getString(Constants.EARPHONE_AUDIO_LOOP,"#") + ";" +
                Constants.AUDIO_RECEIVER + mSp.getString(Constants.AUDIO_RECEIVER,"#") + ";" +
                Constants.SIM + mSp.getString(Constants.SIM,"#") + ";" +
                Constants.IMEI + mSp.getString(Constants.IMEI,"#") + ";" +
                Constants.SDCARD + mSp.getString(Constants.SDCARD,"#") + ";" +
                Constants.BLUTOOTH + mSp.getString(Constants.BLUTOOTH,"#") + ";" +
                Constants.WIFI + mSp.getString(Constants.WIFI,"#") + ";" +
                Constants.FM + mSp.getString(Constants.FM,"#") + ";" +
                Constants.UART + mSp.getString(Constants.UART,"#") + ";" +
                Constants.GPS + mSp.getString(Constants.GPS,"#") + ";" +
                Constants.I2C + mSp.getString(Constants.I2C,"#") + ";" +
                Constants.LED + mSp.getString(Constants.LED,"#") + ";" +
                Constants.BOARD_CODE + mSp.getString(Constants.BOARD_CODE,"#") + ";" +
                Constants.G_SENSOR + mSp.getString(Constants.G_SENSOR,"#") + ";" +
                Constants.GYROSCOPE_SENSOR + mSp.getString(Constants.GYROSCOPE_SENSOR,"#") + ";" +
                Constants.M_SENSOR + mSp.getString(Constants.M_SENSOR,"#") + ";" +
                Constants.L_SENSOR + mSp.getString(Constants.L_SENSOR,"#") + ";" +
                Constants.P_SENSOR + mSp.getString(Constants.P_SENSOR,"#") + ";" +
                Constants.USB_CAMERA + mSp.getString(Constants.USB_CAMERA,"#") + ";" +
                Constants.ETHERNET + mSp.getString(Constants.ETHERNET,"#") + ";" +
                Constants.SN + mSp.getString(Constants.SN,"#") + ";";

        signData = signString.getBytes();
        //TestSign.JNISignInit();
        TestSign.JNISignflagAllClear();
        TestSign.JNISignAllWrite(signData);
    }

    public boolean readStorageToReport(){
        byte[] signData = null;
        String signString = null;
        signData = TestSign.JNISignAllRead();
        if(signData == null){
            return false;
        }
        signString = new String(signData);
        if(signString != null){
            if(!signString.contains(";")){
                return false;
            }
        }
        String itemArray[] = signString.split(";");
        if (itemArray == null || itemArray.length < 1 ) {
            return false;
        }
        System.out.println("---lzg itemArray="+itemArray.length);
        Resources re = mContext.getResources();
        for(int i = 0; i < itemArray.length; i++){
            String itemString = itemArray[i];
            String subItem = itemString.substring(0,itemString.length()-1);
            System.out.println("---lzg subItem="+subItem);
            String itemResult = itemString.substring(itemString.length()-1);
            System.out.println("---lzg itemResult="+itemResult);
            if(subItem != null && !subItem.equals("")){
                if(subItem.equals(Constants.VERSION)){
                    if(itemResult != null){
                        if(itemResult.equals("1")) {
                            mEditor.putString(re.getString(R.string.software_version), "PASS");
                        }else if(itemResult.equals("0")){
                            mEditor.putString(re.getString(R.string.software_version), "FAIL");
                        }else if(itemResult.equals("*")){
                            mEditor.putString(re.getString(R.string.software_version), "NOT_TEST");
                        }else{
                            mEditor.putString(re.getString(R.string.software_version), itemResult);
                        }
                        mEditor.putString(Constants.VERSION,itemResult);
                    }
                }else if(subItem.equals(Constants.BATTERY)){
                    if(itemResult != null){
                        if(itemResult.equals("1")) {
                            mEditor.putString(re.getString(R.string.battery_info), "PASS");
                        }else if(itemResult.equals("0")){
                            mEditor.putString(re.getString(R.string.battery_info), "FAIL");
                        }else if(itemResult.equals("*")){
                            mEditor.putString(re.getString(R.string.battery_info), "NOT_TEST");
                        }else{
                            mEditor.putString(re.getString(R.string.battery_info), itemResult);
                        }
                        mEditor.putString(Constants.BATTERY,itemResult);
                    }
                }else if(subItem.equals(Constants.GPIO)){
                    if(itemResult != null){
                        if(itemResult.equals("1")) {
                            mEditor.putString(re.getString(R.string.gpio_test), "PASS");
                        }else if(itemResult.equals("0")){
                            mEditor.putString(re.getString(R.string.gpio_test), "FAIL");
                        }else if(itemResult.equals("*")){
                            mEditor.putString(re.getString(R.string.gpio_test), "NOT_TEST");
                        }else{
                            mEditor.putString(re.getString(R.string.gpio_test), itemResult);
                        }
                        mEditor.putString(Constants.GPIO,itemResult);
                    }
                }else if(subItem.equals(Constants.LCM)){
                    if(itemResult != null){
                        if(itemResult.equals("1")) {
                            mEditor.putString(re.getString(R.string.lcd), "PASS");
                        }else if(itemResult.equals("0")){
                            mEditor.putString(re.getString(R.string.lcd), "FAIL");
                        }else if(itemResult.equals("*")){
                            mEditor.putString(re.getString(R.string.lcd), "NOT_TEST");
                        }else{
                            mEditor.putString(re.getString(R.string.lcd), itemResult);
                        }
                        mEditor.putString(Constants.LCM,itemResult);
                    }
                }else if(subItem.equals(Constants.BACK_LIGHT)){
                    if(itemResult != null){
                        if(itemResult.equals("1")) {
                            mEditor.putString(re.getString(R.string.backlight), "PASS");
                        }else if(itemResult.equals("0")){
                            mEditor.putString(re.getString(R.string.backlight), "FAIL");
                        }else if(itemResult.equals("*")){
                            mEditor.putString(re.getString(R.string.backlight), "NOT_TEST");
                        }else{
                            mEditor.putString(re.getString(R.string.backlight), itemResult);
                        }
                        mEditor.putString(Constants.BACK_LIGHT,itemResult);
                    }
                }else if(subItem.equals(Constants.TOUCH_PANEL)){
                    if(itemResult != null){
                        if(itemResult.equals("1")) {
                            mEditor.putString(re.getString(R.string.touchpanel), "PASS");
                        }else if(itemResult.equals("0")){
                            mEditor.putString(re.getString(R.string.touchpanel), "FAIL");
                        }else if(itemResult.equals("*")){
                            mEditor.putString(re.getString(R.string.touchpanel), "NOT_TEST");
                        }else{
                            mEditor.putString(re.getString(R.string.touchpanel), itemResult);
                        }
                        mEditor.putString(Constants.TOUCH_PANEL,itemResult);
                    }
                }else if(subItem.equals(Constants.FRONT_CAMERA)){
                    if(itemResult != null){
                        if(itemResult.equals("1")) {
                            mEditor.putString(re.getString(R.string.camera_front), "PASS");
                        }else if(itemResult.equals("0")){
                            mEditor.putString(re.getString(R.string.camera_front), "FAIL");
                        }else if(itemResult.equals("*")){
                            mEditor.putString(re.getString(R.string.camera_front), "NOT_TEST");
                        }else{
                            mEditor.putString(re.getString(R.string.camera_front), itemResult);
                        }
                        mEditor.putString(Constants.FRONT_CAMERA,itemResult);
                    }
                }else if(subItem.equals(Constants.BACK_CAMERA)){
                    if(itemResult != null){
                        if(itemResult.equals("1")) {
                            mEditor.putString(re.getString(R.string.camera_back), "PASS");
                        }else if(itemResult.equals("0")){
                            mEditor.putString(re.getString(R.string.camera_back), "FAIL");
                        }else if(itemResult.equals("*")){
                            mEditor.putString(re.getString(R.string.camera_back), "NOT_TEST");
                        }else{
                            mEditor.putString(re.getString(R.string.camera_back), itemResult);
                        }
                        mEditor.putString(Constants.BACK_CAMERA,itemResult);
                    }
                }else if(subItem.equals(Constants.KEYS)){
                    if(itemResult != null){
                        if(itemResult.equals("1")) {
                            mEditor.putString(re.getString(R.string.key_test), "PASS");
                        }else if(itemResult.equals("0")){
                            mEditor.putString(re.getString(R.string.key_test), "FAIL");
                        }else if(itemResult.equals("*")){
                            mEditor.putString(re.getString(R.string.key_test), "NOT_TEST");
                        }else{
                            mEditor.putString(re.getString(R.string.key_test), itemResult);
                        }
                        mEditor.putString(Constants.KEYS,itemResult);
                    }
                }else if(subItem.equals(Constants.VIBRATOR)){
                    if(itemResult != null){
                        if(itemResult.equals("1")) {
                            mEditor.putString(re.getString(R.string.vibrator), "PASS");
                        }else if(itemResult.equals("0")){
                            mEditor.putString(re.getString(R.string.vibrator), "FAIL");
                        }else if(itemResult.equals("*")){
                            mEditor.putString(re.getString(R.string.vibrator), "NOT_TEST");
                        }else{
                            mEditor.putString(re.getString(R.string.vibrator), itemResult);
                        }
                        mEditor.putString(Constants.VIBRATOR,itemResult);
                    }
                }else if(subItem.equals(Constants.RING)){
                    if(itemResult != null){
                        if(itemResult.equals("1")) {
                            mEditor.putString(re.getString(R.string.ringer), "PASS");
                        }else if(itemResult.equals("0")){
                            mEditor.putString(re.getString(R.string.ringer), "FAIL");
                        }else if(itemResult.equals("*")){
                            mEditor.putString(re.getString(R.string.ringer), "NOT_TEST");
                        }else{
                            mEditor.putString(re.getString(R.string.ringer), itemResult);
                        }
                        mEditor.putString(Constants.RING,itemResult);
                    }
                }else if(subItem.equals(Constants.AUDIO_LOOP)){
                    if(itemResult != null){
                        if(itemResult.equals("1")) {
                            mEditor.putString(re.getString(R.string.audio_loop), "PASS");
                        }else if(itemResult.equals("0")){
                            mEditor.putString(re.getString(R.string.audio_loop), "FAIL");
                        }else if(itemResult.equals("*")){
                            mEditor.putString(re.getString(R.string.audio_loop), "NOT_TEST");
                        }else{
                            mEditor.putString(re.getString(R.string.audio_loop), itemResult);
                        }
                        mEditor.putString(Constants.AUDIO_LOOP,itemResult);
                    }
                }else if(subItem.equals(Constants.EARPHONE_AUDIO_LOOP)){
                    if(itemResult != null){
                        if(itemResult.equals("1")) {
                            mEditor.putString(re.getString(R.string.earphone_audio_loop), "PASS");
                        }else if(itemResult.equals("0")){
                            mEditor.putString(re.getString(R.string.earphone_audio_loop), "FAIL");
                        }else if(itemResult.equals("*")){
                            mEditor.putString(re.getString(R.string.earphone_audio_loop), "NOT_TEST");
                        }else{
                            mEditor.putString(re.getString(R.string.earphone_audio_loop), itemResult);
                        }
                        mEditor.putString(Constants.EARPHONE_AUDIO_LOOP,itemResult);
                    }
                }else if(subItem.equals(Constants.AUDIO_RECEIVER)){
                    if(itemResult != null){
                        if(itemResult.equals("1")) {
                            mEditor.putString(re.getString(R.string.audio_receiver_new), "PASS");
                        }else if(itemResult.equals("0")){
                            mEditor.putString(re.getString(R.string.audio_receiver_new), "FAIL");
                        }else if(itemResult.equals("*")){
                            mEditor.putString(re.getString(R.string.audio_receiver_new), "NOT_TEST");
                        }else{
                            mEditor.putString(re.getString(R.string.audio_receiver_new), itemResult);
                        }
                        mEditor.putString(Constants.AUDIO_RECEIVER,itemResult);
                    }
                }else if(subItem.equals(Constants.SIM)){
                    if(itemResult != null){
                        if(itemResult.equals("1")) {
                            mEditor.putString(re.getString(R.string.SIM), "PASS");
                        }else if(itemResult.equals("0")){
                            mEditor.putString(re.getString(R.string.SIM), "FAIL");
                        }else if(itemResult.equals("*")){
                            mEditor.putString(re.getString(R.string.SIM), "NOT_TEST");
                        }else{
                            mEditor.putString(re.getString(R.string.SIM), itemResult);
                        }
                        mEditor.putString(Constants.SIM,itemResult);
                    }
                }else if(subItem.equals(Constants.IMEI)){
                    if(itemResult != null){
                        if(itemResult.equals("1")) {
                            mEditor.putString(re.getString(R.string.imei), "PASS");
                        }else if(itemResult.equals("0")){
                            mEditor.putString(re.getString(R.string.imei), "FAIL");
                        }else if(itemResult.equals("*")){
                            mEditor.putString(re.getString(R.string.imei), "NOT_TEST");
                        }else{
                            mEditor.putString(re.getString(R.string.imei), itemResult);
                        }
                        mEditor.putString(Constants.IMEI,itemResult);
                    }
                }else if(subItem.equals(Constants.SDCARD)){
                    if(itemResult != null){
                        if(itemResult.equals("1")) {
                            mEditor.putString(re.getString(R.string.sd_info), "PASS");
                        }else if(itemResult.equals("0")){
                            mEditor.putString(re.getString(R.string.sd_info), "FAIL");
                        }else if(itemResult.equals("*")){
                            mEditor.putString(re.getString(R.string.sd_info), "NOT_TEST");
                        }else{
                            mEditor.putString(re.getString(R.string.sd_info), itemResult);
                        }
                        mEditor.putString(Constants.SDCARD,itemResult);
                    }
                }else if(subItem.equals(Constants.BLUTOOTH)){
                    if(itemResult != null){
                        if(itemResult.equals("1")) {
                            mEditor.putString(re.getString(R.string.bt_address), "PASS");
                        }else if(itemResult.equals("0")){
                            mEditor.putString(re.getString(R.string.bt_address), "FAIL");
                        }else if(itemResult.equals("*")){
                            mEditor.putString(re.getString(R.string.bt_address), "NOT_TEST");
                        }else{
                            mEditor.putString(re.getString(R.string.bt_address), itemResult);
                        }
                        mEditor.putString(Constants.BLUTOOTH,itemResult);
                    }
                }else if(subItem.equals(Constants.WIFI)){
                    if(itemResult != null){
                        if(itemResult.equals("1")) {
                            mEditor.putString(re.getString(R.string.wifi_address), "PASS");
                        }else if(itemResult.equals("0")){
                            mEditor.putString(re.getString(R.string.wifi_address), "FAIL");
                        }else if(itemResult.equals("*")){
                            mEditor.putString(re.getString(R.string.wifi_address), "NOT_TEST");
                        }else{
                            mEditor.putString(re.getString(R.string.wifi_address), itemResult);
                        }
                        mEditor.putString(Constants.WIFI,itemResult);
                    }
                }else if(subItem.equals(Constants.FM)){
                    if(itemResult != null){
                        if(itemResult.equals("1")) {
                            mEditor.putString(re.getString(R.string.NM_fm_test), "PASS");
                        }else if(itemResult.equals("0")){
                            mEditor.putString(re.getString(R.string.NM_fm_test), "FAIL");
                        }else if(itemResult.equals("*")){
                            mEditor.putString(re.getString(R.string.NM_fm_test), "NOT_TEST");
                        }else{
                            mEditor.putString(re.getString(R.string.NM_fm_test), itemResult);
                        }
                        mEditor.putString(Constants.FM,itemResult);
                    }
                }else if(subItem.equals(Constants.UART)){
                    if(itemResult != null){
                        if(itemResult.equals("1")) {
                            mEditor.putString(re.getString(R.string.serial_port), "PASS");
                        }else if(itemResult.equals("0")){
                            mEditor.putString(re.getString(R.string.serial_port), "FAIL");
                        }else if(itemResult.equals("*")){
                            mEditor.putString(re.getString(R.string.serial_port), "NOT_TEST");
                        }else{
                            mEditor.putString(re.getString(R.string.serial_port), itemResult);
                        }
                        mEditor.putString(Constants.UART,itemResult);
                    }
                }else if(subItem.equals(Constants.GPS)){
                    if(itemResult != null){
                        if(itemResult.equals("1")) {
                            mEditor.putString(re.getString(R.string.NM_gps_test), "PASS");
                        }else if(itemResult.equals("0")){
                            mEditor.putString(re.getString(R.string.NM_gps_test), "FAIL");
                        }else if(itemResult.equals("*")){
                            mEditor.putString(re.getString(R.string.NM_gps_test), "NOT_TEST");
                        }else{
                            mEditor.putString(re.getString(R.string.NM_gps_test), itemResult);
                        }
                        mEditor.putString(Constants.GPS,itemResult);
                    }
                }else if(subItem.equals(Constants.I2C)){
                    if(itemResult != null){
                        if(itemResult.equals("1")) {
                            mEditor.putString(re.getString(R.string.NM_i2c_test), "PASS");
                        }else if(itemResult.equals("0")){
                            mEditor.putString(re.getString(R.string.NM_i2c_test), "FAIL");
                        }else if(itemResult.equals("*")){
                            mEditor.putString(re.getString(R.string.NM_i2c_test), "NOT_TEST");
                        }else{
                            mEditor.putString(re.getString(R.string.NM_i2c_test), itemResult);
                        }
                        mEditor.putString(Constants.I2C,itemResult);
                    }
                }else if(subItem.equals(Constants.LED)){
                    if(itemResult != null){
                        if(itemResult.equals("1")) {
                            mEditor.putString(re.getString(R.string.led_test), "PASS");
                        }else if(itemResult.equals("0")){
                            mEditor.putString(re.getString(R.string.led_test), "FAIL");
                        }else if(itemResult.equals("*")){
                            mEditor.putString(re.getString(R.string.led_test), "NOT_TEST");
                        }else{
                            mEditor.putString(re.getString(R.string.led_test), itemResult);
                        }
                        mEditor.putString(Constants.LED,itemResult);
                    }
                }else if(subItem.equals(Constants.BOARD_CODE)){
                    if(itemResult != null){
                        if(itemResult.equals("1")) {
                            mEditor.putString(re.getString(R.string.board_code), "PASS");
                        }else if(itemResult.equals("0")){
                            mEditor.putString(re.getString(R.string.board_code), "FAIL");
                        }else if(itemResult.equals("*")){
                            mEditor.putString(re.getString(R.string.board_code), "NOT_TEST");
                        }else{
                            mEditor.putString(re.getString(R.string.board_code), itemResult);
                        }
                        mEditor.putString(Constants.BOARD_CODE, itemResult);
                    }
                } else if (subItem.equals(Constants.G_SENSOR)) {
                    if (itemResult != null) {
                        if (itemResult.equals("1")) {
                            mEditor.putString(re.getString(R.string.g_sensor), "PASS");
                        } else if (itemResult.equals("0")) {
                            mEditor.putString(re.getString(R.string.g_sensor), "FAIL");
                        } else if (itemResult.equals("*")) {
                            mEditor.putString(re.getString(R.string.g_sensor), "NOT_TEST");
                        } else {
                            mEditor.putString(re.getString(R.string.g_sensor), itemResult);
                        }
                        mEditor.putString(Constants.G_SENSOR, itemResult);
                    }
                } else if (subItem.equals(Constants.GYROSCOPE_SENSOR)) {
                    if (itemResult != null) {
                        if (itemResult.equals("1")) {
                            mEditor.putString(re.getString(R.string.gyroscope_sensor), "PASS");
                        } else if (itemResult.equals("0")) {
                            mEditor.putString(re.getString(R.string.gyroscope_sensor), "FAIL");
                        } else if (itemResult.equals("*")) {
                            mEditor.putString(re.getString(R.string.gyroscope_sensor), "NOT_TEST");
                        } else {
                            mEditor.putString(re.getString(R.string.gyroscope_sensor), itemResult);
                        }
                        mEditor.putString(Constants.GYROSCOPE_SENSOR, itemResult);
                    }

                } else if (subItem.equals(Constants.M_SENSOR)) {
                    if (itemResult != null) {
                        if (itemResult.equals("1")) {
                            mEditor.putString(re.getString(R.string.m_sensor), "PASS");
                        } else if (itemResult.equals("0")) {
                            mEditor.putString(re.getString(R.string.m_sensor), "FAIL");
                        } else if (itemResult.equals("*")) {
                            mEditor.putString(re.getString(R.string.m_sensor), "NOT_TEST");
                        } else {
                            mEditor.putString(re.getString(R.string.m_sensor), itemResult);
                        }
                        mEditor.putString(Constants.M_SENSOR, itemResult);
                    }
                } else if (subItem.equals(Constants.L_SENSOR)) {
                    if (itemResult != null) {
                        if (itemResult.equals("1")) {
                            mEditor.putString(re.getString(R.string.l_sensor), "PASS");
                        } else if (itemResult.equals("0")) {
                            mEditor.putString(re.getString(R.string.l_sensor), "FAIL");
                        } else if (itemResult.equals("*")) {
                            mEditor.putString(re.getString(R.string.l_sensor), "NOT_TEST");
                        } else {
                            mEditor.putString(re.getString(R.string.l_sensor), itemResult);
                        }
                        mEditor.putString(Constants.L_SENSOR, itemResult);
                    }
                } else if (subItem.equals(Constants.P_SENSOR)) {
                    if (itemResult != null) {
                        if (itemResult.equals("1")) {
                            mEditor.putString(re.getString(R.string.p_sensor), "PASS");
                        } else if (itemResult.equals("0")) {
                            mEditor.putString(re.getString(R.string.p_sensor), "FAIL");
                        } else if (itemResult.equals("*")) {
                            mEditor.putString(re.getString(R.string.p_sensor), "NOT_TEST");
                        } else {
                            mEditor.putString(re.getString(R.string.p_sensor), itemResult);
                        }
                        mEditor.putString(Constants.P_SENSOR, itemResult);
                    }
                }else if (subItem.equals(Constants.USB_CAMERA)) {
                    if (itemResult != null) {
                        if (itemResult.equals("1")) {
                            mEditor.putString(re.getString(R.string.usb_camera), "PASS");
                        } else if (itemResult.equals("0")) {
                            mEditor.putString(re.getString(R.string.usb_camera), "FAIL");
                        } else if (itemResult.equals("*")) {
                            mEditor.putString(re.getString(R.string.usb_camera), "NOT_TEST");
                        } else {
                            mEditor.putString(re.getString(R.string.usb_camera), itemResult);
                        }
                        mEditor.putString(Constants.USB_CAMERA, itemResult);
                    }
                }else if (subItem.equals(Constants.ETHERNET)) {
                    if (itemResult != null) {
                        if (itemResult.equals("1")) {
                            mEditor.putString(re.getString(R.string.ethernet), "PASS");
                        } else if (itemResult.equals("0")) {
                            mEditor.putString(re.getString(R.string.ethernet), "FAIL");
                        } else if (itemResult.equals("*")) {
                            mEditor.putString(re.getString(R.string.ethernet), "NOT_TEST");
                        } else {
                            mEditor.putString(re.getString(R.string.ethernet), itemResult);
                        }
                        mEditor.putString(Constants.ETHERNET, itemResult);
                    }
                }else if (subItem.equals(Constants.SN)) {
                    if (itemResult != null) {
                        if (itemResult.equals("1")) {
                            mEditor.putString(re.getString(R.string.serial_number), "PASS");
                        } else if (itemResult.equals("0")) {
                            mEditor.putString(re.getString(R.string.serial_number), "FAIL");
                        } else if (itemResult.equals("*")) {
                            mEditor.putString(re.getString(R.string.serial_number), "NOT_TEST");
                        } else {
                            mEditor.putString(re.getString(R.string.serial_number), itemResult);
                        }
                        mEditor.putString(Constants.SN, itemResult);
                    }
                }
                mEditor.commit();
            }
        }
        return  true;
    }
}
