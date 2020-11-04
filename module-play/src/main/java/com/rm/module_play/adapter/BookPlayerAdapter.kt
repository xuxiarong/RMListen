package com.rm.module_play.adapter

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.adapter.multi.BaseMultiVMAdapter import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.binding.bindHorizontalLayout
import com.rm.module_play.BR
import com.rm.module_play.R
import com.rm.module_play.databinding.RecyItemRecommendedBookPlayBinding
import com.rm.module_play.viewmodel.PlayViewModel

/**
 *
 * @des:
 * @data: 9/9/20 3:06 PM
 * @Version: 1.0.0
 */
class BookPlayerAdapter(
    private val playViewModel: PlayViewModel,
    modelBrId: Int,
    itemBrId: Int
) : BaseMultiVMAdapter<MultiItemEntity>(playViewModel, modelBrId, itemBrId) {
    companion object {
        //播放页面
        val ITEM_TYPE_PLAYER = R.layout.recy_item_book_player_control

        //主播订阅
        val ITEM_TYPE_ANCHOR = R.layout.recy_item_book_player_sub

        //精品订阅
        val ITEM_TYPE_BOUTIQUE = R.layout.recy_item_recommended_book_play

        //热门主播推荐
        val ITEM_TYPE_FOLLOW = R.layout.recy_item_book_player_hot_sub

        //评论标题
        val ITEM_TYPE_COMMENT_TITLE = R.layout.recy_item_comment_more_book_play

        //评论列表
        val ITEM_TYPE_COMMENT_LIST = R.layout.recy_item_book_player_comment
    }

    override fun convert(holder: BaseViewHolder, item: MultiItemEntity) {
        super.convert(holder, item)
        when (item.itemType) {
            ITEM_TYPE_BOUTIQUE -> {
                val recommendedBookPlayBinding = DataBindingUtil.getBinding<RecyItemRecommendedBookPlayBinding>(holder.itemView)
                playViewModel.playControlRecommentListModel.value?.let {
                    recommendedBookPlayBinding?.recyItemBookPlayRecommended?.bindHorizontalLayout(CommonBindVMAdapter(playViewModel, it, R.layout.recy_item_book_player_recomment, BR.viewModel, BR.itemModel))
                }

            }
            else -> {

            }
        }
    }


}
