/*package com.deliveryapp.repository

import com.deliveryapp.data.api.RetrofitClient
import com.deliveryapp.data.models.Pedido

class PedidoRepository {

    private val api = RetrofitClient.apiService

    // Devuelve la lista de pedidos o lanza excepción si algo falla
    suspend fun obtenerPedidosDesdeApi(): List<Pedido> {
        val response = api.obtenerPedidos()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Error ${response.code()}: ${response.message()}")
        }
    }

    suspend fun actualizarEstadoPedidoApi(idPedido: Int, nuevoEstado: String): Boolean {
        // Suponemos que el back espera un JSON { "estado": "ENTREGADO" }
        val body = mapOf("estado" to nuevoEstado)
        val response = api.actualizarPedido(idPedido, body)
        return response.isSuccessful
    }
}*/


package com.deliveryapp.repository

import com.deliveryapp.data.models.Pedido

class PedidoRepository {

    // Simula una lista de pedidos de prueba
    private val pedidosDePrueba = listOf(
        Pedido(id = 1, nombreCliente = "Nahuel", estado = "PENDIENTE", listaItems = emptyList(), total = 500.0, direccion = "Avellaneda 2112"),
        Pedido(id = 2, nombreCliente = "Lucía", estado = "EN PREPARACIÓN", listaItems = emptyList(), total = 500.0, direccion = "Independencia 2233"),
        Pedido(id = 3, nombreCliente = "Juan", estado = "ENTREGADO", listaItems = emptyList(), total = 500.0, direccion = "Libertad 1550")
    )

    suspend fun obtenerPedidosDesdeApi(): List<Pedido> {
        // Simula un pequeño retraso como si viniera de la red
        kotlinx.coroutines.delay(500)
        return pedidosDePrueba
    }

    suspend fun actualizarEstadoPedidoApi(idPedido: Int, nuevoEstado: String): Boolean {
        // Simula la actualización localmente
        println("Mock: Se actualiza el pedido $idPedido a estado '$nuevoEstado'")
        return true
    }
}
