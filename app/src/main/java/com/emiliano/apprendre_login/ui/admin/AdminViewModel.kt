package com.emiliano.apprendre_login.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emiliano.apprendre_login.api.ApiService
import com.emiliano.apprendre_login.api.AuthTokenManager
import com.emiliano.apprendre_login.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel per a la pantalla d'administrador.
 * - Carrega tots els usuaris
 * - Carrega l'usuari actual (getCurrentUser)
 * - Permet actualitzar i eliminar usuaris
 * - Gestiona l'estat de logout i diàlegs
 */
class AdminViewModel : ViewModel() {

    private val apiService = ApiService.create()

    private val _usersState = MutableStateFlow<List<User>>(emptyList())
    val usersState: StateFlow<List<User>> = _usersState

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _logoutState = MutableStateFlow(false)
    val logoutState: StateFlow<Boolean> = _logoutState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Diàlegs: usuari actual i edició d'un usuari
    private val _currentUserDialog = MutableStateFlow<User?>(null)
    val currentUserDialog: StateFlow<User?> = _currentUserDialog

    private val _userEditDialog = MutableStateFlow<User?>(null)
    val userEditDialog: StateFlow<User?> = _userEditDialog

    // Filtre de rol: 0 = Tots, 1 = Admin, 2 = Professor, 3 = Alumne
    private val _roleFilter = MutableStateFlow(0)
    val roleFilter: StateFlow<Int> = _roleFilter

    // Usuari actual per mostrar el seu nom a la barra superior
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    init {
        // Carrega dades inicials
        fetchCurrentUser()
        fetchAllUsers()
    }

    /**
     * Recupera informació de l'usuari autenticat
     */
    fun fetchCurrentUser() {
        viewModelScope.launch {
            try {
                val resp = apiService.getCurrentUser()
                if (resp.isSuccessful) {
                    _currentUser.value = resp.body()
                } else {
                    // no és crític: deixem el currentUser null i mostrem error si cal
                    _errorMessage.value = "Error obtenint dades de l'usuari: ${resp.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error connexió (current user): ${e.message}"
            }
        }
    }

    /**
     * Recupera tots els usuaris (GET /user/all)
     */
    fun fetchAllUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getAllUsers()
                if (response.isSuccessful) {
                    // L'API pot retornar UserResponse -> aquí assumim que ApiService ja retorna UserResponse
                    // i que has adaptat el model. Si retorna List<User> simplement assigna response.body()!!
                    val body = response.body()
                    val users = when {
                        // si el body és un objecte amb camps 'users' o 'data'
                        (body as? com.emiliano.apprendre_login.data.model.UserResponse)?.users != null ->
                            (body as com.emiliano.apprendre_login.data.model.UserResponse).users!!
                        (body as? com.emiliano.apprendre_login.data.model.UserResponse)?.data != null ->
                            (body as com.emiliano.apprendre_login.data.model.UserResponse).data!!
                        // si ja és List<User> (no esperat aquí), ho podries convertir
                        else -> {
                            // fallback: intentar cast segur
                            try {
                                @Suppress("UNCHECKED_CAST")
                                (body as? List<User>) ?: emptyList()
                            } catch (_: Exception) {
                                emptyList()
                            }
                        }
                    }
                    _usersState.value = users
                } else {
                    _errorMessage.value = "Error carregant usuaris: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de connexió: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Mostra el diàleg amb les dades del current user
     */
    fun showCurrentUserDialog(user: User) {
        _currentUserDialog.value = user
    }

    fun dismissCurrentUserDialog() {
        _currentUserDialog.value = null
    }

    fun showUserEditDialog(user: User) {
        _userEditDialog.value = user
    }

    fun dismissUserEditDialog() {
        _userEditDialog.value = null
    }

    /**
     * Actualitza un usuari (PUT /user/)
     * TODO: comprova que el backend accepti aquest payload exactament com s'envia
     */
    fun updateUser(
        userId: Int,
        username: String,
        name: String,
        lastName: String,
        email: String,
        phone: String,
        roleId: Int,
        password: String = "1234",
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val updateRequest = mapOf(
                    "user_id" to userId,
                    "username" to username,
                    "password" to password,
                    "name" to name,
                    "last_name" to lastName,
                    "email" to email,
                    "phone" to phone.toIntOrNull(),
                    "role_id" to roleId
                )

                val response = apiService.updateUser(updateRequest as Map<String, Any>)

                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Error al desar canvis: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                onError("Error de connexió: ${e.message}")
            }
        }
    }

    /**
     * Esborra un usuari (DELETE /user/{user_id})
     */
    fun deleteUser(userId: Int?, onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.deleteUser(userId)
                if (response.isSuccessful) {
                    fetchAllUsers()
                    onComplete()
                } else {
                    _errorMessage.value = "Error eliminant usuari: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de connexió: ${e.localizedMessage}"
            }
        }
    }

    /**
     * Fa logout (POST /auth/logout) i neteja token local
     */
    fun logout(onComplete: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.logout()
                // netegem localment sigui com sigui
                AuthTokenManager.clearToken()
                _logoutState.value = true
                onComplete()
            } catch (e: Exception) {
                // netejar localment igualment
                AuthTokenManager.clearToken()
                _logoutState.value = true
                onComplete()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setRoleFilter(roleId: Int) {
        _roleFilter.value = roleId
    }

    fun filteredUsers(): List<User> {
        val allUsers = _usersState.value
        return if (_roleFilter.value == 0) allUsers
        else allUsers.filter { it.role_id == _roleFilter.value }
    }

    fun setCurrentUser(user: User) {
        _currentUser.value = user
    }
}