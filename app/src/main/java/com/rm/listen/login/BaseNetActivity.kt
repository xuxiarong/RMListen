package com.rm.listen.login

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.lm.mvvmcore.base.BaseVMActivity
import com.rm.listen.R
import com.rm.listen.databinding.ActivityBaseNetBinding

abstract class BaseNetActivity : BaseVMActivity() {


    protected abstract fun getLayoutId(): Int
    protected lateinit var childActivity : View



    protected val baseBinding by binding<ActivityBaseNetBinding>(R.layout.activity_base_net).apply {
    }

    protected inline fun <reified T : ViewDataBinding> initChildModule(): Lazy<T> = lazy {
        DataBindingUtil.setContentView<T>(this, getLayoutId()).apply {
            childActivity = View.inflate(this@BaseNetActivity,getLayoutId(),null)
            baseBinding.flBaseLayout.addView(childActivity,0)
        }
    }

    protected fun showContent(){
        childActivity.visibility = View.VISIBLE
        baseBinding.layoutEmpty.visibility = View.GONE
        baseBinding.layoutLoading.visibility = View.GONE
        baseBinding.layoutNetError.visibility = View.GONE
    }

    protected fun showLoading(){
        childActivity.visibility = View.VISIBLE
        baseBinding.layoutLoading.visibility = View.VISIBLE
        baseBinding.layoutEmpty.visibility = View.GONE
        baseBinding.layoutNetError.visibility = View.GONE
    }


    protected fun showNetError(){
        childActivity.visibility = View.GONE
        baseBinding.layoutNetError.visibility = View.VISIBLE
        baseBinding.layoutEmpty.visibility = View.GONE
        baseBinding.layoutLoading.visibility = View.GONE
    }

    protected fun showDataEmpty(){
        childActivity.visibility = View.GONE
        baseBinding.layoutEmpty.visibility = View.VISIBLE
        baseBinding.layoutNetError.visibility = View.GONE
        baseBinding.layoutLoading.visibility = View.GONE
    }
}