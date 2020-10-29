package debug

import android.content.Intent
import com.rm.baselisten.debug.BaseDebugActivity
import com.rm.baselisten.utilExt.Color
import com.rm.business_lib.wedgit.ShadowDrawableUtil
import com.rm.module_home.R
import com.rm.module_home.activity.detail.HomeDetailActivity
import kotlinx.android.synthetic.main.home_activity_main.*


/**
 * desc   :
 * date   : 2020/08/13
 * version: 1.0
 */
class HomeMainDebugActivity : BaseDebugActivity() {
    override fun getLayoutResId(): Int = R.layout.home_activity_main

    override fun initView() {
        ShadowDrawableUtil.setShadowDrawable(
            layout,
            ShadowDrawableUtil.SHAPE_ROUND_PART,
            ShadowDrawableUtil.TypeEnum.TOP,
            Color(R.color.business_text_color_ffffff),
            24,
            Color(R.color.business_text_color_999999),
            10
        )

        btnDetail.setOnClickListener {
            startActivity(Intent(this, HomeDetailActivity::class.java))
        }
    }

    override fun initData() {
    }
}