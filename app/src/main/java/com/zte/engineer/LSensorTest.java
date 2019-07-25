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

public class LSensorTest extends ZteActivity {

    private boolean light_visible = true;
    LinearLayout light_layout;
	private float x, y, z;
	private SensorManager sensorMgr;
	private SensorEventListener lsn;
	TextView LightView;
	Button mPassBt;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.l_sensortest);

		sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        
        light_visible = sensorMgr.getDefaultSensor(Sensor.TYPE_LIGHT) != null;

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
		mPassBt = (Button) findViewById(R.id.l_sensor_pass);
		mPassBt.setOnClickListener(this);
		mPassBt.setEnabled(false);
		((Button) findViewById(R.id.l_sensor_false)).setOnClickListener(this);

        light_layout = (LinearLayout) findViewById(R.id.light_layout);
        if(!light_visible)
            light_layout.setVisibility(View.GONE);
		LightView = (TextView) findViewById(R.id.light_lux);
	}

	private void initSensorListener() {
		List<Sensor> sensors = sensorMgr.getSensorList(Sensor.TYPE_ALL);

		lsn = new SensorEventListener() {
			public void onSensorChanged(SensorEvent e) {
				switch (e.sensor.getType()) {
					case Sensor.TYPE_LIGHT: {
						x = e.values[SensorManager.DATA_X];
						LightView.setText(String.format(
								getString(R.string.light_is), String.valueOf(x)));
						if(String.valueOf(x) != null && !String.valueOf(x).equals("")){
							mPassBt.setEnabled(true);
						}
						break;
					}
	
					default: {
						break;
					}
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
		case R.id.l_sensor_pass:
			finishSelf(RESULT_PASS);
			break;
		case R.id.l_sensor_false:
			finishSelf(RESULT_FALSE);
			break;
		default:
			finishSelf(RESULT_FALSE);
			break;
		}

	}

}
