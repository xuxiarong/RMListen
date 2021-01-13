package com.rm.baselisten.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.observe
import com.rm.baselisten.model.BaseToastModel
import com.rm.baselisten.util.ToastUtil
import com.rm.baselisten.viewmodel.BaseVMViewModel

/**
 * desc   :
 * date   : 2020/08/31
 * version: 1.0
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseMvFragmentDialog : BaseFragmentDialog() {

    companion object {
        const val VIEW_MODEL_BR_ID = "viewModelBrId"
        const val VIEW_MODEL = "viewModel"
    }

    /**
     * 定义子类的dataBing对象
     */
    var mDataBind: ViewDataBinding? = null
    private var viewModel: BaseVMViewModel? = null

    var initDialog: (() -> Unit) = {}
    var destroyDialog: (() -> Unit) = {}

    val clickMap: HashMap<Int, () -> Unit> = HashMap()

    val disMissIdMap: HashMap<Int, () -> Unit> = HashMap()

    /**
     * 开启子类的LiveData观察者
     */
    fun startObserve(mViewModel: BaseVMViewModel) {
        context?.let { context ->
            mViewModel.baseToastModel.observe(this) {
                if (it.isNetError) {
                    ToastUtil.showTopNetErrorToast(context, this)
                    return@observe
                }
                if (it.contentId > 0) {
                    ToastUtil.showTopToast(
                        context,
                        getString(it.contentId),
                        it.colorId,
                        it.canAutoCancel,
                        this
                    )
                } else {
                    if (it.content != null) {
                        ToastUtil.showTopToast(
                            context,
                            it.content,
                            it.colorId,
                            it.canAutoCancel,
                            this
                        )
                    } else {
                        Toast.makeText(context, it.content, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            mViewModel.baseCancelToastModel.observe(this) { isCancel ->
                if (dialog?.isShowing == true && isCancel) {
                    ToastUtil.cancelToast()
                }
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val layoutId = arguments?.getInt(LAYOUT_ID, 0) ?: 0
        mDataBind = DataBindingUtil.inflate(inflater, layoutId, container, false)
        val viewModelBrId = arguments?.getInt(VIEW_MODEL_BR_ID, 0) ?: 0
        viewModel = arguments?.getParcelable(VIEW_MODEL)
        if (viewModelBrId > 0 && viewModel != null) {
            mDataBind?.setVariable(viewModelBrId, viewModel)
        }
        disMissIdMap.forEach { entry ->
            mDataBind?.root?.findViewById<View>(entry.key)?.setOnClickListener {
                dismiss()
                entry.value()
            }
        }
        initDialog()
        viewModel?.let {
            it.baseToastModel.value = BaseToastModel()
            startObserve(it)
        }

        return mDataBind?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyDialog()
        mDataBind?.unbind()
        mDataBind = null
        viewModel = null
    }

    fun setClicks(viewId: Int, viewClickAction: () -> Unit) {
        clickMap[viewId] = viewClickAction
    }

    fun setDismissIdAndAction(id: Int, action: () -> Unit) {
        disMissIdMap[id] = action
    }

}