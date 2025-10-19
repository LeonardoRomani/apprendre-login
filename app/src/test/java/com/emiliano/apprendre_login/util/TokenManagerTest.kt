package com.emiliano.apprendre_login.util

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.robolectric.RobolectricTestRunner
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/**
 * Test per la classe TokenManager.
 * Prova desament, recuperació i eliminació del token.
 */
@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner::class)
class TokenManagerTest {

    private lateinit var tokenManager: TokenManager
    private lateinit var context: Context

    @Before
    fun setup() {
        // Context simulant l'aplicació
        context = ApplicationProvider.getApplicationContext()
        tokenManager = TokenManager(context)

        // Neteja inicial per assegurar test net
        tokenManager.clearToken()
    }

    @Test
    fun saveToken_savesAndGetToken_returnsToken() {
        val testToken = "test.jwt.token"

        tokenManager.saveToken(testToken)
        val retrieved = tokenManager.getToken()

        assertNotNull("El token recuperat no hauria de ser null", retrieved)
        assertEquals("El token recuperat ha de coincidir amb el desat", testToken, retrieved)
    }

    @Test
    fun getToken_whenNoToken_returnsNull() {
        val retrieved = tokenManager.getToken()
        assertNull("Si no hi ha token desat, ha de retornar null", retrieved)
    }

    @Test
    fun clearToken_removesToken() {
        val testToken = "token_a_eliminar"

        tokenManager.saveToken(testToken)
        tokenManager.clearToken()
        val retrieved = tokenManager.getToken()

        assertNull("Després d'eliminar, getToken ha de retornar null", retrieved)
    }
}