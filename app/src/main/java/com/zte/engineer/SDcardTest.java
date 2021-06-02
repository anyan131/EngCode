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
	private boolean isPlusIn = false;
	private long mSDTotalCount;
	private long mSDAvailableCount;
	private long mSDUsedCount;
	private boolean isPlusIn2 = false;
	private long mSDTotalCount2;
	private long mSDAvailableCount2;
	private long mSDUsedCount2;

	private TextView mSDStatus;
	private TextView mSDTotal;
	private TextView mBSDUsed;
	private TextView mBSDAvailable;

	private TextView mSDStatus2;
	private TextView mSDTotal2;
	private TextView mBSDUsed2;
	private TextView mBSDAvailable2;
	private Button btn_pass;
	private Button btn_fail;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.normal);
		initView();
		initData();
	}

	private void initData() {
		StorageManager mStorageManager = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
		String[] storagePathList = mStorageManager.getVolumePaths();
		if (storagePathList.length >= 1) {
			String state = mStorageManager.getVolumeState(storagePathList[0]);
			isPlusIn = Environment.MEDIA_MOUNTED.equals(state);
			StatFs stat = new StatFs(storagePathList[0]);
			if (isPlusIn) {
				mSDTotalCount = (long) stat.getBlockCount() * stat.getBlockSize() / 1024 / 1024;
				mSDAvailableCount = (long) stat.getAvailableBlocks() * stat.getBlockSize() / 1024 / 1024;
				mSDUsedCount = mSDTotalCount - mSDAvailableCount;
				mSDStatus.append(getString(R.string.sd_mounted));
			} else {
				mSDTotalCount = 0;
				mSDAvailableCount = 0;
				mSDUsedCount = 0;
				mSDStatus.append(getString(R.string.sd_removed));
			}

			mSDTotal.append(mSDTotalCount+"MB");
			mBSDUsed.append(mSDUsedCount+"MB");
			mBSDAvailable.append(mSDAvailableCount+"MB");
		}
		if (storagePathList.length >= 2) {
			String state = mStorageManager.getVolumeState(storagePathList[1]);
			isPlusIn2 = Environment.MEDIA_MOUNTED.equals(state);
			StatFs stat = new StatFs(storagePathList[1]);
			if (isPlusIn2) {
				mSDTotalCount2 = (long) stat.getBlockCount() * stat.getBlockSize() / 1024 / 1024;
				mSDAvailableCount2 = (long) stat.getAvailableBlocks() * stat.getBlockSize() / 1024 / 1024;
				mSDUsedCount2 = mSDTotalCount2 - mSDAvailableCount2;
				mSDStatus2.append(getString(R.string.sd_mounted));
				btn_pass.setEnabled(true);
			} else {
				mSDTotalCount2 = 0;
				mSDAvailableCount2 = 0;
				mSDUsedCount2 = 0;
				mSDStatus2.append(getString(R.string.sd_removed));
			}

			mSDTotal2.append(mSDTotalCount2+"MB");
			mBSDUsed2.append(mSDUsedCount2+"MB");
			mBSDAvailable2.append(mSDAvailableCount2+"MB");
		}else {
			mSDStatus2.append(getString(R.string.sd_removed));
			mSDTotal2.append(mSDTotalCount2+"MB");
			mBSDUsed2.append(mSDUsedCount2+"MB");
			mBSDAvailable2.append(mSDAvailableCount2+"MB");
		}
	}

	private void initView() {
		mSDStatus = (TextView) findViewById(R.id.normal_textview2);
		mSDTotal = (TextView) findViewById(R.id.normal_textview3);
		mBSDUsed = (TextView) findViewById(R.id.normal_textview4);
		mBSDAvailable = (TextView) findViewById(R.id.normal_textview5);

		mSDStatus2 = (TextView) findViewById(R.id.normal_textview9);
		mSDTotal2 = (TextView) findViewById(R.id.normal_textview10);
		mBSDUsed2 = (TextView) findViewById(R.id.normal_textview11);
		mBSDAvailable2 = (TextView) findViewById(R.id.normal_textview12);

		btn_pass = (Button) findViewById(R.id.normal_pass_button);
		btn_fail = (Button) findViewById(R.id.normal_false_button);
		btn_pass.setEnabled(false);
		btn_pass.setOnClickListener(this);
		btn_fail.setOnClickListener(this);

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
