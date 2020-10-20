package com.rm.baselisten.ktx

import androidx.lifecycle.MutableLiveData

/**
 * desc   :
 * date   : 2020/10/19
 * version: 1.0
 */

fun<T : Any> MutableLiveData<MutableList<T>>.updateValue(list : List<T>){
    this.postValue(list.toMutableList())
}
fun<T : Any> MutableLiveData<MutableList<T>>.addValue(list : List<T>){
    if(null !=this.value){
        val tempList = this.value!!
        tempList.addAll(list)
        this.postValue(tempList)
    }else{
        this.postValue(list.toMutableList())
    }
}