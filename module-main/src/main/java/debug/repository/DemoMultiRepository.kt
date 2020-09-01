package debug.repository

import com.chad.library.adapter.base.entity.MultiItemEntity
import debug.model.DemoMultiModel1
import debug.model.DemoMultiModel2
import java.util.*

/**
 * desc   :
 * date   : 2020/08/28
 * version: 1.0
 */
class DemoMultiRepository {

    fun getMultiDataFromService() : MutableList<MultiItemEntity>{

        val int1 = Random().nextInt(100)
        val int2 = Random().nextInt(100)
        val int3 = Random().nextInt(100)
        val int4 = Random().nextInt(100)
        return arrayListOf(
            DemoMultiModel2("服务器精品推荐", "获取"),
            DemoMultiModel1("推荐$int1", int1),
            DemoMultiModel1("推荐$int2", int2),
            DemoMultiModel2("服务器发现更多", "更多"),
            DemoMultiModel1("更多$int3", int3),
            DemoMultiModel1("更多$int4", int4)
        )
    }

}