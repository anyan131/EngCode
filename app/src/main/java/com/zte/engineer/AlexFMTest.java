package com.zte.engineer;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mediatek.fmradio.FmRadioNative;

import java.io.IOException;

/**
 * Created by Administrator on 2018/2/1.
 */

public class AlexFMTest extends Activity {

    private static final int CURRENT_RX_ON = 0;
    private static final int CURRENT_TX_ON = 1;
    private float STATION = Float.valueOf("90.1");
    private boolean mIsPowerUp = false;
    private Button passBtn, failBtn;
    private static final String TAG = "AlexFMTest";
    private boolean isFmOpened = false;

    private MediaPlayer mFmPlayer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alextao_fm_test);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        bindView();
        isFmOpened = FmRadioNative.openDev();
        initFmPlayer();

        //here we need to open the fm then power up the fm with a fix radio rate.
        if (isFmOpened){
            FmRadioNative.setFmStatus(CURRENT_RX_ON, true);
            FmRadioNative.setFmStatus(CURRENT_TX_ON, false);
            if(!FmRadioNative.powerUp(STATION)){
                Log.d(TAG,"can not powerup the station");
            }else {
                mIsPowerUp = true;
                enableFmAudio(mIsPowerUp);
            }
        }

    }

    /**
     * bind the view and set the listeners.
     *
     * @author Alex
     */
    private void bindView() {
        passBtn = (Button) findViewById(R.id.fm_pass_btn);
        failBtn = (Button) findViewById(R.id.fm_fail_btn);
        passBtn.setOnClickListener(mListener);
        failBtn.setOnClickListener(mListener);
    }

    private boolean initFmPlayer() {
        mFmPlayer = new MediaPlayer();
        mFmPlayer.setOnErrorListener(mPlayerErrorListener);
        if (mFmPlayer.isPlaying()){
            mFmPlayer.stop();
        }
        return true;
    }


    private final MediaPlayer.OnErrorListener mPlayerErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            if (MediaPlayer.MEDIA_ERROR_SERVER_DIED == what) {
                Log.d(TAG, "onError: MEDIA_SERVER_DIED");
                if (null != mFmPlayer) {
                    mFmPlayer.release();
                    mFmPlayer = null;
                }
                mFmPlayer = new MediaPlayer();

                mFmPlayer.setOnErrorListener(mPlayerErrorListener);
                try {
                    mFmPlayer.setDataSource("THIRDPARTY://MEDIAPLAYER_PLAYERTYPE_FM");
                    mFmPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mFmPlayer.prepare();
                    mFmPlayer.start();
                } catch (IOException ex) {
                    Log.e(TAG, "setDataSource: " + ex);
                    return false;
                } catch (IllegalArgumentException ex) {
                    Log.e(TAG, "setDataSource: " + ex);
                    return false;
                } catch (IllegalStateException ex) {
                    Log.e(TAG, "setDataSource: " + ex);
                    return false;
                }
            }

            return true;
        }
    };
    //handle the click event.
    View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fm_pass_btn:
                    setResult(10);
                    finish();
                    break;
                case R.id.fm_fail_btn:
                    setResult(20);
                    finish();
                    break;
                default:
                    setResult(20);
                    finish();
                    break;


            }
        }
    };


    @Override
    public void onBackPressed() {
        setResult(20);
        super.onBackPressed();

    }

    /**
     * Open or close FM Radio audio
     *
     * @param enable true, open FM audio; false, close FM audio;
     */
    private void enableFmAudio(boolean enable) {
        Log.d(TAG, ">>> FmRadioService.enableFmAudio: " + enable);
        if ((mFmPlayer == null) || !mIsPowerUp) {
            Log.w(TAG, "mFMPlayer is null in Service.enableFmAudio");
            return;
        }

        try {
            if (!enable) {
                if (!mFmPlayer.isPlaying()) {
                    Log.d(TAG, "warning: FM audio is already disabled.");
                    return;
                }

                Log.d(TAG, "call MediaPlayer.stop()");
                mFmPlayer.stop();
                Log.d(TAG, "stop FM audio.");
                return;
            }

            if (mFmPlayer.isPlaying()) {
                Log.d(TAG, "warning: FM audio is already enabled.");
                return;
            }
        } catch (IllegalStateException e) {
            Log.e(TAG, "Exception: Cannot call MediaPlayer isPlaying.", e);
        }

        try {
           // mFmPlayer.prepare();

                mFmPlayer.start();

        }
//        catch (IOException e) {
//            Log.e(TAG, "Exception: Cannot call MediaPlayer prepare.", e);
//        }
        catch (IllegalStateException e) {
            Log.e(TAG, "Exception: Cannot call MediaPlayer prepare.", e);
        }

        Log.d(TAG, "Start FM audio.");
        Log.d(TAG, "<<< FmRadioService.enableFmAudio");
    }
}
