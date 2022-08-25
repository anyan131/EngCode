package com.zte.engineer;


import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    private static FileOutputStream fos;


    public static void writeFile(String content) {
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
//                    String format = simpleDateFormat.format(new Date(System.currentTimeMillis()));
//                    byte[] data = (format + " " + content + "\n").getBytes();
//                    fos.write(data);
//                    fos.flush();
//
//
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();


    }

    public static boolean writePath(String value, String path) {
        boolean isWriteOK = false;
        File file = new File(path);
        if (!file.exists()) {
            return isWriteOK;
        }
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(file);
            outStream.write(value.getBytes());
            isWriteOK = true;
            Log.e("TAG", "write the value " + value + " to path " + path + " successfully");
        } catch (Exception e) {
            e.printStackTrace();
            isWriteOK = false;
            Log.e("TAG", "write the value " + value + " to path " + path + " fail " + e.toString());
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (Exception ex) {
                }
            }
            return isWriteOK;
        }
    }


    public static void writeLog(final String content) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
                    String format = simpleDateFormat.format(new Date(System.currentTimeMillis()));
                    byte[] data = (format + " " + content + "\n").getBytes();
                    fos.write(data);
                    fos.flush();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static void writeConfig(final String url, final String content) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                File file = new File(url);
                try {
                    FileWriter fileWriter = new FileWriter(file, false);
                    fileWriter.write(content);
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }


}
