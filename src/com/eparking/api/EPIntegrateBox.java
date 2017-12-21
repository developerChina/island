package com.eparking.api;

import com.sun.jna.Native;
import com.sun.jna.Library;
//************************************
//以下的4个接口，属于2015年被定义，只支持
//EP-YTX001,EP-YTX002类型的一体箱，进行控制 语音,显示,
//和闸机控制功能。只支持 TCP/IP方式传输。
//保留 EP_OpenDoor  （道闸抬杆功能）
//     EP_Display   （显示屏显示功能）
//     EP_PlayVoice  (语音功能)
//     EP_ModifyIP   (修改IP地址)
//以上4个接口，考虑兼容性，持续对外开放。
//且都可以独立使用，无需和其他的接口配合。
//
//自2016年12月份，研制新的型号（EP-YTX030）
//为了支持新的连接方法，故扩展了一些新的接口，定义
//如下
//     EP_OpenDoorEx （道闸抬杆功能）
//     EP_DisplayEx  （显示屏显示功能）
//     EP_PlayVoiceEx (语音功能)
//     EP_ModifyIPEx  (修改IP地址)
//
//     2017年3月增加获取一体箱信息的接口
//
//     EP_GetBoxInfo
//************************************
public interface EPIntegrateBox extends Library {
public static String strFilePath = System.getProperty("user.dir") + "\\bin\\res\\EPIntegrateBox.dll";
String aa=strFilePath.replaceAll("%20", " ");
	public static EPIntegrateBox INSTANCE = (EPIntegrateBox)Native.loadLibrary(strFilePath, EPIntegrateBox.class);
//************************************
// Create Time [22/12/2016 ]
// Method:    EP_OpenDoorEx
//            开闸
// Returns:   0-失败 1-成功
// Parameter: nBoxType     一体箱型号（类型ID - 对应型号）：
//					    1 - YTX-001型号 ;
//						2 - YTX-002型号 ;
// Parameter: sIpAddress 一体箱控制器IP
//                       
// Parameter: nTransType 传输类型 (当传输类型选择 2时，参数 sIpAddress 为 485的COM的地址。填写方式例如：COM1，COM2等,当传输类型选择 3时,sIpAddress 为相机的IP地址)
//                       1 -  RJ45方式 ;通过网络接线传输。
//                       2 -  RS485方式  ;通过485接线传输。
//                       3 -  RS485相机透传方式;通过相机传输485数据。


//************************************
	public int EP_OpenDoorEx(int nBoxType, String sIpAddress, int nTransType);
//************************************
// Create Time [22/12/2016]
// Method:    EP_DisplayEx
//            设置显示屏显示
// Returns:   0-失败 1-成功
// Parameter: nBoxType     一体箱型号（类型ID - 对应型号）：
//						    1 - YTX-001型号;
//							2 - YTX-002型号;
//							3 - EP-YTX030/EP-YTX031型号;
//							4 - EP-G200/EP-G210型号;
// 
//           sIpAddress 控制器IP地址,IP地址的配置和第一个参数，
//						 一体箱的类型有关系；
//           			 当一体箱类型为 1 - YTX-001 型号 ，2 - YTX-002型号
//						 的时候,IP地址为一体箱控制器的IP；当一体箱型号是 3 - EP-YTX030/EP-YTX031型号、
//						 4 - EP-G200/EP-G210型号的时候，是对应相机的IP地址。
//            nIndex  	1-第一行显示
//                   	2-第二行显示
//                   	3-第三行显示（暂时不支持）
//                   	4-第四行显示（暂时不支持）
//            strValue  显示字符串
//            nColor    显示屏颜色(0-默认 1-红色 2-绿色 3-黄色), EP-G200/EP-G210型号支持多色，其它型号只支持红色
//
//            nTransType 传输类型 
//                       1 -  RJ45方式 ;通过网络接线传输。
//                       2 -  RS485方式  ;通过485接线传输。
//                       3 -  RS485相机透传方式;通过相机传输485数据。
//备注：屏幕显示时，按照多于4个字滚动显示，少于4字居中显示		  
//************************************
	public int EP_DisplayEx(int nBoxType,String sIpAddress,int nIndex,byte[] bs, int nColor,int nTransType);
	
//************************************
// Create Time [22/12/2016]
// Method:    EP_PlayVoice
//            设置语音播放
// Returns:   0-失败 1-成功
// Parameter: nBoxType     一体箱型号（类型ID - 对应型号）：
//					    1 - YTX-001型号;
//						2 - YTX-002型号;
//						3 - EP-YTX030/EP-YTX031型号;
//						4 - EP-G200/EP-G210型号;
//  
//            sIpAddress 控制器IP地址或者相机的IP地址
//            nIndex     0-自定义播报(EP-YTX030/EP-YTX031型号、EP-G200/EP-G210型号支持自定义，001、002不支持自定义，001,002支持1，2,3,4,12,15,16固定语音播报)
//                      1-播报收费金额 配合后面的sData(如 收费5.00元)参数一起使用
//                      2-播报车牌号   配合后边的sData(如 京A12345)参数一起使用
//                      3-播报欢迎光临
//                      4-播报一路顺风
//                      14-播报月租车剩余天数  配合后边的sData(如 sData = 2 则播报月租车剩余2天)参数一起使用
//                      15-播报请稍后
//                      16-播报已过期
//                      
//			  sData     播报收费金额和车牌号时的参数，或者自定义语音播报
//
//            nTransType 传输类型 
//                       1 -  RJ45方式 ;通过网络接线传输。
//                       2 -  RS485方式  ;通过485接线传输。
//                       3 -  RS485相机透传方式;通过相机传输485数据。
//************************************
	public int  EP_PlayVoiceEx(int nBoxType,String sIpAddress,int index,byte[] bs,int nTransType);
	
//************************************
// Create Time [22/12/2016]
// Method:    EP_ModifyIPEx
//            修改IP地址 （只支持001,002）
// Returns:   0-失败 1-成功
// Parameter: nBoxType     一体箱型号（类型ID - 对应型号）：
//					    1 - YTX-001型号;
//						2 - YTX-002型号;
//						 
// 
//            sOldIpAddress 旧的IP地址
//            sNewIpAddress 要设置的新IP地址（加上子网掩码和网关）
//
//备注：sNewIpAddress的格式为IP+子网掩码+网关，如（192.168.0.199 255.255.255.0 192.168.0.1）
//此接口只支持 YTX-001,YTX-002 类型。且修改IP地址采用的是 UDP协议修改。故需要网线连接。
//在修改时请保持一体箱IP地址和电脑IP地址在同一网段。否则不能成功。
//************************************
	public int EP_ModifyIPEx(int nBoxType,String sOldIpAddress,String sNewIpAddress);
//int EP_ModifyIPEx(int nBoxType,const char* sOldIpAddress,const char* sNewIpAddress);	
//************************************
// Create Time [13/3/2017]
// Method:    EP_GetBoxInfo
//            获取LED一体箱信息（包括版本号，IP地址，串口号等）
// Returns:   0-失败 1-成功
// Parameter: buff		传出参数，建议分配256字节空间，返回一体箱信息 格式定义 {"version":"","ip":"","com":""}
//			  len		传出参数字节数
//			  nBoxType     一体箱型号（类型ID - 对应型号）：
//					    1 - EP-YTX001型号;
//						2 - EP-YTX002型号;
//						3 - EP-YTX030/EP-YTX031型号;
//						4 - EP-G200/EP-G210型号;
//            sIpAddress 如果是RJ45通信，填写一体箱IP地址
//            nTransType 传输类型 
//                       1 -  RJ45方式 ;通过网络接线传输。
//                       2 -  RS485方式;通过485接线传输。
//                       3 -  RS485相机透传方式;通过相机传输485数据。
//************************************
//int EP_GetBoxInfo(char* pBuffer, int len, int nBoxType, const char* sIpAddress, int nTransType = 1);	
	public int EP_GetBoxInfo(byte[] byteA,int len,int nBoxType,String sIpAddress,int nTransType);
}