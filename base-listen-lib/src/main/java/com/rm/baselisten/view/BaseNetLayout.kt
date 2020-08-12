package com.rm.baselisten.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import com.rm.baselisten.R
import com.rm.baselisten.net.BaseNetStatus

/**
 * desc   :
 * date   : 2020/08/10
 * version: 1.0
 */
class BaseNetLayout : FrameLayout {

    private val CONTENT_LAYOUT = "contentLayout"
    private val ERROR_LAYOUT = "errorLayout"
    private val LOAD_LAYOUT = "loadLayout"
    private val EMPTY_LAYOUT = "emptyLayout"

    private var initFinished = false

    private var contentView: View = inflateViewWithLayoutId(R.layout.activity_default)
    private var errorView: View = inflateViewWithLayoutId(R.layout.base_layout_error)
    private var loadView: View = inflateViewWithLayoutId(R.layout.base_layout_loading)
    private var emptyView: View = inflateViewWithLayoutId(R.layout.base_layout_empty)

    private var layoutMap: HashMap<String, BaseLayoutBean> = HashMap()

    constructor (context: Context?) : this(context, null)

    constructor (context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context!!,
        attrs,
        defStyle
    ){
        init()
    }

    fun init() {
        if (!initFinished) {
            layoutMap[CONTENT_LAYOUT] = BaseLayoutBean(R.layout.activity_default, contentView)
            layoutMap[ERROR_LAYOUT] = BaseLayoutBean(R.layout.base_layout_error, errorView)
            layoutMap[LOAD_LAYOUT] = BaseLayoutBean(R.layout.base_layout_loading, loadView)
            layoutMap[EMPTY_LAYOUT] = BaseLayoutBean(R.layout.base_layout_empty, emptyView)
            initFinished = true
            layoutMap.forEach {
                addViewWithName(it.key, it.value.view)
            }
        }
    }


    fun addChild(key: String, layoutId: Int) {
        //是相同的View，不做处理
        if (layoutMap.containsKey(key) && layoutMap[key]?.layoutId == layoutId) {
            return
        } else {
            layoutMap[key] = BaseLayoutBean(layoutId, inflateViewWithLayoutId(layoutId))
        }
        addChild(key, inflateViewWithLayoutId(layoutId))
    }

    fun addChild(key: String, view: View) {
        if (!layoutMap.containsKey(key)) {
            return
        } else {
            removeViewWithName(key, layoutMap[key]?.view)
        }
        addViewWithName(key, view)
    }


    fun addViewWithName(key: String, view: View) {

        val statusLayoutParams =
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER)
        val contentLayoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        when (key) {
            CONTENT_LAYOUT -> {
                this.contentView = view
                addView(view, 0, contentLayoutParams)
            }
            ERROR_LAYOUT -> {
                this.errorView = view
                addView(view, 1, statusLayoutParams)
            }
            LOAD_LAYOUT -> {
                this.loadView = view
                addView(view, 2, statusLayoutParams)
            }
            EMPTY_LAYOUT -> {
                this.emptyView = view
                addView(view, 3, statusLayoutParams)
            }
        }
    }

    fun removeViewWithName(key: String, view: View?) {
        if (view == null) {
            return
        }
        when (key) {
            CONTENT_LAYOUT ->
                if (childCount > 1) removeViewAt(0)
            ERROR_LAYOUT ->
                if (childCount > 2) removeViewAt(1)
            LOAD_LAYOUT ->
                if (childCount > 3) removeViewAt(2)
            EMPTY_LAYOUT ->
                if (childCount > 4) removeViewAt(3)
        }
    }

    fun getContentView(): View {
        return contentView
    }

    fun inflateViewWithLayoutId(layoutId: Int): View {
        return View.inflate(context, layoutId, null)
    }

    fun inflateView(bean: BaseLayoutBean): View {
        return inflateViewWithLayoutId(bean.layoutId)
    }

    fun setContentLayout(@LayoutRes layoutId: Int) {
//        init()
        addChild(CONTENT_LAYOUT, layoutId)
    }

    fun setErrorLayout(@LayoutRes layoutId: Int) {
        addChild(ERROR_LAYOUT, layoutId)
    }

    fun setLoadLayout(@LayoutRes layoutId: Int) {
        addChild(LOAD_LAYOUT, layoutId)
    }

    fun setEmptyLayout(@LayoutRes layoutId: Int) {
        addChild(EMPTY_LAYOUT, layoutId)
    }

    fun showStatusView(status: BaseNetStatus) {
        when (status) {
            BaseNetStatus.BASE_SHOW_CONTENT, BaseNetStatus.BASE_SHOW_LOADING -> {
                this.emptyView.visibility = View.GONE
                this.errorView.visibility = View.GONE
                this.contentView.visibility = View.VISIBLE
                this.loadView.visibility =
                    getVisibleWithStatus(BaseNetStatus.BASE_SHOW_LOADING == status)
            }
            BaseNetStatus.BASE_SHOW_NET_ERROR, BaseNetStatus.BASE_SHOW_DATA_EMPTY -> {
                this.contentView.visibility = View.GONE
                this.loadView.visibility = View.GONE
                this.errorView.visibility =
                    getVisibleWithStatus(BaseNetStatus.BASE_SHOW_NET_ERROR == status)
                this.emptyView.visibility =
                    getVisibleWithStatus(BaseNetStatus.BASE_SHOW_DATA_EMPTY == status)
            }
        }
    }

    fun getVisibleWithStatus(condition: Boolean): Int {
        return if (condition) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }


    data class BaseLayoutBean(@LayoutRes var layoutId: Int, var view: View)


}