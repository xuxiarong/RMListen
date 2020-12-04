package com.rm.baselisten.binding

import android.widget.RadioGroup
import androidx.databinding.BindingAdapter

/**
 *
 * @author yuanfang
 * @date 12/4/20
 * @description
 *
 */
@BindingAdapter("bindCheckedChange")
fun RadioGroup.bindCheckedChange(action: ((Int) -> Unit)?) {
    if (action == null) {
        return
    }
    setOnCheckedChangeListener { _, checkedId ->
        action(checkedId)
    }
}