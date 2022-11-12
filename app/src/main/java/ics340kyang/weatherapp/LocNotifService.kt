package ics340kyang.weatherapp

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LocNotifService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var notificationManager: NotificationManagerCompat
    lateinit var locationRequest: LocationRequest
    val CHANNEL_ID = "channelID"
    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            if (locationResult.lastLocation != null) {
                Log.d(
                    "Service",
                    "Location Updated| latitude:" + locationResult.lastLocation?.latitude
                            + " longitude:" + locationResult.lastLocation?.longitude
                )
                submitLastLocation()
            }
            else {
                Log.d("LocationService", "Failed to get latitude and longitude.")
            }
        }
    }

    @Inject
    lateinit var viewModel: ServiceViewModel

    override fun onCreate() {
        super.onCreate()
        Log.d("Service", "Service created")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        createNotificationChannel()
        notificationManager = NotificationManagerCompat.from(this)
        createLocationRequest()
    }

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("Service", "Service is started and running.")
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
        return Service.START_STICKY
    }

    override fun onDestroy() {
        Log.d("Service", "Service stopped and destroyed.")
        notificationManager.cancel(1)
        if (fusedLocationClient != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        // We don't provide binding, so return null
        return null
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create()
        locationRequest.interval = 20000 //Slowest update every 20 sec
        locationRequest.fastestInterval = 10000 //Fastest update every 10 sec
        locationRequest.priority = Priority.PRIORITY_HIGH_ACCURACY
    }

    private fun createNotificationAndPush(currentConditions: CurrentConditions) {
        var cityName = currentConditions.name
        var cityTemp = currentConditions.main.temp
        // val iconName = //currentConditions.weather.firstOrNull()?.icon

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(cityName)
            .setContentText("Weather is currently " + cityTemp)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .setSmallIcon(R.drawable.sun)
            .setContentIntent(pendingIntent)
            .setAutoCancel(false)
            .setChannelId(CHANNEL_ID)

        notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(1, builder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val channelName = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel1 = NotificationChannel(CHANNEL_ID, channelName, importance)
            channel1.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
        }
    }

    @SuppressLint("MissingPermission")
    private fun submitLastLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener {
            viewModel.updateLocation(it.latitude, it.longitude)
            viewModel.submitLocationButton()
            viewModel.currentConditionCall.value?.let { it1 -> createNotificationAndPush(it1) }
        }
    }

}