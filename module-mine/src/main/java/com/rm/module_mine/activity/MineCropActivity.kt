package com.rm.module_mine.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.mvvm.BaseActivity
import com.rm.baselisten.util.DLog
import com.rm.module_mine.R
import com.rm.module_mine.util.BitmapUtil
import com.rm.module_mine.widget.CropImageView
import kotlinx.android.synthetic.main.mine_activity_image_crop.*
import java.io.File

/**
 *
 * @author yuanfang
 * @date 10/24/20
 * @description
 *
 */
class MineCropActivity() :
    BaseActivity(), CropImageView.OnBitmapSaveCompleteListener {
    private var mBitmap: Bitmap? = null
    private var mIsSaveRectangle = false

    companion object {
        const val FILE_PATH = "filePath"
        const val RESULT_CODE_CROP = 500

        fun startActivityForResult(activity: Activity, filePath: String, code: Int) {
            val intent = Intent(activity, MineCropActivity::class.java)
            intent.putExtra(FILE_PATH, filePath)
            activity.startActivityForResult(intent, code)
        }
    }


    override fun getLayoutId() = R.layout.mine_activity_image_crop
    override fun initView() {
        super.initView()

        cv_crop_image.apply {
            setOnBitmapSaveCompleteListener(this@MineCropActivity)
            focusStyle = CropImageView.Style.CIRCLE //裁剪框的形状
            focusWidth = 800
            focusHeight = 800
        }

        //获取需要的参数

        mIsSaveRectangle = false //裁剪后的图片是否是矩形，否者跟随裁剪框的形状

        val imagePath = intent.getStringExtra(FILE_PATH)
        //缩放图片
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imagePath, options)
        val displayMetrics = resources.displayMetrics
        options.inSampleSize =
            calculateInSampleSize(options, displayMetrics.widthPixels, displayMetrics.heightPixels)
        options.inJustDecodeBounds = false
        mBitmap = BitmapFactory.decodeFile(imagePath, options)

        cv_crop_image.setImageBitmap(
            cv_crop_image.rotate(
                mBitmap,
                BitmapUtil.getBitmapDegree(imagePath)
            )
        )

        mine_crop_cancel.setOnClickListener {
            finish()
        }
        mine_crop_sure.setOnClickListener {
            cv_crop_image.saveBitmapToFile(
                File("$cacheDir/ImagePicker/cropTemp/"),
                800,
                800,
                true
            )
        }
    }

    override fun initData() {

    }

    override fun onBitmapSaveError(file: File?) {
    }

    override fun onBitmapSaveSuccess(file: File?) {
        //裁剪后替换掉返回数据的内容，但是不要改变全局中的选中数据
        intent.putExtra(FILE_PATH, file!!.absolutePath)
        setResult(RESULT_CODE_CROP, intent) //单选不需要裁剪，返回数据
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        cv_crop_image.setOnBitmapSaveCompleteListener(null)
        if (null != mBitmap && !mBitmap!!.isRecycled) {
            mBitmap!!.recycle()
            mBitmap = null
        }
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val width = options.outWidth
        val height = options.outHeight
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            inSampleSize = if (width > height) {
                width / reqWidth
            } else {
                height / reqHeight
            }
        }
        return inSampleSize
    }


}