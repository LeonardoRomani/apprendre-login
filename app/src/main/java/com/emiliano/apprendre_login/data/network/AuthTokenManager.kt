package com.emiliano.apprendre_login.api

/**
 * Objecte singleton per gestionar el token d'autenticació.
 * Guarda el token en memòria mentre l'aplicació està activa.
 */
object AuthTokenManager {

    // Variable interna que conté el token. Inicialment és null.
    private var authToken: String? = null

    /**
     * Guarda el token proporcionat.
     * @param token: token d'autenticació a guardar
     */
    fun setToken(token: String) {
        authToken = token
        println(
            "DEBUG - AuthTokenManager: Token guardat - ${
                if (token.isNotBlank()) "${token.take(10)}..." else "BLANK/EMPTY"
            }"
        )
    }

    /**
     * Recupera el token actual.
     * @return token si existeix, o null si no hi ha token guardat
     */
    fun getToken(): String? {
        val token = authToken
        println(
            "DEBUG - AuthTokenManager: Token recuperat - ${
                if (!token.isNullOrBlank()) "${token.take(10)}..." else "NULL/BLANK"
            }"
        )
        return token
    }

    /**
     * Esborra el token guardat.
     */
    fun clearToken() {
        authToken = null
        println("DEBUG - AuthTokenManager: Token esborrat")
    }
}