//
// Created by Alex on 2018/5/22.
//

#include "TestSign.h"
#define SignDataNum 200


char test_item[] ="ver*;bat*;gpio*;lcm*;bl*;tp*;fcam*;bcam*;key*;vib*;ring*;loop*;ear*;rev*;sim*;imei*;sd*;bt*;wifi*;fm*;uart*;gps*;i2c*;led*;rf*;";
jboolean SignInit(JNIEnv *env ,jobject obj)
{
    char buf[SignDataNum];
    int fd = open(TestSign_PATH, O_RDWR);
    //open failed here we just return.
    if (fd < 0) {
        LOGE("open %s failed", TestSign_PATH);
        return 0;
    }

    memset(buf,0x00,SignDataNum);
    strcpy(buf,test_item);
    int count = write(fd, buf, SignDataNum);
    LOGE("count:%d",  count);
    close(fd);
    if (count != 200) {
        LOGE("write file (%s) fail, count :%d", TestSign_PATH, count);
        return 0;
    }

    return true;

}
jboolean SignAllClear(JNIEnv *env,jobject obj)
{
    char buf[SignDataNum];
    int fd = open(TestSign_PATH, O_RDWR);
    //open failed here we just return.
    if (fd < 0) {
        LOGE("open %s failed", TestSign_PATH);
        return 0;
    }

    memset(buf,0x00,SignDataNum);
    int count = write(fd, buf, SignDataNum);
    LOGE("count:%d",  count);
    close(fd);
    if (count != 200) {
        LOGE("write file (%s) fail, count :%d", TestSign_PATH, count);
        return 0;
    }

    return true;

}


 jbyteArray  SignAllRead(JNIEnv *env,jobject thiz)
{
    char buf[SignDataNum];

    int fd = open(TestSign_PATH, O_RDWR);
    //open failed here we just return.
    if (fd < 0) {
        LOGE("open %s failed", TestSign_PATH);
        return 0;
    }

    int count = read(fd, buf, SignDataNum);
    LOGE("%s:%d", buf, count);

    jbyteArray array =(*env).NewByteArray(SignDataNum);

    (*env).SetByteArrayRegion(array, 0, SignDataNum, (const jbyte *) buf);
    close(fd);

    return array;

}

jboolean  SignAllWrite(JNIEnv *env,jobject thiz,jbyteArray array)
{
    //char buf[SignDataNum];

    int fd = open(TestSign_PATH, O_RDWR);
    //open failed here we just return.
    if (fd < 0) {
        LOGE("open %s failed", TestSign_PATH);
        return 0;
    }
    jbyte *buffer = (*env).GetByteArrayElements(array, NULL);
    int count = write(fd, buffer, SignDataNum);
    LOGE("%s:%d", buffer, count);
    (*env).ReleaseByteArrayElements(array,buffer,0);
    close(fd);

    return JNI_TRUE;

}
///JNI注册的模板代码。


static const char *classPath = "com/newmobi/iic/TestSign";

static JNINativeMethod getMethods[] = {
        {"JNISignInit", "()Z", (void *) SignInit},
        {"JNISignflagAllClear", "()Z", (void *) SignAllClear},
        {"JNISignAllRead", "()[B", (void *) SignAllRead},
        {"JNISignAllWrite", "([B)Z", (void *) SignAllWrite},
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