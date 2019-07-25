//
// Created by AlexTao on 2018/1/10.
//

#include "meta_gpio.h"

#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <sys/ioctl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

#include "mtgpio.h"
#include "MetaPub.h"

struct meta_gpio_object {
    GPIO_CNF cnf;
    int fd;
    bool init;
};

static struct meta_gpio_object gpio_object = {.init = false};

static const char *dev = "/sys/bus/platform/drivers/mediatek-mt6765-pinctrl/1000b000.pinctrl/mt_gpio";
#define GPIO_NAME_PATH "/sys/bus/platform/drivers/mediatek-mt6765-pinctrl/1000b000.pinctrl/mt_gpio"
int gpio_fb_63 = -1;

static const int op_map[] = {
        GPIO_IOCQMODE,
        GPIO_IOCTMODE0,
        GPIO_IOCTMODE1,
        GPIO_IOCTMODE2,
        GPIO_IOCTMODE3,

        GPIO_IOCQDIR,
        GPIO_IOCSDIRIN,
        GPIO_IOCSDIROUT,

        GPIO_IOCQPULLEN,
        GPIO_IOCSPULLENABLE,
        GPIO_IOCSPULLDISABLE,

        GPIO_IOCQPULL,
        GPIO_IOCSPULLDOWN,
        GPIO_IOCSPULLUP,

        GPIO_IOCQINV,
        GPIO_IOCSINVENABLE,
        GPIO_IOCSINVDISABLE,

        GPIO_IOCQDATAIN,
        GPIO_IOCQDATAOUT,
        GPIO_IOCSDATALOW,
        GPIO_IOCSDATAHIGH


};

static bool __DEBUG__ = true;

