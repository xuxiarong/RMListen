package lm

import java.util.concurrent.ConcurrentHashMap

/**
 * desc   :
 * date   : 2020/08/12
 * version: 1.0
 */
class ComponentManager {

    companion object {
        @JvmStatic
        val instance: ComponentManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { ComponentManager() }
    }

    val componentMap: ConcurrentHashMap<Class<*>, IComponentCommunication> by lazy {
        ConcurrentHashMap<Class<*>, IComponentCommunication>()
    }

    inline fun<reified T : IMusicPlayer> injectMusic( component: T){
        componentMap[IMusicPlayer::class.java] = component
    }
    inline fun<reified T : IComponentCommunication> getImplWithClass(clazz : Class<T>) : IComponentCommunication {
        if(componentMap[clazz] == null){
            throw Exception(" must first inject ${clazz.simpleName} iml after used")
        }
        return componentMap.getValue(clazz)
    }

    fun test(){

    }

}