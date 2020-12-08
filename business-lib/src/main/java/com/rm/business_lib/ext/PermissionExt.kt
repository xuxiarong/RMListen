package com.rm.business_lib.ext

import android.Manifest
import android.app.Activity
import com.rm.baselisten.util.DLog
import pub.devrel.easypermissions.EasyPermissions

/**
 * desc   :
 * date   : 2020/12/07
 * version: 1.0
 */
object PermissionExt : EasyPermissions.PermissionCallbacks {

    fun requestPermission(
        activity: Activity,
        permissions: Array<String>

    ) {
        if (EasyPermissions.hasPermissions(activity, *permissions)) {
            DLog.d("suolong", "所有权限都已授权")
        } else {
            EasyPermissions.requestPermissions(
                activity, "", 0,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        TODO("Not yet implemented")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        TODO("Not yet implemented")
    }

}