package com.newmobi.iic;

/**
 * Created by Alextao on 2018/5/22,星期二.
 * Email : tao_xue@new-mobi.com
 * This class is a helper for operating the leds.
 * Note:
 * we need to control 3 Leds.
 * the nodes are
 * 'sys/class/leds/red/brightness'
 * 'sys/class/leds/blue/brightness'
 * 'sys/class/leds/green/brightness'
 * 255 for on and 0 for off.
 */

public class LEDHelper {
    static {
        System.loadLibrary("ledhelper");
    }


    /**
     * @param flag the leds.0 for red,1 for blue,2 for green.
     * @return
     */
    public static native boolean JNIcontrolLed(int flag);
}
