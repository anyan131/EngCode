package com.zte.engineer;


public class EmGpio{
	private static final String RESULT_ERROR = "ERROR";
	
	static{
		System.loadLibrary("newmobi_gpio_jni");
	}
	
	public static native int getGpioMaxNumber();

    public static native boolean gpioInit();

    public static native boolean gpioUnInit();

    public static native boolean setGpioInput(int gpioIndex);

    public static native boolean setGpioOutput(int gpioIndex);

    public static native boolean setGpioDataHigh(int gpioIndex);

    public static native boolean setGpioDataLow(int gpioIndex);
    public static native boolean spiGpioSetlow(int gpioIndex);
    public static native boolean spiGpioSethigh(int gpioIndex);
    public static native boolean getGpioData(int gpioIndex);

    public static native int getCurrent(int hostNumber);

    public static native boolean setCurrent(int hostNumber, int currentDataIdx, int currentCmdIdx);
}