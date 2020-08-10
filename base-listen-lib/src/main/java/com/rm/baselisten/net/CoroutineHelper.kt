//package com.sl.common
//
//import kotlinx.coroutines.*
//import kotlin.coroutines.CoroutineContext
//
///**
// * desc   :
// * date   : 2020/08/04
// * version: 1.0
// */
//class CoroutineHelper{
//
//
//    fun launch(){
//        GlobalScope.launch { // 在后台启动一个新的协程并继续
//            delay(1000L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒）
//            println("World!") // 在延迟后打印输出
//        }
//
//
//    }
//
////    suspend fun <T : Any> request(call: suspend () -> ResponseData<T>): ResponseData<T> {
////        return withContext(Dispatchers.IO){ call.invoke()}
////    }
//
//    companion object{
//        fun launchWithApplication(){
//            GlobalScope.launch(Dispatchers.Unconfined, start = CoroutineStart.LAZY,)
//            println("Hello,") // 主线程中的代码会立即执行
//            runBlocking {     // 但是这个表达式阻塞了主线程
//                delay(2000L)  // ……我们延迟 2 秒来保证 JVM 的存活
//            }
//        }
//
//    }
//}