package com.rm.module_home.bean

import com.rm.business_lib.bean.BannerInfoBean
import com.rm.business_lib.bean.SheetListBean

data class MenuSheetBean(
    var banner_list: MutableList<BannerInfoBean>?,
    var block_list: MutableList<Block>?,
    var menu_list: MutableList<Menu>?,
    var page_id: Int,
    var page_name: String,
    var sheet_list: SheetListBean?
)

class Block(
)

class Menu(
)



