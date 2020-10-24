package com.rm.baselisten.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.databinding.ObservableBoolean
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.NetworkUtils

/**
 * desc   :
 * date   : 2020/10/24
 * version: 1.0
 */
class NetworkChangeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent!=null && intent.action!=null){
            if (ConnectivityManager.CONNECTIVITY_ACTION == intent.action) {
                val networkType: NetworkUtils.NetworkType = NetworkUtils.getNetworkType()
                isAvailable.set(networkType != NetworkUtils.NetworkType.NETWORK_NO)
                DLog.d("suolong","${networkType != NetworkUtils.NetworkType.NETWORK_NO}")
            }
        }
    }

    companion object{
        var isAvailable = ObservableBoolean(NetworkUtils.getNetworkType()!= NetworkUtils.NetworkType.NETWORK_NO)
        fun registerNetWorkReceiver(){
            val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            BaseApplication.CONTEXT.registerReceiver(NetworkChangeReceiver(),intentFilter)
        }
    }

}