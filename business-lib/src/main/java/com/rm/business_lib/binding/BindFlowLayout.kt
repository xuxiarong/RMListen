package com.rm.business_lib.binding

import android.view.LayoutInflater
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.nex3z.flowlayout.FlowLayout
import com.rm.business_lib.R
import com.rm.business_lib.bean.DetailTags

/**
 *
 * @author yuanfang
 * @date 12/12/20
 * @description
 *
 */
@BindingAdapter("bindFlowData")
fun FlowLayout.bindFlowData(list: MutableList<DetailTags>?) {
    if (list == null) {
        return
    }
    list.forEach {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.business_tag_book_detail, this, false) as TextView
        view.text = it.tag_name
        this.addView(view)
    }
}

@BindingAdapter("bindSearchFlowData")
fun FlowLayout.bindSearchFlowData(list: MutableList<String>?) {
    if (list == null) {
        return
    }
    list.forEach {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.business_tag_search_history, this, false) as TextView
        view.text = it
        this.addView(view)
    }
}