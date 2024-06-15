package com.max.openweatherapp

import android.app.Application
import android.content.Context
import com.google.android.material.color.DynamicColors


class CityApplication: Application() {

    private var CONTEXT: Context? = null

    override fun onCreate(){
        super.onCreate()
        setContext(this)
        DynamicColors.applyToActivitiesIfAvailable(this)
    }

    private fun setContext(context: Context) {
        CONTEXT = context
    }

    override fun getApplicationContext(): Context {
        return CONTEXT!!
    }
}