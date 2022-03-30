package jp.chikaharu11.instant_drumpad_tr808

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class MyMessageService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Log.d("MyMessageService", "Refreshed token: $token")
    }
}