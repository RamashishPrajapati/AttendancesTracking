package com.ram.attendancestracking.locationUtils

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices
import com.ram.attendancestracking.R
import com.ram.attendancestracking.database.AttendanceDatabase
import com.ram.attendancestracking.model.UserLocation
import com.ram.attendancestracking.utils.CGlobal_lib
import com.ram.attendancestracking.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LocationService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient
    private lateinit var attendanceDatabase: AttendanceDatabase

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
        attendanceDatabase = AttendanceDatabase.getDatabase(
            applicationContext, serviceScope, applicationContext.resources
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        IS_SERVICE_RUNNING = true

        val notification = NotificationCompat.Builder(this, Constants.LOCATION_ID)
            .setContentTitle(getString(R.string.app_name))
            .setSmallIcon(R.drawable.schedule)
            .setOngoing(true)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationClient
            .getLocationUpdates(600000L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                val lat = location.latitude.toString()/*.takeLast(3)*/
                val long = location.longitude.toString()/*.takeLast(3)*/
                val updatedNotification = notification.setContentText(
                    "Location: ($lat, $long)"
                )
                Log.d(Constants.TAG, "Location: ($lat, $long)")

                attendanceDatabase.userLocationDao().addUserLocation(
                    UserLocation(
                        CGlobal_lib.getInstance(applicationContext)?.getCurrentTime().toString(),
                        CGlobal_lib.getInstance(applicationContext)?.getCurrentDate().toString(),
                        lat,
                        long
                    )
                )
            }
            .launchIn(serviceScope)

        startForeground(1, notification.build())
    }

    private fun stop() {
        IS_SERVICE_RUNNING = false
        stopForeground(STOP_FOREGROUND_DETACH)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        IS_SERVICE_RUNNING = false
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        var IS_SERVICE_RUNNING = false
    }
}