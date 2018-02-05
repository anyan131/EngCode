package com.zte.engineer;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

public class DeviceInfo extends Activity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		init();
	}

	private void init() {
		setTitle(R.string.device_info);
		setContentView(R.layout.normal);

		TextView basebandView = (TextView) findViewById(R.id.normal_textview);
		String basebandVersion = Build.BOARD;
		basebandView.setText(String.format(getString(R.string.baseband_is),
				basebandVersion));

		TextView buildVersionView = (TextView) findViewById(R.id.normal_textview1);
		String buildVersion = Build.VERSION.SDK;
		buildVersionView.setText(String.format(
				getString(R.string.buildversion_is), buildVersion));
	}
}
