package com.rm.module_home.model

/**
 * 书籍详情bean类
 */
class HomeDetailModel {
    /**
     * name : 诛仙1
     * type : 1
     * author : 萧鼎
     * anchor_id : 4
     * intro : 张小凡的故事
     * author_intro : 一个汉子
     * audio_id : 151781923415396350
     * progress : 1
     * play_count : 34
     * cover_url : http://ls-book.leimans.com/common/2020/0811/c8b6513aab94f8345122185586351b23.jpeg
     * last_sequence : 3
     * anchor : {"anchor_name":"萧鼎","anchor_avatar":"http://ls-book.leimans.com/common/2020/0811/c8b6513aab94f8345122185586351b23.jpeg","anchor_follows":0,"status":0}
     * tags : [{"id":4,"name":"军事"},{"id":4,"name":"军事"},{"id":4,"name":"军事"}]
     */
    var name: String? = null
    var type = 0
    var author: String? = null
    var anchor_id = 0
    var intro: String? = null
    var author_intro: String? = null
    var audio_id: Long = 0
    var progress = 0
    var play_count = 0
    var cover_url: String? = null
    var last_sequence = 0
    var anchor: AnchorBean? = null
    var tags: List<TagsBean>? = null

    class AnchorBean {
        /**
         * anchor_name : 萧鼎
         * anchor_avatar : http://ls-book.leimans.com/common/2020/0811/c8b6513aab94f8345122185586351b23.jpeg
         * anchor_follows : 0
         * status : 0
         */
        var anchor_name: String? = null
        var anchor_avatar: String? = null
        var anchor_follows = 0
        var status = 0

    }

    class TagsBean {
        /**
         * id : 4
         * name : 军事
         */
        var id = 0
        var name: String? = null

    }

}