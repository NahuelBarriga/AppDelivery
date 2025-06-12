package com.deliveryapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deliveryapp.data.models.Pedido
import com.deliveryapp.repository.PedidoRepository
import kotlinx.coroutines.launch

class PedidoViewModel(private val repo: PedidoRepository) : ViewModel() {

    private val _pedidos = MutableLiveData<List<Pedido>>()
    val pedidos: LiveData<List<Pedido>> get() = _pedidos

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun cargarPedidos() {
        viewModelScope.launch {
            try {
                val lista = repo.obtenerPedidosDesdeApi()
                _pedidos.postValue(lista)
            } catch (e: Exception) {
                _error.postValue("No se pudieron cargar los pedidos: ${e.message}")
            }
        }
    }

    fun cambiarEstadoPedido(idPedido: Int, nuevoEstado: String) {
        viewModelScope.launch {
            try {
                val exito = repo.actualizarEstadoPedidoApi(idPedido, nuevoEstado)
                if (exito) {
                    // Opcional: recargar lista o actualizar localmente
                    cargarPedidos()
                } else {
                    _error.postValue("Error al actualizar estado")
                }
            } catch (e: Exception) {
                _error.postValue("Falló la petición: ${e.message}")
            }
        }
    }
}