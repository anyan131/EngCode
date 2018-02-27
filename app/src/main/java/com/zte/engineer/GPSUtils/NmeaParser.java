package com.zte.engineer.GPSUtils;

import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2018/2/27.
 * This class is for parse the NMEA data.
 * and we can update the view.
 */

public class NmeaParser {

    //for logging
    private static final String TAG = "NmeaParser";
    private static final int SV_UPDATE = 1;
    private static final String DELIMITER = ",";
    //four talkers
    //GPS,伽利略，格洛纳斯，北斗。
    private final String[] mTalker = {"GP", "GL", "GA", "BD"};

    private static NmeaParser nmeaParser = null;
    private int mSatelliteCount;
    private HashMap<String, ArrayList> mSatelliteInfoMap;
    private HashMap<String, SVExtraInfo> mSVInfoMap;
    private HashMap<String, Location> mLocationRecordMap;

    private boolean mbUpdated = false;
    private String mCurrentTalker = "none";

    private final ArrayList<NmeaUpdateViewListener> mListeners = new ArrayList<>();

    private HashMap<String, ArrayList<Integer>> mUsedInFixIdMap = new HashMap<>();
    //parser state.let's init this value to be uninit.
    private int mParseState = STATE_UNINIT;
    private static final int STATE_UNINIT = -1;
    private static final int STATE_PARSE_GGA = 1;
    private static final int STATE_PARSE_GSA = 2;
    private static final int STATE_PARSE_GSV = 3;
    private static final int STATE_PARSE_RMC = 4;
    private static final int STATE_PARSE_OTHERS = 5;


    //here is the singleton pattern.
    public static NmeaParser getNmeaParser() {
        if (nmeaParser == null) {
            nmeaParser = new NmeaParser();
        }
        return nmeaParser;
    }

    /**
     * the constructor of the parser. we made it private for singleton.
     */
    private NmeaParser() {
        //initialize some fields.
        this.mSatelliteInfoMap = new HashMap<>();
        this.mSVInfoMap = new HashMap<>();
        this.mLocationRecordMap = new HashMap<>();
        this.mSatelliteCount = 0;

        for (String s : mTalker) {
            ArrayList<SatelliteInfo> mSatelliteList = new ArrayList<>();
            mSatelliteInfoMap.put(s, mSatelliteList);

            SVExtraInfo svExtraInfo = new SVExtraInfo();
            mSVInfoMap.put(s, svExtraInfo);

            Location location = new Location(LocationManager.GPS_PROVIDER);
            mLocationRecordMap.put(s, location);
        }

    }


    public class SVExtraInfo {

        public String mPdop = "";
        public String mHdop = "";
        public String mVdop = "";
        public String mfixtype = "";

        private SVExtraInfo() {
        }
    }

    /**
     * this is for our activity to callback.
     */
    public interface NmeaUpdateViewListener {
        void onViewUpdateNotify();
    }


    /**
     * Method below.
     * @author AlexTao
     *
     * */
    /**
     * @param prn the prn of the satellite.
     * @return is used in fix or not.
     */
    private boolean isUsedInFix(int prn) {
        ArrayList<Integer> usedInFixIdList = mUsedInFixIdMap.get(mCurrentTalker);
        if (usedInFixIdList == null) {
            usedInFixIdList = new ArrayList<>();
            mUsedInFixIdMap.put(mCurrentTalker, usedInFixIdList);
            return false;
        }
        for (Integer id : usedInFixIdList) {
            if (prn == id) {
                return true;
            }
        }
        return false;
    }

    /**
     * add the satellite prn into the list
     *
     * @param prn the prn of the satellite
     */
    private void addUsedInFixID(int prn) {
        ArrayList<Integer> usedInFixIdList = mUsedInFixIdMap.get(mCurrentTalker);
        if (usedInFixIdList == null) {
            usedInFixIdList = new ArrayList<>();
            mUsedInFixIdMap.put(mCurrentTalker, usedInFixIdList);
        }
        usedInFixIdList.add(prn);
    }

    /**
     * clear the id list which is used in fix.
     */
    private void clearUsedInFixList() {
        ArrayList<Integer> usedInFixIdList = mUsedInFixIdMap.get(mCurrentTalker);
        if (usedInFixIdList == null) {
            usedInFixIdList = new ArrayList<>();
            mUsedInFixIdMap.put(mCurrentTalker, usedInFixIdList);
            return;
        }
        usedInFixIdList.clear();
    }

