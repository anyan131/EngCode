package com.zte.engineer;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SNTest extends ZteActivity {
    /** Called when the activity is first created. */
	private TextView mSNText;
	private Button mPassBt;
	private Button mFailBt;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	setContentView(R.layout.sn_test);
		mPassBt = (Button)findViewById(R.id.sn_pass);
		mFailBt = (Button)findViewById(R.id.sn_false);
		mPassBt.setOnClickListener(this);
		mFailBt.setOnClickListener(this);
		mSNText = (TextView)findViewById(R.id.sn_tv);
		String snString = null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
			snString = Build.getSerial();
		}
		if(snString != null && !snString.equals("")){
			mSNText.setText("SN: " + snString);
			mPassBt.setEnabled(true);
		}else{
			mSNText.setText("SN: null");
			mPassBt.setEnabled(false);
		}
    }

	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.sn_pass:
			finishSelf(RESULT_PASS);
			break;
		case R.id.sn_false:
			finishSelf(RESULT_FALSE);
			break;
		default:
			finishSelf(RESULT_PASS);
			break;
		}
    }
}
