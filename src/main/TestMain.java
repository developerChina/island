package main;

import java.sql.SQLException;
import java.sql.Timestamp;
import org.core.dao.BaseDao;
import org.core.dao.BaseDaoImpl;

public class TestMain {

	public static void main(String[] args) {
		createVIPQueue();
		createGuestQueue();
//		deleteQueue();
	}
	/**
	 * 生成卸货岛VIP队列
	 */
	public static void createVIPQueue(){
		BaseDao baseDao = new BaseDaoImpl();
		for (int i = 1; i <2; i++) {
			 String sql="insert into logis_vip (island_no,car_code,queue_number,comein_time,goout_time) value (?,?,?,?,?)";
			 Object[] para={1,"川A738F6",i,"2015-12-11 12:12:12","2015-12-11 12:12:12"};
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
		for (int i = 1; i <2; i++) {
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
		int islandid=1;
		UnIslandQueue queue=null;
		int source=0;
		for (int i = 0; i < 100; i++) {
			try { 
				//添加历史记录
				if(queue!=null){
					 String sql="insert into logis_history (island_no,car_code,comein_time,goout_time,source) value (?,?,?,?,?)";
					 Object[] para={islandid,queue.getCar_code(),queue.getComein_time(),new Timestamp(System.currentTimeMillis()),source};
					 baseDao.insertSql(sql, para);
					 System.out.println("生成历史:"+queue.getId()+queue.getCar_code()+"类型:"+islandid);
				}
				//首先查询VIP队列
				Object obj_vip= baseDao.queryFirst(UnIslandQueue.class, "select * from logis_vip order by queue_number");
				if(obj_vip!=null){
					queue=(UnIslandQueue) obj_vip;
					source=0;
					baseDao.executeUpdate("delete from logis_vip where id = ? ",queue.getId());
					System.out.println("消费VIP:"+queue.getId()+"同时入口抬杆，出口落杆");
					//添加入口抬杆，出口落杆
					
				}else{
					Object obj_guest= baseDao.queryFirst(UnIslandQueue.class, "select * from logis_ordinary order by queue_number");
					if(obj_guest!=null){
						queue=(UnIslandQueue) obj_guest;
						source=1;
						baseDao.executeUpdate("delete from logis_ordinary where id = ? ",queue.getId());
						System.out.println("消费Guest:"+queue.getId()+"入口抬杆，出口落杆");
						//添加入口抬杆，出口落杆
						
					}else{
						System.out.println("===========空执行");
						queue=null;
						source=0;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
