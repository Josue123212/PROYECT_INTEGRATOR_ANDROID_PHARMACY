package com.example.farmacia_medicitas.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.farmacia_medicitas.data.remote.ApiService
import com.example.farmacia_medicitas.data.remote.dto.order.OrderItemDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OrderDetailState(
    val status: String? = null,
    val total: Double? = null,
    val currency: String? = null,
    val items: List<OrderItemDto> = emptyList()
)

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val api: ApiService
) : ViewModel() {
    var state: OrderDetailState = OrderDetailState()
        private set

    fun load(id: Int) {
        viewModelScope.launch {
            runCatching { api.getOrder(id) }.onSuccess { d ->
                state = OrderDetailState(
                    status = d.status,
                    total = d.total,
                    currency = d.currency,
                    items = d.items ?: emptyList()
                )
            }
        }
    }
}

