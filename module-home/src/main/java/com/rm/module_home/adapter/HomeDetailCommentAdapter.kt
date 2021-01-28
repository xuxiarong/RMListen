package com.rm.module_home.adapter

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.lottie.LottieAnimationView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.adapter.single.BaseBindVMAdapter
import com.rm.business_lib.binding.getPlayCount
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.model.home.detail.CommentList
import com.rm.module_home.viewmodel.HomeDetailViewModel

/**
 *
 * @author yuanfang
 * @date 1/28/21
 * @description
 *
 */
class HomeDetailCommentAdapter(viewModel: HomeDetailViewModel) :
    BaseBindVMAdapter<CommentList>(
        viewModel,
        mutableListOf(),
        R.layout.home_detail_item_comment,
        BR.commentViewModel,
        BR.commentItem
    ) {
    private var likeClickListener: OnCommentLikeClickListener? = null
    override fun convert(holder: BaseViewHolder, item: CommentList) {
        val lottieAnimationView = holder.getView<LottieAnimationView>(R.id.home_item_comment_lottie)
        if (item.is_liked) {
            lottieAnimationView.setImageResource(R.drawable.business_like)
        } else {
            lottieAnimationView.setImageResource(R.drawable.business_unlike)
        }
        val textView = holder.getView<AppCompatTextView>(R.id.home_item_detail_likes)
        textView.text = getPlayCount(item.likes)

        lottieAnimationView.setOnClickListener {
            likeClickListener?.onClickLike(
                lottieAnimationView,
                textView,
                item
            )
        }

        holder.getView<View>(R.id.detail_comment_author_img).setOnClickListener {
            likeClickListener?.onClickMemberIcon(context, item)
        }
    }

    fun setOnCommentLikeClickListener(clickListener: OnCommentLikeClickListener) {
        likeClickListener = clickListener
    }

    interface OnCommentLikeClickListener {
        fun onClickLike(
            lottieView: LottieAnimationView,
            textView: AppCompatTextView,
            bean: CommentList
        )

        fun onClickMemberIcon(context: Context, bean: CommentList)
    }
}