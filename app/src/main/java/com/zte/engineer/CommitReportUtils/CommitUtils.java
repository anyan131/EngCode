package com.zte.engineer.CommitReportUtils;

import android.content.Context;
import android.content.SharedPreferences;

import com.newmobi.iic.TestSign;

import org.json.JSONException;
import org.json.JSONObject;

public class CommitUtils {
    /** 上传测试报告 */
    public static final String  SERVER_COMMIT_REPORT_URL = "http://new-mobi.com:1070/";
    private Context mContext;
    private SharedPreferences mSp;

    public CommitUtils(Context context) {
        this.mContext = context;
        mSp = mContext.getSharedPreferences("engineer", mContext.MODE_MULTI_PROCESS);
    }

    public CommitReportEntity getCommitReport( ) throws JSONException {
        CommitReportEntity commitReportEntity = new CommitReportEntity();
        JSONObject commitReportJson = new JSONObject();
        commitReportJson.put(Constants.IMEI_STRING, StringUtils.getDeviceIMEI(mContext));
        commitReportJson.put(Constants.PROJECT_NAME, StringUtils.getProjectname());
        commitReportJson.put(Constants.SAVE_TIME, StringUtils.getSystemTime());
        commitReportJson.put(Constants.VERSION, mSp.getString(Constants.VERSION,"*"));
        commitReportJson.put(Constants.BATTERY, mSp.getString(Constants.BATTERY,"*"));
        commitReportJson.put(Constants.GPIO, mSp.getString(Constants.GPIO,"*"));
        commitReportJson.put(Constants.LCM, mSp.getString(Constants.LCM,"*"));
        commitReportJson.put(Constants.BACK_LIGHT, mSp.getString(Constants.BACK_LIGHT,"*"));
        commitReportJson.put(Constants.TOUCH_PANEL, mSp.getString(Constants.TOUCH_PANEL,"*"));
        commitReportJson.put(Constants.FRONT_CAMERA, mSp.getString(Constants.FRONT_CAMERA,"*"));
        commitReportJson.put(Constants.BACK_CAMERA, mSp.getString(Constants.BACK_CAMERA,"*"));
        commitReportJson.put(Constants.KEYS, mSp.getString(Constants.KEYS,"*"));
        commitReportJson.put(Constants.VIBRATOR, mSp.getString(Constants.VIBRATOR,"*"));
        commitReportJson.put(Constants.RING, mSp.getString(Constants.RING,"no this item"));
        commitReportJson.put(Constants.AUDIO_LOOP, mSp.getString(Constants.AUDIO_LOOP,"*"));
        commitReportJson.put(Constants.EARPHONE_AUDIO_LOOP, mSp.getString(Constants.EARPHONE_AUDIO_LOOP,"*"));
        commitReportJson.put(Constants.AUDIO_RECEIVER, mSp.getString(Constants.AUDIO_RECEIVER,"*"));
        commitReportJson.put(Constants.SIM, mSp.getString(Constants.SIM,"*"));
        commitReportJson.put(Constants.IMEI, mSp.getString(Constants.IMEI,"*"));
        commitReportJson.put(Constants.SDCARD, mSp.getString(Constants.SDCARD,"*"));
        commitReportJson.put(Constants.BLUTOOTH, mSp.getString(Constants.BLUTOOTH,"*"));
        commitReportJson.put(Constants.WIFI, mSp.getString(Constants.WIFI,"*"));
        commitReportJson.put(Constants.FM, mSp.getString(Constants.FM,"*"));
        commitReportJson.put(Constants.UART, mSp.getString(Constants.UART,"*"));
        commitReportJson.put(Constants.GPS, mSp.getString(Constants.GPS,"*"));
        commitReportJson.put(Constants.I2C, mSp.getString(Constants.I2C,"*"));
        commitReportJson.put(Constants.LED, mSp.getString(Constants.LED,"*"));
        commitReportJson.put(Constants.BOARD_CODE, mSp.getString(Constants.BOARD_CODE,"*"));

        HttpClients client = new HttpClients();
        HttpResponseResult result = client.sendRequestGetResponse(SERVER_COMMIT_REPORT_URL, commitReportJson.toString());
        String json = result.getResponseResult();
        if (json != null && json.equals(Constants.COMMIT_OK)){
            System.out.println("---lzg json="+json);
            commitReportEntity.setSuccess(true);
            commitReportEntity.setData(json);
        }else{
            commitReportEntity.setSuccess(false);
            commitReportEntity.setData("");
        }
        return commitReportEntity;
    }

    public void writeReportToDeviceStorege(){
        byte[] signData = null;
        String signString = null;
        signString = Constants.VERSION + mSp.getString(Constants.VERSION,"*") + ";" +
                Constants.BATTERY + mSp.getString(Constants.BATTERY,"*") + ";" +
                Constants.GPIO + mSp.getString(Constants.GPIO,"*") + ";" +
                Constants.LCM + mSp.getString(Constants.LCM,"*") + ";" +
                Constants.BACK_LIGHT + mSp.getString(Constants.BACK_LIGHT,"*") + ";" +
                Constants.TOUCH_PANEL + mSp.getString(Constants.TOUCH_PANEL,"*") + ";" +
                Constants.FRONT_CAMERA + mSp.getString(Constants.FRONT_CAMERA,"*") + ";" +
                Constants.BACK_CAMERA + mSp.getString(Constants.BACK_CAMERA,"*") + ";" +
                Constants.KEYS + mSp.getString(Constants.KEYS,"*") + ";" +
                Constants.VIBRATOR + mSp.getString(Constants.VIBRATOR,"*") + ";" +
                Constants.RING + mSp.getString(Constants.RING,"*") + ";" +
                Constants.AUDIO_LOOP + mSp.getString(Constants.AUDIO_LOOP,"*") + ";" +
                Constants.EARPHONE_AUDIO_LOOP + mSp.getString(Constants.EARPHONE_AUDIO_LOOP,"*") + ";" +
                Constants.AUDIO_RECEIVER + mSp.getString(Constants.AUDIO_RECEIVER,"*") + ";" +
                Constants.SIM + mSp.getString(Constants.SIM,"*") + ";" +
                Constants.IMEI + mSp.getString(Constants.IMEI,"*") + ";" +
                Constants.SDCARD + mSp.getString(Constants.SDCARD,"*") + ";" +
                Constants.BLUTOOTH + mSp.getString(Constants.BLUTOOTH,"*") + ";" +
                Constants.WIFI + mSp.getString(Constants.WIFI,"*") + ";" +
                Constants.FM + mSp.getString(Constants.FM,"*") + ";" +
                Constants.UART + mSp.getString(Constants.UART,"*") + ";" +
                Constants.GPS + mSp.getString(Constants.GPS,"*") + ";" +
                Constants.I2C + mSp.getString(Constants.I2C,"*") + ";" +
                Constants.LED + mSp.getString(Constants.LED,"*") + ";" +
                Constants.BOARD_CODE + mSp.getString(Constants.BOARD_CODE,"*") + ";" ;
        signData = signString.getBytes();
        TestSign.JNISignInit();
        TestSign.JNISignflagAllClear();
        TestSign.JNISignAllWrite(signData);
    }
}
