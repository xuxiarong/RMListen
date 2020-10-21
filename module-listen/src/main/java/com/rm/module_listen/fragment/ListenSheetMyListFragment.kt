package com.rm.module_listen.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.lifecycle.observe
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.baselisten.utilExt.dimen
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.activity.ListenMySheetDetailActivity
import com.rm.module_listen.activity.ListenMySheetDetailActivity.Companion.LISTEN_SHEET_DETAIL_DELETE
import com.rm.module_listen.activity.ListenMySheetDetailActivity.Companion.LISTEN_SHEET_DETAIL_EDIT
import com.rm.module_listen.activity.ListenMySheetDetailActivity.Companion.LISTEN_SHEET_DETAIL_REQUEST_CODE
import com.rm.module_listen.activity.ListenMySheetDetailActivity.Companion.SHEET_ID
import com.rm.module_listen.activity.ListenMySheetDetailActivity.Companion.SHEET_NAME
import com.rm.module_listen.bean.ListenSheetBean
import com.rm.module_listen.databinding.ListenFragmentSheetMyListBinding
import com.rm.module_listen.viewmodel.ListenSheetMyListViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.listen_fragment_sheet_my_list.*

class ListenSheetMyListFragment :
    BaseVMFragment<ListenFragmentSheetMyListBinding, ListenSheetMyListViewModel>() {

    companion object {
        fun newInstance(): ListenSheetMyListFragment {
            return ListenSheetMyListFragment()
        }
    }


    override fun initModelBrId() = BR.viewModel

    override fun initLayoutId() = R.layout.listen_fragment_sheet_my_list

    override fun initData() {
        mViewModel.showLoading()
        mViewModel.getData()
    }

    override fun startObserve() {
    }


private fun blur(context: Context, bitmap: Bitmap, radius: Float): Bitmap {
    val output = Bitmap.createBitmap(bitmap) // 创建输出图片
    val rs = RenderScript.create(context)// 构建一个RenderScript对象
    val gaussianBlue = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs)) // 创建高斯模糊脚本
    val allIn = Allocation.createFromBitmap(rs, bitmap) // 创建用于输入的脚本类型
    val allOut = Allocation.createFromBitmap(rs, output) // 创建用于输出的脚本类型
    gaussianBlue.setRadius(radius) // 设置模糊半径，范围0f<radius<=25f
    gaussianBlue.setInput(allIn) // 设置输入脚本类型
    gaussianBlue.forEach(allOut) // 执行高斯模糊算法，并将结果填入输出脚本类型中
    allOut.copyTo(output) // 将输出内存编码为Bitmap，图片大小必须注意
    rs.destroy()// 关闭RenderScript对象，API>=23则使用rs.releaseAllContexts()
    return output

}

    /**
     * activity回调监听
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LISTEN_SHEET_DETAIL_REQUEST_CODE) {
            val sheetId = data?.getStringExtra(SHEET_ID) ?: ""
            when (resultCode) {
                //删除
                LISTEN_SHEET_DETAIL_DELETE -> {
                    mViewModel.remove(sheetId)
                }
                //编辑成功
                LISTEN_SHEET_DETAIL_EDIT -> {
                    val sheetName = data?.getStringExtra(SHEET_NAME) ?: ""
                    mViewModel.changeData(sheetId, sheetName)
                }
            }
        }
    }
}