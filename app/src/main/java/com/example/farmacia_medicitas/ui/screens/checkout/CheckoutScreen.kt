package com.example.farmacia_medicitas.ui.screens.checkout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.example.farmacia_medicitas.ui.viewmodel.CheckoutViewModel
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.farmacia_medicitas.data.model.CartItem
import androidx.compose.ui.res.painterResource
import com.example.farmacia_medicitas.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CheckoutScreen(
    total: Double,
    items: List<CartItem>,
    onConfirm: () -> Unit,
    onBack: () -> Unit,
    onGoHome: (() -> Unit)? = null,
    onClearCart: (() -> Unit)? = null,
    vm: CheckoutViewModel = hiltViewModel()
) {
    val step = rememberSaveable { mutableStateOf(0) }
    val country = remember { mutableStateOf("Perú") }
    val address = remember { mutableStateOf("") }
    val zip = remember { mutableStateOf("") }
    val city = remember { mutableStateOf("") }
    val province = remember { mutableStateOf("") }
    val phone = remember { mutableStateOf("") }
    val notes = remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TopAppBar(title = { Text(text = "Checkout") })
        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = if (step.value == 0) "Envío" else "Envío", style = MaterialTheme.typography.bodyMedium)
            Text(text = if (step.value == 1) "Pago" else "Pago", style = MaterialTheme.typography.bodyMedium)
            Text(text = if (step.value == 2) "Confirmación" else "Confirmación", style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(12.dp))
        when (step.value) {
            0 -> {
                Text(text = "Total: S/. ${String.format("%.2f", total)}", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = country.value, onValueChange = { country.value = it }, label = { Text("País") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = address.value, onValueChange = { address.value = it }, label = { Text("Calle y número") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(value = zip.value, onValueChange = { zip.value = it }, label = { Text("Código Postal") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = city.value, onValueChange = { city.value = it }, label = { Text("Localidad") }, modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = province.value, onValueChange = { province.value = it }, label = { Text("Provincia") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = phone.value, onValueChange = { phone.value = it }, label = { Text("Teléfono/Móvil") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = notes.value, onValueChange = { notes.value = it }, label = { Text("Observaciones") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onBack) { Text("Atrás") }
                    Button(onClick = { step.value = 1 }) { Text("Siguiente") }
                }
            }
            1 -> {
                Text(text = "Total: S/. ${String.format("%.2f", total)}", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(12.dp))
                val uiState by vm.ui.collectAsStateWithLifecycle()
                val clientSecret = remember { mutableStateOf<String?>(null) }
                val orderIdState = remember { mutableStateOf<Int?>(null) }
                val activity = LocalContext.current as com.example.farmacia_medicitas.MainActivity
                activity.onPaymentResult = { result ->
                    when (result) {
                        is PaymentSheetResult.Completed -> {
                            val cs = clientSecret.value ?: uiState.clientSecret
                            val oid = orderIdState.value ?: uiState.orderId
                            if (cs != null && oid != null) {
                                val piId = cs.substringBefore("_secret")
                                vm.waitForConfirmation(oid, piId, onSuccess = { step.value = 2 }, onError = { })
                            }
                        }
                        is PaymentSheetResult.Canceled -> {
                            vm.setError("Pago cancelado")
                        }
                        is PaymentSheetResult.Failed -> {
                            val msg = result.error.message ?: "Error en el pago"
                            vm.setError(msg)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Método de pago", style = MaterialTheme.typography.titleSmall)
                        Spacer(Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = null, modifier = Modifier.height(24.dp))
                            Spacer(Modifier.height(0.dp))
                            Text("Tarjeta (Stripe)", style = MaterialTheme.typography.bodyMedium)
                        }
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = { step.value = 0 }) { Text("Atrás") }
                            Button(onClick = {
                                vm.startStripeCheckout(onReady = { cs, oid ->
                                    clientSecret.value = cs
                                    orderIdState.value = oid
                                    activity.paymentSheet.presentWithPaymentIntent(cs)
                                }, onError = { })
                            }) { Text("Pagar ahora") }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Resumen del pedido", style = MaterialTheme.typography.titleSmall)
                        Spacer(Modifier.height(8.dp))
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(items) { it ->
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Column(Modifier.weight(1f)) {
                                        Text(it.product.name, style = MaterialTheme.typography.bodyMedium)
                                        Text("x${it.quantity}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Text("S/. ${String.format("%.2f", it.product.price * it.quantity)}", style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total", style = MaterialTheme.typography.titleMedium)
                            Text("S/. ${String.format("%.2f", total)}", style = MaterialTheme.typography.titleMedium)
                        }
                        Spacer(Modifier.height(8.dp))
                        Text("Pago seguro con Stripe", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                val err = uiState.error
                if (err != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = err, style = MaterialTheme.typography.bodyMedium)
                }
            }
            else -> {
                androidx.compose.runtime.LaunchedEffect(Unit) {
                    onClearCart?.invoke()
                }
                Text(text = "Pedido confirmado", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { onGoHome?.invoke() ?: onBack() }) { Text("Volver") }
            }
        }
    }
}
