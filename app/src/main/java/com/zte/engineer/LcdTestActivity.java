/**
 * The JAVA file is for LCD test activity, it's base on android open source.
 * Add By WeiBo 2010-09-26 
 */
package com.zte.engineer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Button;

/**
 * The Class is for LCD test 2010-09-26
 * 
 * @author WeiBo
 */
public class LcdTestActivity extends ZteActivity {
	/*
	 * Define some aliases to make these debugging flags easier to refer to.
	 */
	private final static String LOGTAG = "TestActivity";
	private static final int TIMER_EVENT_TICK = 1;
	// define constants and variables
	private LinearLayout mFSLl;
	private LinearLayout mButton;
	private int mBGColor;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// hide title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lcd_test_view);
		// hide staus bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		Util.log(LOGTAG, "EMLcdTest.onCreate: this=" + this);

		mFSLl = (LinearLayout) this.findViewById(R.id.fullscreen_linelayout);
		mButton = (LinearLayout) this
				.findViewById(R.id.fullscreen_linelayout_button);
		mButton.setVisibility(View.INVISIBLE);
		((Button) findViewById(R.id.lcd_test_false_button))
				.setOnClickListener(this);
		((Button) findViewById(R.id.lcd_test_pass_button))
				.setOnClickListener(this);

		setDefaultKeyMode(DEFAULT_KEYS_DISABLE);

		// set default color
		mBGColor = Color.WHITE;
		mFSLl.setBackgroundColor(mBGColor);

		mHandler.sendEmptyMessageDelayed(TIMER_EVENT_TICK, 1400);
	}

	/*
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) { boolean
	 * ret_value = true; textview = (TextView) findViewById(R.id.tv2); lcdtest =
	 * (TextView) findViewById(R.id.lcd_test); if (keyCode ==
	 * KeyEvent.KEYCODE_BACK) { switch (mBGColor) { case Color.WHITE: mBGColor =
	 * Color.RED; break; case Color.RED: mBGColor = Color.GREEN; break; case
	 * Color.GREEN: mBGColor = Color.BLUE; break; case Color.BLUE: mBGColor =
	 * Color.BLACK; break; case Color.BLACK: setResult(RESULT_OK); finish();
	 * break; }
	 * 
	 * mFSLl.setBackgroundColor(mBGColor); return true; }
	 * 
	 * return ret_value; }
	 */

	private void changeLCDColor() {

		switch (mBGColor) {
		case Color.WHITE:
			mBGColor = Color.RED;
			break;
		case Color.RED:
			mBGColor = Color.GREEN;
			break;
		case Color.GREEN:
			mBGColor = Color.BLUE;
			break;
		case Color.BLUE:
			mBGColor = Color.BLACK;
			break;
		}

		mFSLl.setBackgroundColor(mBGColor);

	}

	// TIMER_EVENT_TICK handler
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TIMER_EVENT_TICK:

				if (mBGColor == Color.BLACK) {
					mHandler.removeMessages(TIMER_EVENT_TICK);
					mFSLl.setBackgroundColor(Color.WHITE);
					mButton.setVisibility(View.VISIBLE);
				} else {
					changeLCDColor();
					sendEmptyMessageDelayed(TIMER_EVENT_TICK, 1400);
				}
				break;
			}
		}
	};

	public void onBackPressed() {
		if (mBGColor == Color.BLACK) {
			finishSelf(RESULT_PASS);
		}

	}

	@Override
	public void finishSelf(int result) {
		mHandler.removeMessages(TIMER_EVENT_TICK);
		super.finishSelf(result);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lcd_test_pass_button:
			finishSelf(RESULT_PASS);
			break;
		case R.id.lcd_test_false_button:
			finishSelf(RESULT_FALSE);
			break;
		default:
			finishSelf(RESULT_PASS);
			break;
		}
	}
}