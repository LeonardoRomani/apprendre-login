package com.emiliano.apprendre_login.util

import android.content.Context
import android.content.SharedPreferences

/**
 * Classe per gestionar el token JWT de l'aplicació.
 * S'utilitza SharedPreferences per emmagatzemar, recuperar i eliminar el token.
 */
class TokenManager(context: Context) {

    // Accés a les preferències de l'aplicació amb nom "aprendre_prefs"
    private val prefs: SharedPreferences =
        context.getSharedPreferences("aprendre_prefs", Context.MODE_PRIVATE)

    /**
     * Desa el token JWT a les preferències.
     * @param token Token JWT a desar
     */
    fun saveToken(token: String) {
        prefs.edit().putString("JWT_TOKEN", token).apply()
    }

    /**
     * Recupera el token JWT desat.
     * @return El token JWT si existeix, si no retorna null
     */
    fun getToken(): String? {
        return prefs.getString("JWT_TOKEN", null)
    }

    /**
     * Elimina el token JWT de les preferències.
     */
    fun clearToken() {
        prefs.edit().remove("JWT_TOKEN").apply()
    }
}

