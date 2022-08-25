package com.zte.engineer;


import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;

public class newCode extends ZteActivity {
    private static final String TAG = newCode.class.getSimpleName();
    private String FileName = null;
    private Button pass, fail;

    private Button startRecord;
    private Button startPlay;
    private Button stopRecord;
    private Button stopPlay;

    private TextView getDb;

    private MediaPlayer mPlayer = null;
    private MediaRecorder mRecorder = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newtest);

        startRecord = (Button) findViewById(R.id.new_sound);
        startRecord.setOnClickListener(new startRecordListener());

        startPlay = (Button) findViewById(R.id.new_play);
        startPlay.setOnClickListener(new startPlayListener());
        startPlay.setEnabled(false);

        FileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        FileName += "/audiorecordtest.3gp";

        pass = findViewById(R.id.all_pass_button);
        pass.setOnClickListener(this);
        pass.setEnabled(false);
        fail = findViewById(R.id.all_false_button);
        fail.setOnClickListener(this);

        getDb = findViewById(R.id.getDb);
    }


    private final Handler mHandler = new Handler();
    private Runnable mUpdateMicStatusTimer = () -> updateMicStatus();

    /**
     * 更新话筒状态
     */
    private int BASE = 1;
    private int SPACE = 100;// 间隔取样时间

    private void updateMicStatus() {
        if (mRecorder != null) {
            double ratio = (double) mRecorder.getMaxAmplitude() / BASE;
            double db = 0;// 分贝
            if (ratio > 1)
                db = 20 * Math.log10(ratio);
            Log.e(TAG, "updateMicStatus: " + db);

            getDb.setText("分贝值： " + db);
            mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
        }
    }


    class startRecordListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub


            mRecorder = new MediaRecorder();
            mRecorder.setAudioSamplingRate(16000);

            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setOutputFile(FileName);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            try {
                mRecorder.prepare();
            } catch (IOException e) {

            }
            mRecorder.start();


            updateMicStatus();
            startRecord.setEnabled(false);
            startPlay.setEnabled(true);
        }

    }

    class startPlayListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            mPlayer = new MediaPlayer();
            try {
                mPlayer.setDataSource(FileName);
                mPlayer.prepare();
                mPlayer.start();

            } catch (IOException e) {

            }
            startRecord.setEnabled(true);
            startPlay.setEnabled(false);
            mRecorder = null;
//            mRecorder.stop();
            pass.setEnabled(true);
        }
    }


    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mPlayer = null;

    }

    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.singlebutton_pass_button:
                finishSelf(RESULT_PASS);
//                mRecorder.stop();
                mPlayer.stop();
                break;
            case R.id.singlebutton_false_button:
                finishSelf(RESULT_FALSE);
//                mRecorder.stop();
                mPlayer.stop();
                break;
            case R.id.all_pass_button:
                finishSelf(RESULT_PASS);
                break;
            case R.id.all_false_button:
                finishSelf(RESULT_FALSE);
                break;
            default:
                finishSelf(RESULT_PASS);
                break;
        }

    }
}