    /**
     * @return get satellite count.
     */
    public synchronized int getmSatelliteCount() {
        return mSatelliteCount;
    }

    /**
     * @return get all the satellite info. It is stored in a list.
     */
    public ArrayList<SatelliteInfo> getSatelliteList() {
        ArrayList<SatelliteInfo> mList = new ArrayList<>();
        for (ArrayList<SatelliteInfo> satelliteInfos : mSatelliteInfoMap.values()) {
            mList.addAll(satelliteInfos);
        }
        return mList;
    }

    /**
     * clear all the satellite info.actually,call it clear the list which stored the
     * info.
     */
    public void clearSatelliteLists() {
        for (ArrayList<SatelliteInfo> satelliteInfos : mSatelliteInfoMap.values()) {
            satelliteInfos.clear();
        }
    }

    public synchronized boolean isViewNeedUpdate() {
        boolean result = mbUpdated;
        if (mbUpdated) {
            result ^= true;
        }
        return result;
    }

    /**
     * @param talker
     * @return get the satellite info.
     */
    private ArrayList<SatelliteInfo> getSatelliteInfoList(String talker) {
        ArrayList<SatelliteInfo> satelliteInfos = null;
        if (mSatelliteInfoMap.containsKey(talker)) {
            satelliteInfos = mSatelliteInfoMap.get(talker);
        } else {
            Log.w(TAG, "this talker:" + talker + " has no satellite info.");
        }
        return satelliteInfos;
    }


    /**
     * @param talker clear the whole satellite info. Actually we set the
     *               {@link SatelliteInfo#usedInFix} to false.
     */
    private void clearSatelliteInfoList(String talker) {
        ArrayList<SatelliteInfo> satelliteInfos = getSatelliteInfoList(talker);
        if (satelliteInfos != null) {
            for (SatelliteInfo infos : satelliteInfos) {
                infos.usedInFix = false;
            }
        }
    }

    /**
     * @param talker
     * @return the location of the talker.
     */
    private Location getTalkerLocation(String talker) {
        Location location = null;
        if (mLocationRecordMap.containsKey(talker)) {
            location = mLocationRecordMap.get(talker);
        } else {
            Log.w(TAG, "getTalkerLocation error,this:" + talker + "has no location");
        }
        return location;
    }

    /**
     * @param talker
     * @return the SVExtraInfo of the talker.
     */
    private SVExtraInfo getTalkerExtraInfo(String talker) {
        SVExtraInfo svExtraInfo = null;
        if (mSVInfoMap.containsKey(talker)) {
            svExtraInfo = mSVInfoMap.get(talker);
        } else {
            Log.w(TAG, "getTalkerExtraInfo: error,this:" + talker + "has no SVExtraInfo");
        }
        return svExtraInfo;
    }

    private String checkTalker(String record) {
        String result = "none";
        for (String s : mTalker) {
            if (record.contains(s)) {
                result = s;
                break;
            }

        }
        return result;
    }

    private String removeFirstZero(String record) {
        String result = record;
        int ind = 0;
        while (record.charAt(ind++) == '0') ;
        if (ind != 0) {
            result = record.substring(--ind);
        }
        //log("ori:"+ record + " res:" + result);
        return result;
    }

