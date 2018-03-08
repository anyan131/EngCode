package com.zte.engineer.Services;

/**
 * Created by Administrator on 2018/3/1.
 * some constants stored here and work for FM.
 */

public class FMConstants {

    // Bundle keys
    static final String SWITCH_ANNTENNA_VALUE = "switch_anntenna_value";
    static final String CALLBACK_FLAG = "callback_flag";
    static final String KEY_IS_POWER_UP = "key_is_power_up";
    static final String KEY_IS_POWER_DOWN = "key_is_power_down";
    static final String KEY_IS_SWITCH_ANNTENNA = "key_is_switch_anntenna";
    static final String KEY_IS_TUNE = "key_is_tune";
    static final String KEY_TUNE_TO_STATION = "key_tune_to_station";
    static final String KEY_IS_SEEK = "key_is_seek";
    static final String KEY_SEEK_TO_STATION = "key_seek_to_station";
    static final String KEY_IS_SCAN = "key_is_scan";
    static final String KEY_RDS_STATION = "key_rds_station";
    static final String KEY_PS_INFO = "key_ps_info";
    static final String KEY_RT_INFO = "key_rt_info";
    static final String KEY_STATION_NUM = "key_station_num";

    // For change speaker/earphone mode
    static final String KEY_IS_SPEAKER_MODE = "key_is_speaker_mode";


    // Message to handle
    static final int MSGID_UPDATE_RDS = 1;
    static final int MSGID_UPDATE_CURRENT_STATION = 2;
    static final int MSGID_ANTENNA_UNAVAILABE = 3;
    static final int MSGID_SWITCH_ANNTENNA = 4;
    static final int MSGID_SET_RDS_FINISHED = 5;
    static final int MSGID_SET_CHANNEL_FINISHED = 6;
    static final int MSGID_SET_MUTE_FINISHED = 7;

    // Fm main
    static final int MSGID_POWERUP_FINISHED = 9;
    static final int MSGID_POWERDOWN_FINISHED = 10;
    static final int MSGID_FM_EXIT = 11;
    static final int MSGID_SCAN_CANCELED = 12;
    static final int MSGID_SCAN_FINISHED = 13;
    static final int MSGID_AUDIOFOCUS_FAILED = 14;
    static final int MSGID_TUNE_FINISHED = 15;
    static final int MSGID_SEEK_FINISHED = 16;
    static final int MSGID_ACTIVE_AF_FINISHED = 18;

    // FM Record state changed
    static final int LISTEN_SPEAKER_MODE_CHANGED = 0x00101000; // 1052672
    // Audio focus related
    static final String KEY_AUDIOFOCUS_CHANGED = "key_audiofocus_changed";
    // Audio focus related
    static final int MSGID_AUDIOFOCUS_CHANGED = 30;
}
