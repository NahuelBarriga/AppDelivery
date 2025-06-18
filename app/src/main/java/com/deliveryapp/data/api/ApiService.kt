package com.deliveryapp.data.api

import com.deliveryapp.data.models.Pedido
import com.deliveryapp.data.models.PedidoRequest
import com.deliveryapp.data.models.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.POST

interface ApiService {
    @GET("pedidos/delivery")
    suspend fun obtenerPedidos(): Response<List<Pedido>>

    @PATCH("pedidos/delivery/{id}")
    suspend fun actualizarPedido(
        @Path("id") idPedido: Int,
        @Body body: PedidoRequest  // o un DTO específico para actualizar estado
    ): Response<Unit>

    @POST("auth/login")
    suspend fun login(@Body credential: Map<String, String>): Usuario
}