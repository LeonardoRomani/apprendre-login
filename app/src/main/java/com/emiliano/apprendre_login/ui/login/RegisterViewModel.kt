package com.emiliano.apprendre_login.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emiliano.apprendre_login.api.ApiService
import com.emiliano.apprendre_login.data.model.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel encarregat de gestionar el registre d'usuaris.
 * S'encarrega de cridar l'API i exposar l'estat del registre a la UI.
 */
class RegisterViewModel : ViewModel() {

    // Instància del servei d'API per fer les crides de registre
    private val apiService = ApiService.create()

    // Estat mutable intern per guardar missatges de registre
    private val _registerState = MutableStateFlow<String?>(null)

    // Estat públic exposat com a StateFlow per ser observat per la UI
    val registerState: StateFlow<String?> = _registerState

    /**
     * Funció que envia les dades de registre a l'API.
     * @param request: objecte RegisterRequest amb les dades de l'usuari
     */
    fun register(request: RegisterRequest) {
        // Llançar la crida dins el viewModelScope per a que sigui cancellable
        viewModelScope.launch {
            try {
                // Crida síncrona a l'API per registrar l'usuari
                val response = apiService.register(request)

                // Comprovem si la resposta de l'API és correcta
                if (response.isSuccessful) {
                    // Missatge d'èxit exposat a la UI
                    _registerState.value = "Usuari creat exitosamente"
                } else {
                    // Missatge d'error amb codi de resposta
                    _registerState.value = "Error al registrar usuari: ${response.code()}"
                }
            } catch (e: Exception) {
                // Captura excepcions de connexió o errors de xarxa
                _registerState.value = "Error de connexió: ${e.message}"
            }
        }
    }
}
