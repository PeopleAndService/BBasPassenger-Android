package com.pns.bbaspassenger.utils

import android.app.Application

class BBasGlobalApplication : Application() {
    companion object {
        lateinit var prefs: BBasSharedPreference
    }

    override fun onCreate() {
        super.onCreate()
        prefs = BBasSharedPreference(applicationContext)
    }
}