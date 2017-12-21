package utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;

public class RedisUtil {

    private static RedisUtil ru = new RedisUtil();
    private static Jedis jedis;
    private String ip = "192.168.1.128";
    private int port = 6379;
	
    public static void main(String[] args) {
    	//RedisUtil.getRu().set("sunjunhu","dsdasf");
    	
    	System.out.println("-----"+RedisUtil.getRu().get("sunjunhu")); 
    	//RedisUtil.getRu().expire("sunjunhu", 1);
	}
    
    private RedisUtil() {
    	if (jedis == null) {
    		jedis = new Jedis(ip, port);
        }
	}
    /**
     * 设置过期时间
     *
     * @param key
     * @param seconds ，默认值为60000seconds
     * @return 返回影响的记录数
     */
    public long expire(String key, int seconds) {
       return jedis.expire(key, seconds);
    }

     /**
     * 设置key的过期时间,它是距历元（即格林威治标准时间 1970 年 1 月 1 日的 00:00:00，格里高利历）的偏移量。
     *
     * @param key
     * @param timestamp 秒
     * @return 影响的记录数
     */
    public long expireAt(String key, long timestamp) {
        return jedis.expireAt(key, timestamp);
    }

    /**
     * 查询key的过期时间
     *
     * @param key
     * @return 以秒为单位的时间表示
     */
    public long ttl(String key) {
          return jedis.ttl(key);
    }

    /**
     * 取消对key过期时间的设置
     *
     * @param key
     * @return 影响的记录数
     */
    public long persist(String key) {
          return jedis.persist(key);
    }

    /**
     * 清空所有key
     *
     * @return
     */
    public String flushAll() {
         return jedis.flushAll();
    }

    /**
     * 判断key是否存在
     *
     * @param key
     * @return boolean
     */
    public boolean exists(String key) {
          return jedis.exists(key);
    }
	/**
     * <p>通过key获取储存在redis中的value</p>
     * <p>并释放连接</p>
     * @param key
     * @return 成功返回value 失败返回null
     */
    public String get(String key){
        return jedis.get(key);
    }

    /**
     * <p>向redis存入key和value,并释放连接资源</p>
     * <p>如果key已经存在 则覆盖</p>
     * @param key
     * @param value
     * @return 成功 返回OK 失败返回 0
     */
    public String set(String key,String value){
    	return jedis.set(key, value);
    }


    /**
     * <p>删除指定的key,也可以传入一个包含key的数组</p>
     * @param keys 一个key  也可以使 string 数组
     * @return 返回删除成功的个数
     */
    public Long del(String...keys){
    	return jedis.del(keys);
    }

    /**
     * <p>通过key向指定的value值追加值</p>
     * @param key
     * @param str
     * @return 成功返回 添加后value的长度 失败 返回 添加的 value 的长度  异常返回0L
     */
    public Long append(String key ,String str){
        return jedis.append(key, str);
    }

    /**
     * <p>设置key value,如果key已经存在则返回0,nx==> not exist</p>
     * @param key
     * @param value
     * @return 成功返回1 如果存在 和 发生异常 返回 0
     */
    public Long setnx(String key ,String value){
    	return jedis.setnx(key, value);
    }

    /**
     * <p>设置key value并制定这个键值的有效期</p>
     * @param key
     * @param value
     * @param seconds 单位:秒
     * @return 成功返回OK 失败和异常返回null
     */
    public String setex(String key,String value,int seconds){
        return jedis.setex(key, seconds, value);
    }


    /**
     * <p>通过key 和offset 从指定的位置开始将原先value替换</p>
     * <p>下标从0开始,offset表示从offset下标开始替换</p>
     * <p>如果替换的字符串长度过小则会这样</p>
     * <p>example:</p>
     * <p>value : bigsea@zto.cn</p>
     * <p>str : abc </p>
     * <P>从下标7开始替换  则结果为</p>
     * <p>RES : bigsea.abc.cn</p>
     * @param key
     * @param str
     * @param offset 下标位置
     * @return 返回替换后  value 的长度
     */
    public Long setrange(String key,String str,int offset){
    	return jedis.setrange(key, offset, str);
    }



