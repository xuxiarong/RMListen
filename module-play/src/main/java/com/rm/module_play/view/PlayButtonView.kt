package com.rm.module_play.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.IntDef
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.rm.module_play.R
import java.util.*

/**
 * 播放按钮
 */
class PlayButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {
    private var mProgressTrackPaint: Paint? = null
    private var mStrokeSize = 0f
    private var mProgressTrackStrokeSize = 0f

    //region Settable Properties
    // Color of the progress bar
    @ColorInt
    private var mPlayButtonTint = 0

    @ColorInt
    private var mPauseButtonTint = 0

    @ColorInt
    private var mProgressTrackColor = 0

    // Width of the progress bar
    private val mProgressWidthDP = 0f

    //endregion
    //region Button States
    // Button State
    @get:PlayButtonState
    @PlayButtonState
    var buttonState = 0
        private set
    private var mShowProgressTrack = false

    // ProgressViewRect
    private var mProgressRect: RectF? = null
    private var mButtonStateRect: Rect? = null

    // Player Button Drawables
    private var mPlayDrawable: Drawable? = null
    private var mPauseDrawable: Drawable? = null
    private var mPlayButtonAlpha = 0
    private var mPauseButtonAlpha = 0// value between 0...255 = 0
    private var mIsButtonAnimating = false

    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @IntDef(
        STATE_PLAY,
        STATE_PAUSE
    )
    internal annotation class PlayButtonState {}


    init {
        val typedArray =
            context.theme.obtainStyledAttributes(attrs, R.styleable.PlayButtonView, 0, 0)
        try {
            mPlayButtonTint = typedArray.getColor(
                R.styleable.PlayButtonView_playButtonTint,
                ContextCompat.getColor(context, android.R.color.black)
            )
            mPauseButtonTint = typedArray.getColor(
                R.styleable.PlayButtonView_pauseButtonTint,
                ContextCompat.getColor(context, android.R.color.black)
            )
            buttonState = typedArray.getInt(
                R.styleable.PlayButtonView_buttonState,
                STATE_PLAY
            )
            mShowProgressTrack =
                typedArray.getBoolean(R.styleable.PlayButtonView_showProgressTrack, false)
            mProgressTrackColor = typedArray.getColor(
                R.styleable.PlayButtonView_progressTrackColor,
                ContextCompat.getColor(context, android.R.color.black)
            )
        } finally {
            typedArray.recycle()
        }
        init()
    }

    /**
     * View Initializer
     */
    private fun init() {
        // Progress Paint
        val density = resources.displayMetrics.density
        mStrokeSize = mProgressWidthDP * density
        mProgressTrackStrokeSize = Math.max(
            density.toDouble(),
            mProgressWidthDP * 0.5 * density
        ).toFloat()
        mProgressTrackPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mProgressTrackPaint?.color = mProgressTrackColor
        mProgressTrackPaint?.style = Paint.Style.FILL
        mProgressTrackPaint?.strokeWidth = mProgressTrackStrokeSize
        mProgressRect = RectF()
        mButtonStateRect = Rect()
        mPlayDrawable = Objects.requireNonNull(
            ContextCompat.getDrawable(
                context,
                R.drawable.ic_play_arrow
            )
        )?.let {
            DrawableCompat.wrap(
                it
            )
        }
        mPlayDrawable?.let { DrawableCompat.setTint(it, mPlayButtonTint) }
        mPauseDrawable = Objects.requireNonNull(
            ContextCompat.getDrawable(
                context,
                R.drawable.ic_pause
            )
        )?.let {
            DrawableCompat.wrap(
                it
            )
        }
        mPauseDrawable?.let { DrawableCompat.setTint(it, mPauseButtonTint) }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val targetWidthHeight = Math.min(width, height)
        val mProgressViewSize = targetWidthHeight - mStrokeSize
        mProgressRect?.set(mStrokeSize, mStrokeSize, mProgressViewSize, mProgressViewSize)
        val roundOut = mProgressRect?.roundOut(mButtonStateRect)
        mButtonStateRect?.let {
            mPlayDrawable?.bounds = it
            mPauseDrawable?.bounds = it
        }

        setMeasuredDimension(targetWidthHeight, targetWidthHeight)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        if (mShowProgressTrack) {
            mProgressTrackPaint?.let { mProgressRect?.let { it1 -> canvas.drawOval(it1, it) } }
        }
        // if not animating draw only one state otherwise draw both buttons
        if (!mIsButtonAnimating) {
            if (buttonState == STATE_PLAY) {
                mPlayDrawable?.alpha = ALPHA_VISIBLE
                mPlayDrawable?.draw(canvas)
            } else {
                mPauseDrawable?.alpha = ALPHA_VISIBLE
                mPauseDrawable?.draw(canvas)
            }
        } else {
            mPlayDrawable?.alpha = mPlayButtonAlpha
            mPauseDrawable?.alpha = mPauseButtonAlpha
            mPlayDrawable?.draw(canvas)
            mPauseDrawable?.draw(canvas)
        }
        canvas.save()
    }

