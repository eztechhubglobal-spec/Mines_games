package com.example

import android.app.Application
import com.example.data.AppDatabase

class MinesAiApp : Application() {
    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }

    override fun onCreate() {
        super.onCreate()
    }
}
