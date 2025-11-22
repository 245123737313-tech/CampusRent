package com.example.campusrent

import android.app.Application
import com.google.firebase.FirebaseApp

class CampusRentApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
