package com.example.farmacia_medicitas.ui.screens.delivery

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.farmacia_medicitas.core.LocationService

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DeliveryMapScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val loc = LocationService.getLastKnownLocation(context)
    val url = if (loc != null) {
        "https://www.google.com/maps/search/?api=1&query=${loc.latitude},${loc.longitude}+farmacia"
    } else {
        "https://www.google.com/maps/search/?api=1&query=farmacia+near+me"
    }
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(text = "Mapa de entrega") },
            navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás") } }
        )
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    webViewClient = WebViewClient()
                    loadUrl(url)
                }
            }
        )
    }
}

