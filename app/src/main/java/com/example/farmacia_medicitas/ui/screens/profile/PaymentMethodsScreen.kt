package com.example.farmacia_medicitas.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PaymentMethodsScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Métodos de pago", style = MaterialTheme.typography.titleLarge)
        Text(text = "Stripe configurado como método de pago", style = MaterialTheme.typography.bodyMedium)
    }
}

