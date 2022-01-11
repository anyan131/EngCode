package com.zte.engineer;


import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class newCode extends ZteActivity {
    private String FileName = null;
    private Button pass,fail;

    private Button startRecord;
    private Button startPlay;
    private Button stopRecord;
    private Button stopPlay;

    private MediaPlayer mPlayer = null;
    private MediaRecorder mRecorder = null;
    private int mRecord_Size;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newtest);

        startRecord = (Button)findViewById(R.id.new_sound);
        startRecord.setOnClickListener(new startRecordListener());

        startPlay = (Button)findViewById(R.id.new_play);
        startPlay.setOnClickListener(new startPlayListener());
        startPlay.setEnabled(false);

        FileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        FileName += "/audiorecordtest.3gp";

        pass = (Button)findViewById(R.id.all_pass_button);
        pass.setOnClickListener(this);
        pass.setEnabled(false);
        fail = (Button)findViewById(R.id.all_false_button);
        fail.setOnClickListener(this);
    }

    private static void newOne(Context context){    };

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