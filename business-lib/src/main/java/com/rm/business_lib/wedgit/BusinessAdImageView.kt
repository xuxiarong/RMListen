package com.rm.business_lib.wedgit

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.rm.business_lib.insertpoint.BusinessInsertConstance
import com.rm.business_lib.insertpoint.BusinessInsertManager

/**
 *
 * @author yuanfang
 * @date 12/12/20
 * @description
 *
 */
class BusinessAdImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    var adId: String = ""
    var adIsShow = true

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (adId.isNotEmpty() && adIsShow) {
            adIsShow = false
            BusinessInsertManager.doInsertKeyAndAd(
                BusinessInsertConstance.INSERT_TYPE_AD_EXPOSURE,
                adId
            )
        }
    }
}

@BindingAdapter("bindAdId")
fun BusinessAdImageView.bindAdId(bindAdId: String?) {
    if (bindAdId != null && !TextUtils.equals(bindAdId, "0")) {
        adId = bindAdId
        adIsShow = true
    }
}

