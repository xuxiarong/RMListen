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
@Suppress("UNCHECKED_CAST")
abstract class BaseFragmentDialog : DialogFragment() {

    companion object{
        const val LAYOUT_ID = "layoutId"
    }

    private lateinit var rootView: View

    /**
     * Dialog对齐方式
     */
    var gravity = Gravity.CENTER

    /**
     * 是否拦截点击EditText 之外，关闭输入法
     * @return false 不做处理
     */
    var dialogInterceptClickInput = false

    /**
     * 触摸dialog之外的屏幕，是否关闭dialog
     */
    var dialogCanceledOnTouchOutside = true

    /**
     * dialog是否可被取消
     */
    var dialogCancel = true

    /**
     * dialog是否包含背景颜色
     */
    var dialogHasBackground = false

    /**
     * dialog是否水平方向全屏
     */
    var dialogWidthIsMatchParent = false

    /**
     * dialog是否竖直方向全屏
     */
    var dialogHeightIsMatchParent = false

    /**
     * dialog的背景颜色
     */
    var dialogBackgroundColor = R.color.base_transparent

    /**
     * dialog的宽度
     */
    var dialogWidth = 0;

    /**
     * dialog的高度
     */
    var dialogHeight = 0
    /**
     * 创建Dialog实例对象
     * @param savedInstanceState Bundle?
     * @return Dialog
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireContext(), theme) {
            override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
                if (ev.action == MotionEvent.ACTION_DOWN) {
                    if (dialogInterceptClickInput) {
                        GlobalCloseInputHelper.DialogDispatchTouchEvent()
                            .dispatchTouchEventCloseInput(ev, this)
                    }
                }
                return super.dispatchTouchEvent(ev)
            }
        }
    }

    /**
     * Activity创建时回调
     * @param savedInstanceState Bundle?
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        if (dialog != null && dialog?.window != null) {
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
            super.onActivityCreated(savedInstanceState)
            dialog!!.window!!.setGravity(gravity)
            isCancelable = dialogCancel
            dialog!!.setCanceledOnTouchOutside(dialogCanceledOnTouchOutside)
            dialog!!.setCancelable(isCancelable)
            if (!dialogHasBackground) {
                val params: WindowManager.LayoutParams = dialog!!.window!!.attributes
                params.gravity = gravity
                //设置Dialog外部透明
                //设置Dialog外部透明
                params.dimAmount = 0f
                dialog!!.window!!.attributes = params
            } else {
                dialog!!.window?.setBackgroundDrawableResource(R.color.base_transparent_80) // 背景透明
            }

            val layoutWidth = if (dialogWidth > 0) {
                dialogWidth
            } else {
                if (dialogWidthIsMatchParent) {
                    WindowManager.LayoutParams.MATCH_PARENT
                } else {
                    WindowManager.LayoutParams.WRAP_CONTENT
                }
            }
            val layoutHeight = if (dialogHeight > 0) {
                dialogHeight
            } else {
                if (dialogHeightIsMatchParent) {
                    WindowManager.LayoutParams.MATCH_PARENT
                } else {
                    WindowManager.LayoutParams.WRAP_CONTENT
                }
            }
            dialog!!.window!!.setLayout(layoutWidth, layoutHeight)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutId = arguments?.getInt(LAYOUT_ID, 0)?:0
        rootView = inflater.inflate(layoutId, container, false)
        initView()
        return rootView
    }

    /**
     * 显示Dialog
     * @param fragmentActivity FragmentActivity
     * @return BaseFragmentDialog
     */
    fun <T: BaseFragmentDialog> showDialog(fragmentActivity: FragmentActivity): T {
        show(fragmentActivity.supportFragmentManager, this.javaClass.simpleName)

        return this as  T
    }

    /**
     * 重写父类的show方法，使用反射调用，防止异常场景的崩溃和内存泄漏
     * @param manager FragmentManager
     * @param tag String?
     */
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

    /**
     * 重写父类的dismiss方法
     */
    override fun dismiss() {
        try {
            dismissAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
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

    protected open fun initView() {}
}