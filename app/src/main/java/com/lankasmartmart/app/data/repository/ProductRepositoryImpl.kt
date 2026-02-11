package com.lankasmartmart.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.lankasmartmart.app.data.local.dao.ProductDao
import com.lankasmartmart.app.data.local.entity.ProductEntity
import com.lankasmartmart.app.data.model.Product
import com.lankasmartmart.app.data.model.ProductCategory
import com.lankasmartmart.app.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val productDao: ProductDao
) : ProductRepository {

    override fun getProducts(): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading())
        
        try {
            // Observe local DB
            productDao.getAllProducts().collect { entities ->
                val products = entities.map { it.toProduct() }
                android.util.Log.d("ProductRepository", "Emitting ${products.size} products from local DB")
                emit(Resource.Success(products))
            }
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "Error reading local DB", e)
            emit(Resource.Error(e.message ?: "Local DB Error"))
        }
    }

    override fun getProductsByCategory(category: String): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading())
        try {
            // Fetch from Firestore
            val snapshot = firestore.collection("products")
                .whereEqualTo("category", category)
                .get()
                .await()
            
            val products = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Product::class.java)?.copy(productId = doc.id)
            }
            emit(Resource.Success(products))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to fetch products"))
        }
    }

    override suspend fun getProductById(productId: String): Resource<Product> {
        return try {
            // Try local first
            val localEntity = productDao.getProductById(productId)
            if (localEntity != null) {
                return Resource.Success(localEntity.toProduct())
            }
            
            // Try remote if missing
            val doc = firestore.collection("products").document(productId).get().await()
            val product = doc.toObject(Product::class.java)?.copy(productId = doc.id)
            
            if (product != null) {
                Resource.Success(product)
            } else {
                Resource.Error("Product not found")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error fetching product")
        }
    }

    override suspend fun searchProducts(query: String): Resource<List<Product>> {
        // Simple client-side search or firestore simple search
        // Firestore doesn't support native full-text search without extensions (Algolia/Elastic)
        // We will do a basic startAt/endAt or use local room FTS
        return try {
           // For assignment simplicity, let's use Room's search
           // But ProductDao search returns a Flow. We need to collect or change DAO.
           // Let's implement cloud fetch for simplicity of "live" results if online
           val snapshot = firestore.collection("products")
               .whereGreaterThanOrEqualTo("name", query)
               .whereLessThanOrEqualTo("name", query + "\uf8ff")
               .get()
               .await()
               
           val products = snapshot.documents.mapNotNull { it.toObject(Product::class.java)?.copy(productId = it.id) }
           Resource.Success(products)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Search failed")
        }
    }

    override suspend fun refreshProducts(): Result<Unit> {
        return try {
            android.util.Log.d("ProductRepository", "Refreshing products from Firestore...")
            val snapshot = firestore.collection("products").get().await()
            android.util.Log.d("ProductRepository", "Firestore fetched ${snapshot.size()} documents")
            
            val products = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Product::class.java)?.copy(productId = doc.id)
            }
            
            // Save to local DB
            val entities = products.map { it.toEntity() }
            productDao.insertProducts(entities)
            android.util.Log.d("ProductRepository", "Inserted ${entities.size} products into local DB")
            
            Result.success(Unit)
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "Error refreshing products", e)
            Result.failure(e)
        }
    }

    override suspend fun clearLocalData() {
        try {
           productDao.deleteAllProducts()
           android.util.Log.d("ProductRepository", "Cleared local database")
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "Error clearing local DB", e)
        }
    }

    // Mappers
    private fun ProductEntity.toProduct(): Product {
        return Product(
            productId = productId,
            name = name,
            description = description,
            price = price,
            category = try { ProductCategory.valueOf(category) } catch (e: Exception) { ProductCategory.OTHER },
            imageUrl = imageUrl,
            rating = rating,
            stock = stock
        )
    }

    private fun Product.toEntity(): ProductEntity {
        return ProductEntity(
            productId = productId,
            name = name,
            description = description,
            price = price,
            category = category.name,
            imageUrl = imageUrl,
            rating = rating,
            stock = stock
        )
    }
}
