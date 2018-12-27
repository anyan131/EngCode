package com.zte.engineer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class UsbCameraTestActivity extends ZteActivity {
    /** Called when the activity is first created. */
	private final int REQUEST_CODE = 3000;
	private final int RESULT_CODE  = 4000;
	private Button mPassBt;
	private Button mFailBt;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    	setContentView(R.layout.usb_camera);
    	mPassBt = (Button)findViewById(R.id.usb_camera_pass);
    	mFailBt = (Button)findViewById(R.id.usb_camera_false);
    	mPassBt.setOnClickListener(this);
    	mFailBt.setOnClickListener(this);
    	mPassBt.setEnabled(false);
		try {
			Intent intent = new Intent();
			intent.setClassName("com.xinyi.usbcamera", "com.xinyi.usbcamera.view.USBCameraActivity");
			startActivityForResult(intent, REQUEST_CODE);
		} catch (Exception e) {
			Toast.makeText(this, "NO Usb Camera", Toast.LENGTH_SHORT).show();
		}
    }  

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(resultCode == RESULT_CODE){
    		String result = data.getStringExtra("result");
    		if(result != null && result.equals("pass")){
    			mPassBt.setEnabled(true);
    		}
    	 }
    };
    
	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.fm_pass:
			finishSelf(RESULT_PASS);
			break;
		case R.id.fm_false:
			finishSelf(RESULT_FALSE);
			break;
		default:
			finishSelf(RESULT_PASS);
			break;
		}
    }
}
