package com.rm.baselisten.load

import android.R
import android.app.Activity
import android.content.Context
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import com.rm.baselisten.load.core.TargetContext

/**
 * desc   :
 * date   : 2020/08/10
 * version: 1.0
 */
object LoadSirUtil {
    @JvmStatic
    fun getTargetContext(target: Any): TargetContext {
        val contentParent: ViewGroup
        val context: Context
        if (target is Activity) {
            val activity = target
            context = activity
            contentParent =
                activity.findViewById<View>(R.id.content) as ViewGroup
        } else if (target is View) {
            val view = target
            contentParent = view.parent as ViewGroup
            context = view.context
        } else {
            throw IllegalArgumentException("The target must be within Activity, Fragment, View.")
        }
        var childIndex = 0
        val childCount = contentParent?.childCount ?: 0
        val oldContent: View?
        if (target is View) {
            oldContent = target
            for (i in 0 until childCount) {
                if (contentParent.getChildAt(i) === oldContent) {
                    childIndex = i
                    break
                }
            }
        } else {
            oldContent = contentParent?.getChildAt(0)
        }
        requireNotNull(oldContent) {
            String.format(
                "enexpected error when register LoadSir in %s", target
                    .javaClass.simpleName
            )
        }
        contentParent?.removeView(oldContent)
        return TargetContext(
            context,
            contentParent,
            oldContent,
            childIndex
        )
    }

    val isMainThread: Boolean
        get() = Looper.myLooper() == Looper.getMainLooper()
}