package main;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 物流车主启动程序
 * 
 * 1： 启动呼叫器程序 
 * 2：启动车辆识别程序
 * 
 * @author sjh
 *
 */
public class LogisticsMain {
	public static void main(String[] args) {
		// 声明一个容量为500的缓存队列
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>(500);
		//生产者-》呼叫器
		Producer producer = new Producer(queue);
		//消费者-》车辆识别仪
		Consumer consumer = new Consumer(queue);
		// 借助Executors
		ExecutorService service = Executors.newCachedThreadPool();
		// 启动线程
		service.execute(producer);
		service.execute(consumer);
	}
}
