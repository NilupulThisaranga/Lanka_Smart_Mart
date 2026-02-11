package com.lankasmartmart.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lankasmartmart.app.presentation.auth.AuthViewModel
import com.lankasmartmart.app.presentation.auth.LoginScreen
import com.lankasmartmart.app.presentation.auth.SignUpScreen
import com.lankasmartmart.app.presentation.cart.CartScreen
import com.lankasmartmart.app.presentation.home.HomeScreen
import com.lankasmartmart.app.presentation.products.ProductDetailsScreen
import com.lankasmartmart.app.presentation.profile.AccountScreen
import com.lankasmartmart.app.presentation.splash.SplashScreen
import com.lankasmartmart.app.ui.theme.LankaSmartMartTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LankaSmartMartTheme {
                MainApp()
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector? = null) {
    object Splash : Screen("splash", "Splash")
    object Welcome : Screen("welcome", "Welcome")
    object Login : Screen("login", "Login")
    object Signup : Screen("signup", "Signup")
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Favourite : Screen("favourite", "Favourite", Icons.Default.FavoriteBorder)
    object Search : Screen("search", "Search", Icons.Default.Search)
    object Profile : Screen("profile", "Profile", Icons.Default.PersonOutline)
    object Menu : Screen("menu", "Menu", Icons.Default.Menu)
    object ProductDetails : Screen("product_details/{productId}", "Product Details") {
        fun createRoute(productId: String) = "product_details/$productId"
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    val bottomNavItems = listOf(
        Screen.Home,
        Screen.Favourite,
        Screen.Search,
        Screen.Profile,
        Screen.Menu
    )
    
    // Only show bottom bar on these screens
    val showBottomBar = currentDestination?.route in bottomNavItems.map { it.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = androidx.compose.ui.graphics.Color.White,
                    contentColor = androidx.compose.ui.graphics.Color.Black
                ) {
                    bottomNavItems.forEach { screen ->
                        val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                        NavigationBarItem(
                            icon = { 
                                Icon(
                                    imageVector = if (isSelected && screen == Screen.Home) Icons.Filled.Home else screen.icon!!, 
                                    contentDescription = screen.title,
                                    tint = if (isSelected) com.lankasmartmart.app.ui.theme.WelcomeScreenGreen else androidx.compose.ui.graphics.Color.Black
                                ) 
                            },
                            label = { 
                                Text(
                                    text = screen.title,
                                    color = if (isSelected) com.lankasmartmart.app.ui.theme.WelcomeScreenGreen else androidx.compose.ui.graphics.Color.Black,
                                    style = androidx.compose.material3.MaterialTheme.typography.labelSmall
                                ) 
                            },
                            selected = isSelected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = androidx.compose.ui.graphics.Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(navController = navController)
            }
            composable(Screen.Welcome.route) {
                com.lankasmartmart.app.presentation.welcome.WelcomeScreen(navController = navController)
            }
            composable(Screen.Login.route) {
                LoginScreen(navController = navController, viewModel = authViewModel)
            }
            composable(Screen.Signup.route) {
                SignUpScreen(navController = navController, viewModel = authViewModel)
            }
            composable(Screen.Home.route) {
                HomeScreen(
                    authViewModel = authViewModel,
                    onProductClick = { productId ->
                        navController.navigate(Screen.ProductDetails.createRoute(productId))
                    }
                )
            }
            composable(Screen.Favourite.route) {
                // Placeholder for Favourite Screen
                androidx.compose.foundation.layout.Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text("Favourite Screen")
                }
            }
             composable(Screen.Search.route) {
                // Placeholder for Search Screen
                androidx.compose.foundation.layout.Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text("Search Screen")
                }
            }
            composable(Screen.Profile.route) {
                AccountScreen(
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                )
            }
             composable(Screen.Menu.route) {
                // Placeholder for Menu Screen
                androidx.compose.foundation.layout.Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text("Menu Screen")
                }
            }
            composable(
                route = Screen.ProductDetails.route,
                arguments = listOf(navArgument("productId") { type = NavType.StringType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId") ?: ""
                ProductDetailsScreen(
                    productId = productId,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
