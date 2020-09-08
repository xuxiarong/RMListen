package debug.model

import com.rm.baselisten.model.BaseSwipeModel

/**
 * desc   :
 * date   : 2020/09/07
 * version: 1.0
 */
data class DemoSwipeModel constructor(val content : String = "",val top : String = "",val read : String = "",val delete : String = "", var demoPosition: Int = 0  ) : BaseSwipeModel(demoPosition)