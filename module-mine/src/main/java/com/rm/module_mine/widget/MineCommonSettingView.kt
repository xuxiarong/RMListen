package com.rm.module_mine.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.BindingAdapter
import com.rm.baselisten.binding.isVisible
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
) : RelativeLayout(context, attrs, defStyleAttr) {
    private lateinit var settingName: AppCompatTextView
    private lateinit var settingTip: AppCompatTextView
    lateinit var settingSwitch: SwitchCompat
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
        settingShowIcon =
            ta.getBoolean(R.styleable.MineCommonSettingView_setting_show_icon, settingShowIcon)
        settingShowSwitch =
            ta.getBoolean(R.styleable.MineCommonSettingView_setting_show_switch, settingShowSwitch)
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

@BindingAdapter("bindMineCheckAction")
fun MineCommonSettingView.bindMineCheckAction(action: ((Boolean) -> Unit)?) {
    settingSwitch.setOnCheckedChangeListener { _, isChecked ->
        action?.let {
            it(isChecked)
        }
    }
}
@BindingAdapter("bindMineChecked")
fun MineCommonSettingView.bindMineChecked(checked: Boolean) {
    settingSwitch.isChecked = checked
}

