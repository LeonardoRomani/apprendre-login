package com.emiliano.apprendre_login.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emiliano.apprendre_login.api.ApiService
import com.emiliano.apprendre_login.api.AuthTokenManager
import com.emiliano.apprendre_login.data.model.LoginRequest
import com.emiliano.apprendre_login.data.model.LoginResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

open class LoginViewModel : ViewModel() {

    private val apiService = ApiService.create()

    // Estat del login
    open val _loginState = MutableStateFlow<Result<LoginResponse>?>(null)
    open val loginState: StateFlow<Result<LoginResponse>?> = _loginState

    // Rol de l'usuari per navegació
    private val _userRole = MutableStateFlow<Int?>(null)
    open val userRole: StateFlow<Int?> = _userRole

    // Estat de càrrega
    private val _isLoading = MutableStateFlow(false)
    open val isLoading: StateFlow<Boolean> = _isLoading

    // Missatge d'error
    open val _errorMessage = MutableStateFlow<String?>(null)
    open val errorMessage: StateFlow<String?> = _errorMessage

    open fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _errorMessage.value = "Si us plau, omple tots els camps"
            return
        }

        _isLoading.value = true
        _errorMessage.value = null
        _loginState.value = null

        viewModelScope.launch {
            try {
                val response = apiService.login(LoginRequest(username, password))
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        // Guardar el token per a les futures peticions
                        AuthTokenManager.setToken(loginResponse.access_token ?: "")

                        // Actualitzar els estats
                        _loginState.value = Result.success(loginResponse)
                        _userRole.value = loginResponse.role
                    } else {
                        _errorMessage.value = "Resposta buida del servidor"
                        _loginState.value = Result.failure(Exception("Resposta buida"))
                    }
                } else {
                    val errorMsg = when (response.code()) {
                        401 -> "Credencials incorrectes"
                        403 -> "Accés denegat"
                        404 -> "Usuari no trobat"
                        else -> "Error del servidor: ${response.code()}"
                    }
                    _errorMessage.value = errorMsg
                    _loginState.value = Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                val errorMsg = "Error de connexió: ${e.message}"
                _errorMessage.value = errorMsg
                _loginState.value = Result.failure(Exception(errorMsg))
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun clearLoginState() {
        _loginState.value = null
    }
}