    /**
     * <p>通过批量的key获取批量的value</p>
     * @param keys string数组 也可以是一个key
     * @return 成功返回value的集合, 失败返回null的集合 ,异常返回空
     */
    public List<String> mget(String...keys){
         return jedis.mget(keys);
    }

    /**
     * <p>批量的设置key:value,可以一个</p>
     * <p>example:</p>
     * <p>  obj.mset(new String[]{"key2","value1","key2","value2"})</p>
     * @param keysvalues
     * @return 成功返回OK 失败 异常 返回 null
     *
     */
    public String mset(String...keysvalues){
        return jedis.mset(keysvalues);
    }

    /**
     * <p>批量的设置key:value,可以一个,如果key已经存在则会失败,操作会回滚</p>
     * <p>example:</p>
     * <p>  obj.msetnx(new String[]{"key2","value1","key2","value2"})</p>
     * @param keysvalues
     * @return 成功返回1 失败返回0
     */
    public Long msetnx(String...keysvalues){
         return jedis.msetnx(keysvalues);
    }

    /**
     * <p>设置key的值,并返回一个旧值</p>
     * @param key
     * @param value
     * @return 旧值 如果key不存在 则返回null
     */
    public String getset(String key,String value){
        return jedis.getSet(key, value);
    }

    /**
     * <p>通过下标 和key 获取指定下标位置的 value</p>
     * @param key
     * @param startOffset 开始位置 从0 开始 负数表示从右边开始截取
     * @param endOffset
     * @return 如果没有返回null
     */
    public String getrange(String key, int startOffset ,int endOffset){
        return jedis.getrange(key, startOffset, endOffset);
    }

    /**
     * <p>通过key 对value进行加值+1操作,当value不是int类型时会返回错误,当key不存在是则value为1</p>
     * @param key
     * @return 加值后的结果
     */
    public Long incr(String key){
        return jedis.incr(key);
    }

    /**
     * <p>通过key给指定的value加值,如果key不存在,则这是value为该值</p>
     * @param key
     * @param integer
     * @return
     */
    public Long incrBy(String key,Long integer){
        return jedis.incrBy(key, integer);
    }

    /**
     * <p>对key的值做减减操作,如果key不存在,则设置key为-1</p>
     * @param key
     * @return
     */
    public Long decr(String key) {
         return jedis.decr(key);
    }

    /**
     * <p>减去指定的值</p>
     * @param key
     * @param integer
     * @return
     */
    public Long decrBy(String key,Long integer){
        return jedis.decrBy(key, integer);
    }

    /**
     * <p>通过key获取value值的长度</p>
     * @param key
     * @return 失败返回null
     */
    public Long serlen(String key){
        return jedis.strlen(key);
    }

    /**
     * <p>通过key给field设置指定的值,如果key不存在,则先创建</p>
     * @param key
     * @param field 字段
     * @param value
     * @return 如果存在返回0 异常返回null
     */
    public Long hset(String key,String field,String value) {
        return jedis.hset(key, field, value);
    }

    /**
     * <p>通过key给field设置指定的值,如果key不存在则先创建,如果field已经存在,返回0</p>
     * @param key
     * @param field
     * @param value
     * @return
     */
    public Long hsetnx(String key,String field,String value){
        return jedis.hsetnx(key, field, value);
    }

    /**
     * <p>通过key同时设置 hash的多个field</p>
     * @param key
     * @param hash
     * @return 返回OK 异常返回null
     */
    public String hmset(String key,Map<String, String> hash){
        return jedis.hmset(key, hash);
    }

    /**
     * <p>通过key 和 field 获取指定的 value</p>
     * @param key
     * @param field
     * @return 没有返回null
     */
    public String hget(String key, String field){
        return jedis.hget(key, field);
    }

