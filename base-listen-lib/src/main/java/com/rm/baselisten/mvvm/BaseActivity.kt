package com.rm.baselisten.mvvm

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.core.view.contains
import androidx.fragment.app.FragmentActivity
import com.gyf.barlibrary.ImmersionBar
import com.rm.baselisten.R
import com.rm.baselisten.thridlib.statusbarlib.ImmersionBarHelper
import com.rm.baselisten.utilExt.dip
import com.rm.baselisten.view.BaseTipView
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


/**
 * desc   : 基类的MvvmActivity
 * date   : 2020/08/05
 * version: 1.0
 */
abstract class BaseActivity : FragmentActivity(), EasyPermissions.PermissionCallbacks {

    private val immersionBarHelper: ImmersionBarHelper by lazy {
        ImmersionBarHelper.create(this)
    }

    val tipView: BaseTipView by lazy {
        BaseTipView(this)
    }

    private var requestPermission = ""
    private var requestPermissionCode = 1001
    var requestPermissionGranted: () -> Unit = {}
    var requestPermissionDenied: () -> Unit = {}
    var requestPermanentlyDenied: () -> Unit = {}

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
        this.requestPermission = permission
        this.requestPermissionDenied = actionDenied
        this.requestPermissionGranted = actionGranted
        this.requestPermanentlyDenied = actionPermanentlyDenied
        if (EasyPermissions.hasPermissions(this, permission)) {
            actionGranted()
        } else {
            EasyPermissions.requestPermissions(this, "听书需要存储权限",  requestPermissionCode, permission)
        }
    }


    override fun onPermissionsDenied(
        requestCode: Int, perms: List<String?>
    ) {
        if (requestCode == requestPermissionCode) {
            perms.forEach {
                it?.let {
                    if (it == requestPermission) {
                        if (EasyPermissions.somePermissionPermanentlyDenied(this, arrayListOf(requestPermission))) {
                            AppSettingsDialog.Builder(this)
                                .setTitle("权限已被永久拒绝")
                                .setRationale("听书需要存储权限，请前往设置页面打开该权限")
                                .setPositiveButton(R.string.base_to_set)
                                .setNegativeButton(R.string.base_cancel)
                                .setRequestCode(AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE)
                                .build().show()
                        }else{
                            requestPermissionDenied()
                        }
                    }
                }
            }
        }
    }

    override fun onPermissionsGranted(
        requestCode: Int, perms: List<String?>
    ) {
        if (requestCode == requestPermissionCode) {
            perms.forEach {
                it?.let {
                    if (it == requestPermission) {
                        requestPermissionGranted()
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            //从设置页面返回，判断权限是否申请。
            if (EasyPermissions.hasPermissions(this, requestPermission)) {
                requestPermissionGranted()
            } else {
                requestPermanentlyDenied()
            }
        }
    }
}