package com.zte.engineer;

import java.io.File;

import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;
import java.text.DecimalFormat;
public class MemoryTest extends ZteActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.normal);

		File path = Environment.getDataDirectory();
        StatFs statFs = new StatFs(path.getPath());
        long blockSize=statFs.getBlockSize();
        long totalBlocks=statFs.getBlockCount();
        long availableBlocks=statFs.getAvailableBlocks();
        long usedBlocks=totalBlocks-availableBlocks;
        
        String total=formatFileSize(totalBlocks*blockSize,false);
        String available=formatFileSize(availableBlocks*blockSize,false);
        String used=formatFileSize(usedBlocks*blockSize,false);       

		TextView mTextView = (TextView) findViewById(R.id.normal_textview);
		TextView mMemoryTotal = (TextView) findViewById(R.id.normal_textview2);
		TextView mMemoryAvailable = (TextView) findViewById(R.id.normal_textview3);

		mTextView.setText(R.string.memory);
    	mMemoryTotal.setText("Total:"+total);
    	mMemoryAvailable.setText("Available:"+available);

		((Button) findViewById(R.id.normal_pass_button))
				.setOnClickListener(this);
		((Button) findViewById(R.id.normal_false_button))
				.setOnClickListener(this);
	}
    private static DecimalFormat fileIntegerFormat = new DecimalFormat("#0");
    private static DecimalFormat fileDecimalFormat = new DecimalFormat("#0.#");
    /**
     * formatFileSize
     * 
     * @param size Byte
     * @param isInteger if return integer
     * @return string
     */
    public static String formatFileSize(long size, boolean isInteger) {
        DecimalFormat df = isInteger ? fileIntegerFormat : fileDecimalFormat;
        String fileSizeString = "0M";
        if (size < 1024 && size > 0) {
            fileSizeString = df.format((double) size) + "B";
        } else if (size < 1024 * 1024) {
            fileSizeString = df.format((double) size / 1024) + "K";
        } else if (size < 1024 * 1024 * 1024) {
            fileSizeString = df.format((double) size / (1024 * 1024)) + "M";
        } else {
            fileSizeString = df.format((double) size / (1024 * 1024 * 1024)) + "G";
        }
        return fileSizeString;
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
}
