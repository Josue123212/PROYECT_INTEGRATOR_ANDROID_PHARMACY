package com.example.farmacia_medicitas.data.remote.dto.stripe

data class StripeOrderDto(
    val id: Int
)

data class CheckoutStripeResponse(
    val client_secret: String,
    val order: StripeOrderDto
)

data class StripeConfirmRequest(
    val order_id: Int,
    val payment_intent_id: String
)

data class ConfirmOrderDto(
    val id: Int,
    val status: String,
    val total: Double,
    val currency: String
)

data class ConfirmPaymentDto(
    val provider: String,
    val status: String,
    val external_id: String?
)

data class StripeConfirmResponse(
    val order: ConfirmOrderDto?,
    val payment: ConfirmPaymentDto?
)

