package com.asabirov.cityguide.ui

import android.app.Application
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Places.initialize(applicationContext, "AIzaSyDElqkAo17lD7C9VvD-3oBRYDgBi0lytEE")
    }
}