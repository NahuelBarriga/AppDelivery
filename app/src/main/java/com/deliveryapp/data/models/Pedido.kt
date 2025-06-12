package com.deliveryapp.data.models

data class Pedido(
    val id: Int,
    val nombreCliente: String,
    val listaItems: List<Item>,
    val estado: String,
    val total: Double
)

data class Item(
    val id: Int,
    val nombre: String,
    val cantidad: Int,
    val precio: Double
)