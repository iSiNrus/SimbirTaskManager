package ru.barsik.simbirtaskmanager

import android.app.Application
import android.util.Log
import io.realm.Realm

class App: Application() {

    private val TAG = "App"

    override fun onCreate() {
        Realm.init(applicationContext)
        Log.d(TAG, "onCreate: Realm inited. App starts")

        super.onCreate()
    }
}