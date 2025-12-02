package com.example.farmacia_medicitas.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
 
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.farmacia_medicitas.navigation.NavRoutes
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.farmacia_medicitas.ui.components.ProductCard
import com.example.farmacia_medicitas.ui.components.ProductSkeletonCard
import com.example.farmacia_medicitas.ui.theme.Primary500
import com.example.farmacia_medicitas.ui.theme.Primary600
import com.example.farmacia_medicitas.ui.theme.Secondary200
import com.example.farmacia_medicitas.ui.viewmodel.CatalogViewModel
import com.example.farmacia_medicitas.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.launch
import com.example.farmacia_medicitas.core.Notifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun HomeDashboardScreen(
    vm: CatalogViewModel,
    onGoToSearch: () -> Unit,
    onGoToOffers: () -> Unit,
    onGoToProfile: () -> Unit,
    onGoToCart: () -> Unit,
    onGoToCatalog: () -> Unit,
    onGoToSettings: () -> Unit,
    onGoToOrders: () -> Unit,
    onProductClick: (String) -> Unit,
    onAddToCart: (com.example.farmacia_medicitas.data.model.Product) -> Unit
) {
    val state = vm.state.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val authVm: AuthViewModel = hiltViewModel()
    val authState = authVm.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) { authVm.checkSession() }
    val context = LocalContext.current
    val notifiedOffers = remember { mutableStateOf(setOf<String>()) }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(300.dp)
                    .statusBarsPadding()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                        Box(modifier = Modifier.size(48.dp), contentAlignment = Alignment.Center) {
                            Icon(Icons.Filled.Person, contentDescription = null)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            val user = authState.value.user
                            Text(
                                text = user?.let { ((it.first_name ?: "") + " " + (it.last_name ?: "")).trim() }.takeIf { !(it.isNullOrBlank()) } ?: (user?.username ?: "Invitado"),
                                style = MaterialTheme.typography.titleMedium
                            )
                            if (!user?.email.isNullOrBlank()) {
                                Text(text = user!!.email!!, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { onGoToSearch(); scope.launch { drawerState.close() } }.padding(vertical = 12.dp)) {
                        Icon(Icons.Filled.Search, contentDescription = null)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Catálogo")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { onGoToCart(); scope.launch { drawerState.close() } }.padding(vertical = 12.dp)) {
                        Icon(Icons.Filled.ShoppingCart, contentDescription = null)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Carrito")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { onGoToProfile(); scope.launch { drawerState.close() } }.padding(vertical = 12.dp)) {
                        Icon(Icons.Filled.Person, contentDescription = null)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Cuenta")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { onGoToSettings(); scope.launch { drawerState.close() } }.padding(vertical = 12.dp)) {
                        Icon(Icons.Filled.LocationOn, contentDescription = null)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Ubícanos y contáctanos")
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(listOf(Primary500, Primary600))
                        )
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    val search = remember { mutableStateOf("") }
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Visita nuestras farmacias físicas",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            IconButton(onClick = { /* mock notificaciones */ scope.launch { drawerState.close() } }) {
                                Icon(
                                    imageVector = Icons.Filled.Notifications,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                            IconButton(onClick = { onGoToOrders() }) {
                                Icon(
                                    imageVector = Icons.Filled.History,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = search.value,
                            onValueChange = {
                                search.value = it
                                vm.searchSuggestions(it)
                            },
                            placeholder = { Text("Buscar productos") },
                            leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = null) },
                            singleLine = true,
                            shape = RoundedCornerShape(24.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                disabledContainerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        val isSearching = state.value.isSearching
                        val suggestions = state.value.suggestions
                        if (search.value.length >= 2 && (isSearching || suggestions.isNotEmpty())) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                            ) {
                                if (isSearching) {
                                    Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                                        androidx.compose.material3.CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                                    }
                                } else {
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        suggestions.forEach { p ->
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.fillMaxWidth().clickable {
                                                    onProductClick(p.id)
                                                }.padding(12.dp)
                                            ) {
                                                androidx.compose.foundation.layout.Box(modifier = Modifier.size(48.dp), contentAlignment = Alignment.Center) {
                                                    androidx.compose.material3.Icon(Icons.Filled.ShoppingCart, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                                }
                                                Spacer(modifier = Modifier.width(12.dp))
                                                Text(p.name, style = MaterialTheme.typography.bodyMedium)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = true,
                        onClick = { },
                        icon = { Icon(Icons.Filled.Home, contentDescription = null) },
                        label = { Text("") },
                        alwaysShowLabel = false
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { onGoToSearch() },
                        icon = { Icon(Icons.Filled.Search, contentDescription = null) },
                        label = { Text("") },
                        alwaysShowLabel = false
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { onGoToCart() },
                        icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = null) },
                        label = { Text("") },
                        alwaysShowLabel = false
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { onGoToProfile() },
                        icon = { Icon(Icons.Filled.Person, contentDescription = null) },
                        label = { Text("") },
                        alwaysShowLabel = false
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { onGoToSettings() },
                        icon = { Icon(Icons.Filled.LocationOn, contentDescription = null) },
                        label = { Text("") },
                        alwaysShowLabel = false
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {

                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Text(text = "Categorías", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(state.value.categories) { cat ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                modifier = Modifier.clickable { vm.selectCategory(cat.id); onGoToCatalog() }) {
                                Text(
                                    text = cat.name,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No te lo puedes perder",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    val recentOffers = state.value.products.filter { it.saleActive }.take(5)
                    if (state.value.isLoading) {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(5) { ProductSkeletonCard() }
                        }
                    } else if (recentOffers.isNotEmpty()) {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(recentOffers) { p ->
                                ProductCard(
                                    product = p,
                                    onClick = { onProductClick(p.id) },
                                    onAddToCart = { onAddToCart(p) })
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Ofertas del día", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    val allOffers = state.value.products.filter { it.saleActive }
                    if (state.value.isLoading) {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(8) { ProductSkeletonCard() }
                        }
                    } else if (allOffers.isNotEmpty()) {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(allOffers) { p ->
                                ProductCard(
                                    product = p,
                                    onClick = { onProductClick(p.id) },
                                    onAddToCart = { onAddToCart(p) })
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(80.dp))
                }
                LaunchedEffect(state.value.products) {
                    val offers = state.value.products.filter { it.saleActive }
                    val currentNotified = notifiedOffers.value
                    var count = 0
                    offers.forEach { p ->
                        if (!currentNotified.contains(p.id) && count < 3) {
                            Notifier.notifyOffer(context, p.name, p.salePrice ?: p.price)
                            count++
                        }
                    }
                    notifiedOffers.value = currentNotified + offers.take(3).map { it.id }
                }
            }
        }
    }
}
