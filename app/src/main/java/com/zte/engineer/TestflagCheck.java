/**
 * The JAVA file is for IMEI activity, it's base on android open source.
 * Add By WeiBo 2010-12-15
 */
package com.zte.engineer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import android.os.SystemProperties;

//import com.android.internal.telehpony.TelephonyProperties;
import android.telephony.PhoneStateListener;

public class TestflagCheck extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// hide title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.singlebuttonview);

		TextView mTextView = (TextView) findViewById(R.id.singlebutton_textview);
		mTextView.setText(R.string.test_flag);
		// Get Telephony Manager
		//TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		/*
		 * if ("true".equals(SystemProperties.get("ro.mediatek.gemini_support"))) { //String
		 * mIMEI1=telephonyManager.getDeviceIdGemini(0); String
		 * mIMEI2=telephonyManager.getDeviceIdGemini(1);
		 * 
		 * TextView mTextViewIMEI =
		 * (TextView)findViewById(R.id.singlebutton_textview_2);
		 * mTextViewIMEI.setText
		 * (String.format(getResources().getString(R.string.
		 * display_IMEI_1),mIMEI1)); mTextViewIMEI =
		 * (TextView)findViewById(R.id.singlebutton_textview_3);
		 * mTextViewIMEI.setText
		 * (String.format(getResources().getString(R.string.
		 * display_IMEI_2),mIMEI2)); } else { String
		 * mIMEI=telephonyManager.getDeviceId();
		 * 
		 * //TextView mTextView =
		 * (TextView)findViewById(R.id.singlebutton_textview); TextView
		 * mTextViewIMEI = (TextView)findViewById(R.id.singlebutton_textview_2);
		 * 
		 * //mTextView.setText(R.string.IMEI_test); //Get and format IMEI string
		 * mTextViewIMEI
		 * .setText(String.format(getResources().getString(R.string.
		 * display_IMEI),mIMEI)); }
		 */
		// serial number
		TextView mTextViewIMEI;
		//String mbarcode = telephonyManager.getSN();
		//Phone mPhone = PhoneFactory.getDefaultPhone();
		//String mbarcode = mPhone.getSN();
        String mbarcode = SystemProperties.get("gsm.serial");
		int len = mbarcode.length();

		if (16 > len) {
			mTextViewIMEI = (TextView) findViewById(R.id.singlebutton_textview_2);
			mTextViewIMEI
					.setText("barcode length<16,it is invalid Serial Number:"
							+ mbarcode);
		} else {
			String mSerialNumber = mbarcode.substring(0, 16);
			mTextViewIMEI = (TextView) findViewById(R.id.singlebutton_textview_2);
			mTextViewIMEI.setText(String.format(
					getResources().getString(R.string.serialnumber),
					mSerialNumber));
		}

		if (64 > len) {
			mTextViewIMEI = (TextView) findViewById(R.id.singlebutton_textview_5);
			mTextViewIMEI.setText("barcode length<64:"
					+ "Cann't read test flag.");

			mTextViewIMEI = (TextView) findViewById(R.id.singlebutton_textview_3);
			mTextViewIMEI.setText("Test Flag:" + "FFFFF00F");
		} else {
			// comptest flag
			String comptest = mbarcode.substring(62, 63);
			mTextViewIMEI = (TextView) findViewById(R.id.singlebutton_textview_3);
			mTextViewIMEI.setText("Test Recheck Flag:" + comptest);

			// recheck flag
			String recheck = mbarcode.substring(60, 62);
			mTextViewIMEI = (TextView) findViewById(R.id.singlebutton_textview_4);
			mTextViewIMEI.setText("Calibration Flag:" + recheck);

			// gsm coupling ,wifi , bluetooth
			String gsmcoup = mbarcode.substring(56, 57);
			String wifibt = mbarcode.substring(42, 46);
			mTextViewIMEI = (TextView) findViewById(R.id.singlebutton_textview_5);
			mTextViewIMEI.setText("Test Flag:" + wifibt + gsmcoup + recheck
					+ comptest);
		}

		mTextViewIMEI = (TextView) findViewById(R.id.singlebutton_textview_6);
		mTextViewIMEI.setText("Barcode:" + mbarcode);

		Button mButton = (Button) findViewById(R.id.singlebutton_pass_button);

		mButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			finish();
		}

		return super.onKeyDown(keyCode, event);
	}
}
