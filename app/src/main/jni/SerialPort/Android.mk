#This is a make file about make the library to drive the serial port.
#@author AlexTao NewMobi.
#2018/01/16
#

LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_SRC_FILES := SerialPort.cpp
LOCAL_LDLIBS := -llog
LOCAL_C_INCLUDES := $(JNI_H_INCLUDES)
LOCAL_MODULE_TAGS := optional
LOCAL_MODULE := libnewmobiSerialPort
include $(BUILD_SHARED_LIBRARY)


















