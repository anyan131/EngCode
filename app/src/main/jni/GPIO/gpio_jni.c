

/**
 * This is override by NewMobi.
 * author is Alextao.2018/01/11.
 *
 * */


#include <memory.h>
#include "gpio_jni.h"
#include "funk.h"
#include "gpio_exp.h"
#include "Meta_GPIO_Para.h"
#include "meta_gpio.h"
#include <stdio.h>


JNIEXPORT jboolean JNICALL Java_com_zte_engineer_EmGpio_gpioInit
        (JNIEnv *env, jobject jobj) {
    return Meta_GPIO_Init() ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_com_zte_engineer_EmGpio_gpioUnInit
        (JNIEnv *env, jobject jobj) {
    return Meta_GPIO_Deinit() ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jint JNICALL Java_com_zte_engineer_EmGpio_getGpioMaxNumber
        (JNIEnv *env, jobject jobj) {
    return (jint) HW_GPIO_MAX;
}

JNIEXPORT jboolean JNICALL Java_com_zte_engineer_EmGpio_setGpioInput
        (JNIEnv *env, jobject jobj, jint n) {
    GPIO_REQ req;
    GPIO_CNF r;
    memset(&req, 0, sizeof(GPIO_REQ));
	req.pin = (int) n;
    req.op = SET_MODE_0;

    r = Meta_GPIO_OP(req, 0, 0);
    if(r.status != META_SUCCESS)
        return  JNI_FALSE;

    req.pin = (int) n;
    req.op = SET_DIR_IN;

    r = Meta_GPIO_OP(req, 0, 0);
    return (r.status == META_SUCCESS) ? JNI_TRUE : JNI_FALSE;
}


JNIEXPORT jboolean JNICALL Java_com_zte_engineer_EmGpio_setGpioOutput
        (JNIEnv *env, jobject jobj, jint n) {
    GPIO_REQ req;
    GPIO_CNF r;
    memset(&req, 0, sizeof(GPIO_REQ));
    req.pin = (int) n;
    req.op = SET_MODE_0;

    r = Meta_GPIO_OP(req, 0, 0);
    if(r.status != META_SUCCESS)
        return  JNI_FALSE;


    req.pin = (int) n;
    req.op = SET_DIR_OUT;

     r = Meta_GPIO_OP(req, 0, 0);
    return (r.status == META_SUCCESS) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_com_zte_engineer_EmGpio_setGpioDataHigh
        (JNIEnv *env, jobject jobj, jint n) {
    GPIO_REQ req;
    memset(&req, 0, sizeof(GPIO_REQ));
    req.pin = (int) n;
    req.op = SET_DATA_HIGH;
    printf("lrz Java_com_zte_engineer_EmGpio_setGpioDataHigh\n");
    GPIO_CNF r = Meta_GPIO_OP(req, 0, 0);
    return (r.status == META_SUCCESS) ? JNI_TRUE : JNI_FALSE;

}
JNIEXPORT jboolean JNICALL Java_com_zte_engineer_EmGpio_setGpioDataLow
        (JNIEnv *env, jobject jobj, jint n){
    GPIO_REQ req;
    memset(&req, 0, sizeof(GPIO_REQ));
    req.pin = (int) n;
    req.op = SET_DATA_LOW;

    GPIO_CNF r = Meta_GPIO_OP(req, 0, 0);
    return (r.status == META_SUCCESS) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_com_zte_engineer_EmGpio_getGpioData
        (JNIEnv *env, jobject jobj, jint n) {
    GPIO_REQ req;
    memset(&req, 0, sizeof(GPIO_REQ));
    req.pin = (int) n;
    req.op = GET_DATA_IN;

    GPIO_CNF r = Meta_GPIO_OP(req, 0, 0);
    return r.data ;
}


JNIEXPORT jboolean JNICALL Java_com_zte_engineer_EmGpio_spiGpioSetlow
        (JNIEnv *env, jobject jobj, jint n)
{
    spi_gpio_set_low();
    return 1;
}

JNIEXPORT jboolean JNICALL Java_com_zte_engineer_EmGpio_spiGpioSethigh
        (JNIEnv *env, jobject jobj, jint n)
{
    spi_gpio_set_high();
    return 1;
}