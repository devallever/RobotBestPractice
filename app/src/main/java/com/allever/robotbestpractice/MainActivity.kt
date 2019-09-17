package com.allever.robotbestpractice

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.allever.robotbestpractice.wakeup.WakeupListener
import com.allever.robotbestpractice.wakeup.WakeupManager

class MainActivity : AppCompatActivity(), WakeupListener {
    override fun onWakeup(angle: Int) {
        Log.d("Main", "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        WakeupManager.setWakeupListener(this)
        WakeupManager.startWakeup()
    }

    override fun onDestroy() {
        super.onDestroy()
        WakeupManager.destroy()
    }
}
