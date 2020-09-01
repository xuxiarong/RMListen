package com.rm.module_home.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.business_lib.bean.BookBean
import com.rm.module_home.api.HomeApiService
import com.rm.module_home.model.home.detail.*
import com.rm.module_home.viewmodel.HomeDetialChapterViewModel

class DetailRepository(val service: HomeApiService) : BaseRepository(){

    fun getDetailInfo(): HomeDetailModel {

        val anchor = Anchor("萧鼎",0,
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598872294338&di=e452a46759d72fc10f5013911cee1ca9&imgtype=0&src=http%3A%2F%2Fimg.tuquu.com%2F2019-03%2F2%2F2019031910050683821.png",0)
        val Tag = mutableListOf<Tags>()
        Tag.add(Tags(1,"军事"))
        val detailinfo  = DetailArticle(anchor,1,4,"author","author_intro",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598872294338&di=e452a46759d72fc10f5013911cee1ca9&imgtype=0&src=http%3A%2F%2Fimg.tuquu.com%2F2019-03%2F2%2F2019031910050683821.png",
            "intro",1,"测试书名是否能换行且显示省略号","34",1,Tag,1)

        return HomeDetailModel(detailinfo)
    }

    fun getCommentInfo(): HomeCommentViewModel {

        val member = Member("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598872294338&di=e452a46759d72fc10f5013911cee1ca9&imgtype=0&src=http%3A%2F%2Fimg.tuquu.com%2F2019-03%2F2%2F2019031910050683821.png",
            "3163414801716737","Maria Williams")
        val commentlist = CommentList("do velit",1598944782,2188895048094859,
            false,7814,member,"6066473726094696",false)

        val homecomment = mutableListOf<CommentList>()

        for(i in 0..10){
            homecomment.add(i,commentlist)
        }
        return HomeCommentViewModel(homecomment)
    }

    fun getChapterInfo() : DetailChapterModel{

        val chapterlist = ChapterList("2020",25,"章节的名称",
            0 ,"www.baidu.com",100,1,"100M")
        val chapter = mutableListOf<ChapterList>()

        for(i in 0..10){
            chapter.add(i ,chapterlist)
        }

        return DetailChapterModel(chapter,10)

    }
}