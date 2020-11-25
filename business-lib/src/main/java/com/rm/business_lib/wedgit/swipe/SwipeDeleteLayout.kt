package com.rm.business_lib.wedgit.swipe

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.rm.business_lib.R
import kotlinx.android.synthetic.main.business_swipe_delete.view.*


/**
 * desc   :
 * date   : 2020/10/29
 * version: 1.0
 */
class SwipeDeleteLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr) {

    var startPlay = false
    var action : (()->Unit)? = {}
    init {
        View.inflate(context, R.layout.business_swipe_delete, this)
    }

    fun showLottie(action : (()->Unit)?){
        this.action = action
        updateClick()
    }

    fun reset(){
        startPlay = false
        business_swipe_delete_tv.clearAnimation()
        business_swipe_delete_tv.text = context.getString(R.string.business_delete)
        business_swipe_delete_iv.visibility = View.VISIBLE
        business_swipe_delete_lottie.visibility = View.GONE
    }

    fun updateClick(){
        setOnClickListener {
            if(startPlay){
                action?.let {
                    it()
                }
            }else{
                business_swipe_delete_tv.text = context.getString(R.string.business_delete_enter)
                business_swipe_delete_iv.visibility = View.GONE
                business_swipe_delete_lottie.visibility = View.VISIBLE
                business_swipe_delete_lottie.playAnimation()
                startPlay = true
            }

        }
    }

}