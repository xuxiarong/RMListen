package com.rm.baselisten.mvvm

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.annotation.NonNull
import androidx.core.view.contains
import androidx.fragment.app.FragmentActivity
import com.gyf.barlibrary.ImmersionBar
import com.rm.baselisten.R
import com.rm.baselisten.thridlib.statusbarlib.ImmersionBarHelper
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.dip

/**
 * desc   : 基类的MvvmActivity
 * date   : 2020/08/05
 * version: 1.0
 */
abstract class BaseActivity : FragmentActivity() {

    private val immersionBarHelper: ImmersionBarHelper by lazy {
        ImmersionBarHelper.create(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //不是dataBind的模式，则setContentView
        if (!isDataBind()) {
            setContentView(getLayoutId())
            //初始化子类的View
            initView()
            //初始化子类的数据
            initData()
        }
        setStatusBar(R.color.base_activity_bg_color)
        DLog.d(javaClass.simpleName, " --- onCreate")

    }

    protected open fun isDataBind(): Boolean {
        return false
    }

    protected open fun initView() {

    }

    protected abstract fun initData()

    /**
     * 获取子类布局的ID
     * @return Int 子类布局的ID
     */
    protected abstract fun getLayoutId(): Int

    protected open fun setStatusBar(color: String?) {
        immersionBarHelper.init(color)
    }

    open fun setStatusBar(@ColorRes colorId: Int) {
        immersionBarHelper.init(colorId)
    }

    protected open fun setStatusBar(
        @ColorRes colorId: Int,
        darkText: Boolean
    ) {
        immersionBarHelper.init(colorId, darkText)
    }

     fun navigationBarHeight() : Int = ImmersionBar.getNavigationBarHeight(this)

    /**
     * 设置透明沉浸式
     */
    protected open fun setD() {
        immersionBarHelper.defaultInit()
    }

    /**
     * 设置透明沉浸式
     */
    protected open fun setTransparentStatusBar() {
        immersionBarHelper.defaultInit()
    }

    protected open fun setTransparentStatusBarWhiteFont() {
        immersionBarHelper.defaultInitWhiteFont()
    }

    /**
     * 获取跟布局
     */
    protected open fun rootViewAddView(view: View) {
        val rootView: FrameLayout = findViewById(android.R.id.content)
        if ((view.parent as? FrameLayout)?.contains(view) == true) {
            (view.parent as FrameLayout).removeView(view)
        }
        rootView.addView(view, layoutParams)
    }

    protected open val layoutParams by lazy {
        FrameLayout.LayoutParams(dip(40), dip(40)).apply {
            gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
            setMargins(0, 0, 0, dip(25))
        }
    }
}