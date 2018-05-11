package com.zte.engineer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by alextao on 2018/1/8.
 * @author alextao
 *  2018/01/08
 */

public class GPIO extends Activity implements View.OnClickListener {

    private EditText gpioIndex;
    private Button btnSetOutput, btnSetHigh, btnSetLow;
    private Button pass ,fail;
	private static final String TAG = "NewMobi";
	
	
	 private static final int GPIO_INDEX[] = new int[]{95,21,65,127,122,120,121,98,96,97,99,55,80,78,61,
	 79,20,19,3,4,1,0,7,5,6,2,63,62,64,68,67,66
	 };
	
	private static final int GPIO_OUT[] = new int[]{95,21,65,127,122,120,121,98,96,97,99,55,80,78,61,
			79

	};
	private static final int GPIO_IN[] = new int[]{20,19,3,4,1,0,7,5,6,2,63,62,64,68,67,66

	};


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alex_tao_gpio);
		 getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initWidgets();
		EmGpio.gpioInit();

    }

    /**
     * this is for init the widgets.
     */
    private void initWidgets() {
        gpioIndex = (EditText) findViewById(R.id.gpio_edittext_index);
        btnSetOutput = (Button) findViewById(R.id.gpio_btn_setOutput);
        btnSetOutput.setOnClickListener(this);
        btnSetHigh = (Button) findViewById(R.id.gpio_btn_setHigh);
        btnSetHigh.setOnClickListener(this);
        btnSetLow = (Button) findViewById(R.id.gpio_btn_setLow);
        btnSetLow.setOnClickListener(this);
        pass = (Button)findViewById(R.id.gpio_pass);
        pass.setOnClickListener(this);
        fail = (Button) findViewById(R.id.gpio_fail);
        fail.setOnClickListener(this);
		Log.i(TAG,GPIO_INDEX.length+"");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gpio_btn_setHigh:
			for(int i = 0; i< GPIO_INDEX.length;i++){
					boolean res = EmGpio.setGpioDataHigh(GPIO_INDEX[i]);
					if(res == false){
						Toast.makeText(GPIO.this,"set pin "+i+" datahigh error",Toast.LENGTH_SHORT).show();
						break;
					}
				}
				
                break;
            case R.id.gpio_btn_setLow:
			for(int i = 0; i< GPIO_INDEX.length;i++){
					boolean res = EmGpio.setGpioDataLow(GPIO_INDEX[i]);
					if(res == false){
						Toast.makeText(GPIO.this,"set pin "+i+" data low error",Toast.LENGTH_SHORT).show();
						break;
					}
				}

                break;
            case R.id.gpio_btn_setOutput:
			{
				for(int i = 0; i< GPIO_INDEX.length;i++){
					boolean res = EmGpio.setGpioOutput(GPIO_INDEX[i]);
					if(res == false){
						Toast.makeText(GPIO.this,"set pin "+i+" output error",Toast.LENGTH_SHORT).show();
						break;
					}
				}
			}
                break;
			case R.id.gpio_pass:
				setResult(10);
				finish();
				break;
			case R.id.gpio_fail:
				setResult(20);
				finish();
				break;


        }
    }

	@Override
	public void onBackPressed() {
		setResult(20);
		finish();
    	super.onBackPressed();
	}

	@Override
    protected void onDestroy() {
        EmGpio.gpioUnInit();
        super.onDestroy();
    }
}
