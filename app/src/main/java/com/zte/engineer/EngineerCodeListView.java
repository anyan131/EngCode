package com.zte.engineer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class EngineerCodeListView extends Activity {

	private ListView listView = null;
	Resources r = null;
	private int[] stringsIDs = { R.string.produce_information,
			R.string.phone_test, R.string.recover_settings,
			R.string.battery_information };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		r = getResources();
		listView = new ListView(this);
		listView.setAdapter(new MyAdapter(getBaseContext()));
		listView.setOnItemClickListener(new MyOnItemClickListener());
		setContentView(listView);

	}

	private class MyOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			switch (stringsIDs[position]) {
			case R.string.produce_information:
				intent.setClass(EngineerCodeListView.this,
						ProduceInfoListView.class);
				break;
			case R.string.phone_test:
				intent.setAction("com.zte.engineer.action.TEST_LIST");
				break;
			case R.string.recover_settings:
				intent.setClassName(Launcher.FACTORY_RESET_PACKAGE,
						Launcher.FACTORY_RESET_CLASS);
				intent.putExtra("do_factory_reset", "FactoryMode");
				break;
			case R.string.battery_information:
				intent.setAction("com.zte.engineer.action.BATTERY_LOG");
				break;
			default:
				intent.setClass(EngineerCodeListView.this,
						NotSupportNotification.class);
				intent.putExtra("notification", getString(stringsIDs[position]));
				break;
			}
			startActivity(intent);
		}
	}

	private class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		private Context mContext;

		public MyAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return stringsIDs.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (null == convertView) {
				convertView = mInflater.inflate(R.layout.list_text, null);
			}
			((TextView) convertView.findViewById(R.id.list_text)).setText(r
					.getString(stringsIDs[position]));
			return convertView;
		}
	}

}
