package com.rm.module_home.model.home

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.business_lib.bean.BannerInfoBean
import com.rm.module_home.R

/**
 * desc   :
 * date   : 2020/09/17
 * version: 1.0
 */


data class HomeModel(
        val banner_list: MutableList<BannerInfoBean>?,
        val menu_list: ArrayList<HomeMenuModel>?,
        val block_list: List<HomeBlockModel>?,
        val page_id: Int,
        val page_name: String
)

data class HomeBlockModel(
        val audio_list: HomeAudioList,
        val block_id: Int,
        val block_name: String,
        val block_seq: Int,
        val block_type_id: Int,
        val page_id: Int,
        val page_size: Int,
        val topic_id: Int,
        val relation_to: String,
        val single_img_content: HomeSingleImgContentModel
)

data class HomeMenuModel(
        val icon_url: String,
        val menu_icon: String,
        val menu_id: Int,
        val menu_jump: String,
        val menu_name: String,
        val page_id: Int
) : MultiItemEntity {
    override var itemType = R.layout.home_item_menu
}

data class HomeAudioList(
        val list: ArrayList<HomeAudioModel>,
        val total: Int
)

data class HomeAudioModel(
        val anchor_name: String,
        val anchor_id: String,
        val audio_cover: String,
        val audio_id: String,
        val audio_intro: String,
        val audio_label: String,
        var audio_name: String,
        val audio_type: Int,
        val author: String,
        val author_intro: String,
        val chapter_updated_at: String,
        val cover_url: String,
        val created_at: String,
        val original_name: String,
        val play_count: Int,
        val progress: Int,
        val quality: Int,
        val short_intro: String,
        val status: Int
) : MultiItemEntity {
    override var itemType = R.layout.home_item_audio_ver
}

data class HomeSingleImgContentModel(
        val jump_url: String = "",
        val image_path: String = "",
        val image_url: String = ""
) {

}
