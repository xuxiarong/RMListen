package com.rm.baselisten.activity

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.rm.baselisten.R
import com.rm.baselisten.databinding.ActivityBaseNetBinding
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.net.bean.BaseStatusModel

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
abstract class BaseNetActivity : BaseVMActivity() {

    abstract fun getLayoutId(): Int
    protected lateinit var childActivity : View
    protected lateinit var statusModel: BaseStatusModel


    protected val baseBinding by binding<ActivityBaseNetBinding>(R.layout.activity_base_net)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        childActivity = View.inflate(this@BaseNetActivity,getLayoutId(),null)
        baseBinding.flBaseLayout.addView(childActivity,0)
        statusModel = BaseStatusModel(true,true,false)
        baseBinding.statusModel = statusModel
    }

    protected inline fun <reified T : ViewDataBinding> initChildModule(): Lazy<T> = lazy {
        DataBindingUtil.setContentView<T>(this, getLayoutId())
    }

    protected fun showContent(){
        childActivity.visibility = View.VISIBLE
        baseBinding.statusModel = BaseStatusModel(true,true,false)
    }

    protected fun showLoading(){
        childActivity.visibility = View.VISIBLE
        baseBinding.statusModel = BaseStatusModel(true,true,true)

    }


    protected fun showNetError(){
        childActivity.visibility = View.GONE
        baseBinding.statusModel = BaseStatusModel(false,true,false)

    }

    protected fun showDataEmpty(){
        childActivity.visibility = View.GONE
        baseBinding.statusModel = BaseStatusModel(true,false,false)
    }
}