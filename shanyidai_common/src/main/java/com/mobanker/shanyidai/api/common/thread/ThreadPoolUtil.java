package com.mobanker.shanyidai.api.common.thread;

import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.mobanker.shanyidai.api.common.constant.AppConstants.LOG_THREAD_POOL_NUM;
import static com.mobanker.shanyidai.api.common.constant.AppConstants.THREAD_POOL_NUM;

/**
 * @author hantongyang
 * @description 利用classload机制来保证初始化instance时只有一个线程，并且只有主动调用时再初始化
 * @time 2017/2/27 16:46
 */
public class ThreadPoolUtil {

    /**
     * @description 静态内部类，实现主动调用时再加载。初始化线程数是300的线程池，队列数量最大是300，等待时间是10分钟。
     * @author hantongyang
     * @time 2017/2/27 17:17
     */
    private static class SingletonHolder{
        private static BlockingQueue<Runnable> QUEUE = new ArrayBlockingQueue<Runnable>(LOG_THREAD_POOL_NUM);
        /**
         * 参数1：核心线程池大小
         * 参数2：最大线程池大小
         * 参数3：线程池中超过“参数1”数目的空闲线程最大存活时间；可以allowCoreThreadTimeOut(true)使得核心线程有效时间
         * 参数4：时间单位，配合“参数3”使用
         * 参数5：阻塞任务队列
         * 参数6：当提交任务数超过“参数2”+“参数5”之和时，任务会交给RejectedExecutionHandler来处理
         */
        private static ThreadPoolExecutor POOL = new ThreadPoolExecutor(LOG_THREAD_POOL_NUM, LOG_THREAD_POOL_NUM, 10, TimeUnit.MINUTES, QUEUE, new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /**
     * @description 静态内部类，实现主动调用时再加载。初始化线程数是100的线程池，队列数量最大是100，等待时间是10分钟。
     * @author hantongyang
     * @time 2017/2/27 17:17
     */
    private static class ShortSingletonHolder{
        private static BlockingQueue<Runnable> QUEUE = new ArrayBlockingQueue<Runnable>(THREAD_POOL_NUM);
        private static ThreadPoolExecutor POOL = new ThreadPoolExecutor(THREAD_POOL_NUM, THREAD_POOL_NUM, 10, TimeUnit.MINUTES, QUEUE, new ThreadPoolExecutor.CallerRunsPolicy());
    }

    private ThreadPoolUtil() {
    }

    /**
     * @description 获取线程池
     * @author hantongyang
     * @time 2017/2/27 17:24
     * @method newInstanct
     * @param poolNum
     * @return java.util.concurrent.ThreadPoolExecutor
     */
    public static final ThreadPoolExecutor newInstanct(int poolNum){
        if(poolNum - LOG_THREAD_POOL_NUM == 0){
            return SingletonHolder.POOL;
        }else if(poolNum - THREAD_POOL_NUM == 0){
            return ShortSingletonHolder.POOL;
        }else{
            throw new SydException(ReturnCode.THREAD_NULL);
        }
    }

}
