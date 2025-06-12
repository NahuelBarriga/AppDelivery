package com.deliveryapp.data.api

import com.deliveryapp.data.models.Pedido
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("pedidos")
    suspend fun obtenerPedidos(): Response<List<Pedido>>

    @PUT("pedidos/{id}")
    suspend fun actualizarPedido(
        @Path("id") idPedido: Int,
        @Body body: Map<String, String>  // o un DTO específico para actualizar estado
    ): Response<Unit>
}