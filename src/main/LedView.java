package main;

public class LedView {

	public static void main(String[] args) {
		view_send("192.168.1.100","999999");
	}

	// 节目发送例程，里面包含如何添加文字、日期时间、图片
	public static void view_send(String demo_host,String content) {
		int k;
		int dev = LedControl.INSTANCE.LED_UDP_SenderParamEx(0, 9999, demo_host, 6666, 0, 2, 0, 0, 100, 5);
		k = LedControl.INSTANCE.MakeRoot(LedControl.ROOT_PLAY, LedControl.COLOR_MODE_DOUBLE, -1); // -1表示节目始终播放
		LedControl.INSTANCE.AddChapter((short) k, 30000, (short) LedControl.MODE_WAIT_CHILD);
		/**
		 * // 添加分区 // num 节目数据缓冲区编号，是MakeRoot、MakeChapter的返回值 // left、top、width、height
		 * 左、上、宽度、高度 // border 流水边框 int AddRegion(short num, int left, int top, int
		 * width, int height,int border);
		 */
		LedControl.INSTANCE.AddRegion((short) k, 0, 0, 128, 32, 0);

		LedControl.INSTANCE.AddLeaf((short) k, 3000, (short) LedControl.MODE_WAIT_CHILD);

		/**
		 * // 添加文字播放 // num //
		 * 节目数据缓冲区编号，是MakeRoot、MakeChapter、MakeRegion、MakeLeaf、MakeObject的返回值 //
		 * left、top、width、height 左、上、宽度、高度 // transparent 是否透明 =1表示透明；=0表示不透明 // border
		 * 流水边框(未实现) // str 文字字符串 // fontname 字体名称 // fontsize 字体大小 // fontcolor 字体颜色 //
		 * fontstyle 字体样式 // 举例：=WFS_BOLD表示粗体；=WFS_ITALIC表示斜体；=WFS_BOLD+WFS_ITALIC表示粗斜体
		 * // wordwrap 是否自动换行 =1自动换行；=0不自动换行 // inmethod 引入方式(下面有列表说明) // inspeed
		 * 引入速度(取值范围0-5，从快到慢) // outmethod 引出方式(下面有列表说明) // outspeed 引出速度(取值范围0-5，从快到慢)
		 * // stopmethod 停留方式(下面有列表说明) // stopspeed 停留速度(取值范围0-5，从快到慢) // stoptime
		 * 停留时间(单位毫秒) int AddText(short num, int left, int top, int width, int height,
		 * int transparent, int border, String str, String fontname, int fontsize, int
		 * fontcolor, int fontstyle, int wordwrap, int alignment, int inmethod, int
		 * inspeed, int outmethod, int outspeed, int stopmethod, int stopspeed, int
		 * stoptime); // stoptime单位为毫秒
		 */
		LedControl.INSTANCE.AddText((short) k, 0, 0, 128, 32, 
				                    1, 0, content, "宋体", 8, 0xFFFF, 
 			                        0x0, 1, 0, 1, 5, 2, 5,	0, 5, 3000); // stoptime单位为毫秒
		int r = LedControl.INSTANCE.LED_SendToScreen2(dev, k);
		parse(r);
	}

	// 命令执行结果解析
	public static void parse(int r) {
		if (r >= 0) {
			int notify = LedControl.INSTANCE.LED_GetNotifyParam_Notify(r);
			if (notify == LedControl.LM_TIMEOUT) {
				System.out.println("发送节目或者执行命令超时");
			} else if (notify == LedControl.LM_TX_COMPLETE) {
				int result = LedControl.INSTANCE.LED_GetNotifyParam_Result(r);
				if (result == LedControl.RESULT_FLASH) {
					System.out.println("发送节目或者执行命令完成，正在写入Flash");
				} else {
					System.out.println("发送节目或者执行命令完成");
				}
			} else if (notify == LedControl.LM_RESPOND) {
				int command = LedControl.INSTANCE.LED_GetNotifyParam_Command(r);
				if (command == LedControl.PKC_GET_POWER) {
					int status = LedControl.INSTANCE.LED_GetNotifyParam_Status(r);
					if (status == 1) {
						System.out.println("读取电源完成，当前电源状态为开启");
					} else {
						System.out.println("读取电源完成，当前电源状态为关闭");
					}
				} else if (command == LedControl.PKC_SET_POWER) {
					int status = LedControl.INSTANCE.LED_GetNotifyParam_Status(r);
					if (status == 1) {
						System.out.println("设置电源完成，当前电源状态为开启");
					} else {
						System.out.println("设置电源完成，当前电源状态为关闭");
					}
				} else if (command == LedControl.PKC_GET_BRIGHT) {
					int status = LedControl.INSTANCE.LED_GetNotifyParam_Status(r);
					System.out.println("读取亮度完成，当前亮度为" + status);
				} else if (command == LedControl.PKC_SET_BRIGHT) {
					int status = LedControl.INSTANCE.LED_GetNotifyParam_Status(r);
					System.out.println("设置亮度完成，当前亮度为" + status);
				} else if (command == LedControl.PKC_ADJUST_TIME) {
					System.out.println("校正时间完成");
				} else if (command == LedControl.PKC_GET_CHAPTER_COUNT) {
					int status = LedControl.INSTANCE.LED_GetNotifyParam_Status(r);
					System.out.println("读取节目数量完成，节目数量=" + status);
				} else if (command == LedControl.PKC_GET_CURRENT_CHAPTER) {
					int status = LedControl.INSTANCE.LED_GetNotifyParam_Status(r);
					System.out.println("读取当前播放节目完成，当前播放节目编号=" + status);
				} else if (command == LedControl.PKC_SET_CURRENT_CHAPTER) {
					System.out.println("设置当前播放节目完成");
				} else if (command == LedControl.PKC_SET_EXSTRING) {
					System.out.println("设置外部变量字符串完成");
				}
			} else if (notify == LedControl.LM_NOTIFY) {
				int result = LedControl.INSTANCE.LED_GetNotifyParam_Result(r);
				if (result == LedControl.NOTIFY_ROOT_DOWNLOAD) {
					System.out.println("下载节目写入Flash完成");
				} else if (result == LedControl.NOTIFY_SET_PARAM) {
					System.out.println("设置控制卡参数完成");
				} else if (result == LedControl.NOTIFY_GET_PLAY_BUFFER) {
					LedControl.INSTANCE.LED_GetNotifyParam_Buffer("preview.dat", r);
					LedControl.INSTANCE.LED_PreviewFile_Export("SimuLED.exe", 320, 192, 1, "C:\\preview.bmp");
					LedControl.INSTANCE.LED_PreviewFileEx("SimuLED.exe", 320, 192, "preview.dat");
					System.out.println("读取控制卡内播放内容完成");
				}
			}
		} else if (r == LedControl.R_DEVICE_INVALID) {
			System.out.println("打开通讯端口失败");
		} else if (r == LedControl.R_DEVICE_BUSY) {
			System.out.println("该通讯 端口忙，正在发送节目或者执行命令");
		}
	}
}
