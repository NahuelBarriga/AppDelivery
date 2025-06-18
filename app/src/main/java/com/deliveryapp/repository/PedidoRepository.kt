package com.deliveryapp.repository

import androidx.annotation.OptIn
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.deliveryapp.data.api.RetrofitClient
import com.deliveryapp.data.models.Pedido
import com.deliveryapp.data.models.PedidoRequest
import com.deliveryapp.data.SesionUsuario

@UnstableApi
class PedidoRepository {

    private val api = RetrofitClient.apiService

    // Devuelve la lista de pedidos o lanza excepción si algo falla
    @OptIn(UnstableApi::class)
    suspend fun obtenerPedidosDesdeApi(): List<Pedido> {
        Log.w("Pedidos", "llegue")
        try {
            val response = api.obtenerPedidos(1)
            if (response.isSuccessful) {
                Log.w("Pedidos", "llegue2")
                Log.w("Pedidos", "OK: ${response.body()}")
                return response.body() ?: emptyList()
            } else {
                Log.w("Pedidos", "me fui")
                Log.w(
                    "Pedidos",
                    "Error HTTP: ${response.code()} - ${response.errorBody()?.string()}"
                )
                throw Exception("Error ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.w("Pedidos", "${e.localizedMessage}")
            throw Exception("Error")
        }
    }

    suspend fun actualizarEstadoPedidoApi(idPedido: Int, nuevoEstado: String): Boolean {
        val body = PedidoRequest(estado = nuevoEstado, usuarioId = SesionUsuario.idUsuario)
        val response = api.actualizarPedido(idPedido, body)
        return response.isSuccessful
    }
}



