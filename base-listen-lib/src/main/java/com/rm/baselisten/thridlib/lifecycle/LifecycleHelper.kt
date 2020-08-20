package com.rm.baselisten.thridlib.lifecycle

import android.util.LruCache
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.rm.baselisten.thridlib.queue.DataQueue
import com.rm.baselisten.thridlib.queue.DataQueue.Each

/**
 * author：HeXinGen
 * date: 2019/10/23
 * description: fragment或者FragmentActivity的生命周期辅助类
 */
object LifecycleHelper {
    private val lifeLruCache =
        LruCache<Lifecycle, DefaultLifecycleObserver>(100)

    fun with(fragment: Fragment): DefaultLifecycleObserver {
        return queryObserver(fragment.lifecycle)
    }

    @JvmStatic
    fun with(fragmentActivity: FragmentActivity): DefaultLifecycleObserver {
        return queryObserver(fragmentActivity.lifecycle)
    }

    fun queryObserver(lifecycle: Lifecycle): DefaultLifecycleObserver {
        var observer = lifeLruCache[lifecycle]
        if (observer == null) {
            observer = DefaultLifecycleObserver(lifecycle)
            lifeLruCache.put(lifecycle, observer)
        }
        return observer
    }

    class DefaultLifecycleObserver(lifecycle: Lifecycle) :
        LifecycleObserver {
        private val lifecycle: Lifecycle
        private val startListenerQueue: DataQueue<StartListener>
        private val resumeListenerQueue: DataQueue<ResumeListener>
        private val pauseListenerQueue: DataQueue<PauseListener>
        private val stopListenerQueue: DataQueue<StopListener>
        private val destroyListenerQueue: DataQueue<DestroyListener>

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun onStart() {
            startListenerQueue.forEach(object : Each<StartListener> {
                override fun each(data: StartListener) {
                    data.IStart()
                }
            })
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun onResume() {
            resumeListenerQueue.forEach(object : Each<ResumeListener> {
                override fun each(data: ResumeListener) {
                    data.IResume()
                }
            })
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun onPause() {
            pauseListenerQueue.forEach(object : Each<PauseListener> {
                override fun each(data: PauseListener) {
                    data.IPause()
                }
            })
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun onStop() {
            stopListenerQueue.forEach(object : Each<StopListener> {
                override fun each(data: StopListener) {
                    data.IStop()
                }
            })
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            try {
                destroyListenerQueue.forEach(object : Each<DestroyListener> {
                    override fun each(data: DestroyListener) {
                        data.IDestroy()
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                release()
            }
        }

        private fun release() {
            lifeLruCache.remove(lifecycle)
            lifecycle.removeObserver(this)
            startListenerQueue.release()
            resumeListenerQueue.release()
            pauseListenerQueue.release()
            stopListenerQueue.release()
            destroyListenerQueue.release()
        }

        fun addDestroyListener(destroyListener: DestroyListener): DefaultLifecycleObserver {
            destroyListenerQueue.add(destroyListener)
            return this
        }

        fun addResumeListener(resumeListener: ResumeListener): DefaultLifecycleObserver {
            resumeListenerQueue.add(resumeListener)
            return this
        }

        fun addPauseListener(pauseListener: PauseListener): DefaultLifecycleObserver {
            pauseListenerQueue.add(pauseListener)
            return this
        }

        fun addStartListener(startListener: StartListener): DefaultLifecycleObserver {
            startListenerQueue.add(startListener)
            return this
        }

        fun addStopListener(stopListener: StopListener): DefaultLifecycleObserver {
            stopListenerQueue.add(stopListener)
            return this
        }

        init {
            startListenerQueue = DataQueue()
            resumeListenerQueue = DataQueue()
            pauseListenerQueue = DataQueue()
            stopListenerQueue = DataQueue()
            destroyListenerQueue = DataQueue()
            this.lifecycle = lifecycle
            this.lifecycle.addObserver(this)
        }
    }

    interface DestroyListener {
        fun IDestroy()
    }

    interface StopListener {
        fun IStop()
    }

    interface PauseListener {
        fun IPause()
    }

    interface ResumeListener {
        fun IResume()
    }

    interface StartListener {
        fun IStart()
    }
}