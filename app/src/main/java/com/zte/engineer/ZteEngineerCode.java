//add for EngineerCode 20111031

package com.zte.engineer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import static com.android.internal.telephony.TelephonyIntents.SECRET_CODE_ACTION;
import android.os.SystemProperties;

public class ZteEngineerCode {
	/**
	 * refer to ZteEngineerCode.java in EngineerCode Application
	 */
	public static final String TEST_LIST = "*983*0#";
	public static final String SELF_TEST = "*983*70#";
	public static final String BATTERY_LOG = "*983*25#";
	public static final String GPS_TEST = "*983*477#";
	public static final String SMS_STATE = "*983*2#";
	public static final String SMS_START = "*983*1#";
	public static final String SMS_STOP = "*983*3#";
	public static final String SMS_EDITNUMBER = "*983*4#";
	public static final String ENGINEERCODE_LISTVIEW = "*987*0#";
	public static final String PRODUCE_INFO = "*983*154#";
	public static final String BOARD_CODE = "*983*7#";
	public static final String ZTE_VERSION_CMD = "*983*32#";
	public static final String AEON_INTERNAL_VERSION_CMD = "*983*31#";
	public static final String ZTE_CUSTOM_VERSION_CMD = "*983*1275#";
	public static final String ZTE_HARDWARE_TEST_WIFI = "*983*93#";
	public static final String ZTE_HARDWARE_TEST_BT = "*983*28#";
	public static final String FACTORY_RECOVER = "*983*57#";
	public static final String FACTORY_RECOVERY_TWO = "*983*987#";
	public static final String MTK_ENGINEERMODE = "*983*3640#";
	public static final String NETWORK_LOCK_STATE = "*983*239#";
        public static final String COMMAND = "command";

	public static final String ACTION_LAUNCHER_TEST_LIST = "com.zte.engineer.action.TEST_LIST";
	public static final String ACTION_SELF_TEST = "com.zte.engineer.action.SELF_TEST";
	public static final String ACTION_BATTERY_LOG = "com.zte.engineer.action.BATTERY_LOG";
	public static final String ACTION_GPS_TEST = "com.zte.engineer.action.GPS_TEST";

	private static final String OPTION = "option";
	private static final int EDITNUMBER = 1;

	public static boolean handleZteEngineerCode(Context context, String input) {
		if (null == input) {
			return false;
		}
		if (input.equals(TEST_LIST)) {
			Intent intent = new Intent(ACTION_LAUNCHER_TEST_LIST);
			context.startActivity(intent);
			return true;
		} else if (input.equals(SELF_TEST)) {
			Intent intent = new Intent(ACTION_SELF_TEST);
			context.startActivity(intent);
			return true;
		} else if (input.equals(BATTERY_LOG)) {
			Intent intent = new Intent(ACTION_BATTERY_LOG);
			context.startActivity(intent);
			return true;
		} else if (input.equals(GPS_TEST)) {
			Intent intent = new Intent();
			intent.setClassName("com.mediatek.ygps",
					"com.mediatek.ygps.YgpsActivity");
			context.startActivity(intent);
			return true;
		} else if (input.equals(ENGINEERCODE_LISTVIEW)) {
			context.startActivity(new Intent(
					"com.zte.engineer.action.EngineerCodeListView"));
			return true;
		} else if (input.equals(PRODUCE_INFO)) {
			Intent intent = new Intent();
			intent.setClassName("com.zte.engineer",
					"com.zte.engineer.ProduceInfoListView");
			context.startActivity(intent);
			return true;
		} else if (input.equals(BOARD_CODE)) {
			Intent intent = new Intent();
			intent.setClassName("com.zte.engineer",
					"com.zte.engineer.BoardCode");
			context.startActivity(intent);
			return true;
		} else if (input.equals(ZTE_VERSION_CMD)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("Public Version Information")
					.setMessage(SystemProperties.get("ro.build.display.id"))
					.setPositiveButton("OK", null).create().show();
			return true;
		} else if(input.equals(AEON_INTERNAL_VERSION_CMD)){
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("Internal Version Information")
					.setMessage(
							SystemProperties.get("ro.sw.version")
									+ SystemProperties
											.get("ro.sw.version.incremental")
									+ "\n"
									+ SystemProperties.get("ro.build.date"))
					.setPositiveButton("OK", null).create().show();
			return true;
		} else if (input.equals(ZTE_CUSTOM_VERSION_CMD)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("Version Information")
					.setMessage(
							SystemProperties.get("ro.build.display.id")
									+ SystemProperties
											.get("ro.custom.build.version")
									+ "\n"
									+ SystemProperties.get("ro.build.date"))
					.setPositiveButton("OK", null).create().show();
			return true;
		} else if (input.equals(ZTE_HARDWARE_TEST_WIFI)) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setClassName("com.mediatek.engineermode",
					"com.mediatek.engineermode.wifi.WiFi");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
			return true;
		} else if (input.equals(ZTE_HARDWARE_TEST_BT)) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setClassName("com.mediatek.engineermode",
					"com.mediatek.engineermode.bluetooth.BtList");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
			return true;
		} else if ((input.equals(FACTORY_RECOVER))||(input.equals(FACTORY_RECOVERY_TWO))) {
			Intent factoryIntent = new Intent();
			factoryIntent.setClassName("com.android.settings",
					"com.android.settings.Settings$PrivacySettingsActivity");
			factoryIntent.putExtra("do_factory_reset", "FactoryMode");
			factoryIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(factoryIntent);
			return true;
		} else if (input.equals(MTK_ENGINEERMODE)) {
			Intent intent = new Intent(SECRET_CODE_ACTION,
					Uri.parse("android_secret_code://3646633"));
			context.sendBroadcast(intent);
			return true;
		} else if (input.equals(NETWORK_LOCK_STATE)) {
			Intent intent = new Intent();
			intent.setClassName("com.zte.engineer",
					"com.zte.engineer.NetlockInfo");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
			return true;
		}
		PackageInfo sms = null;
		try {
			sms = context.getPackageManager().getPackageInfo(
					"com.zte.smssecurity", 0);
		} catch (NameNotFoundException e) {
		}
		if (null != sms) {
			if (input.equals(SMS_STATE)) {
				Intent intent = new Intent();
				intent.setClassName("com.zte.smssecurity",
						"com.zte.smssecurity.SMSSecuritySettings");
				context.startActivity(intent);
				return true;
			} else if (input.equals(SMS_START)) {
				context.sendBroadcast(new Intent(
						"com.zte.smssecurity.action.startservice"));
				return true;
			} else if (input.equals(SMS_STOP)) {
				context.sendBroadcast(new Intent(
						"com.zte.smssecurity.action.stopservice"));
				return true;
			} else if (input.equals(SMS_EDITNUMBER)) {
				Intent intent = new Intent();
				intent.setClassName("com.zte.smssecurity",
						"com.zte.smssecurity.SMSSecuritySettings");
				intent.putExtra(OPTION, EDITNUMBER);
				context.startActivity(intent);
				return true;
			}
		}
		return false;
	}

}
