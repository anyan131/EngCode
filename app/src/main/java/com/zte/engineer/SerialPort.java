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
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.PasswordAuthentication;
import java.nio.charset.MalformedInputException;


/**
 * here we want to use the handler to refresh the UI. There are a lot of ways to update the UI. Like the asyncTask or
 * RunOnUiThread() method. Also there is a third library called RxJava. But here is just a demonstration so we decied
 * to use the most simple way named Handler way.
 */


public class SerialPort extends Activity implements View.OnClickListener {

    private final String TAG = SerialPort.this.getClass().getName();

    private static int flag1, flag2, flag3;

    private Button serialPortTest1, serialPortTest2;

    private Button pass, fail;

    private TextView testResult2;
    private FileInputStream fileInputStream;
    private boolean isOpen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alextao_serial_port_test);


        initWidgets();
    }


    /**
     * initialize the widgets of the layout and prepare for update the UI.
     */
    private void initWidgets() {
        serialPortTest1 = (Button) findViewById(R.id.start_btn);
        serialPortTest1.setOnClickListener(this);
        serialPortTest2 = (Button) findViewById(R.id.start2_btn);
        serialPortTest2.setOnClickListener(this);


        pass = (Button) findViewById(R.id.pass);
        pass.setOnClickListener(this);
        fail = (Button) findViewById(R.id.fail);
        fail.setOnClickListener(this);


        testResult2 = (TextView) findViewById(R.id.serial_port2_result);

    }

    @Override
    protected void onDestroy() {

        FileUtils.writePath("out 183 0", "sys/devices/platform/pinctrl/mt_gpio");
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        setResult(20);
        finish();
        super.onBackPressed();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_btn:
                FileUtils.writePath("out 183 1", "sys/devices/platform/pinctrl/mt_gpio");

                FileDescriptor qx = Serial.getInstance().open("/dev/ttyS1", 9600, 0);
                fileInputStream = new FileInputStream(qx);
                isOpen = true;
                thread.start();


                break;
            case R.id.start2_btn:
                //serial port2
                try {
                    FileUtils.writePath("out 183 1", "sys/devices/platform/pinctrl/mt_gpio");


                    FileDescriptor shuang = Serial.getInstance().open("/dev/ttyS1", 115200, 0);
                    fileInputStream = new FileInputStream(shuang);
                    isOpen = true;
                    thread.start();

                } catch (Exception e) {
                    e.printStackTrace();
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


    private Thread thread = new Thread() {
        @Override
        public void run() {
            while (isOpen) {
                byte[] bys = new byte[5012];
                try {
                    int read = fileInputStream.read(bys);
                    String s = new String(bys, 0, read);
                    Log.e("TAG", "run: " + s);

                    runOnUiThread(() -> {
                                testResult2.setText(s);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        testResult2.setText("");
                                    }
                                }, 1000);

                            }
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    };


}

	
	
	
	
	
	
	