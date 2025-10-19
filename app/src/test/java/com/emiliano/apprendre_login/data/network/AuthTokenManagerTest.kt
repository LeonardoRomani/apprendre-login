package com.emiliano.apprendre_login.api

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Test de l'objecte AuthTokenManager.
 * Comprova la gestió de tokens en memòria.
 */
class AuthTokenManagerTest {

    @Before
    fun setUp() {
        // Assegurem que cada test comenci sense token
        AuthTokenManager.clearToken()
    }

    @Test
    fun setToken_and_getToken_returnsCorrectValue() {
        val testToken = "myTestToken123"

        // Guardem el token
        AuthTokenManager.setToken(testToken)

        // Recuperem el token i comprovem que és correcte
        val retrievedToken = AuthTokenManager.getToken()
        assertNotNull(retrievedToken)
        assertEquals(testToken, retrievedToken)
    }

    @Test
    fun clearToken_removesToken() {
        val testToken = "myTestToken123"
        AuthTokenManager.setToken(testToken)

        // Esborrem el token
        AuthTokenManager.clearToken()

        // Comprovem que el token ja no existeix
        val retrievedToken = AuthTokenManager.getToken()
        assertNull(retrievedToken)
    }

    @Test
    fun getToken_whenNoTokenSet_returnsNull() {
        // Comprovem que si no hem guardat cap token, retorna null
        val retrievedToken = AuthTokenManager.getToken()
        assertNull(retrievedToken)
    }
}