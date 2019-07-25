package com.zte.engineer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EthernetTestActivity extends ZteActivity {
	private Button mPassBt;
	private Button mFailBt;
	private Button mEthernetTestBt;
	private TextView mEthernetStatus;
	private TextView mEthernetAddr;
	private ConnectivityManager mCM;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    	setContentView(R.layout.ethernet_test);
    	mPassBt = (Button)findViewById(R.id.ethernet_pass);
    	mFailBt = (Button)findViewById(R.id.ethernet_false);
		mEthernetTestBt = (Button)findViewById(R.id.ethernet_bt);
		mEthernetStatus = (TextView) findViewById(R.id.ethernet_status_tv);
		mEthernetAddr = (TextView) findViewById(R.id.ethernet_addr_tv);
    	mPassBt.setOnClickListener(this);
    	mFailBt.setOnClickListener(this);
		mEthernetTestBt.setOnClickListener(this);
    	mPassBt.setEnabled(false);
		mCM = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

    }

	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.ethernet_bt:{
			NetworkInfo netInfo = mCM.getActiveNetworkInfo();
			if(netInfo != null){
				String netName = netInfo.getTypeName();
				if(netName != null && netName.equals("Ethernet")){
					if(netInfo.isAvailable()){
						String ethernetAddr = netInfo.getExtraInfo();
						if(ethernetAddr != null && ethernetAddr.contains(":")){
							mEthernetStatus.setText(R.string.on);
							mEthernetAddr.setText(ethernetAddr);
							mPassBt.setEnabled(true);
						}else {
							mEthernetStatus.setText(R.string.off);
							mEthernetAddr.setText(R.string.ethernet_null_addr);
							mPassBt.setEnabled(false);
						}
					}
				}else{
					mEthernetStatus.setText(R.string.off);
					mEthernetAddr.setText(R.string.ethernet_null_addr);
					mPassBt.setEnabled(false);
				}
			}else{
				mEthernetStatus.setText(R.string.off);
				mEthernetAddr.setText(R.string.ethernet_null_addr);
				mPassBt.setEnabled(false);
			}
			break;
		}
		case R.id.ethernet_pass:
			finishSelf(RESULT_PASS);
			break;
		case R.id.ethernet_false:
			finishSelf(RESULT_FALSE);
			break;
		default:
			finishSelf(RESULT_PASS);
			break;
		}
    }
}
