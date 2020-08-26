package com.rm.baselisten.thridlib.statusbarlib

import android.content.res.Resources
import android.graphics.Color
import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleObserver
import com.gyf.barlibrary.ImmersionBar
import com.rm.baselisten.thridlib.lifecycle.LifecycleHelper.DestroyListener
import com.rm.baselisten.thridlib.lifecycle.LifecycleHelper.with

/**
 * @Author      ： HeXinGen
 * @Date        ： 2019-11-08
 * @Description :  statusbar库的封装使用
 */
class ImmersionBarHelper(
    private var immersionBar: ImmersionBar,
    private val resources: Resources
) : LifecycleObserver {
    fun statusBar(): ImmersionBar {
        return immersionBar
    }

    /**
     * 设置statusBar颜色，例如白色或者蓝色
     *
     * @param color #ffffff
     * @return
     */
    fun init(color: String?): ImmersionBarHelper {
        init(Color.parseColor(color), true)
        return this
    }

    /**
     * 设置statusBar颜色，例如白色或者蓝色
     * @param colorId R.color
     * @return
     */
    @Suppress("DEPRECATION")
    fun init(@ColorRes colorId: Int): ImmersionBarHelper {
        init(resources.getColor(colorId), true)
        return this
    }

    fun init(color: Int, darkFont: Boolean): ImmersionBarHelper {
        immersionBar.reset()
            .statusBarColorInt(color)
            .statusBarDarkFont(darkFont, 0.2f)
            .autoDarkModeEnable(darkFont) //添加自动切换
            .fitsSystemWindows(true) // 解决布局与状态栏重叠问题
            .init()
        return this
    }

    /**
     * 默认,沉浸式，透明色的statusBar，黑色字体
     *
     * @return
     */
    fun defaultInit(): ImmersionBarHelper {
        immersionBar.reset().barColor(color_transparent)
            .statusBarDarkFont(true, 0.2f)
            .navigationBarColor(color_black)
            .statusBarDarkFont(true).init()
        return this
    }

    /**
     * 沉浸式，透明色的statusBar，白色色字体
     * @return ImmersionBarHelper
     */
    fun defaultInitWhiteFont(): ImmersionBarHelper {
        immersionBar.reset().barColor(color_transparent)
            .statusBarDarkFont(true, 0.2f)
            .navigationBarColor(color_black)
            .statusBarDarkFont(false).init()
        return this
    }

    fun release(): ImmersionBarHelper {
        immersionBar.destroy()
        return this
    }

    companion object {
        // 透明色
        private const val color_transparent = "#00000000"

        // 黑色
        private const val color_black = "#000000"
        private const val color_white = "#ffffff"
        fun create(activity: FragmentActivity): ImmersionBarHelper {
            val helper =
                ImmersionBarHelper(ImmersionBar.with(activity), activity.resources)
            with(activity).addDestroyListener(object : DestroyListener {
                override fun IDestroy() {
                    helper.release()
                }
            })
            return helper
        }

        /**
         * 在Fragment中使用immersionBar，要在其Activity中初始化，且销毁
         * @param fragment
         * @return
         */
        fun create(fragment: Fragment): ImmersionBarHelper {
            val activity = fragment.activity
            val immersionBar = ImmersionBar.with(activity!!)
            val helper =
                ImmersionBarHelper(ImmersionBar.with(fragment), fragment.resources)
            with(fragment).addDestroyListener(object : DestroyListener{
                override fun IDestroy() {
                     helper.release()
                }
            })
            with(activity)
                .addDestroyListener(object : DestroyListener{
                    override fun IDestroy() {
                        immersionBar.destroy()
                    }
                })
            return helper
        }
    }

}