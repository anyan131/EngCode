package com.zte.engineer;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import android.os.SystemProperties;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ImeiTest extends ZteActivity {
    private final String TAG = ImeiTest.this.getClass().getName();

    private Button passBtn;
    private Button failBtn;
    private int numSlots = 0;
    private String mIMEI;
    private String mIMEI1;
    private String mIMEI2;
    TelephonyManager telephonyManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setContentView(R.layout.singlebuttonview);

        passBtn = (Button) findViewById(R.id.singlebutton_pass_button);
        failBtn = (Button) findViewById(R.id.singlebutton_false_button);

        TextView mTextView = (TextView) findViewById(R.id.singlebutton_textview);
        mTextView.setText(R.string.imei);
        // Get Telephony Manager
        telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        //add by lzg
        if(Build.VERSION.SDK_INT >= 23) {
            numSlots = telephonyManager.getPhoneCount();
            System.out.println("---lzg numSlots="+numSlots);
        }//end lzg
		if (numSlots == 2 || "true".equals(SystemProperties.get("ro.mediatek.gemini_support"))) {
			//modify by lzg
		    //String mIMEI1 = telephonyManager.getDeviceIdGemini(0);
			//String mIMEI2 = telephonyManager.getDeviceIdGemini(1);
            getTwoImei();
            System.out.println("---lzg mIMEI1="+mIMEI1+"---mIMEI2"+mIMEI2);
			//String mIMEI1 = TelephonyManagerEx.getDefault().getDeviceId(PhoneConstants.SIM_ID_1);
			//String mIMEI2 = TelephonyManagerEx.getDefault().getDeviceId(PhoneConstants.SIM_ID_2);
            //end lzg
			TextView mTextViewIMEI = (TextView) findViewById(R.id.singlebutton_textview_2);
			mTextViewIMEI.setText(String.format(
					getResources().getString(R.string.display_IMEI_1), mIMEI1));
			mTextViewIMEI = (TextView) findViewById(R.id.singlebutton_textview_3);
			mTextViewIMEI.setText(String.format(
                    getResources().getString(R.string.display_IMEI_2), mIMEI2));
//            String defaule_imei1 = new String("00000000");
//            if (mIMEI1 == null || mIMEI1.equals(defaule_imei1)) {
//                passBtn.setEnabled(false);
//            } else {
//                if ((mIMEI1.length() != 15 && mIMEI1.length() != 14)
//                        && (mIMEI2.length() != 15 && mIMEI2.length() != 14)) {
//                    passBtn.setEnabled(false);
//                }
//            }
            if (mIMEI1 == null && mIMEI2 == null) {
                passBtn.setEnabled(false);
            }else if ((mIMEI1 != null && mIMEI1.length() != 15 && mIMEI1.length() != 14) && (mIMEI2 != null && mIMEI2.length() != 15 && mIMEI2.length() != 14)) {
                passBtn.setEnabled(false);
            }else if(mIMEI1 == null && (mIMEI2 != null && mIMEI2.length() != 15 && mIMEI2.length() != 14)){
                passBtn.setEnabled(false);
            }else if((mIMEI1 != null && mIMEI1.length() != 15 && mIMEI1.length() != 14) && mIMEI2 == null){
                passBtn.setEnabled(false);
            }

        } else {
                mIMEI = telephonyManager.getImei();

            if (mIMEI == null) {
                passBtn.setEnabled(false);
            } else {
                if (mIMEI.length() != 15 && mIMEI.length() != 14) {
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

    //add by lzg
    public void getTwoImei() {
        Class<?> clazz = null;
        Method method = null;//(int slotId)
        try {
            clazz = Class.forName("android.os.SystemProperties");
            method = clazz.getMethod("get", String.class, String.class);
            String gsm = (String) method.invoke(null, "ril.gsm.imei", "");
            if (!TextUtils.isEmpty(gsm)) {
                //the value of gsm like:xxxxxx,xxxxxx
                String imeiArray[] = gsm.split(",");
                if (imeiArray != null && imeiArray.length > 0) {
                    mIMEI1 = imeiArray[0];
                    if (imeiArray.length > 1) {
                        mIMEI2 =  imeiArray[1];
                        System.out.println("---lzg 1111111");
                    } else {
                        mIMEI2 = telephonyManager.getImei(1);
                        System.out.println("---lzg 222222222");
                    }
                } else {
                    mIMEI1 = telephonyManager.getImei(0);
                    mIMEI2 = telephonyManager.getImei(1);
                    System.out.println("---lzg 333333333");
                }
            } else {
                System.out.println("---lzg 44444444444");
                mIMEI1 = telephonyManager.getImei(0);
                mIMEI2 = telephonyManager.getImei(1);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    //end lzg

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