    /**
     * <$GPRMC>
     * Recommended minimum specific GPS/Transit data
     * <p>
     * eg. $GPRMC,hhmmss.ss,A,llll.ll,a,yyyyy.yy,a,x.x,x.x,ddmmyy,x.x,a*hh
     * 1    = UTC of position fix
     * 2    = Data status (V=navigation receiver warning)
     * 3    = Latitude of fix
     * 4    = N or S
     * 5    = Longitude of fix
     * 6    = E or W
     * 7    = Speed over ground in knots
     * 8    = Track made good in degrees True
     * 9    = UT date
     * 10   = Magnetic variation degrees (Easterly var. subtracts from true course)
     * 11   = E or W
     * 12   = Checksum
     */
    private synchronized void parseRMC(String record) {

        String[] values = split(record);

        // First value = $GPRMC
        // Date time of fix (eg. 041107.000)
        // String dateTimeOfFix = values[1];

        // Warning (eg. A:valid, V:warning)
        final String warning = values[2];

        // Latitude (eg. 6131.2028)
        final String latitude = values[3];

        // Lattitude direction (eg. N/S)
        final String latitudeDirection = values[4];

        // Longitude (eg. 02356.8782)
        final String longitude = values[5];

        // Longitude direction (eg. E/W)
        final String longitudeDirection = values[6];

        // Ground speed (eg. 18.28[knots])
        final double groundSpeed = parseDouble(values[7]);

        // Course (198.00)
        final String courseString = values[8];

        double longitudeDouble = 0.0;
        double latitudeDouble = 0.0;
        double speed = -2.0;
        if (longitude.length() > 0 && latitude.length() > 0) {
            longitudeDouble = parseDouble(longitude);
            if (longitudeDirection.equals("E") == false) {
                longitudeDouble = -longitudeDouble;
            }

            latitudeDouble = parseDouble(latitude);
            if (latitudeDirection.equals("N") == false) {
                latitudeDouble = -latitudeDouble;
            }
        } else {
            Log.e(TAG, "Error with lat or long");
        }

        int course = 0;
        if (courseString.length() > 0) {
            try {
                course = (int) parseDouble(courseString);
            } catch (Exception e) {
                course = 180;
            }
        }


        // if we have a speed value, work out the Miles Per Hour
        // if we have a speed value, work out the Km Per Hour
        if (groundSpeed > 0) {
            // km/h = knots * 1.852
            speed = ((groundSpeed) * 1.852);
        }
        // A negative speed doesn't make sense.
        if (speed < 0) {
            speed = 0;
        }

        if (warning.equals("A")) {
            Location loc = getTalkerLocation(mCurrentTalker);
            if (loc != null) {
                loc.setLatitude(latitudeDouble);
                loc.setLongitude(longitudeDouble);
                loc.setBearing(course);
                loc.setSpeed((float) speed * 1000);
                mLocationRecordMap.put(mCurrentTalker, loc);
            }
        } else {
            //log("$GPRMC: Warning NOT A, so no position written: (" + warning + ")");
        }

    }

    /**
     * <$GPGSA>
     * GPS DOP and active satellites
     * <p>
     * eg1. $GPGSA,A,3,,,,,,16,18,,22,24,,,3.6,2.1,2.2*3C
     * 1    = Mode:
     * M = Manual, forced to operate in 2D or 3D
     * A = Automatic, 3D/2D
     * 2 = Mode:
     * 1=Fix not available
     * 2=2D
     * 3=3D
     * 3-14 = IDs of SVs used in position fix (null for unused fields)
     * 15   = Position Dilution of Precision (PDOP)
     * 16   = Horizontal Dilution of Precision (HDOP)
     * 17   = Vertical Dilution of Precision (VDOP)
     *
     * @param record
     */
    private synchronized void parseGSA(String record) {
        String[] values = split(record);
        //String mode=values[1];
        SVExtraInfo mInfo = getTalkerExtraInfo(mCurrentTalker);
        clearUsedInFixList();
        if (mInfo != null && values.length >= 17) {
            if (values[2].equals("1")) {
                //no fix
                clearSatelliteInfoList(mCurrentTalker);
                return;
            }
            int[] svid = new int[13];
            mInfo.mfixtype = values[2];
            ArrayList<SatelliteInfo> SVlist = getSatelliteInfoList(mCurrentTalker);
            clearSatelliteInfoList(mCurrentTalker);
            for (int i = 2; i < 15; i++) {
                int prn = parseInt(values[i]);
                Log.d(TAG, "parseGSA get prn = " + prn);
                if (prn > 0) {
                    //Start: Add 200 for BD system
                    if ("BD".equals(mCurrentTalker)) {
                        prn += 200;
                        Log.d(TAG, "For BD prn = " + prn);
                    }
                    //End: Add 200 for BD system
                    addUsedInFixID(prn);
                }
            }

            mInfo.mPdop = values[15];
            mInfo.mHdop = values[16];
            mInfo.mVdop = values[17];
        } else {
            // no fix
            clearSatelliteInfoList(mCurrentTalker);
        }
    }

