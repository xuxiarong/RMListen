package com.rm.baselisten

import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.lm.mvvmcore.base.BaseVMActivity
import com.rm.baselisten.databinding.ActivityVmBaseBinding

/**
 * desc   :
 * date   : 2020/08/06
 * version: 1.1
 */
abstract class BaseListenVMActivity : BaseVMActivity() {
    protected abstract fun getLayoutId(): Int

    protected val binding by binding<ActivityVmBaseBinding>(R.layout.activity_vm_base).apply {
        statusView(ViewStatus.NET_ERROR)
    }

    fun statusView(status: ViewStatus) {
        if (status == ViewStatus.NET_ERROR) {
            // 网络错误
            Log.i("llj", "网络错误")
        }
        binding.flBaseLayout.addView(View.inflate(this, getLayoutId(), null))
    }

    protected inline fun <reified T : ViewDataBinding> initChildModule(): Lazy<T> = lazy {
        DataBindingUtil.setContentView<T>(this, getLayoutId()).apply {
            lifecycleOwner = this@BaseListenVMActivity

        }
    }

    enum class ViewStatus {
        NET_ERROR, EMPTY_VIEW
    }
}