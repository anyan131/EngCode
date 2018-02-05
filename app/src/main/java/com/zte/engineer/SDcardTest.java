package com.zte.engineer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class SDcardTest extends ZteActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// hide title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.normal);

		boolean isPlusIn = false;
		long mSDTotalCount;
		long mSDAvailableCount;
		long mSDUsedCount;

		TextView mTextView = (TextView) findViewById(R.id.normal_textview);
		TextView mSDStatus = (TextView) findViewById(R.id.normal_textview2);
		TextView mSDTotal = (TextView) findViewById(R.id.normal_textview3);
		TextView mBSDUsed = (TextView) findViewById(R.id.normal_textview4);
		TextView mBSDAvailable = (TextView) findViewById(R.id.normal_textview5);

		boolean isPlusIn2 = false;
		long mSDTotalCount2;
		long mSDAvailableCount2;
		long mSDUsedCount2;

		TextView mTextView2 = (TextView) findViewById(R.id.normal_textview7);
		TextView mSDStatus2 = (TextView) findViewById(R.id.normal_textview9);
		TextView mSDTotal2 = (TextView) findViewById(R.id.normal_textview10);
		TextView mBSDUsed2 = (TextView) findViewById(R.id.normal_textview11);
		TextView mBSDAvailable2 = (TextView) findViewById(R.id.normal_textview12);

		Button button = (Button) findViewById(R.id.normal_pass_button);
		StorageManager mStorageManager = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
		String[] storagePathList = mStorageManager.getVolumePaths();
		if (storagePathList.length >= 1) {
			String state = mStorageManager.getVolumeState(storagePathList[0]);
			isPlusIn = Environment.MEDIA_MOUNTED.equals(state);
			mTextView.setText(R.string.sd_info);
			StatFs stat = new StatFs(storagePathList[0]);
			if (true == isPlusIn) {
				mSDTotalCount = (long) stat.getBlockCount()
						* stat.getBlockSize() / 1024 / 1024;
				mSDAvailableCount = (long) stat.getAvailableBlocks()
						* stat.getBlockSize() / 1024 / 1024;
				mSDUsedCount = mSDTotalCount - mSDAvailableCount;
				mSDStatus.setText(getString(R.string.state)
						+ getString(R.string.sd_mounted));

			} else {
				mSDTotalCount = 0;
				mSDAvailableCount = 0;
				mSDUsedCount = 0;
				mSDStatus.setText(getString(R.string.state)
						+ getString(R.string.sd_removed));

			}

			mSDTotal.setText(String.format(getString(R.string.sd_total),
					Long.toString(mSDTotalCount)));
			mBSDUsed.setText(String.format(getString(R.string.sd_used),
					Long.toString(mSDUsedCount)));
			mBSDAvailable.setText(String.format(
					getString(R.string.sd_available),
					Long.toString(mSDAvailableCount)));
		}
		if (storagePathList.length >= 2) {
			String state = mStorageManager.getVolumeState(storagePathList[1]);
			isPlusIn2 = Environment.MEDIA_MOUNTED.equals(state);
			mTextView2.setText(R.string.sd2_info);
			StatFs stat = new StatFs(storagePathList[1]);
			if (true == isPlusIn2) {
				mSDTotalCount2 = (long) stat.getBlockCount()
						* stat.getBlockSize() / 1024 / 1024;
				mSDAvailableCount2 = (long) stat.getAvailableBlocks()
						* stat.getBlockSize() / 1024 / 1024;
				mSDUsedCount2 = mSDTotalCount2 - mSDAvailableCount2;
				mSDStatus2.setText(getString(R.string.state)
						+ getString(R.string.sd_mounted));
				button.setEnabled(true);
			} else {
				mSDTotalCount2 = 0;
				mSDAvailableCount2 = 0;
				mSDUsedCount2 = 0;
				mSDStatus2.setText(getString(R.string.state)
						+ getString(R.string.sd_removed));
				button.setEnabled(false);
			}

			mSDTotal2.setText(String.format(getString(R.string.sd_total),
					Long.toString(mSDTotalCount2)));
			mBSDUsed2.setText(String.format(getString(R.string.sd_used),
					Long.toString(mSDUsedCount2)));
			mBSDAvailable2.setText(String.format(
					getString(R.string.sd_available),
					Long.toString(mSDAvailableCount2)));
		}

		/*
		 * 
		 * if(false == isPlusIn) { String storageDirectory =
		 * Environment.getExternalStorageDirectory().toString(); StatFs stat =
		 * new StatFs(storageDirectory);
		 * 
		 * mSDTotalCount = (long) stat.getBlockCount() * stat.getBlockSize() /
		 * 1024 / 1024; mSDAvailableCount = (long) stat.getAvailableBlocks() *
		 * stat.getBlockSize() / 1024 / 1024; mSDUsedCount = mSDTotalCount -
		 * mSDAvailableCount; mSDStatus.setText(getString(R.string.state) +
		 * getString(R.string.sd_mounted)); } else { mSDTotalCount = 0;
		 * mSDAvailableCount = 0; mSDUsedCount =0;
		 * mSDStatus.setText(getString(R.string.state) +
		 * getString(R.string.sd_removed)); }
		 * 
		 * mSDTotal.setText(String.format(getString(R.string.sd_total),
		 * Long.toString(mSDTotalCount)));
		 * mBSDUsed.setText(String.format(getString(R.string.sd_used),
		 * Long.toString(mSDUsedCount)));
		 * mBSDAvailable.setText(String.format(getString(R.string.sd_available),
		 * Long.toString(mSDAvailableCount)));
		 */
		((Button) findViewById(R.id.normal_pass_button))
				.setOnClickListener(this);
		((Button) findViewById(R.id.normal_false_button))
				.setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.normal_pass_button:
			finishSelf(RESULT_PASS);
			break;
		case R.id.normal_false_button:
			finishSelf(RESULT_FALSE);
			break;
		default:
			finishSelf(RESULT_PASS);
			break;
		}
	}
    @Override
    public void onBackPressed() {
        finishSelf(RESULT_FALSE);
	    super.onBackPressed();
    }
}
