package com.zte.engineer;

public class LcdPowerControl {

	public static final int FAIL = -1;
	public static final int SUCCESS = 0;

	static {
		System.loadLibrary("com_zte_engineer_jni");
	}

	public native int LcdPowerOn();

	public native int LcdPowerOff();

}
