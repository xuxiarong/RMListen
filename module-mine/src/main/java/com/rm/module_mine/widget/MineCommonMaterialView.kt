package com.rm.module_mine.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginStart
import androidx.databinding.BindingAdapter
import com.rm.baselisten.binding.bindUrl
import com.rm.baselisten.util.DLog
import com.rm.baselisten.utilExt.Color
import com.rm.module_mine.R

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description
 *
 */
class MineCommonMaterialView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    RelativeLayout(context, attrs, defStyleAttr) {
    private lateinit var materialName: AppCompatTextView
    private lateinit var materialIcon: AppCompatImageView
    private lateinit var userIcon: AppCompatImageView
    private lateinit var userText: AppCompatTextView

    private var materialNameStr: String? = ""
    private var materialIconRes: Int = 0
    private var needLine = true
    private val paint = Paint()


    init {
        paint.strokeWidth = 1f
        paint.isAntiAlias = true
        paint.color = Color(R.color.business_text_color_666666)

        val ta =
            context.obtainStyledAttributes(attrs, R.styleable.MineCommonMaterialView)
        materialNameStr = ta.getString(R.styleable.MineCommonMaterialView_material_name)
        materialIconRes = ta.getResourceId(
            R.styleable.MineCommonMaterialView_material_icon,
            R.drawable.icon_arrow_right
        )
        needLine = ta.getBoolean(R.styleable.MineCommonMaterialView_need_line, true)
        ta.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        LayoutInflater.from(context).inflate(R.layout.mine_widget_layout_meateria, this)
        materialName = findViewById(R.id.mine_widget_common_material_name)
        materialIcon = findViewById(R.id.mine_widget_common_material_icon)
        userIcon = findViewById(R.id.mine_widget_common_material_user_icon)
        userText = findViewById(R.id.mine_widget_common_material_user_text)

        materialName.text = materialNameStr
        materialIcon.setImageResource(materialIconRes)

        setBackgroundColor(Color.WHITE)
    }

    /**
     * 设置名称
     */
    fun setMaterialName(name: String) {
        this.materialNameStr = name
        materialName.text = materialNameStr
    }

    /**
     * 设置图标
     */
    fun setMaterialIconRes(@DrawableRes resId: Int) {
        this.materialIconRes = resId
        materialIcon.setImageResource(resId)

    }

    /**
     * 是否需要下面的分割线
     */
    fun setNeedLine(needLine: Boolean) {
        this.needLine = needLine
    }

    /**
     * 设置右边的用户文本信息
     */
    fun setMaterialUserIcon(imgUrl: String?) {
        userIcon.visibility = View.VISIBLE
        userIcon.bindUrl(0f, imgUrl, true)
    }

    /**
     * 设置右边用户的头像
     */
    fun setMaterialUserText(text: String) {
        userText.visibility = View.VISIBLE
        userText.text = text
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (needLine) {
            canvas?.drawLine(0f, height.toFloat(), width.toFloat(), height.toFloat(), paint)
        }
    }
}

@BindingAdapter("userText")
fun MineCommonMaterialView.userText(userText: String) {
    if (!TextUtils.isEmpty(userText)) {
        setMaterialUserText(userText)
    }
}

@BindingAdapter("userIcon")
fun MineCommonMaterialView.userIcon(userIconUrl: String?) {

    if (!TextUtils.isEmpty(userIconUrl)) {
        setMaterialUserIcon(userIconUrl)
    }
}
