package io.netty.cases.chapter1;

import java.util.concurrent.TimeUnit;

/**
 * Created by 李林峰 on 2018/8/3.
 * Updated by liwenguang on 2020/05/01.
 * 非守护线程与主线程
 */
public class DaemonT2 {
    public static void main(String[] args)
            throws IllegalArgumentException, InterruptedException {
        long startTime = System.nanoTime();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.DAYS.sleep(Long.MAX_VALUE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Daemon-T");
//        t.setDaemon(true);
        t.setDaemon(false);
        t.start();
        TimeUnit.SECONDS.sleep(15);
        System.out.println("系统退出，程序执行" + (System.nanoTime() - startTime) / 1000 / 1000 / 1000 + " s");
    }
}
