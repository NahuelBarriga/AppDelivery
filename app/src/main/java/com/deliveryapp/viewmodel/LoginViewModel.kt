package com.deliveryapp.viewmodel

import androidx.annotation.OptIn
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.deliveryapp.data.models.Usuario
import com.deliveryapp.repository.UsuarioRepository
import kotlinx.coroutines.launch
import com.deliveryapp.data.SesionUsuario

// Estados del resultado del login
sealed class LoginResult {
    data class Success(val usuario: Usuario) : LoginResult()
    data class Error(val message: String) : LoginResult()
    object Loading : LoginResult()
}

class LoginViewModel(private val repository: UsuarioRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    @OptIn(UnstableApi::class)
    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = LoginResult.Loading

            try {
                val usuario = repository.login(username, password)
                SesionUsuario.idUsuario = usuario.id
                _loginResult.value = LoginResult.Success(usuario)
            } catch (e: Exception) {
                _loginResult.value = LoginResult.Error("Login fallido: ${e.message}")
            }
        }
    }
}
