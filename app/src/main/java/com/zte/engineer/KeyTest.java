package com.zte.engineer;

import java.util.ArrayList;

//import com.android.internal.policy.impl.PhoneWindowManager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.provider.Settings;

public class KeyTest extends ZteActivity {

	private final static String TAG = "KeyTest";

	private boolean querty = false; // default is false

	ArrayList<keyAndTextId> keyAndTextIdArray = new ArrayList<keyAndTextId>();
	keyTestManager manager;

	/*
	 * private final String Screenoff = "android.intent.action."; private
	 * BroadcastReceiver screenoff = new BroadcastReceiver() {
	 * 
	 * @Override public void onReceive(Context context, Intent intent) { // TODO
	 * Auto-generated method stub Log.e("test","test code"); int textId =
	 * getTextId(KeyEvent.KEYCODE_POWER);
	 * 
	 * if (0 != textId) { TextView t = (TextView)findViewById(textId);
	 * t.setVisibility(View.INVISIBLE); }
	 * 
	 * manager.remove(KeyEvent.KEYCODE_POWER); if (0 ==
	 * manager.getRemainnings()) { //mContext.setResult(RESULT_OK); finish(); }
	 * return; } };
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
		//getWindow()
		//		.addFlags(WindowManager.LayoutParams.FLAG_HOMEKEY_DISPATCHED);
		//Settings.System.putInt(getContentResolver(), "HOME_KEY_TEST", 0);
		// setContentView(R.layout.key_test);
		init(querty);

		// PhoneWindowManager.setKeyTestState(true);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		// registerReceiver(screenoff, new IntentFilter(Screenoff));
		Settings.System.putInt(getContentResolver(), "HOME_KEY_TEST", 1);
		Settings.System.putInt(getContentResolver(), "MENU_KEY_TEST", 1);
		super.onResume();

		// PhoneWindowManager.setKeyTestState(true);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		// unregisterReceiver(screenoff);
		Settings.System.putInt(getContentResolver(), "HOME_KEY_TEST", 0);
		Settings.System.putInt(getContentResolver(), "MENU_KEY_TEST", 0);
		super.onPause();

		// PhoneWindowManager.setKeyTestState(false);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Settings.System.putInt(getContentResolver(),"HOME_KEY_TEST", 0);
		Settings.System.putInt(getContentResolver(), "MENU_KEY_TEST", 0);		
		super.onDestroy();		
}
	public boolean dispatchKeyEvent(KeyEvent event) {
		Util.log(TAG, "keycode:" + event.getKeyCode());
		//Settings.System.putInt(getContentResolver(), "HOME_KEY_TEST", 0);
		performanceKeyEvent(event);
		return true;
		// return super.dispatchKeyEvent(event);
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
	}

	private void init(boolean querty) {
		if (querty) {
			// TODO: if it's querty phone, complete here
			setContentView(R.layout.key_test);
			((Button) findViewById(R.id.s_key_test_pass))
					.setOnClickListener(this);
			((Button) findViewById(R.id.s_key_test_false))
					.setOnClickListener(this);
		} else {
			setContentView(R.layout.key_test);
			((Button) findViewById(R.id.s_key_test_pass))
					.setOnClickListener(this);
			((Button) findViewById(R.id.s_key_test_false))
					.setOnClickListener(this);
		}

		// getWindow().addFlags(0x01000000);

		initKeyAndText(querty);
		manager = new keyTestManager(keyAndTextIdArray);

		// PhoneWindowManager wm = get
	}

	private void initKeyAndText(boolean querty) {
		if (querty) {
			initKeyAndTextIdArrayQwerty();
		} else {
			initKeyAndTextIdArray();
		}
	}

	private void initKeyAndTextIdArray() {
		keyAndTextIdArray.clear();
		// addItem(KeyEvent.KEYCODE_POWER, R.id.s_key_power);
		addItem(KeyEvent.KEYCODE_VOLUME_UP, R.id.s_key_volume_up);
		addItem(KeyEvent.KEYCODE_VOLUME_DOWN, R.id.s_key_volume_down);
		addItem(KeyEvent.KEYCODE_MENU, R.id.s_key_menu);
		addItem(KeyEvent.KEYCODE_HOME, R.id.s_key_home);
		addItem(KeyEvent.KEYCODE_BACK, R.id.s_key_back);
		// addItem(KeyEvent.KEYCODE_SEARCH, R.id.s_key_search);
	}

	private void initKeyAndTextIdArrayQwerty() // querty
	{
		// TODO:
	}

	private void addItem(int keyCode, int textId) {
		keyAndTextId k = new keyAndTextId(keyCode, textId);
		keyAndTextIdArray.add(k);
	}

	private int getTextId(int keyCode) {
		int size = keyAndTextIdArray.size();

		if (size == 0) {
			return 0;
		}

		for (int i = 0; i < size; i++) {
			if (keyAndTextIdArray.get(i).keyCode == keyCode) {
				Util.log(TAG, "find keyCode in Array");
				return keyAndTextIdArray.get(i).textId;
			}
		}

		return 0;
	}

	private void performanceKeyEvent(KeyEvent event) {
		int textId = getTextId(event.getKeyCode());

		if (0 != textId) {
			TextView t = (TextView) findViewById(textId);
			t.setVisibility(View.INVISIBLE);
		}

		manager.remove(event.getKeyCode());
		if (0 == manager.getRemainnings()) {
			finishSelf(RESULT_PASS);
		}
	}

	private class keyAndTextId {
		public int keyCode;
		public int textId;

		public keyAndTextId(int keyCode, int textId) {
			this.keyCode = keyCode;
			this.textId = textId;
		}
	}

	private class keyTestManager {
		private ArrayList<keyAndTextId> managerArray;

		public keyTestManager(ArrayList<keyAndTextId> a) {
			managerArray = a;
		}

		public int getRemainnings() {
			return managerArray.size();
		}

		public void remove(int keyCode) {
			int size = getRemainnings();

			if (size <= 0) {
				Util.log(TAG, "keyTestManager remove->size error");
				return;
			}

			for (int i = 0; i < size; i++) {
				if (managerArray.get(i).keyCode == keyCode) {
					managerArray.remove(i);
					Util.log(TAG, "deleted suceed.remain:" + getRemainnings());
					return;
				}
			}

			Util.log(TAG, "remain:" + getRemainnings());
		}

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.s_key_test_pass:
			finishSelf(RESULT_PASS);
			break;
		case R.id.s_key_test_false:
			finishSelf(RESULT_FALSE);
			break;
		default:
			finishSelf(RESULT_PASS);
			break;
		}
	}

}
