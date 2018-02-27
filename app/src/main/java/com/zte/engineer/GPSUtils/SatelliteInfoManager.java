package com.zte.engineer.GPSUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2018/2/27.
 */

public class SatelliteInfoManager {

    private static final String TAG = "SatelliteInfoManager";

    public static final int PRN_ANY = -1;
    public static final int PRN_ALL = -2;

    List<SatelliteInfo> mSatelliteInfoList;

    public SatelliteInfoManager() {
        mSatelliteInfoList = new ArrayList<>();
    }

    public void updateSatelliteInfo(Iterator<SatelliteInfo> infoIterator) {
        if (mSatelliteInfoList != null) {
            mSatelliteInfoList.clear();
        } else {
            mSatelliteInfoList = new ArrayList<>();
        }
        while (infoIterator.hasNext()) {
            mSatelliteInfoList.add(infoIterator.next());
        }

    }

    public List<SatelliteInfo> getmSatelliteInfoList() {
        return mSatelliteInfoList;
    }

    public SatelliteInfo getSatelliteInfo(int prn) {
        for (SatelliteInfo satelliteInfo : mSatelliteInfoList) {
            if (satelliteInfo.prn == prn)
                return satelliteInfo;
        }
        return null;
    }

    //override the toString method,this is to print
    // the whole satellite information.
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{Satellite Count:");
        builder.append(mSatelliteInfoList.size());
        for (SatelliteInfo satelliteInfo : mSatelliteInfoList) {
            builder.append(satelliteInfo.toString());
        }
        builder.append("}");
        return builder.toString();
    }

    public void clearSatelliteInfos() {
        mSatelliteInfoList.clear();
    }

    public boolean isUseInFix(int prn) {
        boolean result = false;
        if (prn == PRN_ALL && mSatelliteInfoList.size() > 0) {
            result = true;
        }
        for (SatelliteInfo satelliteInfo : mSatelliteInfoList) {
            if (prn == PRN_ALL) {
                if (!satelliteInfo.usedInFix) {
                    result = false;
                    break;
                } else if (prn == PRN_ANY) {
                    if (satelliteInfo.usedInFix) {
                        result = true;
                        break;
                    }
                } else if (satelliteInfo.prn == prn) {
                    result = satelliteInfo.usedInFix;
                    break;
                }

            }
        }
        return result;
    }


}
