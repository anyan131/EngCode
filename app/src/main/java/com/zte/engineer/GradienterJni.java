package com.zte.engineer;

public class GradienterJni {
    static {
        System.loadLibrary("com_zte_engineer_jni");
    }
    static native int gsensorCali(int x, int y, int z);
    static native int gsensorClear(int x, int y, int z);
	static native int gsensorOpen(int x, int y, int z);
	static native int gsensorClose(int x, int y, int z);
}
