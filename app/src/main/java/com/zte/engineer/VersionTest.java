package com.zte.engineer;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.SystemProperties;

//import com.mediatek.common.featureoption.FeatureOption;


import java.util.Scanner;
import java.io.File;

public class VersionTest extends ZteActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
    	super.onCreate(savedInstanceState);

    	setContentView(R.layout.singlebuttonview);

		TextView mTextView = (TextView)findViewById(R.id.singlebutton_textview);
		mTextView.setText(R.string.version_number);
    	//Get Telephony Manager
    	//TelephonyManager telephonyManager=(TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		String versionNumber = SystemProperties.get("ro.build.display.id");
		TextView mTextViewVersion = (TextView)findViewById(R.id.singlebutton_textview_1);
		mTextViewVersion.setText(versionNumber);
		
		//TextView mTpVersion = (TextView)findViewById(R.id.singlebutton_textview_3);
		//mTpVersion.setText(R.string.tp_version);
		//TextView mTextTpVersion = (TextView)findViewById(R.id.singlebutton_textview_4);
		//mTextTpVersion.setText(scanTpVersion());

    	((Button)findViewById(R.id.singlebutton_pass_button)).setOnClickListener(this);
    	((Button)findViewById(R.id.singlebutton_false_button)).setOnClickListener(this);
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
	
	private File tpVersion = new File("/proc/gt9xx_version");  


	private String scanTpVersion(){
		try{
			Scanner scan = null;


			if(tpVersion.exists()){
				scan = new Scanner(tpVersion);
			} else {
			return "error0";
			}

			if(scan==null ){
			return "error1";
			}

			if( !scan.hasNextLine()){
			return "error2";
			}
			
			String version = scan.nextLine();
			scan.close();
			return version;
		}catch(Exception e){
		}

		return "error3";
	}	
}
