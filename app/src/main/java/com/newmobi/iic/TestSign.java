package com.newmobi.iic;

/**
 * Created by tangh on 2018/11/23,星期二.
 * Email : tangh@new-mobi.com
 * This class is a helper for operating the factory test flag.
 * Note:
 * In init ,the Test Item is :
 *
 */

public class TestSign {
    static {
        System.loadLibrary("TestSign");
    }


    /**
     * @param
     * @return
     */
    public static native boolean JNISignflagAllClear();
    public static native boolean JNISignInit();
    /**
     * JNISignInit
     * When partition has no test information,You should call function;Otherwise, the function cannot be called.
     *
     *"ver*;bat*;gpio*;lcm*;bl*;tp*;fcam*;bcam*;key*;vib*;ring*;loop*;ear*;rev*;sim*;imei*;sd*;bt*;wifi*;fm*;uart*;gps*;i2c*;led*;rf*;";
     */

    public static native byte[] JNISignAllRead();
    public static native boolean JNISignAllWrite(byte[] array);
}
