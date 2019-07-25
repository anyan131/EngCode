package com.zte.engineer;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class AlexBackCameraTest extends Activity {

    private Button btn_pass, btn_fail;

    private SurfaceView cameraView;
    private SurfaceHolder.Callback mCallback;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private Camera.Parameters mParameters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alex_back_camera_test);

        btn_pass = (Button) findViewById(R.id.btn_pass);
        btn_fail = (Button) findViewById(R.id.btn_fail);

        btn_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(10);
                finish();
            }
        });
        btn_fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(20);
                finish();
            }
        });


        btn_pass.setEnabled(false);

        cameraView = (SurfaceView) findViewById(R.id.cameraView);
        mSurfaceHolder = cameraView.getHolder();
        mCallback = new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                startCamera();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                stopCamera();
            }
        };
        int i = findBackCamera();
        if (i == 0) {

            mSurfaceHolder.addCallback(mCallback);
            btn_pass.setEnabled(true);

        } else {
            Toast.makeText(AlexBackCameraTest.this, "no back camera", Toast.LENGTH_SHORT).show();

        }


    }


    public void startCamera() {

        try{
            mCamera = android.hardware.Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "no camera found", Toast.LENGTH_SHORT).show();
            return;
        }

        mParameters = mCamera.getParameters();
        if (mCamera == null) {
            Toast.makeText(getApplicationContext(), "no camera found", Toast.LENGTH_SHORT).show();
            return;
        }
        try {

            List<Camera.Size> sizes = mParameters.getSupportedPreviewSizes();

            Camera.Size optionSize = getOptimalSize(sizes, cameraView.getHeight(), cameraView.getWidth());

//            if (cameraView.getHeight() > cameraView.getWidth()) {
//                optionSize = getOptimalSize(sizes, cameraView.getHeight(), cameraView.getWidth());
//            } else {
//                optionSize = getOptimalSize(sizes, cameraView.getWidth(), cameraView.getHeight());
//            }

            mParameters.setPreviewSize(optionSize.width, optionSize.height);
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.setDisplayOrientation(0);
            mCamera.setParameters(mParameters);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    private Camera.Size getOptimalSize(List<Camera.Size> sizes, int width, int height) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) width / height;

        if (sizes == null) return null;
        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = height;
        for (Camera.Size size : sizes) {
            double ratio = size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }
        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;

    }

    public void stopCamera() {
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }


    private int findBackCamera() {
        int cameraCount;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int cameraID = 0; cameraID < cameraCount; cameraID++) {
            Camera.getCameraInfo(cameraID, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                return cameraID;
            }
        }
        return -1;
    }

    @Override
    protected void onDestroy() {
        mSurfaceHolder.removeCallback(mCallback);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        setResult(20);
        finish();
        super.onBackPressed();
    }
}
