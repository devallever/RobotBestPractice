package com.allever.robotbestpractice.wakeup

import android.content.Context

object WakeupManager {
    private var mWakeupListener: WakeupListener? = null

    private var mProxy: IWakeupProxy? = null

    fun init(context: Context) {
        mProxy?.init(context)
    }

    fun setProxy(proxy: IWakeupProxy) {
        mProxy = proxy
    }

    fun setWakeupListener(listener: WakeupListener) {
        mWakeupListener = listener
        mProxy?.setListener(listener)
    }

    fun startWakeup() {
        mProxy?.startWakeup()
    }

    fun stopWakeup() {
        mProxy?.stopWakeup()
    }

    fun destroy() {
        mProxy?.destroy()
    }

}