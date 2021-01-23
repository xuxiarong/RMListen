package com.rm.baselisten.mvvm

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.contains
import androidx.fragment.app.FragmentActivity
import com.gyf.barlibrary.ImmersionBar
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.rm.baselisten.BaseApplication.Companion.CONTEXT
import com.rm.baselisten.R
import com.rm.baselisten.dialog.TipsFragmentDialog
import com.rm.baselisten.thridlib.statusbarlib.ImmersionBarHelper
import com.rm.baselisten.utilExt.dip
import leakcanary.AppWatcher


/**
 * desc   : 基类的MvvmActivity
 * date   : 2020/08/05
 * version: 1.0
 */
abstract class BaseActivity : FragmentActivity() {

    private val immersionBarHelper: ImmersionBarHelper by lazy {
        ImmersionBarHelper.create(this)
    }

    private var mPermissions: MutableList<String>? = null
    private var permissionDialog: TipsFragmentDialog? = null

    private var requestPermissionGranted: () -> Unit = {}
    private var requestPermissionDenied: () -> Unit = {}
    private var requestPermanentlyDenied: () -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //不是dataBind的模式，则setContentView
        if (!isDataBind()) {
            setContentView(getLayoutId())
            //初始化子类的View
            initView()
            //初始化子类的数据
            initData()
        }
        setStatusBar(R.color.base_activity_bg_color)
    }

    protected open fun isDataBind(): Boolean {
        return false
    }

    protected open fun initView() {

    }

    protected abstract fun initData()

    /**
     * 获取子类布局的ID
     * @return Int 子类布局的ID
     */
    protected abstract fun getLayoutId(): Int

    protected open fun setStatusBar(color: String?) {
        immersionBarHelper.init(color)
    }

    open fun setStatusBar(@ColorRes colorId: Int) {
        immersionBarHelper.init(colorId)
    }

    protected open fun setStatusBar(
        @ColorRes colorId: Int,
        darkText: Boolean
    ) {
        immersionBarHelper.init(colorId, darkText)
    }

    //获取状态蓝高度
    fun navigationBarHeight(): Int = ImmersionBar.getNavigationBarHeight(this)

    /**
     * 设置透明沉浸式
     */
    protected open fun setTransparentStatusBar() {
        immersionBarHelper.defaultInit()
    }


    protected open fun setTransparentStatusBarWhiteFont() {
        immersionBarHelper.defaultInitWhiteFont()
    }

    /**
     * 获取跟布局
     */
    protected open fun rootViewAddView(view: View) {
        val rootView: FrameLayout = findViewById(android.R.id.content)
        if ((view.parent as? FrameLayout)?.contains(view) == true) {
            (view.parent as FrameLayout).removeView(view)
        }
        rootView.addView(view, layoutParams)
    }

    protected open val layoutParams by lazy {
        FrameLayout.LayoutParams(dip(40), dip(40)).apply {
            gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
            setMargins(0, 0, 0, dip(25))
        }
    }

    fun requestPermissionForResult(
        permission: String,
        actionDenied: () -> Unit,
        actionGranted: () -> Unit,
        actionPermanentlyDenied: () -> Unit
    ) {
        if (!hasPermission(permission)) {
            mPermissions = mutableListOf(permission)
            requestPermissionGranted = actionGranted
            requestPermissionDenied = actionDenied
            requestPermanentlyDenied = actionPermanentlyDenied
            XXPermissions.with(this).apply {
                permission(permission)
                request(object : OnPermissionCallback {
                    override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                        if (all) {
                            actionGranted()
                        }
                    }

                    override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                        if (never) {
                            showPermanentlyDeniedDialog(permissions, actionPermanentlyDenied)
                        } else {
                            actionDenied()
                        }
                    }
                })
            }
        } else {
            actionGranted()
        }
    }

    fun requestPermissionForResult(
        permission: MutableList<String>,
        actionDenied: () -> Unit,
        actionGranted: () -> Unit,
        actionPermanentlyDenied: () -> Unit
    ) {
        if (!hasPermissions(permission)) {
            mPermissions = permission
            requestPermissionGranted = actionGranted
            requestPermissionDenied = actionDenied
            requestPermanentlyDenied = actionPermanentlyDenied
            XXPermissions.with(this).apply {
                permission(permission)
                request(object : OnPermissionCallback {
                    override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                        if (all) {
                            mPermissions = null
                            actionGranted()
                        }
                    }

                    override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                        if (never) {
                            showPermanentlyDeniedDialog(permissions, actionPermanentlyDenied)
                        } else {
                            actionDenied()
                        }
                    }
                })
            }
        } else {
            mPermissions = null
            actionGranted()
        }
    }

    private fun showPermanentlyDeniedDialog(
        permissions: MutableList<String>?,
        actionPermanentlyDenied: () -> Unit
    ) {
        TipsFragmentDialog().apply {
            permissionDialog = this
            titleText = "权限已被永久拒绝"
            contentText = "听书需要存储权限，请前往设置页面打开该权限"
            leftBtnText = CONTEXT.resources.getString(R.string.base_cancel)
            rightBtnText = CONTEXT.resources.getString(R.string.base_to_set)
            dialogCancel = false
            leftBtnClick = {
                dismiss()
                actionPermanentlyDenied()
            }
            rightBtnClick = {
                XXPermissions.startPermissionActivity(this, permissions)
            }
        }.show(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //从设置页面返回，判断权限是否申请。
        if (mPermissions != null) {
            if (hasPermissions(mPermissions!!)) {
                mPermissions = null
                requestPermissionGranted()
                permissionDialog?.dismiss()
            } else {
                requestPermanentlyDenied()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AppWatcher.objectWatcher.clearWatchedObjects()
    }

    private fun hasPermissions(permissions: MutableList<String>): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissions.forEach {
                if (!hasPermission(it)) {
                    return false
                }
            }
            return true
        } else {
            return true
        }
    }

    private fun hasPermission(permission: String): Boolean {
        val b = ContextCompat.checkSelfPermission(this, permission) ==
                PackageManager.PERMISSION_GRANTED
        return b
    }
}