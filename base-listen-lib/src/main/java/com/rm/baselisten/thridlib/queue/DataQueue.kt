package com.rm.baselisten.thridlib.queue

/**
 * author：HeXinGen
 * date: 2019/10/30
 * description: 常见数据的队列,添加，移除，循环，释放
 */
class DataQueue<T> {
    private var dataList: ArrayList<T> = ArrayList()
    fun add(listener: T): DataQueue<T> {

        if (!dataList.contains(listener)) {
            dataList.add(listener)
        }
        return this
    }

    fun forEach(each: Each<T>?): DataQueue<T> {
        if (each != null) {
            for (data in dataList) {
                each.each(data)
            }
        }
        return this
    }

    fun remove(data: T): DataQueue<T> {
        if (dataList.contains(data)) {
            dataList.remove(data)
        }
        return this
    }

    fun release(): DataQueue<T> {
        dataList.clear()

        return this
    }

    fun getDataList(): List<T>? {
        return dataList
    }

    fun size(): Int {
        return dataList.size
    }

    interface Each<T> {
        fun each(data: T)
    }
}