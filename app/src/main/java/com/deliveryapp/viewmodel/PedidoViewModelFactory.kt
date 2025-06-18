package com.deliveryapp.viewmodel

import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.util.UnstableApi
import com.deliveryapp.repository.PedidoRepository

class PedidoViewModelFactory(private val repo: PedidoRepository) : ViewModelProvider.Factory {
    @OptIn(UnstableApi::class)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PedidoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PedidoViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
