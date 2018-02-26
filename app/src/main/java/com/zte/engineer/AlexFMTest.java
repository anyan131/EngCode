package com.zte.engineer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.AudioSystem;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mediatek.fmradio.FmRadioNative;

import java.io.IOException;

import static com.mediatek.fmradio.FmRadioNative.setMute;


/**
 * Created by Administrator on 2018/2/1.
 * This class is for test audio. We've set the fix station at FM 90.1mHz.
 */

public class AlexFMTest extends Activity {


    private static final int CURRENT_RX_ON = 0;
    private static final int CURRENT_TX_ON = 1;
    private boolean DEBUG = true;
    private Button passBtn, failBtn;
    private static final String TAG = "AlexFMTest";

    private boolean mIsPlaying = false;
    private boolean mIsDeviceOpened = false;
    private boolean mIsPowerUp = false;

    // Record whether is speaker used
    private boolean mIsSpeakerUsed = false;
    //play the sound.
    private MediaPlayer mFmPlayer = null;

    private Context mContext;
    //here we use the stable station 90.1mHZ.
    private float mCurrentStation = Float.valueOf("90.1");

    private final int MESSAGE_POWER_UP = 9;

    private AudioManager audioManager = null;
    private HeadSetBroadcastReceiver headSetBroadcastReceiver = null;

    private AlexTaoFmTestHandler alexTaoFmTestHandler = null;

    private class HeadSetBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //plug in headset
            if (intent.getIntExtra("state", 0) == 1) {
                passBtn.setEnabled(true);
            }
            //unplug the headset
            if (intent.getIntExtra("state", 0) == 0) {
                passBtn.setEnabled(false);
            }
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: <<<");
        setContentView(R.layout.alextao_fm_test);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        bindView();

