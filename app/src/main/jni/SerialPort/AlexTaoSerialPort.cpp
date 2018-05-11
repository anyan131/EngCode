//
// Created by Administrator on 2018/3/6.
//

#include <string.h>

#include <jni.h>

#include <android/log.h>
#include <sys/ioctl.h>

#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <termios.h>
#include <errno.h>
#include <poll.h>
#include <stdlib.h>
#include <ctype.h>

#define LOGTAG "AlextaoSerialPort"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOGTAG,__VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,LOGTAG,__VA_ARGS__)
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE,LOGTAG,__VA_ARGS__)

#define BUFFSIZE 20

static const char *className = "com/zte/engineer/SerialPort";
char ttyPath[100] = {0};
int SerialFd = -1;

jint openSerialPort(JNIEnv *env, jobject jobject1, jint uartno) {
    snprintf(ttyPath, 100, "/dev/ttyMT%d", uartno);

    SerialFd = open(ttyPath, O_RDWR | O_NOCTTY | O_NONBLOCK);
    LOGD("the serialfd for ttyMT%d is %d",uartno,SerialFd);
    if (SerialFd < 0) {
        return -1;
    } else {
        close(SerialFd);
        return 0;
    }
}


jint UartTest(JNIEnv *env, jobject jobject1, jint uartno, jint Baud, jint Databit, jint Parity,
              jint Stopbit) {
    struct termios termios_new;


    int len, Ret, first_rec = 0, Result = 0;
    int i=0;
//unsigned char Databit=8, stopbit=1, parity=0, fctl=0,
    unsigned char buf[BUFFSIZE] = {'h', 'e', 'l', 'l', 'o'};
    unsigned char rec_buf[BUFFSIZE] = {0};
    unsigned char rec_data[BUFFSIZE] = {0};
    struct pollfd fds = {0};

    LOGD("uartno= %d, Baud=%d, Databit=%d, Parity=%d, Stopbit=%d\n",
         uartno, Baud, Databit, Parity, Stopbit);


    snprintf(ttyPath, 100, "/dev/ttyMT%d", uartno);

    SerialFd = open(ttyPath, O_RDWR | O_NOCTTY | O_NONBLOCK);
    if (SerialFd < 0) {
        LOGI("Failed[%d] to open %s, errno=%d\n", SerialFd, ttyPath, errno);
        return 0;
        //Alextao try to block here for debug.
        // exit(2);
    } else {
        LOGD("%s[%d] is open\n", ttyPath, SerialFd);
    }

//清零并初始化串口属性数据结构
    memset(&termios_new, 0, sizeof(termios_new));
    cfmakeraw(&termios_new);

//设置串口波特率
    cfsetispeed(&termios_new, B115200);
    cfsetospeed(&termios_new, B115200);

//数据位
    termios_new.c_cflag &= ~CSIZE;
    switch (Databit) {
        case 5:
            termios_new.c_cflag |= CS5;
            break;
        case 6:
            termios_new.c_cflag |= CS6;
            break;
        case 7:
            termios_new.c_cflag |= CS7;
            break;
        default:
            termios_new.c_cflag |= CS8;
            break;
    }

//校验方式
    switch (Parity) {
        case 0:
            termios_new.c_cflag &= ~PARENB;
            break;
        case 1:
            termios_new.c_cflag |= PARENB;
            termios_new.c_cflag &= ~PARODD;
            break;
        case 2:
            termios_new.c_cflag |= PARENB;
            termios_new.c_cflag |= PARODD;
            break;
        default:
            termios_new.c_cflag &= ~PARENB;
            break;
    }

//停止位
    if (Stopbit == 2) {
        termios_new.c_cflag |= CSTOPB;
    } else {
        termios_new.c_cflag &= ~CSTOPB;
    }

//其他属性
    termios_new.c_cflag &= ~CRTSCTS;        //无流控制
    termios_new.c_oflag &= ~OPOST;
    termios_new.c_cflag |= CLOCAL;
    termios_new.c_cflag |= CREAD;
    termios_new.c_cc[VMIN] = 0;
    termios_new.c_cc[VTIME] = 0;

//调用设置函数
    tcflush(SerialFd, TCIFLUSH);
    tcsetattr(SerialFd, TCSANOW, &termios_new);

    LOGD("tty setted\n");
    len = 5;

    fds.fd = SerialFd;
    fds.events = POLLIN;

//while (1) {
    Ret = write(SerialFd, buf, len);
    if (Ret != len) {
        LOGD("Failed[%d] to write %d Bytes, errno=%d\n", Ret, len, errno);
        goto exit;
    } else {
        LOGD("tty writed %d byte data\n", Ret);
    }

    memset(rec_data, 0, BUFFSIZE);

    while (1) {

        Ret = poll(&fds, 1, 100);
        if (Ret != 0) {
            i++;
            if(i>200){
                goto exit;
            }
            LOGD("Poll ret=%d, revent=%hd,errno=%d\n", Ret, fds.revents, errno);
        } else {
            break;
        }

        memset(rec_buf, 0, BUFFSIZE);
        Ret = read(SerialFd, rec_buf, BUFFSIZE);

        if (Ret > 0) {
            LOGD("Recv Data[%dB]:", Ret);

            for (i = 0; i < Ret; i++) {
                LOGD("%c ", rec_buf[i]);
            }
//printf("%s", rec_buf);
//printf("\n");

            if (Ret == 5)
                memcpy(rec_data, rec_buf, sizeof(rec_buf));

            else {
                if (rec_buf[0] == 'h') {
                    first_rec = Ret;
                    LOGD("first_rec=%d\n", first_rec);
                    memcpy(rec_data, rec_buf, first_rec);
                } else
                    LOGD("lrz rec_buf = %s\n",rec_buf);
                    memcpy(rec_data + first_rec, rec_buf, 5 - first_rec);
            }

        } else {
            break;
        }
    }

    if (strcmp((char *) rec_data, "hello") == 0) {
        LOGD("test ok!\n");
        Result = 1;
    } else {
        LOGD("rec_data = %s test fail!\n",rec_data);
        Result = 0;
    }
    exit:
    close(SerialFd);
    return Result;
}

//dynamically register the functions.
static JNINativeMethod getMethods[] = {
        {"UartTest",       "(IIIII)I", (void *) UartTest},
        {"openSerialPort", "(I)I",     (void *) openSerialPort},

};

static int
registerNativeMethods(JNIEnv *env, const char *className, JNINativeMethod *gmethod, int methodNum) {
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
    const char *className = "com/zte/engineer/SerialPort";
    return registerNativeMethods(env, className, getMethods,
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




