package com.rm.business_lib.wedgit.seekbar

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Environment
import android.util.AttributeSet
import android.view.*
import android.view.WindowManager.*
import android.view.WindowManager.LayoutParams.*
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.Nullable
import com.rm.business_lib.R
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*

/**
 *
 * @des:自定义播放器进度条
 * @data: 8/24/20 7:02 PM
 * @Version: 1.0.0
 */
@Suppress("DEPRECATION")
@SuppressLint("CustomViewStyleable", "Recycle")
class BubbleSeekBar @JvmOverloads constructor(
    context: Context,
    @Nullable attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {
    private var mOnProgressChangedListener: OnProgressChangedListener? = null
    private var mMin //最小
            : Float
    private var mMax //最大
            : Float
    private var mProgress //当前量
            = 0f
    private var mThumb //轨道
            : Drawable?
    private var mThumbHeight //拇指高度
            : Int
    private var mThumbWidth //拇指宽度
            : Int
    private var mThumbTextColor //拇指文字颜色
            : Int
    private var mThumbTextSize //拇指文字大小
            : Int
    private var mThumbText = ""
    private var mTrack //轨道
            : Drawable?
    private var mSecondTrack //二层轨道
            : Drawable?
    private var mTrackHeight //轨道高度
            : Int
    private var mTrackMarginLeft //轨道左边空余
            : Int
    private var mTrackMarginRight //轨道右边空余
            : Int
    private var mThumbOffset //拇指偏移量
            = 0
    private var mTrackCenterY //轨道中心Y坐标
            = 0f
    private var isThumbOnDragging = false
    private var mBubbleOffset: Int
    private var mTrackLength = 0
    private val mPaint: Paint by lazy {
        Paint()
    }
    private val mWindowManager: WindowManager
    private val mBubbleFL: FrameLayout?
    private val mLayoutParams: LayoutParams

    /**
     * 初始化
     */
    private fun init() {
        mPaint.isAntiAlias = true
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.textAlign = Paint.Align.CENTER
        mPaint.color = mThumbTextColor
        mPaint.textSize = mThumbTextSize.toFloat()
        mLayoutParams.format = PixelFormat.TRANSLUCENT
        mLayoutParams.flags = (FLAG_NOT_TOUCH_MODAL
                or FLAG_NOT_FOCUSABLE
                or FLAG_SHOW_WHEN_LOCKED)
        if (BubbleSeekBarUtils.isMIUI || Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            mLayoutParams.type = TYPE_APPLICATION
        } else {
            mLayoutParams.type = TYPE_TOAST
        }

        mWindowManager.addView(mBubbleFL, mLayoutParams)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        var measureWidth = layoutParams.width
        var measureHeight = layoutParams.height
        if (measureHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
            measureHeight = if (mThumbHeight > mTrackHeight) mThumbHeight else mTrackHeight
        }
        if (measureWidth == ViewGroup.LayoutParams.WRAP_CONTENT) {
            measureWidth = mThumbWidth
        }
        measureWidth = resolveSize(measureWidth, widthMeasureSpec)
        measureHeight = resolveSize(measureHeight, heightMeasureSpec)
        setMeasuredDimension(measureWidth, measureHeight)
        mTrackCenterY = (measureHeight shr 1.toFloat().toInt()).toFloat()
        mTrackLength = measureWidth - mThumbWidth
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                performClick()
                isThumbOnDragging = true
                mOnProgressChangedListener?.onStartTrackingTouch(this)
                showBubble()
            }
            MotionEvent.ACTION_MOVE -> {
                mThumbOffset = event.x.toInt() - mThumbWidth / 2
                if (mThumbOffset < 0) mThumbOffset =
                    0 else if (mThumbOffset > mTrackLength) mThumbOffset = mTrackLength
                mProgress =
                    if (mTrackLength != 0) mMin + (mMax - mMin) * mThumbOffset / mTrackLength else mMin
                calculateBubble()
                postInvalidate()
                mOnProgressChangedListener?.onProgressChanged(this, getProgress(), true)

            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mOnProgressChangedListener?.onStopTrackingTouch(this)
                isThumbOnDragging = false
                hideBubble()
            }
        }
        return isThumbOnDragging or super.onTouchEvent(event)
    }

    /**
     * 计算Bubble
     */
    private fun calculateBubble() {
        mLayoutParams.x = mPoint[0] + mThumbOffset - (mBubbleFL?.width ?: 0 - mThumbWidth) / 2
        mLayoutParams.y =
            mPoint[1] - (mBubbleFL?.height ?: 0) - (mThumbHeight shr 1) - mBubbleOffset
        mWindowManager.updateViewLayout(mBubbleFL, mLayoutParams)
    }

    /**
     * 显示弹出框
     */
    private fun showBubble() {
        calculateBubble()
        if (mBubbleFL == null) return
        mBubbleFL.alpha = 0f
        mBubbleFL.animate().alpha(1f).setDuration(200)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    mBubbleFL.visibility = VISIBLE
                }

                override fun onAnimationEnd(animation: Animator) {}
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            }).start()
    }

    /**
     * 隐藏弹出框
     */
    private fun hideBubble() {
        if (mBubbleFL == null) return
        mBubbleFL.alpha = 1f
        mBubbleFL.animate().alpha(0f).setDuration(200)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    mBubbleFL.visibility = GONE
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            }).start()
    }

    /**
     * 增加自定义气泡弹出框
     *
     * @param view
     */
    fun addBubbleFL(view: View?) {
        view?.let {
            view.parent?.let {
                (it as? FrameLayout)?.let {
                    it.removeView(view)
                }
            }
        }
        mBubbleFL?.addView(view)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
//        mWindowManager.removeView(mBubbleFL)
    }

    /**
     * 设置最大值
     *
     * @param max
     */
    fun setMax(max: Float) {
        mMax = max
    }

    /**
     * 获取最大值
     *
     * @return
     */
    fun getMax(): Float {
        return mMax
    }

    /**
     * 设置最小值
     *
     * @param min
     */
    fun setMin(min: Float) {
        mMin = min
    }

    /**
     * 获取最小值
     *
     * @return
     */
    fun getMin(): Float {
        return mMin
    }

    /**
     * Thumb显示的文字
     *
     * @param thumbText
     */
    fun updateThumbText(thumbText: String) {
        mThumbText = thumbText
        invalidate()
    }

    /**
     *
     * 设置Thumb的高度
     *
     * @param height
     */
    fun setThumbHeight(height: Int) {
        mThumbHeight = height
        invalidate()
    }

    /**
     *
     * 设置Thumb的宽度
     *
     * @param width
     */
    fun setThumbWidth(width: Int) {
        mThumbWidth = width
        invalidate()
    }

    /**
     *
     * 设置Thumb文字颜色
     *
     * @param color
     */
    fun setThumbTextColor(@ColorInt color: Int) {
        mThumbTextColor = color
        invalidate()
    }

    /**
     *
     * 设置Thumb文字大小
     *
     * @param textSize
     */
    fun setThumbTextSize(textSize: Int) {
        mThumbTextSize = textSize
    }

    /**
     *
     * 设置Thumb
     *
     * @param thumb
     */
    fun setThumb(thumb: Drawable?) {
        mThumb = thumb
        invalidate()
    }

    /**
     *
     * 设置Track
     *
     * @param track
     */
    fun setTrack(track: Drawable?) {
        mTrack = track
        invalidate()
    }

    /**
     *
     * 设置SecondTrack
     *
     * @param secondTrack
     */
    fun setSecondTrack(secondTrack: Drawable?) {
        mSecondTrack = secondTrack
        invalidate()
    }

    /**
     *
     * 设置track的高度
     *
     * @param barHeight
     */
    fun setBarHeight(barHeight: Int) {
        mTrackHeight = barHeight
        invalidate()
    }

    /**
     *
     * 设置Track左右的空白
     *
     * @param leftMargin
     * @param rightMargin
     */
    fun setTrackMargin(leftMargin: Int, rightMargin: Int) {
        mTrackMarginLeft = leftMargin
        mTrackMarginRight = rightMargin
        invalidate()
    }

    /**
     *
     * Bubble的宽度
     *
     * @param width
     */
    fun setBubbleWidth(width: Int) {
        mLayoutParams.width = width
    }

    /**
     *
     * Bubble的高度
     *
     * @param height
     */
    fun setBubbleHeight(height: Int) {
        mLayoutParams.height = height
    }

    /**
     *
     * Bubble向上的偏移量
     *
     * @param bubbleOffset
     */
    fun setBubbleOffset(bubbleOffset: Int) {
        mBubbleOffset = bubbleOffset
    }

    /**
     * 获取当前位置
     *
     * @return
     */
    private fun getProgress(): Float {
        return mProgress
    }

    /**
     * 设置当前位置
     *
     * @param progress
     */
    fun setProgress(progress: Float) {
        mProgress = progress
        mOnProgressChangedListener?.onProgressChanged(this, mProgress, false)
        postInvalidate()
    }
    fun setNoListenerProgress(progress: Float) {
        mProgress = progress
        postInvalidate()
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mThumbOffset = (mTrackLength * (mProgress - mMin) / (mMax - mMin)).toInt()
        drawTrack(canvas)
        drawThumb(canvas)
    }

    /**
     * 绘制拇指
     *
     * @param canvas
     */
    private fun drawThumb(canvas: Canvas) {
        val save = canvas.save()
        if (mThumb != null) {
            canvas.translate(mThumbOffset.toFloat(), mTrackCenterY - mThumbHeight / 2.0f)
            mThumb?.setBounds(
                0,
                0,
                Math.round(mThumbWidth.toFloat()),
                Math.round(mThumbHeight.toFloat())
            )
            mThumb?.draw(canvas)
            /**
             * 绘制文字
             */
            val rect = Rect()
            mPaint.getTextBounds(mThumbText, 0, mThumbText.length, rect)
            val x = (mThumbWidth - rect.width()) / 2.0f + rect.width() / 2.0f
            val y = (mThumbHeight - rect.height()) / 2.0f + rect.height()
            canvas.drawText(mThumbText, x, y, mPaint)
        }
        canvas.restoreToCount(save)
    }

    /**
     * 绘制轨道
     *
     * @param canvas
     */
    private fun drawTrack(canvas: Canvas) {
        val save = canvas.save()
        canvas.translate(0f, mTrackCenterY - mTrackHeight / 2.0f)
        if (mTrack != null) {
            mTrack?.setBounds(
                mTrackMarginLeft,
                0,
                measuredWidth - mTrackMarginLeft - mTrackMarginRight,
                mTrackHeight
            )
            mTrack?.draw(canvas)
        }
        if (mSecondTrack != null) {
            mSecondTrack?.setBounds(
                mTrackMarginLeft,
                0,
                mThumbOffset + mThumbWidth / 2 - mTrackMarginLeft - mTrackMarginRight,
                mTrackHeight
            )
            mSecondTrack?.draw(canvas)
        }
        canvas.restoreToCount(save)
    }

    private val mPoint = IntArray(2)
    override fun onLayout(
        changed: Boolean,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        super.onLayout(changed, left, top, right, bottom)
        getLocationOnScreen(mPoint)
    }

    /**
     * 设置监听
     *
     * @param progressChangedListener
     */
    fun setOnProgressChangedListener(progressChangedListener: OnProgressChangedListener?) {
        mOnProgressChangedListener = progressChangedListener
    }

    /**
     * 监听进度
     */
    interface OnProgressChangedListener {
        fun onProgressChanged(
            bubbleSeekBar: BubbleSeekBar?,
            progress: Float,
            fromUser: Boolean
        )

        fun onStartTrackingTouch(bubbleSeekBar: BubbleSeekBar?)
        fun onStopTrackingTouch(bubbleSeekBar: BubbleSeekBar?)
    }

    /**
     *
     *
     */
    object BubbleSeekBarUtils {
        private val BUILD_PROP_FILE =
            File(Environment.getRootDirectory(), "build.prop")
        private var sBuildProperties: Properties? = null
        private val sBuildPropertiesLock = Any()
        private val buildProperties: Properties?
            get() {
                synchronized(sBuildPropertiesLock) {
                    if (sBuildProperties == null) {
                        sBuildProperties = Properties()
                        var fis: FileInputStream? = null
                        try {
                            fis = FileInputStream(BUILD_PROP_FILE)
                            sBuildProperties?.load(fis)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        } finally {
                            fis?.let {
                                try {
                                    it.close()
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                }
                return sBuildProperties
            }

        /**
         * 判断是否是MIUI
         *
         * @return
         */
        val isMIUI: Boolean
            get() = buildProperties?.containsKey("ro.miui.ui.version.name") ?: false
    }

    init {
        val a =
            context.obtainStyledAttributes(attrs, R.styleable.DaXuBubbleSeekBar, defStyleAttr, 0)
        mMin = a.getFloat(R.styleable.DaXuBubbleSeekBar_min, 0.0f)
        mMax = a.getFloat(R.styleable.DaXuBubbleSeekBar_max, 100.0f)
        mThumbHeight = a.getDimensionPixelSize(R.styleable.DaXuBubbleSeekBar_thumbHeight, 60)
        mThumbWidth = a.getDimensionPixelSize(R.styleable.DaXuBubbleSeekBar_thumbWidth, 100)
        mTrackMarginLeft = a.getDimensionPixelSize(R.styleable.DaXuBubbleSeekBar_trackMarginLeft, 0)
        mTrackMarginRight =
            a.getDimensionPixelSize(R.styleable.DaXuBubbleSeekBar_trackMarginRight, 0)
        mThumbTextColor =
            a.getColor(R.styleable.DaXuBubbleSeekBar_thumbTextColor, Color.BLACK)
        mThumbTextSize = a.getDimensionPixelSize(R.styleable.DaXuBubbleSeekBar_thumbTextSize, 14)
        mTrackHeight = a.getDimensionPixelSize(R.styleable.DaXuBubbleSeekBar_barHeight, 10)
        mTrack = a.getDrawable(R.styleable.DaXuBubbleSeekBar_track)
        mSecondTrack = a.getDrawable(R.styleable.DaXuBubbleSeekBar_secondTrack)
        mBubbleOffset = a.getDimensionPixelOffset(R.styleable.DaXuBubbleSeekBar_bubbleOffset, 10)
        mThumb = a.getDrawable(R.styleable.DaXuBubbleSeekBar_thumb)
        mBubbleFL = FrameLayout(getContext())
        mBubbleFL.visibility = GONE
        mBubbleFL.setBackgroundDrawable(a.getDrawable(R.styleable.DaXuBubbleSeekBar_bubbleBackgroud))
        mWindowManager = getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mLayoutParams = LayoutParams()
        mLayoutParams.x = 0
        mLayoutParams.y = 0
        mLayoutParams.gravity = Gravity.START or Gravity.TOP
        mLayoutParams.width =
            a.getDimensionPixelSize(R.styleable.DaXuBubbleSeekBar_bubbleWidth, 150)
        mLayoutParams.height =
            a.getDimensionPixelSize(R.styleable.DaXuBubbleSeekBar_bubbleHeight, 20)
        init()
    }
}
