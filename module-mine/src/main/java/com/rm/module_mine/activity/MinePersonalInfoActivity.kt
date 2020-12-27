package com.rm.module_mine.activity

import android.content.Intent
import android.os.Build
import android.widget.Toast
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.FileUtils
import com.rm.business_lib.bean.Country
import com.rm.business_lib.loginUser
import com.rm.business_lib.utils.BusinessCameraAndAlbum
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.activity.MineCropActivity.Companion.FILE_PATH
import com.rm.module_mine.activity.MineCropActivity.Companion.RESULT_CODE_CROP
import com.rm.module_mine.bean.UpdateUserInfoBean
import com.rm.module_mine.databinding.MineActivityPersonalInfoBinding
import com.rm.module_mine.viewmodel.MinePersonalInfoViewModel

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description 个人资料页面
 *
 */
class MinePersonalInfoActivity :
    BaseVMActivity<MineActivityPersonalInfoBinding, MinePersonalInfoViewModel>() {

    companion object {
        const val RESULT_CODE_ADDRESS = 200
        const val RESULT_CODE_SIGNATURE = 300
        const val RESULT_CODE_NICK = 400
    }

    override fun initModelBrId() = BR.viewModel

    override fun getLayoutId() = R.layout.mine_activity_personal_info

    override fun initView() {
        super.initView()
        val titleModel = BaseTitleModel()
            .setLeftIcon(R.drawable.base_icon_back)
            .setTitle(getString(R.string.mine_personal_info))
            .setLeftIconClick { finish() }
        mViewModel.baseTitleModel.value = titleModel

    }

    private var cameraPath: String? = null

    /**
     * 初始化数据
     */
    override fun initData() {
    }

    override fun startObserve() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        DLog.i("----->onActivityResult", "requestCode:$requestCode   resultCode:$resultCode ")
        when (requestCode) {
            100 -> {
                when (resultCode) {
                    RESULT_CODE_ADDRESS -> {
                        countrySuccess(data)
                    }
                    RESULT_CODE_NICK, RESULT_CODE_SIGNATURE -> {
                        mViewModel.userInfo.set(loginUser.get())
                        mViewModel.showTip("修改成功")
                    }
                    RESULT_CODE_CROP -> {
                        data?.getStringExtra(FILE_PATH)?.let {
                            DLog.i("----->onActivityResult", "CROP $it")
                            cameraPath?.let {
                                FileUtils.delete(it)
                            }
                            mViewModel.uploadPic(it)
                        }
                    }
                }
            }
            BusinessCameraAndAlbum.CAMERA_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        mViewModel.photoHelp?.getCameraUri()?.let {
                            val path = FileUtils.getPath(this, it)
                            path?.let { filePath ->
                                cropImage(filePath)
                                cameraPath = filePath
                            }
                        }
                    } else {
                        val cameraImagePath = mViewModel.photoHelp?.getCameraImagePath()
                        cameraImagePath?.let {
                            cropImage(it)
                            cameraPath = it
                        }
                    }
                } else {
                    Toast.makeText(this, "拍照被取消", Toast.LENGTH_LONG).show()
                }
            }
            BusinessCameraAndAlbum.ALBUM_REQUEST_CODE -> {
                data?.data?.let {
                    val path = FileUtils.getPath(this, it)
                    path?.let { filePath -> cropImage(filePath) }
                }
            }
        }
    }

    private fun cropImage(path: String) {
        MineCropActivity.startActivityForResult(this, path, 100)
    }


    private fun countrySuccess(data: Intent?) {
        val country = data?.getSerializableExtra("country") as Country
        mViewModel.userInfo.get()?.let {
            mViewModel.updateUserInfo(
                UpdateUserInfoBean(
                    it.nickname!!,
                    it.gender!!,
                    it.birthday!!,
                    country.cn,
                    it.signature!!
                )
            )
        }
    }

}