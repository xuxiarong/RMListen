package com.rm.business_lib.binding

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter

/**
 * desc   :
 * date   : 2020/10/14
 * version: 1.0
 */
@SuppressLint("SetTextI18n")
@BindingAdapter("bindPlayCount")
fun TextView.bindPlayCount(count :Int){
    text = if (count<10000){
        count.toString()
    }else{
        val wan = count/10000
        val qian = count%10000/1000
        val bai = count%1000/100
        "$wan"+"."+"$qian"+"$bai"+"w"
    }
}
@BindingAdapter("bindPlayCountString")
fun TextView.bindPlayCountString(count :String?){
    try{
        if(count!=null){
            bindPlayCount(count.toInt())
        }
    }catch (e : Exception){
        e.printStackTrace()
    }
}