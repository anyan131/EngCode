package com.zte.engineer;

import android.app.Activity;
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

/**
 * TODO we need to find how to test the 3 serial port in one activity.
 * TODO in the afternoon i will find a way to test the whole 3 serial port.
 * find the solutions about this.
 */


public class SerialPort extends Activity {

    private OutputStream outputStream, outputStream1, outputStream2;
    private InputStream inputStream, inputStream1, inputStream2;
    private final Object mLock = new Object();


    boolean mBytesReceivedBack = false;

    boolean isPass1 = false;
    boolean isPass2 = false;
    boolean isPass3 = false;


    byte valueToSend;

    ReadTread readTread, readTread1, readTread2;
    SendThread sendThread, sendThread1, sendThread2;

    Integer mSending = 0;
    Integer mReceive = 0;
    Integer mLost = 0;


    private static final int SEND_MSG = 0X000a;
    private static final int RECEIVE_MSG = 0X000b;
    private static final int LOST_MSG = 0X000c;

    private static final int RESET_MSG = 0X000d;

    private TextView sendBytesTV, receiveBytesTV, lostBytesTV;
    private Button start, stop, start2, stop2, start3, stop3, pass, fail;

    int count = 0;
    /**
     * @warning here we use the handler may cause the memory leak.
     * just ignore it...
     * TODO fix the way or refactor the code.
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SEND_MSG:
                    sendBytesTV.setText(msg.obj + "");
                    break;

                case RECEIVE_MSG:
                    receiveBytesTV.setText(msg.obj + "");
                    break;

                case LOST_MSG:
                    lostBytesTV.setText(msg.obj + "");

                    break;
                case RESET_MSG:
                    mReceive = 0;
                    mSending = 0;
                    mLost = 0;
                    sendBytesTV.setText("0");
                    receiveBytesTV.setText("0");
                    lostBytesTV.setText("0");
                    break;
                case 0x0001a:


                    int i = Integer.valueOf(msg.obj.toString());
                    switch (i) {
                        case 0:
                            isPass1 = true;
                            break;
                        case 1:
                            isPass2 = true;
                            break;
                        case 2:
                            isPass3 = true;
                            break;
                    }
                    if (isPass3 && isPass2 && isPass1) {
                        pass.setEnabled(true);
                    }
                    break;


            }


            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alextao_serial_port_test);

        //initialize the widgets.
        initWidgets();
      //  final boolean res = SerialPort.opend();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final boolean res = SerialPort.opend();
                if (res) {
                    try {
                        outputStream = new FileOutputStream(new File("/dev/ttyMT1"));
                        inputStream = new FileInputStream(new File("/dev/ttyMT1"));
                        outputStream1 = new FileOutputStream(new File("/dev/ttyMT2"));
                        inputStream1 = new FileInputStream(new File("/dev/ttyMT2"));
                        outputStream2 = new FileOutputStream(new File("/dev/ttyMT3"));
                        inputStream2 = new FileInputStream(new File("/dev/ttyMT3"));
                        setListeners();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SerialPort.this,"initial false",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();







    }

    private void setListeners() {
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readTread = new ReadTread(inputStream, 0);
                readTread.start();
                sendThread = new SendThread(outputStream);
                sendThread.start();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!readTread.isInterrupted()) {
                    readTread.interrupt();
                }
                if (!sendThread.isInterrupted()) {
                    sendThread.interrupt();
                }
                Message message = mHandler.obtainMessage(RESET_MSG);
                message.sendToTarget();
                valueToSend = 0;
                count = 0;
            }
        });
        start2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readTread1 = new ReadTread(inputStream1, 1);
                readTread1.start();
                sendThread1 = new SendThread(outputStream1);
                sendThread1.start();
            }
        });
        stop2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!readTread1.isInterrupted()) {
                    readTread1.interrupt();
                }
                if (!sendThread1.isInterrupted()) {
                    sendThread1.interrupt();
                }
                Message message = mHandler.obtainMessage(RESET_MSG);
                message.sendToTarget();
                valueToSend = 0;
                count = 0;
            }
        });
        start3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readTread2 = new ReadTread(inputStream2, 2);
                readTread2.start();
                sendThread2 = new SendThread(outputStream2);
                sendThread2.start();

            }
        });
        stop3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!readTread2.isInterrupted()) {
                    readTread2.interrupt();
                }
                if (!sendThread2.isInterrupted()) {
                    sendThread2.interrupt();
                }
                Message message = mHandler.obtainMessage(RESET_MSG);
                message.sendToTarget();
                valueToSend = 0;
                count = 0;
            }
        });
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    /**
     * initialize the widgets of the layout and prepare for update the UI.
     */
    private void initWidgets() {
        sendBytesTV = (TextView) findViewById(R.id.SendBytesTV);
        receiveBytesTV = (TextView) findViewById(R.id.ReceiveBytesTV);
        lostBytesTV = (TextView) findViewById(R.id.LostBytesTV);
        start = (Button) findViewById(R.id.start_btn);
        stop = (Button) findViewById(R.id.stop);
        start2 = (Button) findViewById(R.id.start2_btn);
        stop2 = (Button) findViewById(R.id.stop2);
        start3 = (Button) findViewById(R.id.start3_btn);
        stop3 = (Button) findViewById(R.id.stop3);
        pass = (Button) findViewById(R.id.pass);
        pass.setEnabled(false);
        fail = (Button) findViewById(R.id.fail);
    }

