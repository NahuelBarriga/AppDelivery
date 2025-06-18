package com.deliveryapp.repository
import android.util.Log
import com.deliveryapp.data.api.ApiService
import com.deliveryapp.data.api.RetrofitClient.apiService
import com.deliveryapp.data.models.Usuario

class UsuarioRepository(private val apiService: ApiService) {

    suspend fun login(username: String, password: String): Usuario {
        return try {
            val credentials = mapOf("username" to username, "password" to password)
            apiService.login(credentials)
        } catch (e: Exception) {
            Log.e("UsuarioRepository", "Error durante login: ${e.message}", e)
            throw e
        }
    }

}
