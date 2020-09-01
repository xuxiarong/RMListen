package com.rm.baselisten.dialog

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.rm.baselisten.R
import com.rm.baselisten.helper.GlobalCloseInputHelper

/**
 * desc   :
 * date   : 2020/08/21
 * version: 1.0
 */
abstract class BaseFragmentDialog : DialogFragment() {
    protected lateinit var rootView: View
    val gravity = Gravity.CENTER
    var type = 0

    /**
     * 是否拦截点击EditText 之外，关闭输入法
     * @return false 不做处理
     */
    protected fun interceptClickInputClose(): Boolean {
        return false
    }

    /**
     * 触摸dialog之外的屏幕，是否关闭dialog
     *
     * @return
     */
    protected fun isDialogCanceledOnTouchOutside() : Boolean {
        return true
    }

    /**
     * back 键是否关闭dialog
     *
     * @return
     */
    protected fun isDialogCancel() : Boolean {
        return true
    }

    /**
     * dialog外部是否具有半透明的背景
     * @return Boolean
     */
    protected fun isDialogHasBackground(): Boolean {
        return false
    }

    protected fun setBackgroundColor(): Int {
        return R.color.base_transparent
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireContext(), theme) {
            override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
                if (ev.action == MotionEvent.ACTION_DOWN) {
                    if (interceptClickInputClose()) {
                        GlobalCloseInputHelper.DialogDispatchTouchEvent()
                            .dispatchTouchEventCloseInput(ev, this)
                    }
                }
                return super.dispatchTouchEvent(ev)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        if (dialog != null && dialog?.window != null) {
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
            super.onActivityCreated(savedInstanceState)
            dialog!!.window!!.setGravity(gravity)
            isCancelable = isDialogCancel()
            dialog!!.setCanceledOnTouchOutside(isDialogCanceledOnTouchOutside())
            if(!isDialogHasBackground()){
                val params: WindowManager.LayoutParams = dialog!!.window!!.attributes
                params.gravity = Gravity.CENTER
                //设置Dialog外部透明
                //设置Dialog外部透明
                params.dimAmount = 0f
                dialog!!.window!!.attributes = params
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(layoutId, container, false)
        initView()
        return rootView
    }

    fun showDialog(fragmentActivity: FragmentActivity): BaseFragmentDialog {
        show(fragmentActivity.supportFragmentManager, this.javaClass.simpleName)

        return this
    }

    override fun show(
        manager: FragmentManager,
        tag: String?
    ) {
        val fragment =
            manager.findFragmentByTag(tag) as DialogFragment?
        try {
            // 检查，若是存在相同的tag 的fragment，先移除
            fragment?.dismissAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            val mClass: Class<*> = DialogFragment::class.java
            val field1 = mClass.getDeclaredField("mShownByMe")
            field1.isAccessible = true
            field1[this] = true
            val field2 = mClass.getDeclaredField("mDismissed")
            field2.isAccessible = true
            field2[this] = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val ft = manager.beginTransaction()
        // 已经添加过add，就 show
        if (this.isAdded) {
            ft.show(this)
        } else {
            ft.add(this, tag)
        }
        ft.commitAllowingStateLoss()
    }

    override fun dismiss() {
        try {
            dismissAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //    super.dismiss();
        DialogDismiss.safeDismiss(dialog)
    }

    object DialogDismiss {
        /**
         * 处理Dialog的Handler导致内存泄漏
         * @param dialog
         */
        fun safeDismiss(dialog: Dialog?) {
            try {
                val mClass = Dialog::class.java
                val mListenersHandlerField =
                    mClass.getDeclaredField("mListenersHandler")
                mListenersHandlerField.isAccessible = true
                val handler =
                    mListenersHandlerField[dialog] as Handler
                handler.removeCallbacksAndMessages(null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    abstract val layoutId: Int
    fun setType(type: Int): BaseFragmentDialog {
        this.type = type
        return this
    }

    protected open fun initView() {}
}