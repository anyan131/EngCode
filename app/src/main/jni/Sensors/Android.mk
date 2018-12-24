LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES :=  com_zte_engineer_LcdPowerControl.cpp \
					gSensorCaliJni.cpp \
					com_zte_engineer_AlspsCali.cpp
									
LOCAL_C_INCLUDES := $(JNI_H_INCLUDE) \
		kernel-3.10/drivers/misc/mediatek/video/mt6582 \
		$(TOP)/frameworks/base/include/media	
		
LOCAL_SHARED_LIBRARIES := \
	  libnativehelper \
	  libandroid_runtime \
	  libutils \
	  libmedia \
	  liblog \
	  libhwm \
	  libnvram 
	  
LOCAL_LDFLAGS :=  -llog -lEGL	
LOCAL_MODULE := libcom_zte_engineer_jni
LOCAL_PRELINK_MODULE := false

include $(BUILD_SHARED_LIBRARY)

