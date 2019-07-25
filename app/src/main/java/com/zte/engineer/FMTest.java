package com.zte.engineer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FMTest extends ZteActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    	setContentView(R.layout.fm_test);
    	((Button)findViewById(R.id.fm_pass)).setOnClickListener(this);
    	((Button)findViewById(R.id.fm_false)).setOnClickListener(this);
		try {
			Intent intent = new Intent();
			intent.setClassName("com.android.fmradio", "com.android.fmradio.FmMainActivity");
			startActivity(intent);
		} catch (Exception e) {
			Toast.makeText(this, "NO FM Radio", Toast.LENGTH_SHORT).show();
		}
    }  

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
