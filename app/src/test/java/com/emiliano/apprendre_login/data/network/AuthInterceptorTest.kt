package com.emiliano.apprendre_login.api

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Test de l'interceptor AuthInterceptor.
 * Comprova que els headers s'afegeixen correctament segons si hi ha token o no.
 */
class AuthInterceptorTest {

    private lateinit var server: MockWebServer
    private lateinit var client: OkHttpClient

    @Before
    fun setUp() {
        // Inicialitzem el servidor simulat
        server = MockWebServer()
        server.start()

        // Assegurem que no hi ha token previ
        AuthTokenManager.clearToken()

        // Creem un client amb l'interceptor a provar
        client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun intercept_withToken_addsAuthorizationHeader() {
        // Guardem un token
        val testToken = "myTestToken123"
        AuthTokenManager.setToken(testToken)

        // Fem una petició de prova al servidor simulat
        server.enqueue(MockResponse().setResponseCode(200))
        val request = Request.Builder()
            .url(server.url("/test"))
            .build()

        val response: Response = client.newCall(request).execute()

        // Comprovem que el request realitzat conté el header Authorization
        val recordedRequest = server.takeRequest()
        assertEquals("Bearer $testToken", recordedRequest.getHeader("Authorization"))

        // Comprovem que els headers comuns també estan presents
        assertEquals("application/json", recordedRequest.getHeader("Content-Type"))
        assertEquals("application/json", recordedRequest.getHeader("Accept"))

        // Comprovem que la resposta és correcta
        assertEquals(200, response.code)
    }

    @Test
    fun intercept_withoutToken_doesNotAddAuthorizationHeader() {
        // Assegurem que no hi ha token
        AuthTokenManager.clearToken()

        // Fem una petició de prova
        server.enqueue(MockResponse().setResponseCode(200))
        val request = Request.Builder()
            .url(server.url("/test"))
            .build()

        val response: Response = client.newCall(request).execute()

        // Comprovem el request
        val recordedRequest = server.takeRequest()

        // Header Authorization no ha d’existir
        assertNull(recordedRequest.getHeader("Authorization"))

        // Headers comuns sempre presents
        assertEquals("application/json", recordedRequest.getHeader("Content-Type"))
        assertEquals("application/json", recordedRequest.getHeader("Accept"))

        assertEquals(200, response.code)
    }
}