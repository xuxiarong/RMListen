package com.rm.baselisten.mvvm

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.core.view.contains
import androidx.fragment.app.FragmentActivity
import com.gyf.barlibrary.ImmersionBar
import com.rm.baselisten.R
import com.rm.baselisten.thridlib.statusbarlib.ImmersionBarHelper
import com.rm.baselisten.utilExt.dip
import com.rm.baselisten.view.BaseTipView

/**
 * desc   : 基类的MvvmActivity
 * date   : 2020/08/05
 * version: 1.0
 */
abstract class BaseActivity : FragmentActivity() {

    private val immersionBarHelper: ImmersionBarHelper by lazy {
        ImmersionBarHelper.create(this)
    }

    val tipView : BaseTipView by lazy {
        BaseTipView(this)
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
    //获取状态蓝高度
     fun navigationBarHeight() : Int = ImmersionBar.getNavigationBarHeight(this)

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