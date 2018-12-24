/**
此处留空。

*/
#include <fcntl.h>
#include <android/log.h>
#include <unistd.h>

#include <jni.h>
#include <asm/ioctl.h>


int i2c2_fd = -1, i2c3_fd = -1;
#define i2c2_dev_name "/dev/i2c2_demo"
#define i2c3_dev_name "/dev/i2c3_demo"

#define TAG "i2c_test"
#define MM_DEV_MAGIC   	'N'
#define Parameter_AddrSet  		_IO(MM_DEV_MAGIC, 0)
//#define Parameter_TimingSet  	_IO(MM_DEV_MAGIC, 1)
//#define Parameter_PowerSet  	_IO(MM_DEV_MAGIC, 2)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__)

const char *className = "com/newmobi/iic/IICHelper";


jint openIIC(JNIEnv *env,jobject obj)
{
  //  i2c2_fd =  open(i2c2_dev_name, O_RDWR | O_NOCTTY | O_NONBLOCK);
 ///   ioctl(i2c2_fd,Parameter_AddrSet,0x18 );
  //  if(i2c2_fd < 0)
  //      return i2c2_fd;

    i2c3_fd =  open(i2c3_dev_name, O_RDWR | O_NOCTTY | O_NONBLOCK);
    ioctl(i2c3_fd,Parameter_AddrSet,0x18 );
    if(i2c3_fd < 0)
        return i2c3_fd;


    LOGD("openIIC i2c2_fd = %d i2c3_fd = %d \n",i2c2_fd,i2c3_fd);

    return 1;

}


jint closeIIC(JNIEnv *env,jobject obj)
{
    int ret ;

   // ret = close(i2c2_fd);
   // if(ret < 0)
   //     return ret;
    ret = close(i2c3_fd);
    if(ret < 0)
        return ret;

    return ret;
}


jint writeIIC(JNIEnv *env,jobject obj,jint IICNumber , jint reg_buf ,jint len)
{
    int ret = -1;
   // if(IICNumber == 2)
   //     ret = write(i2c2_fd,&reg_buf,len);
    if(IICNumber == 3)
        ret = write(i2c3_fd,&reg_buf,len);
    else
    {
        LOGD("writeIIC IICNumber is error \n");
    }
    LOGD("readIIC writeIIC IICNumber=%d reg_buf=%d len=%d\n",IICNumber,reg_buf,len);
    return ret;
}

jint  readIIC(JNIEnv *env,jobject obj,jint IICNumber ,jint len)
{
    int ret = -1;
    unsigned char read_data ;
   // if(IICNumber == 2)
    //    ret = read(i2c2_fd,&read_data,1);
   if (IICNumber == 3)
        ret = read(i2c3_fd,&read_data,1);
    else
        LOGD("readIIC IICNumber is error \n");

    LOGD("readIIC IICNumber is IICNumber =%d read_data=%d \n",IICNumber,read_data);
    return read_data;

}

//dynamically register the functions.
static JNINativeMethod getMethods[] = {
        {"openIIC",  "()I", (void *) openIIC},
        {"closeIIC",  "()I", (void *) closeIIC},
        {"writeIIC",  "(III)I", (void *) writeIIC},
        {"readIIC",  "(II)I", (void *) readIIC},


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