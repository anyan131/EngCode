package com.zte.engineer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;

import android.util.Log;

public class NVRamUtil {
	private static final String TAG = "SalesStatistics";
	private static final String NVFILE = "data/app/.smssecurity";

	public static boolean getServiceState() {
		IBinder binder= ServiceManager.getService("NvRAMAgent");
		NvRAMAgent agent = NvRAMAgent.Stub.asInterface(binder);
		int file_lid=36;//The lid of AP_CFG_REEB_PRODUCT_INFO_LID is 35
		byte[] buff = null;
		try {
			buff = agent.readFile(file_lid);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		StringBuffer sale = new StringBuffer();
		sale.append(buff[104]);
		Log.e("chengrq","sale:"+sale);
		if(sale.toString().contains("1")){
		Log.e("chengrq","false");
			return false;
		}else{
			Log.e("chengrq","TRUE");
			return true;
		}
		
	}

	public static void writeOpenFlag() {
		writeFlag(0);
	}

	public static void writeCloseFlag() {
		writeFlag(1);
	}

	public static void writeFlag(int flag) {
		IBinder binder= ServiceManager.getService("NvRAMAgent");
		NvRAMAgent agent = NvRAMAgent.Stub.asInterface(binder);
		int file_lid=36;//The lid of AP_CFG_REEB_PRODUCT_INFO_LID is 35
		byte[] buff = null;
		try {
			buff = agent.readFile(file_lid);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		StringBuffer sale = new StringBuffer();
		sale.append(buff[104]);
		Log.e("chengrq","sale:"+sale);
		if(sale.equals("1"))
			return;
		
		if(flag == 1)
			buff[104] = 1;
		else
			buff[104] = 0;
		try {
			agent.writeFile(file_lid,buff);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void writeFlag(int flag,int locate) {
		IBinder binder= ServiceManager.getService("NvRAMAgent");
		NvRAMAgent agent = NvRAMAgent.Stub.asInterface(binder);
		int file_lid=36;//The lid of AP_CFG_REEB_PRODUCT_INFO_LID is 35
		byte[] buff = null;
		try {
			buff = agent.readFile(file_lid);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		StringBuffer sale = new StringBuffer();
		sale.append(buff[locate]);
		Log.e("chengrq","sale:"+sale);
		if(sale.equals("1"))
			return;
		
		if(flag == 1)
			buff[locate] = 1;
		else
			buff[locate] = 0;
		try {
			agent.writeFile(file_lid,buff);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
