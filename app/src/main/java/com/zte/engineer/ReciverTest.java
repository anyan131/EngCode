package com.zte.engineer;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class ReciverTest extends ZteActivity {
	/*
	 * Define some aliases to make these debugging flags easier to refer to.
	 */
	private final static String LOGTAG = "ZTEReceiverTest";
	private final static int MESSAGE_START_PLAY = 1;
	private final static int MESSAGE_STOP_PLAY = 2;
	private AudioManager mAudioManager = null;
	private MediaPlayer mp = null;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.singlebuttonview);
		((TextView) findViewById(R.id.singlebutton_textview))
				.setText(R.string.audio_receiver);
		((Button) findViewById(R.id.singlebutton_pass_button))
				.setOnClickListener(this);
		((Button) findViewById(R.id.singlebutton_false_button))
				.setOnClickListener(this);
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		//mp = MediaPlayer.create(ReciverTest.this, R.raw.backroad);
		// mAudioManager.getMode();
		//mAudioManager.setMode(AudioManager.MODE_IN_CALL);
		
		//mp.setVolume(0.125f, 0.125f);
	}

	Handler mPlayerHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_START_PLAY:
				int vol_max_voice = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL); 
				mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, vol_max_voice, 0);
				if(mp.isPlaying()==true){
					mp.reset();
				}
				mp.setLooping(true);
				mp.start();
				break;
			case MESSAGE_STOP_PLAY:
				mp.stop();
				mp.release();
				mAudioManager.setMode(0);
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mPlayerHandler.sendEmptyMessage(MESSAGE_STOP_PLAY);
		mAudioManager.abandonAudioFocusForCall();		
		mAudioManager.setMode(0);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		
		super.onResume();
		mp = MediaPlayer.create(ReciverTest.this, R.raw.backroad);
		mAudioManager.setMode(AudioManager.MODE_IN_CALL);
		//mAudioManager.setMode(0);
		
		mAudioManager.requestAudioFocusForCall(AudioManager.STREAM_VOICE_CALL,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
		mPlayerHandler.sendEmptyMessage(MESSAGE_START_PLAY);
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