#define MGP_LOG(fmt, args...)                           \
    do {                                                \
        if (__DEBUG__)                                  \
            META_LOG("GPIO: %s: "fmt, __func__,##args); \
    } while (0)

void Meta_GPIO_Debug(bool enable) {
    __DEBUG__ = enable;
    printf("debug...%s\n", (enable) ? "enable" : "disable");
}


// this is for the gpio initialization.
bool Meta_GPIO_Init() {
    struct meta_gpio_object *obj = &gpio_object;
    if (obj->init == true) {
        //MGP_LOG("initialized!\n");
        return true;
    }
    obj->fd = open(GPIO_NAME_PATH, O_RDWR | O_NOCTTY | O_NONBLOCK);
    printf("lrz obj->fd = %d\n",obj->fd);
   // gpio_fb_63 = open(GPIO_NAME_PATH, O_RDWR | O_NOCTTY | O_NONBLOCK);

    if (obj->fd == -1) {

        return false;
    } else {
        obj->init = true;
        return true;
    }
}

GPIO_CNF Meta_GPIO_OP(GPIO_REQ req, unsigned char *peer_buf, unsigned short peer_len) {
    struct meta_gpio_object *obj = &gpio_object;

    int res, op = -1;

    obj->cnf.header.id = req.header.id + 1;
    obj->cnf.header.token = req.header.token;
    obj->cnf.status = 0;
    printf("lrz GPIO\n");

    switch (req.op) {

	case SET_MODE_0:
		res = set_GPIO_mode(obj->fd,req.pin, 0);
		obj->cnf.data = (res < 0) ? (0) : res;
        obj->cnf.status = (res < 0) ? META_FAILED : META_SUCCESS;
        break;

	case SET_DIR_IN:
		res = set_GPIO_dir(obj->fd,req.pin, 0);
		obj->cnf.data = (res < 0) ? (0) : res;
        obj->cnf.status = (res < 0) ? META_FAILED : META_SUCCESS;
        break;

	case SET_DIR_OUT:
		res = set_GPIO_dir(obj->fd,req.pin, 1);
		obj->cnf.data = (res < 0) ? (0) : res;
        obj->cnf.status = (res < 0) ? META_FAILED : META_SUCCESS;
        break;	

	case SET_DATA_HIGH:
	res = set_GPIO_out(obj->fd,req.pin, 1);
	obj->cnf.data = (res < 0) ? (0) : res;
    obj->cnf.status = (res < 0) ? META_FAILED : META_SUCCESS;
    break;	

	case SET_DATA_LOW:
	res = set_GPIO_out(obj->fd,req.pin, 0);
	obj->cnf.data = (res < 0) ? (0) : res;
    obj->cnf.status = (res < 0) ? META_FAILED : META_SUCCESS;
    break;
		
/*
		case GET_MODE_STA:
        case GET_DIR_STA:
        case GET_PULLEN_STA:
        case GET_PULL_STA:
        case GET_INV_STA:
        case GET_DATA_IN:
        case GET_DATA_OUT:
            op = op_map[req.op];
            res = ioctl(obj->fd, op, req.pin);
            obj->cnf.data = (res < 0) ? (0) : res;
            obj->cnf.status = (res < 0) ? META_FAILED : META_SUCCESS;
            break;
        case SET_MODE_0:
        case SET_MODE_1:
        case SET_MODE_2:
        case SET_MODE_3:
        case SET_DIR_IN:
            op = op_map[req.op];
            ioctl(obj->fd, GPIO_IOCTMODE0, (req.pin | 0x80000000));
            res = ioctl(obj->fd, op, req.pin);
            obj->cnf.data = (res < 0) ? (0) : res;
            obj->cnf.status = (res < 0) ? META_FAILED : META_SUCCESS;
            break;
        case SET_DIR_OUT:
            op = op_map[req.op];
            ioctl(obj->fd, GPIO_IOCTMODE0, (req.pin | 0x80000000));
            res = ioctl(obj->fd, op, req.pin);
            obj->cnf.data = (res < 0) ? (0) : res;
            obj->cnf.status = (res < 0) ? META_FAILED : META_SUCCESS;
            break;
        case SET_PULLEN_DISABLE:
        case SET_PULLEN_ENABLE:
        case SET_PULL_DOWN:
        case SET_PULL_UP:
        case SET_INV_ENABLE:
        case SET_INV_DISABLE:
        case SET_DATA_LOW:
        case SET_DATA_HIGH:
            op = op_map[req.op];
            res = ioctl(obj->fd, op, req.pin);
            obj->cnf.data = 0;
            obj->cnf.status = (res < 0) ? META_FAILED : META_SUCCESS;
            break;
   */         
        default:
            res = -EFAULT;
            obj->cnf.status = META_FAILED;
            obj->cnf.data = 0;
            break;
    }

    return obj->cnf;
}

int Meta_GPIO_Deinit(void) {
    int res;
    struct meta_gpio_object *obj = &gpio_object;
    if (!obj->init) {
        return true;
    }
    res = close(obj->fd);
    //close(gpio_fb_63);
    obj->init = false;
    return true;
}


/*
	setting GPIO mode
	gpio : gpio num
	modem: gpio mode
*/

int set_GPIO_mode(int fd,int gpio,int mode){
    int ret;
    char buf[16]={0};
    sprintf(buf,"mode %d %d\n",gpio,mode);
    ret = write(fd,buf,16);
    if(ret < 0){
        printf("set_GPIO_mode error\n");
    }
	return ret;
}

/*
	setting GPIO dir
	gpio : gpio num
	dir : gpio dir 0:in or 1:out
*/

int set_GPIO_dir(int fd,int gpio,int dir){
    int ret;
    char buf[16]={0};
    sprintf(buf,"dir %d %d\n",gpio,dir);
    ret = write(fd,buf,16);
    if(ret < 0){
        printf("set_GPIO_dir error\n");
    }
	return ret;
}


/*
	setting GPIO output
	gpio : gpio num
	status : 1:High or 0:Low
*/

int set_GPIO_out(int fd,int gpio,int status){
    int ret;
    char buf[16]={0};
    sprintf(buf,"out %d %d\n",gpio,status);
    ret = write(fd,buf,16);
    if(ret < 0){
        printf("set_GPIO_out error\n");
    }
	return ret;
}

/*
	setting Gpio pull en
	gpio : gpio num
	pull_en: pull  1:enable or 0:disenable
*/

void set_GPIO_pullen(int fd,int gpio,int pull_en){
    int ret;
    char buf[16]={0};
    sprintf(buf,"pullen %d %d\n",gpio,pull_en);
    ret = write(fd,buf,16);
    if(ret < 0){
        printf("set_GPIO_pullen error\n");
    }
}

/*
	setting Gpio pull sel
	gpio : gpio num
	pull_sel: pull 1:high or 0:low
*/

void set_GPIO_pullsel(int fd,int gpio,int pull_sel){
    int ret;
    char buf[16]={0};
    sprintf(buf,"pullsel %d %d\n",gpio,pull_sel);
    ret = write(fd,buf,16);
    if(ret < 0){
        printf("set_GPIO_pullsel error\n");
    }
}


void spi_gpio_set_low(void)
{
/*
    set_GPIO_mode(gpio_fb_63,85,0);
    set_GPIO_mode(gpio_fb_63,86,0);
    set_GPIO_mode(gpio_fb_63,87,0);
    set_GPIO_mode(gpio_fb_63,88,0);

    set_GPIO_dir(gpio_fb_63,85,1);
    set_GPIO_dir(gpio_fb_63,86,1);
    set_GPIO_dir(gpio_fb_63,87,1);
    set_GPIO_dir(gpio_fb_63,88,1);

    set_GPIO_out(gpio_fb_63,85,0);
    set_GPIO_out(gpio_fb_63,86,0);
    set_GPIO_out(gpio_fb_63,87,0);
    set_GPIO_out(gpio_fb_63,88,0);*/

}

void spi_gpio_set_high(void)
{
/*
    set_GPIO_mode(gpio_fb_63,85,0);
    set_GPIO_mode(gpio_fb_63,86,0);
    set_GPIO_mode(gpio_fb_63,87,0);
    set_GPIO_mode(gpio_fb_63,88,0);

    set_GPIO_dir(gpio_fb_63,85,1);
    set_GPIO_dir(gpio_fb_63,86,1);
    set_GPIO_dir(gpio_fb_63,87,1);
    set_GPIO_dir(gpio_fb_63,88,1);

    set_GPIO_out(gpio_fb_63,85,1);
    set_GPIO_out(gpio_fb_63,86,1);
    set_GPIO_out(gpio_fb_63,87,1);
    set_GPIO_out(gpio_fb_63,88,1);
*/
}