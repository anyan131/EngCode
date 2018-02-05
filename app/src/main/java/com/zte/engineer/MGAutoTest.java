package com.zte.engineer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

public class MGAutoTest extends Activity {
	
	private static final String TAG = "MGAutoTest";
	/*add by zhoudawei for factory reset 20110729 start*/
	private int unusefulcode = 0;
	/*add by zhoudawei for factory reset 20110729 start*/	
	ArrayList<Intent> list = new ArrayList<Intent>();
	private List<String> result =  new ArrayList<String>();
	private List<Integer> reCode =  new ArrayList<Integer>();
	private Resources res;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		res = getResources();
		initTestList();
		
		int index = 0;
		Intent intent = list.get(index);
		startActivityForResult(intent, index);
	}
	private void initTestList()
	{
		list.clear();
		result.clear();
		//list.add(newIntent(this, BatteryLog.class));
	
		//zhangle add start
		list.add(newIntent(this, UsbMainActivity.class)); //zhangle add for movego
		result.add(res.getString(R.string.power));
		
		list.add(newIntent(this, SIMTest.class)); //zhangle add for movego
		result.add(res.getString(R.string.SIM));
		
		list.add(newIntent(this, TestPhoneActivity.class)); //zhangle add for movego	
		result.add(res.getString(R.string.phone_test));
		
		list.add(newIntent(Launcher.GPS_TEST_PACKAGE, Launcher.GPS_TEST_TRAGET_CLASS));
		result.add(res.getString(R.string.gps));
		//zhangle add end
		list.add(newIntent(this, TouchScreenTest.class));
		result.add(res.getString(R.string.touchpanel));	
		
		//list.add(newIntent(this, SIMTest.class));		
		//list.add(newIntent(this,ReciverTest.class));
		list.add(newIntent(this, RingerTest.class));
		result.add(res.getString(R.string.ringer));
		
		list.add(newIntent(this, LcdTestActivity.class));
		result.add(res.getString(R.string.lcd));
		
		list.add(newIntent(this, VibratorTest.class));
		result.add(res.getString(R.string.vibrator));
		
		list.add(newIntent(this, BacklightTest.class));
		result.add(res.getString(R.string.vibrator));
		
		list.add(newIntent(this,AudioLoopTest.class));
		result.add(res.getString(R.string.audio_loop));
		
		list.add(newIntent(this,EarPhoneAudioLoopTest.class));
		result.add(res.getString(R.string.earphone_audio_loop));
		
		list.add(newIntent(this, SDcardTest.class));
		result.add(res.getString(R.string.sd_info));
		
		list.add(newIntent(this, MemoryTest.class));
		result.add(res.getString(R.string.memory));
		
		list.add(newIntent(this, KeyTest.class));
		result.add(res.getString(R.string.key_test));
		
		//list.add(newIntent(this, LcdOffTest.class));
		list.add(newIntent(this, BTAddressTest.class));
		result.add(res.getString(R.string.Bluetooth_address));
		
		list.add(newIntent(Launcher.BLUETOOTH_SETTINGS_PACKAGE, Launcher.BLUETOOTH_SETTINGS_CLASS));	
		list.add(newIntent(this, BlutoothTest.class));
		result.add(res.getString(R.string.bt_test));
		
		list.add(newIntent(this, WifiAddressTest.class));
		result.add(res.getString(R.string.wifi_address));
		
		list.add(newIntent(Launcher.WIFI_SETTINGS_PACKAGE, Launcher.WIFI_SETTINGS_CLASS));
		list.add(newIntent(this, WifiTest.class));
		result.add(res.getString(R.string.wifi_test));
        
		list.add(newIntent(this, ImeiTest.class));
		//list.add(newIntent(this, ImsiTest.class));
		result.add(res.getString(R.string.imei));
		
		list.add(newIntent(this, SensorTest.class));
		result.add(res.getString(R.string.sensor));
		
		//list.add(newIntent(Launcher.RADIO_INFO_PACKAGE, Launcher.RADIO_INFO_TARGET_CLASS));
		list.add(newIntent(Launcher.FM_TEST_PACKAGES, Launcher.FM_TEST_TARGET_CLASS));
		list.add(newIntent(this, FMTest.class));
		result.add(res.getString(R.string.fm_test));
		
		
		//list.add(newIntent(Launcher.CAMERA_PACKAGE,Launcher.CAMERA_TARGET_CLASS).putExtra("android.intent.extras.CAMERA_FACING", 1));
		//list.add(newIntent(Launcher.CAMERA_PACKAGE, Launcher.CAMERA_TARGET_CLASS).putExtra("android.intent.extras.CAMERA_FACING", 0));
       list.add(new Intent("android.media.action.IMAGE_CAPTURE")
				.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
				.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK));//back camera id is 0 if existing.

		list.add(new Intent("android.media.action.IMAGE_CAPTURE")
				.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
				.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT));//back camera id is 1 if existing.
		//list.add(newIntent(Launcher.GPS_TEST_PACKAGE, Launcher.GPS_TEST_TRAGET_CLASS));	
		list.add(newIntent(this, CameraTest.class));
		result.add(res.getString(R.string.camera_test));
        
		list.add(newIntent(this, ResultList.class));
		
		Intent factoryIntent = new Intent();
		factoryIntent.setClassName(Launcher.FACTORY_RESET_PACKAGE, Launcher.FACTORY_RESET_CLASS);
		factoryIntent.putExtra("do_factory_reset", "FactoryMode");
		list.add(factoryIntent);			
		Util.log(TAG, "list count:" + list.size());
		if (result.size()>0) {
			for (int i = 0; i < result.size(); i++) {
				reCode.add(10);
			}
		}
		
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{				
		int index = requestCode + 1;
		if (resultCode>10&&requestCode<reCode.size()) {
			reCode.set(requestCode, 20);
		}
		/*add by zhoudawei for factory reset 20110729 start*/
		unusefulcode ++;		
		if(index < unusefulcode)
		{					
			return;
		}

		/*add by zhoudawei for factory reset 20110729 start*/	
		
		if (index >= list.size())
		{
			finish();
			return;
		}
		if (index == list.size()-2) {
			Intent intent = list.get(index);
			intent.putStringArrayListExtra("result", (ArrayList<String>) result);
			intent.putIntegerArrayListExtra("recode", (ArrayList<Integer>) reCode);
			startActivityForResult(intent, index);
		}else {
			Intent intent = list.get(index);
			startActivityForResult(intent, index);
		}
		
	}

	private Intent newIntent(Context packageContext, Class<?> cls)
	{
		return new Intent(packageContext, cls);
	}
	
	/**
	 * FN0001234,Added by shihaijun
	 * @param packageName
	 * @param className
	 * @param cameraId
	 * @return
	 */
	private Intent newIntent(String packageName, String className,int cameraId)
	{
		Intent i = new Intent();
		i.setClassName(packageName, className);
		i.putExtra("android.intent.extras.CAMERA_FACING", cameraId);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		return i;
	}
	
	private Intent newIntent(String packageName, String className)
	{
		Intent i = new Intent();
		i.setClassName(packageName, className);
		return i;
	}
	
}