    /**
     * <$GGA>
     * Global Positioning System Fix Data
     * eg3. $GPGGA,hhmmss.ss,llll.ll,a,yyyyy.yy,a,x,xx,x.x,x.x,M,x.x,M,x.x,xxxx*hh
     * 1    = UTC of Position
     * 2    = Latitude
     * 3    = N or S
     * 4    = Longitude
     * 5    = E or W
     * 6    = GPS quality indicator (0=invalid; 1=GPS fix; 2=Diff. GPS fix)
     * 7    = Number of satellites in use [not those in view]
     * 8    = Horizontal dilution of position
     * 9    = Antenna altitude above/below mean sea level (geoid)
     * 10   = Meters  (Antenna height unit)
     * 11   = Geoidal separation (Diff. between WGS-84 earth ellipsoid and
     * mean sea level.  -=geoid is below WGS-84 ellipsoid)
     * 12   = Meters  (Units of geoidal separation)
     * 13   = Age in seconds since last update from diff. reference station
     * 14   = Diff. reference station ID#
     * 15   = Checksum
     */
    private synchronized void parseGGA(String record) {
        String[] values = split(record);
        clearSatelliteLists();
        Location mInfo = getTalkerLocation(mCurrentTalker);
        long utcTime = (new Double(parseDouble(values[1]))).longValue();
        double lat = parseDouble(values[2]);
        double longt = parseDouble(values[4]);
        double alti = parseDouble(values[9]);
        if (values[3].equals("N") == false) {
            lat = -lat;
        }

        if (values[5].equals("E") == false) {
            longt = -longt;
        }

        if (mInfo != null) {
            mInfo.setTime(utcTime);
            mInfo.setLatitude(lat);
            mInfo.setLongitude(longt);
            mInfo.setAltitude(alti);
        }
    }

    /**
     * <$GPGSV>
     * GPS Satellites in view
     * <p>
     * eg:$GPGSV,1,1,13,02,02,213,,03,-3,000,,11,00,121,,14,13,172,05*67
     * 1    = Total number of messages of this type in this cycle, A: clean all SV
     * 2    = Message number
     * 3    = Total number of SVs in view
     * 4    = SV PRN number
     * 5    = Elevation in degrees, 90 maximum
     * 6    = Azimuth, degrees from true north, 000 to 359
     * 7    = SNR, 00-99 dB (null when not tracking)
     * 8-11 = Information about second SV, same as field 4-7
     * 12-15= Information about third SV, same as field 4-7
     * 16-19= Information about fourth SV, same as field 4-7
     */
    private synchronized void parseGSV(String record) {
        String[] values = split(record);
        ArrayList<SatelliteInfo> SVlist = getSatelliteInfoList(mCurrentTalker);

        if (SVlist == null) {
            Log.d(TAG, "parseGSV get SVlist Error" + SVlist + " Current Talker:" + mCurrentTalker);
            return;
        }

        int mTotalNum = parseInt(values[1]);
        int mMsgInd = parseInt(values[2]);

        if (mTotalNum > 0 && mMsgInd == 1) {
            //clear all SV record
            SVlist.clear();
            mSatelliteInfoMap.put(mCurrentTalker, SVlist);
        }

        int index = 4;
        while (index + 3 < values.length) {
            int satelliteNumber = parseInt(values[index++]);
            Log.d(TAG, "satelliteNumber = " + satelliteNumber);
            float elevation = parseFloat(values[index++]);
            float azimuth = parseFloat(values[index++]);
            float satelliteSnr = 0;
            if (values[index].contains("*")) {
                String[] mStrl = values[index].split("\\*");
                ;
                satelliteSnr = parseFloat(mStrl[0]);
                index++;
            } else {
                satelliteSnr = parseFloat(values[index++]);
            }

            if (satelliteNumber > 0) {
                //Start: Add 200 for BD system
                if ("BD".equals(mCurrentTalker)) {
                    satelliteNumber += 200;
                    Log.d(TAG, "For BD satelliteNumber = " + satelliteNumber);
                }
                //End: Add 200 for BD system
                SatelliteInfo sat = new SatelliteInfo(
                        satelliteNumber, checkTalkerColor(mCurrentTalker));
                sat.snr = satelliteSnr;
                sat.elevation = elevation;
                sat.azimuth = azimuth;
                if (isUsedInFix(satelliteNumber)) {
                    sat.usedInFix = true;
                }
                SVlist.add(sat);
            }
        }

        if (values[1].equals(values[2])) {
            //report location update
            Log.d(TAG, "mSatelliteInfoMap add svlist : " + mCurrentTalker + " size:" + SVlist.size());
            mSatelliteInfoMap.put(mCurrentTalker, SVlist);
        }
    }

