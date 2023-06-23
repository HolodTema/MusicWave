package com.terabyte.musicwave.application

import android.app.Application
import com.terabyte.musicwave.di.AppComponent
import com.terabyte.musicwave.di.DaggerAppComponent

//the class is open since we want to test it later and inherit TestMyApplication class
open class MyApplication: Application() {

    val appComponent: AppComponent by lazy {
        initAppComponent()
    }

    override fun onCreate() {
        super.onCreate()
        initAppComponent()
    }

    //appComponent creating in separate method since we want to test it and we will override it
    open fun initAppComponent() =
        DaggerAppComponent.factory().create()
}