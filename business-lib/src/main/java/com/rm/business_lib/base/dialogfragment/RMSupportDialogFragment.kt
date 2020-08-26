package com.rm.business_lib.base.dialogfragment

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commitNow


/**
 * @des:基类扩展
 * @data: 8/26/20 5:44 PM
 * @Version: 1.0.0
 */
open class RMSupportDialogFragment : RMDialogFragment() {

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes = attributes?.apply {
                if (isFullScreen()) {
                    width = WindowManager.LayoutParams.MATCH_PARENT
                    height = WindowManager.LayoutParams.MATCH_PARENT
                } else {
                    width = WindowManager.LayoutParams.WRAP_CONTENT
                    height = WindowManager.LayoutParams.WRAP_CONTENT
                }
            }
        }
    }

    open fun isFullScreen(): Boolean = false

    //requestWindowFeature(Window.FEATURE_NO_TITLE)
    //为了解决部分机型有蓝边或者有title占位的问题，如果对layout没有其他处理，可直接复写onSetInflaterLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(onSetInflaterLayout(), container, false)
    }



    @LayoutRes
    open fun onSetInflaterLayout(): Int {
        return 0
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            super.show(manager, tag)
        } catch (e: Exception) {
            e.printStackTrace()
            /***
             * 当前应用在后台时 收到通知弹窗处理
             */
            try {
                if (!isAdded) {

                    manager.commitNow(allowStateLoss = true) {
                        add(this@RMSupportDialogFragment, tag)
                    }

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    override fun onDismiss(dialog: DialogInterface) {
        try {
            super.onDismiss(dialog)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun dismissAllowingStateLoss() {
        try {
            super.dismissAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onDestroyView() {
        try {
            super.onDestroyView()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}
