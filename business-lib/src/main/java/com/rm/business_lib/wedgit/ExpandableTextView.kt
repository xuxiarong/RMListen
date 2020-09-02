package com.rm.business_lib.wedgit

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.rm.baselisten.utilExt.dip
import com.rm.baselisten.utilExt.sp
import com.rm.business_lib.R

class ExpandableTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), View.OnClickListener {

    private var mMaxCount = 60//最大显示字数
    private var mText: String? = null//当前的文本信息
    private var isExpand = true//是否折叠
    private lateinit var mTextView: TextView
    private lateinit var mImageView: ImageView
    private var mTextColor = ContextCompat.getColor(context, R.color.business_text_color_333333)
    private var mTextSize = 0
    private var mExpandIcon = R.drawable.business_icon_unfold

    init {
        val ta =
            context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView)
        mTextColor = ta.getColor(R.styleable.ExpandableTextView_expand_text_color, mTextColor)
        mTextSize =
            ta.getDimensionPixelSize(R.styleable.ExpandableTextView_expand_text_size, sp(16f))
        mExpandIcon = ta.getResourceId(R.styleable.ExpandableTextView_expand_iv_icon, mExpandIcon)
        mMaxCount = ta.getInteger(R.styleable.ExpandableTextView_expand_text_max_line, mMaxCount)
        ta.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        mTextView = TextView(context)
        mImageView = ImageView(context)

        mTextView.setLineSpacing(dip(4).toFloat(),1f)
        addView(mTextView)

        mTextView.setTextColor(mTextColor)
        mTextView.textSize = 16f

        val textHeight = textHeight()
        mImageView.setImageResource(mExpandIcon)
        val layoutParams = LayoutParams(textHeight, textHeight)
        layoutParams.addRule(ALIGN_PARENT_RIGHT)
        layoutParams.addRule(ALIGN_PARENT_BOTTOM)
        addView(mImageView, layoutParams)
        mImageView.setOnClickListener(this)
    }

    //设置文本信息
    fun setText(text: String?) {
        mText = text
        changeState()
    }


    //点击展开/收起  对文本进行处理
    private fun changeState() {
        val str = if (isExpand) {
            mText?.substring(0, mMaxCount)
        } else {
            mText
        }
        mTextView.text = str
        mTextView.post {
            val layout = mTextView.layout
            val lineCount = layout.lineCount//获取当前TextView总行数
            val lineStart = layout.getLineStart(lineCount - 1)
            val lineEnd = layout.getLineEnd(lineCount - 1)
            val substring = mText?.substring(lineStart, lineEnd)//获取最后一行的文本
            val measureTextWidth = mTextView.paint.measureText(substring)//测量最后一行文本所需要的宽度
            val fl = measureTextWidth + mImageView.width
            if (fl >= width) {//判断父容器宽度是否能够显示当前文本+图片，如果不行则对当前的文本追加两个空格达到换行的目的
                val stringBuffer = StringBuffer()
                stringBuffer.append(str)
                stringBuffer.append("\t\t")
                mTextView.text = stringBuffer.toString()
            }
        }
    }

    //测量当前单行文本的高度
    private fun textHeight(): Int {
        val fontMetrics = mTextView.paint.fontMetrics
        return (fontMetrics.bottom - fontMetrics.top).toInt()
    }

    override fun onClick(v: View?) {
        isExpand = !isExpand
        changeState()
        startAnim()
    }

    //开始动画，对ImageView进行旋转操作
    private fun startAnim() {
        val value = if (!isExpand) {
            floatArrayOf(0f, 180f)
        } else {
            floatArrayOf(180f, 0f)
        }
        val anim = ObjectAnimator.ofFloat(mImageView, "rotation", value[0], value[1])
        anim.duration = 300
        anim.start()
    }

}