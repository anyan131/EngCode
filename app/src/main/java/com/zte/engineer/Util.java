package com.zte.engineer;

import android.util.Log;
import android.content.Context;
import android.content.SharedPreferences;

public class Util {

	public static final String TAG = "EngineerCode";

	private static boolean LOG = true;

	/**
	 * In release, DEBUG should be set to false; true:Purpose is to debug codes
	 * in eclipse, because some test need permission signature(eg.
	 * BacklightTest). false:Release or other.
	 */
	public static boolean DEBUG = false;

	private Util() {

	}

	public static final void log(String tag, String info) {
		if (LOG) {
			Log.v(TAG, tag + ":" + info);
		}
	}
	
	public static void saveTestResult(Context context, String key, String value){
	    SharedPreferences sharedPref = context.getSharedPreferences("test_results", 0);
		SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
	}
	
	public static String getLastTestResult(Context context){
	    SharedPreferences sharedPref = context.getSharedPreferences("test_results", 0);
		String flag = sharedPref.getString("result", "2");
		//NVRamUtil.writeFlag(1,50); //aeon lee block this
		return flag;
	}		

}
