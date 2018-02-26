package com.zte.engineer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2018/2/6.
 */

public class AlexGPSTest extends Activity implements View.OnClickListener {

    LocationManager manager;
    TextView num_of_sate;
    Button passBtn, failBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alextao_gps_test);
        num_of_sate = (TextView) findViewById(R.id.num_of_sate);
        openGPSSettings();
        passBtn = (Button) findViewById(R.id.gps_pass);
        failBtn = (Button) findViewById(R.id.gps_fail);
        passBtn.setOnClickListener(this);
        failBtn.setOnClickListener(this);
        manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        String provider = LocationManager.GPS_PROVIDER;
        if (manager != null) {
            Location location = manager.getLastKnownLocation(provider);
        }

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

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

        manager.requestLocationUpdates(provider, 2000, 1, locationListener);
        manager.addGpsStatusListener(statusListener);
    }


    /**
     * here we need check the user had open the location manger service or not .
     * if not we need to give them an alarm to open or cant use this application.
     */
    private void openGPSSettings() {
        LocationManager alm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        assert alm != null;
        if (alm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Toast.makeText(this, "GPS enabled", Toast.LENGTH_SHORT).show();
            return;
        }
       Toast.makeText(getApplication(), "please enable GPS!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_SECURITY_SETTINGS);
        startActivityForResult(intent, 0);

    }

    private List<GpsSatellite> satelliteList = new ArrayList<>();
    private final GpsStatus.Listener statusListener = new GpsStatus.Listener() {
        @Override
        public void onGpsStatusChanged(int event) {
            // LocationManager manager1 = (LocationManager) AlexGPSTest.this.getSystemService(Context.LOCATION_SERVICE);
            GpsStatus status = manager.getGpsStatus(null);
            String res = updateGpsStatus(event, status);
            // Log.d("Alextao",res);
            num_of_sate.setText(res);

        }
    };

    private String updateGpsStatus(int event, GpsStatus status) {
        StringBuilder sb = new StringBuilder();
        if (status == null) {
            sb.append("搜索到卫星个数" + 0);
        } else if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
            int maxSatelites = status.getMaxSatellites();
            Iterator<GpsSatellite> it = status.getSatellites().iterator();
            satelliteList.clear();
            int count = 0;
            while (it.hasNext() && count < maxSatelites) {
                GpsSatellite satellite = it.next();
                satelliteList.add(satellite);
                count++;
            }
            sb.append("搜索到卫星个数" + satelliteList.size());
        }

        return sb.toString();
    }

    @Override
    protected void onDestroy() {
        if (manager != null)
            manager.removeGpsStatusListener(statusListener);

        manager = null;
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v == passBtn) {
            setResult(10);
            finish();
        }
        if (v == failBtn) {
            setResult(20);
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        setResult(20);
        finish();
        super.onBackPressed();
    }
}
