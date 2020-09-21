package com.rm.module_home.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.module_home.R
import com.rm.module_home.bean.CategoryTabBean

class HomeTopListTabAdapter :
    BaseQuickAdapter<CategoryTabBean, BaseViewHolder>(layoutResId = R.layout.home_adapter_top_list_tab) {
    private var mPosition = -1//当前选中的下表标
    private var mCurTv: TextView? = null//当前选中的textview

    override fun convert(holder: BaseViewHolder, item: CategoryTabBean) {
        holder.setText(R.id.home_top_list_tab_adapter_tv, item.class_name)
        val tv = holder.getView<TextView>(R.id.home_top_list_tab_adapter_tv)
        if (mPosition == -1 && holder.adapterPosition == 0) {
            setSelectTv(true, tv)
        } else {
            setSelectTv(false, tv)
        }
    }

    //设置选中的textview
    fun setSelect(position: Int, view: TextView?) {
        setSelectTv(false, mCurTv)
        setSelectTv(true, view)
        mPosition = position
    }

    //修改textview样式
    private fun setSelectTv(select: Boolean, textView: TextView?) {
        if (select) {
            textView?.apply {
                mCurTv = this
                setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.business_shape_circle_ff5e5e,
                    0,
                    0,
                    0
                )
                setBackgroundResource(R.drawable.home_top_list_tab_bg)
                elevation = resources.getDimension(R.dimen.dp_2)
            }
        } else {
            textView?.apply {
                setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.business_shape_circle_transparent,
                    0,
                    0,
                    0
                )
                elevation = 0f
            }
        }
    }
}