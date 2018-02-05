#define LOG_TAG "com_zte_engineer_LcdOffTest"
#include "jni.h"

//#include "mtkfb.h"
#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <sys/ioctl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include "utils/Log.h"

#include "utils/String8.h"
#include "utils/Errors.h"
#include "utils/SharedBuffer.h"

using namespace android;

#ifdef __cplusplus
extern "C" {

/* --------------------------------------------------------------------------- */
/* IOCTL commands. */
#define MTK_IOW(num, dtype)     _IOW('O', num, dtype)
#define MTK_IOR(num, dtype)     _IOR('O', num, dtype)
#define MTK_IOWR(num, dtype)    _IOWR('O', num, dtype)
#define MTK_IO(num)             _IO('O', num)
#define MTKFB_QUEUE_OVERLAY_CONFIG			MTK_IOW(137, struct fb_overlay_config)
/* -------------------------------------------------------------------------- */
#define MTKFB_SET_OVERLAY_LAYER                MTK_IOW(0, struct fb_overlay_layer)
#define MTKFB_TRIG_OVERLAY_OUT                 MTK_IO(1)
#define MTKFB_SET_VIDEO_LAYERS                 MTK_IOW(2, struct fb_overlay_layer)
#define MTKFB_CAPTURE_FRAMEBUFFER              MTK_IOW(3, unsigned long)
#define MTKFB_CONFIG_IMMEDIATE_UPDATE          MTK_IOW(4, unsigned long)
#define MTKFB_SET_MULTIPLE_LAYERS              MTK_IOW(5, struct fb_overlay_layer)
#define MTKFB_REGISTER_OVERLAYBUFFER           MTK_IOW(6, struct fb_overlay_buffer_info)
#define MTKFB_UNREGISTER_OVERLAYBUFFER         MTK_IOW(7, unsigned int)
#define MTKFB_SET_ORIENTATION                  MTK_IOW(8, unsigned long)
#define MTKFB_FBLAYER_ENABLE                   MTK_IOW(9, unsigned int)
#define MTKFB_LOCK_FRONT_BUFFER                MTK_IO(10)
#define MTKFB_UNLOCK_FRONT_BUFFER              MTK_IO(11)
#define MTKFB_POWERON				           MTK_IO(12)
#define MTKFB_POWEROFF				           MTK_IO(13)

/* Fence/Ion, OVL decoupling */
#define MTKFB_PREPARE_OVERLAY_BUFFER           MTK_IOW(14, struct fb_overlay_buffer)

/* S3D control */
#define MTKFB_SET_COMPOSING3D                  MTK_IOW(15, unsigned long)
#define MTKFB_SET_S3D_FTM		               MTK_IOW(16, unsigned long)

/* FM De-sense for EM and Normal mode */
#define MTKFB_GET_DEFAULT_UPDATESPEED          MTK_IOR(17, unsigned long)
#define MTKFB_GET_CURR_UPDATESPEED             MTK_IOR(18, unsigned long)
#define MTKFB_CHANGE_UPDATESPEED               MTK_IOW(19, unsigned long)	/* for EM, not called change writecycle because DPI change pll ckl */
#define MTKFB_GET_INTERFACE_TYPE               MTK_IOR(20, unsigned long)	/* /0 DBI, 1 DPI, 2 MIPI */
#define MTKFB_GET_POWERSTATE		           MTK_IOR(21, unsigned long)	/* /0: power off  1: power on */
#define MTKFB_GET_DISPLAY_IF_INFORMATION       MTK_IOR(22, mtk_dispif_info_t)
#define MTKFB_AEE_LAYER_EXIST                  MTK_IOR(23, unsigned long)	//called before SET_OVERLAY each time, if true, hwc will not use FB_LAYER again
#define MTKFB_GET_OVERLAY_LAYER_INFO           MTK_IOR(24, struct fb_overlay_layer_info)
#define MTKFB_FACTORY_AUTO_TEST                MTK_IOR(25, unsigned long)
#define MTKFB_GET_FRAMEBUFFER_MVA              MTK_IOR(26, unsigned int)
#define MTKFB_SLT_AUTO_CAPTURE                 MTK_IOWR(27, struct fb_slt_catpure)

//error handling
#define MTKFB_META_RESTORE_SCREEN              MTK_IOW(101, unsigned long)
#define MTKFB_ERROR_INDEX_UPDATE_TIMEOUT       MTK_IO(103)
#define MTKFB_ERROR_INDEX_UPDATE_TIMEOUT_AEE   MTK_IO(104)

/*restore bootlogo and charater in meta mode*/
#define MTKFB_META_SHOW_BOOTLOGO               MTK_IO(105)


#endif

JNIEXPORT jint JNICALL Java_com_zte_engineer_LcdPowerControl_LcdPowerOn
  (JNIEnv * env, jobject c) {			
	int fd = open("dev/graphics/fb0", O_RDWR, 0);
	if (fd < 0) {
		//LOGE("OPEN Failed");
		return -1;
	} else {
		int ret = -1;
		if (ioctl(fd, MTKFB_POWERON, &ret) == -1) {
			//LOGE("ioctl Failed");
			close(fd);
			return -1;
		}
		close(fd);
		return 0;
	}

}

JNIEXPORT jint JNICALL Java_com_zte_engineer_LcdPowerControl_LcdPowerOff
  (JNIEnv * env, jobject c) {
	int fd = open("dev/graphics/fb0", O_RDWR, 0);
	if (fd < 0) {
		//LOGE("OPEN Failed");
		return -1;
	} else {
		int ret = -1;
		if (ioctl(fd, MTKFB_POWEROFF, &ret) == -1) {
			//LOGE("ioctl Failed");
			close(fd);
			return -1;
		}
		close(fd);
		return 0;
	}

}

#ifdef __cplusplus
}
#endif
