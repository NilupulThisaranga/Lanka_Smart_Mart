package com.lankasmartmart.app.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lankasmartmart.app.data.model.Product
import com.lankasmartmart.app.data.model.ProductCategory
import com.lankasmartmart.app.data.repository.ProductRepository
import com.lankasmartmart.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.google.firebase.firestore.FirebaseFirestore

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val firestore: FirebaseFirestore // Injecting directly for seeding helper
) : ViewModel() {

    private val _productsState = MutableStateFlow<Resource<List<Product>>>(Resource.Loading())
    val productsState: StateFlow<Resource<List<Product>>> = _productsState.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String>("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        android.util.Log.d("ProductViewModel", "loadProducts called")
        // Trigger network refresh in background
        viewModelScope.launch {
            try {
                val result = repository.refreshProducts()
                if (result.isFailure) {
                    android.util.Log.e("ProductViewModel", "Refresh failed", result.exceptionOrNull())
                }
            } catch (e: Exception) {
                android.util.Log.e("ProductViewModel", "Refresh exception", e)
            }
        }

        viewModelScope.launch {
            repository.getProducts().collect { resource ->
                if (resource is Resource.Success && resource.data.isNullOrEmpty()) {
                    // Only seed if we successfully loaded but have no data
                    performHardReset()
                }
                _productsState.value = resource
            }
        }
    }

    fun filterByCategory(category: String) {
        _selectedCategory.value = category
        viewModelScope.launch {
            if (category == "All") {
                repository.getProducts().collect { _productsState.value = it }
            } else {
                repository.getProductsByCategory(category).collect { _productsState.value = it }
            }
        }
    }
    
    // forceful reset to ensure bad data is gone
    fun performHardReset() {
        android.util.Log.d("ProductViewModel", "PERFORMING HARD RESET OF DATA...")
        viewModelScope.launch {
            try {
                // 0. Clear local DB first to remove stale data with old IDs
                repository.clearLocalData()
                
                // 1. Delete all existing products in Firestore
                val snapshot = firestore.collection("products").get().await()
                if (!snapshot.isEmpty) {
                    val batch = firestore.batch()
                    snapshot.documents.forEach { doc ->
                        batch.delete(doc.reference)
                    }
                    batch.commit().await()
                    android.util.Log.d("ProductViewModel", "Deleted ${snapshot.size()} existing products from Firestore")
                }

                // 2. Add fresh sample data with reliable URLs
                val sampleProducts = listOf(
                Product(
                    name = "Sony WH-1000XM5",
                    description = "Industry-leading noise canceling headphones with Auto NC Optimizer",
                    price = 85000.0,
                    category = ProductCategory.ELECTRONICS,
                    imageUrl = "https://m.media-amazon.com/images/I/51SKmu2G9FL._AC_SL1000_.jpg",
                    rating = 4.8,
                    stock = 15
                ),
                Product(
                    name = "Samsung Galaxy S24 Ultra",
                    description = "AI-powered smartphone with 200MP camera and S Pen",
                    price = 450000.0,
                    category = ProductCategory.ELECTRONICS,
                    imageUrl = "https://m.media-amazon.com/images/I/71OptuZh81L._AC_SX679_.jpg", 
                    rating = 4.9,
                    stock = 10
                ),
                Product(
                    name = "Atlas Chooty Pen Blue (Box)",
                    description = "Box of 20 Atlas Chooty pens, smooth writing for students",
                    price = 800.0,
                    category = ProductCategory.STATIONERY,
                    imageUrl = "https://m.media-amazon.com/images/I/61Nl-eJ0XlL._AC_SX679_.jpg", // Generic Pen Box
                    rating = 4.5,
                    stock = 100
                ),
                Product(
                    name = "Munchee Super Cream Cracker 490g",
                    description = "The favorite biscuit of Sri Lanka, perfect with tea",
                    price = 450.0,
                    category = ProductCategory.GROCERIES,
                    imageUrl = "https://m.media-amazon.com/images/I/81+3fL8uPGL._AC_SX679_.jpg", // Generic cracker
                    rating = 4.7,
                    stock = 50
                ),
                Product(
                    name = "Men's Batik Shirt",
                    description = "Traditional Sri Lankan Batik shirt, pure cotton, casual wear",
                    price = 4500.0,
                    category = ProductCategory.FASHION,
                    imageUrl = "https://m.media-amazon.com/images/I/61F9dH8u7GL._AC_UX569_.jpg", // Generic shirt
                    rating = 4.3,
                    stock = 25
                ),
                Product(
                    name = "LG 43-inch 4K Smart TV",
                    description = "Ultra HD LED Smart TV with webOS and Magic Remote",
                    price = 125000.0,
                    category = ProductCategory.ELECTRONICS,
                    imageUrl = "https://m.media-amazon.com/images/I/91t9pI+q+AL._AC_SX466_.jpg",
                    rating = 4.6,
                    stock = 8
                ),
                Product(
                    name = "Fresh Banana 1kg",
                    description = "Organic fresh bananas",
                    price = 250.0,
                    category = ProductCategory.FRUITS,
                    imageUrl = "https://m.media-amazon.com/images/I/51ebZJ+DR4L._AC_SX679_.jpg",
                    rating = 4.8,
                    stock = 50
                ),
                Product(
                    name = "Red Apple",
                    description = "Fresh red apples imported",
                    price = 120.0,
                    category = ProductCategory.FRUITS,
                    imageUrl = "https://m.media-amazon.com/images/I/71TwXw2L+aL._AC_SX679_.jpg",
                    rating = 4.9,
                    stock = 30
                ),
                Product(
                    name = "Bell Pepper",
                    description = "Fresh green and red bell peppers",
                    price = 300.0,
                    category = ProductCategory.VEGETABLES,
                    imageUrl = "https://m.media-amazon.com/images/I/611wByv+T+L._AC_SX679_.jpg",
                    rating = 4.5,
                    stock = 20
                ),
                Product(
                    name = "Fresh Milk 1L",
                    description = "Highland Fresh Milk",
                    price = 450.0,
                    category = ProductCategory.MILK_AND_EGG,
                    imageUrl = "https://m.media-amazon.com/images/I/81xD+-F+1bL._AC_SX679_.jpg",
                    rating = 4.7,
                    stock = 50
                ),
                Product(
                    name = "Orange Juice",
                    description = "Freshly squeezed orange juice",
                    price = 600.0,
                    category = ProductCategory.BEVERAGES,
                    imageUrl = "https://m.media-amazon.com/images/I/71mXkF+e6+L._AC_SX679_.jpg",
                    rating = 4.6,
                    stock = 15
                ),
                Product(
                    name = "Surf Excel 1kg",
                    description = "Washing powder for laundry",
                    price = 850.0,
                    category = ProductCategory.LAUNDRY,
                    imageUrl = "https://m.media-amazon.com/images/I/715w+7gE25L._AC_SX679_.jpg",
                    rating = 4.8,
                    stock = 40
                ),
                Product(
                    name = "Farm Eggs (10 Pack)",
                    description = "Fresh brown eggs",
                    price = 650.0,
                    category = ProductCategory.MILK_AND_EGG,
                    imageUrl = "https://m.media-amazon.com/images/I/81a+s+9F+JL._AC_SX679_.jpg",
                    rating = 4.5,
                    stock = 60
                )
            )

                // Add all individually (simpler than batch for small list)
                sampleProducts.forEach { product ->
                    val ref = firestore.collection("products").add(product).await()
                    android.util.Log.d("ProductViewModel", "Added fresh product: ${product.name}")
                }
                
                // 3. Refresh Local DB
                android.util.Log.d("ProductViewModel", "Reset complete, refreshing local DB...")
                repository.refreshProducts()

            } catch (e: Exception) {
                android.util.Log.e("ProductViewModel", "Error performing hard reset", e)
                e.printStackTrace()
            }
        }
    }
}
