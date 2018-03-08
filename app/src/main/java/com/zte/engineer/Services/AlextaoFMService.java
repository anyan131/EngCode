package com.zte.engineer.Services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.AudioSystem;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;

import com.mediatek.fmradio.FmRadioNative;
import com.zte.engineer.BuildConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2018/2/28.
 * This class is for background fm service.
 */

public class AlextaoFMService extends Service {

    //this is for logging.
    private static final String TAG = "AlextaoFMService";

    // RDS events
    // PS
    private static final int RDS_EVENT_PROGRAMNAME = 0x0008;
    // RT
    private static final int RDS_EVENT_LAST_RADIOTEXT = 0x0040;
    // AF
    private static final int RDS_EVENT_AF = 0x0080;

    private static final int HEADSET_PLUG_IN = 1;

    //this is mean that we must plug in the headset or earphone.we cant use the
    //speaker.
    private static final boolean SHORT_ANTENNA_SUPPORT = false;

    // Set audio policy for FM
    // should check AUDIO_POLICY_FORCE_FOR_MEDIA in audio_policy.h
    private static final int FOR_PROPRIETARY = 1;
    // Forced Use value
    private int mForcedUseForMedia;

    // TX and RX interaction
    private static final int CURRENT_RX_ON = 0;
    private static final int CURRENT_TX_ON = 1;
    private static final int CURRENT_TX_SCAN = 2;


    // RDS
    // PS String
    private String mPSString = "";
    // RT String
    private String mLRTextString = "";
    // PS RT
    private boolean mIsPSRTEnabled = false;
    // AF
    private boolean mIsAFEnabled = false;
    // RDS thread use to receive the information send by station
    private Thread mRdsThread = null;
    // record whether RDS thread exit
    private boolean mIsRdsThreadExit = false;


    //below are some state variables.


    // Record whether FM is in native scan state
    private boolean mIsNativeScanning = false;
    // Record whether FM is in scan thread
    private boolean mIsScanning = false;
    // Record whether FM is in seeking state
    private boolean mIsNativeSeeking = false;
    // Record whether FM is in native seek
    private boolean mIsSeeking = false;
    // Record whether searching progress is canceled
    private boolean mIsStopScanCalled = false;
    // Record whether is speaker used
    private boolean mIsSpeakerUsed = false;
    // Record whether device is open
    private boolean mIsDeviceOpen = false;
    // Record whether FM is power up
    private boolean mIsPowerUp = false;
    // Record whether is power uping, if so, should judge in activity back key.
    private boolean mIsPowerUping = false;
    // Record whether service is init
    private boolean mIsServiceInited = false;
    // Fm power down by loss audio focus,should make power down menu item can
    // click
    private boolean mIsMakePowerDown = false;

    // RDS events
    // PS
   // private static final int RDS_EVENT_PROGRAMNAME = 0x0008;
    // RT
   // private static final int RDS_EVENT_LAST_RADIOTEXT = 0x0040;

    private ArrayList<Record> mRecords = new ArrayList<>();


    // Instance variables
    private Context mContext = null;
    private AudioManager mAudioManager = null;
    private ActivityManager mActivityManager = null;
    private MediaPlayer mFmPlayer = null;
    private PowerManager.WakeLock mWakeLock = null;


    // Audio focus is held or not
    private boolean mIsAudioFocusHeld = false;

    private int mCurrentStation = 104;
    // Headset plug state (0:long antenna plug in, 1:long antenna plug out)
    private int mValueHeadSetPlug = 1;

    private final IBinder mBinder = new ServiceBinder();

    private FMBroadCastReciever mFmBroadCastReceiver;

    private FMServiceHandler fmServiceHandler = null;

    private static boolean sIsActivityOnStop = false;

    // /*below are some listeners.*/

