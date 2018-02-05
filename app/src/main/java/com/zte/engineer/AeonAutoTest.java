package com.zte.engineer;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.location.LocationManager;
import android.location.LocationListener;
import android.location.Location;
import android.provider.Settings;
import android.content.ContentResolver;
import android.content.ComponentName;
import java.util.List;
import android.content.res.Resources;
public class AeonAutoTest extends Activity {
	
	private static final String TAG = "AutoLoopTest";
	/*add by zhoudawei for factory reset 20110729 start*/
	private int unusefulcode = 0;
	/*add by zhoudawei for factory reset 20110729 start*/	
	ArrayList<Intent> list = new ArrayList<Intent>();
	
	//add hexs start
	private List<String> result =  new ArrayList<String>();
    private List<Integer> reCode =  new ArrayList<Integer>();
	private Resources res;
	//add hexs end
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		//add hexs start
		res = getResources();
		//add hexs end
		//leanda
		final ContentResolver resolver = this.getContentResolver();
		Settings.Secure.setLocationProviderEnabled(resolver, LocationManager.GPS_PROVIDER, true);
		LocationListener listener =new MyLocationListener();
		Intent it = new Intent("com.mediatek.ygps.YGPSService").setPackage("com.mediatek.ygps");
		this.startService(it);
		try {
		LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (mLocationManager != null) {
			mLocationManager.requestLocationUpdates("gps", 0, 0,
					listener);
		}
		}catch(Exception e){
		}
		//leanda end
		initTestList();
		
		int index = 0;
		Intent intent = list.get(index);
		startActivityForResult(intent, index);
	}

	    private class MyLocationListener implements LocationListener{

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
		
	private void initTestList()
	{
		list.clear();
		//
		result.clear();
		list.add(newIntent(this, CalibrationTest.class));	
		result.add(res.getString(R.string.calibration_string));
		
		list.add(newIntent(this, VersionTest.class));
		result.add(res.getString(R.string.version_number));
		list.add(newIntent(this, SIMTest.class));
		result.add(res.getString(R.string.SIM));
		
		list.add(newIntent(this, TouchScreenTest.class));
		result.add(res.getString(R.string.touchscreen));	
		//list.add(newIntent(this, HallTest.class));
		list.add(newIntent(this, GSensorTest.class));
		result.add(res.getString(R.string.g_sensor));
		list.add(newIntent(this, PSensorTest.class));
		result.add(res.getString(R.string.p_sensor));
		//list.add(newIntent(this, SensorTest.class));
		//list.add(newIntent(this, SIMTest.class));		
		list.add(newIntent(this,ReciverTest.class));
		result.add(res.getString(R.string.audio_receiver));
		
		list.add(newIntent(this, RingerTest.class));
			result.add(res.getString(R.string.ringer));
		//list.add(newIntent(this, LcdTestActivity.class));
		list.add(newIntent(this, VibratorTest.class));
		result.add(res.getString(R.string.vibrator));
		
		list.add(newIntent(this, BacklightTest.class));
		result.add(res.getString(R.string.backlight));
		
		list.add(newIntent(this, LcdTestActivity.class));
		result.add(res.getString(R.string.lcd));
		
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
		
		list.add(newIntent(this, BTAddressTest.class));
		result.add(res.getString(R.string.Bluetooth_address));
		
		list.add(newIntent(Launcher.BLUETOOTH_SETTINGS_PACKAGE, Launcher.BLUETOOTH_SETTINGS_CLASS));
		result.add(res.getString(R.string.bt_test));
		
		list.add(newIntent(this, WifiAddressTest.class));
		result.add(res.getString(R.string.wifi_address));
		
		list.add(newIntent(Launcher.WIFI_SETTINGS_PACKAGE, Launcher.WIFI_SETTINGS_CLASS));
		result.add(res.getString(R.string.wifi_test));
		
		list.add(newIntent(this, ImeiTest.class));
		result.add(res.getString(R.string.imei));
		
		//list.add(newIntent(Launcher.RADIO_INFO_PACKAGE, Launcher.RADIO_INFO_TARGET_CLASS));
		list.add(newIntent(Launcher.FM_TEST_PACKAGES, Launcher.FM_TEST_TARGET_CLASS));
		result.add(res.getString(R.string.fm_test));
		
       list.add(new Intent("android.media.action.IMAGE_CAPTURE")
				.setComponent(new ComponentName("com.android.gallery3d","com.android.camera.CameraLauncher"))
				.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
				.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK));//back camera id is 0 if existing.

		list.add(new Intent("android.media.action.IMAGE_CAPTURE")
				.setComponent(new ComponentName("com.android.gallery3d","com.android.camera.CameraLauncher"))
				.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
				.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT));//back camera id is 1 if existing.
		result.add(res.getString(R.string.camera_test));
		list.add(newIntent(Launcher.GPS_TEST_PACKAGE, Launcher.GPS_TEST_TRAGET_CLASS));	
		result.add(res.getString(R.string.gps));
		list.add(newIntent(this, FlashLightTest.class));
		result.add(res.getString(R.string.flash));
		
		list.add(newIntent(this, BatteryLog.class));
		result.add(res.getString(R.string.battery_info));
		
		list.add(newIntent(this, ResultList.class));
		
		Intent factoryIntent = new Intent();
		factoryIntent.setClassName(Launcher.FACTORY_RESET_PACKAGE, Launcher.FACTORY_RESET_CLASS);
		factoryIntent.putExtra("do_factory_reset", "FactoryMode");
		list.add(factoryIntent);			
		Util.log(TAG, "list count:" + list.size());
		
		Util.log(TAG, "hexs modify list count:" + list.size());
		if (result.size()>0){
            for (int i = 0; i < result.size(); i++) {
                reCode.add(10);
              }
        }
		
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{				
		int index = requestCode + 1;
		//hexs start
		if (resultCode>10&&requestCode<reCode.size()) {
			reCode.set(requestCode, 20);
		}
		//hexs end
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
