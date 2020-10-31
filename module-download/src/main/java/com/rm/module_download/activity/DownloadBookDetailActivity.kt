package com.rm.module_download.activity

import android.content.Context
import android.content.Intent
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.business_lib.db.DaoUtil
import com.rm.business_lib.db.DownloadAudioDao
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.module_download.BR
import com.rm.module_download.R
import com.rm.module_download.databinding.DownloadActivityBookDetailBinding
import com.rm.module_download.viewmodel.DownloadBookDetailViewModel

class DownloadBookDetailActivity :
    BaseVMActivity<DownloadActivityBookDetailBinding, DownloadBookDetailViewModel>() {

    companion object {
        fun startActivity(context: Context,audio: DownloadAudio) {
            val intent = Intent(context, DownloadBookDetailActivity::class.java)
            intent.putExtra("download_audio",audio)
            context.startActivity(intent)
        }
    }

    override fun initModelBrId(): Int = BR.viewModel

    override fun startObserve() {

    }

    override fun initData() {
        if(intent!=null){
            val audio = intent.getSerializableExtra("download_audio") as DownloadAudio
            val qb = DaoUtil(DownloadAudio::class.java, "").queryBuilder()
            qb?.where(DownloadAudioDao.Properties.Audio_id.eq(audio.audio_id))
            val list = qb?.list()
            val end = System.currentTimeMillis()
            if(list!=null && list.size>0){
                mViewModel.downloadAudio.set(list[0])
                mViewModel.downloadingAdapter.setList(list[0].chapterList)
            }else{
                mViewModel.downloadAudio.set(audio)
                mViewModel.downloadingAdapter.setList(audio.chapterList)
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.download_activity_book_detail

    override fun initView() {
        super.initView()
        val baseTitleModel = BaseTitleModel().apply {
            setLeftIcon(R.drawable.base_icon_back)
            setRightIcon(R.drawable.business_ic_share)
            setRightIconClick {
                finish()
            }
            setLeftIconClick {
                finish()
            }

        }
        mViewModel.baseTitleModel.value = baseTitleModel

    }


}