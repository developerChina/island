package main;

import com.eparking.api.WTY_EX;
import com.eparking.callback.IOnConnectStatus;
import com.eparking.callback.IOnGetDataEx2;
import com.eparking.data.PlateResult_Ex;

import utils.FileLockUtil;

public class VehicleIdent {

	public static void main(String[] args) {
		Dev_Bash_Init("192.168.1.98",1);
		while (true);
	}
	/**
	 * 初始化 车辆识别仪
	 */
	public static void Dev_Bash_Init(String ip, int unIsland) {
		IOnConnectStatus connStatus = new OnConnectStatus(); // 连接状态的回调函数
		try {
			// 注册连接状态的回调函数（必选）
			WTY_EX.INSTANCE.CLIENT_LPRC_RegCLIENTConnEvent(connStatus);
			// 注册识别结果的回调函数（必选）
			WTY_EX.INSTANCE.CLIENT_LPRC_RegDataEx2Event(new OnGetDataEx2());
			WTY_EX.INSTANCE.CLIENT_LPRC_InitSDK(8080, null, 0, ip, unIsland);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void Dev_Bash_Init_1(String ip, int unIsland) {
		IOnConnectStatus connStatus = new OnConnectStatus(); // 连接状态的回调函数
		try {
			// 注册连接状态的回调函数（必选）
			WTY_EX.INSTANCE.CLIENT_LPRC_RegCLIENTConnEvent(connStatus);
			// 注册识别结果的回调函数（必选）
			WTY_EX.INSTANCE.CLIENT_LPRC_RegDataEx2Event(new OnGetDataEx3());
			WTY_EX.INSTANCE.CLIENT_LPRC_InitSDK(8080, null, 0, ip, unIsland);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 连接状态的回调函数
	public static class OnConnectStatus implements IOnConnectStatus {
		public void callback(String chWTYIP, int nStatus, long dwUser) {
			// 设备的连接状态
			if (nStatus != 1) {
				System.out.println(chWTYIP + "连接断开");
			}
		}
	}
	// 识别结果的回调函数
	public static class OnGetDataEx2 implements IOnGetDataEx2 {
		public void callback(PlateResult_Ex.ByReference plateResult, long dwUser) {
		
		}
	}
	// 识别结果的回调函数
	public static class OnGetDataEx3 implements IOnGetDataEx2 {
		public void callback(PlateResult_Ex.ByReference plateResult, long dwUser) {
			if (plateResult.pPlateImage.nLen != 0) {
				try {
					String cacrip = new String(plateResult.chWTYIP, "GBK").trim();
					String cacrno=new String(plateResult.chLicense, "GBK").trim();
					String shootTime=plateResult.shootTime.Year + "-" + plateResult.shootTime.Month + "-"
							+ plateResult.shootTime.Day + " " + plateResult.shootTime.Hour + ":"
							+ plateResult.shootTime.Minute + ":" + plateResult.shootTime.Second;
					String data = FileLockUtil.readByLines(System.getProperty("user.dir") + "\\bin\\res\\Sequence.txt");
					System.out.println(data+ "=======" + cacrip+"====="+cacrno+"====="+shootTime);
					/**
					 * 比对  抬杆 进入
					 * 
					 */
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
