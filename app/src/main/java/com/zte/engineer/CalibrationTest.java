package com.zte.engineer;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.PhoneProxy;
import com.android.internal.telephony.Phone;
//import com.android.internal.telephony.gemini.MTKPhoneFactory;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.SystemProperties;
//import com.mediatek.common.featureoption.FeatureOption;

public class CalibrationTest extends ZteActivity {

	private TextView mSNNumber;
    private TextView mBtWifiFlag;
    private TextView mGSMFlag;
   // private TextView mTDFlag;
	private TextView mWCDMAFlag;
    private TextView mLTEFlag;
    private TextView mCouplingFlag;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
    	super.onCreate(savedInstanceState);

    	setContentView(R.layout.calibration_layout);
    	
		mSNNumber = (TextView)findViewById(R.id.sn_number);
        mBtWifiFlag = (TextView)findViewById(R.id.bt_wifi_flag);
        mGSMFlag = (TextView)findViewById(R.id.gsm_flag);
       // mTDFlag = (TextView)findViewById(R.id.td_flag);
		mWCDMAFlag = (TextView)findViewById(R.id.wcdma_flag);
        mLTEFlag = (TextView)findViewById(R.id.lte_flag);
        mCouplingFlag = (TextView)findViewById(R.id.coupling_flag);
		
		//TextView mTextView = (TextView)findViewById(R.id.singlebutton_textview);
		//mTextView.setText(R.string.calibration_string);
    	//Get Telephony Manager
    	//TelephonyManager telephonyManager=(TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
    	//String barcode = "";
		//barcode = TelephonyManager.getDefault().getSN();
		//Phone mPhone = (Phone)(MTKPhoneFactory.getDefaultPhone());
		//barcode = mPhone.getSN();
		String barcode = SystemProperties.get("gsm.serial");
		
		char a = barcode.length()<43?'0':barcode.charAt(42);
		char b = barcode.length()<46?'0':barcode.charAt(45);
		char c = barcode.length()<47?'0':barcode.charAt(46);
		char d = barcode.length()<48?'0':barcode.charAt(47);
		char wcdma_1 = barcode.length()<49?'0':barcode.charAt(48);
		char wcdma_2 = barcode.length()<50?'0':barcode.charAt(49);
		char e = barcode.length()<51?'0':barcode.charAt(50);
		char f = barcode.length()<52?'0':barcode.charAt(51);
		char g = barcode.length()<38?'0':barcode.charAt(37);
		char h = barcode.length()<39?'0':barcode.charAt(38);
		char i = barcode.length()<55?'0':barcode.charAt(54);
		char j = barcode.length()<56?'0':barcode.charAt(55);
		char k = barcode.length()<56?'0':barcode.charAt(56);
		char rf = barcode.length()<63?'0':barcode.charAt(62);

		String boardcode = barcode.length()<16?barcode:barcode.substring(0, 16);
		StringBuffer boardCode1 = new StringBuffer();
		StringBuffer boardCode2 = new StringBuffer();
		StringBuffer boardCode3 = new StringBuffer();
		StringBuffer boardCode_wcdma = new StringBuffer();
		StringBuffer boardCode4 = new StringBuffer();
		StringBuffer boardCode5 = new StringBuffer();		
		//boardCode.append((isLetterOrNumber(a)?a:"0")+"\n");
		//boardCode.append(boardcode);
		//text.setText(boardCode.toString());
		//setContentView(text);
		
		mSNNumber.setText(boardcode);
		//boardCode.delete(0,boardCode.length()-1);
		boardCode1.append((isLetterOrNumber(a)?a:"0")+" ");
		boardCode1.append((isLetterOrNumber(b)?b:"0"));
        mBtWifiFlag.setText(boardCode1.toString());
		//boardCode.delete(0,boardCode.length()-1);
		boardCode2.append((isLetterOrNumber(c)?c:"0")+" ");
		boardCode2.append((isLetterOrNumber(d)?d:"0"));		
        mGSMFlag.setText(boardCode2.toString());
		//boardCode.delete(0,boardCode.length()-1);
		boardCode3.append((isLetterOrNumber(e)?e:"0")+" ");
		boardCode3.append((isLetterOrNumber(f)?f:"0"));		
       // mTDFlag.setText(boardCode3.toString());
		
		//aeon add for wcdma_flag start
		boardCode_wcdma.append((isLetterOrNumber(wcdma_1)?wcdma_1:"0")+" ");
		boardCode_wcdma.append((isLetterOrNumber(wcdma_2)?wcdma_2:"0"));
		mWCDMAFlag.setText(boardCode_wcdma.toString());
		//aeon add for wcdma_flag end
		
		//boardCode.delete(0,boardCode.length()-1);
		boardCode4.append((isLetterOrNumber(g)?g:"0")+" ");
		boardCode4.append((isLetterOrNumber(h)?h:"0"));		
        mLTEFlag.setText(boardCode4.toString());
		//boardCode.delete(0,boardCode.length()-1);
		//boardCode5.append((isLetterOrNumber(i)?i:"0")+" ");
		//boardCode5.append((isLetterOrNumber(j)?j:"0")+" ");
		//boardCode5.append((isLetterOrNumber(k)?k:"0"));
		boardCode5.append((isLetterOrNumber(rf)?rf:"0"));
        mCouplingFlag.setText(boardCode5.toString());
		
    	((Button)findViewById(R.id.singlebutton_pass_button)).setOnClickListener(this);
    	((Button)findViewById(R.id.singlebutton_false_button)).setOnClickListener(this);
	}
	
	private boolean isLetterOrNumber(char b){
		return (b<='Z'&&b>='A')||(b>='0'&&b<='9')||(b<='z'&&b>='a');
	}
	
	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
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
