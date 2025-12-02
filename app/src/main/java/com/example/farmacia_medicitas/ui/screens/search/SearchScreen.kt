package com.example.farmacia_medicitas.ui.screens.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.farmacia_medicitas.R
import com.example.farmacia_medicitas.data.model.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    products: List<Product>,
    isLoading: Boolean,
    onBack: () -> Unit,
    onProductClick: (String) -> Unit,
    onAddToCart: (Product) -> Unit,
    onNavigateHome: (() -> Unit)? = null,
    onNavigateProfile: (() -> Unit)? = null,
    onNavigateCart: (() -> Unit)? = null,
    onNavigateSettings: (() -> Unit)? = null
) {
    var query by remember { mutableStateOf("") }
    val filtered = remember(query, products) {
        val q = query.trim().lowercase()
        if (q.isEmpty()) products
        else products.filter { p ->
            p.name.lowercase().contains(q) || (p.description ?: "").lowercase().contains(q)
        }
    }

    Scaffold(
        topBar = {},
        bottomBar = {
            NavigationBar {
                NavigationBarItem(selected = false, onClick = { onNavigateHome?.invoke() }, icon = { Icon(Icons.Filled.Home, contentDescription = null) }, label = { Text("") }, alwaysShowLabel = false)
                NavigationBarItem(selected = true, onClick = { }, icon = { Icon(Icons.Filled.Search, contentDescription = null) }, label = { Text("") }, alwaysShowLabel = false)
                NavigationBarItem(selected = false, onClick = { onNavigateCart?.invoke() }, icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = null) }, label = { Text("") }, alwaysShowLabel = false)
                NavigationBarItem(selected = false, onClick = { onNavigateProfile?.invoke() }, icon = { Icon(Icons.Filled.Person, contentDescription = null) }, label = { Text("") }, alwaysShowLabel = false)
                NavigationBarItem(selected = false, onClick = { onNavigateSettings?.invoke() }, icon = { Icon(Icons.Filled.Settings, contentDescription = null) }, label = { Text("") }, alwaysShowLabel = false)
            }
        }
    ) { inner ->
        Column(modifier = Modifier.padding(inner).fillMaxSize()) {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), shape = RoundedCornerShape(20.dp), modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                placeholder = { Text("Buscar productos…") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )
            }

            if (!isLoading) {
                Text(
                    text = "Resultados: ${filtered.size}",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (isLoading) {
                androidx.compose.foundation.lazy.LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(6) {
                        com.example.farmacia_medicitas.ui.components.ProductSkeletonCard(modifier = Modifier.fillMaxWidth())
                    }
                }
            } else {
                androidx.compose.foundation.lazy.LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filtered, key = { it.id }) { product ->
                        SearchProductCard(
                            product = product,
                            onClick = { onProductClick(product.id) },
                            onAddToCart = { onAddToCart(product) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchProductCard(
    product: Product,
    onClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.clickable(onClick = onClick).padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = product.name,
                    modifier = Modifier.size(72.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(product.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            if (!product.description.isNullOrBlank()) {
                Text(product.description!!, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text("$${product.price}", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                FilledTonalButton(onClick = onAddToCart) { Text("Agregar") }
            }
        }
    }
}
