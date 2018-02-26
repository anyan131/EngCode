package com.newmobi.iic;

/**
 * Created by Administrator on 2018/2/6.
 */

public class IICHelper {
    static {
        System.loadLibrary("iichelper");
    }
    public static native boolean openIIC();


    public static native int closeIIC();

    public static native int writeIIC(int IICNumber);

    public static native byte readIIC();
}
