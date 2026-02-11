package com.lankasmartmart.app.data.model

import com.google.firebase.Timestamp

data class Order(
    val orderId: String = "",
    val userId: String = "",
    val items: List<OrderItem> = emptyList(),
    val subtotal: Double = 0.0,
    val discount: Double = 0.0,
    val deliveryFee: Double = 0.0,
    val totalAmount: Double = 0.0,
    val status: OrderStatus = OrderStatus.PENDING,
    val orderDate: Timestamp = Timestamp.now(),
    val deliveryAddress: DeliveryAddress = DeliveryAddress(),
    val paymentMethod: PaymentMethod = PaymentMethod.CASH_ON_DELIVERY
)

data class OrderItem(
    val productId: String = "",
    val productName: String = "",
    val productImage: String = "",
    val quantity: Int = 1,
    val price: Double = 0.0,
    val total: Double = 0.0
)

data class DeliveryAddress(
    val fullName: String = "",
    val phoneNumber: String = "",
    val addressLine1: String = "",
    val addressLine2: String = "",
    val city: String = "",
    val postalCode: String = ""
)

enum class OrderStatus(val displayName: String) {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    PROCESSING("Processing"),
    SHIPPED("Shipped"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled")
}

enum class PaymentMethod(val displayName: String) {
    CASH_ON_DELIVERY("Cash on Delivery"),
    CREDIT_CARD("Credit Card"),
    DEBIT_CARD("Debit Card"),
    MOBILE_PAYMENT("Mobile Payment")
}
