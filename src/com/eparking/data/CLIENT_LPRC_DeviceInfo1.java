package com.eparking.data;

import com.sun.jna.Structure;

public class CLIENT_LPRC_DeviceInfo1 extends Structure 
{	
	public static class ByReference extends CLIENT_LPRC_DeviceInfo1 implements Structure.ByReference {}  
	public static class ByValue extends CLIENT_LPRC_DeviceInfo1 implements Structure.ByValue {}  			
    public byte[] chDevName=new byte[256];	/* 设备名		*/
	public byte[] chSoftVer=new byte[20];	/* 软件版本号	*/
	public byte[] chHardVer=new byte[20];	/* 硬件版本号	*/
	public byte[] chSysVer=new byte[20];	/* 系统版本	*/
	public int 	  nSdkPort;					/* sdk端口号	*/
	//public byte[] chIp = new byte[16];		/* ip地址		*/
	public String chIp;
	public String chGateway;	/* 网关	 	*/
	public String chNetmask;	/* 子网掩码	*/
	public String chMac;		/* 子网掩码	*/
	public byte[] chRoomID = new byte[20];	/* RooMID	*/
	public byte[] chSN = new byte[20];		/* SN		*/
	public byte[] reserved = new byte[256];	/* 保留		*/
}



