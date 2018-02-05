package com.zte.engineer;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class NetlockInfo extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.netlock);
		TextView text = (TextView) this.findViewById(R.id.netlock);
		ListView listview = (ListView) this.findViewById(R.id.mccmnc);
		String[] mccmnc = {
		// "001 01",
		};
		text.setTextSize(20);
		text.setText("Netlcok Num: " + mccmnc.length + "\n" + "MCC+MNC:" + "\n");
		// setContentView(text);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mccmnc);
		listview.setDivider(new ColorDrawable(Color.BLACK));
		listview.setAdapter(arrayAdapter);
	}
}
