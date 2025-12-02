package com.example.farmacia_medicitas.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.farmacia_medicitas.ui.viewmodel.AuthViewModel
 

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    onLogoutNavigate: () -> Unit,
    onOpenOrders: () -> Unit,
    onOpenAddresses: () -> Unit,
    onOpenPayments: () -> Unit,
    onNavigateHome: (() -> Unit)? = null,
    onNavigateProfile: (() -> Unit)? = null,
    onNavigateCart: (() -> Unit)? = null,
    onNavigateSettings: (() -> Unit)? = null
) {
    val vm: AuthViewModel = hiltViewModel()
    val state by vm.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) { vm.checkSession() }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil") },
                navigationIcon = {
                IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás") }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(selected = false, onClick = { onNavigateHome?.invoke() }, icon = { Icon(Icons.Filled.Home, contentDescription = null) }, label = { Text("") }, alwaysShowLabel = false)
                NavigationBarItem(selected = false, onClick = { /* ya estamos en perfil */ }, icon = { Icon(Icons.Filled.Person, contentDescription = null) }, label = { Text("") }, alwaysShowLabel = false)
                NavigationBarItem(selected = false, onClick = { onNavigateCart?.invoke() }, icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = null) }, label = { Text("") }, alwaysShowLabel = false)
                NavigationBarItem(selected = false, onClick = { onNavigateSettings?.invoke() }, icon = { Icon(Icons.Filled.LocationOn, contentDescription = null) }, label = { Text("") }, alwaysShowLabel = false)
            }
        }
    ) { inner ->
        Column(
            modifier = Modifier.padding(inner).fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Box(modifier = Modifier.size(100.dp), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(64.dp))
                }
            }
            Spacer(Modifier.height(12.dp))

            if (state.isAuthenticated) {
                val user = state.user
                Text(
                    user?.let { (it.first_name ?: "") + " " + (it.last_name ?: "") }
                        ?.trim()
                        ?.takeIf { it.isNotBlank() }
                        ?: (user?.username ?: "Usuario"),
                    style = MaterialTheme.typography.titleMedium
                )
                if (!user?.email.isNullOrBlank()) {
                    Text(user!!.email!!, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                

                

                Spacer(Modifier.height(24.dp))
                Text("Opciones", style = MaterialTheme.typography.titleSmall)
                Spacer(Modifier.height(8.dp))
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ElevatedCard(onClick = onOpenOrders) { ListItem(headlineContent = { Text("Mis pedidos") }, supportingContent = { Text("Historial de compras") }) }
                    ElevatedCard(onClick = onOpenAddresses) { ListItem(headlineContent = { Text("Direcciones") }, supportingContent = { Text("Gestiona tus direcciones") }) }
                    ElevatedCard(onClick = onOpenPayments) { ListItem(headlineContent = { Text("Métodos de pago") }, supportingContent = { Text("Tarjetas y pagos") }) }
                }
                Spacer(Modifier.height(24.dp))
                Button(onClick = {
                    vm.logout()
                    onLogoutNavigate()
                }, modifier = Modifier.fillMaxWidth()) {
                    Text("Cerrar sesión")
                }
            } else {
                Text("Invitado", style = MaterialTheme.typography.titleMedium)
                Text("Inicia sesión para ver tus pedidos y datos", color = MaterialTheme.colorScheme.onSurfaceVariant)

                Spacer(Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(onClick = onLogin, modifier = Modifier.weight(1f)) { Text("Iniciar sesión") }
                    Button(onClick = onRegister, modifier = Modifier.weight(1f)) { Text("Registrarse") }
                }

                Spacer(Modifier.height(32.dp))
                Text("Opciones", style = MaterialTheme.typography.titleSmall)
                Spacer(Modifier.height(8.dp))
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ElevatedCard(onClick = onOpenOrders) { ListItem(headlineContent = { Text("Mis pedidos") }, supportingContent = { Text("Historial de compras") }) }
                    ElevatedCard(onClick = onOpenAddresses) { ListItem(headlineContent = { Text("Direcciones") }, supportingContent = { Text("Gestiona tus direcciones") }) }
                    ElevatedCard(onClick = onOpenPayments) { ListItem(headlineContent = { Text("Métodos de pago") }, supportingContent = { Text("Tarjetas y pagos") }) }
                }
            }
        }
    }
}