    /**
     * use to interact with other voice apps.
     */
    private final AudioManager.OnAudioFocusChangeListener mAudioChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {

                /**
                 * handle audio change ensure message fifo.
                 * */
                @Override
                public void onAudioFocusChange(int focusChange) {
                    Log.d(TAG, "onAudioFocusChange: " + focusChange);
                    switch (focusChange) {
                        case AudioManager.AUDIOFOCUS_LOSS:
                            synchronized (this) {
                                logd("received AUDIOFOCUS_LOSS");
                                mAudioManager.setParameters("AudioFmPreStop=1");
                                setMute(true);
                                exitFm();
                                stopSelf();
                            }
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                            synchronized (this) {
                                mAudioManager.setParameters("AudioFmPreStop=1");
                                setMute(true);
                                Log.d(TAG, "AudioFocus: received AUDIOFOCUS_LOSS_TRANSIENT");
                                stopFmFocusLoss(AudioManager.AUDIOFOCUS_LOSS_TRANSIENT);
                            }
                            break;
                        case AudioManager.AUDIOFOCUS_GAIN:
                            synchronized (this) {
                                Log.d(TAG, "AudioFocus: received AUDIOFOCUS_GAIN");
                                updateAudioFocusAync(AudioManager.AUDIOFOCUS_GAIN);
                            }
                            break;

                        default:
                            Log.d(TAG, "AudioFocus: Audio focus change, but not need handle");
                            break;
                    }
                }
            };

    /**
     * This class is for handle the FM player error.
     */
    private final MediaPlayer.OnErrorListener mOnErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {

            if (MediaPlayer.MEDIA_ERROR_SERVER_DIED == what) {
                logd("Media error server died.");
                if (null != mFmPlayer) {
                    mFmPlayer.release();
                    mFmPlayer = null;
                }

                mFmPlayer = new MediaPlayer();
                mFmPlayer.setOnErrorListener(mOnErrorListener);
                try {
                    mFmPlayer.setDataSource("THIRDPARTY://MEDIAPLAYER_PLAYERTYPE_FM");
                    mFmPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    // if the fm device is power up.
                    if (mIsPowerUp) {
                        setSpeakerPhoneOn(mIsSpeakerUsed);
                        mFmPlayer.prepareAsync();
                        mFmPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mFmPlayer.start();
                            }
                        });

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    };

    /**
     * when audio focus changed,will send messages to a handler thread.
     *
     * @param focusState AudioManager state.
     */
    private synchronized void updateAudioFocusAync(int focusState) {

        Log.d(TAG, "updateAudioFocusAync: focusState = " + focusState);
        final int bundleSize = 1;
        Bundle bundle = new Bundle(bundleSize);
        bundle.putInt(FMConstants.KEY_AUDIOFOCUS_CHANGED, focusState);
        Message msg = fmServiceHandler.obtainMessage(FMConstants.MSGID_AUDIOFOCUS_CHANGED);
        msg.setData(bundle);
        fmServiceHandler.sendMessage(msg);
    }

    /**
     * exit FMRadio application.
     */
    private void exitFm() {
        Log.d(TAG, "exitFm: start");
        mIsAudioFocusHeld = false;

        if (mIsNativeScanning || mIsNativeSeeking) {
            stopScan();
        }

        fmServiceHandler.removeCallbacksAndMessages(null);
        fmServiceHandler.removeMessages(FMConstants.MSGID_FM_EXIT);
        fmServiceHandler.sendEmptyMessage(FMConstants.MSGID_FM_EXIT);
        Log.d(TAG, "service.exitFm end");

    }


    public boolean isAntennaAvailable() {
        return mAudioManager.isWiredHeadsetOn();
    }


    /**
     * Broadcast monitor external event, Other app want FM stop, Phone shut
     * down, screen state, headset state
     */
    private class FMBroadCastReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String command = intent.getStringExtra("command");
            logd("action is " + action + "," + "command is" + command);

