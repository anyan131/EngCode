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

static const char *dev = "/dev/mtgpio";

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
    obj->fd = open(dev, O_RDONLY);

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

    switch (req.op) {
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
        case SET_DIR_OUT:
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
    obj->init = false;
    return true;
}






