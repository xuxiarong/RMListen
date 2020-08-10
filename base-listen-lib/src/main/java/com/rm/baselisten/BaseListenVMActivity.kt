package com.rm.baselisten

//import com.lm.mvvmcore.base.BaseVMActivity

/**
 * desc   :
 * date   : 2020/08/06
 * version: 1.1
 */
//abstract class BaseListenVMActivity : BaseVMActivity() {
//    protected abstract fun getLayoutId(): Int
//
//    protected val binding by binding<ActivityBaseListenVmBinding>(R.layout.activity_base_listen_vm).apply {
//    }
//
//    protected inline fun <reified T : ViewDataBinding> initChildModule(): Lazy<T> = lazy {
//        DataBindingUtil.setContentView<T>(this, getLayoutId()).apply {
//            lifecycleOwner = this@BaseListenVMActivity
//
//        }
//    }

//    fun statusView(status: ViewStatus) {
//        if (status == ViewStatus.NET_ERROR) {
//            // 网络错误
//            Log.i("llj", "网络错误")
//        }
//        binding.flBaseLayout.addView(View.inflate(this, getLayoutId(), null))
//    }
//
//    enum class ViewStatus {
//        NET_ERROR, EMPTY_VIEW
//    }
//}