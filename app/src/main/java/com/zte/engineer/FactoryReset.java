/*add by zhoudawei for factory reset 20110729 start*/
package com.zte.engineer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FactoryReset extends ZteActivity {
	/*
	 * Define some aliases to make these debugging flags easier to refer to.
	 */
	private static final String FACTORY_RESET_PACKAGE = "com.android.settings";
	private static final String FACTORY_RESET_CLASS = "com.android.settings.MasterClear";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = new Intent();
		intent.setClassName(FACTORY_RESET_PACKAGE, FACTORY_RESET_CLASS);
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	@Override
	public void onClick(View arg0) {
	}
}
/* add by zhoudawei for factory reset 20110729 end */
