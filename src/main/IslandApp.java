package main;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;

import org.core.dao.BaseDao;
import org.core.dao.BaseDaoImpl;
import org.core.utils.GenId;

import com.eparking.api.EPIntegrateBox;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import utils.FileLockUtil;
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

	// 6.定义 自镇长的序列
//	private static AtomicInteger count = new AtomicInteger();
	// 7.定义配置属性
	Properties properties = LoadPropertyUtil.loadPropertyFile("system.properties");

	// 8.定义当前处理队列
	public static UnIslandQueue queue=null;
	// 9.定义当前队列类型
	public static int source=0;;// 0=vip  , 1=普通表
	public static String uuid=null;// 历史记录表id
		
	String com=(String) properties.get("com");
	String eparkIp=(String) properties.get("eparkIp");
	int unIsland=Integer.valueOf((String) properties.get("unIsland"));
	String bigLed=(String) properties.get("bigLed");
	String smallLed=(String) properties.get("smallLed");
	
	public IslandApp() {
		try {
			// 获取串口、打开窗串口、获取串口的输入流。
			serialPort = SerialTool.openPort(com, 115200);
			inputStream = serialPort.getInputStream();
			// 向串口添加事件监听对象。
			serialPort.addEventListener(this);
			// 设置当端口有可用数据时触发事件，此设置必不可少。
			serialPort.notifyOnDataAvailable(true);
			//初始化车辆
			VehicleIdent.Dev_Bash_Init(eparkIp, unIsland);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			call(unIsland);
		}
	}

	/**
	 * 业务处理类 流程： 数据库里 查询 “VIP+普通号”-》删除数据库-》把记录压入 车辆识别的队列里
	 * 
	 * @param unIsland
	 */
	public void call(int unIsland) {
		/**
		 * 1: 记录压入 车辆识别的队列里
		 */
		try { 
			//添加历史记录
			String data = FileLockUtil.readByLines(System.getProperty("user.dir") + "\\bin\\res\\Sequence.txt");
			String[] data_=data.split(",");
			if(!data.equals("") && data_.length==5) {
				String flag=data_[0];
				String car_code=data_[1];
				String id=data_[2];
				if("1".equals(flag)&&uuid!=null) {
					 String sql="update logis_history set goout_time = ? where id =?";
					 Object[] para={new Timestamp(System.currentTimeMillis()),id};
					 baseDao.insertSql(sql, para);
					 System.out.println("修改历史:"+id+"车牌号："+car_code);
				}
			}
			
			//首先查询VIP队列
			Object obj_vip= baseDao.queryFirst(UnIslandQueue.class, "select * from logis_vip order by queue_number");
			if(obj_vip!=null){
				queue=(UnIslandQueue) obj_vip;
				queue.setComein_time(new Date());
				source=0;
				baseDao.executeUpdate("delete from logis_vip where id = ? ",queue.getId());
				System.out.println("消费VIP:"+queue.getId()+"---");
				 //添加历史记录
				 uuid=GenId.UUID();
				 String sql="insert into logis_history (id,island_no,car_code,comein_time,source) value (?,?,?,?,?)";
				 Object[] para={uuid,unIsland,queue.getCar_code(),new Timestamp(System.currentTimeMillis()),source};
				 baseDao.insertSql(sql, para);
				 System.out.println("生成历史:"+uuid+"类型:"+source);
			}else{
				Object obj_guest= baseDao.queryFirst(UnIslandQueue.class, "select * from logis_ordinary order by queue_number");
				if(obj_guest!=null){
					queue=(UnIslandQueue) obj_guest;
					queue.setComein_time(new Date());
					source=1;
					baseDao.executeUpdate("delete from logis_ordinary where id = ? ",queue.getId());
					System.out.println("消费Guest:"+queue.getId()+"---");
					//添加历史记录
					 uuid=GenId.UUID();
					 String sql="insert into logis_history (id,island_no,car_code,comein_time,source) value (?,?,?,?,?)";
					 Object[] para={uuid,unIsland,queue.getCar_code(),new Timestamp(System.currentTimeMillis()),source};
					 baseDao.insertSql(sql, para);
					 System.out.println("生成历史:"+uuid+"类型:"+source);
				}else{
					queue=null;
					source=0;
					FileLockUtil.saveAs("",System.getProperty("user.dir") + "\\bin\\res\\Sequence.txt");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		//判断没有队列
		if(queue!=null){
			//data=状态，车牌号，历史记录id
			String data = "0,"+queue.getCar_code()+","+uuid+",";
			FileLockUtil.saveAs(data,System.getProperty("user.dir") + "\\bin\\res\\Sequence.txt");
			/**
			 * 2:语音播报 
			 */
			try {
				EPIntegrateBox.INSTANCE.EP_PlayVoiceEx(3,eparkIp, 16, (queue.getCar_code() + "\0").getBytes("GBK"), 3);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			/**
			 * 3:“小显示屏” 显示
			 */
			LedView.view_send(smallLed, data);
			/**
			 * 4:“大显示屏” 显示
			 */
		}else{
			String data = "没有车辆";
			/**
			 * 2:语音播报 
			 */
			try {
				EPIntegrateBox.INSTANCE.EP_PlayVoiceEx(3,eparkIp, 16, (data + "\0").getBytes("GBK"), 3);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
	}
}