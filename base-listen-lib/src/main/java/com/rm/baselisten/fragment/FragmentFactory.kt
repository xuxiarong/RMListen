package com.rm.baselisten.fragment

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment

/**
 * @Author ：HeXinGen
 * @Date : 2019-11-14
 * @Description : fragment的工具类
 */
@Suppress("UNCHECKED_CAST")
class FragmentFactory private constructor(
    private val mClass: Class<out Fragment>,
    private val args: Bundle?
) {
    private var instance: Fragment? = null
    private var tag: String? = null
    fun setFragment(instance: Fragment?): FragmentFactory {
        this.instance = instance
        return this
    }

    fun <T : Fragment?> getFragment(): T {
        try {
            if (instance == null) {
                val fragment = mClass.newInstance()
                if (args != null) {
                    fragment.arguments = args
                }
                instance = fragment
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return instance as T
    }

    fun setTag(tag: String?): FragmentFactory {
        this.tag = tag
        return this
    }

    fun getTag(): String? {
        if (TextUtils.isEmpty(tag)) {
            tag = mClass.simpleName
        }
        return tag
    }

    companion object {
        @JvmOverloads
        fun create(
            mClass: Class<out Fragment>,
            args: Bundle? = null
        ): FragmentFactory {
            return FragmentFactory(mClass, args)

        }
    }

}