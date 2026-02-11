package com.lankasmartmart.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lankasmartmart.app.data.local.dao.CartDao
import com.lankasmartmart.app.data.local.dao.ProductDao
import com.lankasmartmart.app.data.local.dao.WishlistDao
import com.lankasmartmart.app.data.local.entity.CartItemEntity
import com.lankasmartmart.app.data.local.entity.ProductEntity
import com.lankasmartmart.app.data.local.entity.WishlistItemEntity

@Database(
    entities = [
        ProductEntity::class,
        CartItemEntity::class,
        WishlistItemEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun wishlistDao(): WishlistDao
}
