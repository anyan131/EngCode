/**
 * ****************************************************File Name ******************************************
 *                                                      funk.h
 * ********************************************************************************************************
 *
 * *************************************************Description********************************************
 *
 * declare the function of init the GPIO and deinit the GPIO. Of course here is a function about operate
 * the GPIO.
 * ********************************************************************************************************
 * Author NewMobi Alextao
 *
 * 2018/01/11
 * */

#include "Meta_GPIO_Para.h"

struct GPIO_REQ;
struct GPIO_CNF;


int Meta_GPIO_Init(void);
GPIO_CNF Meta_GPIO_OP(GPIO_REQ req, unsigned char* peer_buf, unsigned short peer_len); 
int Meta_GPIO_Deinit(void);