    /**
     * <p>通过key 和 fields 获取指定的value 如果没有对应的value则返回null</p>
     * @param key
     * @param fields 可以使 一个String 也可以是 String数组
     * @return
     */
    public List<String> hmget(String key,String...fields){
        return jedis.hmget(key, fields);
    }

    /**
     * <p>通过key给指定的field的value加上给定的值</p>
     * @param key
     * @param field
     * @param value
     * @return
     */
    public Long hincrby(String key ,String field ,Long value){
        return jedis.hincrBy(key, field, value);
    }

    /**
     * <p>通过key和field判断是否有指定的value存在</p>
     * @param key
     * @param field
     * @return
     */
    public Boolean hexists(String key , String field){
        return jedis.hexists(key, field);
    }

    /**
     * <p>通过key返回field的数量</p>
     * @param key
     * @return
     */
    public Long hlen(String key){
        return jedis.hlen(key);

    }

    /**
     * <p>通过key 删除指定的 field </p>
     * @param key
     * @param fields 可以是 一个 field 也可以是 一个数组
     * @return
     */
    public Long hdel(String key ,String...fields){
        return jedis.hdel(key, fields);
    }

    /**
     * <p>通过key返回所有的field</p>
     * @param key
     * @return
     */
    public Set<String> hkeys(String key){
        return jedis.hkeys(key);
    }

    /**
     * <p>通过key返回所有和key有关的value</p>
     * @param key
     * @return
     */
    public List<String> hvals(String key){
        return jedis.hvals(key);
    }

    /**
     * <p>通过key获取所有的field和value</p>
     * @param key
     * @return
     */
    public Map<String, String> hgetall(String key){
        return jedis.hgetAll(key);
    }

    /**
     * <p>通过key向list头部添加字符串</p>
     * @param key
     * @param strs 可以使一个string 也可以使string数组
     * @return 返回list的value个数
     */
    public Long lpush(String key ,String...strs){
        return jedis.lpush(key, strs);
    }

    /**
     * <p>通过key向list尾部添加字符串</p>
     * @param key
     * @param strs 可以使一个string 也可以使string数组
     * @return 返回list的value个数
     */
    public Long rpush(String key ,String...strs){
         return jedis.rpush(key, strs);
    }

    /**
     * <p>通过key在list指定的位置之前或者之后 添加字符串元素</p>
     * @param key
     * @param where LIST_POSITION枚举类型
     * @param pivot list里面的value
     * @param value 添加的value
     * @return
     */
    public Long linsert(String key, LIST_POSITION where,
                        String pivot, String value){
         return jedis.linsert(key, where, pivot, value);
    }

    /**
     * <p>通过key设置list指定下标位置的value</p>
     * <p>如果下标超过list里面value的个数则报错</p>
     * @param key
     * @param index 从0开始
     * @param value
     * @return 成功返回OK
     */
    public String lset(String key ,Long index, String value){
         return jedis.lset(key, index, value);
    }

    /**
     * <p>通过key从对应的list中删除指定的count个 和 value相同的元素</p>
     * @param key
     * @param count 当count为0时删除全部
     * @param value
     * @return 返回被删除的个数
     */
    public Long lrem(String key,long count,String value){
        return jedis.lrem(key, count, value);
    }

    /**
     * <p>通过key保留list中从strat下标开始到end下标结束的value值</p>
     * @param key
     * @param start
     * @param end
     * @return 成功返回OK
     */
    public String ltrim(String key ,long start ,long end){

        return jedis.ltrim(key, start, end);
    }

    /**
     * <p>通过key从list的头部删除一个value,并返回该value</p>
     * @param key
     * @return
     */
    synchronized public String lpop(String key){

        return jedis.lpop(key);
    }

    /**
     * <p>通过key从list尾部删除一个value,并返回该元素</p>
     * @param key
     * @return
     */
    synchronized public String rpop(String key){

        return jedis.rpop(key);
    }

