package com.example.farmacia_medicitas.core

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class AppMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        val data = message.data
        val type = data["type"]
        when (type) {
            "ORDER_CONFIRMED" -> {
                val total = data["total"]?.toDoubleOrNull()
                if (total != null) Notifier.notifyCheckoutSuccess(this, total)
            }
            "OFFER" -> {
                val name = data["name"] ?: "Oferta"
                val price = data["price"]?.toDoubleOrNull()
                Notifier.notifyOffer(this, name, price)
            }
            else -> {
                val title = message.notification?.title ?: "Medicitas"
                val body = message.notification?.body ?: (data["message"] ?: "")
                if (body.isNotBlank()) {
                    Notifier.notifyOffer(this, title, null)
                }
            }
        }
    }

    override fun onNewToken(token: String) {
        // TODO: enviar token a backend cuando expongas endpoint
    }
}
