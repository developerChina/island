package main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.core.dao.BaseDao;
import org.core.dao.BaseDaoImpl;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import utils.LoadPropertyUtil;
import utils.SerialTool;

/**
 * 卸货岛处理 流程
 * 
 * @author sjh
 *
 */
public class IslandApp implements SerialPortEventListener {
	// 1.定义变量
	SerialPort serialPort = null;
	// 2.定义变量 输入流
	InputStream inputStream = null;
	// 3.定义開始時間
	Date beginTime = new Date();
	// 4.定义 数据源
	public static BaseDao baseDao = new BaseDaoImpl();
	// 5.定义 队列
	private BlockingQueue<String> queue;
	// 6.定义 自镇长的序列
	private static AtomicInteger count = new AtomicInteger();
	// 7.定义配置属性
	Properties properties = LoadPropertyUtil.loadPropertyFile("system.properties");
	
	public IslandApp(BlockingQueue<String> queue) {
		try {
			// 获取串口、打开窗串口、获取串口的输入流。
			serialPort = SerialTool.openPort((String) properties.get("com"), 115200);
			inputStream = serialPort.getInputStream();
			// 向串口添加事件监听对象。
			serialPort.addEventListener(this);
			// 设置当端口有可用数据时触发事件，此设置必不可少。
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    this.queue = queue;
	}

	/**
	 * 重写继承的监听器方法
	 */
	@Override
	public void serialEvent(SerialPortEvent event) {
		// 定义用于缓存读入数据的数组
		byte[] cache = new byte[1024];
		// 记录已经到达串口COM21且未被读取的数据的字节（Byte）数。
		int availableBytes = 0;
		// 如果是数据可用的时间发送，则进行数据的读写
		if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				availableBytes = inputStream.available();
				while (availableBytes > 0) {
					inputStream.read(cache);
					availableBytes = inputStream.available();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Date endTime = new Date();
		long interval = (endTime.getTime() - beginTime.getTime()) / 1000;
		// System.out.println("两个时间相差" + interval + "秒");// 会打印出相差3秒
		beginTime = endTime;
		if (interval >= 3) {// 系统默认等待 时间 单位：秒
			call((String) properties.get("unIsland"));
		}
	}
    /**
     * 业务处理类
     * 流程： 数据库里 查询  “VIP+普通号”-》删除数据库-》把记录压入   车辆识别的队列里
     * @param unIsland
     */
	public void call(String unIsland) {
		/**
		 * 1:  记录压入   车辆识别的队列里
		 */
		String data = "data:" + count.incrementAndGet();
		try {
			if (!queue.offer(data, 2, TimeUnit.SECONDS)) {
				System.out.println("放入数据失败："+data);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		/**
		 * 2:语音播报   ， “小显示屏” 显示   ，  “大显示屏”显示
		 */
		
	}
}