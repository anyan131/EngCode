package com.zte.engineer;

public class AlspsCali {
	
    static {
        System.loadLibrary("com_zte_engineer_jni");
    }	
	
	public native int AlspsGetVal();
	public native void AlspsSetVal(int psClose,int psAway);	

}

