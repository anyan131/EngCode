#This is a make file about make the library to drive the serial port.
#@author AlexTao NewMobi.
#2018/01/16
#

LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := AlexTaoSerialPort.cpp
LOCAL_SHARED_LIBRARIES := \
    libandroid_runtime \
    libnativehelper \
    libcutils \
    libutils \
    liblog \
    libhardware


LOCAL_MODULE    := libSerialPort

LOCAL_PROGUARD_ENABLED:= disabled
include $(BUILD_SHARED_LIBRARY)



