        mContext = getApplicationContext();
        headSetBroadcastReceiver = new HeadSetBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_HEADSET_PLUG);
        this.registerReceiver(headSetBroadcastReceiver, filter);


        audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        passBtn.setEnabled(false);
        HandlerThread handlerThread = new HandlerThread("AlexTaoThread");
        handlerThread.start();
        alexTaoFmTestHandler = new AlexTaoFmTestHandler(handlerThread.getLooper());

        if (!initFmPlayer()) {
            Log.e(TAG, "onCreate: >> init FM player failed");
            return;
        }

        openDevice();

        //set the FM audio not from the speaker.
        setSpeakPhoneOn(mIsSpeakerUsed);
        Log.d(TAG, "AlexFMTest.onCreate: >>>");

    }

    /**
     * at the onStart phase,we need to send the message to start the FM. at the previous
     * phase(onCreate),we only init the view and the fm player.
     * maybe we can do some work in the handler thread???
     */

    @Override
    protected void onStart() {
        alexTaoFmTestHandler.removeMessages(MESSAGE_POWER_UP);
        Message msg = alexTaoFmTestHandler.obtainMessage(MESSAGE_POWER_UP);
        alexTaoFmTestHandler.sendMessage(msg);

        super.onStart();
    }

    /**
     * @return init the FM player or not.
     */
    private boolean initFmPlayer() {
        mFmPlayer = new MediaPlayer();
        //because we do not support the short antenna so we need to make sure our
        //app running in current activity.
        mFmPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);

        mFmPlayer.setOnErrorListener(mOnErrorListener);
        try {
            mFmPlayer.setDataSource("THIRDPARTY://MEDIAPLAYER_PLAYERTYPE_FM");
            mFmPlayer.setAudioStreamType(AudioSystem.STREAM_MUSIC);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * set FM audio from speaker or not.
     *
     * @param mIsSpeakerUsed
     */

    private void setSpeakPhoneOn(boolean mIsSpeakerUsed) {
        if (DEBUG)
            Log.d(TAG, "setSpeakPhoneOn: <<<" + mIsSpeakerUsed);
        AudioSystem.
                setForceUse(1, (mIsSpeakerUsed ? AudioSystem.FORCE_SPEAKER : AudioSystem.FORCE_NONE));
        if (DEBUG)
            Log.d(TAG, "setSpeakPhoneOn: >>>");

    }

    //try to open the fm device.
    private boolean openDevice() {
        if (DEBUG)
            Log.d(TAG, "openDevice: <<<" + mIsDeviceOpened);
        if (!mIsDeviceOpened) {
            mIsDeviceOpened = FmRadioNative.openDev();
        }
        if (DEBUG)
            Log.d(TAG, "openDevice: >>>" + mIsDeviceOpened);
        return mIsDeviceOpened;
    }

    /**
     * bind the view and set the listeners.
     *
     * @author Alex
     */
    private void bindView() {
        if (DEBUG)
            Log.d(TAG, ">>>binding views");
        passBtn = (Button) findViewById(R.id.fm_pass_btn);
        failBtn = (Button) findViewById(R.id.fm_fail_btn);
        passBtn.setOnClickListener(mListener);
        failBtn.setOnClickListener(mListener);
        if (DEBUG)
            Log.d(TAG, "<<<binding views end");
    }


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

    private final MediaPlayer.OnErrorListener mOnErrorListener = new MediaPlayer.OnErrorListener() {

        /**
         * handle the error message.
         *@param mp
         *@param what
         *@param extra
         *@return handle the error or not.
         *
         * */

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {

            if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
                Log.d(TAG, "onError: media server died.");
                if (mFmPlayer != null) {
                    mFmPlayer.release();
                    mFmPlayer = null;
                }
                mFmPlayer = new MediaPlayer();
                mFmPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
                mFmPlayer.setOnErrorListener(mOnErrorListener);

                try {
                    mFmPlayer.setDataSource("THIRDPARTY://MEDIAPLAYER_PLAYERTYPE_FM");
                    mFmPlayer.setAudioStreamType(AudioSystem.STREAM_MUSIC);
                    if (mIsPowerUp) {
                        setSpeakPhoneOn(false);
                        mFmPlayer.prepare();
                        mFmPlayer.start();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "onError: setDataSource Error");

                }


            }

            return true;
        }
    };


    @Override
    public void onBackPressed() {
        setResult(20);
        super.onBackPressed();

    }

    @Override
    protected void onDestroy() {
        //at this stage,we need to stop and release.
        this.unregisterReceiver(headSetBroadcastReceiver);
        if (mIsPlaying){
            setMute(true);
            mFmPlayer.stop();
        }
        if (mFmPlayer != null)
            mFmPlayer = null;
        alexTaoFmTestHandler.removeMessages(MESSAGE_POWER_UP);
        alexTaoFmTestHandler = null;

        super.onDestroy();
    }

    //handle some async operate.
    class AlexTaoFmTestHandler extends Handler {
        AlexTaoFmTestHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_POWER_UP:
                    if (handlePowerUpFM()) {
                        startPlayFM();
                    }
                    break;
            }
        }
    }

    private boolean startPlayFM() {
        Log.d(TAG, "startPlayFM: >>>");
        if (mFmPlayer == null || !mIsPowerUp){
            Log.e(TAG, "startPlayFM: "+mIsPowerUp );
            Log.e(TAG, "startPlayFM: mFmPlayer is null when start play" );
            return false;
        }

        if (mFmPlayer.isPlaying()){
            Log.d(TAG, "startPlayFM: player is playing");
            return false;
        }
        try {
            mFmPlayer.prepare();
            mFmPlayer.start();
            mIsPlaying = true;

        } catch (IOException e) {
            e.printStackTrace();
        }


        return true;
    }

    private boolean handlePowerUpFM() {
        Log.d(TAG, "handlePowerUpFM: >>>");
        if (mIsPowerUp) {
            Log.d(TAG, "handlePowerUpFM: fm is already power up.");
            return true;
        }
        if (!mIsDeviceOpened) {
            openDevice();
        }
        Log.d(TAG, "set CURRENT_RX_ON true, CURRENT_TX_ON false");
        FmRadioNative.setFmStatus(CURRENT_RX_ON, true);
        FmRadioNative.setFmStatus(CURRENT_TX_ON, false);
        Log.d(TAG, "service native power up start");
        if (!FmRadioNative.powerUp(mCurrentStation)) {
            Log.e(TAG, "Error: powerup failed.");
            return false;
        }
        Log.d(TAG, "service native power up end");
        mIsPowerUp = true;
        // need mute after power up
        setMute(true);

        return mIsPowerUp;
    }

}
