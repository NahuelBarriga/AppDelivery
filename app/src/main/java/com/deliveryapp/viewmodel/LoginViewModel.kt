package com.deliveryapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deliveryapp.data.models.Usuario
import kotlinx.coroutines.delay // Para simular una llamada de red
import kotlinx.coroutines.launch

// Estado para el resultado del login
sealed class LoginResult {
    object Success : LoginResult()
    data class Error(val message: String) : LoginResult()
    object Loading : LoginResult()
}

class LoginViewModel : ViewModel() { // Si usas un Repository, lo inyectarías aquí

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    // Lista de usuarios hardcodeada para este ejemplo.
    // En una app real, esto vendría de un AuthRepository/DataSource.
    private val validUsers = listOf(
        Usuario(1, "Juan", "test123"),
        Usuario(2, "Pepe", "test123")
    )

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = LoginResult.Loading // Indicar que está cargando

            // Simular una pequeña demora como si fuera una llamada de red
            delay(1500)

            val user = validUsers.find { it.username == username }

            if (user != null && user.password == password) {
                _loginResult.value = LoginResult.Success
            } else {
                _loginResult.value = LoginResult.Error("Usuario o contraseña incorrectos.")
            }
        }
    }
}