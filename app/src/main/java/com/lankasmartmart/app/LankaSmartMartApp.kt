package com.lankasmartmart.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LankaSmartMartApp : Application() {
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }
    
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channels = listOf(
                NotificationChannel(
                    CHANNEL_OFFERS,
                    "Offers & Promotions",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "New offers, discounts, and flash sales"
                },
                NotificationChannel(
                    CHANNEL_ORDERS,
                    "Order Updates",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Order status and delivery updates"
                }
            )
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            channels.forEach { channel ->
                notificationManager.createNotificationChannel(channel)
            }
        }
    }
    
    companion object {
        const val CHANNEL_OFFERS = "offers_channel"
        const val CHANNEL_ORDERS = "orders_channel"
    }
}
