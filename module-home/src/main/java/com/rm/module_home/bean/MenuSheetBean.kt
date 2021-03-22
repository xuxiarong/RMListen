package com.rm.module_home.bean

import com.rm.business_lib.bean.BannerInfoBean
import com.rm.business_lib.bean.SheetListBean

/**
 * desc   : 菜单类bean
 * date   : 2020/09/19
 * version: 1.0
 */
data class MenuSheetBean(
    var banner_list: MutableList<BannerInfoBean>?,
    var block_list: MutableList<Block>?,
    var menu_list: MutableList<Menu>?,
    var page_id: Int,
    var page_name: String
)

class Block(
)

class Menu(
)



