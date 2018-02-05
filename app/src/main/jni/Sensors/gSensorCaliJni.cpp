/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#define LOG_TAG "gradienterjni native.cpp"
#include "utils/Log.h"

#include <stdio.h>

#include "jni.h"
#include <errno.h>
#include <string.h>

#ifdef __cplusplus
 extern "C" {
#endif
#include "libhwm.h"

static const char *classPathName = "com/zte/engineer/GradienterJni";

/*---------------------------------------------------------------------------*/
#define C_MAX_MEASURE_NUM (20)
#define C_MAX_HWMSEN_EVENT_NUM 4
/*---------------------------------------------------------------------------*/
struct gsc_priv
{
    /*specific data field*/
	int fd;
	
    int  cali_delay;
    int  cali_num;
    int  cali_tolerance;
    bool bUpToDate; 
    HwmData cali_drv;
    HwmData cali_nvram;
    HwmData dat;
};

struct gsc_priv data;

static jint
gsensorClose(JNIEnv *env, jobject thiz, jint x, jint y,jint z) {
	memset(&data,0,sizeof(struct gsc_priv));
    gsensor_close(data.fd);
   // ALOGI("gsensorRead fd=%d",data.fd);
	return 0;
}

static jint
gsensorOpen(JNIEnv *env, jobject thiz, jint x, jint y,jint z) {
	memset(&data,0,sizeof(struct gsc_priv));
    gsensor_open(&data.fd);
  //  ALOGI("gsensorRead fd=%d",data.fd);

	data.cali_delay = 50;
	data.cali_num   = 2;
	data.cali_tolerance = 40;
	return 0;	
}

static jint
gsensorCali(JNIEnv *env, jobject thiz, jint x, jint y,jint z) {
    int err;
			
    if((err = gsensor_calibration(data.fd, data.cali_delay, data.cali_num,
    		data.cali_tolerance, 0, &data.cali_drv)) != 0)
	{
    //	ALOGE("calibrate acc: %d\n", err);
	}
	else if((err = gsensor_set_cali(data.fd, &data.cali_drv)) != 0)
	{
		//ALOGE("set calibration fail: (%s) %d\n", strerror(errno), err);
	}
	else if((err = gsensor_get_cali(data.fd, &data.cali_drv)) != 0)
	{
		//ALOGE("get calibration fail: (%s) %d\n", strerror(errno), err);
	}
	else if((err = gsensor_write_nvram(&data.cali_drv)) != 0)
	{
		//ALOGE("write nvram fail: (%s) %d\n", strerror(errno), err);
	}
	else
	{
		data.cali_delay = data.cali_num = data.cali_tolerance = 0;
	}
	//gsensor_close(data.fd);
    return 0;
}

static jint
gsensorClear(JNIEnv *env, jobject thiz, jint x, jint y,jint z) {
	//ALOGI("%d,%d,%d", x, y, z);
	memset(&data.cali_nvram, 0x00, sizeof(data.cali_nvram));
	memset(&data.cali_drv, 0x00, sizeof(data.cali_drv));
	//gsensor_open(&data.fd);
	int err = gsensor_rst_cali(data.fd);
	if(err)
	{
		//ALOGE("rst calibration: %d\n", err);
	}
	else if((err = gsensor_write_nvram(&data.cali_nvram)) != 0)
	{
		//ALOGE("write nvram: %d\n", err);
	}

	if(err)
	{
		//snprintf(dat->gsc.status, sizeof(data.status), uistr_info_sensor_cali_fail);
	}
	else
	{
		//snprintf(dat->gsc.status, sizeof(data.status), uistr_info_sensor_cali_ok);
		//dat->mod->test_result = FTM_TEST_PASS;
	}
	return 0;
}

static JNINativeMethod methods[] = {
  {"gsensorClear", "(III)I", (int*)gsensorClear },
  {"gsensorCali", "(III)I", (int*)gsensorCali },
  {"gsensorOpen", "(III)I", (int*)gsensorOpen },
  {"gsensorClose", "(III)I", (int*)gsensorClose },
};

/*
 * Register several native methods for one class.
 */
static int registerNativeMethods(JNIEnv* env, const char* className,
    JNINativeMethod* gMethods, int numMethods)
{
    jclass clazz;

    clazz = env->FindClass(className);
    if (clazz == NULL) {
//        ALOGE("Native registration unable to find class '%s'", className);
        return JNI_FALSE;
    }
    if (env->RegisterNatives(clazz, gMethods, numMethods) < 0) {
      //  ALOGE("RegisterNatives failed for '%s'", className);
        return JNI_FALSE;
    }

    return JNI_TRUE;
}

/*
 * Register native methods for all classes we know about.
 *
 * returns JNI_TRUE on success.
 */
static int registerNatives(JNIEnv* env)
{
  if (!registerNativeMethods(env, classPathName,
                 methods, sizeof(methods) / sizeof(methods[0]))) {
    return JNI_FALSE;
  }

  return JNI_TRUE;
}


// ----------------------------------------------------------------------------

/*
 * This is called by the VM when the shared library is first loaded.
 */
 
typedef union {
    JNIEnv* env;
    void* venv;
} UnionJNIEnvToVoid;

jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
    UnionJNIEnvToVoid uenv;
    uenv.venv = NULL;
    jint result = -1;
    JNIEnv* env = NULL;
    
//    ALOGI("JNI_OnLoad");

    if (vm->GetEnv(&uenv.venv, JNI_VERSION_1_4) != JNI_OK) {
       // ALOGE("ERROR: GetEnv failed");
        goto bail;
    }
    env = uenv.env;

    if (registerNatives(env) != JNI_TRUE) {
       // ALOGE("ERROR: registerNatives failed");
        goto bail;
    }
    
    result = JNI_VERSION_1_4;
    
bail:
    return result;
}

#ifdef __cplusplus
 }
#endif
