package com.lankasmartmart.app.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.lankasmartmart.app.LankaSmartMartApp
import com.lankasmartmart.app.MainActivity
import com.lankasmartmart.app.R

class LankaSmartMartMessagingService : FirebaseMessagingService() {
    
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        
        // Handle different notification types
        remoteMessage.data.let { data ->
            val type = data["type"] ?: "general"
            val title = remoteMessage.notification?.title ?: "LankaSmartMart"
            val body = remoteMessage.notification?.body ?: ""
            
            sendNotification(title, body, type)
        }
    }
    
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // TODO: Send token to your server or save to Firestore
        // This will be implemented when we add user management
    }
    
    private fun sendNotification(title: String, messageBody: String, type: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra("notification_type", type)
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        val channelId = if (type == "order") {
            LankaSmartMartApp.CHANNEL_ORDERS
        } else {
            LankaSmartMartApp.CHANNEL_OFFERS
        }
        
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
}
