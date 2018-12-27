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

public class MSensorTest extends ZteActivity {

    private boolean magnetic_visible = true;
    LinearLayout magnetic_layout;
	private float x, y, z;
	private SensorManager sensorMgr;
	private SensorEventListener lsn;
	TextView MagneticX, MagneticY, MagneticZ;
	private int mData = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.m_sensortest);

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
		((Button) findViewById(R.id.m_sensor_pass)).setOnClickListener(this);
		((Button) findViewById(R.id.m_sensor_false)).setOnClickListener(this);

        magnetic_layout = (LinearLayout) findViewById(R.id.magnetic_layout);
        if(!magnetic_visible)
            magnetic_layout.setVisibility(View.GONE);
        
		MagneticX = (TextView) findViewById(R.id.magnetic_x);
		MagneticY = (TextView) findViewById(R.id.magnetic_y);
		MagneticZ = (TextView) findViewById(R.id.magnetic_z);
	}

	private void initSensorListener() {
		List<Sensor> sensors = sensorMgr.getSensorList(Sensor.TYPE_ALL);

		lsn = new SensorEventListener() {
			public void onSensorChanged(SensorEvent e) {
				switch (e.sensor.getType()) {
					case Sensor.TYPE_MAGNETIC_FIELD: {
						x = e.values[SensorManager.DATA_X];
						y = e.values[SensorManager.DATA_Y];
						z = e.values[SensorManager.DATA_Z];
						
						MagneticX.setText(String.valueOf(x));
						MagneticY.setText(String.valueOf(y));
						MagneticZ.setText(String.valueOf(z));
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
		case R.id.m_sensor_pass:
			finishSelf(RESULT_PASS);
			break;
		case R.id.m_sensor_false:
			finishSelf(RESULT_FALSE);
			break;
		default:
			finishSelf(RESULT_PASS);
			break;
		}

	}

}
