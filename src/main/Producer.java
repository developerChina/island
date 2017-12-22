package main;

import java.util.concurrent.BlockingQueue;

/**
 * 生产者线程
 * 
 * @author sjh
 */
public class Producer implements Runnable {

	public Producer(BlockingQueue<String> queue) {
		this.queue = queue;
	}

	public void run() {
		new IslandApp(this.queue);
		System.out.println("====== 呼叫器程序  启动成功  =======");
	}
	private BlockingQueue<String> queue;
}