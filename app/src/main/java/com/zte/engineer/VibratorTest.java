package com.zte.engineer;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class VibratorTest extends ZteActivity {
	/*
	 * Define some aliases to make these debugging flags easier to refer to.
	 */
	private final static String LOGTAG = "VibrateTest";

	// Set Vibrate frequency, vibrate 500ms, stop 500ms
	long[] mVibFreq = { 500, 500 };
	private Vibrator mVibrator;
	private NotificationManager mLed;
	// Notification for LED test
	private Notification mNotification;
	// timer handle event
	private static final int TIMER_EVENT_TICK = 1;
	// Notificatin ID
	private static final int NOTIFY_LED = 0x1010;
	
	/*
	 * The method id for restart vibrate & LED
	 */
	private void changeVibratorLedStatus() {
		// Stop the predecessor LED test
//		mLed.cancel(NOTIFY_LED);
//		// change LED color
//		if (Color.RED == mNotification.ledARGB) {
//			mNotification.ledARGB = Color.GREEN;
//		} else if (Color.GREEN == mNotification.ledARGB) {
//			mNotification.ledARGB = Color.RED;
//		}
		// Set new LED config
		//mLed.notify(NOTIFY_LED, mNotification);

		// start new vibrator
		mVibrator.vibrate(this.mVibFreq, -1);
	}

	// TIMER_EVENT_TICK handler
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TIMER_EVENT_TICK:
				changeVibratorLedStatus();
				// send new TIMER_EVENT_TICK message
				sendEmptyMessageDelayed(TIMER_EVENT_TICK, 1000);
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// hide title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.singlebuttonview);

		((Button) findViewById(R.id.singlebutton_pass_button))
				.setOnClickListener(this);
		((Button) findViewById(R.id.singlebutton_false_button))
				.setOnClickListener(this);
		TextView mTextView = (TextView) findViewById(R.id.singlebutton_textview);

		mTextView.setText(R.string.vibrator);

		mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

		//mLed = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		//mNotification = new Notification();

		//mNotification.flags |= Notification.FLAG_SHOW_LIGHTS;

		//mNotification.ledARGB = Color.RED;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mHandler.removeMessages(TIMER_EVENT_TICK);
		mVibrator.cancel();
		//mLed.cancel(NOTIFY_LED);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//mLed.notify(NOTIFY_LED, mNotification);
		mVibrator.vibrate(mVibFreq, -1);

		mHandler.sendEmptyMessageDelayed(TIMER_EVENT_TICK, 1000);
	}

	@Override
	public void finishSelf(int result) {
		mHandler.removeMessages(TIMER_EVENT_TICK);
		mVibrator.cancel();
		//mLed.cancel(NOTIFY_LED);
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
