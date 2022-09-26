package com.lzm.lightLive.util

import java.lang.Thread
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

object Thread {
    //线程池核心线程数
    private const val CORE_POOL_SIZE = 5

    //线程池最大线程数
    private const val MAX_POOL_SIZE = 100

    //额外线程空状态生存时间
    private const val KEEP_ALIVE_TIME = 10000L

    //阻塞队列。当核心线程都被占用，且阻塞队列已满的情况下，才会开启额外线程。
    private val workQueue: BlockingQueue<Runnable?> = ArrayBlockingQueue<Runnable?>(10)

    //线程池
    private var threadPool: ThreadPoolExecutor? = null

    //线程工厂
    private val threadFactory: ThreadFactory = object : ThreadFactory {
        private val integer = AtomicInteger()
        override fun newThread(r: Runnable): Thread {
            return Thread(r, "mThreadPool thread:" + integer.getAndIncrement())
        }
    }

    fun execute(runnable: Runnable?) {
        threadPool!!.execute(runnable)
    }

    fun execute(futureTask: FutureTask<*>?) {
        threadPool!!.execute(futureTask)
    }

    fun cancel(futureTask: FutureTask<*>) {
        futureTask.cancel(true)
    }

    init {
        threadPool = ThreadPoolExecutor(
            CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
            TimeUnit.SECONDS, workQueue, threadFactory
        )
    }
}