    /**
     * <p>通过key从一个list的尾部删除一个value并添加到另一个list的头部,并返回该value</p>
     * <p>如果第一个list为空或者不存在则返回null</p>
     * @param srckey
     * @param dstkey
     * @return
     */
    public String rpoplpush(String srckey, String dstkey){
 
        return jedis.rpoplpush(srckey, dstkey);
    }

    /**
     * <p>通过key获取list中指定下标位置的value</p>
     * @param key
     * @param index
     * @return 如果没有返回null
     */
    public String lindex(String key,long index){
   
        return jedis.lindex(key, index);
    }

    /**
     * <p>通过key返回list的长度</p>
     * @param key
     * @return
     */
    public Long llen(String key){

        return jedis.llen(key);
    }

    /**
     * <p>通过key获取list指定下标位置的value</p>
     * <p>如果start 为 0 end 为 -1 则返回全部的list中的value</p>
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<String> lrange(String key,long start,long end){
  
        return jedis.lrange(key, start, end);
    }

    /**
     * <p>通过key向指定的set中添加value</p>
     * @param key
     * @param members 可以是一个String 也可以是一个String数组
     * @return 添加成功的个数
     */
    public Long sadd(String key,String...members){

        return jedis.sadd(key, members);
    }

    /**
     * <p>通过key删除set中对应的value值</p>
     * @param key
     * @param members 可以是一个String 也可以是一个String数组
     * @return 删除的个数
     */
    public Long srem(String key,String...members){
  
        return jedis.srem(key, members);
    }

    /**
     * <p>通过key随机删除一个set中的value并返回该值</p>
     * @param key
     * @return
     */
    public String spop(String key){

        return jedis.spop(key);
    }

    /**
     * <p>通过key获取set中的差集</p>
     * <p>以第一个set为标准</p>
     * @param keys 可以使一个string 则返回set中所有的value 也可以是string数组
     * @return
     */
    public Set<String> sdiff(String...keys){
        return jedis.sdiff(keys);
    }

    /**
     * <p>通过key获取set中的差集并存入到另一个key中</p>
     * <p>以第一个set为标准</p>
     * @param dstkey 差集存入的key
     * @param keys 可以使一个string 则返回set中所有的value 也可以是string数组
     * @return
     */
    public Long sdiffstore(String dstkey,String... keys){
        return jedis.sdiffstore(dstkey, keys);
    }

    /**
     * <p>通过key获取指定set中的交集</p>
     * @param keys 可以使一个string 也可以是一个string数组
     * @return
     */
    public Set<String> sinter(String...keys){
        return jedis.sinter(keys);
    }

    /**
     * <p>通过key获取指定set中的交集 并将结果存入新的set中</p>
     * @param dstkey
     * @param keys 可以使一个string 也可以是一个string数组
     * @return
     */
    public Long sinterstore(String dstkey,String...keys){
        return jedis.sinterstore(dstkey, keys);
    }

    /**
     * <p>通过key返回所有set的并集</p>
     * @param keys 可以使一个string 也可以是一个string数组
     * @return
     */
    public Set<String> sunion(String... keys){
        return jedis.sunion(keys);
    }

    /**
     * <p>通过key返回所有set的并集,并存入到新的set中</p>
     * @param dstkey
     * @param keys 可以使一个string 也可以是一个string数组
     * @return
     */
    public Long sunionstore(String dstkey,String...keys){
        return jedis.sunionstore(dstkey, keys);
    }

    /**
     * <p>通过key将set中的value移除并添加到第二个set中</p>
     * @param srckey 需要移除的
     * @param dstkey 添加的
     * @param member set中的value
     * @return
     */
    public Long smove(String srckey, String dstkey, String member){
        return jedis.smove(srckey, dstkey, member);
    }

    /**
     * <p>通过key获取set中value的个数</p>
     * @param key
     * @return
     */
    public Long scard(String key){
        return jedis.scard(key);
    }

