#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <sys/ioctl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include "utils/Log.h"
#include "jni.h"

#include "utils/String8.h"

using namespace android;

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jint JNICALL Java_com_zte_engineer_AlspsCali_AlspsGetVal(JNIEnv *env, jobject c) {
    int fd = -1;
    char buffer[20] = {0};
    int psFlag, psVal;

    fd = open("/proc/tmd2771closeaway", O_RDWR, 0);
    if (fd < 0) {
        return -1;
    } else {
        read(fd, buffer, 15);
        sscanf(buffer, "%d:%d", &psFlag, &psVal);
        close(fd);
        if (psFlag == 0) {
            return -1;
        } else {
            return psVal;
        }
    }
}

JNIEXPORT void JNICALL
Java_com_zte_engineer_AlspsCali_AlspsSetVal(JNIEnv *env, jobject c, jint psClose, jint psAway) {
    int fd = -1;
    char buffer[20] = {0};
    int psFlag, psVal;

    fd = open("/proc/tmd2771closeaway", O_RDWR, 0);
    if (fd < 0) {
        return;
    } else {
        sprintf(buffer, "%d:%d", psClose, psAway);
        write(fd, buffer, 15);
        close(fd);
    }
}


#ifdef __cplusplus
}
#endif
