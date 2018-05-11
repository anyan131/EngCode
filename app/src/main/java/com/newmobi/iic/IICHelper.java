package com.newmobi.iic;

/**
 * Created by Administrator on 2018/2/6.
 */

public class IICHelper {
    static {
        System.loadLibrary("iichelper");
    }
    public static native int openIIC();
    public static native int closeIIC();
    public static native int writeIIC(int IICNumber , int reg_buf ,int len);
    public static native int readIIC(int IICNumber,int len);
}
