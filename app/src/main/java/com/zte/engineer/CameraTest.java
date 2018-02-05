package com.zte.engineer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CameraTest extends ZteActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    	setContentView(R.layout.camera_test);
    	((Button)findViewById(R.id.camera_pass)).setOnClickListener(this);
    	((Button)findViewById(R.id.camera_false)).setOnClickListener(this);
    }  
	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.camera_pass:
			finishSelf(RESULT_PASS);
			break;
		case R.id.camera_false:
			finishSelf(RESULT_FALSE);
			break;
		default:
			finishSelf(RESULT_PASS);
			break;
		}
    }
}