    private int checkTalkerColor(String record) {
        int result = 0xffff0000; //red
        if (record.equals("GP")) {
            result = 0xff00ffff; //cyan
        } else if (record.equals("GL")) {
            result = 0xffffff00; //yellow
        } else if (record.equals("GA")) {
            result = 0xffffffff; //white
        } else if (record.equals("BD")) {
            result = 0xff0000ff; //blue
        }
        return result;
    }


    private String[] split(String record) {
        String[] result = null;
        try {
            String delims = "[,]";
            result = record.split(delims);
        } catch (Exception e) {
            Log.d("nmeaParser", "split:" + e);
        }
        return result;
    }

    public int parseInt(String str) {
        int d = 0;
        if (str.equals("")) {
            return d;
        }
        String mStr = removeFirstZero(str);
        try {
            d = Integer.valueOf(mStr);
        } catch (Exception e) {
            Log.d("nmeaParser", "parseDouble:" + e);
        }
        return d;
    }

    public double parseDouble(String str) {
        double d = 0;
        if (str.equals("")) {
            return d;
        }
        String mStr = removeFirstZero(str);
        try {
            d = Double.parseDouble(mStr);
        } catch (Exception e) {
            Log.d("nmeaParser", "parseDouble:" + e);
        }
        return d;
    }

    public float parseFloat(String str) {
        float d = 0;
        if (str.equals("")) {
            return d;
        }
        String mStr = removeFirstZero(str);
        try {
            d = Float.parseFloat(mStr);
        } catch (Exception e) {
            Log.d("nmeaParser", "parseFloat:" + e);
        }
        return d;
    }


    private void reportSVUpdate() {
        synchronized (mListeners) {
            int size = mListeners.size();
            for (int i = 0; i < size; i++) {
                NmeaUpdateViewListener listener = mListeners.get(i);
                listener.onViewUpdateNotify();
            }
        }
    }

    public void addSVUpdateListener(NmeaUpdateViewListener l) {
        synchronized (mListeners) {
            mListeners.add(l);
        }
    }

    public void removeSVUpdateListener(NmeaUpdateViewListener l) {
        synchronized (mListeners) {
            mListeners.remove(l);
        }
    }


    private void sendMessage(int what) {
        Message m = new Message();
        m.what = what;
        mHandler.sendMessage(m);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SV_UPDATE: //timer update
                    reportSVUpdate();
                    break;
                default:
                    Log.d(TAG, "WARNING: unknown handle event recv!!");
                    break;
            }
        }
    };

    /**
     * Parse GPS position
     */
    public synchronized void parse(String record) {
        mCurrentTalker = checkTalker(record);
        /*
         * only parse all GSV sentences, then notify user
         */
        if (mParseState == STATE_PARSE_GSV && !record.contains("GSV")) {
            sendMessage(SV_UPDATE);
        }
        if (record.contains("RMC")) {
            mParseState = STATE_PARSE_RMC;
            try {
                parseRMC(record);
            } catch (Exception e) {
                Log.d(TAG, "Exception in parseRMC()");
            }
        } else if (record.contains("GSA")) {
            mParseState = STATE_PARSE_GSA;
            try {
                parseGSA(record);
            } catch (Exception e) {
                Log.d(TAG, "Exception in parseGSA()" + e);
            }
        } else if (record.contains("GGA")) {
            mParseState = STATE_PARSE_GGA;
            try {
                parseGGA(record);
            } catch (Exception e) {
                Log.d(TAG, "Exception in parseGGA()" + e);
            }
        } else if (record.contains("GSV")) {
            mParseState = STATE_PARSE_GSV;
            try {
                parseGSV(record);
            } catch (Exception e) {
                Log.d(TAG, "Exception in parseGSV()" + e);
            }
        } else {
            mParseState = STATE_PARSE_OTHERS;
            //log("undefined format");
        }

    }


}
