package com.emiliano.apprendre_login.data.model

import com.google.gson.annotations.SerializedName

// Data class que representa la petició per crear un nou usuari a l'API
// Aquesta informació s'envia en format JSON quan un usuari es registra
data class RegisterRequest(
    @SerializedName("username") val username: String,   // Nom d'usuari que vol registrar-se
    @SerializedName("email") val email: String,         // Correu electrònic de l'usuari
    @SerializedName("name") val name: String,           // Nom de l'usuari
    @SerializedName("last_name") val last_name: String, // Cognom de l'usuari
    @SerializedName("dni") val dni: String?,             // DNI o identificador personal
    @SerializedName("password") val password: String,   // Contrasenya que l'usuari vol utilitzar
    @SerializedName("phone") val phone: String?,            // Número de telèfon
    @SerializedName("role_id") val role_id: Int         // Rol assignat a l'usuari (1-Admin, 2-Teacher, 3-Teacher, 4-Parent)
)