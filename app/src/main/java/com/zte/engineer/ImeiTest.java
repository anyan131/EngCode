package com.zte.engineer;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.internal.telephony.PhoneConstants;
import com.mediatek.telephony.TelephonyManagerEx;
import android.os.SystemProperties;

public class ImeiTest extends ZteActivity {
    private final String TAG = ImeiTest.this.getClass().getName();

    private Button passBtn;
    private Button failBtn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setContentView(R.layout.singlebuttonview);

        passBtn = (Button) findViewById(R.id.singlebutton_pass_button);
        failBtn = (Button) findViewById(R.id.singlebutton_false_button);

        TextView mTextView = (TextView) findViewById(R.id.singlebutton_textview);
        mTextView.setText(R.string.imei);
        // Get Telephony Manager
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);

		if ("true".equals(SystemProperties.get("ro.mediatek.gemini_support"))) {
			//String mIMEI1 = telephonyManager.getDeviceIdGemini(0);
			//String mIMEI2 = telephonyManager.getDeviceIdGemini(1);
			String mIMEI1 = TelephonyManagerEx.getDefault().getDeviceId(PhoneConstants.SIM_ID_1);
			String mIMEI2 = TelephonyManagerEx.getDefault().getDeviceId(PhoneConstants.SIM_ID_2);
			TextView mTextViewIMEI = (TextView) findViewById(R.id.singlebutton_textview_2);
			mTextViewIMEI.setText(String.format(
					getResources().getString(R.string.display_IMEI_1), mIMEI1));
			mTextViewIMEI = (TextView) findViewById(R.id.singlebutton_textview_3);
			mTextViewIMEI.setText(String.format(
                    getResources().getString(R.string.display_IMEI_2), mIMEI2));
            if (mIMEI1 == null || mIMEI2 == null) {
                passBtn.setEnabled(false);
            } else {
                if (mIMEI1.length() != 15 || mIMEI1.length() != 14
                        || mIMEI2.length() != 15 || mIMEI2.length() != 14) {
                    passBtn.setEnabled(false);
                }
            }


        } else {
            String mIMEI = telephonyManager.getDeviceId();

            if (mIMEI == null) {
                passBtn.setEnabled(false);
            } else {
                if (mIMEI.length() != 15 || mIMEI.length() != 14) {
                    passBtn.setEnabled(false);
                }
            }
			// TextView mTextView =
			// (TextView)findViewById(R.id.singlebutton_textview);
			TextView mTextViewIMEI = (TextView) findViewById(R.id.singlebutton_textview_2);

            // mTextView.setText(R.string.IMEI_test);
            // Get and format IMEI string
            mTextViewIMEI.setText(String.format(
                    getResources().getString(R.string.display_IMEI), mIMEI));
        }
        passBtn.setOnClickListener(this);
        failBtn.setOnClickListener(this);
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
