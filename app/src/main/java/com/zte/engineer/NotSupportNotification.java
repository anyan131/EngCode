package com.zte.engineer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class NotSupportNotification extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// hide title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.singlebuttonview);

		Intent intent = getIntent();
		String notification = intent.getStringExtra("notification");

		String s = "This feature is not supported now.";
		TextView mTextView = (TextView) findViewById(R.id.singlebutton_textview_1);
		if (notification == null) {
			mTextView.setText(s);
		} else {
			s = notification + ":" + s;
		}
		mTextView.setText(s);

		// Set ok button
		Button mButton = (Button) findViewById(R.id.singlebutton_pass_button);
		mButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finishActivity();
			}
		});
	}

	private void finishActivity() {
		setResult(RESULT_CANCELED);
		finish();
	}

	@Override
	public void onBackPressed() {
		finishActivity();
	}

}
