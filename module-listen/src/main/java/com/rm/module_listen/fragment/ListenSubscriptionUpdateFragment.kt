package com.rm.module_listen.fragment

import androidx.databinding.Observable
import androidx.lifecycle.Observer
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.binding.bindHorizontalLayout
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.business_lib.isLogin
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.adapter.ListenAudioAdapter
import com.rm.module_listen.databinding.ListenFragmentSubscriptionUpdateBinding
import com.rm.module_listen.model.ListenAudioDateModel
import com.rm.module_listen.viewmodel.ListenSubsUpdateViewModel

/**
 * desc   :
 * date   : 2020/09/09
 * version: 1.0
 */
class ListenSubscriptionUpdateFragment :
    BaseVMFragment<ListenFragmentSubscriptionUpdateBinding, ListenSubsUpdateViewModel>() {

    private val mSubsDateAdapter : CommonBindVMAdapter<ListenAudioDateModel> by lazy {
        CommonBindVMAdapter(mViewModel, mutableListOf<ListenAudioDateModel>(),
            R.layout.listen_item_subs_date,
            BR.viewModel,
            BR.item)
    }

    private val mListenAudioAdapter : ListenAudioAdapter by lazy {
        ListenAudioAdapter(
            mViewModel,
            BR.viewModel,
            BR.item
        )
    }

    override fun initModelBrId() = BR.viewModel

    override fun startObserve() {
        mViewModel.subsDateListDate.observe(this, Observer {
            mSubsDateAdapter.setList(it)
        })
        mViewModel.allUpdateList.observe(this, Observer {
            mListenAudioAdapter.setList(it)
        })
        mViewModel.todayUpdateList.observe(this, Observer {
            mListenAudioAdapter.notifyDataSetChanged()
        })
        mViewModel.earlyUpdateList.observe(this, Observer {
            mListenAudioAdapter.notifyDataSetChanged()
        })
        mViewModel.yesterdayUpdateList.observe(this, Observer {
            mListenAudioAdapter.notifyDataSetChanged()
        })
        isLogin.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if(isLogin.get()){
                    mViewModel.getSubsDataFromService()
                }
            }
        })

    }

    override fun initLayoutId() = R.layout.listen_fragment_subscription_update

    override fun initData() {
        mViewModel.onAudioClick = {startDetail(it)}
        if(isLogin.get()){
            mViewModel.getSubsDataFromService()
        }
    }

    override fun onStart() {
        super.onStart()
        if(!isLogin.get()){
            RouterHelper.createRouter(LoginService::class.java).quicklyLogin(mViewModel,activity!!)
        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getSubsDataFromService()

    }

    fun startDetail(audioId :String){
        val homeService = RouterHelper.createRouter(HomeService::class.java)
        homeService.toDetailActivity(context!!,audioId)
    }

    override fun initView() {
        super.initView()
        mDataBind.listenSubsDataRv.bindHorizontalLayout(mSubsDateAdapter)
        mDataBind.listenSubscriptionRv.bindVerticalLayout(mListenAudioAdapter)

    }

    companion object {
        fun newInstance(): ListenSubscriptionUpdateFragment {
            return ListenSubscriptionUpdateFragment()
        }
    }
}