            //headset is plug in or plug out.
            if (Intent.ACTION_HEADSET_PLUG.equals(action)) {
                //switch antenna should not impact the audio state.
                mValueHeadSetPlug = intent
                        .getIntExtra("state", -1) == HEADSET_PLUG_IN ? 0 : 1;
                switchAntennaAsync(mValueHeadSetPlug);
                //whether short antenna supports.
                if (SHORT_ANTENNA_SUPPORT) {
                    boolean isSwitch = switchAntenna(mValueHeadSetPlug) == 0;
                    logd("onReceive in switchAntenna : isSwitch" + isSwitch);
                    //plug out -> Speaker mode,plug in -> earphone mode.
                    boolean plugInEarPhone = mValueHeadSetPlug == 0;
                    if (plugInEarPhone) {
                        mForcedUseForMedia = AudioSystem.FORCE_NONE;
                        mIsSpeakerUsed = false;
                    }
                    //notify UI to change to earphone mode.
                    Bundle bundle = new Bundle(2);
                    bundle.putInt(FMConstants.CALLBACK_FLAG, FMConstants.LISTEN_SPEAKER_MODE_CHANGED);
                    bundle.putBoolean(FMConstants.KEY_IS_SPEAKER_MODE, !plugInEarPhone);
                    notifyActivityStateChanged(bundle);
                    powerUpAutoIfNeed();
                } else {
                    // Avoid Service is killed,and receive headset plug in
                    // broadcast again
                    if (!mIsServiceInited) {
                        logd("onReceive switch antenna :service is not init");
                        powerUpAutoIfNeed();
                        return;
                    }

                    /*
                    * if earphone is plugged in,and activity is foreground
                    *  power up FM automatically.
                    * */
                    if ((mValueHeadSetPlug == 0) && isActivityForeground()) {
                        Log.d(TAG, "onReceive: need power up fm async");
                        powerUpAsync(1043);
                    } else if (1 == mValueHeadSetPlug) {
                        Log.d(TAG, "plug out earphone, need to stop fm");
                        //plug out the earphone. we should setMute anyway.
                        setMute(true);
                        fmServiceHandler.removeMessages(FMConstants.MSGID_SCAN_FINISHED);
                        fmServiceHandler.removeMessages(FMConstants.MSGID_SEEK_FINISHED);
                        fmServiceHandler.removeMessages(FMConstants.MSGID_TUNE_FINISHED);
                        fmServiceHandler.removeMessages(FMConstants.MSGID_POWERUP_FINISHED);
                        fmServiceHandler.removeMessages(FMConstants.MSGID_POWERDOWN_FINISHED);
                        stopFmFocusLoss(AudioManager.AUDIOFOCUS_LOSS);
                        setSpeakerPhoneOn(false);

                        //notify UI change to speaker mode. false means not speaker mode
                        Bundle bundle = new Bundle(2);
                        bundle.putInt(FMConstants.CALLBACK_FLAG, FMConstants.LISTEN_SPEAKER_MODE_CHANGED);
                        bundle.putBoolean(FMConstants.KEY_IS_SPEAKER_MODE, false);
                        notifyActivityStateChanged(bundle);
                    }
                }
            }
        }
    }


    private static class Record {
        int hashCode;
        FMListener mCallback;
    }

    /**
     * Set FM audio from speaker or not.
     *
     * @param isSpeaker
     */
    public void setSpeakerPhoneOn(boolean isSpeaker) {
        Log.d(TAG, "setSpeakerPhoneOn: isSpeaker: " + isSpeaker);
        mForcedUseForMedia = isSpeaker ? AudioSystem.FORCE_SPEAKER : AudioSystem.FORCE_NONE;
        AudioSystem.setForceUse(1, mForcedUseForMedia);
        mIsSpeakerUsed = isSpeaker;
        Log.d(TAG, "<<< FmRadioService.useSpeaker");
    }


    /**
     * Need to power up in two cases:
     * case 1: Launcher click FM app, then quickly click Power key to lock phone.
     * case 2: Launcher click FM app, then quickly click Home key.
     * Because power up action is in FmRadioActivity.onServiceConnected(), these two cases
     * will not callback onServiceConnected() cause FmRadioActivity.onStop() has called unbind()
     */
    private void powerUpAutoIfNeed() {
        //here if plug in earphone
        if (mValueHeadSetPlug == 0) {
            if (!mIsPowerUping && !mIsPowerUp && sIsActivityOnStop) {
                logw("enter the method powerupauto.because user do something quickly.");
                powerUpAsync(computeFrequency(1043));
            }
        }

    }

    /**
     * power up FM,and make FM voice output from earphone.
     *
     * @param frequency
     */
    public void powerUpAsync(float frequency) {
        mIsPowerUping = true;
        final int bundleSize = 1;
        fmServiceHandler.removeMessages(FMConstants.MSGID_POWERUP_FINISHED);
        fmServiceHandler.removeMessages(FMConstants.MSGID_POWERDOWN_FINISHED);
        Bundle bundle = new Bundle(bundleSize);
        bundle.putFloat("frequency", frequency);
        Message message = fmServiceHandler.obtainMessage(FMConstants.MSGID_POWERUP_FINISHED);
        message.setData(bundle);
        fmServiceHandler.sendMessage(message);

    }


    /**
     * callback from service to activity
     *
     * @param bundle the message to the activity.
     */
    private void notifyActivityStateChanged(Bundle bundle) {
        if (!mRecords.isEmpty()) {
            logd(">>>notifyActivityStateChanged :clients" + mRecords.size());
            synchronized (mRecords) {
                Iterator<Record> iterator = mRecords.iterator();
                while (iterator.hasNext()) {
                    Record record = iterator.next();
                    FMListener listener = record.mCallback;
                    if (listener == null) {
                        iterator.remove();
                        return;
                    }
                    listener.onCallback(bundle);
                }
            }
        }

    }

    /**
     * Need native support whether antenna support interface.
     *
     * @param antennaType (0 :long antenna,1 short antenna)
     * @return (0 success, 1 failed, 2 not support)
     */
    private int switchAntenna(int antennaType) {
        logd(">>>switchAntenna, the antenna type is" + antennaType);
        // if fm not power up, this function will flag whether has earphone.
        int result = FmRadioNative.switchAntenna(antennaType);
        logd("<<<switchAntenna,the result is " + result);

        return result;
    }

    /**
     * switch antenna.There are two types of antenna(long and short).actually long antenna means we
     * plug in our headset while short antenna is on the board.If long antenna(most time is this case)
     * we must plug in our headset to receive FM.
     *
     * @param antennaType 0 : long antenna, 1: short antenna
     */
    public void switchAntennaAsync(int antennaType) {
        final int bundleSize = 1;
        fmServiceHandler.removeMessages(FMConstants.MSGID_SWITCH_ANNTENNA);
        Bundle bundle = new Bundle(bundleSize);
        bundle.putInt(FMConstants.SWITCH_ANNTENNA_VALUE, antennaType);
        Message message = fmServiceHandler.obtainMessage(FMConstants.MSGID_SWITCH_ANNTENNA);
        message.setData(bundle);
        fmServiceHandler.sendMessage(message);
    }


    public class ServiceBinder extends Binder {
        public AlextaoFMService getService() {
            return AlextaoFMService.this;
        }

    }


    class FMServiceHandler extends Handler {
        public FMServiceHandler(Looper looper) {
            super(looper);
        }

        //todo here we need to add some new method in
        //todo in message.
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle;
            boolean isPowerUp = false;
            boolean isSwitch = true;
            switch (msg.what) {

                //power up
                case FMConstants.MSGID_POWERUP_FINISHED:
                    bundle = msg.getData();
                    handlePowerUp(bundle);

                    break;


            }

        }
    }

    /**
     * handle power up,and callback to activity.
     *
     * @param bundle it contains the frequency.
     */
    private void handlePowerUp(Bundle bundle) {
        boolean isPowerUp = false;
        boolean isSwitch = true;
        Log.d(TAG, "handlePowerUp: start");
        float currentFrequency = bundle.getFloat("frequency");

        if (!SHORT_ANTENNA_SUPPORT && !isAntennaAvailable()) {
            Log.d(TAG, "handlePowerUp: callback to activity,earphone is not ready");
            mIsPowerUping = false;
            bundle = new Bundle(2);
            bundle.putInt(FMConstants.CALLBACK_FLAG, FMConstants.MSGID_SWITCH_ANNTENNA);
            bundle.putBoolean(FMConstants.KEY_IS_SWITCH_ANNTENNA, false);
            notifyActivityStateChanged(bundle);
            return;
        }
        if (powerUpFm(currentFrequency)) {
            isPowerUp = startPlayFm(currentFrequency);
            Log.d(TAG, "handlePowerUp: " + isPowerUp);
        }
        mIsPowerUping = false;


    }

    private boolean startPlayFm(float currentFrequency) {

        enableAudioFm(true);
        setMute(false);
        return mIsPowerUp;

    }

    private void enableAudioFm(boolean enable) {
        if (!mIsPowerUp || mFmPlayer == null) {
            logd("mfmplayer is null when enable audio.");
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
            mFmPlayer.prepareAsync();
            mFmPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mFmPlayer.start();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Exception: Cannot call MediaPlayer prepare.", e);
        }

    }


    private boolean powerUpFm(float currentFrequency) {
        Log.d(TAG, "powerUpFm: frequency is" + currentFrequency);
        if (mIsPowerUp) {
            Log.d(TAG, "powerUpFm: FM is already powered up" + mIsPowerUp);
            return true;
        }

        if (!requestAudioFocus()) {
            return false;
        }
        if (!mIsDeviceOpen) {
            openDevice();
        }

        FmRadioNative.setFmStatus(CURRENT_RX_ON, true);
        FmRadioNative.setFmStatus(CURRENT_TX_ON, false);

        if (!FmRadioNative.powerUp(currentFrequency)) {
            Log.e(TAG, "Error: powerup failed.");
            return false;
        }

        mIsPowerUp = true;


        return mIsPowerUp;
    }

    /**
     * request the audio focus.
     *
     * @return
     */
    private boolean requestAudioFocus() {
        if (mIsAudioFocusHeld) {
            return true;
        }
        int audioFocus = mAudioManager.requestAudioFocus(mAudioChangeListener, AudioSystem.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        return false;
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "AlextaoFMService.onBind>>>");
        return mBinder;
    }


    //if debug,we need this log.d feature.
    public void logd(String log) {
        if (BuildConfig.DEBUG)
            Log.d(TAG, log);
    }

    // the encapsulation of Log.w() method
    public void logw(String log) {
        Log.w(TAG, log);
    }

    public static float computeFrequency(int station) {
        return station / 10;
    }

    /**
     * check the activity is foreground or background
     *
     * @return
     */
    public boolean isActivityForeground() {
        boolean isForeground = true;
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos =
                mActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : appProcessInfos) {
            if (info.processName.equals(mContext.getPackageName())) {
                int importance = info.importance;
                if (importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND ||
                        importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                    logd("isActivityForeground is foreground");
                    isForeground = true;
                } else {
                    logd("isActivityForeground is background");
                    isForeground = false;
                }
                break;
            }
        }
        return isForeground;
    }

    private int setMute(boolean mute) {
        if (!mIsPowerUp) {
            Log.w(TAG, "setMute: FM is not powered up");
            return -1;
        }
        Log.d(TAG, ">>> FmService.setMute: " + mute);
        int iRet = FmRadioNative.setMute(mute);
        Log.d(TAG, "<<< FmService.setMute: " + iRet);
        return iRet;
    }

    private void stopFmFocusLoss(int focusState) {
        mIsAudioFocusHeld = false;
        if (mIsNativeScanning || mIsNativeSeeking) {
            stopScan();
            Log.d(TAG, "need to stop FM, so stop scan channel.");
        }
        updateAudioFocusStateAsync(focusState);
        Log.d(TAG, "setFmFocusLoss: need to stop FM,so power down it.");

    }

    /**
     * Audio focus state changed. It will send message to handler thread.
     * synchronized makes sure this one message can go into this method.
     *
     * @param focusState AudioManager state
     */
    private synchronized void updateAudioFocusStateAsync(int focusState) {
        Log.d(TAG, "updateAudioFocusStateAsync: focusState" + focusState);
        final int bundleSize = 1;
        Bundle bundle = new Bundle(bundleSize);
        bundle.putInt(FMConstants.KEY_AUDIOFOCUS_CHANGED, focusState);
        Message message = fmServiceHandler.obtainMessage(FMConstants.MSGID_AUDIOFOCUS_CHANGED);
        message.setData(bundle);
        fmServiceHandler.sendMessage(message);

    }

    /**
     * stop scan process.
     *
     * @return true if can stop, false can not stop.
     */
    public boolean stopScan() {
        Log.d(TAG, "stopScan: >>");
        if (!mIsPowerUp) {

            Log.w(TAG, "stopScan: FM is not powered up.");
            return false;
        }
        boolean bRet = false;
        fmServiceHandler.removeMessages(FMConstants.MSGID_SCAN_FINISHED);
        fmServiceHandler.removeMessages(FMConstants.MSGID_SEEK_FINISHED);
        if (mIsNativeSeeking || mIsNativeScanning) {
            mIsStopScanCalled = true;
            Log.d(TAG, "native stop scan:start");
            bRet = FmRadioNative.stopScan();
            Log.d(TAG, "native stop scan:end --" + bRet);
        }
        Log.d(TAG, "<<< FmRadioService.stopScan: " + bRet);
        return bRet;
    }

    private boolean initFmPlayer() {
        mFmPlayer = new MediaPlayer();
        mFmPlayer.setOnErrorListener(mOnErrorListener);
        try {
            mFmPlayer.setDataSource("THIRDPARTY://MEDIAPLAYER_PLAYERTYPE_FM");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "initFmPlayer: when set dataSource error ");
            return false;
        }
        mFmPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        return true;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, ">>> AlextaoFmService onCreate");
        logd("short antenna support " + SHORT_ANTENNA_SUPPORT);
        mContext = getApplicationContext();
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        if (!initFmPlayer()) {
            Log.e(TAG, "onCreate when initFM error.");
            return;
        }

        registerFmBroadCastReceiver();

        HandlerThread handlerThread = new HandlerThread("AlexTaoFMServiceThread");
        handlerThread.start();
        fmServiceHandler = new FMServiceHandler(handlerThread.getLooper());

        openDevice();

        setSpeakerPhoneOn(mIsSpeakerUsed);
        Log.d(TAG, "onCreate: >>>");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    private void registerFmBroadCastReceiver() {
        IntentFilter filter = new IntentFilter();

        filter.addAction(Intent.ACTION_SHUTDOWN);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_HEADSET_PLUG);

        mFmBroadCastReceiver = new FMBroadCastReciever();
        registerReceiver(mFmBroadCastReceiver, filter);

    }

    /**
     * open FM device, should be call before power up
     *
     * @return true if FM device open, false FM device not open
     */
    private boolean openDevice() {
        Log.d(TAG, ">>> FmRadioService.openDevice");
        if (!mIsDeviceOpen) {
            mIsDeviceOpen = FmRadioNative.openDev();
        }
        Log.d(TAG, "<<< FmRadioService.openDevice: " + mIsDeviceOpen);
        return mIsDeviceOpen;
    }

    public static void setsIsActivityOnStop(boolean isStop) {
        sIsActivityOnStop = isStop;
    }

    /**
     * Check service is initialed or not
     *
     * @return true if initialed, otherwise return false
     */
    public boolean ismIsServiceInited() {
        return mIsServiceInited;
    }

    public void initSerivce(int currentStation) {
        mIsServiceInited = true;
        mCurrentStation = currentStation;
    }

    public boolean ismIsDeviceOpen() {
        return mIsDeviceOpen;
    }

    public boolean ismIsPowerUp() {
        return mIsPowerUp;
    }
}