    @Override
    protected void onDestroy() {
       SerialPort.closed();
        if (readTread != null)
            readTread.interrupt();
        readTread = null;
        if (sendThread != null)
            sendThread.interrupt();
        sendThread = null;
        if (readTread1 != null)
            readTread1.interrupt();
        readTread1 = null;
        if (sendThread1 != null)
            sendThread1.interrupt();
        sendThread1 = null;
        if (readTread2 != null)
            readTread2.interrupt();
        readTread2 = null;
        if (sendThread2 != null)
            sendThread2.interrupt();
        sendThread = null;

        try {
            if (inputStream != null)
            closeAll(inputStream);
            if (outputStream!= null)
            closeAll(outputStream);
            if (inputStream1 != null)
            closeAll(inputStream1);
            if (outputStream1 != null)
            closeAll(outputStream1);
            if (inputStream2 != null)
            closeAll(inputStream2);
            if (outputStream2 != null)
            closeAll(outputStream2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.onDestroy();
    }

    private class ReadTread extends Thread {
        private InputStream inputStream;
        //here we decide to use flag to tell the difference of the serial port.
        private int flag;

        ReadTread(InputStream inputStream, int flag) {
            this.inputStream = inputStream;
            this.flag = flag;
        }

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                int size;

                byte buffer[] = new byte[64];
                if (inputStream == null) return;
                try {
                    size = inputStream.read(buffer);
                    if (size > 0) {
                        //TODO here we need to update UI based on what we received.
                        onDataReceived(buffer, size);
//                        Message message1 = new Message();
//                        message1.what = RECEIVE_MSG;
//                        mHandler.sendMessage(message1);


                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        inputStream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    return;
                }
                if (mLost < 5) {
                    Message msg = mHandler.obtainMessage(0x0001a);
                    msg.obj = flag;
                    msg.sendToTarget();
                }
            }

        }
    }

    private class SendThread extends Thread {
        private OutputStream outputStream;

        SendThread(OutputStream outputStream) {
            this.outputStream = outputStream;
        }

        @Override
        public void run() {
            super.run();
            while (valueToSend < 100 && count < 100) {
                synchronized (mLock) {
                    mBytesReceivedBack = false;
                    if (outputStream == null) return;

                    try {
                        outputStream.write(valueToSend);

                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    mSending++;
                    count++;
                    Message message = new Message();
                    message.what = SEND_MSG;
                    message.obj = mSending;
                    mHandler.sendMessage(message);
                    try {
                        //try to pause here 0.5s here and to do some complex logic here.

                        mLock.wait(500);
                        if (mBytesReceivedBack) {
                            //here we can do some logic here.
                            mReceive++;
                            Message message2 = new Message();
                            message2.what = RECEIVE_MSG;
                            message2.obj = mReceive;
                            mHandler.sendMessage(message2);

                        } else {
                            Message message3 = new Message();
                            message3.what = LOST_MSG;
                            mLost++;

                            message3.obj = mLost;
                            mHandler.sendMessage(message3);
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        try {
                            outputStream.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }

                }
            }
        }
    }


    private void onDataReceived(byte[] buffer, int size) {
        synchronized (mLock) {
            int j;
            for (j = 0; j < size; j++) {
                if ((buffer[j] == valueToSend) && (mBytesReceivedBack)) {
                    valueToSend++;

                    mBytesReceivedBack = true;
                    mLock.notify();
                } else {
                    break;
                }
            }
        }
    }

    void closeAll(Closeable closeable) throws IOException {
        closeable.close();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    //here is the native method about.
    static {
        System.loadLibrary("newmobiSerialPort");
    }

    public static native boolean opend();

    public static native void closed();

}

	
	
	
	
	
	
	