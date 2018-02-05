package com.zte.engineer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

public class ItemAdapter extends BaseAdapter {
	private Context mContext;
	private String[] mImageIds;
	// minjibing
	private static final String EXTRAS_CAMERA_FACING = "android.intent.extras.CAMERA_FACING";

	public ItemAdapter(Context c, String[] Ids) {
		mContext = c;
		mImageIds = Ids;
	}

	public int getCount() {
		return mImageIds.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Button itemButton;
		if (convertView == null) {
			itemButton = new Button(mContext);
			itemButton.setLayoutParams(new GridView.LayoutParams(230, 90));
		} else {
			itemButton = (Button) convertView;
		}

		final int index = position;
		itemButton.setText(mImageIds[position]);
		itemButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int id = index + 1;
				Intent intent = new Intent();
				switch (id) {
				case 1:
					intent.setClass(mContext, TouchScreenTest.class);
					break;
				case 2:
					intent.setClass(mContext, LcdTestActivity.class);
					break;
				case 3:
					intent.setClassName(Launcher.GPS_TEST_PACKAGE,
							Launcher.GPS_TEST_TRAGET_CLASS);
					break;
				case 4:
					intent.setClass(mContext, UsbMainActivity.class);
					break;
				case 5:
					intent.setClass(mContext, KeyTest.class);
					break;
				case 6:
					intent.setClass(mContext, RingerTest.class);
					break;
				case 7:
					intent.setClass(mContext, EarPhoneAudioLoopTest.class);
					break;
				case 8:
					intent.setClass(mContext, ImeiTest.class); 
					break;
				case 9:
					intent.setClass(mContext, AudioLoopTest.class);
					break;
				case 10:
					intent.setClassName(Launcher.WIFI_SETTINGS_PACKAGE,
							Launcher.WIFI_SETTINGS_CLASS);
					break;
				case 11:
					intent.setClassName(Launcher.BLUETOOTH_SETTINGS_PACKAGE,
							Launcher.BLUETOOTH_SETTINGS_CLASS);
					break;
				case 12:
					intent.setClass(mContext, VibratorTest.class);
					break;
				case 13:
					intent.setClass(mContext, TestPhoneActivity.class);
					break;
				case 14:
					intent.setClass(mContext, BacklightTest.class);
					break;
				case 15:
					intent.setClass(mContext, MemoryTest.class);
					break;

				case 16:
					intent.setClass(mContext, SensorTest.class);
					break;
				case 17:
					intent.setClass(mContext, SDcardTest.class);
					break;
				case 18:
					// intent.setClassName(Launcher.CAMERA_PACKAGE,
					// Launcher.CAMERA_TARGET_CLASS);
					intent.setAction("android.media.action.IMAGE_CAPTURE");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra(
							EXTRAS_CAMERA_FACING,
							android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK);
					Log.i("CAMAPP",
							"__________should to boot the back camera ...");
					break;
				case 19:
					if (android.hardware.Camera.getNumberOfCameras() <= 0) {
						Toast.makeText(mContext, R.string.non_front_camera,
								Toast.LENGTH_SHORT).show();
						return;
					}

					// intent.setClassName(Launcher.CAMERA_PACKAGE,
					// Launcher.CAMERA_TARGET_CLASS);
					// intent.setAction(ACTION_FRONT_CAMERA_TEST);
					intent.setAction("android.media.action.IMAGE_CAPTURE");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra(
							EXTRAS_CAMERA_FACING,
							android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
					Log.i("CAMAPP",
							"__________should to boot the front camera ...");
					break;
				case 20:
					intent.setClassName(Launcher.FM_TEST_PACKAGES,
							Launcher.FM_TEST_TARGET_CLASS);
					break;
				case 21:
					intent.setClass(mContext, SIMTest.class);
					break;

				default:
					break;
				}
				mContext.startActivity(intent);
			}
		});
		return itemButton;
	}

}