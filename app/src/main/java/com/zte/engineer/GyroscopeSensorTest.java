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

public class GyroscopeSensorTest extends ZteActivity {

    private boolean gyroscope_visible = true;
    LinearLayout gyroscope_layout;
	private float x, y, z;
	private SensorManager sensorMgr;
	private SensorEventListener lsn;
	TextView GyroscopeX, GyroscopeY, GyroscopeZ;
	Button mPassBt;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.gyroscope_sensortest);

		sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        
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
		mPassBt = (Button) findViewById(R.id.gyroscope_sensor_pass);
		mPassBt.setOnClickListener(this);
		mPassBt.setEnabled(false);
		((Button) findViewById(R.id.gyroscope_sensor_false)).setOnClickListener(this);

        gyroscope_layout = (LinearLayout) findViewById(R.id.gyroscope_layout);
        if(!gyroscope_visible)
            gyroscope_layout.setVisibility(View.GONE);
		GyroscopeX = (TextView) findViewById(R.id.gyroscope_x);
		GyroscopeY = (TextView) findViewById(R.id.gyroscope_y);
		GyroscopeZ = (TextView) findViewById(R.id.gyroscope_z);
	}

	private void initSensorListener() {
		List<Sensor> sensors = sensorMgr.getSensorList(Sensor.TYPE_ALL);

		lsn = new SensorEventListener() {
			public void onSensorChanged(SensorEvent e) {
				switch (e.sensor.getType()) {
					case Sensor.TYPE_GYROSCOPE: {
						x = e.values[SensorManager.DATA_X];
						y = e.values[SensorManager.DATA_Y];
						z = e.values[SensorManager.DATA_Z];
						
						GyroscopeX.setText(String.valueOf(x));
						GyroscopeY.setText(String.valueOf(y));
						GyroscopeZ.setText(String.valueOf(z));
						if(String.valueOf(x) != null && !String.valueOf(x).equals("")
								&& String.valueOf(y) != null && !String.valueOf(y).equals("")
								&& String.valueOf(z) != null && !String.valueOf(z).equals("")&&
								!(String.valueOf(x).equals("0") && String.valueOf(y).equals("0") && String.valueOf(z).equals("0"))){
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
		case R.id.gyroscope_sensor_pass:
			finishSelf(RESULT_PASS);
			break;
		case R.id.gyroscope_sensor_false:
			finishSelf(RESULT_FALSE);
			break;
		default:
			finishSelf(RESULT_FALSE);
			break;
		}

	}

}
