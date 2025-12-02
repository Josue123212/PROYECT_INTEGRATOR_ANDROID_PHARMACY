package com.example.farmacia_medicitas.ui.screens.orders

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.farmacia_medicitas.ui.viewmodel.OrdersViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.farmacia_medicitas.ui.theme.Primary500
import com.example.farmacia_medicitas.ui.theme.Primary600
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun OrdersScreen(onViewDetail: (Int) -> Unit = {}) {
    val vm: OrdersViewModel = hiltViewModel()
    LaunchedEffect(Unit) { vm.load() }
    val orders by vm.orders.collectAsStateWithLifecycle()
    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(Brush.horizontalGradient(listOf(Primary500, Primary600)))
            .padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(text = "Historial de compras", style = MaterialTheme.typography.titleLarge, color = Color.White)
        }
        LazyColumn(contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
            items(orders) { o ->
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Orden #${o.id}", style = MaterialTheme.typography.titleMedium)
                        Text(text = "Estado: ${o.status}", style = MaterialTheme.typography.bodySmall)
                        val fecha = runCatching { ZonedDateTime.parse(o.created_at).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) }.getOrDefault("")
                        if (fecha.isNotEmpty()) {
                            Text(text = "Fecha: $fecha", style = MaterialTheme.typography.bodySmall)
                        }
                        Text(text = "${o.total} ${o.currency}", style = MaterialTheme.typography.titleMedium)
                        Text(text = "Ver detalle", color = MaterialTheme.colorScheme.primary, modifier = Modifier.clickable { onViewDetail(o.id) })
                    }
                }
            }
        }
    }
}
