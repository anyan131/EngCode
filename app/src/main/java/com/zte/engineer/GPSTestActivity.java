package com.zte.engineer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class GPSTestActivity extends ZteActivity {
	private static final String TAG = "GPSTestActivity";
	private TextView show;
	private TextView tv_gps, tv_satellites;
	private StringBuilder sb;
	private LocationManager location;
	boolean flag;
	private int count = 0;
	private static final String[] A = { "n/a", "fine", "coarse" };
	private static final String[] P = { "n/a", "low", "medium", "high" };
	private static final String[] S = { "out of service",
			"temporarily unavailable", "available" };
	

	private String best;
	Button success;
	Button failed;

	private void toggleGPS() {
		Intent gpsIntent = new Intent();
		gpsIntent.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");
		gpsIntent.addCategory(Intent.CATEGORY_ALTERNATIVE);
		gpsIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(this, 0, gpsIntent, 0).send();
		} catch (CanceledException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gps_test);
		init();
	}

	public void init() {
		success = (Button) findViewById(R.id.s_gps_test_pass);
		failed = (Button) findViewById(R.id.s_gps_test_false);
		success.setEnabled(false);
		success.setOnClickListener(this);
		failed.setOnClickListener(this);

		show = (TextView) findViewById(R.id.gps_show);
		tv_satellites = (TextView) this.findViewById(R.id.tv_satellites);
		tv_gps = (TextView) findViewById(R.id.gps_content);
		location = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		flag = location.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (flag) {
			show.setText(getString(R.string.gps_open));
			//dumpProviders();
		} else {
			show.setText(getString(R.string.gps_opening));
			Settings.Secure.setLocationProviderEnabled(getContentResolver(),
					LocationManager.GPS_PROVIDER, true);
			// toggleGPS();
			// Intent intent = new Intent();
			// intent.setAction(GPS_ON);
			// this.sendBroadcast(intent);
			// jessen added begin
			int timeout = 0;
			for (;;) {
				try {
					Thread.currentThread().sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// jessen added end
				timeout++;
				location = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				flag = location.isProviderEnabled(LocationManager.GPS_PROVIDER);
				if (flag) {
					show.setText(getString(R.string.gps_open));
					break;
				} else if (timeout > 3) {
					show.setText("Open gps Failed!");
					break;
				}
			}

			if (timeout < 4) {
				show.setText(getString(R.string.gps_open));
			}
		}

		String provider = LocationManager.GPS_PROVIDER;
		Location lo = location.getLastKnownLocation(provider);
		updateMsg(lo);

		LocationListener ll = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				String locInfo = updateMsg(location);
				System.out.println("---lzg locInfo="+locInfo);
				tv_gps.setText(null);
				tv_gps.setText(locInfo);
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {

			}

			@Override
			public void onProviderEnabled(String provider) {

			}

			@Override
			public void onProviderDisabled(String provider) {

			}

		};
		location.requestLocationUpdates(provider, 1000, 1, ll);
		location.addGpsStatusListener(statusListener);
	}

	private void log(String string) {
		show.append(string + "\n");
	}

	private void dumpProviders() {
		List<String> providers = location.getAllProviders();
		for (String provider : providers) {
			dumpProvider(provider);
		}
	}

	private void dumpProvider(String provider) {
		LocationProvider info = location.getProvider(provider);
		StringBuilder builder = new StringBuilder();
		builder.append("LocationProvider[")
				.append("name=")
				.append(info.getName())
				// .append(",getAccuracy=")
				// .append(A[info.getAccuracy()])
				// .append(",getPowerRequirement=")
				// .append(P[info.getPowerRequirement()])
				.append(",hasMonetaryCost=").append(info.hasMonetaryCost())
				.append(",requiresCell=").append(info.requiresCell())
				.append(",requiresNetwork=").append(info.requiresNetwork())
				.append(",requiresSatellite=").append(info.requiresSatellite())
				.append(",supportsAltitude=").append(info.supportsAltitude())
				.append(",supportsBearing=").append(info.supportsBearing())
				.append(",supportsSpeed=").append(info.supportsSpeed())
				.append("]");
		log(builder.toString());
	}

	void showGPS() {
		if (location.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Iterator<GpsSatellite> iterator = location.getGpsStatus(null)
					.getSatellites().iterator();
			while (iterator.hasNext()) {
				count++;
				iterator.next();
			}
			show.setText(getString(R.string.gps_open) + "\n"
					+ getString(R.string.gps_num) + count);
		} else {
			show.setText(getString(R.string.gps_no));
		}
	}

	private String updateMsg(Location loc) {
		sb = null;
		sb = new StringBuilder("Location info:\n");
		if (loc != null) {
			double lat = loc.getLatitude();
			double lng = loc.getLongitude();

			sb.append("Latitude:" + lat + "\nLongitude:" + lng);

			if (loc.hasAccuracy()) {
				sb.append("\nAccuracy:" + loc.getAccuracy());
			}

			if (loc.hasAltitude()) {
				sb.append("\nAltitude:" + loc.getAltitude() + "m");
			}

			if (loc.hasBearing()) {//
				sb.append("\nDirection:" + loc.getBearing());
			}

			if (loc.hasSpeed()) {
				if (loc.getSpeed() * 3.6 < 5) {
					sb.append("\nSpeed:0.0km/h");
				} else {
					sb.append("\nSpeed:" + loc.getSpeed() * 3.6 + "km/h");
				}

			}
		} else {
			sb.append("No Location info!");
		}

		return sb.toString();
	}

	private List<GpsSatellite> numSatelliteList = new ArrayList<GpsSatellite>();

	private final GpsStatus.Listener statusListener = new GpsStatus.Listener() {
		public void onGpsStatusChanged(int event) {
			LocationManager location = (LocationManager) GPSTestActivity.this
					.getSystemService(Context.LOCATION_SERVICE);
			GpsStatus status = location.getGpsStatus(null);
			String satelliteInfo = updateGpsStatus(event, status);
			tv_satellites.setText(null);
			tv_satellites.setText(satelliteInfo);
		}
	};

	private String updateGpsStatus(int event, GpsStatus status) {
		StringBuilder sb2 = new StringBuilder();
		if (status == null) {
			//Log.d(TAG, "houjian maxSatellites = 0");
			sb2.append("Satellites:" + 0);
		} else if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
			int maxSatellites = status.getMaxSatellites();
			//Log.d(TAG, "houjian maxSatellites = "+maxSatellites);
			Iterator<GpsSatellite> it = status.getSatellites().iterator();
			numSatelliteList.clear();
			int count = 0;
			while (it.hasNext() && count <= maxSatellites) {
				GpsSatellite s = it.next();
				numSatelliteList.add(s);
				count++;
			}
			sb2.append("Satellites:" + numSatelliteList.size());
			//add by lzg
			if(count>0){
				success.setEnabled(true);
			}
			//end lzg
		}
		return sb2.toString();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Settings.Secure.setLocationProviderEnabled(getContentResolver(),
				LocationManager.GPS_PROVIDER, false);
		Settings.Secure.putInt(getContentResolver(), Settings.Secure.LOCATION_MODE,
                		3);
		Intent intent = new Intent();
		intent.setAction(GPS_OFF);
		this.sendBroadcast(intent);
	}
	
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.s_gps_test_pass:
			finishSelf(RESULT_PASS);
			break;
		case R.id.s_gps_test_false:
			finishSelf(RESULT_FALSE);
			break;
		default:
			finishSelf(RESULT_PASS);
			break;
		}
	}

	private static String GPS_ON = "com.ipro.gps.on";
	private static String GPS_OFF = "com.ipro.gps.off";

}
