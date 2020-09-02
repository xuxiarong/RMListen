package com.rm.module_play.helper

/**
 *
 * @des:
 * @data: 9/2/20 11:23 AM
 * @Version: 1.0.0
 */
class MusicClickControler {

    private var mCounts = 0 //设定时间内允许触发的次数

    private var mMillisSeconds = 0 //设定的时间毫秒数

    private var mCurrentCounts = 0 //当前已经触发的次数

    private var mFirstTriggerTime: Long = 0 //当前时间段内首次触发的时间


    fun init(nCounts: Int, millisSeconds: Int) {
        mCounts = nCounts
        mMillisSeconds = millisSeconds
        mCurrentCounts = 0
        mFirstTriggerTime = 0
    }

    fun canTrigger(): Boolean {
        val time = System.currentTimeMillis()
        //重置首次触发时间和已经触发次数
        if (mFirstTriggerTime == 0L || time - mFirstTriggerTime > mMillisSeconds) {
            mFirstTriggerTime = time
            mCurrentCounts = 0
        }
        //已经触发了mCounts次，本次不能触发
        if (mCurrentCounts >= mCounts) {
            return false
        }
        ++mCurrentCounts
        return true
    }
}