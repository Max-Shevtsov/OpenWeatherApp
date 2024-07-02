package com.max.openweatherapp

import android.app.Application
import android.content.Context
import com.google.android.material.color.DynamicColors


class CityApplication: Application() {

    override fun onCreate(){
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }

}