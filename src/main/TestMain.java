package main;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.core.dao.BaseDao;
import org.core.dao.BaseDaoImpl;
import org.core.utils.GenId;

import utils.FileLockUtil;

public class TestMain {

	public static void main(String[] args) {
		createVIPQueue();
		createGuestQueue();
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
		String uuid=null;
		for (int i = 0; i < 100; i++) {
			/**
			 * 1: 记录压入 车辆识别的队列里
			 */
			try { 
				//添加历史记录
				String data = FileLockUtil.readByLines(System.getProperty("user.dir") + "\\bin\\res\\Sequence.txt");
				String[] data_=data.split(",");
				if(!data.equals("") && data_.length==4) {
					String flag=data_[0];
					String car_code=data_[1];
					String id=data_[2];
					if("1".equals(flag)) {
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
						System.out.println("------------------"+i);
						queue=null;
						source=0;
						FileLockUtil.saveAs("",System.getProperty("user.dir") + "\\bin\\res\\Sequence.txt");
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if(queue!=null){
				//data=状态，车牌号，历史记录id
				String data = "1,"+queue.getCar_code()+","+uuid+",";
				FileLockUtil.saveAs(data,System.getProperty("user.dir") + "\\bin\\res\\Sequence.txt");
			}
		}
	}
}
