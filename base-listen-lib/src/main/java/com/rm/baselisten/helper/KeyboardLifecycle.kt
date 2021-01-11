package com.rm.baselisten.helper

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**
 *
 * @author yuanfang
 * @date 10/12/20
 * @description 监听键盘的显示/隐藏 生命周期绑定，防止内存泄漏
 *
 */
class KeyboardLifecycle(private val keyboard: KeyboardStatusDetector) : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        keyboard.removeListener()
    }
}