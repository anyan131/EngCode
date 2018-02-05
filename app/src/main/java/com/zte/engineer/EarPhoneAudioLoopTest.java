package com.zte.engineer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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

//import com.mediatek.audioprofile.AudioProfile;
//import com.mediatek.audioprofile.AudioProfileImpl;
//import com.mediatek.audioprofile.AudioProfileManagerImpl;

public class EarPhoneAudioLoopTest extends ZteActivity {

	private final static String LOGTAG = "EarPhoneAudioLoopTest";

	private int isHeadsetConnect;
	private AudioManager mAudioManager = null;
	// private AudioProfileImpl mProfile;
	private boolean soundeffect = false;
	private boolean running = false;
	private AlertDialog mDialog = null;

	protected BroadcastReceiver mHeadsetReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			int mState = 0;
			String action = intent.getAction();

			// check the action event
			if (action.equals(Intent.ACTION_HEADSET_PLUG)) {
				Bundle bundle = intent.getExtras();
				if (bundle != null)
					mState = bundle.getInt("state");
				// changing status string
				// mProfile.setSoundEffectEnabled(soundeffect);
				mAudioManager.setParameters("SET_LOOPBACK_TYPE=0");
				if (0 == mState) {
					displayInsertEarphoneDialog();
				} else if (1 == mState) {
					// mProfile.setSoundEffectEnabled(soundeffect);
					cancleInsertEarphoneDialog();
					mAudioManager.setParameters("SET_LOOPBACK_TYPE=22");
				}

				isHeadsetConnect = mState;
			}
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// mProfile = (AudioProfileImpl)
		// AudioProfileManagerImpl.getInstance(this)
		// .getActiveProfile();
		// soundeffect = mProfile.getSoundEffectEnabled();
		// if (soundeffect == true) {
		// mProfile.setSoundEffectEnabled(false);
		// }
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.singlebuttonview);

		TextView mTextView = (TextView) findViewById(R.id.singlebutton_textview);
		mTextView.setText(R.string.earphone_audio_loop);

		mAudioManager = (AudioManager) this
				.getSystemService(Context.AUDIO_SERVICE);

		isHeadsetConnect = AudioSystem.getDeviceConnectionState(
				AudioSystem.DEVICE_OUT_WIRED_HEADSET, "");

		((Button) findViewById(R.id.singlebutton_pass_button))
				.setOnClickListener(this);
		((Button) findViewById(R.id.singlebutton_false_button))
				.setOnClickListener(this);

		mDialog = new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setMessage(
						getText(R.string.insert_earphone_warning).toString())
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.dismiss();
							}
						}).create();

	}

	@Override
	public void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
		registerReceiver(mHeadsetReceiver, filter);

		running = true;
		new Thread() {
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (java.lang.InterruptedException e) {
				}
				if (running) {
					// if(soundeffect == true)
					// {
					// mProfile.setSoundEffectEnabled(false);
					// }
					mAudioManager.setParameters("SET_LOOPBACK_TYPE=22");
				}
			}
		}.start();
	}

	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(mHeadsetReceiver);
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

	private void displayInsertEarphoneDialog() {
		mDialog.show();
	}

	private void cancleInsertEarphoneDialog() {
		mDialog.dismiss();
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
