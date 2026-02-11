package com.lankasmartmart.app.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.lankasmartmart.app.data.local.dao.CartDao
import com.lankasmartmart.app.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao,
    private val auth: FirebaseAuth
) : CartRepository {

    private val currentUserId: String
        get() = auth.currentUser?.uid ?: "guest"

    override fun getCartItems(): Flow<List<CartItemEntity>> {
        return cartDao.getCartItems(currentUserId)
    }

    override suspend fun addToCart(cartItem: CartItemEntity) {
        val userId = currentUserId
        val existingItem = cartDao.getCartItemByProductId(cartItem.productId, userId)
        if (existingItem != null) {
            cartDao.updateQuantity(cartItem.productId, existingItem.quantity + cartItem.quantity, userId)
        } else {
            cartDao.insertCartItem(cartItem.copy(userId = userId))
        }
    }

    override suspend fun removeFromCart(productId: String) {
        cartDao.deleteCartItem(productId, currentUserId)
    }

    override suspend fun updateQuantity(productId: String, quantity: Int) {
        cartDao.updateQuantity(productId, quantity, currentUserId)
    }

    override suspend fun clearCart() {
        cartDao.clearCart(currentUserId)
    }
}
