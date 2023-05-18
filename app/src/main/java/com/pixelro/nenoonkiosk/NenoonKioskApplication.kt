package com.pixelro.nenoonkiosk

import android.app.Application
import android.content.Context

class NenoonKioskApplication : Application() {
    init {
        instance = this
    }

    companion object {
        lateinit var instance: NenoonKioskApplication
        fun applicationContext(): Context {
            return instance.applicationContext
        }
    }
}