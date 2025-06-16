package com.deliveryapp.repository
import com.deliveryapp.data.models.Usuario

class UsuarioRepository(

    private val usuariosDePrueba: List<Usuario> = listOf(
        Usuario(id = 1, username = "Juan", password = "Test123"),
        Usuario(id = 2, username = "Pedro", password = "Test123")
    )



)