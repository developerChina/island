package org.core.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.core.utils.PropertyUtils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

public class DbPoolConnection {

	private static DbPoolConnection databasePool = null;
	private static DruidDataSource dds = null;
	private static ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();
	static {
		Properties properties = loadPropertyFile("db_server.properties");
		try {
			dds = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private DbPoolConnection() {
	}

	public static synchronized DbPoolConnection getInstance() {
		if (null == databasePool) {
			databasePool = new DbPoolConnection();
		}
		return databasePool;
	}

	public DruidDataSource getDataSource() throws SQLException {
		return dds;
	}

	/**
	 * 将connection放入本地thread，保障事务的正常执行
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
	     //从当前线程中获取Connection
		Connection conn = threadLocal.get();
		if(conn==null){
		 //从数据源中获取数据库
		 conn = dds.getConnection();
		 //将conn绑定到当前线程
		 threadLocal.set(conn);
		 }
		 return conn;
	}
	
	/**
	 * 开启事务
	 * @throws RuntimeException
	 */
	 public static void startTransaction() throws RuntimeException{
         try{
        	 Connection conn =  threadLocal.get();
        	 if(conn==null){
             conn = getConnection();
              //把 conn绑定到当前线程上
             threadLocal.set(conn);
         }
         //开启事务
         conn.setAutoCommit(false);
	     }catch (Exception e) {
	         throw new RuntimeException(e);
	     }
	 }
	 
	/**
	 * 回滚事务
	 * @throws RuntimeException
	 */
    public static void rollback() throws RuntimeException {
        try{
            //从当前线程中获取Connection
            Connection conn = threadLocal.get();
            if(conn!=null){
                //回滚事务
                conn.rollback();
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	
    /**
     * 提交事务
     * @throws RuntimeException
     */
    public static void commit() throws RuntimeException {
        try{
            //从当前线程中获取Connection
            Connection conn = threadLocal.get();
            if(conn!=null){
                //提交事务
                conn.commit();
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	    
    /**
     * 关闭连接
     * @throws RuntimeException
     */
    public static void close() throws RuntimeException{
        try{
            //从当前线程中获取Connection
            Connection conn = threadLocal.get();
            if(conn!=null){
                conn.close();
                 //解除当前线程上绑定conn
                threadLocal.remove();
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

	public static Properties loadPropertyFile(String fullFile) {
		Properties properties = new Properties();
		try {
			InputStream inStream = PropertyUtils.class.getClassLoader().getResourceAsStream(fullFile);
			properties.load(inStream);
			inStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
}
