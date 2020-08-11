package com.rm.baselisten.load.core

import com.rm.baselisten.load.LoadSirUtil.getTargetContext
import com.rm.baselisten.load.callback.Callback
import com.rm.baselisten.load.callback.Callback.OnReloadListener
import java.util.*

/**
 * desc   :
 * date   : 2020/08/10
 * version: 1.0
 */
class LoadSir {
    private var builder: Builder

    private constructor() {
        builder = Builder()
    }

    private fun setBuilder(builder: Builder) {
        this.builder = builder
    }

    private constructor(builder: Builder) {
        this.builder = builder
    }

    fun register(target: Any): LoadService<*> {
        return register<Any>(target, null, null)
    }

    @JvmOverloads
    fun <T> register(
        target: Any?,
        onReloadListener: OnReloadListener?,
        convertor: Convertor<T>? = null
    ): LoadService<*> {
        val targetContext =
            getTargetContext(target!!)
        return LoadService(convertor, targetContext, onReloadListener, builder)
    }

    class Builder {
         internal val callbacks: MutableList<Callback> =
            ArrayList()
        var defaultCallback: Class<out Callback>? =
            null
            private set

        fun addCallback(callback: Callback): Builder {
            callbacks.add(callback)
            return this
        }

        fun setDefaultCallback(defaultCallback: Class<out Callback>): Builder {
            this.defaultCallback = defaultCallback
            return this
        }

        fun getCallbacks(): List<Callback> {
            return callbacks
        }

        fun commit() {
            default!!.setBuilder(this)
        }

        fun build(): LoadSir {
            return LoadSir(this)
        }
    }

    companion object {
        @Volatile
        private var loadSir: LoadSir? = null
        val default: LoadSir?
            get() {
                if (loadSir == null) {
                    synchronized(LoadSir::class.java) {
                        if (loadSir == null) {
                            loadSir = LoadSir()
                        }
                    }
                }
                return loadSir
            }

        fun beginBuilder(): Builder {
            return Builder()
        }
    }
}