    /**
     * <p>通过key判断value是否是set中的元素</p>
     * @param key
     * @param member
     * @return
     */
    public Boolean sismember(String key,String member){
        return jedis.sismember(key, member);
    }

    /**
     * <p>通过key获取set中随机的value,不删除元素</p>
     * @param key
     * @return
     */
    public String srandmember(String key){
         return  jedis.srandmember(key);
    }

    /**
     * <p>通过key获取set中所有的value</p>
     * @param key
     * @return
     */
    public Set<String> smembers(String key){
        return jedis.smembers(key);
    }


    /**
     * <p>通过key向zset中添加value,score,其中score就是用来排序的</p>
     * <p>如果该value已经存在则根据score更新元素</p>
     * @param key
     * @param score
     * @param member
     * @return
     */
    public Long zadd(String key,double score,String member){
         return jedis.zadd(key, score, member);
    }

    /**
     * <p>通过key删除在zset中指定的value</p>
     * @param key
     * @param members 可以使一个string 也可以是一个string数组
     * @return
     */
    public Long zrem(String key,String...members){
        return jedis.zrem(key, members);
    }

    /**
     * <p>通过key增加该zset中value的score的值</p>
     * @param key
     * @param score
     * @param member
     * @return
     */
    public Double zincrby(String key ,double score ,String member){
        return jedis.zincrby(key, score, member);
    }

    /**
     * <p>通过key返回zset中value的排名</p>
     * <p>下标从小到大排序</p>
     * @param key
     * @param member
     * @return
     */
    public Long zrank(String key,String member){
        return jedis.zrank(key, member);
    }

    /**
     * <p>通过key返回zset中value的排名</p>
     * <p>下标从大到小排序</p>
     * @param key
     * @param member
     * @return
     */
    public Long zrevrank(String key,String member){
        return jedis.zrevrank(key, member);
    }

    /**
     * <p>通过key将获取score从start到end中zset的value</p>
     * <p>socre从大到小排序</p>
     * <p>当start为0 end为-1时返回全部</p>
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> zrevrange(String key ,long start ,long end){
         return jedis.zrevrange(key, start, end);
    }

    /**
     * <p>通过key返回指定score内zset中的value</p>
     * @param key
     * @param max
     * @param min
     * @return
     */
    public Set<String> zrangebyscore(String key,String max,String min){
        return jedis.zrevrangeByScore(key, max, min);
    }

    /**
     * <p>通过key返回指定score内zset中的value</p>
     * @param key
     * @param max
     * @param min
     * @return
     */
    public Set<String> zrangeByScore(String key ,double max,double min){
         return jedis.zrevrangeByScore(key,max,min);
    }

    /**
     * <p>返回指定区间内zset中value的数量</p>
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Long zcount(String key,String min,String max){
         return jedis.zcount(key, min, max);
    }

    /**
     * <p>通过key返回zset中的value个数</p>
     * @param key
     * @return
     */
    public Long zcard(String key){
         return jedis.zcard(key);
    }

    /**
     * <p>通过key获取zset中value的score值</p>
     * @param key
     * @param member
     * @return
     */
    public Double zscore(String key,String member){
        return jedis.zscore(key, member);
    }

    /**
     * <p>通过key删除给定区间内的元素</p>
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Long zremrangeByRank(String key ,long start, long end){
        return jedis.zremrangeByRank(key, start, end);
    }

    /**
     * <p>通过key删除指定score内的元素</p>
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Long zremrangeByScore(String key,double start,double end){
        return jedis.zremrangeByScore(key, start, end);
    }
    /**
     * <p>返回满足pattern表达式的所有key</p>
     * <p>keys(*)</p>
     * <p>返回所有的key</p>
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern){
        return jedis.keys(pattern);
    }

    /**
     * <p>通过key判断值得类型</p>
     * @param key
     * @return
     */
    public String type(String key){
       return jedis.type(key);
    }

	public static RedisUtil getRu() {
		return ru;
	}

	public static void setRu(RedisUtil ru) {
		RedisUtil.ru = ru;
	}
}
