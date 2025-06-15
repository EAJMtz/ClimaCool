package com.example.climacool

import android.app.Application
import com.google.firebase.FirebaseApp

class ClimaCool : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
