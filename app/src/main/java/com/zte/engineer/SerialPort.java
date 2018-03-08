package com.zte.engineer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.MalformedInputException;


/**
 * here we want to use the handler to refresh the UI. There are a lot of ways to update the UI. Like the asyncTask or
 * RunOnUiThread() method. Also there is a third library called RxJava. But here is just a demonstration so we decied
 * to use the most simple way named Handler way.
 */


public class SerialPort extends Activity implements View.OnClickListener {

    private static int flag1, flag2, flag3;

    private Button serialPortTest1, serialPortTest2, serialPortTest3;

    private Button pass, fail;

    private TextView testResult1, testResult2, testResult3;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alextao_serial_port_test);


        initWidgets();
        // pass.setEnabled(false);

        if (openSerialPort(1) < 0) {
            Toast.makeText(this, "can not open serial port 1", Toast.LENGTH_SHORT).show();
            pass.setEnabled(false);
        }
        if (openSerialPort(2) < 0) {
            Toast.makeText(this, "can not open serial port 2", Toast.LENGTH_SHORT).show();
            pass.setEnabled(false);
        }
        if (openSerialPort(3) < 0) {
            Toast.makeText(this, "can not open serial port 3", Toast.LENGTH_SHORT).show();
            pass.setEnabled(false);
        }


    }


    /**
     * initialize the widgets of the layout and prepare for update the UI.
     */
    private void initWidgets() {
        serialPortTest1 = (Button) findViewById(R.id.start_btn);
        serialPortTest1.setOnClickListener(this);
        serialPortTest2 = (Button) findViewById(R.id.start2_btn);
        serialPortTest2.setOnClickListener(this);
        serialPortTest3 = (Button) findViewById(R.id.start3_btn);
        serialPortTest3.setOnClickListener(this);

        pass = (Button) findViewById(R.id.pass);
        pass.setOnClickListener(this);
        fail = (Button) findViewById(R.id.fail);
        fail.setOnClickListener(this);

        testResult1 = (TextView) findViewById(R.id.serial_port1_result);
        testResult2 = (TextView) findViewById(R.id.serial_port2_result);
        testResult3 = (TextView) findViewById(R.id.serial_port3_result);
    }

    @Override
    protected void onDestroy() {


        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
       setResult(20);
        finish();
        super.onBackPressed();
    }


    //here is the native method about.
    static {
        System.loadLibrary("SerialPort");
    }

    public static native int UartTest(int uartNo, int baudrate, int dataBit, int Parity, int stopBit);

    public static native int openSerialPort(int uartNo);

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_btn:
                //serial port1
                if (UartTest(1, 115200, 8, 0, 1) == 1) {
                    testResult1.setText("SUCCESS");
                    flag1 = 1;
                    testResult1.setTextColor(Color.GREEN);
                } else {
                    flag1 = 0;
                    testResult1.setText("FAIL");
                    testResult1.setTextColor(Color.RED);
                }
                break;
            case R.id.start2_btn:
                //serial port2
                if (UartTest(2, 115200, 8, 0, 1) == 1) {
                    testResult2.setText("SUCCESS");
                    flag2 = 1;
                    testResult2.setTextColor(Color.GREEN);
                } else {
                    flag2 = 0;
                    testResult2.setText("FAIL");
                    testResult2.setTextColor(Color.RED);
                }

                break;

            case R.id.start3_btn:

                //serial port3
                if (UartTest(3, 115200, 8, 0, 1) == 1) {
                    testResult3.setText("SUCCESS");
                    flag3 = 1;
                    testResult3.setTextColor(Color.GREEN);
                } else {
                    flag3 = 0;
                    testResult3.setText("FAIL");
                    testResult3.setTextColor(Color.RED);
                }

                break;
            case R.id.pass:
                setResult(10);
                finish();
                break;
            case R.id.fail:
                setResult(20);
                finish();
                break;
            default:
                break;
        }
    }


}

	
	
	
	
	
	
	