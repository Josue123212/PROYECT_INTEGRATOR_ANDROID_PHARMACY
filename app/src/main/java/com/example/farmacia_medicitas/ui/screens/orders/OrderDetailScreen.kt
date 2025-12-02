package com.example.farmacia_medicitas.ui.screens.orders

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.farmacia_medicitas.ui.viewmodel.OrderDetailViewModel

@Composable
fun OrderDetailScreen(orderId: Int, onBack: () -> Unit) {
    val vm: OrderDetailViewModel = hiltViewModel()
    val state = remember { mutableStateOf(vm.state) }
    LaunchedEffect(orderId) { vm.load(orderId) }
    val s = vm.state
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Detalle de la orden #$orderId", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Estado: ${s.status ?: ""}")
        Text(text = "Total: ${s.total ?: 0.0} ${s.currency ?: ""}")
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn {
            items(s.items) { it ->
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), modifier = Modifier.padding(vertical = 6.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = it.product_title ?: "Producto", style = MaterialTheme.typography.titleSmall)
                        Text(text = "Cantidad: ${it.quantity ?: 0}")
                        Text(text = "Subtotal: ${it.subtotal ?: 0.0}")
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onBack) { Text("Volver") }
    }
}

