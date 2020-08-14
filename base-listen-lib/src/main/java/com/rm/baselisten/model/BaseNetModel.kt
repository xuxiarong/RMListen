package com.rm.baselisten.model

import androidx.annotation.LayoutRes
import com.rm.baselisten.R
import com.rm.baselisten.net.BaseNetStatus
import com.rm.baselisten.net.bean.BaseStatusModel

/**
 * desc   :
 * date   : 2020/08/10
 * version: 1.0
 */
class BaseNetModel {

    lateinit var baseTitleModel : BaseTitleModel
    lateinit var baseNetLayoutModel : BaseNetLayoutModel
    lateinit var baseStatusModel : BaseStatusModel

    init {
        setTitleModel(null)
        setLayoutModel(null)
        setStatusModel(null)
    }

    fun setTitleModel (baseTitleModel : BaseTitleModel?) :BaseNetModel{
        if(baseTitleModel!=null){
            this.baseTitleModel = baseTitleModel
        }else{
            this.baseTitleModel = BaseTitleModel().setTitle("登陆")
        }
        return this
    }

    fun setLayoutModel (baseNetLayoutModel: BaseNetLayoutModel?) :BaseNetModel{
        if(baseNetLayoutModel!=null){
            this.baseNetLayoutModel = checkAndSetLayout(baseNetLayoutModel)
        }else{
            this.baseNetLayoutModel = checkAndSetLayout(BaseNetLayoutModel(R.layout.activity_default,R.layout.base_layout_error,R.layout.base_layout_loading,R.layout.base_layout_empty))
        }
        return this
    }

    fun setContentLayout(@LayoutRes contentLayout : Int): BaseNetModel {
        this.baseNetLayoutModel.contentLayout = contentLayout
        return this
    }

    fun setLoadLayout(@LayoutRes loadLayout : Int): BaseNetModel {
        this.baseNetLayoutModel.loadLayout = loadLayout
        return this
    }

    fun setErrorLayout(@LayoutRes netErrorLayout : Int): BaseNetModel {
        this.baseNetLayoutModel.netErrorLayout = netErrorLayout
        return this
    }

    fun setEmptyLayout(@LayoutRes emptyLayout : Int): BaseNetModel {
        this.baseNetLayoutModel.emptyLayout = emptyLayout
        return this
    }

    private fun checkAndSetLayout(baseNetLayoutModel: BaseNetLayoutModel) : BaseNetLayoutModel{

        if(baseNetLayoutModel.contentLayout == 0){
            baseNetLayoutModel.contentLayout = R.layout.activity_default
        }
        if(baseNetLayoutModel.emptyLayout == 0){
            baseNetLayoutModel.emptyLayout = R.layout.base_layout_empty
        }
        if(baseNetLayoutModel.loadLayout == 0){
            baseNetLayoutModel.loadLayout = R.layout.base_layout_loading
        }
        if(baseNetLayoutModel.netErrorLayout == 0){
            baseNetLayoutModel.netErrorLayout = R.layout.base_layout_error
        }
        return baseNetLayoutModel
    }

    private fun setStatusModel(baseStatusModel: BaseStatusModel?){
        if(baseStatusModel !=null){
            this.baseStatusModel = baseStatusModel
        }else{
            this.baseStatusModel = BaseStatusModel(BaseNetStatus.BASE_SHOW_CONTENT)
        }
    }

    private fun checkAndSetStatus(baseStatusModel: BaseStatusModel?){

        if(baseStatusModel !=null){

        }else{

        }
    }

    fun setStatus(baseNetStatus: BaseNetStatus) : BaseNetModel{
        this.baseStatusModel.netStatus = baseNetStatus
        return this
    }

}