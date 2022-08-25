package com.zte.engineer;

import java.io.FileDescriptor;

public class Serial {

    private static Serial serial;

    public static Serial getInstance() {
        if (serial == null) {
            synchronized (Serial.class) {
                if (null == serial) {
                    serial = new Serial();
                }
            }
        }
        return serial;
    }

    static {
        System.loadLibrary("Serial");
    }

    public native FileDescriptor open(String path, int bat, int flag);


    public native void close();
}
