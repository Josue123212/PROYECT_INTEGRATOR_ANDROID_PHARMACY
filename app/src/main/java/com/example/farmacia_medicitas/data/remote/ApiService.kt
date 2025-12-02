package com.example.farmacia_medicitas.data.remote

import com.example.farmacia_medicitas.data.remote.dto.PaginatedResponse
import com.example.farmacia_medicitas.data.remote.dto.ProductDto
import com.example.farmacia_medicitas.data.remote.dto.CategoryDto
import com.example.farmacia_medicitas.data.remote.dto.auth.UserDto
import com.example.farmacia_medicitas.data.remote.dto.auth.MeEnvelope
import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.Response
import com.example.farmacia_medicitas.data.remote.dto.cart.AddItemRequest
import com.example.farmacia_medicitas.data.remote.dto.cart.CartItemDto
import com.example.farmacia_medicitas.data.remote.dto.cart.CartDto
import retrofit2.http.Headers
import com.example.farmacia_medicitas.data.remote.dto.stripe.CheckoutStripeResponse
import com.example.farmacia_medicitas.data.remote.dto.stripe.StripeConfirmRequest
import com.example.farmacia_medicitas.data.remote.dto.stripe.StripeConfirmResponse
import com.example.farmacia_medicitas.data.remote.dto.order.OrderDto
import com.example.farmacia_medicitas.data.remote.dto.order.OrderDetailDto

interface ApiService {
    @GET("ecommerce/products/")
    suspend fun getProducts(
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null,
        @Query("search") search: String? = null,
        @Query("category") categoryId: Int? = null
    ): PaginatedResponse<ProductDto>

    @GET("ecommerce/products/{id}/")
    suspend fun getProduct(
        @Path("id") id: Int
    ): ProductDto

    @GET("ecommerce/categories/")
    suspend fun getCategories(): PaginatedResponse<CategoryDto>

    @GET("ecommerce/cart/")
    suspend fun getCart(): CartDto

    @POST("ecommerce/cart/items/")
    suspend fun addCartItem(@Body body: AddItemRequest): CartItemDto

    @POST("ecommerce/cart/clear/")
    suspend fun clearCart(): Response<Unit>

    @POST("ecommerce/checkout/")
    suspend fun checkout(): Response<Unit>

    @POST("ecommerce/checkout/stripe/")
    suspend fun checkoutStripe(): CheckoutStripeResponse

    @POST("ecommerce/stripe/confirm/")
    suspend fun stripeConfirm(@Body body: StripeConfirmRequest): StripeConfirmResponse

    @GET("ecommerce/orders/")
    suspend fun getOrders(@Query("page") page: Int? = null): PaginatedResponse<OrderDto>

    @GET("ecommerce/orders/{id}/")
    suspend fun getOrder(@Path("id") id: Int): OrderDetailDto

    // Auth endpoints
    @POST("users/auth/login/")
    suspend fun login(@Body body: com.example.farmacia_medicitas.data.remote.dto.auth.LoginRequest): com.example.farmacia_medicitas.data.remote.dto.auth.LoginEnvelope

    @POST("users/auth/register/")
    suspend fun register(@Body body: com.example.farmacia_medicitas.data.remote.dto.auth.RegisterRequest): com.example.farmacia_medicitas.data.remote.dto.auth.RegisterEnvelope

    @POST("users/auth/refresh/")
    suspend fun refresh(@Body body: com.example.farmacia_medicitas.data.remote.dto.auth.RefreshRequest): com.example.farmacia_medicitas.data.remote.dto.auth.RefreshResponse

    // Protected endpoint: current authenticated user (según backend: /api/users/me/)
    @GET("users/me/")
    suspend fun me(): MeEnvelope
}
