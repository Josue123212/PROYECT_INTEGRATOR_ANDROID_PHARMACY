package com.example.farmacia_medicitas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.compose.rememberNavController
import com.example.farmacia_medicitas.navigation.NavRoutes
import com.example.farmacia_medicitas.ui.screens.cart.CartScreen
import com.example.farmacia_medicitas.ui.screens.checkout.CheckoutScreen
import com.example.farmacia_medicitas.ui.screens.login.LoginScreen
import com.example.farmacia_medicitas.ui.screens.login.RegisterScreen
import com.example.farmacia_medicitas.ui.screens.HomeScreen
// Offers/catalog eliminados: usamos Search como catálogo
import com.example.farmacia_medicitas.ui.screens.product.ProductDetailScreen
import com.example.farmacia_medicitas.ui.theme.FarmaciaMedicitasTheme
import com.example.farmacia_medicitas.ui.viewmodel.CartViewModel
import com.example.farmacia_medicitas.ui.viewmodel.CatalogViewModel
import com.example.farmacia_medicitas.ui.screens.search.SearchScreen
import com.example.farmacia_medicitas.ui.viewmodel.AuthViewModel
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import dagger.hilt.android.AndroidEntryPoint
import com.example.farmacia_medicitas.ui.screens.profile.ProfileScreen
import com.example.farmacia_medicitas.core.Notifier
import androidx.compose.ui.platform.LocalContext
import com.example.farmacia_medicitas.ui.screens.webview.WebViewScreen
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.LocationOn

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        const val EXTRA_NAV_ROUTE = "nav_route"
    }
    lateinit var paymentSheet: PaymentSheet
    var onPaymentResult: ((PaymentSheetResult) -> Unit)? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        paymentSheet = PaymentSheet(this) { result ->
            onPaymentResult?.invoke(result)
        }
        setContent {
            val initialRoute = intent?.getStringExtra(EXTRA_NAV_ROUTE)
            FarmaciaMedicitasApp(initialRoute)
        }
    }
}

