package com.zte.engineer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class LcdOffTest extends ZteActivity {

	private static final String TAG = "LcdOffTest";

	private LcdPowerControl mLcdPowerControl;
	private static final int DELAY_TIME = 2000; // seconds

	// timer handle event
	private static final int LCDOFF_TIMER_EVENT_TICK = 1;

	// TIMER_EVENT_TICK handler
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LCDOFF_TIMER_EVENT_TICK: {
				// It will turn on LCD in onPause
				finishSelf(true);
			}
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.singlebuttonview);

		((Button) findViewById(R.id.singlebutton_pass_button))
				.setVisibility(View.INVISIBLE);
		((Button) findViewById(R.id.singlebutton_false_button))
				.setVisibility(View.INVISIBLE);

		mLcdPowerControl = new LcdPowerControl();
	}

	@Override
	protected void onResume() {
		super.onResume();

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		if (mLcdPowerControl.LcdPowerOff() != LcdPowerControl.SUCCESS) {
			Util.log(TAG, "LcdPowerOff fail");
			finishSelf(false);

			return;
		}

		mHandler.sendEmptyMessageDelayed(LCDOFF_TIMER_EVENT_TICK, DELAY_TIME);
	}

	@Override
	protected void onPause() {
		super.onPause();

		mHandler.removeMessages(LCDOFF_TIMER_EVENT_TICK);

		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		if (mLcdPowerControl.LcdPowerOn() != LcdPowerControl.SUCCESS) {
			Util.log(TAG, "LcdPowerOn fail");
			finishSelf(false);
		}
	}

	private void finishSelf(boolean resultOK) {
		if (true == resultOK) {
			setResult(RESULT_OK);
		} else {
			setResult(RESULT_FALSE); // fail
		}
		finish();
	}

	@Override
	public void onBackPressed() {
		finishSelf(true);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

}
