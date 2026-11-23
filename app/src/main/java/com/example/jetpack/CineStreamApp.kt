package com.example.jetpack

import android.app.Application
import com.example.jetpack.di.AppContainer
import com.example.jetpack.di.DefaultAppContainer

class CineStreamApp : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}

