package com.rm.baselisten.util.toast

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.WindowManager.BadTokenException
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.ToastUtil
import com.rm.baselisten.util.toast.draggable.BaseDraggable
import java.lang.RuntimeException

/**
 *
 * @author yuanfang
 * @date 12/31/20
 * @description
 *
 */
class XToast @JvmOverloads constructor(context: Context) {

    private val HANDLER = Handler(Looper.getMainLooper())

    /** 上下文  */
    private var mContext: Context? = null

    /** 根布局  */
    private var mRootView: View? = null

    /** 悬浮窗口  */
    private var mWindowManager: WindowManager? = null

    /** 窗口参数  */
    private var mWindowParams: WindowManager.LayoutParams? = null

    /** 当前是否已经显示  */
    private var mShow = false

    /** 窗口显示时长  */
    private var mDuration = 0

    /** 自定义拖动处理  */
    private var mDraggable: BaseDraggable? = null

    /** 吐司显示和取消监听  */
    private var mListener: OnToastListener? = null
    private var mCanAutoCancel = true

    init {
        mContext = context
        if (mContext is Application) {
            throw RuntimeException("content is not application")
        }
        if (mContext is Activity) {
            val activity = mContext as Activity
            if (activity.window.attributes.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN != 0) {
                // 如果当前 Activity 是全屏模式，那么需要添加这个标记，否则会导致 WindowManager 在某些机型上移动不到状态栏的位置上
                // 如果不想让状态栏显示的时候把 WindowManager 顶下来，可以添加 FLAG_LAYOUT_IN_SCREEN，但是会导致软键盘无法调整窗口位置
                addWindowFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }
        }

        mWindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        // 配置一些默认的参数
        mWindowParams = WindowManager.LayoutParams()
        mWindowParams?.apply {
            height = WindowManager.LayoutParams.WRAP_CONTENT
            width = WindowManager.LayoutParams.WRAP_CONTENT
            format = PixelFormat.TRANSLUCENT
            windowAnimations = android.R.style.Animation_Toast
            packageName = context.packageName
            // 开启窗口常亮和设置可以触摸外层布局（除 WindowManager 外的布局，默认是 WindowManager 显示的时候外层不可触摸）
            // 需要注意的是设置了 FLAG_NOT_TOUCH_MODAL 必须要设置 FLAG_NOT_FOCUSABLE，否则就会导致用户按返回键无效
            flags = (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        }

    }

    /**
     * 是否外层可触摸
     */
    fun setOutsideTouchable(touchable: Boolean): XToast {
        val flags =
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        if (touchable) {
            addWindowFlags(flags)
        } else {
            clearWindowFlags(flags)
        }
        if (isShow()) {
            update()
        }
        return this
    }

    /**
     * 设置窗口背景阴影强度
     */
    fun setBackgroundDimAmount(amount: Float): XToast {
        require(!(amount < 0 || amount > 1)) { "are you ok?" }
        mWindowParams!!.dimAmount = amount
        val flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        if (amount != 0f) {
            addWindowFlags(flags)
        } else {
            clearWindowFlags(flags)
        }
        if (isShow()) {
            update()
        }
        return this
    }

    /**
     * 是否有这个标志位
     */
    fun hasWindowFlags(flags: Int): Boolean {
        return mWindowParams!!.flags and flags != 0
    }

    /**
     * 添加一个标记位
     */
    fun addWindowFlags(flags: Int): XToast {
        mWindowParams!!.flags = mWindowParams!!.flags or flags
        if (isShow()) {
            update()
        }
        return this
    }

    /**
     * 移除一个标记位
     */
    fun clearWindowFlags(flags: Int): XToast {
        mWindowParams!!.flags = mWindowParams!!.flags and flags.inv()
        if (isShow()) {
            update()
        }
        return this
    }

    /**
     * 设置标记位
     */
    fun setWindowFlags(flags: Int): XToast {
        mWindowParams!!.flags = flags
        if (isShow()) {
            update()
        }
        return this
    }

    /**
     * 设置窗口类型
     */
    fun setWindowType(type: Int): XToast {
        mWindowParams!!.type = type
        if (isShow()) {
            update()
        }
        return this
    }

    /**
     * 设置动画样式
     */
    fun setAnimStyle(id: Int): XToast {
        mWindowParams!!.windowAnimations = id
        if (isShow()) {
            update()
        }
        return this
    }


    /**
     * 设置拖动规则
     */
    fun setDraggable(draggable: BaseDraggable?): XToast {
        // 当前是否设置了不可触摸，如果是就擦除掉这个标记
        if (hasWindowFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)) {
            clearWindowFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
        mDraggable = draggable
        if (isShow()) {
            update()
            mDraggable?.start(this)
        }
        return this
    }

    /**
     * 设置宽度
     */
    fun setWidth(width: Int): XToast {
        mWindowParams!!.width = width
        if (isShow()) {
            update()
        }
        return this
    }

    /**
     * 设置高度
     */
    fun setHeight(height: Int): XToast {
        mWindowParams!!.height = height
        if (isShow()) {
            update()
        }
        return this
    }

    /**
     * 限定显示时长
     */
    fun setDuration(duration: Int): XToast {
        mDuration = duration
        if (isShow()) {
            if (mDuration != 0) {
                removeCallbacks()
                postDelayed(CancelRunnable(this), mDuration.toLong())
            }
        }
        return this
    }

    fun setCanAutoCancel(canAutoCancel: Boolean): XToast {
        mCanAutoCancel = canAutoCancel
        return this
    }

    /**
     * 设置监听
     */
    fun setOnToastListener(listener: OnToastListener?): XToast {
        mListener = listener
        return this
    }

    /**
     * 设置窗口重心
     */
    fun setGravity(gravity: Int): XToast {
        mWindowParams!!.gravity = gravity
        if (isShow()) {
            update()
        }
        return this
    }

    /**
     * 设置窗口方向
     * @param orientation
     * 自适应：[ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED]
     * 横屏：[ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE]
     * 竖屏：[ActivityInfo.SCREEN_ORIENTATION_PORTRAIT]
     */
    fun setOrientation(orientation: Int): XToast {
        mWindowParams!!.screenOrientation = orientation
        if (isShow()) {
            update()
        }
        return this
    }

    /**
     * 设置水平偏移量
     */
    fun setXOffset(x: Int): XToast {
        mWindowParams!!.x = x
        if (isShow()) {
            update()
        }
        return this
    }

    /**
     * 设置垂直偏移量
     */
    fun setYOffset(y: Int): XToast {
        mWindowParams!!.y = y
        if (isShow()) {
            update()
        }
        return this
    }

    /**
     * 重新设置 WindowManager 参数集
     */
    fun setWindowParams(params: WindowManager.LayoutParams?): XToast {
        mWindowParams = params
        if (isShow()) {
            update()
        }
        return this
    }

    /**
     * 设置布局
     */
    fun setView(id: Int): XToast {
        return setView(LayoutInflater.from(mContext).inflate(id, FrameLayout(mContext!!), false))
    }

    private fun setView(view: View?): XToast {
        mRootView = view
        val params = mRootView!!.layoutParams
        if (params != null && mWindowParams!!.width == WindowManager.LayoutParams.WRAP_CONTENT && mWindowParams!!.height == WindowManager.LayoutParams.WRAP_CONTENT) {
            // 如果当前 Dialog 的宽高设置了自适应，就以布局中设置的宽高为主
            setWidth(params.width)
            setHeight(params.height)
        }

        // 如果当前没有设置重心，就自动获取布局重心
        if (mWindowParams!!.gravity == Gravity.NO_GRAVITY) {
            when (params) {
                is FrameLayout.LayoutParams -> {
                    setGravity(params.gravity)
                }
                is LinearLayout.LayoutParams -> {
                    setGravity(params.gravity)
                }
                else -> {
                    // 默认重心是居中
                    setGravity(Gravity.CENTER)
                }
            }
        }
        if (isShow()) {
            update()
        }
        return this
    }

    /**
     * 显示
     */
    fun show(lifecycleOwner: LifecycleOwner? = null): XToast {
        require(!(mRootView == null || mWindowParams == null)) { "WindowParams and view cannot be empty" }

        // 如果当前已经显示取消上一次显示
        if (mShow) {
            cancel()
        }
        if (mContext is Activity) {
            if ((mContext as Activity).isFinishing || ((mContext as Activity).isDestroyed)) {
                return this
            }
        }
        if (lifecycleOwner != null) {
            val lifecycleRegistry = LifecycleRegistry(lifecycleOwner)
            lifecycleRegistry.addObserver(ToastLifecycle(this, lifecycleRegistry))
        }
        try {
            // 如果这个 View 对象被重复添加到 WindowManager 则会抛出异常
            // java.lang.IllegalStateException: View android.widget.TextView{3d2cee7 V.ED..... ......ID 0,0-312,153} has already been added to the window manager.
            mRootView?.let {
                if (it.isAttachedToWindow) {
                    mWindowManager?.removeView(it)
                }
                mWindowManager?.addView(it, mWindowParams)
            }

            // 当前已经显示
            mShow = true
            // 如果当前限定了显示时长
            if (mDuration != 0 && mCanAutoCancel) {
                postDelayed(CancelRunnable(this), mDuration.toLong())
            }
            // 如果设置了拖拽规则
            if (mDraggable != null) {
                mDraggable?.start(this)
            }
            // 回调监听
            if (mListener != null) {
                mListener?.onShow(this)
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: BadTokenException) {
            e.printStackTrace()
        }
        return this
    }

    /**
     * 取消
     */
    fun cancel(): XToast {
        if (mShow) {
            try {
                // 如果当前 WindowManager 没有附加这个 View 则会抛出异常
                // java.lang.IllegalArgumentException: View=android.widget.TextView{3d2cee7 V.ED..... ........ 0,0-312,153} not attached to window manager
                mWindowManager?.removeViewImmediate(mRootView)

                // 回调监听
                if (mListener != null) {
                    mListener?.onDismiss(this)
                }
            } catch (e: NullPointerException) {
                e.printStackTrace()
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
            // 当前没有显示
            mShow = false
        }
        return this
    }

    /**
     * 更新
     */
    fun update() {
        // 更新 WindowManger 的显示
        mWindowManager?.updateViewLayout(mRootView, mWindowParams)
    }

    /**
     * 回收
     */
    fun recycle() {
        mContext = null
        mWindowManager = null
        mListener = null
        mDraggable = null
        mRootView = null
    }

    /**
     * 当前是否已经显示
     */
    fun isShow(): Boolean {
        return mShow
    }

    /**
     * 获取 WindowManager 对象
     */
    fun getWindowManager(): WindowManager? {
        return mWindowManager
    }

    /**
     * 获取 WindowManager 参数集
     */
    fun getWindowParams(): WindowManager.LayoutParams? {
        return mWindowParams
    }

    /**
     * 获取上下文对象
     */
    fun getContext(): Context? {
        return mContext
    }

    /**
     * 获取根布局
     */
    fun getView(): View? {
        return mRootView
    }

    /**
     * 根据 ViewId 获取 View
     */
    fun <V : View?> findViewById(id: Int): V? {
        checkNotNull(mRootView) { "Please setup view" }
        return mRootView?.findViewById<View>(id) as V?
    }

    /**
     * 跳转 Activity
     */
    fun startActivity(clazz: Class<out Activity?>?) {
        startActivity(Intent(mContext, clazz))
    }

    fun startActivity(intent: Intent) {
        if (mContext !is Activity) {
            // 如果当前的上下文不是 Activity，调用 startActivity 必须加入新任务栈的标记
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        mContext!!.startActivity(intent)
    }

    /**
     * 设置可见状态
     */
    fun setVisibility(id: Int, visibility: Int): XToast {
        findViewById<View>(id)?.visibility = visibility
        return this
    }

    /**
     * 设置文本
     */
    fun setText(id: Int): XToast {
        return setText(android.R.id.message, id)
    }

    fun setText(viewId: Int, stringId: Int): XToast {
        return setText(viewId, mContext!!.resources.getString(stringId))
    }

    fun setText(text: CharSequence?): XToast {
        return setText(android.R.id.message, text)
    }

    fun setText(id: Int, text: CharSequence?): XToast {
        (findViewById<View>(id) as TextView).text = text
        return this
    }

    /**
     * 设置文本颜色
     */
    fun setTextColor(id: Int, color: Int): XToast {
        (findViewById<View>(id) as TextView).setTextColor(color)
        return this
    }

    /**
     * 设置提示
     */
    fun setHint(viewId: Int, stringId: Int): XToast {
        return setHint(viewId, mContext!!.resources.getString(stringId))
    }

    fun setHint(id: Int, text: CharSequence?): XToast {
        (findViewById<View>(id) as TextView).hint = text
        return this
    }

    /**
     * 设置提示文本颜色
     */
    fun setHintColor(id: Int, color: Int): XToast {
        (findViewById<View>(id) as TextView).setHintTextColor(color)
        return this
    }

    /**
     * 设置背景
     */
    fun setBackground(viewId: Int, drawableId: Int): XToast {
        val drawable: Drawable? = ContextCompat.getDrawable(mContext!!, drawableId)
        return setBackground(viewId, drawable)
    }

    fun setBackground(id: Int, drawable: Drawable?): XToast {
        findViewById<View>(id)?.background = drawable
        return this
    }

    /**
     * 设置图片
     */
    fun setImageDrawable(viewId: Int, drawableId: Int): XToast {
        val drawable: Drawable? = ContextCompat.getDrawable(mContext!!, drawableId)
        return setImageDrawable(viewId, drawable)
    }

    fun setImageDrawable(viewId: Int, drawable: Drawable?): XToast {
        (findViewById<View>(viewId) as ImageView).setImageDrawable(drawable)
        return this
    }

    /**
     * 设置点击事件
     */
    fun setOnClickListener(listener: OnClickListener<View>): XToast {
        return setOnClickListener(mRootView, listener)
    }

    fun setOnClickListener(id: Int, listener: OnClickListener<View>): XToast {
        return setOnClickListener(findViewById<View>(id), listener)
    }

    private fun setOnClickListener(view: View?, listener: OnClickListener<View>): XToast {
        // 当前是否设置了不可触摸，如果是就擦除掉
        if (hasWindowFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)) {
            clearWindowFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
        view?.let { ViewClickWrapper(this, it, listener) }
        return this
    }

    /**
     * 设置触摸事件
     */
    fun setOnTouchListener(listener: OnTouchListener<View>): XToast {
        return setOnTouchListener(mRootView, listener)
    }

    fun setOnTouchListener(id: Int, listener: OnTouchListener<View>): XToast {
        return setOnTouchListener(findViewById<View>(id), listener)
    }

    private fun setOnTouchListener(view: View?, listener: OnTouchListener<View>): XToast {
        // 当前是否设置了不可触摸，如果是就擦除掉
        if (hasWindowFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)) {
            clearWindowFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
        view?.let { ViewTouchWrapper(this, it, listener) }
        return this
    }

    /**
     * 获取 Handler
     */
    fun getHandler(): Handler? {
        return HANDLER
    }

    /**
     * 延迟执行
     */
    fun post(r: Runnable?): Boolean {
        return postDelayed(r, 0)
    }

    /**
     * 延迟一段时间执行
     */
    fun postDelayed(r: Runnable?, delayMillis: Long): Boolean {
        var delayMillis = delayMillis
        if (delayMillis < 0) {
            delayMillis = 0
        }
        return postAtTime(r, SystemClock.uptimeMillis() + delayMillis)
    }

    /**
     * 在指定的时间执行
     */
    fun postAtTime(r: Runnable?, uptimeMillis: Long): Boolean {
        // 发送和这个 WindowManager 相关的消息回调
        return HANDLER.postAtTime(r, this, uptimeMillis)
    }

    /**
     * 移除消息回调
     */
    fun removeCallbacks() {
        HANDLER.removeCallbacksAndMessages(this)
    }
}