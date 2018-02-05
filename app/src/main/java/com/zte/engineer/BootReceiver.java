package com.zte.engineer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
	private static final String TAG = "BootReceiver";

	/**
	 * Receive com.zte.engineer.action.LAUNCHER_TEST only.
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		String command = intent.getStringExtra(ZteEngineerCode.COMMAND);

		if (null == command) {
			Util.log(TAG, "command is null");
			return;
		}
	}

}
