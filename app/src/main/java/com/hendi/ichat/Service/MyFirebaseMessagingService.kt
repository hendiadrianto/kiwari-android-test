package com.hendi.ichat.Service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        Log.d("Token","My token ${p0}")
        //super.onNewToken(p0)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        Log.d("onMessageReceived","My onMessageReceived ${p0.notification}")
        super.onMessageReceived(p0)
    }

}