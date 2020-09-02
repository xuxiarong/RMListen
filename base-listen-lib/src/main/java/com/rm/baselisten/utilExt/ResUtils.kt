package com.rm.baselisten.utilExt

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 *
 * @des:
 * @data: 9/2/20 2:53 PM
 * @Version: 1.0.0
 */

inline fun Context.Color(@ColorRes resColor: Int): Int = ContextCompat.getColor(this, resColor)

inline fun View.Color(@ColorRes resColor: Int): Int = ContextCompat.getColor(context, resColor)

inline fun FragmentActivity.Color(@ColorRes resColor: Int): Int =
    ContextCompat.getColor(this, resColor)

inline fun Fragment.Color(@ColorRes resColor: Int): Int? =
    activity?.let { ContextCompat.getColor(it, resColor) }

inline fun View.String(@StringRes resStr: Int): String = resources.getString(resStr)

inline fun Context.String(@StringRes resStr: Int): String = resources.getString(resStr)

inline fun FragmentActivity.String(@StringRes resStr: Int): String = resources.getString(resStr)

inline fun Fragment.String(resStr: Int): String = resources.getString(resStr)

inline fun Context.Drawable(@DrawableRes res: Int): Drawable? = ContextCompat.getDrawable(this, res)

inline fun View.Drawable(@DrawableRes res: Int): Drawable? = ContextCompat.getDrawable(context, res)

inline fun FragmentActivity.Drawable(@DrawableRes res: Int): Drawable? =
    ContextCompat.getDrawable(this, res)

inline fun Fragment.Drawable(@DrawableRes res: Int): Drawable? =
    activity?.let { ContextCompat.getDrawable(it, res) }