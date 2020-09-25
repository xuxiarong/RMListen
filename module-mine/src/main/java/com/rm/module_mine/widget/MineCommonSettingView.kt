package com.rm.module_mine.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.util.TypedValue.COMPLEX_UNIT_PX
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginStart
import androidx.databinding.BindingAdapter
import com.rm.baselisten.binding.bindUrl
import com.rm.baselisten.binding.isVisible
import com.rm.baselisten.util.DLog
import com.rm.baselisten.utilExt.Color
import com.rm.baselisten.utilExt.dimen
import com.rm.module_mine.R

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description
 *
 */
class MineCommonSettingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    RelativeLayout(context, attrs, defStyleAttr) {
    private lateinit var settingName: AppCompatTextView
    private lateinit var settingTip: AppCompatTextView
    private lateinit var settingSwitch: SwitchCompat
    private lateinit var settingIcon: AppCompatImageView

    private var settingNameStr: String? = ""
    private var settingTipStr: String? = ""
    private var settingChecked = false
    private var settingShowIcon = false
    private var settingShowSwitch = true


    init {
        val ta =
            context.obtainStyledAttributes(attrs, R.styleable.MineCommonSettingView)
        settingNameStr = ta.getString(R.styleable.MineCommonSettingView_setting_name)
        settingTipStr = ta.getString(R.styleable.MineCommonSettingView_setting_tip)
        settingChecked = ta.getBoolean(R.styleable.MineCommonSettingView_setting_checked, false)
        settingShowIcon = ta.getBoolean(R.styleable.MineCommonSettingView_setting_show_icon, settingShowIcon)
        settingShowSwitch = ta.getBoolean(R.styleable.MineCommonSettingView_setting_show_switch, settingShowSwitch)
        ta.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        LayoutInflater.from(context).inflate(R.layout.mine_widget_layout_setting, this)
        settingName = findViewById(R.id.mine_widget_common_setting_name)
        settingTip = findViewById(R.id.mine_widget_common_setting_tips)
        settingSwitch = findViewById(R.id.mine_widget_common_setting_switch)
        settingIcon = findViewById(R.id.mine_widget_common_setting_icon)

        settingName.text = settingNameStr
        settingTip.text = settingTipStr
        settingSwitch.isChecked = settingChecked

        settingIcon.isVisible(settingShowIcon)
        settingSwitch.isVisible(settingShowSwitch)

        setPadding(
            dimen(R.dimen.dp_16),
            0,
            dimen(R.dimen.dp_16),
            0
        )
    }

    fun setChecked(checked: Boolean) {
        this.settingChecked = checked
        settingSwitch.isChecked = settingChecked
    }
}

@BindingAdapter("bindChecked")
fun MineCommonSettingView.bindChecked(checked: Boolean?) {
    setChecked(checked == true)
}
