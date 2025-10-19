package com.emiliano.apprendre_login.data.model

import com.google.gson.annotations.SerializedName

/**
 * Classe de dades que representa la petició d'inici de sessió cap a l'API.
 *
 * Aquesta classe encapsula les credencials necessàries per autenticar un usuari
 * al sistema. S'utilitza per serialitzar les dades a format JSON quan es fa
 * una petició POST al endpoint d'autenticació.
 *
 * Les anotacions @SerializedName asseguren que els noms dels camps en JSON
 * coincideixin amb els esperats per l'API del servidor.
 *
 * @property username Nom d'usuari per a l'autenticació
 * @property password Contrasenya de l'usuari
 */
data class LoginRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String

)