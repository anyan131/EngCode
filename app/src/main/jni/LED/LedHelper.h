//
// Created by Administrator on 2018/5/22.
//
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <string.h>
#include <stdint.h>
#include <termios.h>
#include <android/log.h>
#include <sys/select.h>
#include <jni.h>

#ifndef NEWENGCODE_LEDHELPER_H
#define NEWENGCODE_LEDHELPER_H

#define LOG_TAG "LED_LOG_TAG"
#define DEBUGGING_MODE true
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

//SOME PATHS DEFINE HERE

#define RED_LED_PATH "/sys/class/leds/red/brightness"
#define BLUE_LED_PATH "/sys/class/leds/blue/brightness"
#define GREEN_LED_PATH "/sys/class/leds/green/brightness"



#endif //NEWENGCODE_LEDHELPER_H
