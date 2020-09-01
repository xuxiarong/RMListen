package com.rm.baselisten.dialog

import android.app.Dialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.thridlib.lifecycle.LifecycleHelper.DestroyListener
import com.rm.baselisten.thridlib.lifecycle.LifecycleHelper.with
import java.lang.ref.SoftReference

/**
 * author：HeXinGen
 * date: 2019/10/23
 * description: Dialog的辅助类：  在Activity中销毁,应该关闭Dialog,防止意外情况出现
 */
object DialogHelper {
    fun bindLife(
        dialog: Dialog?,
        fragmentActivity: FragmentActivity?
    ): DialogTask? {
        return if (fragmentActivity == null || dialog == null) {
            null
        } else DialogTask().with(dialog, fragmentActivity)
    }

    fun bindLife(
        dialog: Dialog?,
        fragment: Fragment?
    ): DialogTask? {
        return if (fragment == null || dialog == null) {
            null
        } else DialogTask().with(dialog, fragment)
    }

    interface DialogOperate {
        fun show()
        fun dismiss()
    }

    /**
     * 定义一个DialogTask
     */
    class DialogTask : DialogOperate {
        private var proxyOperate: DialogOperate? = null
        fun with(
            dialog: Dialog?,
            fragment: Fragment?
        ): DialogTask {
            val observer =
                DefaultLifecycleObserver(dialog)
            proxyOperate = observer
            with(fragment!!).addDestroyListener(observer)
            return this
        }

        fun with(
            dialog: Dialog?,
            activity: FragmentActivity?
        ): DialogTask {
            val observer =
                DefaultLifecycleObserver(dialog)
            proxyOperate = observer
            with(activity!!).addDestroyListener(observer)
            return this
        }

        override fun show() {
            proxyOperate!!.show()
        }

        override fun dismiss() {
            proxyOperate!!.dismiss()
        }
    }

    /**
     * 监听Activity和Fragment的生命周期，在onDestroy中取消dialog,防止内存泄漏
     */
    private class DefaultLifecycleObserver(dialog: Dialog?) : DialogOperate,
        DestroyListener {
        private var softReference: SoftReference<Dialog?>?
        val dialog: Dialog?
            get() = if (softReference == null) null else softReference!!.get()

        override fun show() {
            val dialog = dialog
            if (dialog != null && !dialog.isShowing) {
                dialog.show()
            }
        }

        override fun dismiss() {
            val dialog = dialog
            if (dialog != null && dialog.isShowing) {
                dialog.dismiss()
            }
        }

        override fun IDestroy() {
            dismiss()
            if (softReference != null) {
                softReference!!.clear()
                softReference = null
            }
        }

        companion object {
            fun with(dialog: Dialog?): DefaultLifecycleObserver {
                return DefaultLifecycleObserver(dialog)
            }
        }

        init {
            softReference = SoftReference(dialog)
        }
    }
}