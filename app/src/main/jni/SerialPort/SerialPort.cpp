/**
*This is a driver of the SerialPort.
*cause we have defined the Baud rate B115200 so we can use it.
*for the nodes, /dev/ttyMT1 - /dev/ttyMT4.
*
*@author AlexTao NewMobi 2018/01/06.
*
*/

/**
*Here we only design 2 functions: open and close the serial port.
*also here we want to make sure we use the way to dynamically load the functions so we do
*not need to write the complex name.
*/
#include <jni.h>
#include <stdio.h>
#include <string.h>
//#include <cassert.h>
#include <android/log.h>

//this is the third party import.
#include <termios.h>
#include <termio.h>
#include <unistd.h>
#include <fcntl.h>

#define TAG "AlexT" //
  
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) //define LOGD 
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__) // define LOGI
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__) // define LOGW 
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__) // define LOGE  
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__) // define LOGF   

static int fd = -1;
static int fd1 = -1;
static int fd2 = -1;
static int fd3 = -1;

jboolean opend(JNIEnv* env,jclass clazz){
	speed_t speed = B115200;
    //this is for open the only node. /dev/ttyMT1.
    const char *path = "/dev/ttyMT1";
	const char *path1 = "/dev/ttyMT2";
	const char *path2 = "/dev/ttyMT3";
	const char *path3 = "/dev/ttyMT0";
    //try to open the device.
    fd = open(path, O_RDWR);
	
	fd1 = open(path1,O_RDWR);
	fd2 = open(path2,O_RDWR);
	fd3 = open(path3,O_RDWR);
	//open ttyMT1.
    if (fd < 0) {
		printf("fd error!");
		//LOGD("fd error! %d",fd);
        return JNI_FALSE;
    }
	//open ttyMT2.
	if (fd1 < 0) {
		printf("fd1 error!");
		//LOGD("fd1 error! %d",fd);
        return JNI_FALSE;
    }
	//open ttyMT3.
	if (fd2 < 0) {
		printf("fd2 error!");
		//LOGD("fd2 error! %d",fd);
        return JNI_FALSE;
    }
	//open ttyMT4
	if (fd3 < 0) {
		printf("fd3 error!");
		//LOGD("fd3 error! %d",fd);
        return JNI_FALSE;
    }
	
	

    //config the device ttyMT1.
    {
        struct termios cfg;
        if (tcgetattr(fd, &cfg)) {
            close(fd);
			printf("tcgetattr error!");
			//LOGD("tcgetattr failed!");
            return JNI_FALSE;
        }

        cfmakeraw(&cfg);
        cfsetispeed(&cfg, speed);
        cfsetospeed(&cfg, speed);
        if (tcsetattr(fd, TCSANOW, &cfg)) {
            close(fd);
			printf("tcsetattr error !");
			//LOGD("tcsetattr fail!");
            return JNI_FALSE;
        }
       // return JNI_TRUE;
    }
	
	
	//config the device ttyMT2.
    {
        struct termios cfg1;
        if (tcgetattr(fd1, &cfg1)) {
            close(fd1);
			printf("tcgetattr error!");
			//LOGD("tcgetattr failed!");
            return JNI_FALSE;
        }

        cfmakeraw(&cfg1);
        cfsetispeed(&cfg1, speed);
        cfsetospeed(&cfg1, speed);
        if (tcsetattr(fd1, TCSANOW, &cfg1)) {
            close(fd1);
			printf("tcsetattr error !");
			//LOGD("tcsetattr fail!");
            return JNI_FALSE;
        }
        //return JNI_TRUE;
    }
	
	//config the device ttyMT3.
    {
        struct termios cfg2;
        if (tcgetattr(fd2, &cfg2)) {
            close(fd2);
			printf("tcgetattr error!");
			//LOGD("tcgetattr failed!");
            return JNI_FALSE;
        }

        cfmakeraw(&cfg2);
        cfsetispeed(&cfg2, speed);
        cfsetospeed(&cfg2, speed);
        if (tcsetattr(fd2, TCSANOW, &cfg2)) {
            close(fd2);
			printf("tcsetattr error !");
			//LOGD("tcsetattr fail!");
            return JNI_FALSE;
        }
       // return JNI_TRUE;
    }
	
	//config the device ttyMT4.
    {
        struct termios cfg3;
        if (tcgetattr(fd3, &cfg3)) {
            close(fd3);
			printf("tcgetattr error!");
			//LOGD("tcgetattr failed!");
            return JNI_FALSE;
        }

        cfmakeraw(&cfg3);
        cfsetispeed(&cfg3, speed);
        cfsetospeed(&cfg3, speed);
        if (tcsetattr(fd3, TCSANOW, &cfg3)) {
            close(fd3);
			printf("tcsetattr error !");
			//LOGD("tcsetattr fail!");
            return JNI_FALSE;
        }

    }
	        return JNI_TRUE;
	
}

void closed(){
	close(fd);
	close(fd1);
	close(fd2);
	close(fd3);
}

//dynamically register the functions.
static JNINativeMethod getMethods[] = {
        {"opend", "()Z", (void *) opend},
        {"closed",   "()V", (void *)closed}
};

static int registerNativeMethods(JNIEnv* env,const char* className,JNINativeMethod* gmethod,int methodNum){
	jclass clazz;
	//here we need to find the class name.
	clazz = env->FindClass(className);
	if(clazz == NULL){
		return JNI_FALSE;
	}
	if(env-> RegisterNatives(clazz,gmethod,methodNum)<0){
		return JNI_FALSE;
	}
	return JNI_TRUE;

}

static int registerNatives(JNIEnv* env){
	const char* className = "com/zte/engineer/SerialPort";
	return registerNativeMethods(env,className,getMethods,sizeof(getMethods)/sizeof(getMethods[0]));
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


















