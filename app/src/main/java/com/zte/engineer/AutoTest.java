package com.zte.engineer;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.content.ComponentName;
public class AutoTest extends Activity {
	
	private static final String TAG = "AutoLoopTest";
	/*add by zhoudawei for factory reset 20110729 start*/
	private int unusefulcode = 0;
	/*add by zhoudawei for factory reset 20110729 start*/	
	ArrayList<Intent> list = new ArrayList<Intent>();
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		initTestList();
		
		int index = 0;
		Intent intent = list.get(index);
		startActivityForResult(intent, index);
	}
	
	private void initTestList()
	{
		list.clear();
		//list.add(newIntent(this, BatteryLog.class));
		list.add(new Intent("android.media.action.IMAGE_CAPTURE")
				.setComponent(new ComponentName("com.android.gallery3d","com.android.camera.CameraLauncher"))
				.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
				.putExtra("android.intent.extras.CAMERA_FACING", 0));//back camera id is 0 if existing.
		
		list.add(newIntent(this, UsbMainActivity.class)); //zhangle add for movego
		list.add(newIntent(this, SIMTest.class)); //zhangle add for movego
		list.add(newIntent(this, TestPhoneActivity.class)); //zhangle add for movego	
		list.add(new Intent("android.media.action.IMAGE_CAPTURE")
				.setComponent(new ComponentName("com.android.gallery3d","com.android.camera.CameraLauncher"))
				.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
				.putExtra("android.intent.extras.CAMERA_FACING", 1));//front camera id is 1 if existing.
		//list.add(newIntent(Launcher.FM_TEST_PACKAGES, Launcher.FM_TEST_TARGET_CLASS));
		list.add(newIntent("com.mediatek.fmradio", "com.mediatek.fmradio.FmRadioEmActivity"));//zhangle add
		list.add(newIntent(this, TouchScreenTest.class));				
		//list.add(newIntent(this, SIMTest.class));		
		//list.add(newIntent(this,ReciverTest.class));
		list.add(newIntent(this, RingerTest.class));
		list.add(newIntent(this, LcdTestActivity.class));
		list.add(newIntent(this, VibratorTest.class));
		list.add(newIntent(this, BacklightTest.class));
		list.add(newIntent(this,AudioLoopTest.class));		
		list.add(newIntent(this,EarPhoneAudioLoopTest.class));
		list.add(newIntent(this, SDcardTest.class));
		list.add(newIntent(this, MemoryTest.class));
		list.add(newIntent(this, KeyTest.class));
		//list.add(newIntent(this, LcdOffTest.class));
		list.add(newIntent(this, BTAddressTest.class));

		list.add(newIntent(Launcher.BLUETOOTH_SETTINGS_PACKAGE, Launcher.BLUETOOTH_SETTINGS_CLASS));
		//zhangle add start for movego
		Intent gpsIntent = new Intent();
		gpsIntent.setClassName(Launcher.GPS_TEST_PACKAGE, 
						Launcher.GPS_TEST_TRAGET_CLASS); 
		list.add(gpsIntent);
		//zhangle add start for movego
		list.add(newIntent(this, WifiAddressTest.class));
		list.add(newIntent(Launcher.WIFI_SETTINGS_PACKAGE, Launcher.WIFI_SETTINGS_CLASS));

		list.add(newIntent(this, ImeiTest.class));
		//list.add(newIntent(this, ImsiTest.class));
		list.add(newIntent(this, SensorTest.class));
		//list.add(newIntent(Launcher.RADIO_INFO_PACKAGE, Launcher.RADIO_INFO_TARGET_CLASS));
		//list.add(newIntent(Launcher.CAMERA_PACKAGE,Launcher.CAMERA_TARGET_CLASS).putExtra("android.intent.extras.CAMERA_FACING", 1));
		//list.add(newIntent(Launcher.CAMERA_PACKAGE, Launcher.CAMERA_TARGET_CLASS).putExtra("android.intent.extras.CAMERA_FACING", 0));
      
		//list.add(newIntent(Launcher.GPS_TEST_PACKAGE, Launcher.GPS_TEST_TRAGET_CLASS));
        
		list.add(newIntent(this, FlashLightTest.class));
		
        list.add(newIntent(this, ReciverTest.class));		
		Intent factoryIntent = new Intent();
		factoryIntent.setClassName(Launcher.FACTORY_RESET_PACKAGE, Launcher.FACTORY_RESET_CLASS);
		factoryIntent.putExtra("do_factory_reset", "FactoryMode");
		list.add(factoryIntent);	
		
		Util.log(TAG, "list count:" + list.size());
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{				
		int index = requestCode + 1;
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
		
		Intent intent = list.get(index);
		startActivityForResult(intent, index);
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
