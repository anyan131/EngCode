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
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.Context;
import android.content.Intent;

public class HallTest extends ZteActivity {
	
	private TextView HallView;
	private HallOpenBroadcastReceiver mBroadcastReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.halltest);
       
        
        initUi();
        
        initReceiver();
    }
    

    
    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		//initSensorListener();
	}



	private void initUi()
    {  	
    	((Button)findViewById(R.id.sensor_pass)).setOnClickListener(this);
    	((Button)findViewById(R.id.sensor_false)).setOnClickListener(this);
    	
    	HallView = (TextView)findViewById(R.id.hall_status_text);
    	
    }
	
	public class HallOpenBroadcastReceiver extends BroadcastReceiver {  	
		
		@Override  
		public void onReceive(Context context, Intent intent) {  	  
			if(intent.getAction().equals("com.eastaeon.action.HALLOPEN")) {
				HallView.setText(R.string.hall_open);
			} else if(intent.getAction().equals("com.eastaeon.action.HALLCLOSE")) {
				HallView.setText(R.string.hall_close);
			} 
		}  
	}
    
	private void initReceiver() {
		mBroadcastReceiver = new HallOpenBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
        filter.addAction("com.eastaeon.action.HALLOPEN");
		filter.addAction("com.eastaeon.action.HALLCLOSE");
		registerReceiver(mBroadcastReceiver,filter);		
	}
	
	
	@Override
	public void finishSelf(int result) {
	if(mBroadcastReceiver!=null)
		unregisterReceiver(mBroadcastReceiver);

		
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
		default:
			finishSelf(RESULT_PASS);
			break;
		}
		
	}
    
    

}
