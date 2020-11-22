package com.razvantmz.onemove.ui.base

import android.app.Application
import android.content.Context
import com.razvantmz.onemove.core.interfaces.IViewContainer

class CoreApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Instance = this
    }

    var intentData:String? = null

    companion object
    {
        lateinit var Instance: CoreApplication
        var currentActivity: IViewContainer?= null
    }
}