package com.lankasmartmart.app.presentation.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lankasmartmart.app.data.model.Product
import com.lankasmartmart.app.data.local.entity.CartItemEntity
import com.lankasmartmart.app.data.repository.CartRepository
import com.lankasmartmart.app.data.repository.ProductRepository
import com.lankasmartmart.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val cartRepository: CartRepository,
    private val auth: com.google.firebase.auth.FirebaseAuth
) : ViewModel() {

    private val _productState = MutableStateFlow<Resource<Product>>(Resource.Loading())
    val productState: StateFlow<Resource<Product>> = _productState.asStateFlow()

    fun loadProduct(productId: String) {
        viewModelScope.launch {
            _productState.value = Resource.Loading()
            _productState.value = repository.getProductById(productId)
        }
    }
    
    fun addToCart(product: Product) {
        val user = auth.currentUser
        val userId = user?.uid ?: "guest" // Or handle not logged in
        
        viewModelScope.launch {
            val cartItem = CartItemEntity(
                cartItemId = java.util.UUID.randomUUID().toString(),
                productId = product.productId,
                productName = product.name,
                productImage = product.imageUrl,
                productPrice = product.price,
                quantity = 1,
                userId = userId
            )
            cartRepository.addToCart(cartItem)
        }
    }
}
