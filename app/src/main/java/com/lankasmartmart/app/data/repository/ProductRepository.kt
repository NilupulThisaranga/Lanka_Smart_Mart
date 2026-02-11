package com.lankasmartmart.app.data.repository

import com.lankasmartmart.app.data.model.Product
import com.lankasmartmart.app.util.Resource
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(): Flow<Resource<List<Product>>>
    
    fun getProductsByCategory(category: String): Flow<Resource<List<Product>>>
    
    suspend fun getProductById(productId: String): Resource<Product>
    
    suspend fun searchProducts(query: String): Resource<List<Product>>
    
    suspend fun refreshProducts(): Result<Unit>
    
    suspend fun clearLocalData()
}
