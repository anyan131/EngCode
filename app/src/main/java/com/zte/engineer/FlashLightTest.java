package com.zte.engineer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;

import android.hardware.Camera;
import android.view.WindowManager;
import android.widget.ToggleButton;

public class FlashLightTest extends ZteActivity {
    private static final String TAG = "FlashLightTest";
	private ToggleButton toggleButton;

	private Camera camera = null;
	private Camera.Parameters param;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
       setContentView(R.layout.flash_test);

		toggleButton = (ToggleButton) this.findViewById(R.id.toggleButton1);
		toggleButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				ToggleButton tb = (ToggleButton) v;
				if(!tb.isChecked()){
					param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
				//	toggleButton.setBackgroundColor(0x30ffffff);
				    if(camera!=null){
					   camera.setParameters(param);
					}else{
					camera = Camera.open();
	                camera.setParameters(param);
					}
					
				}else if(tb.isChecked()){
					param.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
				//	toggleButton.setBackgroundColor(0x30ffffff);
				    System.out.println("FlashLightTest----camera");
					 if(camera!=null){
					   camera.setParameters(param);
					}else{
					 camera = Camera.open();
	                 camera.setParameters(param);
					}
				}
				
			}
		});
		((Button)findViewById(R.id.flash_pass)).setOnClickListener(this);
    	((Button)findViewById(R.id.flash_false)).setOnClickListener(this);
		 getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		camera = Camera.open();
	    param = camera.getParameters();
		camera.startPreview();
	}
	
	@Override
	public void finishSelf(int result) {
		super.finishSelf(result);
    //     toggleButton.setChecked(true);
	//	 camera.stopPreview();
	//	 camera.release();
	//	 camera=null;
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		 toggleButton.setChecked(true);
		 camera.stopPreview();
		 camera.release();
		 camera=null;
	}
	
	
	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.flash_pass:
			finishSelf(RESULT_PASS);
			break;
		case R.id.flash_false:
			finishSelf(RESULT_FALSE);
			break;
		default:
			finishSelf(RESULT_PASS);
			break;
		}
	}

		}

