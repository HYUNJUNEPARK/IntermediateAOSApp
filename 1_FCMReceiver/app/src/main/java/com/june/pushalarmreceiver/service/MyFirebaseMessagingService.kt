package com.june.pushalarmreceiver.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.june.pushalarmreceiver.activity.MainActivity.Companion.TAG
import com.june.pushalarmreceiver.notification.FCMNotification

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d(TAG, "MyFirebaseMessagingService onNewToken : $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        FCMNotification(this).notificationChannel()
        FCMNotification(this).initNotification(remoteMessage)
    }
}