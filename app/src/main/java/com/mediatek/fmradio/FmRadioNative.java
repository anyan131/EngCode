package com.mediatek.fmradio;

/**
 * Created by Administrator on 2018/1/31.
 *
 */

public class FmRadioNative {
    static {
        System.loadLibrary("fmjni");
    }

    /**
     * Open FM device, call before power up
     *
     * @return (true, success; false, failed)
     */
    public static native boolean openDev();

    /**
     * Close FM device, call after power down
     *
     * @return (true, success; false, failed)
     */
    public static native boolean closeDev();

    /**
     * power up FM with frequency use long antenna
     *
     * @param frequency frequency(50KHZ, 87.55; 100KHZ, 87.5)
     * @return (true, success; false, failed)
     */
    public static native boolean powerUp(float frequency);

    /**
     * Power down FM
     *
     * @param type (0, FMRadio; 1, FMTransimitter)
     * @return (true, success; false, failed)
     */
    public static native boolean powerDown(int type);

    /**
     * tune to frequency
     *
     * @param frequency frequency(50KHZ, 87.55; 100KHZ, 87.5)
     * @return (true, success; false, failed)
     */
    public static native boolean tune(float frequency);

    /**
     * seek with frequency in direction
     *
     * @param frequency frequency(50KHZ, 87.55; 100KHZ, 87.5)
     * @param isUp      (true, next channel; false previous channel)
     * @return frequency(float)
     */
    public static native float seek(float frequency, boolean isUp);

    /**
     * Auto scan(from 87.50-108.00)
     *
     * @return The scan channel array(short)
     */
    public static native short[] autoScan();

    /**
     * Stop scan, also can stop seek, other native when scan should call stop
     * scan first, else will execute wait auto scan finish
     *
     * @return (true, can stop scan process; false, can't stop scan process)
     */
    public static native boolean stopScan();

    /**
     * Open or close rds fuction
     *
     * @param rdson The rdson (true, open; false, close)
     * @return rdsset
     */
    public static native int setRds(boolean rdson);

    /**
     * Read rds events
     *
     * @return rds event type
     */
    public static native short readRds();

    /**
     * Get program service(program name)
     *
     * @return The program name
     */
    public static native byte[] getPs();

    /**
     * Get radio text, RDS standard not support Chinese character
     *
     * @return The radio text(byte)
     */
    public static native byte[] getLrText();

    /**
     * Active alternative frequencies
     *
     * @return The frequency(float)
     */
    public static native short activeAf();

    /**
     * Mute or unmute FM voice
     *
     * @param mute (true, mute; false, unmute)
     * @return (true, success; false, failed)
     */
    public static native int setMute(boolean mute);

    /**
     * Inquiry if RDS is support in driver
     *
     * @return (1, support; 0, NOT support; -1, error)
     */
    public static native int isRdsSupport();

    /**
     * Switch antenna
     *
     * @param antenna antenna (0, long antenna, 1 short antenna)
     * @return (0, success; 1 failed; 2 not support)
     */
    public static native int switchAntenna(int antenna);

    /**
     * Set Fm status to tell native RX or TX is on
     *
     * @param which 0 is RX, 1 is TX
     * @param on    true is on
     * @return
     */
    public static native boolean setFmStatus(int which, boolean on);

    /**
     * Get the native Fm status
     *
     * @param which 0 is RX ON, 1 is TX ON, 2 is TX Scaning
     * @return true if dothing
     */
    public static native boolean getFmStatus(int which);

    // FM EM start

    /**
     * get rssi from hardware(use for engineer mode)
     *
     * @return rssi value
     */
    public static native int readRssi();

    /**
     * Inquiry if fm stereo mono(true, stereo; false mono)
     *
     * @return (true, stereo; false, mono)
     */
    public static native boolean stereoMono();

    /**
     * Force set to stero/mono mode
     *
     * @param isMono (true, mono; false, stereo)
     * @return (true, success; false, failed)
     */
    public static native boolean setStereoMono(boolean isMono);

    /**
     * Read cap array of short antenna
     *
     * @return cap array value
     */
    public static native short readCapArray();

    /**
     * read rds bler
     *
     * @return rds bler value
     */
    public static native short readRdsBler();

    /**
     * send variables to native, and get some variables return.
     *
     * @param val send to native
     * @return get value from native
     */
    public static native short[] emcmd(short[] val);

    /**
     * set RSSI, desense RSSI, mute gain soft
     *
     * @param index flag which will execute
     *              (0:rssi threshold,1:desense rssi threshold,2: SGM threshold)
     * @param value send to native
     * @return execute ok or not
     */
    public static native boolean emsetth(int index, int value);

    /**
     * get hardware version
     *
     * @return hardware version information array(0, ChipId; 1, EcoVersion; 2, PatchVersion; 3,
     * DSPVersion)
     */
    public static native int[] getHardwareVersion();
    // FM EM end
}
