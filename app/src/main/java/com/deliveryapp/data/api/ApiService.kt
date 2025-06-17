package com.deliveryapp.data.api

import com.deliveryapp.data.models.Pedido
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ApiService {
    @GET("pedidos/delivery")
    suspend fun obtenerPedidos(): Response<List<Pedido>>

    @PATCH("pedidos/{id}")
    suspend fun actualizarPedido(
        @Path("id") idPedido: Int,
        @Body body: Map<String, String>  // o un DTO específico para actualizar estado
    ): Response<Unit>
}