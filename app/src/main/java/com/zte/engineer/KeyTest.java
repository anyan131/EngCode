package com.zte.engineer;

import java.util.ArrayList;

//import com.android.internal.policy.impl.PhoneWindowManager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.provider.Settings;

public class KeyTest extends ZteActivity {

	private static final String TAG="KeyTestActivity";
	private static String str="";//"|";
	private static final int maxKeyNum = 5; //This is define for this project,pls changed it according to your's.
	private static int sCount = 0;
	private TextView show_key;
	private Button success;
	private Button failed;
	private static final ArrayList<Integer> mTestedKey = new ArrayList<Integer>();
	private boolean mRecentKeyFlag = false;
	private boolean mIsHadTestHomeKey = false;
	private boolean mIsHadTestRecentKey = false;
	
	private void buttonEnableCheck(int keyCode, int resId){
		if(!isTestedKey(keyCode)){
			System.out.println("---lzg sCount="+sCount);
			str += getString(resId)+"\n";
			show_key.setText(str);
			setKeyTested(keyCode);
			sCount += 1;
			if(sCount == maxKeyNum){
				success.setEnabled(true);
			}
		}
	}
	
	private void setKeyTested(int keyCode) {
		mTestedKey.add(new Integer(keyCode));
	}
	
	private boolean isTestedKey(int keyCode) {
		if(mTestedKey.contains(new Integer(keyCode))){
			return true;
		}else{
			return false;
		}
	}
	
	private void initKeyTest(){	
		sCount = 0;
		if(mTestedKey.size() > 0){
			mTestedKey.clear();
		}
		show_key = (TextView) findViewById(R.id.show_key);
		success = (Button) findViewById(R.id.s_key_test_pass);
		failed = (Button) findViewById(R.id.s_key_test_false);
		
		success.setEnabled(false);
		failed.setEnabled(true);
		
		success.setOnClickListener(this);
		failed.setOnClickListener(this);
		
		IntentFilter intent = new IntentFilter();
		intent.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
		registerReceiver(getKeyReceiver, intent);
		setTitle(R.string.key_test);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return true;
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		 
		switch (keyCode) {
		case KeyEvent.KEYCODE_MENU:
			buttonEnableCheck(keyCode, R.string.key_menu);
			return true;
		case KeyEvent.KEYCODE_BACK:
			buttonEnableCheck(keyCode, R.string.key_back);
			return true;
		case KeyEvent.KEYCODE_VOLUME_UP:
			buttonEnableCheck(keyCode, R.string.key_volume_up);
			return true ;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			buttonEnableCheck(keyCode, R.string.key_volume_down);
			return true ;
		case KeyEvent.KEYCODE_1:
			buttonEnableCheck(keyCode, R.string.key_1);
			return true ;
	   default:
	   		return false;
		}
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);
	  //  this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_SCRIM);
		setContentView(R.layout.key_test);
		initKeyTest(); 
	}
	
	@Override
	protected void onPause() {
		//e.ro add begin
		/*
		 *do not go home
//		 */
//		Intent tmpIntent = new Intent();
//		tmpIntent.setAction("donotgohome");
//		tmpIntent.putExtra("home", false);
//	    sendBroadcast(tmpIntent);
		//e.ro add end		
	    super.onPause();
	}
	
	@Override
	protected void onResume() {
		//e.ro add begin
		/*
		 *do not go home
		 */
//		Intent tmpIntent = new Intent();
//		tmpIntent.setAction("donotgohome");
//		tmpIntent.putExtra("home", true);
//	    sendBroadcast(tmpIntent);
		//e.ro add end
		super.onResume();
	}
	
	//////////////////////
	static final String SYSTEM_DIALOG_REASON_KEY ="reason"; 
	static final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS ="globalactions";
	
	static final String SYSTEM_DIALOG_REASON_RECENT_APPS ="recentapps"; 
	
	static final String SYSTEM_DIALOG_REASON_HOME_KEY ="homekey"; 
	////////////////
	
	@Override

    public void onAttachedToWindow() {
      //this.getWindow().addFlags(WindowManager.LayoutParams.TYPE_KEYGUARD);
  	  // this.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);
  	  super.onAttachedToWindow();//e.ro modify 
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		str="|";
		unregisterReceiver(getKeyReceiver);
		finish();
	}

	BroadcastReceiver getKeyReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			System.out.println("---lzg action="+action);
			if(action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {  
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                System.out.println("---lzg reason="+reason);
                if(reason != null) {
                    if(reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) { 
                    	if(mRecentKeyFlag){
                    		mRecentKeyFlag = false;
                    	}else{
                    		if(!mIsHadTestHomeKey){
                    			str+="Home|" + "\n";
                    			mIsHadTestHomeKey = true;
                    			sCount += 1;
                    			if(sCount == maxKeyNum){
                    				success.setEnabled(true);
                    			}
                    		}
                    	}
                    }else if(reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)){
                    	if(!mIsHadTestRecentKey){
                    		str+="Recent|" + "\n";
                        	mRecentKeyFlag = true;
                        	mIsHadTestRecentKey = true;
                        	sCount += 1;
                			if(sCount == maxKeyNum){
                				success.setEnabled(true);
                			}
                    	}
                    }
                }
	        }
			show_key.setText(str);
		}
	};
	
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.s_key_test_pass:
			finishSelf(RESULT_PASS);
			break;
		case R.id.s_key_test_false:
			finishSelf(RESULT_FALSE);
			break;
		default:
			finishSelf(RESULT_PASS);
			break;
		}
	}

}
