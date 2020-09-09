package com.rm.module_play.adapter

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.adapter.multi.BaseMultiVMAdapter
import com.rm.module_play.R
import com.rm.module_play.viewmodel.PlayViewModel

/**
 *
 * @des:
 * @data: 9/9/20 3:06 PM
 * @Version: 1.0.0
 */
internal class BookPlayerAdapter(
    homeViewModel: PlayViewModel,
    modelBrId: Int,
    itemBrId: Int
) : BaseMultiVMAdapter<MultiItemEntity>(homeViewModel, modelBrId, itemBrId) {
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
        val ITEM_TYPE_COMMENR_TITLE = R.layout.recy_item_comment_more_book_play
        //评论列表
        val ITEM_TYPE_COMMENR_LIST = R.layout.recy_item_book_player_comment
    }



}
