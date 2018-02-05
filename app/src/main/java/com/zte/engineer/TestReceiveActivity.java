package com.zte.engineer;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TestReceiveActivity extends ZteActivity {

	private static final String LOG_TAG = "TestReceiveActivity";
	// ToneGenerator instance for playing SignalInfo tones
	private ToneGenerator mSignalInfoToneGenerator;

	// The tone volume relative to other sounds in the stream SignalInfo
	private static final int TONE_RELATIVE_VOLUME_SIGNALINFO = ToneGenerator.MAX_VOLUME;
	// int mode_back = AudioManager.MODE_INVALID;
	AudioManager mAudioManager;
	SignalInfoTonePlayer signalInfoTonePlayer;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(LOG_TAG, "onCreate ");

		setContentView(R.layout.singlebuttonview);

		((Button) findViewById(R.id.singlebutton_pass_button)).setOnClickListener(this);
		((Button) findViewById(R.id.singlebutton_false_button)).setOnClickListener(this);
        
		TextView mTextView = (TextView) findViewById(R.id.singlebutton_textview);
		mTextView.setText(R.string.audio_receiver_new);

		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		// mode_back = mAudioManager.getMode();

		try {
			mSignalInfoToneGenerator = new ToneGenerator(AudioManager.STREAM_VOICE_CALL, 100);
			setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
			// setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);

		} catch (RuntimeException e) {
			Log.v(LOG_TAG, "CallNotifier: Exception caught while creating "
					+ "mSignalInfoToneGenerator: " + e);
			mSignalInfoToneGenerator = null;
		}

		int toneID = ToneGenerator.TONE_DTMF_8;

		// Create the SignalInfo tone player and pass the ToneID
		signalInfoTonePlayer = new SignalInfoTonePlayer(toneID);
		signalInfoTonePlayer.start();
		// mSignalInfoToneGenerator.startTone(toneID);
		// mAudioManager.setMode(mode_back);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.v(LOG_TAG, "onResume ");
		super.onResume();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(mAudioManager.getMode() == AudioManager.MODE_IN_CALL){
			mAudioManager.setMode(AudioManager.MODE_NORMAL);
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(mAudioManager.getMode() == AudioManager.MODE_IN_CALL){
			mAudioManager.setMode(AudioManager.MODE_NORMAL);
		}
	}

	private class SignalInfoTonePlayer extends Thread {
		private int mToneId;

		SignalInfoTonePlayer(int toneId) {
			super();
			mToneId = toneId;
			Log.v(LOG_TAG, "mToneId= " + mToneId);
		}

		@Override
		public void run() {
			Log.v(LOG_TAG, "SignalInfoTonePlayer.run(toneId = " + mToneId
					+ ")...");

			if (mSignalInfoToneGenerator != null) {
				Log.v(LOG_TAG, "mSignalInfoToneGenerator ");
				// First stop any ongoing SignalInfo tone
				mSignalInfoToneGenerator.stopTone();

				// Start playing the new tone if its a valid tone
				mAudioManager.setMode(AudioManager.MODE_IN_CALL);
				mSignalInfoToneGenerator.startTone(mToneId, 2000);
				mAudioManager.setMode(AudioManager.MODE_NORMAL);
			}
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
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
