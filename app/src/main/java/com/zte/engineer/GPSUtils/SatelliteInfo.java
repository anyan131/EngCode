package com.zte.engineer.GPSUtils;

/**
 * Created by Administrator on 2018/2/27.
 */

public class SatelliteInfo {
    int prn;
    float snr;
    float elevation;
    float azimuth;
    boolean usedInFix;
    int color;

    public SatelliteInfo(int prn, int color) {
        this.prn = prn;
        this.color = color;
    }

    public String toString() {
        return "[" + prn + ", " +
                snr + ", " +
                elevation + ", " +
                azimuth + ", " +
                usedInFix + "]";

    }

}