@Composable
fun FarmaciaMedicitasApp(initialRoute: String?) {
    FarmaciaMedicitasTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val navController = rememberNavController()
            val catalogViewModel: CatalogViewModel = androidx.hilt.navigation.compose.hiltViewModel()
            val cartViewModel: CartViewModel = androidx.hilt.navigation.compose.hiltViewModel()
            AppNavHost(
                navController = navController,
                catalogViewModel = catalogViewModel,
                cartViewModel = cartViewModel,
                initialRoute = initialRoute
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavHost(
    navController: NavHostController,
    catalogViewModel: CatalogViewModel,
    cartViewModel: CartViewModel,
    initialRoute: String?
) {
    // Collect catalog state inside this composable's scope
    val catalogState = catalogViewModel.state.collectAsStateWithLifecycle()
    androidx.compose.runtime.LaunchedEffect(initialRoute) {
        when (initialRoute) {
            NavRoutes.Search.route -> navController.navigate(NavRoutes.Search.route)
            NavRoutes.Cart.route -> navController.navigate(NavRoutes.Cart.route)
            else -> {}
        }
    }
    AnimatedNavHost(
        navController = navController,
        startDestination = NavRoutes.Welcome.route,
        enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(220)) },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(220)) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(220)) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(220)) }
    ) {
        composable(NavRoutes.Home.route) {
            HomeScreen(onStartClick = { navController.navigate(NavRoutes.Search.route) })
        }
        composable(NavRoutes.Welcome.route) {
            com.example.farmacia_medicitas.ui.screens.welcome.WelcomeScreen(
                onSignUp = { navController.navigate(NavRoutes.Register.route) },
                onSignIn = { navController.navigate(NavRoutes.Login.route) }
            )
        }
        // Ofertas eliminadas
        // Catálogo eliminado; usamos Search

        composable(NavRoutes.Dashboard.route) {
            val catalogViewModel: CatalogViewModel = androidx.hilt.navigation.compose.hiltViewModel()
            val cartViewModel: CartViewModel = androidx.hilt.navigation.compose.hiltViewModel()
            com.example.farmacia_medicitas.ui.screens.home.HomeDashboardScreen(
                vm = catalogViewModel,
                onGoToSearch = { navController.navigate(NavRoutes.Search.route) },
                onGoToOffers = { navController.navigate(NavRoutes.Search.route) },
                onGoToProfile = { navController.navigate(NavRoutes.Profile.route) },
                onGoToCart = { navController.navigate(NavRoutes.Cart.route) },
                onGoToCatalog = { navController.navigate(NavRoutes.Search.route) },
                onGoToSettings = { navController.navigate(NavRoutes.Location.route) },
                onGoToOrders = { navController.navigate(NavRoutes.Orders.route) },
                onProductClick = { pid -> navController.navigate(NavRoutes.ProductDetail.route(pid)) },
                onAddToCart = { p -> cartViewModel.addItem(p) }
            )
        }

        composable(NavRoutes.ProductDetail.routeTemplate) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("id") ?: return@composable
            val product = catalogViewModel.getProduct(productId)
            ProductDetailScreen(
                product = product,
                onBack = { navController.popBackStack() },
                onAddToCart = { p ->
                    cartViewModel.addItem(p)
                    navController.navigate(NavRoutes.Cart.route)
                }
            )
        }

        composable(NavRoutes.Cart.route) {
            val cartItemsState = cartViewModel.itemsFlow.collectAsStateWithLifecycle()
            val itemsList = cartItemsState.value
            val totalAmount = itemsList.sumOf { it.product.price * it.quantity }
            CartScreen(
                items = itemsList,
                total = totalAmount,
                onIncrease = { cartViewModel.increase(it) },
                onDecrease = { cartViewModel.decrease(it) },
                onCheckout = { navController.navigate(NavRoutes.Checkout.route) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.Checkout.route) {
            val authVm: AuthViewModel = androidx.hilt.navigation.compose.hiltViewModel()
            val authState = authVm.state.collectAsStateWithLifecycle()
            val context = LocalContext.current
            val checkoutVm: com.example.farmacia_medicitas.ui.viewmodel.CheckoutViewModel = androidx.hilt.navigation.compose.hiltViewModel()
            val checkoutItemsState = cartViewModel.itemsFlow.collectAsStateWithLifecycle()

            // Asegurar verificación de sesión al entrar a Checkout para evitar mostrar el modal por estado inicial desactualizado
            androidx.compose.runtime.LaunchedEffect(Unit) {
                if (!authState.value.isAuthenticated) {
                    authVm.checkSession()
                }
            }

            when {
                authState.value.isAuthenticated -> {
                    CheckoutScreen(
                        total = cartViewModel.total,
                        items = checkoutItemsState.value,
                        onConfirm = {
                            checkoutVm.confirm(
                                onSuccess = {
                                    Notifier.notifyCheckoutSuccess(context, cartViewModel.total)
                                },
                                onError = { /* mostrar mensaje simple por ahora */ }
                            )
                        },
                        onBack = { navController.popBackStack() },
                        onGoHome = {
                            navController.navigate(NavRoutes.Dashboard.route) {
                                popUpTo(NavRoutes.Home.route) { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        onClearCart = { cartViewModel.clear() }
                    )
                }
                authState.value.isLoading -> {
                    // Mostrar un progreso mientras se valida la sesión
                    androidx.compose.material3.Surface {
                        androidx.compose.material3.CircularProgressIndicator()
                    }
                }
                else -> {
                    androidx.compose.material3.AlertDialog(
                        onDismissRequest = { navController.popBackStack() },
                        title = { androidx.compose.material3.Text("Necesitas iniciar sesión") },
                        text = { androidx.compose.material3.Text("Para finalizar la compra, inicia sesión o crea una cuenta.") },
                        confirmButton = {
                            androidx.compose.material3.TextButton(onClick = { navController.navigate(NavRoutes.LoginToCheckout.route) }) {
                                androidx.compose.material3.Text("Iniciar sesión")
                            }
                        },
                        dismissButton = {
                            androidx.compose.material3.TextButton(onClick = { navController.navigate(NavRoutes.RegisterToCheckout.route) }) {
                                androidx.compose.material3.Text("Crear cuenta")
                            }
                        }
                    )
                }
            }
        }

        composable(NavRoutes.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(NavRoutes.Dashboard.route) {
                        popUpTo(NavRoutes.Login.route) { inclusive = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onSignUp = { navController.navigate(NavRoutes.Register.route) },
                onForgotPassword = { navController.navigate(NavRoutes.WebView.route) }
            )
        }

        composable(NavRoutes.LoginToCheckout.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(NavRoutes.Checkout.route) {
                        popUpTo(NavRoutes.LoginToCheckout.route) { inclusive = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onSignUp = { navController.navigate(NavRoutes.RegisterToCheckout.route) },
                onForgotPassword = { navController.navigate(NavRoutes.WebView.route) }
            )
        }

        composable(NavRoutes.Register.route) {
            RegisterScreen(
                onRegistered = {
                    navController.navigate(NavRoutes.Search.route) {
                        popUpTo(NavRoutes.Register.route) { inclusive = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onBack = { navController.popBackStack() },
                onSignIn = { navController.navigate(NavRoutes.Login.route) }
            )
        }

        composable(NavRoutes.RegisterToCheckout.route) {
            RegisterScreen(
                onRegistered = {
                    navController.navigate(NavRoutes.LoginToCheckout.route) {
                        popUpTo(NavRoutes.RegisterToCheckout.route) { inclusive = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onBack = { navController.popBackStack() },
                onSignIn = { navController.navigate(NavRoutes.LoginToCheckout.route) }
            )
        }

        composable(NavRoutes.Search.route) {
            SearchScreen(
                products = catalogState.value.products,
                isLoading = catalogState.value.isLoading,
                onBack = { navController.popBackStack() },
                onProductClick = { productId -> navController.navigate(NavRoutes.ProductDetail.route(productId)) },
                onAddToCart = { p -> cartViewModel.addItem(p) },
                onNavigateHome = { navController.navigate(NavRoutes.Dashboard.route) },
                onNavigateProfile = { navController.navigate(NavRoutes.Profile.route) },
                onNavigateCart = { navController.navigate(NavRoutes.Cart.route) },
                onNavigateSettings = { navController.navigate(NavRoutes.Location.route) }
            )
        }

        composable(NavRoutes.Profile.route) {
            ProfileScreen(
                onBack = { navController.popBackStack() },
                onLogin = { navController.navigate(NavRoutes.Login.route) },
                onRegister = { navController.navigate(NavRoutes.Register.route) },
                onLogoutNavigate = {
                    navController.navigate(NavRoutes.Search.route) {
                        popUpTo(NavRoutes.Home.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onOpenOrders = { navController.navigate(NavRoutes.Orders.route) },
                onOpenAddresses = { navController.navigate(NavRoutes.Addresses.route) },
                onOpenPayments = { navController.navigate(NavRoutes.PaymentMethods.route) },
                onNavigateHome = { navController.navigate(NavRoutes.Dashboard.route) },
                onNavigateProfile = { },
                onNavigateCart = { navController.navigate(NavRoutes.Cart.route) },
                onNavigateSettings = { navController.navigate(NavRoutes.Location.route) }
            )
        }
        composable(NavRoutes.WebView.route) {
            WebViewScreen(url = "https://www.android.com/intl/es_es/", onBack = { navController.popBackStack() })
        }
        composable(NavRoutes.Orders.route) {
            com.example.farmacia_medicitas.ui.screens.orders.OrdersScreen(onViewDetail = { oid ->
                navController.navigate(com.example.farmacia_medicitas.navigation.NavRoutes.OrderDetail.route(oid))
            })
        }
        composable(com.example.farmacia_medicitas.navigation.NavRoutes.OrderDetail.routeTemplate) { backStackEntry ->
            val idStr = backStackEntry.arguments?.getString("id")
            val oid = idStr?.toIntOrNull() ?: return@composable
            com.example.farmacia_medicitas.ui.screens.orders.OrderDetailScreen(orderId = oid, onBack = { navController.popBackStack() })
        }
        composable(NavRoutes.Wishlist.route) { com.example.farmacia_medicitas.ui.screens.mock.MockScreen("Wishlist") }
        composable(NavRoutes.Location.route) {
            androidx.compose.material3.Scaffold(
                bottomBar = {
                    NavigationBar {
                        NavigationBarItem(selected = false, onClick = { navController.navigate(NavRoutes.Dashboard.route) }, icon = { androidx.compose.material3.Icon(Icons.Filled.Home, contentDescription = null) }, label = { androidx.compose.material3.Text("") }, alwaysShowLabel = false)
                        NavigationBarItem(selected = false, onClick = { navController.navigate(NavRoutes.Search.route) }, icon = { androidx.compose.material3.Icon(Icons.Filled.Search, contentDescription = null) }, label = { androidx.compose.material3.Text("") }, alwaysShowLabel = false)
                        NavigationBarItem(selected = false, onClick = { navController.navigate(NavRoutes.Cart.route) }, icon = { androidx.compose.material3.Icon(Icons.Filled.ShoppingCart, contentDescription = null) }, label = { androidx.compose.material3.Text("") }, alwaysShowLabel = false)
                        NavigationBarItem(selected = true, onClick = { }, icon = { androidx.compose.material3.Icon(Icons.Filled.LocationOn, contentDescription = null) }, label = { androidx.compose.material3.Text("") }, alwaysShowLabel = false)
                    }
                }
            ) { inner ->
                androidx.compose.foundation.layout.Box(modifier = androidx.compose.ui.Modifier.padding(inner)) {
                    com.example.farmacia_medicitas.ui.screens.location.LocationScreen()
                }
            }
        }
        composable(NavRoutes.Addresses.route) { com.example.farmacia_medicitas.ui.screens.profile.AddressesScreen() }
        composable(NavRoutes.PaymentMethods.route) { com.example.farmacia_medicitas.ui.screens.profile.PaymentMethodsScreen() }
        composable(NavRoutes.Notifications.route) { com.example.farmacia_medicitas.ui.screens.mock.MockScreen("Notificaciones de ofertas") }
    }
}
