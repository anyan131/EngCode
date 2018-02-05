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

public class GSensorTest extends ZteActivity {
	
	private float x, y, z;  
	private SensorManager sensorMgr;
	private SensorEventListener lsn;
	TextView GsensorX, GsensorY, GsensorZ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.gsensortest);
        
        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        
        initUi();
        
        initSensorListener();
    }
    

    
    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		GradienterJni.gsensorOpen(0,0,0);
		//initSensorListener();
	}

    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		 GradienterJni.gsensorClose(0,0,0);
	}

	private void initUi()
    {  	
    	((Button)findViewById(R.id.sensor_pass)).setOnClickListener(this);
    	((Button)findViewById(R.id.sensor_false)).setOnClickListener(this);
		((Button)findViewById(R.id.sensor_calibration)).setOnClickListener(this);
    	
    	GsensorX = (TextView)findViewById(R.id.g_sensor_x); 	
    	GsensorY = (TextView)findViewById(R.id.g_sensor_y);
    	GsensorZ = (TextView)findViewById(R.id.g_sensor_z);    	
    }
    
    private void initSensorListener()
    {    	
    	List<Sensor> sensors = sensorMgr.getSensorList(Sensor.TYPE_ALL);
    	
        lsn = new SensorEventListener() {
            public void onSensorChanged(SensorEvent e) 
            {
            	x = e.values[SensorManager.DATA_X]; 
            	y = e.values[SensorManager.DATA_Y];       
            	z = e.values[SensorManager.DATA_Z];
            	       
            	switch(e.sensor.getType())
            	{
            		case Sensor.TYPE_ACCELEROMETER:
            		{
            			GsensorX.setText(String.valueOf(x));
            			GsensorY.setText(String.valueOf(y));
            			GsensorZ.setText(String.valueOf(z));            	       			
            		}
            		break; 		
            		
            		default:
            		{
            		}
            		break;
            	       			
            	}
            	       
            } 
            
            public void onAccuracyChanged(Sensor s, int accuracy) 
            { 
            }  
            
        };

        for(Sensor s:sensors)
        {
            sensorMgr.registerListener(lsn, s, SensorManager.SENSOR_DELAY_NORMAL);   
        }
    }

	@Override
	public void finishSelf(int result) {
		
		sensorMgr.unregisterListener(lsn);
		
		super.finishSelf(result);
	}



	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.sensor_pass:
			finishSelf(RESULT_PASS);
			break;
		case R.id.sensor_false:
			finishSelf(RESULT_FALSE);
			break;
		case R.id.sensor_calibration:
			GradienterJni.gsensorCali(0,0,0);
			break;
		default:
			finishSelf(RESULT_PASS);
			break;
		}
		
	}
    
    

}