    fun setButtonState(@PlayButtonState state: Int, animate: Boolean) {
        if (mIsButtonAnimating) return
        if (animate) {
            mIsButtonAnimating = true
            val appearingButton: String
            val disAppearingButton: String
            if (state == STATE_PLAY) {
                appearingButton = "PlayButtonAlpha"
                disAppearingButton = "PauseButtonAlpha"
            } else {
                appearingButton = "PauseButtonAlpha"
                disAppearingButton = "PlayButtonAlpha"
            }
            val appearAnimation = ObjectAnimator.ofInt(
                this,
                appearingButton,
                ALPHA_INVISIBLE,
                ALPHA_VISIBLE
            )
                .setDuration(DEFAULT_FAST_ANIMATION_DURATION.toLong())
            val disAppearAnimation = ObjectAnimator.ofInt(
                this,
                disAppearingButton,
                ALPHA_VISIBLE,
                ALPHA_INVISIBLE
            )
                .setDuration(DEFAULT_FAST_ANIMATION_DURATION.toLong())
            val animatorSet = AnimatorSet()
            animatorSet.play(appearAnimation)
                .with(disAppearAnimation)
            animatorSet.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationCancel(animation: Animator) {
                    mIsButtonAnimating = false
                    setButtonStateInternal(state)
                }

                override fun onAnimationEnd(animation: Animator) {
                    mIsButtonAnimating = false
                    setButtonStateInternal(state)
                }
            })
            animatorSet.start()
        } else {
            setButtonStateInternal(state)
        }
    }

    @Synchronized
    private fun setPlayButtonAlpha(alpha: Int) {
        mPlayButtonAlpha = alpha
        invalidate()
    }

    @Synchronized
    private fun setPauseButtonAlpha(alpha: Int) {
        mPauseButtonAlpha = alpha
        invalidate()
    }

    private fun setButtonStateInternal(@PlayButtonState state: Int) {
        buttonState = state
        invalidate()
    }

    //endregion
    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val savedState =
            SavedState(superState)
        savedState.buttonState = buttonState
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        val savedState =
            state
        buttonState = savedState.buttonState
        super.onRestoreInstanceState(savedState.superState)
    }

    class SavedState : BaseSavedState, Parcelable {
        @PlayButtonState
        var buttonState = 0

        constructor(superState: Parcelable?) : super(superState) {}
        protected constructor(`in`: Parcel) : super(`in`) {
            buttonState = `in`.readInt()
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeInt(buttonState)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object {
            val CREATOR: Parcelable.Creator<SavedState> =
                object : Parcelable.Creator<SavedState> {
                    override fun createFromParcel(`in`: Parcel): SavedState? {
                        return SavedState(`in`)
                    }

                    override fun newArray(size: Int): Array<SavedState?> {
                        return arrayOfNulls(size)
                    }
                }
        }
    }

    companion object {
        private const val DEFAULT_FAST_ANIMATION_DURATION = 200
        private const val ALPHA_INVISIBLE = 0
        private const val ALPHA_VISIBLE = 255
        const val STATE_PLAY = 0
        const val STATE_PAUSE = 1
    }
}