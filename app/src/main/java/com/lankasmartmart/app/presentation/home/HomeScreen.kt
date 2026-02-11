package com.lankasmartmart.app.presentation.home

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.lankasmartmart.app.data.model.ProductCategory
import androidx.hilt.navigation.compose.hiltViewModel
import com.lankasmartmart.app.presentation.auth.AuthViewModel
import com.lankasmartmart.app.presentation.components.ProductCard
import com.lankasmartmart.app.util.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    onProductClick: (String) -> Unit,
    viewModel: ProductViewModel = hiltViewModel()
) {
    val productsState by viewModel.productsState.collectAsState()
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf(ProductCategory.FRUITS) }

    Scaffold(
        topBar = {
            HomeTopBar(
                onCartClick = { /* TODO: Navigate to Cart */ }
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = productsState) {
                is Resource.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is Resource.Error -> {
                    Text(
                        text = "Error: ${state.message}", 
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is Resource.Success -> {
                    val products = state.data ?: emptyList()
                    
                    if (products.isEmpty()) {
                        // Show loading or empty state while auto-seeding happens
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                             CircularProgressIndicator(color = com.lankasmartmart.app.ui.theme.WelcomeScreenGreen)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            // Banner
                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                HomeBanner()
                                Spacer(modifier = Modifier.height(24.dp))
                            }

                            // Categories
                            item {
                                CategorySection(
                                    selectedCategory = selectedCategory,
                                    onCategorySelected = { selectedCategory = it }
                                )
                                Spacer(modifier = Modifier.height(24.dp))
                            }

                            // Fruits Section
                            item {
                                SectionHeader(
                                    title = "Fruits",
                                    onSeeAllClick = { /* TODO */ }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            item {
                                // Filter products for "Fruits" or just show some products as horizontal list
                                val fruitProducts = products.filter { it.category == ProductCategory.FRUITS }.take(5).ifEmpty { products.take(5) }
                                
                                LazyRow(
                                    contentPadding = PaddingValues(horizontal = 8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(fruitProducts) { product ->
                                        ProductCard(
                                            product = product,
                                            onClick = { onProductClick(product.productId) }
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(24.dp))
                            }
                            
                            // All Products Grid (optional, or just another section)
                            item {
                                SectionHeader(title = "All Products")
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            
                             // For grid-like behavior in LazyColumn, use FlowRow or chunked items
                             val otherProducts = products.drop(5).ifEmpty { products }
                             items(otherProducts.chunked(2)) { pair ->
                                 Row(
                                     modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                                     horizontalArrangement = Arrangement.spacedBy(8.dp)
                                 ) {
                                     for (product in pair) {
                                         Box(modifier = Modifier.weight(1f)) {
                                             ProductCard(
                                                 product = product,
                                                 onClick = { onProductClick(product.productId) }
                                             )
                                         }
                                     }
                                     if (pair.size == 1) {
                                         Spacer(modifier = Modifier.weight(1f))
                                     }
                                 }
                             }
                        }
                    }
                }
            }
        }
    }
}
