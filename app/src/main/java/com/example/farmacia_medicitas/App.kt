package com.example.farmacia_medicitas

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import com.stripe.android.PaymentConfiguration
import com.example.farmacia_medicitas.BuildConfig
import com.google.android.gms.maps.MapsInitializer

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        PaymentConfiguration.init(applicationContext, BuildConfig.STRIPE_PUBLISHABLE_KEY)
        MapsInitializer.initialize(applicationContext, MapsInitializer.Renderer.LATEST) { }
    }
}
