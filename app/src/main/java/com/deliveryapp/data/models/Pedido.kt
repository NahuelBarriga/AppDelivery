package com.deliveryapp.data.models

import java.sql.Timestamp

data class Pedido(
    val id: Int,
    val listaItems: List<Item>,
    var estado: String,
    val cliente: Cliente,
    var expandido: Boolean = false,
    val timestamp: Timestamp,
)

data class Item(
    val id: Int,
    val nombre: String,
    val cantidad: Int,
    val precio: Double
)

data class Cliente(
    val nombre: String,
    val direccion: String,
    val ciudad: String,
    val telefono: String,
)