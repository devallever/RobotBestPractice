package com.allever.robotbestpractice.wakeup

import android.content.Context

interface IWakeupProxy {
    fun init(context: Context)
    fun setListener(wakeupListener: WakeupListener)
}