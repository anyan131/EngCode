package com.zte.engineer;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.internal.telephony.PhoneConstants;
import com.mediatek.telephony.TelephonyManagerEx;
import android.os.SystemProperties;

public class ImsiTest extends ZteActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.singlebuttonview);

		TelephonyManager telephonyManager = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);

		TextView mTextView = (TextView) findViewById(R.id.singlebutton_textview);
		mTextView.setText(R.string.imsi);
		if ("true".equals(SystemProperties.get("ro.mediatek.gemini_support"))) {
			//String mIMSI1 = telephonyManager.getSubscriberIdGemini(0);
			//String mIMSI2 = telephonyManager.getSubscriberIdGemini(1);
            String mIMSI1 = TelephonyManagerEx.getDefault().getSubscriberId(PhoneConstants.SIM_ID_1);
            String mIMSI2 = TelephonyManagerEx.getDefault().getSubscriberId(PhoneConstants.SIM_ID_2);
			TextView mTextViewIMEI = (TextView) findViewById(R.id.singlebutton_textview_2);
			mTextViewIMEI.setText(String.format(
					getResources().getString(R.string.display_IMSI_1), mIMSI1));
			mTextViewIMEI = (TextView) findViewById(R.id.singlebutton_textview_3);
			mTextViewIMEI.setText(String.format(
					getResources().getString(R.string.display_IMSI_2), mIMSI2));
		} else {
			String mIMSI = telephonyManager.getSubscriberId();

			TextView mTextViewIMEI = (TextView) findViewById(R.id.singlebutton_textview_2);

			mTextViewIMEI.setText(String.format(
					getResources().getString(R.string.display_IMSI), mIMSI));
		}

		((Button) findViewById(R.id.singlebutton_pass_button))
				.setOnClickListener(this);
		((Button) findViewById(R.id.singlebutton_false_button))
				.setOnClickListener(this);

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
