package org.core.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author junhu.sun
 *
 */
public interface BaseDao {

     /**
	 * 带可变参数查询,返回执行结果
	 * 
	 * @param sql
	 *            查询sql
	 * @param para
	 *            可变参数
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> querySql(String sql, Object... para) throws SQLException ;

	/**
	 * 带可变参数查询,返回执行结果
	 * 
	 * @param <T>
	 * @param <T>
	 * 
	 * @param sql
	 *            查询sql
	 * @param para
	 *            可变参数
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings({"rawtypes" })
	public <T> List querySql(T clazz, String sql, Object... para) throws SQLException ;

	/**
	 * 带可变参数查询,返回首条执行结果
	 * 
	 * @param <T>
	 * 
	 * @param sql
	 *            查询sql
	 * @param para
	 *            可变参数
	 * @return
	 * @throws SQLException
	 */
	public <T> T queryFirst(T clazz, String sql, Object... para) throws SQLException;
	/**
	 * 带可变参数查询，返回long类型数据
	 * 
	 * @param countSql
	 *            查询记录条数的sql
	 * @param para
	 *            可变参数
	 * @return
	 * @throws SQLException
	 * @throws SQLException
	 */
	public Long queryForLong(String countSql, Object... para) throws SQLException ;

	/**
	 * 带可变参数, 执行sql，返回执行影响的记录条数
	 * 
	 * @param sql
	 *            执行的sql 语句
	 * @param para
	 *            可变参数
	 * @return
	 * @throws SQLException
	 */
	public int executeUpdate(String sql, Object... para) throws SQLException ;

	/**
	 * 带可变参数, 执行sql插入，返回新增记录的自增主键<BR>
	 * 注意： 若插入的表无自增主键则返回 0，异常的话则返回 null
	 * 
	 * @param sql
	 * @param para
	 * @return
	 * @throws SQLException
	 * @see [类、类#方法、类#成员]
	 */
	public Long insertSql(String sql, Object... para) throws SQLException ;

	/**
	 * 批量更新
	 * 
	 * @param sql
	 *            需执行的sql
	 * @param params
	 *            参数组
	 * @throws SQLException
	 * @see [类、类#方法、类#成员]
	 */
	public void executeBatch(String sql, Object[][] params) throws SQLException ;

	/**
	 * 批量更新
	 * 
	 * @param sql
	 *            需执行的sql
	 * @param params
	 *            List参数组
	 * @throws SQLException
	 * @see [类、类#方法、类#成员]
	 */
	public void executeBatch(String sql, List<Object[]> params) throws SQLException ;
	// ************** 事务操作 **************
}