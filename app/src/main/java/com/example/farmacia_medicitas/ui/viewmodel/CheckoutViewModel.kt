package com.example.farmacia_medicitas.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.farmacia_medicitas.core.Result
import com.example.farmacia_medicitas.data.remote.ApiService
import com.example.farmacia_medicitas.data.cart.CartRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.farmacia_medicitas.data.remote.dto.stripe.CheckoutStripeResponse
import com.example.farmacia_medicitas.data.remote.dto.stripe.StripeConfirmRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val api: ApiService,
    private val cartRepo: CartRepository
) : ViewModel() {

    data class CheckoutUiState(
        val clientSecret: String? = null,
        val orderId: Int? = null,
        val confirming: Boolean = false,
        val error: String? = null
    )
    private val _ui = MutableStateFlow(CheckoutUiState())
    val ui: StateFlow<CheckoutUiState> = _ui

    fun confirm(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val resp = api.checkout()
                if (resp.isSuccessful) onSuccess() else onError("Error en checkout: ${resp.code()}")
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "Error de red en checkout")
            }
        }
    }

    fun startStripeCheckout(onReady: (clientSecret: String, orderId: Int) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                cartRepo.syncRemote()
                val res: CheckoutStripeResponse = api.checkoutStripe()
                _ui.value = _ui.value.copy(clientSecret = res.client_secret, orderId = res.order.id, error = null)
                onReady(res.client_secret, res.order.id)
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "Error creando PaymentIntent")
            }
        }
    }

    fun confirmStripe(orderId: Int, paymentIntentId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val resp = api.stripeConfirm(StripeConfirmRequest(order_id = orderId, payment_intent_id = paymentIntentId))
                val ok = resp.order?.status == "confirmed" || resp.payment?.status == "succeeded"
                if (ok) {
                    onSuccess()
                } else {
                    onError("Pago no confirmado: ${resp.payment?.status ?: resp.order?.status ?: ""}")
                }
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "Error confirmando pago")
            }
        }
    }

    fun waitForConfirmation(orderId: Int, paymentIntentId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            _ui.value = _ui.value.copy(confirming = true, error = null)
            var attempts = 0
            while (attempts < 3) {
                try {
                    val resp = api.stripeConfirm(StripeConfirmRequest(order_id = orderId, payment_intent_id = paymentIntentId))
                    val ok = resp.order?.status == "confirmed" || resp.payment?.status == "succeeded"
                    if (ok) {
                        _ui.value = _ui.value.copy(confirming = false)
                        onSuccess()
                        return@launch
                    }
                } catch (_: Exception) { }
                attempts++
                delay(1200)
            }
            _ui.value = _ui.value.copy(confirming = false, error = "Confirmación pendiente")
            onError("Confirmación pendiente")
        }
    }

    fun setError(message: String?) {
        _ui.value = _ui.value.copy(error = message)
    }
}
