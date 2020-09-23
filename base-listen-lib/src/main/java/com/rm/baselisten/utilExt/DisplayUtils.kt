package com.rm.baselisten.utilExt

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment

/**
 * desc   :
 * date   : 2020/08/20
 * version: 1.0
 */
object DisplayUtils {
    //提供给JAVA代码调用
    fun getDip(context: Context, value: Float): Float {
        return context.dip(value).toFloat()
    }

    fun getSp(context: Context,value: Float):Float{
        return context.sp(value).toFloat()
    }
}

inline fun Context.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()

inline fun Context.dip(value: Float): Int = (value * resources.displayMetrics.density).toInt()

//return sp dimension value in pixels
inline fun Context.sp(value: Int): Int = (value * resources.displayMetrics.scaledDensity).toInt()

inline fun Context.sp(value: Float): Int = (value * resources.displayMetrics.scaledDensity).toInt()

//converts px value into dip or sp
inline fun Context.px2dip(px: Int): Float = (px.toFloat() / resources.displayMetrics.density)

inline fun Context.px2sp(px: Int): Float = (px.toFloat() / resources.displayMetrics.scaledDensity)

inline fun Context.dimen(resource: Int): Int = resources.getDimensionPixelSize(resource)

inline fun Context.sp2px(spValue: Float): Int =
    (spValue * resources.displayMetrics.scaledDensity + 0.5f * (if (spValue >= 0.0f) 1 else -1).toFloat()).toInt()

//the same for the views
inline fun View.dip(value: Int): Int = context.dip(value)

inline fun View.dip(value: Float): Int = context.dip(value)
inline fun View.sp(value: Int): Int = context.sp(value)
inline fun View.sp(value: Float): Int = context.sp(value)
inline fun View.px2dip(px: Int): Float = context.px2dip(px)
inline fun View.px2sp(px: Int): Float = context.px2sp(px)
inline fun View.dimen(resource: Int): Int = context.dimen(resource)

//the same for Fragments
inline fun Fragment.dip(value: Int): Int = activity?.dip(value) ?: 0

inline fun Fragment.dip(value: Float): Int = activity?.dip(value) ?: 0
inline fun Fragment.sp(value: Int): Int = activity?.sp(value) ?: 0
inline fun Fragment.sp(value: Float): Int = activity?.sp(value) ?: 0
inline fun Fragment.px2dip(px: Int): Float = activity?.px2dip(px) ?: 0f
inline fun Fragment.px2sp(px: Int): Float = activity?.px2sp(px) ?: 0f
inline fun Fragment.dimen(resource: Int): Int = activity?.dimen(resource) ?: 0

fun Context.getStateHeight(context: Context): Int {
    val identifier =
        context.applicationContext.resources.getIdentifier("status_bar_height", "dimen", "android")

    return if (identifier > 0) {
        context.applicationContext.resources.getDimensionPixelSize(identifier)
    } else {
        px2dip(50).toInt()
    }
}


/**屏幕的宽高**/
inline val Context.screenWidth: Int
    get() = resources.displayMetrics.widthPixels

inline val Context.screenHeight: Int
    get() = resources.displayMetrics.heightPixels

inline val View.screenWidth: Int
    get() = resources.displayMetrics.widthPixels

inline val View.screenHeight: Int
    get() = resources.displayMetrics.heightPixels

inline val Fragment.screenWidth: Int
    get() = resources.displayMetrics.widthPixels

inline val Fragment.screenHeight: Int
    get() = resources.displayMetrics.heightPixels

inline val Context.density: Float
    get() = resources.displayMetrics.density

