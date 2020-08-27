//package com.rm.business_lib.wedgit.recycleview
//
//import android.content.Context
//import android.support.annotation.LayoutRes
//import android.support.v7.widget.GridLayoutManager
//import android.support.v7.widget.LinearLayoutManager
//import android.support.v7.widget.RecyclerView
//import android.support.v7.widget.StaggeredGridLayoutManager
//import android.util.AttributeSet
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.annotation.LayoutRes
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import androidx.recyclerview.widget.StaggeredGridLayoutManager
//
//
//class PerfectRecyclerView : RecyclerView {
//    var headerParent: View? = null
//        private set
//    var footerParent: View? = null
//        private set
//    private var perfectAdapter: PerfectAdapter? = null
//    private var context: Context? = null
//    private var loadMoreSwitch = true
//
//    constructor(context: Context?) : super(context) {}
//    constructor(
//        context: Context?,
//        @Nullable attrs: AttributeSet?
//    ) : super(context, attrs) {
//    }
//
//    constructor(
//        context: Context?,
//        @Nullable attrs: AttributeSet?,
//        defStyle: Int
//    ) : super(context, attrs, defStyle) {
//        this.context = context
//    }
//
//    fun addHeaderView(@LayoutRes id: Int): PerfectRecyclerView {
//        headerParent = LayoutInflater.from(getContext()).inflate(id, this, false)
//        perfectAdapter.notifyItemInserted(0)
//        return this
//    }
//
//    fun addFooterView(@LayoutRes id: Int): PerfectRecyclerView {
//        footerParent = LayoutInflater.from(getContext()).inflate(id, this, false)
//        perfectAdapter.notifyItemInserted(perfectAdapter!!.itemCount - 1)
//        return this
//    }
//
//    fun closeLoadMore() {
//        loadMoreSwitch = false
//    }
//
//    fun notifyItemInserted(i: Int) {
//        var i = i
//        if (headerParent != null) i++
//        perfectAdapter.notifyItemInserted(i)
//    }
//
//    fun notifyItemChanged(i: Int) {
//        var i = i
//        if (headerParent != null) i++
//        perfectAdapter.notifyItemChanged(i)
//    }
//
//    fun notifyItemRemoved(i: Int) {
//        var i = i
//        if (headerParent != null) i++
//        perfectAdapter.notifyItemRemoved(i)
//    }
//
//    fun notifyDataSetChanged() {
//        perfectAdapter.notifyDataSetChanged()
//    }
//
//    @Deprecated("")
//    fun setAdapter(adapter: Adapter?) {
//        if (adapter != null) {
//            perfectAdapter = PerfectAdapter(adapter)
//        }
//        super.setAdapter(perfectAdapter)
//    }
//
//    fun setAdapterPerfect(adapter: Adapter?): PerfectRecyclerView {
//        setAdapter(adapter)
//        return this
//    }
//
//    fun onScrolled(dx: Int, dy: Int) {
//        super.onScrolled(dx, dy)
//        if (onRollingRequestMore != null) {
//            if (isSlideToBottom && loadMoreSwitch) {
//                onRollingRequestMore!!.scrollBottom()
//            }
//        }
//    }
//
//    fun onNestedPreScroll(
//        target: View,
//        dx: Int,
//        dy: Int,
//        consumed: IntArray?
//    ) {
//        if ((target as RecyclerView).getChildAt(0).getY() === 0) {
//            scollTop!!.isScollTop
//        }
//        super.onNestedPreScroll(target, dx, dy, consumed)
//    }
//
//    private var scollTop: ScollTop? = null
//    fun scollTop(scollTop: ScollTop?): PerfectRecyclerView {
//        this.scollTop = scollTop
//        return this
//    }
//
//    interface ScollTop {
//        val isScollTop: Unit
//    }
//
//    //VerticalLinear
//    fun setLayoutManagerVerticalLinear(): PerfectRecyclerView {
//        val linearLayoutManager = LinearLayoutManager(context)
//        layoutManager = linearLayoutManager
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL)
//        this.setLayoutManager(linearLayoutManager)
//        return this
//    }
//
//    //HorizontalLinear
//    fun setLayoutManagerHorizontalLinear(): PerfectRecyclerView {
//        val linearLayoutManager = LinearLayoutManager(context)
//        layoutManager = linearLayoutManager
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL)
//        this.setLayoutManager(linearLayoutManager)
//        return this
//    }
//
//    //VerticalGird
//    fun setLayoutManagerVerticalGird(i: Int): PerfectRecyclerView {
//        val gridLayoutManager = GridLayoutManager(context, i)
//        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL)
//        layoutManager = gridLayoutManager
//        this.setLayoutManager(gridLayoutManager)
//        return this
//    }
//
//    //HorizontalGird
//    fun setLayoutManagerHorizontalGird(i: Int): PerfectRecyclerView {
//        val gridLayoutManager = GridLayoutManager(context, i)
//        gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL)
//        layoutManager = gridLayoutManager
//        this.setLayoutManager(gridLayoutManager)
//        return this
//    }
//
//    //VerticalStaggeredGrid
//    fun setLayoutManagerVerticalStaggered(i: Int): PerfectRecyclerView {
//        val sgl =
//            StaggeredGridLayoutManager(i, StaggeredGridLayoutManager.VERTICAL)
//        layoutManager = sgl
//        this.setLayoutManager(sgl)
//        return this
//    }
//
//    /**
//     * 平行瀑布流
//     *
//     * @param i 垂直每行 i 条
//     * @return
//     */
//    fun setLayoutManagerHorizontalStaggered(i: Int): PerfectRecyclerView {
//        val sgl =
//            StaggeredGridLayoutManager(i, StaggeredGridLayoutManager.HORIZONTAL)
//        layoutManager = sgl
//        this.setLayoutManager(sgl)
//        return this
//    }
//
//    private var layoutManager: LayoutManager? = null
//
//    /**
//     * @return LayoutManager
//     */
//    fun getLayoutManager(): LayoutManager? {
//        return layoutManager
//    }
//
//    var onRollingRequestMore: OnScrollBottom? = null
//
//    /**
//     * 滑动到底部监听
//     *
//     * @param onBottomLoad
//     */
//    fun addScrollBottom(onBottomLoad: OnScrollBottom?) {
//        onRollingRequestMore = onBottomLoad
//    }
//
//    interface OnScrollBottom {
//        fun scrollBottom()
//    }
//
//    protected val isSlideToBottom: Boolean
//        protected get() = if (computeVerticalScrollExtent() + computeVerticalScrollOffset() >= computeVerticalScrollRange()) true else false
//
//    private inner class PerfectAdapter(adapter: Adapter) : Adapter<ViewHolder?>() {
//        private val adapter: Adapter
//        private val Item_Normal = 0
//        private val Item_Header = 1
//        private val Item_Footer = 2
//        fun onViewAttachedToWindow(holder: ViewHolder) {
//            super.onViewAttachedToWindow(holder)
//            if (isStaggeredGridLayout(holder)) {
//                handleLayoutIfStaggeredGridLayout(holder, holder.getLayoutPosition())
//            }
//        }
//
//        fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
//            super.onAttachedToRecyclerView(recyclerView)
//            val manager: LayoutManager = recyclerView.getLayoutManager()
//            if (manager is GridLayoutManager) {
//                val gridManager: GridLayoutManager = manager as GridLayoutManager
//                gridManager.setSpanSizeLookup(object : GridLayoutManager.SpanSizeLookup() {
//                    fun getSpanSize(position: Int): Int {
//                        return if (getItemViewType(position) == Item_Footer or getItemViewType(
//                                position
//                            ) == Item_Header
//                        ) gridManager.getSpanCount() else 1
//                    }
//                })
//            }
//        }
//
//        fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
//            return if (viewType == Item_Header) {
//                PerfectViewHolder(headerParent)
//            } else if (viewType == Item_Footer) {
//                PerfectViewHolder(footerParent)
//            } else {
//                adapter.onCreateViewHolder(parent, viewType)
//            }
//        }
//
//        fun onBindViewHolder(holder: ViewHolder?, position: Int) {
//            val type = getItemViewType(position)
//            if (type == Item_Header || type == Item_Footer) {
//                return
//            }
//            val realPosition = getRealItemPosition(position)
//            adapter.onBindViewHolder(holder, realPosition)
//        }
//
//        val itemCount: Int
//            get() {
//                var itemCount: Int = adapter.getItemCount()
//                if (null != headerParent) itemCount++
//                if (null != footerParent) itemCount++
//                return itemCount
//            }
//
//        fun getItemViewType(position: Int): Int {
//            if (null != headerParent && position == 0) return Item_Header
//            return if (null != footerParent && position == itemCount - 1) Item_Footer else Item_Normal
//        }
//
//        private fun isStaggeredGridLayout(holder: ViewHolder): Boolean {
//            val layoutParams: ViewGroup.LayoutParams = holder.itemView.getLayoutParams()
//            return if (layoutParams != null && layoutParams is StaggeredGridLayoutManager.LayoutParams) {
//                true
//            } else false
//        }
//
//        protected fun handleLayoutIfStaggeredGridLayout(
//            holder: ViewHolder,
//            position: Int
//        ) {
//            if (getItemViewType(position) == Item_Footer or getItemViewType(position) == Item_Header) {
//                val p: StaggeredGridLayoutManager.LayoutParams =
//                    holder.itemView.getLayoutParams() as StaggeredGridLayoutManager.LayoutParams
//                p.setFullSpan(true)
//            }
//        }
//
//        private fun getRealItemPosition(position: Int): Int {
//            return if (null != headerParent) {
//                position - 1
//            } else position
//        }
//
//        private inner class PerfectViewHolder internal constructor(itemView: View?) :
//            ViewHolder(itemView)
//
//        init {
//            this.adapter = adapter
//        }
//    }
//}