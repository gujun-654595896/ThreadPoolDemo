package com.gujun.threaddemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.gujun.threaddemo.utils.ThreadPoolUtil
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    //缓存（线程在创建后还没被销毁时）线程池
    private lateinit var newCachedExecutors: ExecutorService

    //固定数量线程池
    private lateinit var newFixedExecutors: ExecutorService

    //延迟线程池
    private lateinit var newScheduledExecutors: ScheduledExecutorService

    //单线程池
    private lateinit var newSingleExecutors: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initExecutors()

        thread1.setOnClickListener { clickThread1() }
        thread2.setOnClickListener { clickThread2() }
        thread3.setOnClickListener { clickThread3() }
        thread4.setOnClickListener { clickThread4() }
        thread5.setOnClickListener { clickThread5() }
        thread6.setOnClickListener { clickThread6() }
    }

    private fun initExecutors() {
        newCachedExecutors = Executors.newCachedThreadPool()
        newFixedExecutors = Executors.newFixedThreadPool(4)
        newScheduledExecutors = Executors.newScheduledThreadPool(4)
        newSingleExecutors = Executors.newSingleThreadExecutor()
    }

    private fun clickThread1() {
        ThreadPoolUtil.execute {
            Log.e("Thread1----", Thread.currentThread().name)
            Thread.sleep(10_000)
        }
    }

    private fun clickThread2() {
        val a = ThreadPoolUtil.submit {
            Log.e("Thread2----", Thread.currentThread().name)
            Thread.sleep(10_000)
            "我是结果"
        }
        //get()方法会阻塞线程等待获取结果后才往下执行
        Log.e("Thread2----", "clickThread2结果: ${a.get()}")
    }

    private fun clickThread3() {
        newCachedExecutors.execute {
            Log.e("Thread3----", Thread.currentThread().name)
            Thread.sleep(10_000)
        }
    }

    private fun clickThread4() {
        newFixedExecutors.execute {
            Log.e("Thread4----", Thread.currentThread().name)
            Thread.sleep(10_000)
        }
    }

    private fun clickThread5() {
        newScheduledExecutors.schedule({
            Log.e("Thread5----", Thread.currentThread().name)
            Thread.sleep(10_000)
        }, 5_000, TimeUnit.MILLISECONDS)
    }

    private fun clickThread6() {
        newSingleExecutors.execute {
            Log.e("Thread6----", Thread.currentThread().name)
            Thread.sleep(10_000)
        }
    }

}