package com.allever.robotbestpractice

import android.app.Application
import com.allever.robotbestpractice.ifly.IFlyWakeupProxy
import com.allever.robotbestpractice.wakeup.WakeupManager

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        WakeupManager.setProxy(IFlyWakeupProxy())
        WakeupManager.init(this)
    }
}