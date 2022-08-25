package com.zte.engineer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemProperties;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.internal.telephony.PhoneConstants;

import java.lang.reflect.Method;

public class SIMTest extends ZteActivity {
	private TextView tv_sim1;
	private TextView tv_sim2;
	private Button btn_pass;
	private Button btn_fail;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_simtest);
		initView();
		initData();
	}

	private void initData() {
		boolean sim1=isSimInsert(0);
		boolean sim2=isSimInsert(1);
		if(sim1){
			tv_sim1.append("已插入");
		}else {
			tv_sim1.append("未插入");
		}
		if(sim2){
			tv_sim2.append("已插入");
		}else {
			tv_sim2.append("未插入");
		}
		if(sim1 && sim2){
			btn_pass.setEnabled(true);
		}
	}

	private void initView() {
		tv_sim1 = (TextView)findViewById(R.id.tv_sim1);
		tv_sim2 = (TextView)findViewById(R.id.tv_sim2);
		btn_pass = (Button)findViewById(R.id.btn_pass);
		btn_fail = (Button) findViewById(R.id.btn_fail);
		btn_pass.setEnabled(false);
		btn_pass.setOnClickListener(this);
		btn_fail.setOnClickListener(this);
	}

	private boolean isSimInsert(int sim) {
		TelephonyManager mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		try {
			Method method = TelephonyManager.class.getMethod("getSimState", int.class);
			int simState = (Integer) method.invoke(mTelephonyManager, new Object[]{sim});
			if (TelephonyManager.SIM_STATE_READY == simState) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
			case R.id.btn_pass:
				finishSelf(RESULT_PASS);
				break;
			case R.id.btn_fail:
				finishSelf(RESULT_FALSE);
				break;
			default:
				finishSelf(RESULT_FALSE);
				break;
		}
	}

    @Override
    public void onBackPressed() {
        finishSelf(RESULT_FALSE);
        super.onBackPressed();
    }
}
