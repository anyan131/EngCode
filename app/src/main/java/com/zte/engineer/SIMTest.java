package com.zte.engineer;

import android.os.Bundle;
import android.os.SystemProperties;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.android.internal.telephony.PhoneConstants;

public class SIMTest extends ZteActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.singlebuttonview);

		TextView mTextView = (TextView) findViewById(R.id.singlebutton_textview);
		mTextView.setText(R.string.SIM);
		// Get Telephony Manager

		if ("true".equals(SystemProperties.get("ro.mediatek.gemini_support"))) {

			//boolean Sim1State = mGeminiPhone.isSimInsert(PhoneConstants.GEMINI_SIM_1)& mGeminiPhone.isRadioOnGemini(PhoneConstants.GEMINI_SIM_1);
			//boolean Sim2State = mGeminiPhone.isSimInsert(PhoneConstants.GEMINI_SIM_2)& mGeminiPhone.isRadioOnGemini(PhoneConstants.GEMINI_SIM_2);
			//boolean Sim1State = mGeminiPhone.getPhonebyId(PhoneConstants.GEMINI_SIM_1).getIccCard().hasIccCard();
			//boolean Sim2State = mGeminiPhone.getPhonebyId(PhoneConstants.GEMINI_SIM_2).getIccCard().hasIccCard();
            boolean Sim1State = TelephonyManager.getDefault().hasIccCard(PhoneConstants.SIM_ID_1);
            boolean Sim2State = TelephonyManager.getDefault().hasIccCard(PhoneConstants.SIM_ID_2);
            ((Button) findViewById(R.id.singlebutton_pass_button)).setEnabled(false);
            if (Sim1State && Sim2State) {
                ((Button) findViewById(R.id.singlebutton_pass_button)).setEnabled(true);
            }

			TextView mTextViewIMEI = (TextView) findViewById(R.id.singlebutton_textview_2);
			if (Sim1State == true)
                mTextViewIMEI.setText(String.format(getResources().getString(R.string.display_SIM_1), getResources().getString(R.string.SIM_INSERT)));
			else
                mTextViewIMEI.setText(String.format(getResources().getString(R.string.display_SIM_1), getResources().getString(R.string.SIM_NOT_INSERT)));

			mTextViewIMEI = (TextView) findViewById(R.id.singlebutton_textview_3);
			if (Sim2State == true)
                mTextViewIMEI.setText(String.format(getResources().getString(R.string.display_SIM_2), getResources().getString(R.string.SIM_INSERT)));
			else
                mTextViewIMEI.setText(String.format(getResources().getString(R.string.display_SIM_2), getResources().getString(R.string.SIM_NOT_INSERT)));
		} else {
			boolean isSimInsert = TelephonyManager.getDefault().hasIccCard();

			// TextView mTextView =
			// (TextView)findViewById(R.id.singlebutton_textview);
			TextView mTextViewIMEI = (TextView) findViewById(R.id.singlebutton_textview_2);

			// mTextView.setText(R.string.IMEI_test);
			// Get and format IMEI string
			if (isSimInsert == true)
                mTextViewIMEI.setText(String.format(getResources().getString(R.string.display_SIM), getResources().getString(R.string.SIM_INSERT)));
			else
                mTextViewIMEI.setText(String.format(getResources().getString(R.string.display_SIM), getResources().getString(R.string.SIM_NOT_INSERT)));

		}
        ((Button) findViewById(R.id.singlebutton_pass_button)).setOnClickListener(this);

        ((Button) findViewById(R.id.singlebutton_false_button)).setOnClickListener(this);


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
    @Override
    public void onBackPressed() {
        finishSelf(RESULT_FALSE);
        super.onBackPressed();
    }
}
