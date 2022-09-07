package com.uuc.job.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;


public class AsynCallUtil {

    /** 默认执行器 */
    private static final ExecutorService DEFAULT;

    /** 执行器池 */
    public static final Map<String, ExecutorService> pool = new HashMap();

    static {
        DEFAULT = createExecutorForName("CMDB-DefaultPool", 30, 1024);
    }

    public static void run(Callable callable){
        DEFAULT.submit(callable);
    }

    public static void run(Runnable runnable){
        DEFAULT.submit(runnable);
    }

    /**
     * @param execName 执行器名称
     * @param callable 作业
     */
    public static void run(String execName, Callable callable){
        ExecutorService executor = getExecutor(execName);
        executor.submit(callable);
    }


    /**
     * @param execName 执行器名称
     * @param threadCoreSize 核心线程数
     * @param threadPoolSize 线程池大小
     * @param timeMilliseconds 空闲时间
     * @param callable 作业
     */
    public static void run(String execName,int threadCoreSize,int threadPoolSize,long timeMilliseconds, Callable callable){
        ExecutorService executor = getExecutor(execName, threadCoreSize,threadPoolSize,timeMilliseconds);
        executor.submit(callable);
    }


    /**
     * 从执行器中根据名称获取执行器，如果没有，则会新创建一个
     * @param execName 执行器逻辑名
     * @return 执行器对象
     */
    private static ExecutorService getExecutor(String execName) {
        if(StringUtils.isEmpty(execName)){
            return DEFAULT;
        }

        ExecutorService executor = pool.get(execName);
        if(executor != null){
            return executor;
        }
        synchronized (AsynCallUtil.class) {
            executor = pool.computeIfAbsent(execName, n -> createExecutorForName(n, 10, 56));
        }

        return executor;
    }

    private static ExecutorService getExecutor(String execName,int threadCoreSize,int threadPoolSize,long timeMilliseconds) {
        if(StringUtils.isEmpty(execName)||threadCoreSize<1||threadPoolSize<1){
            return DEFAULT;
        }
        //默认空闲设置为10秒
        if(timeMilliseconds<1L){
            timeMilliseconds=1000*10L;
        }

        ExecutorService executor = pool.get(execName);
        if(executor != null){
            return executor;
        }
        synchronized (AsynCallUtil.class) {
            executor = pool.get(execName);
            if (executor == null) {
                ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat(execName+"-%d").build();
                executor = new ThreadPoolExecutor(threadCoreSize,threadPoolSize,timeMilliseconds,TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>(),namedThreadFactory);
                pool.put(execName, executor);
            }
        }
        return executor;
    }

    /**
     * 根据执行器名、核心线程数和最大线程数创建执行器
     * @param executorName 执行器逻辑名
     * @param coreSize 核心线程数
     * @param maxSize 最大线程数
     * @return 新的执行器
     */
    private static ExecutorService createExecutorForName(String executorName, int coreSize, int maxSize){
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat(executorName+"-pool-%d").build();
        return new ThreadPoolExecutor(coreSize, maxSize, 10L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    }

}
