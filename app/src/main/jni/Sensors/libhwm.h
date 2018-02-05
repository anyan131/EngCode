#ifndef __LIBHWM_H__
#define __LIBHWM_H__
/*---------------------------------------------------------------------------*/
#include <math.h>
#include <fcntl.h>

#ifdef __cplusplus
 extern "C" {
#endif

#define	GSENSOR_NAME "/dev/gsensor"
#define GSENSOR_ATTR_SELFTEST "/sys/bus/platform/drivers/gsensor/selftest"

/*---------------------------------------------------------------------------*/
// hardware\libhardware\include\hardware\sensors.h
#define LIBHWM_GRAVITY_EARTH            (9.80665f)  
/*---------------------------------------------------------------------------*/
#define LIBHWM_ACC_NVRAM_SENSITIVITY    (65536)  /*16bits : 1g*/
/*---------------------------------------------------------------------------*/
#define LSB_TO_GRA(X)                   ((X*LIBHWM_GRAVITY_EARTH)/LIBHWM_ACC_NVRAM_SENSITIVITY)
#define GRA_TO_LSB(X)                   (round((X*LIBHWM_ACC_NVRAM_SENSITIVITY)/LIBHWM_GRAVITY_EARTH))
/*---------------------------------------------------------------------------*/
#define LIBHWM_INVAL_FD                 (-1)
/*---------------------------------------------------------------------------*/
#define LIBHWM_IS_INVAL_FD(fd)          (fd == LIBHWM_INVAL_FD)
// Gyroscope sensor sensitivity 1000
#define LIBHWM_GYRO_NVRAM_SENSITIVITY	1000
/*---------------------------------------------------------------------------*/ 
#define ABSDIF(X,Y) ((X > Y) ? (Y - X) : (X - Y))
#define ABS(X)      ((X > 0) ? (X) : (-X))


/*---------------------------------------------------------------------------*/
//传感器类型
typedef enum {
    HWM_TYPE_NONE = 0,
    HWM_TYPE_ACC = 1,
    HWM_TYPE_MAG = 2,
    HWM_TYPE_PRO = 3,
    HWM_TYPE_LIG = 4,    
} HwmType;
/*---------------------------------------------------------------------------*/
//传感器设备节点数据结构
typedef struct {
    HwmType  type;
    char        *ctl;
    char        *dat;
    int         ctl_fd;
    int         dat_fd;    
} HwmDev;
/*---------------------------------------------------------------------------*/
typedef union{
    struct {    /*raw data*/
        int rx;
        int ry;
        int rz;
    };
    struct {    
        float x;
        float y;
        float z;
    };
    struct {
        float azimuth;
        float pitch;
        float roll;
    };    
} HwmData;
/*---------------------------------------------------------------------------*/
typedef struct {
    void *ptr;
    int   len;
} HwmPrivate;
/*---------------------------------------------------------------------------*/
typedef struct {
    int64_t time;
    HwmData dat;
} AccItem;

extern int gsensor_selftest(int enable);
extern int gsensor_enable_selftest(int enable);
 int gsensor_open(int *fd){
    return open(GSENSOR_NAME,O_RDWR);
};
 int gsensor_close(int fd){
     return close(fd);
 };
extern int gsensor_read(int fd, HwmData *dat);
 int gsensor_get_cali(int fd, HwmData *dat){
     return 0;
 };
 int gsensor_set_cali(int fd, HwmData *dat){
     return 0;
 };
 int gsensor_rst_cali(int fd){
     return 0;
 };
extern int gsensor_read_nvram(HwmData *dat);
 int gsensor_write_nvram(HwmData *dat){
     return 0;
 };
extern int libhwm_wait_delay(int ms);
extern long libhwm_current_ms(void);
 int gsensor_calibration(int fd, int period, int count, int tolerance, int trace, HwmData *cali){
     return 0;
 };

#ifdef __cplusplus
 }
#endif
#endif

