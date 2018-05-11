package com.zte.engineer;

import android.app.Activity;
import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zte.engineer.GPSUtils.NmeaParser;
import com.zte.engineer.GPSUtils.SatelliteInfo;
import com.zte.engineer.GPSUtils.SatelliteInfoManager;
import com.zte.engineer.GPSUtils.SatelliteSignalChartView;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Administrator on 2018/2/27.
 */

public class AlexNewGPSTest extends Activity {

    private Button pass, fail;
    private final static String TAG = "GPSMainActivity";
    private SatelliteSignalChartView chartView;
    private LocationManager mLocationManager;
    private SatelliteInfoManager satelliteInfoManager;

    private ArrayList<GpsSatellite> satellites = new ArrayList<>();


    private NmeaParser nmeaParser = null;
    private NmeaParser.NmeaUpdateViewListener nmeaUpdateViewListener;

    public final GpsStatus.NmeaListener nmeaListener = new GpsStatus.NmeaListener() {
        @Override
        public void onNmeaReceived(long timestamp, String nmea) {
            NmeaParser.getNmeaParser().parse(nmea);
        }
    };

    GpsStatus.Listener listener = new GpsStatus.Listener() {
        @Override
        public void onGpsStatusChanged(int event) {
            GpsStatus gpsStatus = mLocationManager.getGpsStatus(null);
            satellites.clear();
            int count = 0;
            while (gpsStatus.getSatellites().iterator().hasNext() && count < gpsStatus.getMaxSatellites()) {
                GpsSatellite satellite = gpsStatus.getSatellites().iterator().next();
                satellites.add(satellite);
                count++;
            }
            Log.d(TAG, "onGpsStatusChanged: "+satellites.size());
            if (satellites.size() ==0){
                pass.setEnabled(false);
            }else {
                pass.setEnabled(true);
            }
        }
    };

    public final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "onLocationChanged: start");


        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    //this listener may about the view.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: enter the function ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alextao_gps_test);
        //here we open the strictMode in debug build.
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectNetwork().build());
        }


        satelliteInfoManager = new SatelliteInfoManager();
        //initial widgets.
        chartView = (SatelliteSignalChartView) findViewById(R.id.chartView1);
        pass = (Button) findViewById(R.id.gps_pass);
        pass.setEnabled(false);
        fail = (Button) findViewById(R.id.gps_fail);
        if (nmeaParser == null) {
            nmeaParser = NmeaParser.getNmeaParser();
        }
        nmeaUpdateViewListener = new NmeaParser.NmeaUpdateViewListener() {
            @Override
            public void onViewUpdateNotify() {
                Log.d(TAG, "onViewUpdateNotify: ");
                setSatelliteStatus(nmeaParser.getSatelliteList().iterator());
            }
        };
        nmeaParser.addSVUpdateListener(nmeaUpdateViewListener);

        try {
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (mLocationManager != null) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                        mLocationListener);


                mLocationManager.addNmeaListener(nmeaListener);
                mLocationManager.addGpsStatusListener(listener);
            } else {
                Log.w(TAG, "onCreate: locationManager failed.");
            }
        } catch (SecurityException e) {
            Toast.makeText(this, "Security Exception", Toast.LENGTH_SHORT).show();
            Log.w(TAG, e.getMessage());
        }
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(10);
                finish();
            }
        });
        fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(20);
                finish();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationManager.removeUpdates(mLocationListener);
        mLocationManager.removeNmeaListener(nmeaListener);
        mLocationManager.removeGpsStatusListener(listener);
        nmeaParser.removeSVUpdateListener(nmeaUpdateViewListener);

    }

    private void setSatelliteStatus(Iterator<SatelliteInfo> infoIterator) {
        if (infoIterator == null) {
            satelliteInfoManager.clearSatelliteInfos();
        } else {
            satelliteInfoManager.updateSatelliteInfo(nmeaParser.getSatelliteList().iterator());

        }
        chartView.requestUpdate(satelliteInfoManager);

    }

    @Override
    public void onBackPressed() {
        setResult(20);
        finish();
        super.onBackPressed();
    }
}
