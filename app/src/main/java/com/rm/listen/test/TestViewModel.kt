package com.rm.listen.test

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rm.baselisten.viewmodel.BaseNetViewModel
import com.rm.listen.R
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.flow as flow

/**
 *
 * @ClassName: TestViewModel
 * @Description:
 * @Author: 鲸鱼
 * @CreateDate: 8/12/20 10:35 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 8/12/20 10:35 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0.0
 */
class TestViewModel :
    BaseNetViewModel(R.layout.activity_home_test) {

    val downTimeData = MutableLiveData<String>()
    val downTime = ObservableField<String>()
    fun downTime() {
        if ((downTime.get() ?: "0" ).toInt()> 0) {
            viewModelScope.launch {
                flow {
                    for (i in 1..(downTime.get() ?: "0" ) .toInt()) {
                        delay(1000)
                        emit("倒计时：${i}")
                    }
                }.flowOn(Dispatchers.Default)
                    .onStart {
                        Log.i("downTime", "开始倒计时")

                    }.onCompletion {
                        Log.i("downTime", "倒计时结束")
                    }
                    .collect {
                        downTimeData.postValue(it)
                    }
            }
        }


    }
    fun testLoad(){
        viewModelScope.launch {
//            val result = withContext(Dispatchers.Default){
//                testD()
//            }
            val result=testD()
            Log.i("testLoad","子线程work${result}")

        }
        Log.i("testLoad","继续执行主线成")
    }
    suspend fun testD():String{
        delay(3000)
        return "迟到的正义${Thread.currentThread()}"
    }
    //合并两个延时任务
    fun zipLoad(){
        viewModelScope.launch {
            flow{
                emit(load1())
            }.flatMapConcat {
                return@flatMapConcat flow {
                    emit(load2(it))
                }
            }.onStart {
                Log.i("zipLoad","开始请求000")
            }.onCompletion {
                Log.i("zipLoad","请求结束")
            }.collect{
                Log.i("zipLoad","请求结果${it}")
            }
        }
    }
    fun<T> launchFlow(block:suspend() ->T):Flow<T>{
        return  flow {
            emit(block())
        }
    }
    suspend fun load1():String{
        delay(3000)
        return "我是延时1的任务"
    }
    suspend fun load2(msg:String): String {
        delay(3000)
        return "${msg}我是延时1的任务"
    }
}