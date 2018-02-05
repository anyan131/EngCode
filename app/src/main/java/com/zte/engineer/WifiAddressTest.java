package com.zte.engineer;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

public class WifiAddressTest extends ZteActivity {

	private boolean needclose = false;
	WifiManager wifiManager;
	String macAddress;
	WifiInfo wifiInfo;

	TextView mWifiAddress;
	TextView mWifiStatus;
	// Receiver the wifi status change
	protected BroadcastReceiver mwifistatusReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
				switch (intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
						WifiManager.WIFI_STATE_UNKNOWN)) {
				case WifiManager.WIFI_STATE_ENABLED: {
					WifiInfo wifiInfo1 = wifiManager.getConnectionInfo();
					String macAddress1 = wifiInfo1.getMacAddress();
					if (macAddress1 != null) {
						mWifiStatus.setText(String.format(
								getString(R.string.wifi_status),
								getString(R.string.on)));
						mWifiAddress.setText(String.format(
								getString(R.string.wifi_address_is),
								macAddress1));
					} else {
						myHandler.sendEmptyMessageDelayed(1, 500);
					}
				}
					break;
				case WifiManager.WIFI_STATE_DISABLING: {
					mWifiStatus.setText(String.format(
							getString(R.string.wifi_status),
							getString(R.string.off)));
					mWifiAddress.setText(String.format(
							getString(R.string.wifi_address_is),
							getString(R.string.unknown)));
				}
					break;
				}
			}
		}
	};

	private Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			WifiInfo wifiInfo1 = wifiManager.getConnectionInfo();
			String macAddress1 = wifiInfo1.getMacAddress();
			if (macAddress1 != null) {
				mWifiStatus.setText(String
						.format(getString(R.string.wifi_status),
								getString(R.string.on)));
				mWifiAddress.setText(String.format(
						getString(R.string.wifi_address_is), macAddress1));
			} else {
				sendEmptyMessageDelayed(1, 500);
			}
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.singlebuttonview);

		TextView mTextView = (TextView) findViewById(R.id.singlebutton_textview);
		mWifiStatus = (TextView) findViewById(R.id.singlebutton_textview_2);
		mWifiAddress = (TextView) findViewById(R.id.singlebutton_textview_3);

		mTextView.setText(R.string.wifi_address);

		wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		// check if wifi is on

		if (!wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
			needclose = true;

			mWifiStatus.setText(String.format(getString(R.string.wifi_status),
					getString(R.string.off)));
			mWifiAddress.setText(R.string.bt_read_address);
		} else {
			wifiInfo = wifiManager.getConnectionInfo();
			// int ipAddress = wifiInfo.getIpAddress();
			macAddress = wifiInfo.getMacAddress();

			mWifiStatus.setText(String.format(getString(R.string.wifi_status),
					getString(R.string.on)));
			mWifiAddress.setText(String.format(
					getString(R.string.wifi_address_is), macAddress));
		}

		// Set intent filter
		IntentFilter filter = new IntentFilter(
				WifiManager.WIFI_STATE_CHANGED_ACTION);
		// register filter receiver
		registerReceiver(mwifistatusReceiver, filter);

		((Button) findViewById(R.id.singlebutton_pass_button))
				.setOnClickListener(this);
		((Button) findViewById(R.id.singlebutton_false_button))
				.setOnClickListener(this);
	}

	@Override
	public void finishSelf(int result) {
		if (true == needclose) {
			wifiManager.setWifiEnabled(false);
			needclose = false;
		}
		myHandler.removeMessages(1);
		unregisterReceiver(mwifistatusReceiver);
		super.finishSelf(result);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.singlebutton_pass_button:
			finishSelf(RESULT_PASS);
			break;
		case R.id.singlebutton_false_button:
			finishSelf(RESULT_FALSE);
			break;
		default:
			finishSelf(RESULT_PASS);
			break;
		}
	}
}
