/**
 * The JAVA file is for backlight test activity, it's base on android open source.
 * Add By WeiBo 2010-12-14
 */
package com.zte.engineer;

import android.app.Activity;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IPowerManager;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The Class is for backlight test activity
 * 
 * @author WeiBo
 */
public class BacklightTest extends ZteActivity {
	/*
	 * Define some aliases to make these debugging flags easier to refer to.
	 */
	private final static String LOGTAG = "BacklightTest";

	// Backlight range is from 0 - 255. Need to make sure that user
	// doesn't set the backlight to 0 and get stuck
	private static final int MINIMUM_BACKLIGHT = android.os.PowerManager.BRIGHTNESS_OFF + 10;//10
	private static final int MAXIMUM_BACKLIGHT = android.os.PowerManager.BRIGHTNESS_ON;//255
    private static final int AUTOMATIC_ENABLED = 1;
    private static final int AUTOMATIC_DISABLED = 0;

	private static int mCurrentBrightness = 0;
    private static int mAutomatic = 0;
	// timer handle event
	private static final int BACKLIGHT_TIMER_EVENT_TICK = 1;

	/**
	 * Switch the backlight brightness
	 * 
	 * @author WeiBo
	 */
	private void changeBrightness() {
		if (MINIMUM_BACKLIGHT == mCurrentBrightness) {
			mCurrentBrightness = MAXIMUM_BACKLIGHT;
		} else if (MAXIMUM_BACKLIGHT == mCurrentBrightness) {
			mCurrentBrightness = MINIMUM_BACKLIGHT;
		}

		setBrightness(mCurrentBrightness);
	}

	// TIMER_EVENT_TICK handler
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BACKLIGHT_TIMER_EVENT_TICK:
				changeBrightness();
				// send new TIMER_EVENT_TICK message
				sendEmptyMessageDelayed(BACKLIGHT_TIMER_EVENT_TICK, 1500);
				break;
			}
		}
	};

	private void setBrightness(int brightness) {
		if (Util.DEBUG) {
			return;
		}
//		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//		pm.setBacklightBrightness(brightness);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.screenBrightness = brightness / 255.0f;
		Log.i(LOGTAG,"brightness: "+brightness / 255.0f);
		getWindow().setAttributes(lp);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// hide title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.singlebuttonview);
		
		Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.bkcolor);
        this.getWindow().setBackgroundDrawable(drawable);

		TextView mTextView = (TextView) findViewById(R.id.singlebutton_textview);
		mTextView.setText(R.string.backlight);

		// Set ok button
		((Button) findViewById(R.id.singlebutton_pass_button))
				.setOnClickListener(this);
		((Button) findViewById(R.id.singlebutton_false_button))
				.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();

        //set backlight not auto mode
        mAutomatic = getMode();
        Log.d(LOGTAG, "mAutomatic=" + mAutomatic);
        if(mAutomatic == AUTOMATIC_ENABLED) {
            setMode(AUTOMATIC_DISABLED);
        }
		// Set backlight bright as 255
		mCurrentBrightness = MAXIMUM_BACKLIGHT;
		setBrightness(mCurrentBrightness);

		// Start time handle event delay
		mHandler.sendEmptyMessageDelayed(BACKLIGHT_TIMER_EVENT_TICK, 1000);
	}

	@Override
	protected void onPause() {
		super.onPause();

		mHandler.removeMessages(BACKLIGHT_TIMER_EVENT_TICK);
		set2SystemBrightness();
	}

	private void set2SystemBrightness() {
        if(mAutomatic == AUTOMATIC_ENABLED) {
            setMode(AUTOMATIC_ENABLED);
        }
		int systemBrightness = 0;
		try {
			systemBrightness = Settings.System.getInt(getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS);
		} catch (SettingNotFoundException snfe) {
			// if can't get the initializing backlight brightness, set
			// systemBright max.
			systemBrightness = MAXIMUM_BACKLIGHT;
		}
		setBrightness(systemBrightness);
	}
    private int getMode() {
		return Settings.System.getInt(getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE, 0);
    }
    private void setMode(int mode) {
        Settings.System.putInt(getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE, mode);
    }

	@Override
	public void finishSelf(int result) {
		mHandler.removeMessages(BACKLIGHT_TIMER_EVENT_TICK);
		set2SystemBrightness();
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
    @Override
    public void onBackPressed() {
        finishSelf(RESULT_FALSE);
	    super.onBackPressed();
    }
}
