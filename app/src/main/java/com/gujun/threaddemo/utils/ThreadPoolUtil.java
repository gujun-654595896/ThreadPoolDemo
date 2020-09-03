package com.gujun.threaddemo.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * author : gujun
 * date   : 2020/8/14 13:28
 * desc   : 线程池工具类，避免过多的创建线程
 */
public class ThreadPoolUtil {

    //参数初始化，CPU个数
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    //核心线程数量大小
    private static final int corePoolSize = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    //线程池最大容纳线程数,Android 应用的话应该是属于IO密集型应用，所以数量一般设置为 2N+1
    private static final int maximumPoolSize = CPU_COUNT * 2 + 1;
    //线程空闲后的存活时长
    private static final int keepAliveTime = 30;

    private static ThreadPoolExecutor executor = null;

    private static ThreadPoolExecutor getExecutor() {
        if (executor == null) {
            synchronized (ThreadPoolUtil.class) {
                if (executor == null) {
                    executor = new ThreadPoolExecutor(
                            corePoolSize,
                            maximumPoolSize,
                            keepAliveTime,
                            TimeUnit.SECONDS,
                            new LinkedBlockingDeque<Runnable>(Integer.MAX_VALUE),//任务过多后，存储任务的一个阻塞队列
                            new ThreadFactory() {
                                private final AtomicInteger mCount = new AtomicInteger(1);

                                public Thread newThread(Runnable r) {
                                    return new Thread(r, "IMThreadPoolUtil #" + mCount.getAndIncrement());
                                }
                            },//线程的创建工厂,默认
                            new ThreadPoolExecutor.DiscardOldestPolicy()//线程池任务满载后采取的任务拒绝策略
                    );
                    //设置核心线程空闲时间超过keepAliveTime值时释放线程
                    executor.allowCoreThreadTimeOut(true);
                }
            }
        }
        return executor;
    }

    /**
     * 无返回值直接执行
     *
     * @param runnable
     */
    public static void execute(Runnable runnable) {
        getExecutor().execute(runnable);
    }

    /**
     * 返回值Future可控制异步回调状态，如果调用get()方法则会阻塞线程直到异步执行完返回值
     *
     * @param callable
     */
    public static <T> Future<T> submit(Callable<T> callable) {
        return getExecutor().submit(callable);
    }

}
