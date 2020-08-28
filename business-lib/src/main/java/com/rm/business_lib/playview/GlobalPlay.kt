package com.rm.business_lib.playview

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.rm.baselisten.util.DisplayUtils.getDip
import com.rm.business_lib.R


class GlobalPlay @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null
) : View(context, attrs), AnimatorUpdateListener {
    private var mPaint: Paint? = null
    private var mBpPaint: Paint? = null
    private var mWidth = 0f
    private var mHeight = 0f
    private val mUnreachColor = -0x7759595a
    private val mReachedColor = -0x8fb0
    private var mRadius = 0f
    private var mBarWidth = 0f
    private var mBitmap: Bitmap? = null
    private var mPlayPath: Path? = null

    /**
     * 0.0~1.0
     */
    private var mProgress = 0f

    /**
     * 矩形范围
     */
    private var mRectF: RectF? = null
    private var mMatrix: Matrix? = null
    private var mShader: BitmapShader? = null
    private var mDegree = 0
    private var isPlaying = false
    private var mAnimator: ValueAnimator? = null
    private var mPathEffect: CornerPathEffect? = null
    private fun init() {
        mPaint =
            Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        mPaint?.color = Color.BLACK
        mPaint?.style = Paint.Style.STROKE
        setBitmap(BitmapFactory.decodeResource(resources, R.drawable.business_defualt_img))
        mBpPaint =
            Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        mMatrix = Matrix()
        mAnimator = ValueAnimator.ofInt(360)
        mAnimator?.duration = 5000
        mAnimator?.interpolator = LinearInterpolator()
        mAnimator?.repeatMode = ValueAnimator.RESTART
        mAnimator?.repeatCount = ValueAnimator.INFINITE
        mAnimator?.addUpdateListener(this)
        mPathEffect = CornerPathEffect(getDip(context, 2f))
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w.toFloat()
        mHeight = h.toFloat()
    }

    @Synchronized
    override fun onDraw(canvas: Canvas) {
        //0,先将坐标移至中心点
        canvas.translate(mWidth / 2, mHeight / 2)
        canvas.rotate(-90f)
        //1.画未到达进度条弧形
        mPaint?.style = Paint.Style.STROKE
        mPaint?.color = mUnreachColor
        mPaint?.strokeWidth = mBarWidth
        if (mPaint != null && mRectF != null) {
            canvas.drawArc(mRectF!!, mProgress * 360, (1 - mProgress) * 360, false, mPaint!!)
        }
        //2.画到达进度条弧形
        mPaint?.color = mReachedColor
        if (mPaint != null && mRectF != null) {
            canvas.drawArc(mRectF!!, 0f, mProgress * 360, false, mPaint!!)
        }
        canvas.rotate(90f)
        //3.画圆形图片
        mMatrix?.setRotate(
            mDegree.toFloat(),
            (mBitmap?.width ?: 0).toFloat(),
            (mBitmap?.height ?: 0).toFloat()
        )
        mMatrix?.postTranslate(
            (mBitmap?.width ?: 0).toFloat(),
            (mBitmap?.height ?: 0).toFloat()
        )
        val scale = (mRadius - mBarWidth) * 1.0f / (Math.min(
            mBitmap?.width ?: 0,
            mBitmap?.height ?: 0
        ) shr 1)
        mMatrix?.postScale(scale, scale)
        mShader?.setLocalMatrix(mMatrix)
        mBpPaint?.shader = mShader
        mBpPaint?.let { canvas.drawCircle(0f, 0f, mRadius - mBarWidth, it) }
        //4.绘制半透明蒙版
        if (isPlaying) return
        mPaint?.color = -0x77000001
        mPaint?.style = Paint.Style.FILL
        canvas.drawCircle(0f, 0f, mRadius - mBarWidth, mPaint!!)
        mPaint?.color = mReachedColor
        mPaint?.strokeJoin = Paint.Join.ROUND
        mPaint?.pathEffect = mPathEffect
        canvas.translate(mRadius / 2.3f, 0f)
        //5.绘制开始播放按钮
        if (mPlayPath != null && mPaint != null) {
            canvas.drawPath(mPlayPath!!, mPaint!!)

        }
    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
        mDegree = animation.animatedValue as Int
        invalidate()
    }

    fun play(avatarUrl: String?) {
        Glide.with(this).asBitmap()
            .load(avatarUrl)
            .error(R.drawable.business_defualt_img)
            .placeholder(R.drawable.business_defualt_img)
            .into(object : CustomTarget<Bitmap>(mWidth.toInt(), mHeight.toInt()) {
                override fun onLoadCleared(placeholder: Drawable?) {
                }
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    setBitmap(resource)
                    isPlaying = true;
                    if (mAnimator?.isStarted ==true)
                        mAnimator?.resume()
                    else
                        mAnimator?.start()
                }
            })

    }

    fun play(@DrawableRes res: Int) {
        setBitmap(BitmapFactory.decodeResource(resources, res))
        isPlaying = true
        if (mAnimator?.isStarted == true) mAnimator?.resume() else mAnimator?.start()
    }

    fun pause() {
        mAnimator?.pause()
        isPlaying = false
        invalidate()
    }

    fun setProgress(progress: Float) {
        mProgress = progress
        invalidate()
    }

    fun show() {
        animate().translationY(0f).setDuration(300).withStartAction {
            if (isPlaying) {
                if (mAnimator?.isStarted == true) mAnimator?.resume() else mAnimator?.start()
            }
            visibility = VISIBLE
        }
    }

    fun hide() {
        animate().translationY(height.toFloat()).setDuration(100)
            .withEndAction {
                visibility = GONE
                mAnimator?.pause()
            }
    }

    fun setImage(avatarUrl: String?) {
        Glide.with(this).asBitmap()
            .load(avatarUrl)
            .error(R.drawable.business_defualt_img)
            .placeholder(R.drawable.business_defualt_img)
            .into(object : CustomTarget<Bitmap>(mWidth.toInt(), mHeight.toInt()) {
                override fun onLoadCleared(placeholder: Drawable?) {
                }
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    setBitmap(resource);
                    invalidate()
                }
            })
    }

    fun setImage(@DrawableRes res: Int) {
        setBitmap(BitmapFactory.decodeResource(resources, res))
        invalidate()
    }

    fun setBitmap(bitmap: Bitmap?) {
        mBitmap = bitmap
        mShader = mBitmap?.let { BitmapShader(it, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP) }
    }

    fun setRadius(radius: Float) {
        mRadius = radius
        mRectF = RectF(-mRadius, -mRadius, mRadius, mRadius)
        //考虑线条宽度,向内缩小半个宽度
        mRectF?.inset(mBarWidth / 2, mBarWidth / 2)
        mPlayPath = Path()
        mPlayPath?.moveTo(0f, 0f)
        mPlayPath?.lineTo(
            -mRadius / 1.4f,
            (Math.tan(Math.toRadians(30.0)) * mRadius / 1.4f).toFloat()
        )
        mPlayPath?.lineTo(
            -mRadius / 1.4f,
            (-(Math.tan(Math.toRadians(30.0)) * mRadius / 1.4f)).toFloat()
        )
        mPlayPath?.close()
    }

    fun setBarWidth(barWidth: Float) {
        mBarWidth = barWidth
        setRadius(mRadius)
    }

    companion object {
        private const val TAG = "GlobalPlay"
    }

    init {
        init()
    }
}