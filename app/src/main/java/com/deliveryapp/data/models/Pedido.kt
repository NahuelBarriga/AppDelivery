package com.deliveryapp.data.models

data class Pedido(
    val id: Int,
    val nombreCliente: String,
    val listaItems: List<Item>,
    var estado: String,
    val total: Double,
    val direccion: String,
    var expandido: Boolean = false
)

data class Item(
    val id: Int,
    val nombre: String,
    val cantidad: Int,
    val precio: Double
)