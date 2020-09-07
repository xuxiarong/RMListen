package com.rm.module_home.activity.menu

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.binding.linearBottomItemDecoration
import com.rm.baselisten.dialog.CommonMvFragmentDialog
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.thridlib.glide.loadBlurImage
import com.rm.baselisten.utilExt.getStateHeight
import com.rm.business_lib.bean.AudioBean
import com.rm.business_lib.bean.BookBean
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.activity.detail.HomeDetailActivity
import com.rm.module_home.databinding.HomeActivityListenMenuDetailBinding
import com.rm.module_home.databinding.HomeDialogMenuDetailBinding
import com.rm.module_home.databinding.HomeHeaderMenuDetailBinding
import kotlinx.android.synthetic.main.home_activity_listen_menu_detail.*


class MenuDetailActivity :
    BaseVMActivity<HomeActivityListenMenuDetailBinding, MenuDetailViewModel>(),
    View.OnClickListener {
    companion object {
        fun startActivity(context: Context, sheetId: String?, pageId: Int?) {
            context.startActivity(Intent(context, MenuDetailActivity::class.java).apply {
                putExtra("sheetId", sheetId)
                putExtra("pageId", pageId)
            })
        }
    }

    private val mAdapter by lazy {
        CommonBindVMAdapter<AudioBean>(
            mViewModel,
            mutableListOf(),
            R.layout.home_adapter_menu_detail,
            BR.click,
            BR.item
        ).apply {
//            addHeaderView(layoutInflater.inflate(R.layout.home_header_menu_detail,null))
//            addHeaderView(dataBinding!!.root)
        }
    }

    private val mDialogAdapter by lazy {
        CommonBindVMAdapter<BookBean>(
            mViewModel,
            mutableListOf(),
            R.layout.home_adapter_dialog_book_list,
            BR.dialogClick,
            BR.dialogItem
        )
    }
    private val dataBinding by lazy {
        DataBindingUtil.inflate<HomeHeaderMenuDetailBinding>(
            LayoutInflater.from(this@MenuDetailActivity),
            R.layout.home_header_menu_detail,
            home_menu_detail_recycler_view,
            false
        )
    }
    private var menuDialog: CommonMvFragmentDialog? = null

    override fun startObserve() {
        mViewModel.data.observe(this) {
//            dataBinding?.setVariable(BR.header, it)
            loadBlurImage(home_menu_detail_iv_bg, it.cover)

//            if (it.isCollected) {
//                home_menu_detail_collected.apply {
//                    setBackgroundResource(R.drawable.home_select_menu_collected_unselect)
//                    text = resources.getString(R.string.home_menu_detail_collected)
//                    setTextColor(Color(R.color.business_text_color_b1b1b1))
//                }
//            } else {
//                home_menu_detail_collected.apply {
//                    setBackgroundResource(R.drawable.home_select_menu_collected_select)
//                    text = resources.getString(R.string.home_menu_detail_add_collected)
//                    setTextColor(Color(R.color.business_text_color_ffffff))
//                }
//            }
            mAdapter.setList(it.audio_list)
        }

//        mViewModel.dialogData.observe(this) {
//            mDialogAdapter.setList(it)
//        }
    }

    override fun initView() {
        super.initView()
        setD()//设置透明沉浸状态栏
        val layoutParams = (home_menu_detail_title_cl.layoutParams) as ConstraintLayout.LayoutParams
        layoutParams.apply {
            //动态获取状态栏的高度,并设置标题栏的topMargin
            val stateHeight = getStateHeight(this@MenuDetailActivity)
            topMargin = stateHeight
        }

        home_menu_detail_recycler_view.apply {
            bindVerticalLayout(mAdapter)
            linearBottomItemDecoration(resources.getDimensionPixelOffset(R.dimen.dp_18))
        }

        //item点击事件
        mViewModel.itemClick = {
            HomeDetailActivity.startActivity(this)
        }
//        home_menu_detail_collected.setOnClickListener(this)
        home_menu_detail_back.setOnClickListener(this)
        home_menu_detail_share.setOnClickListener(this)

    }

    override fun initData() {
        val pageId = intent.getIntExtra("pageId", 0)
        val sheetId = intent.getStringExtra("sheetId")
        mViewModel.getData("$pageId", sheetId ?: "", "")
    }

    override fun getLayoutId(): Int {
        return R.layout.home_activity_listen_menu_detail
    }

    override fun initModelBrId(): Int {
        return BR.viewModel
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.home_menu_detail_collected -> {
                showCollectedDialog()
            }
            R.id.home_menu_detail_back -> {
                finish()
            }
            R.id.home_menu_detail_share -> {
                share()
            }
        }
    }

    //加入书单弹窗
    private fun showCollectedDialog() {
        if (menuDialog == null) {
            val height = resources.getDimensionPixelSize(R.dimen.dp_390)
            menuDialog = CommonMvFragmentDialog().apply {
                gravity = Gravity.BOTTOM
                dialogWidthIsMatchParent = true
                dialogHeight = height
                dialogHasBackground = true
                initDialog = {
                    val homeDialogMenuDetailBinding =
                        (menuDialog?.mDataBind) as HomeDialogMenuDetailBinding?

                    homeDialogMenuDetailBinding?.homeDialogMenuRecyclerView?.let {
                        it.bindVerticalLayout(mDialogAdapter)
                        it.linearBottomItemDecoration(resources.getDimensionPixelSize(R.dimen.dp_14))
                    }
                }
            }
        }
        menuDialog?.showCommonDialog(this,
            R.layout.home_dialog_menu_detail,
            mViewModel,
            BR.viewModel)
    }

    //分享
    private fun share() {

    }
}