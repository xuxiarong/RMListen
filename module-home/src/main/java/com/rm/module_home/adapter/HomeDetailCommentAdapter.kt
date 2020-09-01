package com.rm.module_home.adapter

import androidx.appcompat.widget.AppCompatImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.binding.bindUrl
import com.rm.module_home.R
import com.rm.module_home.model.home.detail.CommentList

class HomeDetailCommentAdapter : BaseQuickAdapter<CommentList,BaseViewHolder>(R.layout.home_detail_item_comment){
    override fun convert(holder: BaseViewHolder, item: CommentList) {

        holder.getView<AppCompatImageView>(R.id.detail_comment_author_img)
            .bindUrl(bindUrl =  item.member.avatar_url ,isCircle = true)

        holder.setText(R.id.detail_comment_name_tx,item.member.nickname)
            .setText(R.id.detail_comment_day_tx,item.created_at)
            ?.setText(R.id.detail_comment_like_count_tx,item.likes)
            ?.setText(R.id.detail_comment_content_tx,item.content)
    }
}