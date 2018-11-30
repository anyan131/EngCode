package com.zte.engineer.CommitReportUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemProperties;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.PhoneConstants;
import com.mediatek.telephony.TelephonyManagerEx;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public abstract class StringUtils {

	/**
	 * 是否为空
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(String value) {
		if (value == null || value.length() == 0) {
			return true;
		}
		return false;
	}

	public static boolean isNotBlank(String text) {
		return text != null && !"".equalsIgnoreCase(text.trim());
	}

	public static boolean isBlank(String text) {
		return !isNotBlank(text);
	}

	public static String getTestItemName(int requestCode){
		String testItemName = "";
		if(requestCode==0){
			testItemName = Constants.VERSION;
		}else if(requestCode==1){
			testItemName = Constants.BATTERY;
		}else if(requestCode==2){
			testItemName = Constants.GPIO;
		}else if(requestCode==3){
			testItemName = Constants.LCM;
		}else if(requestCode==4){
			testItemName = Constants.BACK_LIGHT;
		}else if(requestCode==5){
			testItemName = Constants.TOUCH_PANEL;
		}else if(requestCode==6){
			testItemName = Constants.FRONT_CAMERA;
		}else if(requestCode==7){
			testItemName = Constants.BACK_CAMERA;
		}else if(requestCode==8){
			testItemName = Constants.KEYS;
		}else if(requestCode==9){
			testItemName = Constants.VIBRATOR;
		}else if(requestCode==10){
			testItemName = Constants.RING;
		}else if(requestCode==11){
			testItemName = Constants.AUDIO_LOOP;
		}else if(requestCode==12){
			testItemName = Constants.EARPHONE_AUDIO_LOOP;
		}else if(requestCode==13){
			testItemName = Constants.AUDIO_RECEIVER;
		}else if(requestCode==14){
			testItemName = Constants.SIM;
		}else if(requestCode==15){
			testItemName = Constants.IMEI;
		}else if(requestCode==16){
			testItemName = Constants.SDCARD;
		}else if(requestCode==17){
			testItemName = Constants.BLUTOOTH;
		}else if(requestCode==18){
			testItemName = Constants.WIFI;
		}else if(requestCode==19){
			testItemName = Constants.FM;
		}else if(requestCode==20){
			testItemName = Constants.UART;
		}else if(requestCode==21){
			testItemName = Constants.GPS;
		}else if(requestCode==22){
			testItemName = Constants.I2C;
		}else if(requestCode==23){
			testItemName = Constants.BOARD_CODE;
		}else if(requestCode==24){
			testItemName = Constants.LED;
		}
		return testItemName;
	}

	public static String getProjectname(){
		String displayID = SystemProperties.get("ro.build.display.id");
		int index = displayID.indexOf('.');
		//index = displayID.indexOf('.',index+1);
		String projectName = displayID.substring(0,index );
		return projectName;
	}

	public static String getDeviceIMEI(Context context){
		String mIMEI = "";
		String mIMEI1 = "";
		String mIMEI2 = "";
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		mIMEI = telephonyManager.getDeviceId();
		if(mIMEI != null && !mIMEI.equals("")){
			return  mIMEI;
		}else{
			mIMEI1 = TelephonyManagerEx.getDefault().getDeviceId(PhoneConstants.SIM_ID_1);
			mIMEI2 = TelephonyManagerEx.getDefault().getDeviceId(PhoneConstants.SIM_ID_2);
			if(mIMEI1 != null && !mIMEI1.equals("")){
				return  mIMEI1;
			}else if(mIMEI2 != null && !mIMEI2.equals("")){
				return  mIMEI2;
			}else{
				return "";
			}
		}


	}

	public static String getSystemTime(){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		return simpleDateFormat.format(date);
	}

	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	//5.0以上系统用这个接口
	public static void openMobileNetwork(Context context){
		TelephonyManager teleManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		Class[] getArgArray = null;
		Class[] setArgArray = new Class[] {boolean.class};
		Object[] getArgInvoke = null;
		try {
			Method mGetMethod = teleManager.getClass().getMethod("getDataEnabled", getArgArray);
			Method mSetMethod = teleManager.getClass().getMethod("setDataEnabled", setArgArray);
			boolean isOpen = (Boolean) mGetMethod.invoke(teleManager, getArgInvoke);
			if (!isOpen) {
				mSetMethod.invoke(teleManager, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//5.0以下系统用这个接口
	public static void openMobileNetworkOther(Context context){
		ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		Class[] getArgArray = null;
		Class[] setArgArray = new Class[] {boolean.class};
		Object[] getArgInvoke = null;

		try {
			Method mGetMethod = conManager.getClass().getMethod("getMobileDataEnabled", getArgArray);
			Method mSetMethod = conManager.getClass().getMethod("setMobileDataEnabled", setArgArray);
			boolean isOpen = (Boolean) mGetMethod.invoke(conManager, getArgInvoke);
			if (!isOpen) {
				mSetMethod.invoke(conManager, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
