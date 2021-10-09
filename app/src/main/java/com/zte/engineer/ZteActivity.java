package com.zte.engineer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public abstract class ZteActivity extends Activity implements View.OnClickListener {

	public static final int RESULT_PASS = 10;
	public static final int RESULT_FALSE = 20;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	public void onBackPressed() {
		finishSelf(RESULT_FALSE);
	}

	public void finishSelf(int result) {
		setResult(result);
		finish();
	}

	@Override
	abstract public void onClick(View arg0);
}
