package org.core.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.core.db.DBUtilsHelper;



public class BaseDaoImpl implements BaseDao{

	DBUtilsHelper dbh = new DBUtilsHelper();
	QueryRunner runner = dbh.getRunner();

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
	public List<Map<String, Object>> querySql(String sql, Object... para) throws SQLException {
		return runner.query(sql, new MapListHandler(), para);
	}

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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> List querySql(T clazz, String sql, Object... para) throws SQLException {
		RowProcessor processor = new BasicRowProcessor(new GenerousBeanProcessor());
		return runner.query(sql, new BeanListHandler<Object>((Class) clazz, processor), para);
	}

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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T queryFirst(T clazz, String sql, Object... para) throws SQLException {
		if (!sql.toLowerCase().contains(" limit ")) // 前后有空格
		{
			sql = sql + " limit 1";
		}
		List<T> list = querySql((Class) clazz, sql, para);
		if (list.isEmpty()) {
			return null;
		}
		return (T) list.get(0);
	}

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
	public Long queryForLong(String countSql, Object... para) throws SQLException {
		return runner.query(countSql, new ScalarHandler<Long>(), para);
	}
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
	public int executeUpdate(String sql, Object... para) throws SQLException {
		return runner.update(sql, para);
	}

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
	public Long insertSql(String sql, Object... para) throws SQLException {
		return (Long) runner.insert(sql, new ScalarHandler<Object>(), para);
	}

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
	public void executeBatch(String sql, Object[][] params) throws SQLException {
		runner.batch(sql, params);
	}

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
	public void executeBatch(String sql, List<Object[]> params) throws SQLException {
		Object[][] paramArr = params.toArray(new Object[0][]);
		executeBatch(sql, paramArr);
	}
	// ************** 事务操作 **************
}
