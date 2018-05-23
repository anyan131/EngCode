//
// Created by Alex on 2018/5/22.
//

#include "LedHelper.h"
int write_to_file(const char *path, const char *buf, int size, bool force) {
    if (!path) {
        LOGE("null path to write");
        return 0;
    }

    int fd = open(path, O_RDWR);
    //open failed here we just return.
    if (fd < 0) {
        LOGE("open %s failed", path);
        return 0;
    }
    int count = write(fd, buf, size);
    close(fd);
    if (count != size) {
        LOGE("write file (%s) fail, count :%d", path, count);
        return 0;
    }

    return count;
}

//工具类方法。
void set_int_value(const char *path, const int value, const bool force) {
    char buf[32];
    sprintf(buf, "%d", value);
    write_to_file(path, buf, strlen(buf), force);
}

//读取文件值。
int read_from_file(const char *path, char *buf, const int size, const bool force) {
    if (!path) {
        return 0;
    }

    int fd = open(path, O_RDONLY);
    if (fd < 0) {
        //open failed.
        if (force) {
            LOGE("OPEN FILE %s FAILED!", path);
        }
        return 0;
    }
    int count = read(fd, buf, size);
    if (count > 0) {
        count = (count < size) ? count : size - 1;
        while (count > 0 && buf[count - 1] == '\n') count--;
        buf[count] = '\0';
    } else {
        buf[0] = '\0';
    }
    close(fd);
    return count;
}

//read int value form a specific path.
int get_int_value(const char *path, const bool force) {
    int size = 32;
    char buf[size];
    if (!read_from_file(path, buf, size, force)) {
        return 0;
    }
    return atoi(buf);
}

jboolean controlLed(JNIEnv *env,jobject obj,jint flag) {

    int led_status;
    //first we need to make sure which led.
    switch (flag) {
        //red
        case 0:
            led_status = get_int_value(RED_LED_PATH, true);
            LOGD("the red led status now is %d", led_status);
            if (led_status < 255) {
                set_int_value(RED_LED_PATH, 255, true);
            } else {
                set_int_value(RED_LED_PATH, 0, true);
            }
            break;
            //blue
        case 1:
            led_status = get_int_value(BLUE_LED_PATH, true);
            LOGD("the blue led status now is %d", led_status);
            if (led_status < 255) {
                set_int_value(BLUE_LED_PATH, 255, true);
            } else {
                set_int_value(BLUE_LED_PATH, 0, true);
            }
            break;
            //green
        case 2:
            led_status = get_int_value(GREEN_LED_PATH, true);
            LOGD("the green led status now is %d", led_status);
            if (led_status < 255) {
                set_int_value(GREEN_LED_PATH, 255, true);
            } else {
                set_int_value(GREEN_LED_PATH, 0, true);
            }
            break;
    }

    return true;

}


///JNI注册的模板代码。


static const char *classPath = "com/newmobi/iic/LEDHelper";

static JNINativeMethod getMethods[] = {
        {"JNIcontrolLed",  "(I)Z", (void *) controlLed},
};



static int registerNativeMethods(JNIEnv *env, const char *className, JNINativeMethod *gmethod, int methodNum) {
    jclass clazz;
    //here we need to find the class name.
    clazz = env->FindClass(className);
    if (clazz == NULL) {
        return JNI_FALSE;
    }
    if (env->RegisterNatives(clazz, gmethod, methodNum) < 0) {
        return JNI_FALSE;
    }
    return JNI_TRUE;

}

static int registerNatives(JNIEnv *env) {
    //const char *className = "com/newmobi/iic/IICHelper";
    return registerNativeMethods(env, classPath, getMethods,
                                 sizeof(getMethods) / sizeof(getMethods[0]));
}


JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reversed) {
    JNIEnv *env = NULL;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return -1;
    }
    //assert(env != NULL);
    if (!registerNatives(env)) {
        return -1;
    }
    return JNI_VERSION_1_6;
}