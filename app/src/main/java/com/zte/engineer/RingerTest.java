package com.zte.engineer;

import java.io.IOException;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class RingerTest extends ZteActivity {
	private static final String TAG = "RingerTest";
	private MediaPlayer mMediaP = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// hide title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.singlebuttonview);

		TextView mTextView = (TextView) findViewById(R.id.singlebutton_textview);
		mTextView.setText(R.string.ringer);

		((Button) findViewById(R.id.singlebutton_pass_button))
				.setOnClickListener(this);
		((Button) findViewById(R.id.singlebutton_false_button))
				.setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		mMediaP = MediaPlayer.create(RingerTest.this, R.raw.backroad);
		mMediaP.setOnErrorListener(new OnErrorListener() {
			public boolean onError(MediaPlayer mp, int what, int extra) {
				Log.e(TAG, "Error occurred while playing audio.");
				mp.stop();
				mp.release();
				return true;
			}
		});
		new Thread() {
			public void run() {
				Log.d(TAG, "public void run()");
				/*try {
					mMediaP.prepare();
					Log.d(TAG, "mMediaP.prepare()");
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				mMediaP.setLooping(true);
				mMediaP.start();
				Log.d(TAG, "mMediaP.start()");
			}
		}.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(mMediaP != null){
			mMediaP.reset();
			mMediaP.stop();
			mMediaP.release();
		}
		Log.d(TAG, "onPause");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(mMediaP != null){
				mMediaP.release();
		}
		Log.d(TAG, "onDestroy");
		super.onDestroy();
	}

	@Override
	public void finishSelf(int result) {
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
