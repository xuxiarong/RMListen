package com.rm.module_home.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.module_home.api.HomeApiService
import com.rm.module_home.model.home.detail.Anchor
import com.rm.module_home.model.home.detail.DetailArticle
import com.rm.module_home.model.home.detail.Tags

class DetailRepository(val service: HomeApiService) : BaseRepository(){
    fun getDetailInfo(): MutableList<DetailArticle>{
        val detailinfo  = mutableListOf<DetailArticle>()
        val anchor = mutableListOf<Anchor>()
        val Tag = mutableListOf<Tags>()

        Tag.add(Tags(1,"军事"))
        anchor.add(Anchor("萧鼎",0,
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598872294338&di=e452a46759d72fc10f5013911cee1ca9&imgtype=0&src=http%3A%2F%2Fimg.tuquu.com%2F2019-03%2F2%2F2019031910050683821.png",0))
        detailinfo.add(DetailArticle(anchor,1,4,"author","author_intro",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598872294338&di=e452a46759d72fc10f5013911cee1ca9&imgtype=0&src=http%3A%2F%2Fimg.tuquu.com%2F2019-03%2F2%2F2019031910050683821.png",
            "intro",1,"name",34,1,Tag,1)
        )
        return detailinfo
    }
}