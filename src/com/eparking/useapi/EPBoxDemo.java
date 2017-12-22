package com.eparking.useapi;

import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import com.eparking.api.EPIntegrateBox;

public class EPBoxDemo {

	@SuppressWarnings({"resource" })
	public static void main(String[] args) throws UnsupportedEncodingException {
		Scanner sc = new Scanner(System.in);
		
		while(true)
		{
			int a =sc.nextInt();	
			System.out.println(a);
			switch(a){
			case 1 : System.out.println("̧抬杆");

			EPIntegrateBox.INSTANCE.EP_OpenDoorEx(3, new String("192.168.1.98"), 3);
			break;			
			case 2 : System.out.println("显示");	

			String gbk = "我来了是肯定vo为说GV都忘\0";	
			EPIntegrateBox.INSTANCE.EP_DisplayEx(3, new String("192.168.1.98"), 1,"rrrrrrrr\0".getBytes("GBK"), 1, 3);
			EPIntegrateBox.INSTANCE.EP_DisplayEx(3, new String("192.168.1.98"), 2,"rrrrddd".getBytes("GBK"), 1, 3);
			break;		
			case 3 : System.out.println("语音播报");	
			String gbk1="tian天气不错哦\0";
			EPIntegrateBox.INSTANCE.EP_PlayVoiceEx(3, new String("192.168.0.223"), 16,gbk1.getBytes("GBK"), 3);
			break;		
			case 4 : System.out.println("修改IP ");
		
			EPIntegrateBox.INSTANCE.EP_ModifyIPEx(3, new String("192.168.0.207"), new String("192.168.0.208"));
			break;
			
			case 5: System.out.println("获得版本");
			byte[] byteA = new byte[256];
			
			EPIntegrateBox.INSTANCE.EP_GetBoxInfo(byteA, 128, 3, new String("192.168.0.251"), 1);
			//EPIntegrateBox.INSTANCE.EP_GetBoxInfo(aa, 128, 3, new String("192.168.0.251"), 1);
			String aa=new String(byteA,"utf-8");
			System.out.println(aa);
			break;
			}
		}
	}

}
