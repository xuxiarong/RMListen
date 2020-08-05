package com.lm.common.net

import java.io.Serializable

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class RetrofitKotlinManager private constructor() : Serializable{

    companion object {
        //通过@JvmStatic注解，使得在Java中调用instance直接是像调用静态函数一样，
        //类似KLazilyDCLSingleton.getInstance(),如果不加注解，在Java中必须这样调用: KLazilyDCLSingleton.Companion.getInstance().
        @JvmStatic
        //使用lazy属性代理，并指定LazyThreadSafetyMode为SYNCHRONIZED模式保证线程安全
        val instance: RetrofitKotlinManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { RetrofitKotlinManager() }
    }

//    fun initRetrofit(){
//        val retrofit = Retrofit.Builder()
//            .baseUrl(RetrofitClient.BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(CoroutineCallAdapterFactory())
//            .build()
//    }

}