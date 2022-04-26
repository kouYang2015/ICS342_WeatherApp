package ics340kyang.weatherapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class LocNotifService : Service(){

    override fun onCreate() {
        super.onCreate()
        Log.d("Service", "Service created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("Service", "Service is started and running.")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.d("Service", "Service stopped and destroyed.")
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        // We don't provide binding, so return null
        return null
    }

}