package main;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
 
/**
 * 消费者线程
 * 
 * @author sjh
 */
public class Consumer implements Runnable {
 
    public Consumer(BlockingQueue<String> queue) {
        this.queue = queue;
    }
 
    public void run() {
        System.out.println("====== 车辆识别程序  启动成功  =======");
        boolean isRunning = true;
        try {
            while (isRunning) {
                String data = queue.poll(2, TimeUnit.SECONDS);
                if (null != data) {
                    System.out.println("拿到数据：" + data);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
    private BlockingQueue<String> queue;
}
