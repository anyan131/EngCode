package com.zte.engineer;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TestFlag extends ZteActivity {
	private int[] stringIDs = { R.id.flag_TD_Calibration,
			R.id.flag_TD_FinalTest, R.id.flag_GSM_Calibration,
			R.id.flag_GSM_FinalTest, R.id.flag_CMMB_Chip,
			R.id.flag_CMMB_Coupling, R.id.flag_GPS_Chip,
			R.id.flag_GSP_Coupling, R.id.flag_WIFI_Chip,
			R.id.flag_WIFI_Coupling, R.id.flag_SENSOR_ACCELEROMETER_Chip,
			R.id.flag_SENSOR_MAGNETIC_FIELD_Chip, R.id.flag_TD_Coupling };

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.test_flag);
		((Button) findViewById(R.id.flag_test_ok)).setOnClickListener(this);
		setState(getIntent().getStringExtra("flag"));
	}

	private void setState(String flag) {
		TextView textView = null;
		Resources r = getResources();
		int length = stringIDs.length;
		int state = 0;
		for (int i = 0; i < length; i++) {
			if ('1' == flag.charAt(i)) {
				state = R.string.pass;
			} else {
				state = R.string.falsed;
			}
			textView = (TextView) findViewById(stringIDs[i]);
			textView.setText(textView.getText() + r.getString(state));
		}
	}

	@Override
	public void onClick(View arg0) {
		finishSelf(RESULT_PASS);
	}

}
