package com.rm.baselisten.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * desc   :
 * date   : 2020/08/05
 * version: 1.0
 */
abstract class BaseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutResId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initData()
        super.onViewCreated(view, savedInstanceState)
    }

    abstract fun getLayoutResId(): Int

    abstract fun initView()


    abstract fun initData()
}