package com.rm.business_lib.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.os.EnvironmentCompat
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * @author yuan fang
 * @date 12/27/20
 * @description
 *
 */
class BusinessCameraAndAlbum(private val activity: Activity) {
    companion object {
        const val CAMERA_REQUEST_CODE = 0x1001
        const val ALBUM_REQUEST_CODE = 0x1002
    }

    //用于保存拍照图片的uri
    private var mCameraUri: Uri? = null

    // 用于保存图片的文件路径，Android 10以下使用图片路径访问图片
    private var mCameraImagePath: String = ""

    @SuppressLint("QueryPermissionsNeeded")
    fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //判断是否有相机
        if (intent.resolveActivity(activity.packageManager) != null) {
            var photoFile: File? = null
            var photoUri: Uri? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                photoUri = createImageUri()
            } else {
                try {
                    photoFile = createImageFile()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                if (photoFile != null) {
                    mCameraImagePath = photoFile.absolutePath
                    photoUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        FileProvider.getUriForFile(
                            activity,
                            "${activity.packageName}.fileprovider",
                            photoFile
                        )
                    } else {
                        Uri.fromFile(photoFile)
                    }
                }
            }
            mCameraUri = photoUri
            if (photoUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                activity.startActivityForResult(intent, CAMERA_REQUEST_CODE)
            }
        } else {
            Toast.makeText(activity, "没有相机", Toast.LENGTH_SHORT).show()
        }
    }

    fun getCameraUri(): Uri? {
        return mCameraUri
    }

    fun getCameraImagePath(): String? {
        return mCameraImagePath
    }

    fun openAlbum() {
        val intent = Intent(Intent.ACTION_PICK,null)
        intent.type = "image/*"
        activity.startActivityForResult(
            intent,
            ALBUM_REQUEST_CODE
        ) //跳转，传递打开相册请求码
    }

    /**
     * 创建保存图片的文件
     */
    private fun createImageFile(): File? {
        val format = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        if (!storageDir.exists()) {
            storageDir.mkdir()
        }
        val file = File(storageDir, format)
        if (Environment.MEDIA_MOUNTED != EnvironmentCompat.getStorageState(file)) {
            return null
        }
        return file
    }

    /**
     * 创建图片地址uri,用于保存拍照后的照片 Android 10以后使用这种方法
     */
    private fun createImageUri(): Uri? {
        val status = Environment.getExternalStorageState()
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        return if (TextUtils.equals(status, Environment.MEDIA_MOUNTED)) {
            activity.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ContentValues()
            )
        } else {
            activity.contentResolver.insert(
                MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                ContentValues()
            )
        }
    }

}