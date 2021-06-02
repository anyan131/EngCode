package com.zte.engineer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class AeonTouchScreenTest extends ZteActivity {
    private static final String TAG = "MainActivity";
	/** Called when the activity is first created. */
	public final static String PRIVATE_ACTION = "aeon.marine.test.action.finish";
	View mCircleView ;
	View mRectangleView ;
	View mDoovCustomView;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.aeon_tp_test);


        mCircleView = findViewById(R.id.circleView);
        mRectangleView = findViewById(R.id.rectangleView);
		mDoovCustomView = findViewById(R.id.doovCustomView);
		
//		mRectangleView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_GESTURE_ISOLATED);
//		mDoovCustomView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_GESTURE_ISOLATED);
		
		registerReceiver(mReceiver, new IntentFilter(PRIVATE_ACTION));
	}

    BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.i(TAG,"________receive broadcast.");
			//TP test : circle mode
			if(1 == intent.getIntExtra("TestPass", 0)){
				mCircleView.setVisibility(View.GONE);
				mRectangleView.setVisibility(View.VISIBLE);
			
			//TP test : Rectangle mode	
			} else if(2 == intent.getIntExtra("TestPass", 0)){
				mRectangleView.setVisibility(View.GONE);
				mDoovCustomView.setVisibility(View.VISIBLE);
			} else if(3 == intent.getIntExtra("TestPass", 0)){
				doFinish();
			}			
		}
	};
			
	private void doFinish(){
		setResult(RESULT_PASS);
		finish();
		}

		@Override
	public void onConfigurationChanged(Configuration arg0) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(arg0);
			}

	@Override
    protected void onDestroy(){
    	super.onDestroy();
    	unregisterReceiver(mReceiver);
			}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		}

		}

