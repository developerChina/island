package main;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.core.dao.BaseDao;
import org.core.dao.BaseDaoImpl;

import utils.DateStyle;
import utils.DateUtil;
import utils.FileLockUtil;

public class TestMain {

	public static void main(String[] args) {
//		createVIPQueue();
//		createGuestQueue();
		deleteQueue();
	}
	/**
	 * 生成卸货岛VIP队列
	 */
	public static void createVIPQueue(){
		BaseDao baseDao = new BaseDaoImpl();
		for (int i = 1; i <5; i++) {
			 String sql="insert into logis_vip (island_no,car_code,queue_number,comein_time,goout_time) value (?,?,?,?,?)";
			 Object[] para={1,"川A738F"+i,i,"2015-12-11 12:12:12","2015-12-11 12:12:12"};
			 try {
				baseDao.insertSql(sql, para);
				System.err.println("创建一条VIP");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 生成卸货岛普通队列
	 */
	public static void createGuestQueue(){
		BaseDao baseDao = new BaseDaoImpl();
		for (int i = 1; i <21; i++) {
			 String sql="insert into logis_ordinary (island_no,car_code,queue_number,comein_time,goout_time) value (?,?,?,?,?)";
			 Object[] para={1,"京B02128",i,"2015-12-11 12:12:12","2015-12-11 12:12:12"};
			 try {
				baseDao.insertSql(sql, para);
				System.err.println("创建一条guest");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void deleteQueue() {
		BaseDao baseDao = new BaseDaoImpl();
		int unIsland=1;
		UnIslandQueue queue=null;
		int source=0;
		for (int i = 0; i < 100; i++) {
			try { 
				//添加历史记录
				String data = FileLockUtil.readByLines(System.getProperty("user.dir") + "\\bin\\res\\Sequence.txt");
				String[] data_=data.split(",");
				System.out.println(data);
				if(!data.equals("") && data_.length==5) {
					String flag=data_[0];
					String car_code=data_[1];
					String comeinTime=data_[2];
					int soure=Integer.parseInt(data_[3]);
					if("1".equals(flag)) {
						 String sql="insert into logis_history (island_no,car_code,comein_time,goout_time,source) value (?,?,?,?,?)";
						 Object[] para={unIsland,car_code,comeinTime,new Timestamp(System.currentTimeMillis()),soure};
						 baseDao.insertSql(sql, para);
						 System.out.println("生成历史:"+car_code+"类型:"+unIsland);
					}
				}
				
				//首先查询VIP队列
				Object obj_vip= baseDao.queryFirst(UnIslandQueue.class, "select * from logis_vip order by queue_number");
				if(obj_vip!=null){
					queue=(UnIslandQueue) obj_vip;
					queue.setComein_time(new Date());
					source=0;
					baseDao.executeUpdate("delete from logis_vip where id = ? ",queue.getId());
					System.out.println("消费VIP:"+queue.getId()+"同时入口抬杆，出口落杆");
					
				}else{
					
					Object obj_guest= baseDao.queryFirst(UnIslandQueue.class, "select * from logis_ordinary order by queue_number");
					if(obj_guest!=null){
						queue=(UnIslandQueue) obj_guest;
						queue.setComein_time(new Date());
						source=1;
						baseDao.executeUpdate("delete from logis_ordinary where id = ? ",queue.getId());
						System.out.println("消费Guest:"+queue.getId()+"入口抬杆，出口落杆");
						//添加入口抬杆，出口落杆
						
					}else{
						queue=null;
						source=0;
						FileLockUtil.saveAs("",System.getProperty("user.dir") + "\\bin\\res\\Sequence.txt");
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if(queue!=null){
				String data = "1,"+queue.getCar_code()+","+DateUtil.DateToString(queue.getComein_time(), DateStyle.YYYY_MM_DD_HH_MM_SS)+","+source+",";
				FileLockUtil.saveAs(data,System.getProperty("user.dir") + "\\bin\\res\\Sequence.txt");
			}
		}
	}
}
