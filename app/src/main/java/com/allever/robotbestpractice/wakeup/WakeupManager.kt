package com.allever.robotbestpractice.wakeup

import android.content.Context

object WakeupManager: IWakeupProxy {
    override fun setListener(wakeupListener: WakeupListener) {
        mWakeupListener = wakeupListener
        mProxy?.setListener(wakeupListener)
    }

    override fun enhanceAhead() {
        mProxy?.enhanceAhead()
    }

    override fun enhanceAround() {
        mProxy?.enhanceAround()
    }

    private var mWakeupListener: WakeupListener? = null

    private var mProxy: IWakeupProxy? = null

    override fun init(context: Context) {
        mProxy?.init(context)
    }

    fun setProxy(proxy: IWakeupProxy) {
        mProxy = proxy
    }

    override fun startWakeup() {
        mProxy?.startWakeup()
    }

    override fun stopWakeup() {
        mProxy?.stopWakeup()
    }

    override fun destroy() {
        mProxy?.destroy()
    }

}