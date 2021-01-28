package com.rm.module_play.adapter

import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.lottie.LottieAnimationView
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.adapter.multi.BaseMultiVMAdapter
import com.rm.business_lib.bean.BusinessAdModel
import com.rm.business_lib.binding.getPlayCount
import com.rm.module_play.R
import com.rm.module_play.model.Comments
import com.rm.module_play.viewmodel.PlayViewModel

/**
 *
 * @author yuanfang
 * @date 12/7/20
 * @description
 *
 */
class PlayDetailCommentAdapter(
    playViewModel: PlayViewModel,
    modelBrId: Int,
    itemBrId: Int
) : BaseMultiVMAdapter<MultiItemEntity>(playViewModel, modelBrId, itemBrId) {
    private var likeClickListener: OnCommentLikeClickListener? = null

    override fun convert(holder: BaseViewHolder, item: MultiItemEntity) {
        super.convert(holder, item)
        when (holder.itemViewType) {
            R.layout.play_item_comment -> {
                commentConvert(holder, item)
            }
        }
    }

    private fun commentConvert(holder: BaseViewHolder, item: MultiItemEntity) {
        val comment = item as PlayDetailCommentItemEntity
        val lottieView = holder.getView<LottieAnimationView>(R.id.play_item_comment_lottie)
        val textView = holder.getView<AppCompatTextView>(R.id.play_item_comment_likes)
        if (comment.data.is_liked) {
            lottieView.setImageResource(R.drawable.business_like)
        } else {
            lottieView.setImageResource(R.drawable.business_unlike)
        }
        textView.text = getPlayCount(item.data.likes)
        lottieView.setOnClickListener {
            likeClickListener?.onClickLike(lottieView, textView, comment)
        }

    }


    fun setOnCommentLikeClickListener(clickListener: OnCommentLikeClickListener) {
        likeClickListener = clickListener
    }

    interface OnCommentLikeClickListener {
        fun onClickLike(
            lottieView: LottieAnimationView,
            textView: AppCompatTextView,
            bean: PlayDetailCommentItemEntity
        )

    }

    class PlayDetailCommentItemEntity(val data: Comments) : MultiItemEntity {
        override val itemType: Int = R.layout.play_item_comment
    }

    class PlayDetailCommentAdvertiseItemEntity(val data: BusinessAdModel) : MultiItemEntity {
        override val itemType: Int = R.layout.play_item_comment_adver
    }
}