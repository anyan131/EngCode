package com.zte.engineer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.AudioSystem;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.view.WindowManager;
//import com.mediatek.audioprofile.AudioProfile;
//import com.mediatek.audioprofile.AudioProfileImpl;
//import com.mediatek.audioprofile.AudioProfileManagerImpl;

public class AudioLoopTest extends ZteActivity {
	/*
	 * Define some aliases to make these debugging flags easier to refer to.
	 */
	private final static String LOGTAG = "ZTEAudioLoopTest";

	private int isHeadsetConnect;
	private AudioManager mAudioManager = null;
	// private AudioProfileImpl mProfile;
	private boolean soundeffect = false;
	private boolean running = false;

	public void onCreate(Bundle savedInstanceState) {
		// Turn screen on and show above the keyguard for emergency alert
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		super.onCreate(savedInstanceState);
		// mProfile =
		// (AudioProfileImpl)AudioProfileManagerImpl.getInstance(this).getActiveProfile();
		// soundeffect = mProfile.getSoundEffectEnabled();
		// if(soundeffect == true)
		// {
		// mProfile.setSoundEffectEnabled(false);
		// }
		// hide title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.singlebuttonview);

		TextView mTextView = (TextView) findViewById(R.id.singlebutton_textview);
		mTextView.setText(R.string.audio_loop);

		mAudioManager = (AudioManager) this
				.getSystemService(Context.AUDIO_SERVICE);

		((Button) findViewById(R.id.singlebutton_pass_button))
				.setOnClickListener(this);
		((Button) findViewById(R.id.singlebutton_false_button))
				.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		running = true;
		new Thread() {
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (java.lang.InterruptedException e) {
				}
				if (running) {
					if (soundeffect == true) {
						// mProfile.setSoundEffectEnabled(false);
					}
					mAudioManager.setParameters("SET_LOOPBACK_TYPE=21");
				}
			}
		}.start();
	}

	@Override
	public void onPause() {
		super.onPause();
		running = false;
		mAudioManager.setParameters("SET_LOOPBACK_TYPE=0");
		// mProfile.setSoundEffectEnabled(soundeffect);
	}

	@Override
	protected void onStop() {
		super.onStop();
		running = false;
		mAudioManager.setParameters("SET_LOOPBACK_TYPE=0");
		// mProfile.setSoundEffectEnabled(soundeffect);
	}

	@Override
	public void finishSelf(int result) {
		running = false;
		mAudioManager.setParameters("SET_LOOPBACK_TYPE=0");
		// mProfile.setSoundEffectEnabled(soundeffect);
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
