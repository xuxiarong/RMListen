package com.rm.baselisten.ktx

import androidx.lifecycle.MutableLiveData

/**
 * desc   :
 * date   : 2020/10/19
 * version: 1.0
 */

fun <T : Any> MutableLiveData<MutableList<T>>.updateValue(list: List<T>) {
    this.postValue(list.toMutableList())
}

fun <T : Any> MutableLiveData<MutableList<T>>.addAll(list: List<T>) {
    val tempList = if (this.value == null) {
        mutableListOf()
    } else {
        this.value!!
    }
    tempList.addAll(list)
    this.value = tempList
}

fun <E : Any> MutableLiveData<MutableList<E>>.add(element: E) {
    val tempList = if (this.value == null) {
        mutableListOf()
    } else {
        this.value!!
    }
    tempList.add(element)
    this.value = tempList
}

fun <E : Any> MutableLiveData<MutableList<E>>.remove(element: E) {
    if (null != this.value) {
        val tempList = this.value!!
        tempList.remove(element)
        this.value = tempList
    }
}