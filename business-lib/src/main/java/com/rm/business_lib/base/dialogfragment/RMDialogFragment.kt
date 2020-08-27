package com.rm.business_lib.base.dialogfragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.DialogFragment


/**
 * @des:基类
 * @data: 8/26/20 5:44 PM
 * @Version: 1.0.0
 */
abstract class RMDialogFragment : DialogFragment() {

    override fun dismiss() {
        dismissAllowingStateLoss()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
    }

    @CallSuper
    override fun onDetach() {
        super.onDetach()
    }


}