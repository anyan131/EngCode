/**
 * type                                 sensor
 * 1(Sensor.TYPE_ACCELEROMETER)         Accelerometer
 * 2(Sensor.TYPE_MAGNETIC_FIELD)        Magnetic
 * 3(Sensor.TYPE_ORIENTATION)           Orientation
 * 4(Sensor.TYPE_GYROSCOPE)             GyroScope
 * 5(Sensor.TYPE_LIGHT)                 Light
 * 8(Sensor.TYPE_PROXIMITY)             Proximity
 * 9(Sensor.TYPE_GRAVITY)               Gravity
 * 10(Sensor.TYPE_LINEAR_ACCELERATION)  Linear Acceleration
 * 11(Sensor.TYPE_ROTATION_VECTOR)      Rotation vector
 */
package com.zte.engineer;

import java.util.List;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout;

public class SensorTest extends ZteActivity {

    private boolean g_sensor_visible = true;
    private boolean gyroscope_visible = false;
    private boolean magnetic_visible = true;
    private boolean light_visible = true;
    private boolean proximity_visible = true;
    LinearLayout g_sensor_layout, gyroscope_layout, magnetic_layout, light_layout, proximity_layout;
	private float x, y, z;
	private SensorManager sensorMgr;
	private SensorEventListener lsn;
	TextView GsensorX, GsensorY, GsensorZ;
	TextView MagneticX, MagneticY, MagneticZ;
	TextView GyroscopeX, GyroscopeY, GyroscopeZ;
	TextView LightView;
	TextView ProximityView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.sensortest);

		sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        
        light_visible = sensorMgr.getDefaultSensor(Sensor.TYPE_LIGHT) != null;
        proximity_visible = sensorMgr.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null;

		initUi();

		initSensorListener();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		// initSensorListener();
	}

	private void initUi() {
		((Button) findViewById(R.id.sensor_pass)).setOnClickListener(this);
		((Button) findViewById(R.id.sensor_false)).setOnClickListener(this);

        g_sensor_layout = (LinearLayout) findViewById(R.id.g_sensor_layout);
        gyroscope_layout = (LinearLayout) findViewById(R.id.gyroscope_layout);
        magnetic_layout = (LinearLayout) findViewById(R.id.magnetic_layout);
        light_layout = (LinearLayout) findViewById(R.id.light_layout);
        proximity_layout = (LinearLayout) findViewById(R.id.proximity_layout);
        if(!g_sensor_visible)
            g_sensor_layout.setVisibility(View.GONE);
        if(!gyroscope_visible)
            gyroscope_layout.setVisibility(View.GONE);
        if(!magnetic_visible)
            magnetic_layout.setVisibility(View.GONE);
        if(!light_visible)
            light_layout.setVisibility(View.GONE);
        if(!proximity_visible)
            proximity_layout.setVisibility(View.GONE);
        
		GsensorX = (TextView) findViewById(R.id.g_sensor_x);
		GsensorY = (TextView) findViewById(R.id.g_sensor_y);
		GsensorZ = (TextView) findViewById(R.id.g_sensor_z);

		MagneticX = (TextView) findViewById(R.id.magnetic_x);
		MagneticY = (TextView) findViewById(R.id.magnetic_y);
		MagneticZ = (TextView) findViewById(R.id.magnetic_z);

		GyroscopeX = (TextView) findViewById(R.id.gyroscope_x);
		GyroscopeY = (TextView) findViewById(R.id.gyroscope_y);
		GyroscopeZ = (TextView) findViewById(R.id.gyroscope_z);

		LightView = (TextView) findViewById(R.id.light_lux);

		ProximityView = (TextView) findViewById(R.id.proximity);
        
	}

	private void initSensorListener() {
		List<Sensor> sensors = sensorMgr.getSensorList(Sensor.TYPE_ALL);

		lsn = new SensorEventListener() {
			public void onSensorChanged(SensorEvent e) {
				x = e.values[SensorManager.DATA_X];
				y = e.values[SensorManager.DATA_Y];
				z = e.values[SensorManager.DATA_Z];

				switch (e.sensor.getType()) {
				case Sensor.TYPE_ACCELEROMETER: {
					GsensorX.setText(String.valueOf(x));
					GsensorY.setText(String.valueOf(y));
					GsensorZ.setText(String.valueOf(z));
				}
					break;

				case Sensor.TYPE_MAGNETIC_FIELD: {
					MagneticX.setText(String.valueOf(x));
					MagneticY.setText(String.valueOf(y));
					MagneticZ.setText(String.valueOf(z));
				}
					break;

				case Sensor.TYPE_GYROSCOPE: {
					GyroscopeX.setText(String.valueOf(x));
					GyroscopeY.setText(String.valueOf(y));
					GyroscopeZ.setText(String.valueOf(z));
				}
					break;

				case Sensor.TYPE_LIGHT: {
					LightView.setText(String.format(
							getString(R.string.light_is), String.valueOf(x)));
				}
					break;

				case Sensor.TYPE_PROXIMITY: {
					ProximityView.setText(String.valueOf(x));
				}
					break;

				default: {
				}
					break;

				}

			}

			public void onAccuracyChanged(Sensor s, int accuracy) {
			}

		};

		for (Sensor s : sensors) {
			sensorMgr.registerListener(lsn, s,
					SensorManager.SENSOR_DELAY_NORMAL);
		}
	}

	@Override
	public void finishSelf(int result) {

		sensorMgr.unregisterListener(lsn);

		super.finishSelf(result);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.sensor_pass:
			finishSelf(RESULT_PASS);
			break;
		case R.id.sensor_false:
			finishSelf(RESULT_FALSE);
			break;
		default:
			finishSelf(RESULT_PASS);
			break;
		}

	}

}
