package com.example.farmacia_medicitas.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.farmacia_medicitas.data.remote.ApiService
import com.example.farmacia_medicitas.data.remote.dto.order.OrderDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val api: ApiService
) : ViewModel() {
    private val _orders = MutableStateFlow<List<OrderDto>>(emptyList())
    val orders: StateFlow<List<OrderDto>> = _orders

    fun load() {
        viewModelScope.launch {
            runCatching { api.getOrders() }.onSuccess { res ->
                _orders.value = res.results ?: emptyList()
            }
        }
    }
}

